package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.FragmentAddEditPressureBinding;
import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.model.Pressure;
import com.adam51.przypominacz_leki.viewmodel.PressureViewModel;

import java.util.Calendar;

public class AddEditPressureFragment extends Fragment {
  private FragmentAddEditPressureBinding pressureBinding;
  private PressureViewModel pressureViewModel;
  private NavController navController;

  public AddEditPressureFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    pressureBinding = FragmentAddEditPressureBinding.inflate(inflater, container, false);
    return pressureBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);
    pressureViewModel = new ViewModelProvider(this).get(PressureViewModel.class);
    setHasOptionsMenu(true);

    if (getArguments() != null) {
      String mode = AddEditPressureFragmentArgs.fromBundle(getArguments()).getMode();
      if ("Add".equals(mode)) {
        pressureBinding.pressureInputAddButton.setVisibility(View.VISIBLE);
        pressureBinding.pressureInputEditButton.setVisibility(View.INVISIBLE);

        Calendar c = Calendar.getInstance();
        String time = c.get(Calendar.DAY_OF_MONTH) + "."
                + (c.get(Calendar.MONTH) + 1) + " - "
                + c.get(Calendar.HOUR_OF_DAY) + ":"
                + c.get(Calendar.MINUTE);

        pressureBinding.textPressureTime.setText(time);

        pressureBinding.pressureInputAddButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            savePressure();
            Util.hideKeyboard(getContext(), v);
          }
        });
      } else if ("Edit".equals(mode)) {
        pressureBinding.pressureInputAddButton.setVisibility(View.INVISIBLE);
        pressureBinding.pressureInputEditButton.setVisibility(View.VISIBLE);

        final int position = AddEditPressureFragmentArgs.fromBundle(getArguments()).getPressurePosition();
        if (position != -5) {
          Pressure current_pressure = AddEditPressureFragmentArgs.fromBundle(getArguments()).getPressure();
          pressureBinding.pressureDetailHigh.setText(current_pressure.getHigh_pressure());
          pressureBinding.pressureDetailLow.setText(current_pressure.getLow_pressure());
          pressureBinding.pressureDetailDescription.setText(current_pressure.getDescription());
          pressureBinding.textPressureTime.setText(current_pressure.getDate());

          pressureBinding.pressureInputEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updatePressure();
              Util.hideKeyboard(getContext(), v);
            }
          });
        }
      }
    }
  }

  private void savePressure() {
    if (isDataValid()) {
      Pressure pressure = new Pressure(pressureBinding.textPressureInputLow.getEditText().getText().toString(),
              pressureBinding.textPressureInputHigh.getEditText().getText().toString(),
              pressureBinding.textPressureInputDescription.getEditText().getText().toString(),
              pressureBinding.textPressureTime.getText().toString());
      pressureViewModel.insert(pressure);
      Toast.makeText(getActivity(), "Measurement saved", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
      return;
    }
    navController.navigate(AddEditPressureFragmentDirections.actionAddEditPressureFragmentToPressureFragment());
  }

  private void updatePressure() {
    if (isDataValid()) {
      if (getArguments() != null) {
        Pressure pressure = new Pressure(pressureBinding.textPressureInputLow.getEditText().getText().toString(),
                pressureBinding.textPressureInputHigh.getEditText().getText().toString(),
                pressureBinding.textPressureInputDescription.getEditText().getText().toString(),
                pressureBinding.textPressureTime.getText().toString());
        pressure.setId(AddEditPressureFragmentArgs.fromBundle(getArguments()).getPressure().getId());
        pressureViewModel.update(pressure);
        Toast.makeText(getActivity(), "Measurement updated", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getActivity(), "Error while saving data", Toast.LENGTH_SHORT).show();
        return;
      }
    } else {
      Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
      return;
    }
    navController.navigate(AddEditPressureFragmentDirections.actionAddEditPressureFragmentToPressureFragment());

  }

  private void deletePressure() {
    if (getArguments() != null) {
      Pressure pressure = AddEditPressureFragmentArgs.fromBundle(getArguments()).getPressure();

      pressureViewModel.delete(pressure);
      Toast.makeText(getActivity(), "Measurement deleted", Toast.LENGTH_SHORT).show();
      navController.navigate(AddEditPressureFragmentDirections.actionAddEditPressureFragmentToPressureFragment());
    } else {
      Toast.makeText(getActivity(), "Error with Measurement", Toast.LENGTH_SHORT).show();
    }
  }

  private boolean isDataValid() {
    return !(pressureBinding.textPressureInputHigh.getEditText().getText().toString().isEmpty()
            || pressureBinding.textPressureInputLow.getEditText().getText().toString().isEmpty());
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    if (getArguments() != null) {
      switch (AddEditPressureFragmentArgs.fromBundle(getArguments()).getMode()) {
        case "Add": {
          menu.clear();
          inflater.inflate(R.menu.add_pressure_menu, menu);
          break;
        }
        case "Edit": {
          menu.clear();
          inflater.inflate(R.menu.edit_pressure_menu, menu);
          break;
        }
      }
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_icon_pressure_save: {
        savePressure();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_pressure_update: {
        updatePressure();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_pressure_delete: {
        deletePressure();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      default:
        break;
    }
    return false;
  }
}