package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SetLezione extends AppCompatActivity {

    private TextView timeView;
    private Intent extras;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lezione);

        timeView = (TextView) findViewById(R.id.time);
        extras = getIntent();
        time = extras.getStringExtra("TIME");
        timeView.setText(time);
    }

}
