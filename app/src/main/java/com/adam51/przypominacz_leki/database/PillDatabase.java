package com.adam51.przypominacz_leki.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.adam51.przypominacz_leki.model.Pill;
import com.adam51.przypominacz_leki.dao.PillDao;

@Database(entities = {Pill.class}, version = 1)
public abstract class PillDatabase extends RoomDatabase {

  private static PillDatabase instance;

  public abstract PillDao pillDao();

  public static synchronized PillDatabase getInstance(Context context) {
    if(instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(),
              PillDatabase.class, "pill_database")
              .fallbackToDestructiveMigration()
              .addCallback(roomCallback)
              .build();
    }
    return instance;
  }

  private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      new PopulateDbAsyncTask(instance).execute();
    }
  };

  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
    private PillDao pillDao;

    private PopulateDbAsyncTask(PillDatabase db) {
      pillDao = db.pillDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      pillDao.insert(new Pill("Name 1","Description 1","pill_oval_orange"));
      pillDao.insert(new Pill("Name 2","Description 2","pill_oval_blue"));
      return null;
    }
  }
}
