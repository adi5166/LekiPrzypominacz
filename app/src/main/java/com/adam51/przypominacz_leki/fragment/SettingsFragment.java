package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam51.przypominacz_leki.databinding.FragmentSettingsBinding;
import com.adam51.przypominacz_leki.viewmodel.PressureViewModel;
import com.adam51.przypominacz_leki.viewmodel.SugarViewModel;

public class SettingsFragment extends Fragment {
  private FragmentSettingsBinding binding;
  private SugarViewModel sugarViewModel;
  private PressureViewModel pressureViewModel;
  private NavController navController;

  public SettingsFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }


  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);

    navController = Navigation.findNavController(view);
    sugarViewModel = new ViewModelProvider(getActivity()).get(SugarViewModel.class);
    pressureViewModel = new ViewModelProvider(getActivity()).get(PressureViewModel.class);


    binding.settingsDeleteSugarButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sugarViewModel.deleteAllSugars();
        Toast.makeText(getContext(), "All sugar measurement deleted", Toast.LENGTH_SHORT).show();
      }
    });

    binding.settingsDeletePressureButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pressureViewModel.deleteAllPressures();
        Toast.makeText(getContext(), "All blood pressure measurement deleted", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
  }
}
