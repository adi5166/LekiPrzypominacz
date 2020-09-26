package com.adam51.przypominacz_leki.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.dao.AlarmDao;
import com.adam51.przypominacz_leki.dao.SugarDao;
import com.adam51.przypominacz_leki.database.AlarmDatabase;
import com.adam51.przypominacz_leki.database.SugarDatabase;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.Sugar;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SugarRepository {
  private SugarDao sugarDao;
  private LiveData<List<Sugar>> allSugars;

  public SugarRepository(Application application) {
    SugarDatabase database = SugarDatabase.getInstance(application);

    sugarDao = database.sugarDao();
    allSugars = sugarDao.getAllSugars();
  }

  public void insert(Sugar sugar) {
    new InsertSugarAsyncTask(sugarDao).execute(sugar);
  }

  public void update(Sugar sugar) {
    new UpdateSugarAsyncTask(sugarDao).execute(sugar);
  }

  public void delete(Sugar sugar) {
    new DeleteSugarAsyncTask(sugarDao).execute(sugar);
  }

  public void deleteAllSugar() {
    new SugarRepository.DeleteAllSugarAsyncTask(sugarDao).execute();
  }

  public LiveData<List<Sugar>> getAllSugars() {
    return allSugars;
  }

  private static class InsertSugarAsyncTask extends AsyncTask<Sugar, Void, Void> {
    private SugarDao sugarDao;

    private InsertSugarAsyncTask(SugarDao sugarDao) {
      this.sugarDao = sugarDao;
    }

    @Override
    protected Void doInBackground(Sugar... sugars) {
      sugarDao.insert(sugars[0]);
      return null;
    }
  }

  private static class UpdateSugarAsyncTask extends AsyncTask<Sugar, Void, Void> {
    private SugarDao sugarDao;

    private UpdateSugarAsyncTask(SugarDao sugarDao) {
      this.sugarDao = sugarDao;
    }

    @Override
    protected Void doInBackground(Sugar... sugars) {
      sugarDao.update(sugars[0]);
      return null;
    }
  }

  private static class DeleteSugarAsyncTask extends AsyncTask<Sugar, Void, Void> {
    private SugarDao sugarDao;

    private DeleteSugarAsyncTask(SugarDao sugarDao) {
      this.sugarDao = sugarDao;
    }

    @Override
    protected Void doInBackground(Sugar... sugars) {
      sugarDao.delete(sugars[0]);
      return null;
    }
  }

  private static class DeleteAllSugarAsyncTask extends AsyncTask<Void, Void, Void> {
    private SugarDao sugarDao;

    private DeleteAllSugarAsyncTask(SugarDao sugarDao) {
      this.sugarDao = sugarDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      sugarDao.deleteAllAlarm();
      return null;
    }
  }
}
