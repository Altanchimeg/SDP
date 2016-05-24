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
    String PREF_ISLOGGEDOUT = "is_logged_out";
    String PREF_AUTH_TOKEN = "auth_token";


    /* Service Functions */
    String SERVER_URL = "http://10.1.90.65:8080/cosax-service-dealer/";
    String FUNCTION_LOGIN = "login.json";
    String FUNCTION_CHARGE = "charge.json";
    String FUNCTION_GET_INVOICE = "getinvoice.json";
    String FUNCTION_DO_PAYMENT = "dopayment.json";
    String FUNCTION_FORGET = "forget.json";
    String FUNCTION_RECOVER = "recover.json";
    String FUNCTION_CHANGE_PASSWORD = "changepass.json";
    String FUNCTION_CHANGE_PIN = "changepin.json";

    int RESULT_CODE_SUCCESS = 0;
    String RESULT_STATUS_SUCCESS = "success";

    int CONST_COLOR_DATA_PACKAGE = 0;
    int CONST_SKYTEL_NODAY_PACKAGE = 1;
    int CONST_SKYTEL_DAY_PACKAGE = 2;
    int CONST_SMART_PACKAGE = 3;

    boolean DEBUG = false;

}
