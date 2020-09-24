package com.adam51.przypominacz_leki.adapter;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.databinding.FragmentHomeBinding;
import com.adam51.przypominacz_leki.databinding.ItemAlarmBinding;
import com.adam51.przypominacz_leki.databinding.ItemHomeBinding;
import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.Pill;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
  private List<Alarm> alarmList = new ArrayList<>();
  private List<Pill> pillList = new ArrayList<>();
  private List<Integer> pill_list_id = new ArrayList<>();

  @NonNull
  @Override
  public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemHomeBinding homeBinding = ItemHomeBinding.inflate(layoutInflater, parent, false);
    return new HomeHolder(homeBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
    Alarm current_alarm = alarmList.get(position);
    String time = current_alarm.getHour() + ":" + current_alarm.getMinute();
    holder.binding.homeTimeText.setText(time);

    String pill_name = "";
    int pill_id = current_alarm.getPill_id();
    for (int i = 0; i < pillList.size(); i++) {
      if (pill_id == pillList.get(i).getId()) {
        pill_name = pillList.get(i).getName();
        Util.SetPillImageView(Util.getPillImageID(pillList.get(i).getPicPath()), holder.binding.homePillImage);
        pill_list_id.add(i);
        break;
      }
    }
    holder.binding.homePillNameText.setText(pill_name);
  }

  public void setAlarmList(List<Alarm> alarmList) {
    this.alarmList = alarmList;
    notifyDataSetChanged();
  }

  public void setPillList(List<Pill> pillList) {
    this.pillList = pillList;
    notifyDataSetChanged();
  }

  public Alarm getAlarmAt(int position) {
    return alarmList.get(position);
  }

  public Pill getPillAt(int position) {
    return pillList.get(position);
  }

  public Integer getPillID(int position){
    return pill_list_id.get(position);
  }

  @Override
  public int getItemCount() {
    if (alarmList != null) {
      return alarmList.size();
    }else return 0;
  }



  class HomeHolder extends RecyclerView.ViewHolder {
    private ItemHomeBinding binding;

    public HomeHolder(ItemHomeBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
