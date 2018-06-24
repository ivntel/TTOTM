package com.example.ivantelisman.ttotm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ivantelisman.ttotm.fragments.CalanderFragment;
import com.example.ivantelisman.ttotm.fragments.MainFragment;
import com.example.ivantelisman.ttotm.pushNotification.AlarmReceiver;
import com.example.ivantelisman.ttotm.pushNotification.UpdateByDay;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Calendar mCurrentDate = Calendar.getInstance();
    private Calendar mCurrentDateDayUpdate = Calendar.getInstance();
    private int mDiffInDays;
    private boolean notificationHasShown;
    private boolean fromNotificationActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ViewModel for this screen.

        triggerPushNotification();
        mDiffInDays = PreferenceUtil.getInstance(this).getDifferenceInDays();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        triggerPushNotification();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        PreferenceUtil.getInstance(this).saveFromNotificationActivityBool(false);
        triggerPushNotification();
        super.onPause();
    }

    @Override
    protected void onResume() {
        triggerPushNotification();
        mDiffInDays = PreferenceUtil.getInstance(this).getDifferenceInDays();
        fromNotificationActivity = PreferenceUtil.getInstance(this).getFromNotificationActivityBool();
        notificationHasShown = fromNotificationActivity;
        Log.d("onResume: ", String.valueOf(fromNotificationActivity) + " " + String.valueOf(notificationHasShown));
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        CalanderFragment myFragment = (CalanderFragment) getSupportFragmentManager().findFragmentByTag("calender");
        if (myFragment != null && myFragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
        } else if (PreferenceUtil.getInstance(this).getFromNotificationActivityBool()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else{
            super.onBackPressed();
        }
    }

    public void triggerPushNotification() {

        //alarm for updating boolean for push notification
        AlarmManager alarmManagerDayUpdate = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentDayUpdate = new Intent(this, UpdateByDay.class);
        PendingIntent broadcastDayUpdate = PendingIntent.getBroadcast(this, 100, intentDayUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        mCurrentDateDayUpdate.set(Calendar.HOUR_OF_DAY, 11);
        mCurrentDateDayUpdate.set(Calendar.MINUTE, 59);
        mCurrentDateDayUpdate.set(Calendar.SECOND, 00);

        try {
            //alarmManagerDayUpdate.setRepeating(AlarmManager.RTC_WAKEUP, mCurrentDateDayUpdate.getTimeInMillis(), 12 * 60 * 60 * 1000, broadcastDayUpdate);
            alarmManagerDayUpdate.set(AlarmManager.RTC_WAKEUP, mCurrentDateDayUpdate.getTimeInMillis(), broadcastDayUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //alarm for push notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mCurrentDate.set(Calendar.HOUR_OF_DAY, 12);
        mCurrentDate.set(Calendar.MINUTE, 00);
        mCurrentDate.set(Calendar.SECOND, 00);

        if (mDiffInDays <= 0 && mDiffInDays >= -4 || mDiffInDays == 14 && !PreferenceUtil.getInstance(this).getUpdateDayBool()) {
            try {
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCurrentDate.getTimeInMillis(), 12 * 60 * 60 * 1000, broadcast);
                alarmManager.set(AlarmManager.RTC_WAKEUP, mCurrentDate.getTimeInMillis(), broadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

