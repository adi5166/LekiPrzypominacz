package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.adapter.ImagePillAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentAddEditPillBinding;
import com.adam51.przypominacz_leki.model.ImagePill;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.viewmodel.EditPillViewModel;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddEditPillFragment extends Fragment {
  private FragmentAddEditPillBinding addEditPillBinding;
  private NavController navController;
  private ArrayList<ImagePill> imagePillList;
  private ImagePillAdapter adapter;
  private PillViewModel pillViewModel;
  private EditPillViewModel editPillViewModel;

  public AddEditPillFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    addEditPillBinding = FragmentAddEditPillBinding.inflate(inflater, container, false);

    return addEditPillBinding.getRoot();

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);
    pillViewModel = new ViewModelProvider(getActivity()).get(PillViewModel.class);
    editPillViewModel = new ViewModelProvider(this).get(EditPillViewModel.class);
    setHasOptionsMenu(true);

    initSpinnerList();
    adapter = new ImagePillAdapter(getActivity(), imagePillList);
    addEditPillBinding.spinnerPillAdd.setAdapter(adapter);

    if (getArguments() != null) {
      String mode = AddEditPillFragmentArgs.fromBundle(getArguments()).getMode();
      if (mode.equals("Edit")) {

        final int position = AddEditPillFragmentArgs.fromBundle(getArguments()).getPillPosition();
        if (position != -5) {
          /*
          List<Pill> pill_list;
          pill_list = pillViewModel.getAllPills().getValue();
          editPillViewModel.setPill(pill_list.get(position));
          Log.d(TAG, "Pill Name: "+editPillViewModel.getPill().getName());
          */
          //TODO zmiana orientacji usuwa lokalne zmiany, ale pobiera z bazy poprawne   TODO utworzyć nowy ViweModel i pobrać dane z pillViewModel, wtedy pracujemy na kopi

          pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {

            @Override
            public void onChanged(List<Pill> pills) {

              //Pill current_pill = editPillViewModel.getPill();
              Pill current_pill = pillViewModel.getAllPills().getValue().get(position);
              addEditPillBinding.pillDetailName.setText(current_pill.getName());
              addEditPillBinding.pillDetailDescription.setText(current_pill.getDescription());
              //String picPath = current_pill.getPicPath();
              //imagePillList.get(0).getName();
            }

          });


        }


        //TODO edit
        Log.d(TAG, "onViewCreated: Edit mode");
      } else if (mode.equals("Add")) {
        Log.d(TAG, "onViewCreated: Add mode");
      }
    }

    addEditPillBinding.addPillButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        savePill();
      }
    });
  }
/*
  private int getSpinnerPosition(Spinner spinner, String string){
    imagePillList.
    for(int i=0; i<spinner.getCount(); i++)
      if(spinner.getItemAtPosition(i).getName())
  }
  */

  private void initSpinnerList() {
    imagePillList = new ArrayList<>();
    imagePillList.add(new ImagePill("pill_oval_blue", R.drawable.pill_oval_blue));
    imagePillList.add(new ImagePill("pill_oval_orange", R.drawable.pill_oval_orange));
    imagePillList.add(new ImagePill("pill_oval_green", R.drawable.pill_oval_green));
  }

  private void savePill() {
    try {
      if (!Objects.requireNonNull(addEditPillBinding.textInputName.getEditText()).getText().toString().isEmpty()
              && !Objects.requireNonNull(addEditPillBinding.textInputDescription.getEditText()).getText().toString().isEmpty()) {

        ImagePill imagePill = (ImagePill) addEditPillBinding.spinnerPillAdd.getSelectedItem();
        Pill pill = new Pill(addEditPillBinding.textInputName.getEditText().getText().toString(),
                addEditPillBinding.textInputDescription.getEditText().getText().toString(),
                imagePill.getName()
        );
        pillViewModel.insert(pill);

        Toast.makeText(getActivity(), "Pill saved", Toast.LENGTH_SHORT).show();

        navController.navigate(AddEditPillFragmentDirections.actionPillSavedBackToRecycler());


      } else {
        Toast.makeText(getActivity(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
      }
    } catch (NullPointerException ex) {
      Toast.makeText(getActivity(), "Error while saving data", Toast.LENGTH_SHORT).show();
    }
  }

  public void updatePill() {

  }

  public void deletePill() {
    int position = PillDetailFragmentArgs.fromBundle(getArguments()).getPillId();
    pillViewModel.delete(pillViewModel.getAllPills().getValue().get(position));
    navController.navigate(PillDetailFragmentDirections.actionPillDetailFragmentToMedicineFragment());
    Toast.makeText(getActivity(), "Pill deleted", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    switch (AddEditPillFragmentArgs.fromBundle(getArguments()).getMode()) {
      case "Add": {
        menu.clear();
        inflater.inflate(R.menu.add_pill_menu, menu);
        break;
      }
      case "Edit": {
        menu.clear();
        inflater.inflate(R.menu.edit_pill_menu, menu);
        break;
      }
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_icon_pill_save:
        savePill();
        return true;
      case R.id.menu_icon_pill_update:
        updatePill();
        return true;
      case R.id.menu_icon_pill_delete:
        deletePill();
        return true;
      default:
        break;
    }
    return false;
  }
}