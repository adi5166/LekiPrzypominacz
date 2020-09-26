package com.adam51.przypominacz_leki.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pressure_table")
public class Pressure implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String low_pressure;
  private String high_pressure;
  private String description;
  private String date;

  public Pressure(String low_pressure, String high_pressure, String description, String date) {
    this.low_pressure = low_pressure;
    this.high_pressure = high_pressure;
    this.description = description;
    this.date = date;
  }

  protected Pressure(Parcel in) {
    id = in.readInt();
    low_pressure = in.readString();
    high_pressure = in.readString();
    description = in.readString();
    date = in.readString();
  }

  public static final Creator<Pressure> CREATOR = new Creator<Pressure>() {
    @Override
    public Pressure createFromParcel(Parcel in) {
      return new Pressure(in);
    }

    @Override
    public Pressure[] newArray(int size) {
      return new Pressure[size];
    }
  };

  public int getId() {
    return id;
  }

  public String getLow_pressure() {
    return low_pressure;
  }

  public String getHigh_pressure() {
    return high_pressure;
  }

  public String getDescription() {
    return description;
  }

  public String getDate() {
    return date;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(low_pressure);
    dest.writeString(high_pressure);
    dest.writeString(description);
    dest.writeString(date);
  }
}
