package com.example.angela.bestlesson;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AggiungiContatto extends Activity {


    private FirebaseAuth mAuth;

    private EditText txtNome, txtCognome;
    private Button conferma, aggiungi;
    private Utente utente;
    private LinearLayout linearLayout;

    private ListView listView;
    private DataSnapshot ds;
    boolean flagColor = false;
    boolean presente;

    private UserInformation userInformation;
    private Integer tipo = 0;

    private TextView email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inizializeUI();

        final ArrayList<String> arrayListEdit = new ArrayList<>();
        final ArrayList<Utente> listaStudenti = new ArrayList<>();
        final ArrayList<Utente> studentiSelezionati = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEdit);

        conferma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                listView.setAdapter(null); //cancello le righe
                arrayListEdit.clear();     //cancello l'array temporaneo per stampare a video
                arrayAdapter.clear();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference utentiRef = rootRef.child("utenti");
                ValueEventListener eventListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userID = mAuth.getCurrentUser().getUid();

                        String nome, cognome, email, tipo2, idStudente;

                        Boolean flag = false;

                        listaStudenti.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            cognome = ds.child("cognome").getValue(String.class);
                            nome = ds.child("nome").getValue(String.class);
                            tipo2 = ds.child("tipo").getValue().toString();
                            email = ds.child("email").getValue(String.class);

                            // if (type == 1) {
                            //  System.out.println(getTipo(dataSnapshot, userID));
                            if (cognome.equalsIgnoreCase(txtCognome.getText().toString()) == true || (nome.equalsIgnoreCase(txtNome.getText().toString())) == true) {
                                if (tipo2.equals(("2"))) {

                                    utente = new Utente();

                                    utente.setNome(nome);
                                    utente.setCognome(cognome);
                                    utente.setEmail(email);
                                    utente.setTipo(Integer.parseInt(tipo2));

                                    arrayListEdit.add(email);

                                    listaStudenti.add(utente); //lista degli studenti

                                    listView.setAdapter(arrayAdapter);

                                    flag = true;

                                }
                            }
                        }

                        if(flag == false){

                            Toast.makeText(getApplicationContext(), "Studente non trovato in database!", Toast.LENGTH_SHORT ).show();
                        }
                            //}

//
                            //if (type == 2) {
//                                if ((cognome.equalsIgnoreCase(txtCognome.getText().toString())) == true && tipo2.equals(("2"))) {
//                                    if ((nome.equalsIgnoreCase(txtNome.getText().toString())) == true) {
//
//                                        flag = true;
//                                        System.out.println(tipo);
//                                        cognomeOK = cognome;
//                                        nomeOK = nome;
//                                        emailOK = email;
//                                    }
//                                }
//                                if ((cognome.equalsIgnoreCase(txtCognome.getText().toString())) == false) {}
//                                if ((nome.equalsIgnoreCase(txtNome.getText().toString())) == false) {}
//                            }


                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                utentiRef.addListenerForSingleValueEvent(eventListener);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flagColor == false) {
                    view.setBackgroundColor(Color.CYAN);
                    flagColor = true;
                    view.setSelected(true);
                    studentiSelezionati.add(listaStudenti.get(position));

                } else {
                    view.setSelected(false);
                    flagColor = false;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    studentiSelezionati.remove(listaStudenti.get(position));
                }
            }
        });

        aggiungi.setOnClickListener(new View.OnClickListener() {

            int j=0;

            @Override
            public void onClick(View v) {


                presente = false;


                for(int i=0; i<studentiSelezionati.size(); i++){

                    j = i;

                    String userID = mAuth.getCurrentUser().getUid();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference studentiRef = rootRef.child("utenti").child(userID).child("studenti");

                    ValueEventListener eventListener = new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                if(studentiSelezionati.get(j).getEmail().equals(ds.child("email").getValue(String.class))) {
                                    presente = true;
                                }
                            }

                            if(presente == false){
                                FirebaseDatabase.getInstance().getReference("utenti")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("studenti").push().setValue(studentiSelezionati.get(j));
                            }else{
                                Toast.makeText(getApplicationContext(), studentiSelezionati.get(j).getEmail() + " già presente in database", Toast.LENGTH_SHORT ).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    };

                    studentiRef.addListenerForSingleValueEvent(eventListener);
                }


            }
        });
    }

    public void inizializeUI(){
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_aggiungicontatto);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtCognome = (EditText) findViewById(R.id.txtCognome);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);

        listView = (ListView) findViewById(R.id.list_view);
        email = (TextView) findViewById(R.id.email);

        aggiungi = (Button) findViewById(R.id.aggiungi);

        conferma = (Button) findViewById(R.id.btn_conferma);
    }
}

