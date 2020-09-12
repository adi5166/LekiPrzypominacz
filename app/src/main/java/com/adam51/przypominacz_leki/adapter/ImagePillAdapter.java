package com.adam51.przypominacz_leki.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.model.ImagePill;

import java.util.ArrayList;

public class ImagePillAdapter extends ArrayAdapter<ImagePill> {

  public ImagePillAdapter(Context context, ArrayList<ImagePill> imagePillsList) {
    super(context, 0, imagePillsList);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position, convertView, parent);
  }

  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position, convertView, parent);
  }

  private View initView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(
              R.layout.spinner_pill_row, parent, false
      );
    }
    ImageView imageViewPill = convertView.findViewById(R.id.spinner_image_view_row);

    ImagePill currentItem = getItem(position);
    if (currentItem != null) {
      imageViewPill.setImageResource(currentItem.getPillImage());
    }

    return convertView;
  }
}
