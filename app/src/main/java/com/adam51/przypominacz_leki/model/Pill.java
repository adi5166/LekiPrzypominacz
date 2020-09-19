package com.adam51.przypominacz_leki.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pill_table")
public class Pill implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String name;
  private String description;
  private String picPath;
  private int radioId;


  public Pill(String name, String description, String picPath, int radioId) {
    this.name = name;
    this.description = description;
    this.picPath = picPath;
    this.radioId = radioId;
  }

  protected Pill(Parcel in) {
    id = in.readInt();
    name = in.readString();
    description = in.readString();
    picPath = in.readString();
    radioId = in.readInt();
  }

  public static final Creator<Pill> CREATOR = new Creator<Pill>() {
    @Override
    public Pill createFromParcel(Parcel in) {
      return new Pill(in);
    }

    @Override
    public Pill[] newArray(int size) {
      return new Pill[size];
    }
  };

  public void setId(int id) {
    this.id = id;
  }

  public void setRadioId(int radioId) {
    this.radioId = radioId;
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

  public int getRadioId() {
    return radioId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(picPath);
    dest.writeInt(radioId);
  }
}
