package com.example.ivantelisman.ttotm.pushNotification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.ivantelisman.ttotm.MainActivity;
import com.example.ivantelisman.ttotm.PreferenceUtil;
import com.example.ivantelisman.ttotm.R;

import java.util.Calendar;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    TextView notificationMessage;
    TextView appLink;
    String appPackageName = "com.amazon.mShop.android.shopping";
    private Calendar mCurrentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setUpUi();
        openedNotificationMessage();
    }

    private void setUpUi() {
        notificationMessage = findViewById(R.id.notificationMessage);
        appLink = findViewById(R.id.appLink);

        appLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    private void openedNotificationMessage(){
        List<Integer> daysList = PreferenceUtil.getInstance(this).getNotificationDates();
        if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(4)) {
            notificationMessage.setText(getString(R.string.day_4_extended));
            appPackageName = "com.jjwanda.compliments";
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(3)) {
            notificationMessage.setText(getString(R.string.day_3_extended));
            appPackageName = "com.amazon.mShop.android.shopping";
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(2)) {
            notificationMessage.setText(getString(R.string.day_2_extended));
            appPackageName = "com.amazon.mShop.android.shopping";
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(1)) {
            notificationMessage.setText(getString(R.string.day_1_extended));
            appPackageName = "com.edible.consumer";
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(0)) {
            notificationMessage.setText(getString(R.string.day_0_extended));
            appPackageName = "com.ubercab.eats";
        } else if (mCurrentDate.get(Calendar.DAY_OF_YEAR) == daysList.get(5)) {
            notificationMessage.setText(getString(R.string.day_14_extended));
            appPackageName = "com.shycartapp.shycart";
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
