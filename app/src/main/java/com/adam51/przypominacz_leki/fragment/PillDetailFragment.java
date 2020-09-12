package com.adam51.przypominacz_leki.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
  private NavController navController;

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

    navController = Navigation.findNavController(view);

    try {
      final Context context = getContext();
      final int position;
      if (getArguments() == null) {
        navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
        Toast.makeText(getContext(), R.string.error_get_pill_detail, Toast.LENGTH_SHORT).show();
      } else {
        //TODO zamienić z pozycji na obiekt Pill
        position = PillDetailFragmentArgs.fromBundle(getArguments()).getPillId();
        pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {
          @Override
          public void onChanged(List<Pill> pills) {
            Pill pill = Objects.requireNonNull(pillViewModel.getAllPills().getValue()).get(position);
            //pobieranie obiektu na danej pozycji
            detailPillBinding.pillDetailDescription.setText(pill.getDescription());
            detailPillBinding.pillDetailName.setText(pill.getName());
            Util.SetPillImageView(context, pill.getPicPath(), detailPillBinding.imageView);
          }
        });
      }
    } catch (NullPointerException en) {
      Log.d(TAG, "NullPointerException at getAllPills");
      navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
      Toast.makeText(getContext(), R.string.error_get_pill_detail, Toast.LENGTH_SHORT).show();
    }
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
