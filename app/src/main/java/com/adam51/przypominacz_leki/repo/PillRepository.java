package com.adam51.przypominacz_leki.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.Pill;
import com.adam51.przypominacz_leki.dao.PillDao;
import com.adam51.przypominacz_leki.database.PillDatabase;

import java.util.List;

public class PillRepository {
  private PillDao pillDao;
  private LiveData<List<Pill>> allPills;

  public PillRepository(Application application) {
    PillDatabase database = PillDatabase.getInstance(application);
    pillDao = database.pillDao();
    allPills = pillDao.getAllPills();
  }

  public void insert(Pill pill) {
    new InsertPillAsyncTask(pillDao).execute(pill);
  }

  public void update(Pill pill) {
    new UpdatePillAsyncTask(pillDao).execute(pill);
  }

  public void delete(Pill pill) {
    new DeletePillAsyncTask(pillDao).execute(pill);
  }

  public void deleteAllPills() {
    new DeleteAllPillAsyncTask(pillDao).execute();
  }

  public LiveData<List<Pill>> getAllPills() {
    return allPills;
  }

  private static class InsertPillAsyncTask extends AsyncTask<Pill, Void, Void> {
    private PillDao pillDao;

    private InsertPillAsyncTask( PillDao pillDao) {
      this.pillDao = pillDao;
    }

    @Override
    protected Void doInBackground(Pill... pills) {
      pillDao.insert(pills[0]);
      return null;
    }
  }

  private static class UpdatePillAsyncTask extends AsyncTask<Pill, Void, Void> {
    private PillDao pillDao;

    private UpdatePillAsyncTask( PillDao pillDao) {
      this.pillDao = pillDao;
    }

    @Override
    protected Void doInBackground(Pill... pills) {
      pillDao.update(pills[0]);
      return null;
    }
  }

  private static class DeletePillAsyncTask extends AsyncTask<Pill, Void, Void> {
    private PillDao pillDao;

    private DeletePillAsyncTask( PillDao pillDao) {
      this.pillDao = pillDao;
    }

    @Override
    protected Void doInBackground(Pill... pills) {
      pillDao.delete(pills[0]);
      return null;
    }
  }

  private static class DeleteAllPillAsyncTask extends AsyncTask<Void, Void, Void> {
    private PillDao pillDao;

    private DeleteAllPillAsyncTask( PillDao pillDao) {
      this.pillDao = pillDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      pillDao.deleteAllPill();
      return null;
    }
  }
}
