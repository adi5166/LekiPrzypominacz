package com.adam51.przypominacz_leki.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adam51.przypominacz_leki.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

  @Insert
  void insert (Alarm alarm);

  @Update
  void update(Alarm alarm);

  @Delete
  void delete(Alarm alarm);

  @Query("DELETE FROM alarm_table")
  void deleteAllAlarm();

  @Query("DELETE FROM alarm_table WHERE pill_id = :pill_id")
  void deleteAlarmFromPill(int pill_id);

  @Query("SELECT * FROM alarm_table WHERE pill_id =:pill_id")
  LiveData<List<Alarm>> getAlarmFromPill(int pill_id);

  @Query("SELECT * FROM alarm_table")
  LiveData<List<Alarm>> getAllAlarms();
}
