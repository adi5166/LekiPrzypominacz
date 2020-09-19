package com.adam51.przypominacz_leki.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sugar_table")
public class Sugar implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String quantity;
  private String description;
  private String date;

  public Sugar(String quantity, String description, String date) {
    this.quantity = quantity;
    this.description = description;
    this.date = date;
  }

  protected Sugar(Parcel in) {
    id = in.readInt();
    quantity = in.readString();
    description = in.readString();
    date = in.readString();
  }

  public static final Creator<Sugar> CREATOR = new Creator<Sugar>() {
    @Override
    public Sugar createFromParcel(Parcel in) {
      return new Sugar(in);
    }

    @Override
    public Sugar[] newArray(int size) {
      return new Sugar[size];
    }
  };

  public int getId() {
    return id;
  }

  public String getQuantity() {
    return quantity;
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
    dest.writeString(quantity);
    dest.writeString(description);
    dest.writeString(date);
  }
}
