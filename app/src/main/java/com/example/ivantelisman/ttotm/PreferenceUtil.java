package com.example.ivantelisman.ttotm;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class PreferenceUtil {
    static PreferenceUtil preferenceUtil;
    Context context;
    SharedPreferences sharedPreferences;

    public PreferenceUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
    }

    /**
     * Returns the instance of the Shared preference.
     *
     * @param context is the context of the activity that called PreferenceUtils.
     */
    public static PreferenceUtil getInstance(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveDifferenceInDays(int differenceInDays) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("DIFFERENCE_IN_DAYS", differenceInDays);
        editor.commit();
    }

    /**
     * Returns the DifferenceInDays as a int.
     */
    public int getDifferenceInDays() {
        return sharedPreferences.getInt("DIFFERENCE_IN_DAYS", 100);
    }

    public void saveFromNotificationActivityBool(boolean fromNotificationActivity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FROM_NOTIFICATION_ACTIVITY", fromNotificationActivity);
        editor.commit();
    }

    /**
     * Returns the FromNotificationActivityBool as a boolean.
     */
    public boolean getFromNotificationActivityBool() {
        return sharedPreferences.getBoolean("FROM_NOTIFICATION_ACTIVITY", false);
    }

    public void saveNotificationHasShown(boolean notificationHasShown) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NOTIFICATION_HAS_SHOWN", notificationHasShown);
        editor.commit();
    }

    /**
     * Returns the NotificationHasShown as a boolean.
     */
    public boolean getNotificationHasShown() {
        return sharedPreferences.getBoolean("NOTIFICATION_HAS_SHOWN", false);
    }

    public void saveNotificationDates(String day0, String day1, String day2, String day3, String day4, String day14) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DAY_ZERO", day0);
        editor.putString("DAY_ONE", day1);
        editor.putString("DAY_TWO", day2);
        editor.putString("DAY_THREE", day3);
        editor.putString("DAY_FOUR", day4);
        editor.putString("DAY_FOURTEEN", day14);
        editor.commit();
    }

    /**
     * Returns the NotificationHasShown as a boolean.
     */
    public List<String> getNotificationDates() {
        String day0 = sharedPreferences.getString("DAY_ZERO", null);
        String day1 = sharedPreferences.getString("DAY_ONE", null);
        String day2 = sharedPreferences.getString("DAY_TWO", null);
        String day3 = sharedPreferences.getString("DAY_THREE", null);
        String day4 = sharedPreferences.getString("DAY_FOUR", null);
        String day14 = sharedPreferences.getString("DAY_FOURTEEN", null);
        List<String> list = new ArrayList<>();
        list.add(day0);
        list.add(day1);
        list.add(day2);
        list.add(day3);
        list.add(day4);
        list.add(day14);
        return list;
    }

}
