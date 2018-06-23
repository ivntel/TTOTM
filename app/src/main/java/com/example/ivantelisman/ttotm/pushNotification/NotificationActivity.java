package com.example.ivantelisman.ttotm.pushNotification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ivantelisman.ttotm.MainActivity;
import com.example.ivantelisman.ttotm.R;

public class NotificationActivity extends AppCompatActivity {
    TextView notificationMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationMessage = findViewById(R.id.notificationMessage);
        openedNotificationMessage();
    }

    private void openedNotificationMessage(){
        if(MainActivity.diffInDays == -4){
            notificationMessage.setText("Remember to be nice");
        }
        else if(MainActivity.diffInDays == -3){
            notificationMessage.setText("Remember to be nice");
        }
        else if(MainActivity.diffInDays == -2){
            notificationMessage.setText("Remember to be nice");
        }
        else if(MainActivity.diffInDays == -1){
            notificationMessage.setText("Remember to be nice");
        }
        else if(MainActivity.diffInDays == 0){
            notificationMessage.setText("Remember to be nice");
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
