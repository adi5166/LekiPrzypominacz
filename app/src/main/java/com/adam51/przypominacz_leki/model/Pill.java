package com.adam51.przypominacz_leki.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pill_table")
public class Pill {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String name;
  private String description;
  private String picPath;

  public Pill(String name, String description, String picPath) {
    this.name = name;
    this.description = description;
    this.picPath = picPath;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getPicPath() {
    return picPath;
  }
}
