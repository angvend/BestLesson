package com.example.angela.bestlesson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VisualizzaRubrica extends Activity {

    private FirebaseAuth mAuth;
    private ListView listView;
    private LinearLayout linearLayout;
    private FloatingActionButton button_add;

    private Intent intent;

    private String typeUser;

    private Utente utente;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizzarubrica);

        mAuth = FirebaseAuth.getInstance();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        listView = (ListView) findViewById(R.id.lista);


        String userID = mAuth.getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference utentiRef = rootRef.child("utenti");


        intent = getIntent();
        typeUser = intent.getStringExtra("tipoUtente1");

        if(typeUser.equals("1")){

            utentiRef = rootRef.child("utenti").child(userID).child("studenti");

            button_add = (FloatingActionButton) findViewById(R.id.aggiungiUtente);
            button_add.setVisibility(View.VISIBLE);
        }

        if(typeUser.equals("2")){

            utentiRef = rootRef.child("utenti");

            button_add = (FloatingActionButton) findViewById(R.id.aggiungiUtente);
            button_add.setVisibility(View.INVISIBLE);
        }


        final ArrayList<String> listaEmail = new ArrayList<>();
        final ArrayList<String> arrayListEdit = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEdit);

        ValueEventListener eventListener = new ValueEventListener() {

            String nome, cognome, email, tipo2, idStudente;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listView.setAdapter(null); //cancello le righe
                arrayListEdit.clear();     //cancello l'array temporaneo per stampare a video
                arrayAdapter.clear();
                listaEmail.clear();

                if(dataSnapshot.getChildren() == null){

                    Toast.makeText(getApplicationContext(), "Studente non trovato in database!", Toast.LENGTH_SHORT ).show();
                }else{

                    if(typeUser.equals("1")){
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            cognome = ds.child("cognome").getValue(String.class);
                            nome = ds.child("nome").getValue(String.class);
                            email = ds.child("email").getValue(String.class);


                            arrayListEdit.add(nome + " " + cognome);
                            listaEmail.add(email);

                            listView.setAdapter(arrayAdapter);

                        }
                    }

                    if(typeUser.equals("2")){
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if(ds.child("tipo").getValue().toString().equals("1")){

                                cognome = ds.child("cognome").getValue(String.class);
                                nome = ds.child("nome").getValue(String.class);
                                email = ds.child("email").getValue(String.class);


                                arrayListEdit.add(nome + " " + cognome);
                                listaEmail.add(email);

                                listView.setAdapter(arrayAdapter);
                            }

                        }
                    }

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), Contatta.class);
                intent.putExtra("emailPassata", listaEmail.get(position));
                Toast.makeText(getApplicationContext(), listaEmail.get(position), Toast.LENGTH_SHORT ).show();
                startActivity(intent);
        }

        });


        utentiRef.addListenerForSingleValueEvent(eventListener);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AggiungiContatto.class);
                startActivity(intent);
            }
        });
    }

}
