package com.adam51.przypominacz_leki.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.model.Pressure;
import com.adam51.przypominacz_leki.repo.PressureRepository;

import java.util.List;

public class PressureViewModel extends AndroidViewModel {
  private PressureRepository repository;
  private LiveData<List<Pressure>> allPressures;

  public PressureViewModel(@NonNull Application application) {
    super(application);
    repository = new PressureRepository(application);
    allPressures = repository.getAllPressures();
  }

  public void insert(Pressure pressure) {
    repository.insert(pressure);
  }

  public void update(Pressure pressure) {
    repository.update(pressure);
  }

  public void delete(Pressure pressure) {
    repository.delete(pressure);
  }

  public void deleteAllPressures() {
    repository.deleteAllPressures();
  }

  public LiveData<List<Pressure>> getAllPressures() {
    return allPressures;
  }
}
