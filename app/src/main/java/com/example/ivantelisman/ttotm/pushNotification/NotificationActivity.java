package com.example.ivantelisman.ttotm.pushNotification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ivantelisman.ttotm.MainActivity;
import com.example.ivantelisman.ttotm.PreferenceUtil;
import com.example.ivantelisman.ttotm.R;

import java.util.Calendar;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    TextView notificationMessage;
    private Calendar mCurrentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationMessage = findViewById(R.id.notificationMessage);
        openedNotificationMessage();
    }

    private void openedNotificationMessage(){
        List<Integer> daysList = PreferenceUtil.getInstance(this).getNotificationDates();
        if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(4)) {
            notificationMessage.setText(getString(R.string.day_4_extended));
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(3)) {
            notificationMessage.setText(getString(R.string.day_3_extended));
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(2)) {
            notificationMessage.setText(getString(R.string.day_2_extended));
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(1)) {
            notificationMessage.setText(getString(R.string.day_1_extended));
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(0)) {
            notificationMessage.setText(getString(R.string.day_0_extended));
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(5)) {
            notificationMessage.setText(getString(R.string.day_14_extended));
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
