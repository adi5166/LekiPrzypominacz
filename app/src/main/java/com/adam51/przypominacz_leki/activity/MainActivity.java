package com.adam51.przypominacz_leki.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding mainBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mainBinding.getRoot());
    //View view = binding.getRoot();
    //setContentView(view);
    //setContentView(R.layout.activity_main);
    //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

    BottomNavigationView bottomNavigationView = mainBinding.bottomNavigationView;

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    //TODO chowanie bottom bar
    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
      @Override
      public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if(destination.getId() == R.id.pillDetailFragment) {
          mainBinding.bottomNavigationView.setVisibility(View.INVISIBLE);
        }else {
          mainBinding.bottomNavigationView.setVisibility(View.VISIBLE);
        }
      }
    });

    NavigationUI.setupWithNavController(bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController(this,navController);
  //TODO działające appBar - przyciski powrotu i nie tylko


  }
}