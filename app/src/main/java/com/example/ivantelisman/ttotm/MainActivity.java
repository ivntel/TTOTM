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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mDiffInDays;
    private boolean notificationHasShown;
    private boolean fromNotificationActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ViewModel for this screen.

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

    //don't need else if
    @Override
    public void onBackPressed() {
        CalanderFragment myFragment = (CalanderFragment) getSupportFragmentManager().findFragmentByTag("calender");
        if (myFragment != null && myFragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
        } else if (PreferenceUtil.getInstance(this).getFromNotificationActivityBool()) {
            navigateOutOfApplication();
        } else{
            navigateOutOfApplication();
        }
    }

    public void triggerPushNotification() {
        //alarm for push notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mCurrentDate.set(Calendar.HOUR_OF_DAY, 12);
        mCurrentDate.set(Calendar.MINUTE, 00);
        mCurrentDate.set(Calendar.SECOND, 00);

        if (mDiffInDays <= 0 && mDiffInDays >= -4 || mDiffInDays == 14) {
            try {
                if (System.currentTimeMillis() < mCurrentDate.getTimeInMillis()) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCurrentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void navigateOutOfApplication() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}

