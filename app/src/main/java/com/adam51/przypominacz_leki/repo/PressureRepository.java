package com.adam51.przypominacz_leki.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.dao.PressureDao;
import com.adam51.przypominacz_leki.database.PressureDatabase;
import com.adam51.przypominacz_leki.model.Pressure;

import java.util.List;

public class PressureRepository {
  private PressureDao pressureDao;
  private LiveData<List<Pressure>> allPressures;

  public PressureRepository(Application application) {
    PressureDatabase database = PressureDatabase.getInstance(application);

    this.pressureDao = database.pressureDao();
    this.allPressures = pressureDao.getAllPressures();
  }

  public void insert(Pressure pressure){
    new PressureRepository.InsertPressureAsyncTask(pressureDao).execute(pressure);
  }

  public void update(Pressure pressure){
    new PressureRepository.UpdatePressureAsyncTask(pressureDao).execute(pressure);
  }

  public void delete(Pressure pressure){
    new DeletePressureAsyncTask(pressureDao).execute(pressure);
  }

  public void deleteAllPressures(){
    new DeleteAllPressureAsyncTask(pressureDao).execute();
  }

  public LiveData<List<Pressure>> getAllPressures() {
    return allPressures;
  }

  private static class InsertPressureAsyncTask extends AsyncTask<Pressure, Void, Void> {
    private PressureDao pressureDao;

    public InsertPressureAsyncTask(PressureDao pressureDao) {
      this.pressureDao = pressureDao;
    }

    @Override
    protected Void doInBackground(Pressure... pressures) {
      pressureDao.insert(pressures[0]);
      return null;
    }
  }

  private static class UpdatePressureAsyncTask extends AsyncTask<Pressure, Void, Void> {
    private PressureDao pressureDao;

    public UpdatePressureAsyncTask(PressureDao pressureDao) {
      this.pressureDao = pressureDao;
    }

    @Override
    protected Void doInBackground(Pressure... pressures) {
      pressureDao.update(pressures[0]);
      return null;
    }
  }

  private static class DeletePressureAsyncTask extends AsyncTask<Pressure, Void, Void> {
    private PressureDao pressureDao;

    public DeletePressureAsyncTask(PressureDao pressureDao) {
      this.pressureDao = pressureDao;
    }

    @Override
    protected Void doInBackground(Pressure... pressures) {
      pressureDao.delete(pressures[0]);
      return null;
    }
  }

  private static class DeleteAllPressureAsyncTask extends AsyncTask<Void, Void, Void> {
    private PressureDao pressureDao;

    public DeleteAllPressureAsyncTask(PressureDao pressureDao) {
      this.pressureDao = pressureDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      pressureDao.deleteAllPressures();
      return null;
    }
  }
}
