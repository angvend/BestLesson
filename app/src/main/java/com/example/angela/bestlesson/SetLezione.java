package com.example.angela.bestlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SetLezione extends AppCompatActivity{


    private EditText nome;
    private TextView ora;
    private TextView lezione;

    private TimePicker inizio;
    private TimePicker fine;

    private String tempoPassato;
    private String time;

    private Intent intent;

    Button btn_imposta;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lezione);

        ora = (TextView) findViewById(R.id.time);

        lezione = (TextView) findViewById(R.id.lezione);

        inizio = (TimePicker) findViewById(R.id.datePicker1);
        fine = (TimePicker) findViewById(R.id.datePicker2);

        inizio.setIs24HourView(true);
        fine.setIs24HourView(true);

        intent = getIntent();

        tempoPassato = intent.getStringExtra("passaggioTempo");

        time = tempoPassato + ".00";

        Log.d("AAA", time);

        ora.setText(time);

        nome = (EditText) findViewById(R.id.nomeStudente);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        btn_imposta = findViewById(R.id.btn_imposta);

        mAuth = FirebaseAuth.getInstance();




        btn_imposta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard(v);
                impostaLezione();
            }

            public void hideKeyboard(View v) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void impostaLezione(){

        final String nomeStudente = nome.getText().toString().trim();

        if (TextUtils.isEmpty(nomeStudente)) {
            Toast.makeText(getApplicationContext(), "Per favore inserisci l'email...", Toast.LENGTH_LONG).show();
            return;
        }


        //CODICE PUT LEZIONE IN DATABASE

    }

    @Override
    protected void onResume() {
        ora.setText(time);
        super.onResume();

    }
}




