package com.example.angela.bestlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SetLezione extends AppCompatActivity{


    private EditText nome;
    private TextView ora;
    private TextView lezione;
    private TextView textViewStudenti;

    EditText txtNome, txtCognome;

    private TimePicker inizio;
    private TimePicker fine;

    private String tempoPassato;
    private String time;

    private String email;

    Button conferma;


    ListView listView;

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

        textViewStudenti = (TextView) findViewById(R.id.textViewStudenti);

        txtNome = (EditText) findViewById(R.id.txtNome1);
        txtCognome = (EditText) findViewById(R.id.txtCognome1);

        listView = (ListView) findViewById(R.id.list_view1);
        conferma = (Button) findViewById(R.id.btn_conferma);

        inizio.setIs24HourView(true);
        fine.setIs24HourView(true);


        final ArrayList<String> listaStudenti = new ArrayList<>();
        final ArrayList<String> arrayListEdit = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEdit);

        intent = getIntent();

        tempoPassato = intent.getStringExtra("passaggioTempo");

        time = tempoPassato + ".00";

        Log.d("AAA", time);

        ora.setText(time);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        btn_imposta = findViewById(R.id.btn_imposta);

        mAuth = FirebaseAuth.getInstance();




        conferma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String idUtente = mAuth.getCurrentUser().getUid();

                //cancello le righe

                arrayListEdit.clear();     //cancello l'array temporaneo per stampare a video
                arrayAdapter.clear();
                listaStudenti.clear();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference utentiRef = rootRef.child("utenti").child(idUtente).child("studenti");

                ValueEventListener eventListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userID = mAuth.getCurrentUser().getUid();

                        String nome, cognome, email, tipo2, idStudente;

                        Boolean flag = false;


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            cognome = ds.child("cognome").getValue(String.class);
                            nome = ds.child("nome").getValue(String.class);
                            tipo2 = ds.child("tipo").getValue().toString();
                            email = ds.child("email").getValue(String.class);
                            //idStudente = ds.getKey();

                            // if (type == 1) {
                            //  System.out.println(getTipo(dataSnapshot, userID));
                            if (cognome.equalsIgnoreCase(txtCognome.getText().toString()) == true || (nome.equalsIgnoreCase(txtNome.getText().toString())) == true) {
                                if (tipo2.equals(("2"))) {

                                    arrayListEdit.add(email);

                                    listaStudenti.add(email); //lista degli studenti

                                    listView.setAdapter(arrayAdapter);

                                    System.out.println(listaStudenti);

                                    flag = true;

                                }
                            }

                            if(flag=false){

                                Toast.makeText(getApplicationContext(), "Studente non trovato in database!", Toast.LENGTH_SHORT ).show();
                            }
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                utentiRef.addListenerForSingleValueEvent(eventListener);


            }
        });

        btn_imposta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard(v);
                impostaLezione();

            }
    });
    }

    @Override
    protected void onResume() {
        ora.setText(time);
        super.onResume();

    }
    private void impostaLezione(){

        final String nomeStudente = nome.getText().toString().trim();

        if (TextUtils.isEmpty(nomeStudente)) {
            Toast.makeText(getApplicationContext(), "Per favore inserisci l'email...", Toast.LENGTH_LONG).show();
            return;
        }


        //CODICE PUT LEZIONE IN DATABASE

    }


    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}