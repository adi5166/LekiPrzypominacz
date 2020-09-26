package com.adam51.przypominacz_leki.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adam51.przypominacz_leki.model.Pressure;

import java.util.List;

@Dao
public interface PressureDao {
  @Insert
  void insert(Pressure pressure);

  @Update
  void update(Pressure pressure);

  @Delete
  void delete(Pressure pressure);

  @Query("DELETE FROM pressure_table")
  void deleteAllPressures();

  @Query("SELECT * FROM pressure_table")
  LiveData<List<Pressure>> getAllPressures();
}
