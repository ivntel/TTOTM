package com.example.ivantelisman.ttotm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ivantelisman.ttotm.fragments.CalanderFragment;
import com.example.ivantelisman.ttotm.fragments.MainFragment;
import com.example.ivantelisman.ttotm.pushNotification.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Calendar mDay0Calender = Calendar.getInstance();
    private Calendar mDay1Calender = Calendar.getInstance();
    private Calendar mDay2Calender = Calendar.getInstance();
    private Calendar mDay3Calender = Calendar.getInstance();
    private Calendar mDay4Calender = Calendar.getInstance();
    private Calendar mDay14Calender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigateToMainFragment();
    }

    @Override
    protected void onDestroy() {
        triggerPushNotification();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        triggerPushNotification();
        super.onPause();
    }

    @Override
    protected void onResume() {
        triggerPushNotification();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        CalanderFragment myFragment = (CalanderFragment) getSupportFragmentManager().findFragmentByTag("calender");
        if (myFragment != null && myFragment.isVisible()){
            navigateToMainFragment();
        } else {
            navigateOutOfApplication();
        }
    }

    public void triggerPushNotification() {
        //alarm for push notification
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager3 = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager4 = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager6 = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        getNotificationDaysAndSetUpTime();

        try {
            if (System.currentTimeMillis() < mDay0Calender.getTimeInMillis()) {
                alarmManager1.setExact(AlarmManager.RTC, mDay0Calender.getTimeInMillis(), broadcast);
            }
            if (System.currentTimeMillis() < mDay1Calender.getTimeInMillis()) {
                alarmManager2.setExact(AlarmManager.RTC, mDay1Calender.getTimeInMillis(), broadcast);
            }
            if (System.currentTimeMillis() < mDay2Calender.getTimeInMillis()) {
                alarmManager3.setExact(AlarmManager.RTC, mDay2Calender.getTimeInMillis(), broadcast);
            }
            if (System.currentTimeMillis() < mDay3Calender.getTimeInMillis()) {
                alarmManager4.setExact(AlarmManager.RTC, mDay3Calender.getTimeInMillis(), broadcast);
            }
            if (System.currentTimeMillis() < mDay4Calender.getTimeInMillis()) {
                alarmManager5.setExact(AlarmManager.RTC, mDay4Calender.getTimeInMillis(), broadcast);
            }
            if (System.currentTimeMillis() < mDay14Calender.getTimeInMillis()) {
                alarmManager6.setExact(AlarmManager.RTC, mDay14Calender.getTimeInMillis(), broadcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateOutOfApplication() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void getNotificationDaysAndSetUpTime() {
        List<Integer> daysList = PreferenceUtil.getInstance(this).getNotificationDates();
        mDay0Calender.set(Calendar.DAY_OF_YEAR, daysList.get(0));
        mDay0Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay0Calender.set(Calendar.MINUTE, 02);
        mDay0Calender.set(Calendar.SECOND, 00);
        mDay1Calender.set(Calendar.DAY_OF_YEAR, daysList.get(1));
        mDay1Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay1Calender.set(Calendar.MINUTE, 02);
        mDay1Calender.set(Calendar.SECOND, 00);
        mDay2Calender.set(Calendar.DAY_OF_YEAR, daysList.get(2));
        mDay2Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay2Calender.set(Calendar.MINUTE, 00);
        mDay2Calender.set(Calendar.SECOND, 00);
        mDay3Calender.set(Calendar.DAY_OF_YEAR, daysList.get(3));
        mDay3Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay3Calender.set(Calendar.MINUTE, 00);
        mDay3Calender.set(Calendar.SECOND, 00);
        mDay4Calender.set(Calendar.DAY_OF_YEAR, daysList.get(4));
        mDay4Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay4Calender.set(Calendar.MINUTE, 00);
        mDay4Calender.set(Calendar.SECOND, 00);
        mDay14Calender.set(Calendar.DAY_OF_YEAR, daysList.get(5));
        mDay14Calender.set(Calendar.HOUR_OF_DAY, 15);
        mDay14Calender.set(Calendar.MINUTE, 00);
        mDay14Calender.set(Calendar.SECOND, 00);
    }

    private void navigateToMainFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
    }

}

