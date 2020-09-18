package com.adam51.przypominacz_leki.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class Alarm {
  @PrimaryKey(autoGenerate = true)
  private int id;

  private int pill_id;
  private int hour;
  private int minute;
  private boolean active;
  private boolean setup;

  public Alarm(int pill_id, int hour, int minute, boolean active, boolean setup) {
    this.pill_id = pill_id;
    this.hour = hour;
    this.minute = minute;
    this.active = active;
    this.setup = setup;
  }

  public int getId() {
    return id;
  }

  public int getPill_id() {
    return pill_id;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public boolean isActive() {
    return active;
  }

  public boolean isSetup() {
    return setup;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSetup(boolean setup) {
    this.setup = setup;
  }
}