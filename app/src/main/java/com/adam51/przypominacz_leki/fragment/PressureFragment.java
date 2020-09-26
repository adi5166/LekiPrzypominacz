package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.adapter.PressureAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentPressureBinding;
import com.adam51.przypominacz_leki.model.Pressure;
import com.adam51.przypominacz_leki.viewmodel.PressureViewModel;

import java.util.List;

public class PressureFragment extends Fragment implements PressureAdapter.OnPressureListener {
  private FragmentPressureBinding binding;
  private PressureViewModel pressureViewModel;
  private PressureAdapter adapter;
  private NavController navController;

  public PressureFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentPressureBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);
    adapter = new PressureAdapter(this);
    binding.pressureRecycler.setAdapter(adapter);

    pressureViewModel = new ViewModelProvider(this).get(PressureViewModel.class);
    setHasOptionsMenu(true);

    pressureViewModel.getAllPressures().observe(getViewLifecycleOwner(), new Observer<List<Pressure>>() {
      @Override
      public void onChanged(List<Pressure> pressures) {
        adapter.setPressures(pressures);
      }
    });

    binding.pressureAddMeasurement.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.navigate(PressureFragmentDirections.actionPressureFragmentToAddEditPressureFragment(null)
                .setMode("Add")
                .setToolbarName("Add Measurement"));
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  @Override
  public void onPressureClick(int position) {
    navController.navigate(PressureFragmentDirections.actionPressureFragmentToAddEditPressureFragment(adapter.getPressureAt(position))
            .setMode("Edit")
            .setToolbarName("Edit Measurement")
            .setPressurePosition(position));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
  }
}