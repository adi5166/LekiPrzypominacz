package com.adam51.przypominacz_leki.fragment;

import android.content.Context;
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
import com.adam51.przypominacz_leki.helper.Util;
import com.adam51.przypominacz_leki.adapter.MedicineAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentDetailPillBinding;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

public class PillDetailFragment extends Fragment {
  private FragmentDetailPillBinding detailPillBinding;
  private PillViewModel pillViewModel;
  private NavController navController;
  private MedicineAdapter medicineAdapter;

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
    setHasOptionsMenu(true);


    final Context context = getContext();
    final int position;
    if (getArguments() == null) {
      navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
      Toast.makeText(getContext(), R.string.error_get_pill_detail, Toast.LENGTH_SHORT).show();
    } else {
      //TODO zamienić z pozycji na obiekt Pill
      position = PillDetailFragmentArgs.fromBundle(getArguments()).getPillId();
      Pill pill = PillDetailFragmentArgs.fromBundle(getArguments()).getPill();

      detailPillBinding.pillDetailName.setText(pill.getName());
      detailPillBinding.pillDetailDescription.setText(pill.getDescription());
      String meal;
      switch (pill.getRadioId()) {
        case R.id.radio_pill_anytime: {
          meal = "Anytime";
          break;
        }
        case R.id.radio_pill_before: {
          meal = "Before meal";
          break;
        }
        case R.id.radio_pill_at: {
          meal = "At meal";
          break;
        }
        case R.id.radio_pill_after: {
          meal = "After meal";
          break;
        }
        default:
          meal = "Any time";
      }
      detailPillBinding.detailPillMeal.setText(meal);

      Util.SetPillImageView(context, pill.getPicPath(), detailPillBinding.imageView);

        /*
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

         */
    }
      /*
    } catch (NullPointerException en) {
      Log.d(TAG, "NullPointerException at getAllPills");
      navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
      Toast.makeText(getContext(), R.string.error_get_pill_detail, Toast.LENGTH_SHORT).show();
    }
    */
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    //menu.clear();
    inflater.inflate(R.menu.detail_pill_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (getArguments() != null) {
      switch (item.getItemId()) {
        case R.id.menu_icon_pill_edit: {
          int position = PillDetailFragmentArgs.fromBundle(getArguments()).getPillId();
          navController.navigate(PillDetailFragmentDirections.actionEditPillFromDetail(
                  PillDetailFragmentArgs.fromBundle(getArguments()).getPill())
                  .setMode("Edit")
                  .setPillPosition(position)
                  .setToolbarName("Edit Pill"));
          return true;
        }
        case R.id.menu_icon_pill_delete: {
          Pill pill = AddEditPillFragmentArgs.fromBundle(getArguments()).getPill();
          AlarmViewModel alarmViewModel = new ViewModelProvider(getActivity()).get(AlarmViewModel.class);
          alarmViewModel.deleteAlarmFromPill(pill.getId());
          pillViewModel.delete(pill);

          navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
          Toast.makeText(getActivity(), "Pill deleted", Toast.LENGTH_SHORT).show();
          return true;
        }
        default:
          break;
      }
    } else {
      Toast.makeText(getActivity(), "Error with Pill", Toast.LENGTH_SHORT).show();
    }
    return false;
  }
}
