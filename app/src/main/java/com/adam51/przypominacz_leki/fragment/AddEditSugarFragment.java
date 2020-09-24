package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.FragmentAddEditSugarBinding;
import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.model.Sugar;
import com.adam51.przypominacz_leki.viewmodel.SugarViewModel;

import java.util.Calendar;
import java.util.Locale;

public class AddEditSugarFragment extends Fragment {
  private FragmentAddEditSugarBinding sugarBinding;
  private NavController navController;
  private SugarViewModel sugarViewModel;

  public AddEditSugarFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    sugarBinding = FragmentAddEditSugarBinding.inflate(inflater, container, false);
    return sugarBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);
    sugarViewModel = new ViewModelProvider(getActivity()).get(SugarViewModel.class);
    setHasOptionsMenu(true);

    if (getArguments() != null) {
      String mode = AddEditSugarFragmentArgs.fromBundle(getArguments()).getMode();
      if ("Add".equals(mode)) {
        sugarBinding.sugarInputAddButton.setVisibility(View.VISIBLE);
        sugarBinding.sugarInputEditButton.setVisibility(View.INVISIBLE);

        Calendar c = Calendar.getInstance();
        String time = c.get(Calendar.DAY_OF_MONTH) + "."
                + (c.get(Calendar.MONTH) + 1) + " - "
                + c.get(Calendar.HOUR_OF_DAY) + ":"
                + c.get(Calendar.MINUTE);
        sugarBinding.sugarInputTimeText.setText(time);

        sugarBinding.sugarInputAddButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            saveSugar();
            Util.hideKeyboard(getContext(), v);
          }
        });

      } else if ("Edit".equals(mode)) {
        sugarBinding.sugarInputAddButton.setVisibility(View.INVISIBLE);
        sugarBinding.sugarInputEditButton.setVisibility(View.VISIBLE);

        final int position = AddEditSugarFragmentArgs.fromBundle(getArguments()).getSugarPosition();
        if (position != -5) {
          Sugar current_sugar = AddEditSugarFragmentArgs.fromBundle(getArguments()).getSugar();
          sugarBinding.sugerDetailNumber.setText(current_sugar.getQuantity());
          sugarBinding.sugerDetailDescription.setText(current_sugar.getDescription());
          sugarBinding.sugarInputTimeText.setText(current_sugar.getDate());

          sugarBinding.sugarInputEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updateSugar();
              Util.hideKeyboard(getContext(), v);
            }
          });
        }

      }
    }
  }

  public void saveSugar() {
    if (isDataValid()) {
      String quantity = sugarBinding.textSugarInputNumber.getEditText().getText().toString();
      Sugar sugar = new Sugar(quantity,
              sugarBinding.sugarDetailDescription.getEditText().getText().toString(),
              sugarBinding.sugarInputTimeText.getText().toString());

      sugarViewModel.insert(sugar);
      Toast.makeText(getActivity(), "Measurement saved", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
      return;
    }
    navController.navigate(AddEditSugarFragmentDirections.actionAddSugarFragmentToSugarFragment());
  }

  public void updateSugar() {
    if (isDataValid()) {
      if (getArguments() != null) {
        String quantity = sugarBinding.textSugarInputNumber.getEditText().getText().toString();
        Sugar sugar = new Sugar(quantity,
                sugarBinding.sugarDetailDescription.getEditText().getText().toString(),
                sugarBinding.sugarInputTimeText.getText().toString());

        sugar.setId(AddEditSugarFragmentArgs.fromBundle(getArguments()).getSugar().getId());
        sugarViewModel.update(sugar);
        Toast.makeText(getActivity(), "Measurement saved", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
        return;
      }
    } else {
      Toast.makeText(getActivity(), "Error while saving data", Toast.LENGTH_SHORT).show();
    }
    navController.navigate(AddEditSugarFragmentDirections.actionAddSugarFragmentToSugarFragment());
  }

  public void deleteSugar() {
    if (getArguments() != null) {
      Sugar sugar = AddEditSugarFragmentArgs.fromBundle(getArguments()).getSugar();


      sugarViewModel.delete(sugar);
      Toast.makeText(getActivity(), "Measurement deleted", Toast.LENGTH_SHORT).show();
      navController.navigate(AddEditSugarFragmentDirections.actionAddSugarFragmentToSugarFragment());
    } else {
      Toast.makeText(getActivity(), "Error with Measurement", Toast.LENGTH_SHORT).show();
    }
  }

  private boolean isDataValid() {
    return !(sugarBinding.textSugarInputNumber.getEditText().getText().toString().isEmpty());
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    if (getArguments() != null) {
      switch (AddEditSugarFragmentArgs.fromBundle(getArguments()).getMode()) {
        case "Add": {
          menu.clear();
          inflater.inflate(R.menu.add_sugar_menu, menu);
          break;
        }
        case "Edit": {
          menu.clear();
          inflater.inflate(R.menu.edit_sugar_menu, menu);
          break;
        }
      }
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_icon_sugar_save: {
        saveSugar();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_sugar_update: {
        updateSugar();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      case R.id.menu_icon_sugar_delete: {
        deleteSugar();
        Util.hideKeyboard(getContext(), getView().getRootView());
        return true;
      }
      default:
        break;
    }
    return false;
  }
}
