package com.dm.rosary.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

/**
 * Created by User on 2015-10-06.
 */
public class PreferenceHelper implements SharedPreferences {

    public static final String APP_SHARED_PREFS = "rosary_prefs";

    SharedPreferences prefs;
    static PreferenceHelper helper;

    public static PreferenceHelper getInstance(Context context){
        if (helper != null){
            return helper;
        }else{
            helper = new PreferenceHelper(context);
            return helper;
        }

    }
    public PreferenceHelper(Context context){
        if (context!=null) {
            prefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        }
    }

    public SharedPreferences getPrefs(){
        return prefs;
    }

    @Override
    public Map<String, ?> getAll() {
        return prefs.getAll();
    }

    @Override
    public String getString(String s, String s2) {
        return prefs.getString(s, s2);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Set<String> getStringSet(String s, Set<String> strings) {
        return prefs.getStringSet(s, strings);
    }

    @Override
    public int getInt(String s, int i) {
        return prefs.getInt(s, i);
    }

    @Override
    public long getLong(String s, long l) {
        return prefs.getLong(s, l);
    }

    @Override
    public float getFloat(String s, float v) {
        return prefs.getFloat(s, v);
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        return prefs.getBoolean(s, b);
    }

    @Override
    public boolean contains(String s) {
        return prefs.contains(s);
    }

    @Override
    public Editor edit() {
        return prefs.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }
    public void DeletePreferences(){
        SharedPreferences.Editor editor = helper.edit();
        editor.clear();
        editor.commit();
        editor.apply();
    }
}
