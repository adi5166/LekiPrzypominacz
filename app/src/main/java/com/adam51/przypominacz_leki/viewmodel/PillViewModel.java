package com.adam51.przypominacz_leki.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.repo.PillRepository;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PillViewModel extends AndroidViewModel {
  private PillRepository repository;
  private LiveData<List<Pill>> allPills;

  public PillViewModel(@NonNull Application application) {
    super(application);
    repository = new PillRepository(application);
    allPills = repository.getAllPills();
  }

  public void insert(Pill pill) {
    repository.insert(pill);
  }

  public void update(Pill pill) {
    repository.update(pill);
  }

  public void delete(Pill pill) {
    Log.d(TAG, "delete: pill: "+pill.getId());
    repository.delete(pill);
  }

  public void deleteAllPills() {
    repository.deleteAllPills();
  }
  public LiveData<List<Pill>> getAllPills() {
    return allPills;
  }

}
