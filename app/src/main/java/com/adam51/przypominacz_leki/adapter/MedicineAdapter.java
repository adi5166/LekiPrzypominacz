package com.adam51.przypominacz_leki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.databinding.ItemPillBinding;


import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {
  private List<Pill> pills = new ArrayList<>();
  private OnPillListener pillListener;

  public MedicineAdapter (OnPillListener onPillListener){
    this.pillListener = onPillListener;
  }

  // Data
  @NonNull
  @Override
  public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemPillBinding itemPillBinding = ItemPillBinding.inflate(layoutInflater, parent, false);
    return new MedicineHolder(itemPillBinding, pillListener);
  }

  @Override
  public void onBindViewHolder(@NonNull MedicineHolder holder, int position) {

    Pill currentPill = pills.get(position);
    holder.itemBinding.medicineItemName.setText(currentPill.getName());

    Context context = holder.itemView.getContext();
    Util.SetPillImageView(context, currentPill.getPicPath(), holder.itemBinding.medicineItemImage);
    /*
    Drawable drawable;

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

    holder.itemBinding.medicineItemImage.setImageDrawable(drawable);
    */
  }

  @Override
  public int getItemCount() {
    return pills.size();
  }

  public void setPills(List<Pill> pills) {
    this.pills = pills;
    notifyDataSetChanged();
    //TODO beter from recyclerview
  }

  //używane z klasy maciezrzystej / parent
  public Pill getPillAt(int position) {
    return pills.get(position);
  }

  class MedicineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemPillBinding itemBinding;
    OnPillListener onPillListener;

    public MedicineHolder(@NonNull ItemPillBinding itemBinding, OnPillListener onPillListener) {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
      this.onPillListener = onPillListener;

      itemView.setOnClickListener(this);
      // Lepiej używać interfejsu
    }

    @Override
    public void onClick(View v) {
      onPillListener.onPillClick(getAdapterPosition());
    }
  }

  public interface OnPillListener{
    void onPillClick(int position);
  }

}
