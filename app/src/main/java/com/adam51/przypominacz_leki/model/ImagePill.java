package com.adam51.przypominacz_leki.model;

public class ImagePill {
  private String name;
  private int PillImage;

  public ImagePill(String name, int pillImage) {
    this.name = name;
    this.PillImage = pillImage;
  }

  public String getName() {
    return name;
  }

  public int getPillImage() {
    return PillImage;
  }
}
