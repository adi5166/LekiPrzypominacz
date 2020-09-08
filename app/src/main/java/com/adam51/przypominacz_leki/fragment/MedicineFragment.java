package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.adapter.MedicineAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentMedicineBinding;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MedicineFragment extends Fragment implements MedicineAdapter.OnPillListener {

  private FragmentMedicineBinding medicineBinding;
  private MedicineAdapter medicineAdapter;
  private PillViewModel pillViewModel;

  public MedicineFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    medicineBinding = FragmentMedicineBinding.inflate(inflater, container, false);
    return medicineBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    medicineAdapter = new MedicineAdapter(this);
    // Add LayoutManager to xml
    //medicineBinding.medicineRecycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
    medicineBinding.medicineRecycle.setAdapter(medicineAdapter);

    //inne
    pillViewModel = new ViewModelProvider(this).get(PillViewModel.class);
    //getViewLifecycleOwner zamiast this
    pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {
      @Override
      public void onChanged(@Nullable List<Pill> pills) {
        medicineAdapter.setPills(pills);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    medicineBinding = null;
  }

  @Override
  public void onPillClick(int position) {
    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    Log.d(TAG, "onPillClick: clicked at "+position);
    navController.navigate(MedicineFragmentDirections.viewPillDetail(position));
  }
}