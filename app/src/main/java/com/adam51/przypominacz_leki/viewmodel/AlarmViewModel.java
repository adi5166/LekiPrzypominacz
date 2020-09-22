package com.adam51.przypominacz_leki.viewmodel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.adam51.przypominacz_leki.activity.MainActivity;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.receiver.AlarmReceiver;
import com.adam51.przypominacz_leki.repo.AlarmRepository;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_COLOR;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_INT;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_STRING;

public class AlarmViewModel extends AndroidViewModel {
  private AlarmRepository repository;
  private LiveData<List<Alarm>> allAlarms;
  Observer<List<Alarm>> observer;

  public AlarmViewModel(@NonNull Application application) {
    super(application);
    repository = new AlarmRepository(application);
    allAlarms = repository.getAllAlarms();
  }

  public void insert(Alarm alarm) {
    repository.insert(alarm);
  }

  public void update(Alarm alarm) {
    repository.update(alarm);
  }

  public void delete(Alarm alarm) {
    Log.d(TAG, "delete alarm: fun");
    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarm.getId(), intent, 0);
    alarmManager.cancel(pendingIntent);

    repository.delete(alarm);
  }

  //TODO usunąć wszystko
  /*
  public void deleteAlarmFromPill(int pill_id) {
    List<Alarm> alarmList = allAlarms.getValue();
    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    if (alarmList != null) {
      for (Alarm alarm : alarmList
      ) {
        if (alarm.getPill_id() == pill_id) {
          PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarm.getId(), intent, 0);

          Calendar calendar = Calendar.getInstance();
          calendar.setTimeInMillis(System.currentTimeMillis());
          calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
          calendar.set(Calendar.MINUTE, alarm.getMinute());
          calendar.set(Calendar.SECOND, 0);

          alarmManager.cancel(pendingIntent);
        }
      }
    }else Log.d(TAG, "deleteAlarmFromPill: alarmList is null");


    repository.deleteAlarmFromPill(pill_id);
  }
   */
  public void cancelAlarmFromPill(final int pill_id, final String pill_name, final String color) {
    final AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    observer = new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
          if (alarms.get(i).getPill_id() == pill_id) {

            intent.putExtra(ALARM_EXTRA_STRING, pill_name);
            intent.putExtra(ALARM_EXTRA_COLOR, color);
            intent.putExtra(ALARM_EXTRA_INT, alarms.get(i).getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarms.get(i).getId(), intent, 0);
            alarmManager.cancel(pendingIntent);
          }
        }
      }
    };
    allAlarms.observeForever(observer);
  }

  public void startAlarmFromPill(final int pill_id, final String pill_name, final String color) {
    final AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    observer = new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
          if (alarms.get(i).getPill_id() == pill_id) {

            intent.putExtra(ALARM_EXTRA_COLOR, color);
            intent.putExtra(ALARM_EXTRA_STRING, pill_name);
            intent.putExtra(ALARM_EXTRA_INT, alarms.get(i).getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarms.get(i).getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, alarms.get(i).getHour());
            calendar.set(Calendar.MINUTE, alarms.get(i).getMinute());
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) {
              calendar.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Alarm current_alarm = alarms.get(i);
            current_alarm.setSetup(true);
            update(current_alarm);
          }
          Log.d(TAG, "onChanged: start alarm name");
        }
      }
    };
    allAlarms.observeForever(observer);
  }

  public void deleteAlarmFromPill(final int pill_id) {
    final AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    observer = new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
          if (alarms.get(i).getPill_id() == pill_id) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarms.get(i).getId(), intent, 0);
            alarmManager.cancel(pendingIntent);
          }
        }
      }
    };
    allAlarms.observeForever(observer);
    new DeleteAlarmAsyncTask(repository).execute(pill_id);
  }

  public void updateAlarmFromPill(final int pill_id, final String pill_name, final String color) {
    final AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    final Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    observer = new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
          if (alarms.get(i).getPill_id() == pill_id && alarms.get(i).isActive()) {

            intent.putExtra(ALARM_EXTRA_COLOR, color);
            intent.putExtra(ALARM_EXTRA_STRING, pill_name);
            intent.putExtra(ALARM_EXTRA_INT, alarms.get(i).getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarms.get(i).getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, alarms.get(i).getHour());
            calendar.set(Calendar.MINUTE, alarms.get(i).getMinute());
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) {
              calendar.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
/*
            Alarm current_alarm = alarms.get(i);
            current_alarm.setSetup(true);
            repository.update(current_alarm);*/
          }
        }
        Log.d(TAG, "onChanged: update alarm name");
      }
    };
    allAlarms.observeForever(observer);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    allAlarms.removeObserver(observer);
  }

  public void setupAlarmFromPill(int pill_id, boolean setup) {

  }

  private static class DeleteAlarmAsyncTask extends AsyncTask<Integer, Void, Void> {
    AlarmRepository alarmRepository;

    public DeleteAlarmAsyncTask(AlarmRepository alarmRepository) {
      this.alarmRepository = alarmRepository;
    }

    @Override
    protected Void doInBackground(Integer... integers) {

      Log.d(TAG, "doInBackground: before get all alarms");
/*
      AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
      Intent intent = new Intent(getApplication(), AlarmReceiver.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);



      if (alarmList != null) {
        for (int i = 0; i < alarmList.size(); i++) {

          if (alarmList.get(i).getPill_id() == idPill) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarmList.get(i).getId(), intent, 0);
            alarmManager.cancel(pendingIntent);
          }

        }
        alarmRepository.deleteAlarmFromPill(idPill);
      } else Log.d(TAG, "deleteAlarmFromPill: alarmList is null");

 */
      alarmRepository.deleteAlarmFromPill(integers[0]);
      return null;
    }
  }


  public void deleteAllAlarm() {
    List<Alarm> alarmList = allAlarms.getValue();
    AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getApplication(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    for (Alarm alarm : alarmList
    ) {
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarm.getId(), intent, 0);

      alarmManager.cancel(pendingIntent);
    }
    repository.deleteAllPills();
  }

  public LiveData<List<Alarm>> getActiveAlarms(boolean active) {
    return repository.getActiveAlarms(active);
  }

  public void unSetupAllAlarms(boolean setup) {
    repository.unSetupAllAlarms(setup);
  }

  public LiveData<List<Alarm>> getAlarmFromPill(int pill_id) {
    return repository.getAlarmFromPill(pill_id);
  }

  public LiveData<List<Alarm>> getAllAlarms() {
    return allAlarms;
  }
}

