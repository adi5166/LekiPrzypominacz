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

import com.adam51.przypominacz_leki.activity.MainActivity;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.receiver.AlarmReceiver;
import com.adam51.przypominacz_leki.repo.AlarmRepository;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AlarmViewModel extends AndroidViewModel {
  private AlarmRepository repository;
  private LiveData<List<Alarm>> allAlarms;

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

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    calendar.set(Calendar.MINUTE, alarm.getMinute());
    calendar.set(Calendar.SECOND, 0);

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
  public void deleteAlarmFromPill(int pill_id) {
    new DeleteAlarmAsyncTask(repository).execute(pill_id);
  }

  private class DeleteAlarmAsyncTask extends AsyncTask<Integer, Void, Void> {
    AlarmRepository alarmRepository;
    int idPill;

    public DeleteAlarmAsyncTask(AlarmRepository alarmRepository) {
      this.alarmRepository = alarmRepository;
    }

    @Override
    protected Void doInBackground(Integer... integers) {

      List<Alarm> alarmList = allAlarms.getValue();
      AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
      Intent intent = new Intent(getApplication(), AlarmReceiver.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      for (Alarm alarm : alarmList
      ) {
        if (alarm.getPill_id() == idPill) {

          PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), alarm.getId(), intent, 0);

          Calendar calendar = Calendar.getInstance();
          calendar.setTimeInMillis(System.currentTimeMillis());
          calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
          calendar.set(Calendar.MINUTE, alarm.getMinute());
          calendar.set(Calendar.SECOND, 0);

          alarmManager.cancel(pendingIntent);
        } else Log.d(TAG, "deleteAlarmFromPill: alarmList is null");
        alarmRepository.deleteAlarmFromPill(idPill);

      }
      Log.d(TAG, "onPostExecute: alarms deleted");
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

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(System.currentTimeMillis());
      calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
      calendar.set(Calendar.MINUTE, alarm.getMinute());
      calendar.set(Calendar.SECOND, 0);

      alarmManager.cancel(pendingIntent);
    }
    repository.deleteAllPills();
  }

  public LiveData<List<Alarm>> getActiveAlarms(boolean active){
    return repository.getActiveAlarms(active);
  }

  public void unSetupAllAlarms(boolean setup){
    repository.unSetupAllAlarms(setup);
  }

  public LiveData<List<Alarm>> getAlarmFromPill(int pill_id) {
    return repository.getAlarmFromPill(pill_id);
  }

  public LiveData<List<Alarm>> getAllAlarms() {
    return allAlarms;
  }
}

