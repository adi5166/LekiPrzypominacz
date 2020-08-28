package com.adam51.przypominacz_leki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.adam51.przypominacz_leki.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    //View view = binding.getRoot();
    //setContentView(view);
    //setContentView(R.layout.activity_main);
    //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

    BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController(this,navController);
  }
}