package com.adam51.przypominacz_leki.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam51.przypominacz_leki.databinding.ItemSugarBinding;
import com.adam51.przypominacz_leki.model.Sugar;

import java.util.ArrayList;
import java.util.List;

public class SugarAdapter extends RecyclerView.Adapter<SugarAdapter.SugarHolder> {
  private List<Sugar> sugars = new ArrayList<>();
  private OnSugarListener sugarListener;

  public SugarAdapter(OnSugarListener sugarListener) {
    this.sugarListener = sugarListener;
  }

  @NonNull
  @Override
  public SugarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ItemSugarBinding sugarBinding = ItemSugarBinding.inflate(layoutInflater, parent, false);
    return new SugarHolder(sugarBinding, sugarListener);
  }

  @Override
  public void onBindViewHolder(@NonNull SugarHolder holder, int position) {
    Sugar current_sugar = sugars.get(position);
    holder.sugarBinding.itemSugarQuantity.setText(current_sugar.getQuantity());
    holder.sugarBinding.itemSugarDescription.setText(current_sugar.getDescription());
    holder.sugarBinding.itemSugarTime.setText(current_sugar.getDate());
  }

  @Override
  public int getItemCount() {
    return sugars.size();
  }

  public void setSugars(List<Sugar> sugars) {
    this.sugars = sugars;
    notifyDataSetChanged();
  }

  public Sugar getSugarAt(int position) {
    return sugars.get(position);
  }

  class SugarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ItemSugarBinding sugarBinding;
    private OnSugarListener onSugarListener;

    public SugarHolder(@NonNull ItemSugarBinding sugarBinding, OnSugarListener sugarListener) {
      super(sugarBinding.getRoot());
      this.sugarBinding = sugarBinding;
      this.onSugarListener =sugarListener;

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      onSugarListener.onSugarClick(getAdapterPosition());
    }
  }

  public interface OnSugarListener {
    void onSugarClick(int position);
  }

}
