package com.example.angela.bestlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

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


    private Utente utente;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizzarubrica);

        mAuth = FirebaseAuth.getInstance();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        listView = (ListView) findViewById(R.id.lista);


        final ArrayList<String> arrayListEdit = new ArrayList<>();
        final ArrayList<Utente> listaStudenti = new ArrayList<>();
        final ArrayList<Utente> studentiSelezionati = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEdit);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference utentiRef = rootRef.child("utenti");
        ValueEventListener eventListener = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userID = mAuth.getCurrentUser().getUid();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.child(userID).child("studenti").child("LpQ7HpmzNsQWbCLHaOv").getValue().toString();
                    Log.d("vedi", ds.child(userID).child("studenti").child("LpQ7HpmzNsQWbCLHaOv").getValue().toString());
                    listView.setAdapter(arrayAdapter);
                    arrayListEdit.add(ds.child(userID).child("studenti").child("LpQ7HpmzNsQWbCLHaOv").getValue().toString());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        button_add = (FloatingActionButton) findViewById(R.id.aggiungiUtente);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AggiungiContatto.class);
                startActivity(intent);
            }
        });



    }

}
