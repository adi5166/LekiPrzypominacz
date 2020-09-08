package com.adam51.przypominacz_leki.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adam51.przypominacz_leki.model.Pill;

import java.util.List;

@Dao
public interface PillDao {

  @Insert
  void insert(Pill pill);

  @Update
  void update(Pill pill);

  @Delete
  void delete(Pill pill);

  @Query("DELETE FROM pill_table")
  void deleteAllPill();

  @Query("SELECT * FROM pill_table")
  LiveData<List<Pill>> getAllPills();

  //TODO dodać metode zwracającą po ID ?
}
