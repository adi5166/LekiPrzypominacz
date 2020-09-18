package com.adam51.przypominacz_leki;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import static android.content.ContentValues.TAG;

public class App extends Application {
  public static final String ALARM_ACTION = "com.adam51.ALARM_ACTION";
  public static final String ALARM_EXTRA_STRING = "com.adam51.EXTRA_STRING";
  public static final String CHANNEL_ID = "notification";
  private NotificationManagerCompat notificationManager;


  @Override
  public void onCreate() {
    super.onCreate();
    notificationManager = NotificationManagerCompat.from(this);

    if (!notificationManager.areNotificationsEnabled()) {
      openNotificationSettings();
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            isChannelBlock(CHANNEL_ID)) {
      openChannelSettings(CHANNEL_ID);
      return;
    }
    createChannelNotification();
  }

  private void createChannelNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
              CHANNEL_ID,
              "Alarm Notification",
              NotificationManager.IMPORTANCE_HIGH
      );
      channel.setDescription("This is alarm notification");

      if (getSystemService(NotificationManager.class) != null) {
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        Log.d(TAG, "createChannelNotification: getSystemService OK");
      }
    }
  }

  private void openNotificationSettings() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
      intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
      startActivity(intent);
    } else {
      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      intent.setData(Uri.parse("package:" + getPackageName()));
      startActivity(intent);
    }
  }

  @RequiresApi(26)
  private boolean isChannelBlock(String channelId) {
    NotificationManager manager = getSystemService(NotificationManager.class);
    NotificationChannel channel = manager.getNotificationChannel(channelId);

    return channel != null &&
            channel.getImportance() == NotificationManager.IMPORTANCE_NONE;
  }

  @RequiresApi(26)
  private void openChannelSettings(String channelId) {
    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
    startActivity(intent);
  }
}
