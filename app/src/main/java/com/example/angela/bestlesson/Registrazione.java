package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Registrazione extends AppCompatActivity {

    EditText txtEmail, txtPassword, txtConfirmPassword;
    Button btn_registra;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);


        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        btn_registra = (Button) findViewById(R.id.btn_registra);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Registrazione.this, "Per favore, inserisci l'email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(Registrazione.this, "Per favore, conferma la password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<6){
                    Toast.makeText(Registrazione.this, "Password troppo corta", Toast.LENGTH_SHORT).show();

                }

                progressBar.setVisibility(View.VISIBLE);

                if (password.equals(confirmPassword)){
                    firebaseAuth .createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Registrazione.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(getApplicationContext(),Home.class));
                                        Toast.makeText(Registrazione.this, "Registrazione completata", Toast.LENGTH_SHORT);

                                    } else {

                                        Toast.makeText(Registrazione.this, "Autenticazione fallita", Toast.LENGTH_SHORT);
                                    }

                                    // ...
                                }
                            });
                }

            }

        });
    }
}