package com.skytel.sdp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager implements Constants {

    public String TAG = PrefManager.class.getName();

    SharedPreferences pref;
    public static PrefManager instance;
    SharedPreferences.Editor editor;
    Context context;

    // Constructor
    public PrefManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static PrefManager getSessionInstance() {
        return instance;
    }

    public static void setSessionInstance(PrefManager session) {
        instance = session;
    }

    public boolean getIsLoggedIn() {
        return pref.getBoolean(PREF_ISLOGGEDIN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(PREF_ISLOGGEDIN, isLoggedIn);
        editor.commit();
    }



}
