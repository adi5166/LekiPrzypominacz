package com.adam51.przypominacz_leki.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.databinding.ItemPressureBinding;
import com.adam51.przypominacz_leki.model.Pressure;

import java.util.ArrayList;
import java.util.List;

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.PressureHolder> {
  private List<Pressure> pressures = new ArrayList<>();
  private OnPressureListener pressureListener;

  public PressureAdapter(OnPressureListener pressureListener) {
    this.pressureListener = pressureListener;
  }

  @NonNull
  @Override
  public PressureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemPressureBinding pressureBinding = ItemPressureBinding.inflate(layoutInflater, parent, false);
    return new PressureHolder(pressureBinding, pressureListener);
  }

  @Override
  public void onBindViewHolder(@NonNull PressureHolder holder, int position) {
    Pressure current_pressure = pressures.get(position);
    holder.pressureBinding.itemPressureLow.setText(current_pressure.getLow_pressure());
    holder.pressureBinding.itemPressureHigh.setText(current_pressure.getHigh_pressure());
    holder.pressureBinding.itemPressureDescription.setText(current_pressure.getDescription());
    holder.pressureBinding.itemPressureTime.setText(current_pressure.getDate());
  }

  @Override
  public int getItemCount() {
    return pressures.size();
  }

  public void setPressures(List<Pressure> pressureList) {
    this.pressures = pressureList;
    notifyDataSetChanged();
  }

  public Pressure getPressureAt(int position) {
    return pressures.get(position);
  }

  class PressureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ItemPressureBinding pressureBinding;
    private OnPressureListener pressureListener;


    public PressureHolder(ItemPressureBinding pressureBinding, OnPressureListener pressureListener) {
      super(pressureBinding.getRoot());
      this.pressureBinding = pressureBinding;
      this.pressureListener = pressureListener;

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      pressureListener.onPressureClick(getAdapterPosition());
    }
  }

  public interface OnPressureListener {
    void onPressureClick(int position);
  }
}
