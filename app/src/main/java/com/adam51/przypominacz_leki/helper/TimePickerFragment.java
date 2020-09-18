package com.adam51.przypominacz_leki.helper;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
  private int hour;
  private int minute;
  private boolean isSet;

  public TimePickerFragment(int hour, int minute) {
    this.hour = hour;
    this.minute = minute;
    this.isSet = true;
  }

  public TimePickerFragment() {
    this.isSet = false;
  }

  public boolean isSet() {
    return isSet;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    if(!isSet) {
      Calendar calendar = Calendar.getInstance();
      this.hour = calendar.get(Calendar.HOUR_OF_DAY);
      this.minute = calendar.get(Calendar.MINUTE);
    }
    return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
  }

  public interface OnSetTimeListener {
    public void onTimeSet(TimePicker view, int hourOfDay, int minute);
  }
}
