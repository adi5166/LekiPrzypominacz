package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.FragmentSurveyBinding;

public class SurveyFragment extends Fragment {
  private FragmentSurveyBinding binding;
  private NavController navController;

  public SurveyFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentSurveyBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);

    binding.surveyButtonSugar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.navigate(SurveyFragmentDirections.actionSurveyFragmentToSugarFragment());
      }
    });

    binding.surveyButtonPressure.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.navigate(SurveyFragmentDirections.actionSurveyFragmentToPressureFragment());
      }
    });
  }
}