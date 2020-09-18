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
import com.adam51.przypominacz_leki.activity.MainActivity;
import com.adam51.przypominacz_leki.activity.TimePickerActivity;
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

public class AddEditPillFragment extends Fragment {

  private FragmentAddEditPillBinding addEditPillBinding;
  private NavController navController;
  private ArrayList<ImagePill> imagePillList;
  private ImagePillAdapter adapter;
  private PillViewModel pillViewModel;
  //private EditPillViewModel editPillViewModel;

  private AlarmViewModel alarmViewModel;
  public static final String EXTRA_PILL = "com.adam51.pills";


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


    alarmViewModel = new ViewModelProvider(getActivity()).get(AlarmViewModel.class);
    //TODO zabieranie częsci alarmów z bazy danych

    addEditPillBinding.switchPillRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) addEditPillBinding.radioGroupPill.setVisibility(View.VISIBLE);
        else addEditPillBinding.radioGroupPill.setVisibility(View.GONE);
      }
    });

    if (getArguments() != null) {
      String mode = AddEditPillFragmentArgs.fromBundle(getArguments()).getMode();
      if (mode.equals("Edit")) {
        addEditPillBinding.addPillButton.setVisibility(View.INVISIBLE);
        addEditPillBinding.editPillButton.setVisibility(View.VISIBLE);
        addEditPillBinding.alarmAddButton.setVisibility(View.VISIBLE);

        final int position = AddEditPillFragmentArgs.fromBundle(getArguments()).getPillPosition();
        if (position != -5) {
          Pill current_pill = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill();
          addEditPillBinding.pillDetailName.setText(current_pill != null ? current_pill.getName() : "");
          addEditPillBinding.pillDetailDescription.setText(current_pill != null ? current_pill.getDescription() : "");
          int spinner_pos = getSpinnerPosition(current_pill.getPicPath());
          if (spinner_pos != -1) {
            addEditPillBinding.spinnerPillAdd.setSelection(spinner_pos);
          }
          //Util.SetPillImageView(getContext(), current_pill. ,addEditPillBinding.spinnerPillAdd);
        }

        addEditPillBinding.alarmAddButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            openAlarms();
          }
        });

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
        addEditPillBinding.alarmAddButton.setVisibility(View.INVISIBLE);

        addEditPillBinding.addPillButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            savePill();
            Util.hideKeyboard(getContext(), v);
          }
        });
        Log.d(TAG, "onViewCreated: Add mode");
      }
    }
  }

  public void openAlarms() {
    if (getArguments() != null) {
      Pill current_pill = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill();
      int pill_id = current_pill.getId();
      if (pill_id != 0) {

        Intent intent = new Intent(getActivity(), TimePickerActivity.class);
        intent.putExtra(EXTRA_PILL, current_pill);
        startActivity(intent);


        //navController.navigate(AddEditPillFragmentDirections.actionAddEditPillFragmentToTimePickerActivity(current_pill));
      }else {
        Log.d(TAG, "openAlarms: problem with pill_id");
      }
    }else {
      Log.d(TAG, "openAlarms: problem with arguments");
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


  /*
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

  }*/
}