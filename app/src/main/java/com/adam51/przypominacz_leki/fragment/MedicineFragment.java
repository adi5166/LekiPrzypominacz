package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.adapter.MedicineAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentMedicineBinding;

public class MedicineFragment extends Fragment {

  private FragmentMedicineBinding medicineBinding;
  private MedicineAdapter medicineAdapter;

  public MedicineFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    medicineBinding = FragmentMedicineBinding.inflate(inflater, container, false);
    View view = medicineBinding.getRoot();

    medicineAdapter = new MedicineAdapter();
    // Add LayoutManager to xml
    //medicineBinding.medicineRecycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
    medicineBinding.medicineRecycle.setAdapter(medicineAdapter);

    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    medicineBinding = null;
  }
}