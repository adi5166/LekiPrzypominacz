package com.adam51.przypominacz_leki.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.databinding.FragmentMedicineBinding;
import com.adam51.przypominacz_leki.databinding.ItemPillBinding;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {

  // Data
  @NonNull
  @Override
  public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemPillBinding itemPillBinding = ItemPillBinding.inflate(layoutInflater, parent, false);
    return new MedicineHolder(itemPillBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull MedicineHolder holder, int position) {
    //holder.bindView(pillList.get(position));
    holder.itemBinding.medicineItemName.setText(String.valueOf(position));

  }

  @Override
  public int getItemCount() {
    return 10;
  }

  class MedicineHolder extends RecyclerView.ViewHolder {
/*
    public MedicineHolder(@NonNull View itemView) {
      super(itemView);
    }
    */
    ItemPillBinding itemBinding;

    public MedicineHolder(@NonNull ItemPillBinding itemBinding) {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
      // Można dodać click listiner
      // Lepiej używać interfejsu
    }

    public void bindView(String test) {
      itemBinding.medicineItemName.setText(test);

    }

  }
}
