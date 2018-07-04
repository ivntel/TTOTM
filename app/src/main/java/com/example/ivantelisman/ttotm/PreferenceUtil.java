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

    public void saveNotificationDates(int day0, int day1, int day2, int day3, int day4, int day14) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("DAY_ZERO", day0);
        editor.putInt("DAY_ONE", day1);
        editor.putInt("DAY_TWO", day2);
        editor.putInt("DAY_THREE", day3);
        editor.putInt("DAY_FOUR", day4);
        editor.putInt("DAY_FOURTEEN", day14);
        editor.commit();
    }

    /**
     * Returns the NotificationHasShown as a boolean.
     */
    public List<Integer> getNotificationDates() {
        int day0 = sharedPreferences.getInt("DAY_ZERO", 0);
        int day1 = sharedPreferences.getInt("DAY_ONE", 0);
        int day2 = sharedPreferences.getInt("DAY_TWO", 0);
        int day3 = sharedPreferences.getInt("DAY_THREE", 0);
        int day4 = sharedPreferences.getInt("DAY_FOUR", 0);
        int day14 = sharedPreferences.getInt("DAY_FOURTEEN", 0);
        List<Integer> list = new ArrayList<>();
        list.add(day0);
        list.add(day1);
        list.add(day2);
        list.add(day3);
        list.add(day4);
        list.add(day14);
        return list;
    }

}
