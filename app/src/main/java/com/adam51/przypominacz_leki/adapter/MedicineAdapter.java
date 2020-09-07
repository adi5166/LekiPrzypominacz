package com.adam51.przypominacz_leki.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.ItemPillBinding;


import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {
  private List<Pill> pills = new ArrayList<>();

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

    Pill currentPill = pills.get(position);
    holder.itemBinding.medicineItemName.setText(currentPill.getName());

    Drawable drawable;
    Context context = holder.itemView.getContext();
    if(!currentPill.getPicPath().isEmpty()) {
      switch (currentPill.getPicPath()) {
        case "pill_oval_orange":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_orange);
          break;
        case "pill_oval_blue":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_blue);
          break;
        default:
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
      }
    }else drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);

    /*
    Can use:
    drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
    drawable = AppCompatResources.getDrawable(context, R.drawable.pill_oval);

    Can't use
    drawable = ResourcesCompat.getDrawable(Resources.getSystem(), R.drawable.pill_oval, null);
    */

    holder.itemBinding.medicineItemImage.setImageDrawable(drawable);
  }

  @Override
  public int getItemCount() {
    return pills.size();
  }

  public void setPills(List<Pill> pills) {
    this.pills = pills;
    notifyDataSetChanged();
    //beter from recyclerview
  }

  class MedicineHolder extends RecyclerView.ViewHolder {
    ItemPillBinding itemBinding;

    public MedicineHolder(@NonNull ItemPillBinding itemBinding) {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
      // Można dodać click listiner
      // Lepiej używać interfejsu
    }
  }

}
