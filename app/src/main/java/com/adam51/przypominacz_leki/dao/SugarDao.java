package com.adam51.przypominacz_leki.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adam51.przypominacz_leki.model.Sugar;

import java.util.List;

@Dao
public interface SugarDao {
  @Insert
  void insert(Sugar sugar);

  @Update
  void update(Sugar sugar);

  @Delete
  void delete(Sugar sugar);

  @Query("DELETE FROM sugar_table")
  void deleteAllAlarm();

  @Query("SELECT * FROM sugar_table")
  LiveData<List<Sugar>> getAllSugars();

}
