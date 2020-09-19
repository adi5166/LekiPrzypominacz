package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam51.przypominacz_leki.adapter.SugarAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentSugarBinding;
import com.adam51.przypominacz_leki.model.Sugar;
import com.adam51.przypominacz_leki.viewmodel.SugarViewModel;

import java.util.List;

public class SugarFragment extends Fragment implements SugarAdapter.OnSugarListener {
  private FragmentSugarBinding sugarBinding;
  private SugarViewModel sugarViewModel;
  private SugarAdapter sugarAdapter;
  private NavController navController;

  public SugarFragment() {
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    sugarBinding = FragmentSugarBinding.inflate(inflater, container, false);
    return sugarBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);
    sugarAdapter = new SugarAdapter(this);


    sugarBinding.sugarRecycler.setAdapter(sugarAdapter);
    sugarViewModel = new ViewModelProvider(this).get(SugarViewModel.class);
    setHasOptionsMenu(true);

    sugarViewModel.getAllSugars().observe(getViewLifecycleOwner(), new Observer<List<Sugar>>() {
      @Override
      public void onChanged(List<Sugar> sugars) {
        sugarAdapter.setSugars(sugars);
      }
    });

    sugarBinding.sugarAddMeasurement.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.navigate(SugarFragmentDirections.actionSugarFragmentToAddSugarFragment(null).setMode("Add").setToolbarName("Add Measurement"));
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    sugarBinding = null;
  }

  @Override
  public void onSugarClick(int position) {
    navController.navigate(SugarFragmentDirections.actionSugarFragmentToAddSugarFragment(sugarAdapter.getSugarAt(position))
            .setMode("Edit")
            .setToolbarName("Edit Measurement")
            .setSugarPosition(position));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
  }
}
