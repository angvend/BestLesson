package com.example.angela.bestlesson.Utility;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.example.angela.bestlesson.BasicActivity;
import com.example.angela.bestlesson.R;


public class NotificationActivity extends Activity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = new Intent(getApplicationContext(), BasicActivity.class);
        startActivity(intent);

        super.onCreate(savedInstanceState);
    }
}
