package com.example.angela.bestlesson;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Profilo extends AppCompatActivity {

    public TextView emailTV, nomeTV, professioneTV;
    public UserInformation userInformation;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;

    String nome, cognome, professione;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("utenti");


        emailTV = (TextView) findViewById(R.id.email);
        nomeTV = (TextView) findViewById(R.id.nome_profilo);
        professioneTV = (TextView) findViewById(R.id.professione);

        userInformation = new UserInformation();
        mAuth = FirebaseAuth.getInstance();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email = mAuth.getCurrentUser().getEmail();

                getNomeCognome(dataSnapshot, email);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        String email = mAuth.getCurrentUser().getEmail();
        emailTV.setText("  " + email);
    }

    private String getNomeCognome(DataSnapshot dataSnapshot, String email) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {


            if (ds.child("email").getValue().equals(email)) {

                nome = ds.child("nome").getValue().toString();
                cognome = ds.child("cognome").getValue().toString();

                nomeTV.setText("  " + nome + " " + cognome);

                userInformation.setTipo(Integer.parseInt(ds.child("tipo").getValue().toString()));
                Integer tipo = userInformation.getTipo();

                if (tipo == 1) {
                    professioneTV.setText("Insegnante");
                } else
                    professioneTV.setText("Studente");
            }
        }
        return nome;
    }
}
