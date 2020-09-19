package com.adam51.przypominacz_leki.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.adam51.przypominacz_leki.model.Sugar;
import com.adam51.przypominacz_leki.repo.SugarRepository;

import java.util.List;

public class SugarViewModel extends AndroidViewModel {
  private SugarRepository repository;
  private LiveData<List<Sugar>> allSugars;

  public SugarViewModel(@NonNull Application application) {
    super(application);
    repository = new SugarRepository(application);
    allSugars = repository.getAllSugars();
  }

  public void insert(Sugar sugar){
    repository.insert(sugar);
  }

  public void update(Sugar sugar){
    repository.update(sugar);
  }

  public void delete(Sugar sugar){
    repository.delete(sugar);
  }

  public void deleteAllSugars(){
    repository.deleteAllSugar();
  }

  public LiveData<List<Sugar>> getAllSugars(){
    return allSugars;
  }
}
