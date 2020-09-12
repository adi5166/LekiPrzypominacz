package com.adam51.przypominacz_leki.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding mainBinding;
  private NavController navController;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mainBinding.getRoot());

    BottomNavigationView bottomNavigationView = mainBinding.bottomNavigationView;
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
      @Override
      public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() == R.id.pillDetailFragment ||
                destination.getId() == R.id.addEditPillFragment) {
          mainBinding.bottomNavigationView.setVisibility(View.GONE);
          //getActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        } else {
          mainBinding.bottomNavigationView.setVisibility(View.VISIBLE);
        }
      }
    });

    NavigationUI.setupWithNavController(bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController(this, navController);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    navController.navigateUp();
    return super.onSupportNavigateUp();
  }
}