package com.example.angela.bestlesson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Registrazione extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtEmail, txtPassword, txtConfirmPassword, txtNome, txtCognome;

    Utente utente;

    Integer type = 2;


    String[] tipo = {"Studente","Insegnante"};

    Button btn_registra;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);


        txtNome = (EditText) findViewById(R.id.txtNome);
        txtCognome = (EditText) findViewById(R.id.txtCognome);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        btn_registra = (Button) findViewById(R.id.btn_registra);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Spinner spin = (Spinner) findViewById(R.id.spinner1);

        firebaseAuth = FirebaseAuth.getInstance();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


        btn_registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txtEmail.getText().toString().trim();
                final String nome = txtNome.getText().toString().trim();
                final String cognome = txtCognome.getText().toString().trim();

                utente = new Utente();

                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();


                if (TextUtils.isEmpty(nome)){
                    Toast.makeText(Registrazione.this, "Per favore, inserisci il nome", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    utente.setNome(nome);
                }

                if (TextUtils.isEmpty(cognome)){
                    Toast.makeText(Registrazione.this, "Per favore, inserisci il cognome", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    utente.setCognome(cognome);
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Registrazione.this, "Per favore, inserisci l'email", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    utente.setEmail(email);
                }

                if (adapter.isEmpty() != false){
                    Toast.makeText(Registrazione.this, "Per favore, inserisci il tipo di utente", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    utente.setTipo(type);
                }

                if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(Registrazione.this, "Per favore, conferma la password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6){
                    Toast.makeText(Registrazione.this, "Password troppo corta", Toast.LENGTH_SHORT).show();

                }else{

                }

                progressBar.setVisibility(View.VISIBLE);

                if (password.equals(confirmPassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Registrazione.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference listaUtenti = database.getInstance().getReference().child("utenti");

                                        if(listaUtenti.equals("")){
                                            DatabaseReference databaseVuoto = database.getReference();
                                            databaseVuoto.setValue("utenti");
                                        }else {

                                        }

                                        FirebaseDatabase.getInstance().getReference("utenti")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(utente);

                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                        Toast.makeText(Registrazione.this, "Registrazione completata", Toast.LENGTH_SHORT);

                                    } else {

                                        Toast.makeText(Registrazione.this, "Email già presente in database!", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }

            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

        if(tipo[position].equals("Studente")){

            type = 2;

        }

        if(tipo[position].equals("Insegnante")){

            type = 1;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}