package com.example.bogdan.feastfordriver.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.example.bogdan.feastfordriver.FdApp;

/**
 * Util class to encapsulate Shared Preferences handling logic.
 * Created on 3/29/17.
 *
 * @author Evgenii Kanivets
 */

public class Prefs {

    private static final String KEY_LATITUDE = "key_latitude";
    private static final String KEY_LONGITUDE = "key_longitude";

    private static Prefs prefs;

    public static Prefs get() {
        if (prefs == null) {
            prefs = new Prefs(FdApp.get());
        }
        return prefs;
    }

    @NonNull
    private Context context;

    private Prefs(@NonNull Context context) {
        this.context = context;
    }

    public double readLatitude() {
        return getDefaultPrefs().getFloat(KEY_LATITUDE, 0);
    }

    public double readLongitude() {
        return getDefaultPrefs().getFloat(KEY_LONGITUDE, 0);
    }

    public void writeLatitude(double latitude) {
        SharedPreferences.Editor editor = getEditor();
        editor.putFloat(KEY_LATITUDE, (float) latitude);
        editor.apply();
    }

    public void writeLongitude(double longitude) {
        SharedPreferences.Editor editor = getEditor();
        editor.putFloat(KEY_LONGITUDE, (float) longitude);
        editor.apply();
    }

    @NonNull
    private SharedPreferences getDefaultPrefs() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @NonNull
    private SharedPreferences.Editor getEditor() {
        return getDefaultPrefs().edit();
    }

}
