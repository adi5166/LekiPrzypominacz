package com.adam51.przypominacz_leki.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.Util;
import com.adam51.przypominacz_leki.databinding.FragmentDetailPillBinding;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class PillDetailFragment extends Fragment {
  private FragmentDetailPillBinding detailPillBinding;
  private PillViewModel pillViewModel;
  private Context context;
  private int position;

  public PillDetailFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    detailPillBinding = FragmentDetailPillBinding.inflate(inflater, container, false);
    View view = detailPillBinding.getRoot();

    pillViewModel = new ViewModelProvider(this).get(PillViewModel.class);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    try {
      context = getContext();
      if (getArguments() == null || context == null) {
        //TODO brak danych lub contextu powrót do Medicinefragment
      } else {
        position = PillDetailFragmentArgs.fromBundle(getArguments()).getPillId();
        pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {
          @Override
          public void onChanged(List<Pill> pills) {
            Pill pill = Objects.requireNonNull(pillViewModel.getAllPills().getValue()).get(position);
            //pobieranie obiektu na danej pozycji
            detailPillBinding.pillDetailDescription.setText(pill.getDescription());
            detailPillBinding.pillDetailName.setText(pill.getName());
            Util.SetImageView(context, pill.getPicPath(), detailPillBinding.imageView);
            /*
            Drawable drawable;

            if (!pill.getPicPath().isEmpty()) {
              switch (pill.getPicPath()) {
                case "pill_oval_orange":
                  drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_orange);
                  break;
                case "pill_oval_blue":
                  drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_blue);
                  break;
                default:
                  drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
              }
            } else drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
            detailPillBinding.imageView.setImageDrawable(drawable);
            */
          }
        });
      }
    } catch (NullPointerException en) {
      Log.d(TAG, "NullPointerException at getAllPills");
    }
    //odebranie z bazy konkretnego obiektu, chyba trzeba zmienić prametr na ID
    //zrobione przez ViewModel
  }
/*
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Log.d(TAG, "onDestroyView: destroy");
    pillViewModel.getAllPills().removeObservers(getViewLifecycleOwner());
    //TODO nie wiem czy potrzebne, czy ovservwer wie że owner kończy życie
  }
 */
}
