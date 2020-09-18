package com.adam51.przypominacz_leki.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.dao.AlarmDao;
import com.adam51.przypominacz_leki.database.AlarmDatabase;
import com.adam51.przypominacz_leki.model.Alarm;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmRepository {
  private AlarmDao alarmDao;
  private LiveData<List<Alarm>> allAlarms;

  public AlarmRepository(Application application) {
    AlarmDatabase database = AlarmDatabase.getInstance(application);
    alarmDao = database.alarmDao();
    allAlarms = alarmDao.getAllAlarms();
  }

  public void insert(Alarm alarm) {
    new InsertAlarmAsyncTask(alarmDao).execute(alarm);
  }

  public void update(Alarm alarm) {
    new UpdateAlarmAsyncTask(alarmDao).execute(alarm);
  }

  public void delete(Alarm alarm) {
    new DeleteAlarmAsyncTask(alarmDao).execute(alarm);
  }

  public void deleteAlarmFromPill(int pill_id) {
    new DeleteAlarmFromPillAsyncTask(alarmDao).execute(pill_id);
  }

  public void deleteAllPills() {
    new DeleteAllAlarmAsyncTask(alarmDao).execute();
  }

  public LiveData<List<Alarm>> getAlarmFromPill(int pill_id) {

    LiveData<List<Alarm>> alarms = null;
    try {
      alarms = new GetAlarmFromPillAsyncTask(alarmDao).execute(pill_id).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return alarms;
  }

  public LiveData<List<Alarm>> getAllAlarms() {
    return allAlarms;
  }

  private static class InsertAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
    private AlarmDao alarmDao;

    private InsertAlarmAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected Void doInBackground(Alarm... alarms) {
      alarmDao.insert(alarms[0]);
      return null;
    }
  }

  private static class UpdateAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
    private AlarmDao alarmDao;

    private UpdateAlarmAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected Void doInBackground(Alarm... alarms) {
      alarmDao.update(alarms[0]);
      return null;
    }
  }

  private static class DeleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
    private AlarmDao alarmDao;

    private DeleteAlarmAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected Void doInBackground(Alarm... alarms) {
      alarmDao.delete(alarms[0]);
      return null;
    }
  }

  private static class DeleteAlarmFromPillAsyncTask extends AsyncTask<Integer, Void, Void> {
    private AlarmDao alarmDao;

    private DeleteAlarmFromPillAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
      alarmDao.deleteAlarmFromPill(integers[0]);
      return null;
    }
  }

  private static class DeleteAllAlarmAsyncTask extends AsyncTask<Void, Void, Void> {
    private AlarmDao alarmDao;

    private DeleteAllAlarmAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      alarmDao.deleteAllAlarm();
      return null;
    }
  }

  private static class GetAlarmFromPillAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Alarm>>> {
    private AlarmDao alarmDao;

    public GetAlarmFromPillAsyncTask(AlarmDao alarmDao) {
      this.alarmDao = alarmDao;
    }

    @Override
    protected LiveData<List<Alarm>> doInBackground(Integer... integers) {
      return alarmDao.getAlarmFromPill(integers[0]);
    }

    @Override
    protected void onPostExecute(LiveData<List<Alarm>> listLiveData) {
      super.onPostExecute(listLiveData);
    }
  }
}