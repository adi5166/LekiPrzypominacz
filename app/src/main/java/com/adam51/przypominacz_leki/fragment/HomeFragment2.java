package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam51.przypominacz_leki.adapter.HomeAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentHome2Binding;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.List;

public class HomeFragment2 extends Fragment {
  private FragmentHome2Binding binding;
  private NavController navController;
  private HomeAdapter homeAdapter;

  public HomeFragment2() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentHome2Binding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);


    homeAdapter = new HomeAdapter();
    binding.recyclerHome2.setAdapter(homeAdapter);

    AlarmViewModel alarmViewModel = new ViewModelProvider(getActivity()).get(AlarmViewModel.class);
    PillViewModel pillViewModel = new ViewModelProvider(getActivity()).get(PillViewModel.class);

    pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {
      @Override
      public void onChanged(List<Pill> pills) {
        homeAdapter.setPillList(pills);
      }
    });

    alarmViewModel.getActiveAlarms(true).observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        homeAdapter.setAlarmList(alarms);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}