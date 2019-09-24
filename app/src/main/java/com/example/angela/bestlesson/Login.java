package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText emailTV, passwordTV;
    private Button loginBtn, registraBtn, passwordDimenticataBtn;
    private ProgressBar progressBar;

    private Integer tipo = 0;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;

    private UserInformation userInformation;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("utenti");

        userInformation = new UserInformation();

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        registraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrazione.class);
                startActivity(intent);
            }
        });

        passwordDimenticataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecuperaPassword.class);
                startActivity(intent);
            }
        });


    }

    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);

        final String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Per favore inserisci l'email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Per favore inserisci la password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Login effettuato!", Toast.LENGTH_LONG).show();
                            // progressBar.setVisibility(View.GONE);

                            switcha();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login fallito! Per favore riprova", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }

                });

    }

    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);

        loginBtn = findViewById(R.id.btn_login);
        registraBtn = findViewById(R.id.btn_registra);
        passwordDimenticataBtn = findViewById(R.id.btn_password_dimenticata);
        //progressBar = findViewById(R.id.progressBar);
    }

    private void switcha(){

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userID = mAuth.getCurrentUser().getUid();

                    Integer studente = 2;
                    Integer insegnante = 1;

                    Integer type = getTipo(dataSnapshot,userID);

                    if(type == insegnante){

                        Intent intent = new Intent(Login.this, BasicActivity.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    if(type == studente){

                        Intent intent = new Intent(Login.this, BasicActivity.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    private Integer getTipo(DataSnapshot dataSnapshot, String userId) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){

            String type = ds.child("tipo").getValue().toString();

            userInformation.setTipo(Integer.parseInt(type));
            tipo = userInformation.getTipo();

        }
        return tipo;
    }
}
