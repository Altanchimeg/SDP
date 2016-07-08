package com.skytel.sdp.utils;

import java.text.SimpleDateFormat;

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
    String PREF_AUTH_TOKEN = "AUTH_TOKEN";
    String PREF_DEALER_NAME = "dealer_name";
    String PREF_DEALER_BALANCE = "dealer_balance";
    String PREF_DEALER_ZONE = "dealer_zone";

    /* Service Dealer Service Functions */
    String SERVER_URL = "http://10.1.90.65:8080/cosax-service-dealer/";
    String FUNCTION_LOGIN = "login.json";
    String FUNCTION_CHARGE = "charge.json";
    String FUNCTION_GET_INVOICE = "getinvoice.json";
    String FUNCTION_DO_PAYMENT = "dopayment.json";
    String FUNCTION_FORGET = "forget.json";
    String FUNCTION_RECOVER = "recover.json";
    String FUNCTION_CHANGE_PASSWORD = "changepass.json";
    String FUNCTION_CHANGE_PIN = "changepin.json";
    String FUNCTION_DEALER_REPORT = "dealerreport.json";
    String FUNCTION_SEND_FEEDBACK = "newcomment.json";
    String FUNCTION_PROFILE_INFO = "dealerinfo.json";
    String FUNCTION_RESERVE_NUMBER = "reservenumber.json";
    String FUNCTION_NEW_NUMBER_DETAIL = "newnumber.json";
    String FUNCTION_NEW_NUMBER_REPORT = "newnumber_report.json";
    String FUNCTION_DEALER_CHANNEL_INFO = "dealer_channel_info.json";
    String FUNCTION_NEW_DEALER = "newdealer.json";
    String FUNCTION_NEW_SKYMEDIA = "newskymedia.json";
    String FUNCTION_REGISTER_REPORT = "register_report.json";
    String FUNCTION_HANDSET_CHANGE_INFO = "simchange_info.json";
    String FUNCTION_HANDSET_CHANGE = "simchange.json";
    String FUNCTION_VAS_TYPE = "vas_info.json";
    String FUNCTION_VAS_CHANGE_STATE = "vaschange.json";
    String FUNCTION_SERVICE_REPORT = "service_report.json";

    /* Skytel.mn Service Functions */
    String SERVER_SKYTEL_MN_URL = "https://www.skytel.mn";
    String FUNCTION_GET_PREFIX = "/api/content/numberPrefixSearch";
    String FUNCTION_GET_PRICE = "/api/content/numberPrice";

    /* number.skytel.mn Service Functions */
    String SERVER_NUMBER_SKYTEL_URL = "http://number.skytel.mn/server/client.php";

    int RESULT_CODE_SUCCESS = 0;
    String RESULT_STATUS_SUCCESS = "success";

    int CONST_COLOR_DATA_PACKAGE = 0;
    int CONST_SKYTEL_NODAY_PACKAGE = 1;
    int CONST_SKYTEL_DAY_PACKAGE = 2;
    int CONST_SMART_PACKAGE = 3;


    int FILTER_ALL = 0;
    int FILTER_WAITING = 1;
    int FILTER_SUCCESS = 2;
    int FILTER_FAILED = 3;



}
