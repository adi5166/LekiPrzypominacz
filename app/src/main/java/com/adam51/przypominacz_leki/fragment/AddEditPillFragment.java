package com.adam51.przypominacz_leki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddEditPillFragment extends Fragment {
  private FragmentAddEditPillBinding addEditPillBinding;
  private NavController navController;
  private ArrayList<ImagePill> imagePillList;
  private ImagePillAdapter adapter;
  private PillViewModel pillViewModel;

  public AddEditPillFragment() {
    // Required empty public constructor
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
    pillViewModel = new ViewModelProvider(this).get(PillViewModel.class);
    setHasOptionsMenu(true);

    initSpinnerList();
    adapter = new ImagePillAdapter(getActivity(), imagePillList);
    addEditPillBinding.spinnerPillAdd.setAdapter(adapter);

    addEditPillBinding.addPillButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        savePill();
      }
    });

    String mode = AddEditPillFragmentArgs.fromBundle(getArguments()).getMode();
    if(mode.equals("Edit")){
      //TODO edit
      Log.d(TAG, "onViewCreated: Edit mode");
    }else if(mode.equals("Add")){
      Log.d(TAG, "onViewCreated: Add mode");
    }
  }

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
    }catch (NullPointerException ex){
      Toast.makeText(getActivity(), "Error while saving data", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    switch (AddEditPillFragmentArgs.fromBundle(getArguments()).getMode()){
      case "Add":
        menu.clear();
        inflater.inflate(R.menu.add_pill_menu, menu);
        break;
        //TODO dodać meny edit
    }

    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save_pill:
        savePill();
        return true;
      default:
        break;
    }
    return false;
  }
}