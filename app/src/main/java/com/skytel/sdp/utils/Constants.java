package com.skytel.sdp.utils;

/**
 * Created by Altanchimeg on 4/18/2016.
 */
public interface Constants {
    int MENU_NEWNUMBER = 0;
    int MENU_SKYDEALER = 1;
    int MENU_SERVICE = 2;
    int MENU_REGISTRATION = 3;
    int MENU_INFORMATION = 4;
    int MENU_PLAN = 5;
    int MENU_FEEDBACK = 6;
    int MENU_SETTINGS = 7;
    int MENU_LOGOUT = 8;

    /* Preference */
    String PREF_NAME = "sdp";
    String PREF_ISLOGGEDIN = "is_logged_in";


    /* Service Functions */
    String SERVER_URL = "http://192.168.1.1/";
    String FUNCTION_LOGIN = "login.json";
    String FUNCTION_CHARGE = "charge.json";

    int RESULT_CODE_SUCCESS = 0;

    int CONST_COLOR_DATA_PACKAGE = 0;
    int CONST_SKYTEL_NODAY_PACKAGE = 1;
    int CONST_SKYTEL_DAY_PACKAGE = 2;
    int CONST_SMART_PACKAGE = 3;

}
