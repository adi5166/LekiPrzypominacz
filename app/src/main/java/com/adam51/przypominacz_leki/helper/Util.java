package com.adam51.przypominacz_leki.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.adam51.przypominacz_leki.R;
import com.adam51.przypominacz_leki.model.ImagePill;

import static android.content.ContentValues.TAG;

public class Util {

  public static void SetPillImageView(int resId, @Nullable ImageView imageView) {
    if (imageView == null) {
      return;
    }

    try {
      imageView.setImageResource(resId);
    } catch (Exception ex) {
      Log.e(TAG, "SetPillImageView: error with resId", ex);
      imageView.setImageResource(R.drawable.pill_oval);
    }
    /*
    switch (resId) {
      case R.drawable.pill_oval_orange:
        imageView.setImageResource(R.drawable.pill_oval_orange);
        break;
      case R.drawable.pill_oval_blue:
        imageView.setImageResource(R.drawable.pill_oval_blue);
        break;
      case R.drawable.pill_oval_green:
        imageView.setImageResource(R.drawable.pill_oval_green);
        break;
      default:
        imageView.setImageResource(R.drawable.pill_oval);
    }
    */
  }

  public static void SetPillImageView(@Nullable Context context, String picPath, @Nullable ImageView imageView) {

    if (context == null || imageView == null) {
      return;
    }
    Drawable drawable;
    if (!picPath.isEmpty()) {
      switch (picPath) {
        case "pill_oval_orange":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_orange);
          break;
        case "pill_oval_blue":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_blue);
          break;
        case "pill_oval_green":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_green);
          break;
        case "pill_oval_red":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_red);
          break;
        case "pill_oval_violet":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_violet);
          break;
        case "pill_oval_yellow":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_yellow);
          break;
        case "pill_oval_lime":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_lime);
          break;
        case "pill_oval_pink":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_pink);
          break;
        case "pill_oval_brown":
          drawable = ContextCompat.getDrawable(context, R.drawable.pill_oval_brown);
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

  public static void hideKeyboard(Context context, View view) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }
}
