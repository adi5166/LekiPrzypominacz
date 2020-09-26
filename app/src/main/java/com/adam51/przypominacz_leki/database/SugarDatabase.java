package com.adam51.przypominacz_leki.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adam51.przypominacz_leki.dao.SugarDao;
import com.adam51.przypominacz_leki.model.Sugar;

@Database(entities = Sugar.class, version = 1)
public abstract class SugarDatabase extends RoomDatabase {
  private static SugarDatabase instance;
  public abstract SugarDao sugarDao();

  public static synchronized SugarDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
              SugarDatabase.class, "sugar_database")
              .fallbackToDestructiveMigration()
              .build();
    }
    return instance;
  }
}
