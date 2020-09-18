package com.adam51.przypominacz_leki.helper;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adam51.przypominacz_leki.R.drawable;

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

  public NotificationCompat.Builder getChannelNotification(String title, String text) {
    return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(drawable.ic_icon);
  }
}

