package com.adam51.przypominacz_leki.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adam51.przypominacz_leki.dao.PressureDao;
import com.adam51.przypominacz_leki.model.Pressure;

@Database(entities = Pressure.class, version = 1)
public abstract class PressureDatabase extends RoomDatabase {
  private static PressureDatabase instance;
  public abstract PressureDao pressureDao();

  public static synchronized PressureDatabase getInstance(Context context){
    if(instance == null){
      instance = Room.databaseBuilder(context.getApplicationContext(),
              PressureDatabase.class, "pressure_database")
              .fallbackToDestructiveMigration()
              .build();
    }
    return instance;
  }
}
