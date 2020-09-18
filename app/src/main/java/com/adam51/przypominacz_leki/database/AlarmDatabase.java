package com.adam51.przypominacz_leki.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adam51.przypominacz_leki.dao.AlarmDao;
import com.adam51.przypominacz_leki.model.Alarm;

@Database(entities = Alarm.class, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {

  private static AlarmDatabase instance;
  public abstract AlarmDao alarmDao();

  public static synchronized AlarmDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
              AlarmDatabase.class, "alarm_database")
              .fallbackToDestructiveMigration()
              .build();
    }
    return instance;
  }
}
