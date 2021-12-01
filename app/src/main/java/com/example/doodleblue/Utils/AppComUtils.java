package com.example.doodleblue.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppComUtils {
    private static final String PREFS_NAME = "MablizAdmin";
    public static void setPreferenceString(Context context, String key, String value) {
        if (context == null) {
            return;
        }

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String getPreferenceString(Context context, String key) {
        if (context == null) {
            return "";
        }

        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
