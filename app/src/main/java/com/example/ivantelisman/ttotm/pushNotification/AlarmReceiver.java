package com.example.ivantelisman.ttotm.pushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.ivantelisman.ttotm.R;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "push_notification";
    private String message = "Default Message";
    private int mDiffInDays;
    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mDiffInDays = preferences.getInt("DIFFERENCE_IN_DAYS", 0);

        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        setUpMessage();

        Notification notification = builder.setContentTitle("That Time Of The Month Notification")
                .setContentText(message)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }

    private void setUpMessage() {
        if (mDiffInDays == -4) {
            message = "Remember to be nice!";
        } else if (mDiffInDays == -3) {
            message = "Bring Home Flowers!";
        } else if (mDiffInDays == -2) {
            message = "Bring Home Food!";
        } else if (mDiffInDays == -1) {
            message = "Be Very Nice!";
        } else if (mDiffInDays == 0) {
            message = "It's that time of the month!";
        } else if (mDiffInDays == 14) {
            message = "Pick up some condoms your woman is very fertile!";
        }
    }

}
