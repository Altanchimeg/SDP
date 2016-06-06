package com.skytel.sdp.utils;

import android.widget.EditText;
import android.widget.Spinner;

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
    public static boolean isSpinnerSelected(int id) {
        if (id<0) {

            return false;
        }
        return true;
    }
}
