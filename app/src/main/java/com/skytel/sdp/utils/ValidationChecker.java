package com.skytel.sdp.utils;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by altanchimeg on 5/19/2016.
 */
public class ValidationChecker {
    public static boolean isValidationPassed(EditText editText) {
        String userInput = editText.getText().toString();
        if (userInput.isEmpty()) {

            return false;
        }
        return true;
    }
    public static boolean isValidationPassedTextView(TextView textView) {
        String userInput = textView.getText().toString();
        if (userInput.isEmpty()) {

            return false;
        }
        return true;
    }
    public static boolean isSelected(int id) {
        if (id<0) {

            return false;
        }
        return true;
    }
    public static boolean hasBitmapValue(Bitmap bm){
        if(bm == null){
            return false;
        }
        return true;
    }
    public static boolean isSimcardSerial(int length){
        if(length != 20){
            return false;
        }
        return true;
    }
}
