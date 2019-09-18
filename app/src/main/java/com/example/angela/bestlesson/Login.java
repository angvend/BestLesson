package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private Button loginBtn, registraBtn;
    private ProgressBar progressBar;

    private boolean taskSuccess;

    private Integer tipo = 0;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private String uid;
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
        myRef = mFirebaseDatabase.getReference();

        userInformation = new UserInformation();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
                switcha(mAuth);

            }
        });

        registraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrazione.class);
                startActivity(intent);
            }
        });


    }

    private void loginUserAccount() {
        //progressBar.setVisibility(View.VISIBLE);

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

                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Login effettuato!", Toast.LENGTH_LONG).show();
                            // progressBar.setVisibility(View.GONE);
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
        //progressBar = findViewById(R.id.progressBar);
    }

    private void switcha(final FirebaseAuth firebaseAuth){

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Accesso effettuato con " + user.getEmail(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Aaaaaaa", Toast.LENGTH_LONG).show();
                }
            }
        };

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = firebaseAuth.getCurrentUser();
                    String email = user.getEmail();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


}
