package com.example.angela.bestlesson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
    private TextView data;
    private TextView lezione;
    private TextView textViewStudenti;

    EditText txtNome, txtCognome;

    private TimePicker inizio;
    private TimePicker fine;

    private String dataPassata;
    private String date;

    private String email;

    Button conferma;


    ListView listView;

    private Intent intent;

    private Lezione lezioneDaSalvare;


    boolean flagColor = false;

    Button btn_imposta;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    String utenteSelezionato;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lezione);

        data = (TextView) findViewById(R.id.data);

        lezione = (TextView) findViewById(R.id.lezione);

        lezioneDaSalvare = new Lezione();

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

        dataPassata = intent.getStringExtra("dataPassata");

        date = dataPassata;

        data.setText(date);

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

                                    arrayListEdit.add(nome + " " + cognome + " (" + email + ")");

                                    listaStudenti.add(email); //lista degli studenti

                                    listView.setAdapter(arrayAdapter);

                                    System.out.println(listaStudenti);

                                    flag = true;

                                }
                            }
                        }

                        if(flag == false){

                            Toast.makeText(getApplicationContext(), "Studente non trovato in database!", Toast.LENGTH_SHORT ).show();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flagColor == false) {
                    view.setBackgroundColor(Color.CYAN);
                    flagColor = true;
                    view.setSelected(true);
                    utenteSelezionato = listaStudenti.get(position);

                } else {
                    view.setSelected(false);
                    flagColor = false;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    utenteSelezionato = "";
                }
            }
        });

    }

    @Override
    protected void onResume() {
        data.setText(date);
        super.onResume();

    }
    private void impostaLezione() {

        int oraInizio = inizio.getCurrentHour();
        int minutoInizio = inizio.getCurrentMinute();
        int oraFine = fine.getCurrentHour();
        int minutoFine = fine.getCurrentMinute();

        int conteggioMinutiInizio = 0;
        int conteggioMinutiFine = 0;

        conteggioMinutiInizio = (oraInizio * 60) + minutoInizio;
        conteggioMinutiFine = (oraFine * 60) + minutoFine;

        String orarioInizio = correggiOraInizio();
        String orarioFine = correggiOraFine();


        if (conteggioMinutiInizio < conteggioMinutiFine) {

            if(utenteSelezionato == null){
                Toast.makeText(getApplicationContext(), "Inserisci lo studente!", Toast.LENGTH_SHORT ).show();
            }else{
            lezioneDaSalvare.setStudente(utenteSelezionato);

            lezioneDaSalvare.setInsegnante(mAuth.getCurrentUser().getEmail());
            lezioneDaSalvare.setOraInizio(orarioInizio);
            lezioneDaSalvare.setOraFine(orarioFine);
            lezioneDaSalvare.setData(date);
            lezioneDaSalvare.setOreDiLezione(oraFine-oraInizio);

            FirebaseDatabase.getInstance().getReference("lezioni").push().setValue(lezioneDaSalvare);

            Toast.makeText(getApplicationContext(), "Lezione assegnata con successo!", Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent(getApplicationContext(), BasicActivity.class);
            startActivity(intent);

        }
        }else{
            Toast.makeText(getApplicationContext(), "Attenzione, inserisci un orario valido!", Toast.LENGTH_SHORT ).show();
        }
    }

    private String correggiOraInizio(){

        String oraInizio1;
        String minutiInizio1;

        if(inizio.getCurrentHour() < 10) {
            oraInizio1 = "0" + inizio.getCurrentHour();
        }else{
            oraInizio1 = inizio.getCurrentHour().toString();
        }

        if(inizio.getCurrentMinute() < 10) {
            minutiInizio1 = "0" + inizio.getCurrentMinute();
        }else{
            minutiInizio1 = inizio.getCurrentMinute().toString();
        }


        String orarioInizio = oraInizio1 + ":" + minutiInizio1;

        return orarioInizio;

    }

    private String correggiOraFine(){

        String minutiFine1;
        String oraFine1;

        if(fine.getCurrentHour() < 10) {
            oraFine1 = "0" + fine.getCurrentHour();
        }else{
            oraFine1 = fine.getCurrentHour().toString();
        }

        if(fine.getCurrentMinute() < 10) {
            minutiFine1 = "0" + fine.getCurrentMinute();
        }else{
            minutiFine1 = fine.getCurrentMinute().toString();
        }

        String orarioFine = oraFine1 + ":" + minutiFine1;

        return orarioFine;

    }


    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}