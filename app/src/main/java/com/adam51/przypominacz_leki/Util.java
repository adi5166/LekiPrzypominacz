package com.adam51.przypominacz_leki;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Util {

  @Nullable
  public static void SetImageView(@Nullable Context context, String picPath, @Nullable ImageView imageView) {

    //TODO co gdy ctx lub image jest null
    Drawable drawable;
    if (!picPath.isEmpty()) {
      switch (picPath) {
        case "pill_oval_orange":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_orange);
          break;
        case "pill_oval_blue":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_blue);
          break;
        default:
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
      }
    } else drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
    imageView.setImageDrawable(drawable);
  }
  /*
    Can use:
    drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval);
    drawable = AppCompatResources.getDrawable(context, R.drawable.pill_oval);

    Can't use
    drawable = ResourcesCompat.getDrawable(Resources.getSystem(), R.drawable.pill_oval, null);
  */
}
