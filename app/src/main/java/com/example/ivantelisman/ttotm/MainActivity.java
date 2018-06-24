package com.example.ivantelisman.ttotm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ivantelisman.ttotm.db.User;
import com.example.ivantelisman.ttotm.fragments.CalanderFragment;
import com.example.ivantelisman.ttotm.fragments.MainFragment;
import com.example.ivantelisman.ttotm.pushNotification.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    Date mEstimatedDate = new Date();
    Date mSelectedDate = new Date();
    Calendar mCalendarEstimatedDate = Calendar.getInstance();
    Calendar mCalendarSelectedDate = Calendar.getInstance();
    Calendar mCurrentDate = Calendar.getInstance();
    private int mDiffInDays;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        subscribeUiUsers();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDiffInDays = preferences.getInt("DIFFERENCE_IN_DAYS", 0);
        Log.d("onCreate: ", String.valueOf(mDiffInDays));

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        //AppDatabase.destroyInstance();
        subscribeUiUsers();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CalanderFragment myFragment = (CalanderFragment) getSupportFragmentManager().findFragmentByTag("calender");
        if (myFragment != null && myFragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
        }
        else{
        super.onBackPressed();
        }
    }

    public void triggerPushNotification(String estimatedStartDate, String selectedDate){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mCurrentDate.set(Calendar.HOUR_OF_DAY, 10);
        mCurrentDate.set(Calendar.MINUTE, 00);
        mCurrentDate.set(Calendar.SECOND, 00);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            mEstimatedDate = dateFormat.parse(estimatedStartDate);
            mSelectedDate = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mCalendarEstimatedDate.setTime(mEstimatedDate);
        mCalendarSelectedDate.setTime(mSelectedDate);

        if (mDiffInDays <= 0 && mDiffInDays >= -4) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCurrentDate.getTimeInMillis(), 12 * 60 * 60 * 1000, broadcast);
        }
    }

    private void subscribeUiUsers() {
        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@NonNull final List<User> users) {
                try{
                triggerPushNotification(users.get(0).estimatedStartDate, users.get(0).date);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

}

