package com.adam51.przypominacz_leki.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.adapter.AlarmAdapter;
import com.adam51.przypominacz_leki.adapter.HomeAdapter;
import com.adam51.przypominacz_leki.databinding.FragmentHomeBinding;
import com.adam51.przypominacz_leki.model.Alarm;
import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.receiver.AlarmReceiver;
import com.adam51.przypominacz_leki.viewmodel.AlarmViewModel;
import com.adam51.przypominacz_leki.viewmodel.PillViewModel;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_INT;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_STRING;

public class HomeFragment extends Fragment {
  private FragmentHomeBinding binding;
  private NavController navController;
  private HomeAdapter homeAdapter;
  private AlarmViewModel alarmViewModel;
  private PillViewModel pillViewModel;

  public HomeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);

    homeAdapter = new HomeAdapter();
    binding.recyclerHome.setAdapter(homeAdapter);

    alarmViewModel = new ViewModelProvider(getActivity()).get(AlarmViewModel.class);
    pillViewModel = new ViewModelProvider(getActivity()).get(PillViewModel.class);

    pillViewModel.getAllPills().observe(getViewLifecycleOwner(), new Observer<List<Pill>>() {
      @Override
      public void onChanged(List<Pill> pills) {
        homeAdapter.setPillList(pills);
      }
    });

    alarmViewModel.unSetupAllAlarms(false);
    homeAdapter.setAlarmList(alarmViewModel.getActiveAlarms(true).getValue());
    alarmViewModel.getActiveAlarms(true).observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
      @Override
      public void onChanged(List<Alarm> alarms) {
        homeAdapter.setAlarmList(alarms);
        for (int i = 0; i < alarms.size(); i++) {
          Alarm alarm = alarms.get(i);
          if (alarm.isActive() && !alarm.isSetup()) {
            startAlarm(i);
            alarm.setSetup(true);
            alarmViewModel.update(alarm);
            break;
          }
        }
      }
    });
  }

  private void startAlarm(int position) {
    Log.d(TAG, "startAlarm: fun");
    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Alarm alarm = homeAdapter.getAlarmAt(position);
    int id = alarm.getId();
    if (id != 0) {
      //TODO dostać z image
      //String name = homeAdapter.getPillAt(homeAdapter.getPillID(position)).getName();
      String name = "";
      intent.putExtra(ALARM_EXTRA_STRING, name);
      intent.putExtra(ALARM_EXTRA_INT, id);
    }
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    calendar.set(Calendar.MINUTE, alarm.getMinute());
    calendar.set(Calendar.SECOND, 0);

    if (calendar.before(Calendar.getInstance())) {
      calendar.add(Calendar.DATE, 1);
    }

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}