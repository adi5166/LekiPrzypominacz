package com.adam51.przypominacz_leki.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.adam51.przypominacz_leki.helper.NotificationHelper;

import static android.content.ContentValues.TAG;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_INT;
import static com.adam51.przypominacz_leki.App.ALARM_EXTRA_STRING;

public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "onReceive: Recive message");
    String id_text =  intent.getStringExtra(ALARM_EXTRA_STRING);
    String title = "Get your pill";
    int id = intent.getIntExtra(ALARM_EXTRA_INT, 1);

    NotificationHelper notificationHelper = new NotificationHelper(context);
    //NotificationCompat.Builder nBilder = notificationHelper.getChannelNotification();
    NotificationCompat.Builder nBilder = notificationHelper.getChannelNotification(title, id_text);
    notificationHelper.getManager().notify(id, nBilder.build());
  }
}
