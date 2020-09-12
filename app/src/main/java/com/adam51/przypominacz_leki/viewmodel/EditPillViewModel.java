package com.adam51.przypominacz_leki.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam51.przypominacz_leki.model.Pill;

public class EditPillViewModel extends ViewModel {

  private MutableLiveData<Pill> pill;

  public void setPill(MutableLiveData<Pill> pill) {
    this.pill = pill;
  }

  public LiveData<Pill> getPill() {
    return pill;
  }
}
