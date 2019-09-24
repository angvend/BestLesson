package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Contatta extends AppCompatActivity {

    private EditText destinatarioET, oggettoET, messaggioET;
    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatta);

        destinatarioET = (EditText) findViewById(R.id.txtEmail);
        oggettoET = (EditText) findViewById(R.id.txtOggetto);
        messaggioET = (EditText) findViewById(R.id.textMessaggio);

        intent = getIntent();

        if(intent.getStringExtra("emailPassata") != null){
            destinatarioET.setText(intent.getStringExtra("emailPassata"));
        }

        Button invia = findViewById(R.id.btn_invia);
        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail(){
        String recipientList = destinatarioET.getText().toString();
        String[] recipients = recipientList.split(",");

        String oggetto = oggettoET.getText().toString();
        String messaggio = messaggioET.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, oggetto);
        intent.putExtra(Intent.EXTRA_TEXT, messaggio);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Scegli un email") );
    }
}
