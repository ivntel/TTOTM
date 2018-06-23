package com.example.ivantelisman.ttotm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ivantelisman.ttotm.db.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    //public AppDatabase mDb;
    Date d = new Date();
    Date c = new Date();
    Calendar calendarEstimatedtDate = Calendar.getInstance();
    Calendar calendarSelectedDate = Calendar.getInstance();
    Calendar currentDate = Calendar.getInstance();
    public static int diffInDays = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Note: Db references should not be in an activity.
        //mDb = AppDatabase.getInMemoryDatabase(this);

        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        subscribeUiUsers();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
    }

    /*@Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }*/

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            d = dateFormat.parse(estimatedStartDate);
            c = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("showDateInUiD: ", d.toString());

        calendarEstimatedtDate.setTime(d);
        calendarSelectedDate.setTime(c);
        //diffInDays = calendarSelectedDate.get(Calendar.DAY_OF_YEAR) - calendarEstimatedtDate.get(Calendar.DAY_OF_YEAR);
        diffInDays = currentDate.get(Calendar.DAY_OF_YEAR) - calendarEstimatedtDate.get(Calendar.DAY_OF_YEAR);
        if(diffInDays <= 0 && diffInDays >= -4){
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, broadcast);
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

