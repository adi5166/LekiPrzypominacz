package com.adam51.przypominacz_leki.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.ActivityMainBinding;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding mainBinding;
  private NavController navController;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mainBinding.getRoot());

    final BottomNavigationView bottomNavigationView = mainBinding.bottomNavigationView;
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController(this, navController);
    navController.navigate(R.id.homeFragment);

    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
      @Override
      public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() == R.id.pillDetailFragment
                || destination.getId() == R.id.addEditPillFragment
                || destination.getId() == R.id.settings
                || destination.getId() == R.id.addSugarFragment
                || destination.getId() == R.id.sugarFragment

        ) {
          mainBinding.bottomNavigationView.setVisibility(View.GONE);
        } else {
          mainBinding.bottomNavigationView.setVisibility(View.VISIBLE);
        }
        if (R.id.homeFragment == destination.getId()
                || R.id.medicineFragment == destination.getId()
                || R.id.surveyFragment == destination.getId()) {
          ActionBar actionBar = getSupportActionBar();
          if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
          }
        }
      }
    });


    //TODO unset alarms once
    //AlarmViewModel alarmViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AlarmViewModel.class);
    //alarmViewModel.unSetupAllAlarms(false);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.settings) {
      navController.navigate(R.id.settingsFragment);
      return true;
    } else return false;
  }

  @Override
  public boolean onSupportNavigateUp() {
    navController.navigateUp();
    return super.onSupportNavigateUp();
  }
}