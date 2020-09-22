package com.adam51.przypominacz_leki.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.R.color;
import com.adam51.przypominacz_leki.R.drawable;
import com.adam51.przypominacz_leki.activity.MainActivity;

import static com.adam51.przypominacz_leki.App.CHANNEL_ID;

public class NotificationHelper extends ContextWrapper {
  private NotificationManagerCompat mManager;

  public NotificationHelper(Context base) {
    super(base);
  }

  public NotificationManagerCompat getManager() {
    if (mManager == null) {
      mManager =  NotificationManagerCompat.from(getApplicationContext());
    }
    return mManager;
  }

  public NotificationCompat.Builder getChannelNotification() {
    return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setContentTitle("Alarm!")
            .setContentText("Your AlarmManager is working.")
            .setSmallIcon(drawable.ic_icon);
  }

  public NotificationCompat.Builder getChannelNotification(String title, String text, String color) {
    Intent resultIntent = new Intent(this, MainActivity.class);
    PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(drawable.ic_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setColor(getResources().getColor(Util.getColor(color)));
  }
}

