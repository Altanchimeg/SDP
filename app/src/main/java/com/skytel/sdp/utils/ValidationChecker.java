package com.skytel.sdp.utils;

import android.widget.EditText;

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
}
