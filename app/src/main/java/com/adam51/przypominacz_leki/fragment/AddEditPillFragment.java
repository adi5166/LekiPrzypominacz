package com.adam51.przypominacz_leki.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.adapter.AlarmAdapter;
import com.adam51.przypominacz_leki.helper.TimePickerFragment;
import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.adapter.ImagePillAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentAddEditPillBinding;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.ImagePill;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.receiver.AlarmReceiver;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_STRING;

public class AddEditPillFragment extends Fragment
        implements TimePickerDialog.OnTimeSetListener, AlarmAdapter.OnAlarmListener {

  private FragmentAddEditPillBinding addEditPillBinding;
  private NavController navController;
  private ArrayList<ImagePill> imagePillList;
  private ImagePillAdapter adapter;
  private PillViewModel pillViewModel;
  //private EditPillViewModel editPillViewModel;
  private NotificationManagerCompat notificationManager;
  private AlarmViewModel alarmViewModel;
  private AlarmAdapter alarmAdapter;
  private boolean modeAlarm = false;
  private int new_pill_id;
  public static final int REQUEST_CODE = 11;

  public AddEditPillFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    addEditPillBinding = FragmentAddEditPillBinding.inflate(inflater, container, false);
    return addEditPillBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);
    pillViewModel = new ViewModelProvider(getActivity()).get(PillViewModel.class);
    //editPillViewModel = new ViewModelProvider(this).get(EditPillViewModel.class);

    setHasOptionsMenu(true);

    initSpinnerList();
    adapter = new ImagePillAdapter(getActivity(), imagePillList);
    addEditPillBinding.spinnerPillAdd.setAdapter(adapter);

    alarmAdapter = new AlarmAdapter(this);
    addEditPillBinding.alarmRecycle.setAdapter(alarmAdapter);

    alarmViewModel = new ViewModelProvider(getActivity()).get(AlarmViewModel.class);
    //TODO zabieranie częsci alarmów z bazy danych

    addEditPillBinding.switchPillRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) addEditPillBinding.radioGroupPill.setVisibility(View.VISIBLE);
        else addEditPillBinding.radioGroupPill.setVisibility(View.GONE);
      }
    });
    addEditPillBinding.switchPillHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          addEditPillBinding.alarmAddButton.setVisibility(View.VISIBLE);
          addEditPillBinding.alarmRecycle.setVisibility(View.VISIBLE);
        } else {
          addEditPillBinding.alarmAddButton.setVisibility(View.INVISIBLE);
          addEditPillBinding.alarmRecycle.setVisibility(View.GONE);
        }
      }
    });

    if (getArguments() != null) {
      String mode = AddEditPillFragmentArgs.fromBundle(getArguments()).getMode();
      if (mode.equals("Edit")) {
        addEditPillBinding.addPillButton.setVisibility(View.INVISIBLE);
        addEditPillBinding.editPillButton.setVisibility(View.VISIBLE);

        final int position = AddEditPillFragmentArgs.fromBundle(getArguments()).getPillPosition();
        if (position != -5) {
          Pill current_pill = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill();
          addEditPillBinding.pillDetailName.setText(current_pill != null ? current_pill.getName() : "");
          addEditPillBinding.pillDetailDescription.setText(current_pill != null ? current_pill.getDescription() : "");
          int spinner_pos = getSpinnerPosition(current_pill.getPicPath());
          if (spinner_pos != -1) {
            addEditPillBinding.spinnerPillAdd.setSelection(spinner_pos);
          }

          //TODO zastąpienie this getActivity()
          notificationManager = NotificationManagerCompat.from(getActivity());
          addEditPillBinding.alarmAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              modeAlarm = false;
              DialogFragment timePicker = new TimePickerFragment();
              timePicker.show(getChildFragmentManager(), "time picker");
            }
          });

          alarmViewModel.getAlarmFromPill(current_pill.getId()).observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
              alarmAdapter.setAlarms(alarms);
              for (int i = 0; i < alarmAdapter.getItemCount(); i++) {
                Alarm alarm = alarms.get(i);
                if (alarm.isActive() && !alarm.isSetup()) {
                  Log.d(TAG, "onChanged: on start");
                  startAlarm(i);
                  alarm.setSetup(true);
                  alarmViewModel.update(alarm);
                  break;
                } else {
                  if (!alarm.isActive() && alarm.isSetup()) {
                    Log.d(TAG, "onChanged: on cancel");
                    cancelAlarm(i);
                    alarm.setSetup(false);
                    alarmViewModel.update(alarm);
                    break;
                  }
                }
              }
              //TODO update alarmów
              Log.d(TAG, "onChanged: Update alarms ");

            }
          });
          //Util.SetPillImageView(getContext(), current_pill. ,addEditPillBinding.spinnerPillAdd);
        }

        addEditPillBinding.editPillButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            updatePill();
            Util.hideKeyboard(getContext(), v);
          }
        });
        Log.d(TAG, "onViewCreated: Edit mode");
      } else if (mode.equals("Add")) {
        addEditPillBinding.addPillButton.setVisibility(View.VISIBLE);
        addEditPillBinding.editPillButton.setVisibility(View.INVISIBLE);

        addEditPillBinding.addPillButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            savePill();
            Util.hideKeyboard(getContext(), v);
          }
        });

        final int position = AddEditPillFragmentArgs.fromBundle(getArguments()).getPillPosition();
        if (position >= 0) {
          Log.d(TAG, "onViewCreated: position: "+position);


          addEditPillBinding.alarmAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new_pill_id = pillViewModel.getAllPills().getValue().get(position).getId();
              modeAlarm = false;
              DialogFragment timePicker = new TimePickerFragment();
              timePicker.setTargetFragment(AddEditPillFragment.this,REQUEST_CODE);
              timePicker.show(getChildFragmentManager(), "time picker");
            }
          });

        }

        Log.d(TAG, "onViewCreated: Add mode");
      }
    }
  }

  private int getSpinnerPosition(String string) {
    for (int i = 0; i < imagePillList.size(); i++) {
      if (imagePillList.get(i).getName().equals(string))
        return i;
    }
    return -1;
  }

  private void initSpinnerList() {
    imagePillList = new ArrayList<>();
    imagePillList.add(new ImagePill("pill_oval_blue", R.drawable.pill_oval_blue));
    imagePillList.add(new ImagePill("pill_oval_orange", R.drawable.pill_oval_orange));
    imagePillList.add(new ImagePill("pill_oval_green", R.drawable.pill_oval_green));
  }

  private void savePill() {
    try {
      if (isDataValid()) {
        ImagePill imagePill = (ImagePill) addEditPillBinding.spinnerPillAdd.getSelectedItem();
        Pill pill = new Pill(addEditPillBinding.textInputName.getEditText().getText().toString(),
                addEditPillBinding.textInputDescription.getEditText().getText().toString(),
                imagePill.getName()
        );
        pillViewModel.insert(pill);
        //TODO czy tu mam id pill do Alarmu?

        Toast.makeText(getActivity(), "Pill saved", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
        return;
      }
      navController.navigate(AddEditPillFragmentDirections.actionPillSavedBackToRecycler());
    } catch (NullPointerException ex) {
      Toast.makeText(getActivity(), "Error while saving data", Toast.LENGTH_SHORT).show();
      navController.navigate(AddEditPillFragmentDirections.actionPillSavedBackToRecycler());
    }
  }

  public void updatePill() {
    if (isDataValid()) {
      if (getArguments() != null) {
        ImagePill imagePill = (ImagePill) addEditPillBinding.spinnerPillAdd.getSelectedItem();

        Pill pill = new Pill(addEditPillBinding.textInputName.getEditText().getText().toString(),
                addEditPillBinding.textInputDescription.getEditText().getText().toString(),
                imagePill.getName()
        );
        pill.setId(AddEditPillFragmentArgs.fromBundle(getArguments()).getPill().getId());
        pillViewModel.update(pill);
        Toast.makeText(getActivity(), "Pill updated", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getActivity(), "Error while updating data", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
      return;
    }
    navController.navigate(AddEditPillFragmentDirections.actionPillSavedBackToRecycler());
  }

  public void deletePill() {
    if (getArguments() != null) {
      Pill pill = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill();
      alarmViewModel.deleteAlarmFromPill(pill.getId());
      pillViewModel.delete(pill);
      navController.navigate(AddEditPillFragmentDirections.actionPillSavedBackToRecycler());
      Toast.makeText(getActivity(), "Pill deleted", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getActivity(), "Error with Pill", Toast.LENGTH_SHORT).show();
    }
  }

  private boolean isDataValid() {
    //TODO zamienić walidacje
    return !(addEditPillBinding.textInputName.getEditText().getText().toString().isEmpty()
            || addEditPillBinding.textInputDescription.getEditText().getText().toString().isEmpty());
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    if (getArguments() != null) {
      switch (AddEditPillFragmentArgs.fromBundle(getArguments()).getMode()) {
        case "Add": {
          menu.clear();
          inflater.inflate(R.menu.add_pill_menu, menu);
          break;
        }
        case "Edit": {
          menu.clear();
          inflater.inflate(R.menu.edit_pill_menu, menu);
          break;
        }
      }
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_icon_pill_save: {
        savePill();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_pill_update: {
        updatePill();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_pill_delete: {
        deletePill();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      default:
        break;
    }
    return false;
  }


  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    if (modeAlarm) {
      int position = Integer.parseInt(addEditPillBinding.alarmTextPosition.getText().toString());
      Alarm current_alarm = alarmAdapter.getAlarmAt(position);
      int pill_id;
      if (getArguments() != null) {
        pill_id = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill().getId();
      } else pill_id = -4;

      Alarm alarm = new Alarm(pill_id, hourOfDay, minute, current_alarm.isActive(), false);
      alarm.setId(current_alarm.getId());
      alarmViewModel.update(alarm);
      cancelAlarm(position);
      Log.d(TAG, "onTimeSet: Edit alarm");


    } else {
      int pill_id;
      if (getArguments() != null) {
        if ("Add".equals(AddEditPillFragmentArgs.fromBundle(getArguments()).getMode())) {
          pill_id = new_pill_id;
        } else {
          pill_id = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill().getId();
        }
      } else pill_id = -4;
      //Dodana linia
      Alarm alarm = new Alarm(pill_id, hourOfDay, minute, true, false);
      alarmViewModel.insert(alarm);
      //startAlarm(adapter.getItemCount());
      Log.d(TAG, "onTimeSet: Add alarm");
    }
  }

  private void startAlarm(int position) {
    Log.d(TAG, "startAlarm: fun");
    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Alarm alarm = alarmAdapter.getAlarmAt(position);
    long ll = 23;
    int id = alarm.getId();
    if (id != 0) {
      intent.putExtra(ALARM_EXTRA_STRING, id);
    }
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    calendar.set(Calendar.MINUTE, alarm.getMinute());
    calendar.set(Calendar.SECOND, 0);

    if (calendar.before(Calendar.getInstance())) {
      calendar.add(Calendar.DATE, 1);
    }

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
  }

  private void cancelAlarm(int position) {
    Log.d(TAG, "cancelAlarm: fun");
    //TODO dodanie getActivity()
    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Alarm alarm = alarmAdapter.getAlarmAt(position);
    int id = alarm.getId();
    if (id != 0) {
      intent.putExtra(ALARM_EXTRA_STRING, id);
    }
    //getContext() zamist this
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    calendar.set(Calendar.MINUTE, alarm.getMinute());
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.DAY_OF_WEEK, 4);

    alarmManager.cancel(pendingIntent);
  }

  @Override
  public void onAlarmClick(int position) {
    Alarm current_alarm = alarmAdapter.getAlarmAt(position);
    modeAlarm = true;
    addEditPillBinding.alarmTextPosition.setText(String.valueOf(position));
    DialogFragment timePicker = new TimePickerFragment(current_alarm.getHour(), current_alarm.getMinute());
    timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
  }

  @Override
  public void onAlarmSwitchClick(int position, boolean active) {
    Alarm current_alarm = alarmAdapter.getAlarmAt(position);

    Alarm alarm = new Alarm(current_alarm.getPill_id(), current_alarm.getHour(), current_alarm.getMinute(), active, current_alarm.isSetup());
    alarm.setId(current_alarm.getId());
    alarmViewModel.update(alarm);
  }

  @Override
  public void onDeleteAlarmClick(int position) {
    alarmViewModel.delete(alarmAdapter.getAlarmAt(position));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();

    if (new_pill_id != 0) {
      Pill pill = pillViewModel.getAllPills().getValue().get(new_pill_id);
      if ("default".equals(pill.getPicPath())) {
        alarmViewModel.getAlarmFromPill(pill.getId());
        pillViewModel.delete(pill);
        //TODO zakończenie i usunięcie alarmów
      }
    }

  }
}