package com.example.ivantelisman.ttotm;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void saveUpdateDayBool(boolean updateDay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NOTIFICATION_HAS_SHOWN", updateDay);
        editor.commit();
    }

    /**
     * Returns the NotificationHasShown as a boolean.
     */
    public boolean getUpdateDayBool() {
        return sharedPreferences.getBoolean("NOTIFICATION_HAS_SHOWN", true);
    }

}
