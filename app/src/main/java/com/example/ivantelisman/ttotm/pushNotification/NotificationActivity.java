package com.example.ivantelisman.ttotm.pushNotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ivantelisman.ttotm.MainActivity;
import com.example.ivantelisman.ttotm.R;

public class NotificationActivity extends AppCompatActivity {
    TextView notificationMessage;
    private int mDiffInDays;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDiffInDays = preferences.getInt("DIFFERENCE_IN_DAYS", 0);

        notificationMessage = findViewById(R.id.notificationMessage);
        openedNotificationMessage();
    }

    private void openedNotificationMessage(){
        if (mDiffInDays == -4) {
            notificationMessage.setText("4");
        } else if (mDiffInDays == -3) {
            notificationMessage.setText("3");
        } else if (mDiffInDays == -2) {
            notificationMessage.setText("2");
        } else if (mDiffInDays == -1) {
            notificationMessage.setText("1");
        } else if (mDiffInDays == 0) {
            notificationMessage.setText("0");
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
