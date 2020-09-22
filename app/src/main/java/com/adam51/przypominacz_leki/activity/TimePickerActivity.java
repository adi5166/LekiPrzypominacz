package com.adam51.przypominacz_leki.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.adam51.przypominacz_leki.adapter.AlarmAdapter;
import com.adam51.przypominacz_leki.databinding.ActivityTimePickerBinding;
import com.adam51.przypominacz_leki.fragment.AddEditPillFragment;
import com.adam51.przypominacz_leki.fragment.AddEditPillFragmentArgs;
import com.adam51.przypominacz_leki.helper.TimePickerFragment;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.receiver.AlarmReceiver;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_INT;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_STRING;

public class TimePickerActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, AlarmAdapter.OnAlarmListener {

  private ActivityTimePickerBinding binding;
  private AlarmViewModel alarmViewModel;
  private NotificationManagerCompat notificationManager;
  private AlarmAdapter alarmAdapter;
  private boolean modeAlarm = false;
  private Pill current_pill;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityTimePickerBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Intent intent = getIntent();
    current_pill = intent.getParcelableExtra(AddEditPillFragment.EXTRA_PILL);
    Log.d(TAG, "onCreate: New Activity");

    alarmAdapter = new AlarmAdapter(this);
    binding.alarmRecycle.setAdapter(alarmAdapter);

    alarmViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AlarmViewModel.class);
    alarmViewModel.getAlarmFromPill(current_pill.getId()).observe(this, new Observer<List<Alarm>>() {
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
        //update
      }
    });

    notificationManager = NotificationManagerCompat.from(this);

    binding.timePickerAddButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        modeAlarm = false;
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
      }
    });


  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    if (modeAlarm) {
      int position = Integer.parseInt(binding.alarmTextPosition.getText().toString());
      Alarm current_alarm = alarmAdapter.getAlarmAt(position);

      Alarm alarm = new Alarm(current_pill.getId(), hourOfDay, minute, current_alarm.isActive(), false);
      alarm.setId(current_alarm.getId());
      alarmViewModel.update(alarm);
      cancelAlarm(position);
      Log.d(TAG, "onTimeSet: Edit alarm");


    } else {

      Alarm alarm = new Alarm(current_pill.getId(), hourOfDay, minute, true, false);
      alarmViewModel.insert(alarm);
      //startAlarm(adapter.getItemCount());
      Log.d(TAG, "onTimeSet: Add alarm");
    }
  }

  private void startAlarm(int position) {
    Log.d(TAG, "startAlarm: fun");
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this, AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Alarm alarm = alarmAdapter.getAlarmAt(position);
    int id = alarm.getId();
    if (id != 0) {
      intent.putExtra(ALARM_EXTRA_STRING, current_pill.getName());
      intent.putExtra(ALARM_EXTRA_INT, id);
    }
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

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

    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this, AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Alarm alarm = alarmAdapter.getAlarmAt(position);
    int id = alarm.getId();
    if (id != 0) {
      intent.putExtra(ALARM_EXTRA_STRING, id);
      intent.putExtra(ALARM_EXTRA_STRING, current_pill.getName());
    }
    //getContext() zamist this
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

    alarmManager.cancel(pendingIntent);
  }

  @Override
  public void onAlarmClick(int position) {
    Alarm current_alarm = alarmAdapter.getAlarmAt(position);
    modeAlarm = true;
    binding.alarmTextPosition.setText(String.valueOf(position));
    DialogFragment timePicker = new TimePickerFragment(current_alarm.getHour(), current_alarm.getMinute());
    timePicker.show(getSupportFragmentManager(), "time picker");
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

}
