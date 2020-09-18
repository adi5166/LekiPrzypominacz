package com.adam51.przypominacz_leki.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.databinding.ItemAlarmBinding;
import com.adam51.przypominacz_leki.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
  private List<Alarm> alarms = new ArrayList<>();
  private OnAlarmListener alarmListener;

  public AlarmAdapter(OnAlarmListener alarmListener) {
    this.alarmListener = alarmListener;
  }

  @NonNull
  @Override
  public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemAlarmBinding alarmBinding = ItemAlarmBinding.inflate(layoutInflater, parent, false);
    return new AlarmHolder(alarmBinding, alarmListener);
  }

  @Override
  public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
    Alarm current_alarm = alarms.get(position);
    holder.alarmBinding.itemAlarmSwitch.setChecked(current_alarm.isActive());
    holder.alarmBinding.itemAlarmTime.setText(current_alarm.getHour() + ":" + current_alarm.getMinute());
    //TODO ustawienie zegara
  }

  @Override
  public int getItemCount() {
    return alarms.size();
  }

  public void setAlarms(List<Alarm> alarms) {
    this.alarms = alarms;
    notifyDataSetChanged();
  }

  public Alarm getAlarmAt(int position) {
    return alarms.get(position);
  }

  class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private ItemAlarmBinding alarmBinding;
    private OnAlarmListener alarmListener;

    public AlarmHolder(@NonNull ItemAlarmBinding alarmBinding, final OnAlarmListener alarmListener) {
      super(alarmBinding.getRoot());
      this.alarmBinding = alarmBinding;
      this.alarmListener = alarmListener;
/*
      alarmBinding.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (alarmListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              alarmListener.onAlarmClick(position);
            }
          }
        }
      });
*/
      alarmBinding.itemAlarmTime.setOnClickListener(this);
      alarmBinding.itemAlarmDelete.setOnLongClickListener(this);
      alarmBinding.itemAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (isChecked) {
            alarmListener.onAlarmSwitchClick(getAdapterPosition(), true);
          } else {
            alarmListener.onAlarmSwitchClick(getAdapterPosition(), false);
          }
        }
      });
    }

    @Override
    public void onClick(View v) {
      alarmListener.onAlarmClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
      alarmListener.onDeleteAlarmClick(getAdapterPosition());
      return false;
    }
  }

  public interface OnAlarmListener {
    void onAlarmClick(int position);

    void onAlarmSwitchClick(int position, boolean active);

    void onDeleteAlarmClick(int position);
  }
}