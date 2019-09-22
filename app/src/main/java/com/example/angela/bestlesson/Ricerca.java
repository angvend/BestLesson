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

public class Ricerca extends Activity {


    FirebaseAuth mAuth;

    EditText txtNome, txtCognome;
    Button conferma, aggiungi;
    Utente utente;
    LinearLayout linearLayout;

    ListView listView;
    DataSnapshot ds;
    boolean flagColor = false;

    private UserInformation userInformation;
    private Integer tipo = 0;


    TextView email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_ricerca);


        txtNome = (EditText) findViewById(R.id.txtNome);
        txtCognome = (EditText) findViewById(R.id.txtCognome);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        listView = (ListView) findViewById(R.id.list_view);
        email = (TextView) findViewById(R.id.email);

        aggiungi = (Button) findViewById(R.id.aggiungi);
        final ArrayList<String> arrayListEdit = new ArrayList<>();
        final ArrayList<String> listaStudenti = new ArrayList<>();

        final ArrayList<String> studenti = new ArrayList<>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEdit);


        conferma = (Button) findViewById(R.id.btn_conferma);

        conferma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference utentiRef = rootRef.child("utenti");
                ValueEventListener eventListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userID = mAuth.getCurrentUser().getUid();



                        //Integer type = getTipo(dataSnapshot,userID);


                        String cognome, nome, email, tipo2;
                        Boolean flag = false;

                        String cognomeOK = null, nomeOK = null, emailOK = null;


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            cognome = ds.child("cognome").getValue(String.class);
                            nome = ds.child("nome").getValue(String.class);
                            tipo2 = ds.child("tipo").getValue().toString();
                            email = ds.child("email").getValue(String.class);

                           // if (type == 1) {
                                //  System.out.println(getTipo(dataSnapshot, userID));
                                if ((cognome.equalsIgnoreCase(txtCognome.getText().toString())) == true && tipo2.equals(("2"))) {
                                    if ((nome.equalsIgnoreCase(txtNome.getText().toString())) == true) {

                                        flag = true;
                                        System.out.println(tipo2);
                                        cognomeOK = cognome;
                                        nomeOK = nome;
                                        emailOK = email;
                                    }
                                }
                                if ((cognome.equalsIgnoreCase(txtCognome.getText().toString())) == false) {
                                }
                                if ((nome.equalsIgnoreCase(txtNome.getText().toString())) == false) {
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
                        if (flag == true) {

                            listView.setAdapter(null); //cancello le righe
                            arrayListEdit.clear();     //cancello l'array temporaneo per stampare a video
                            arrayListEdit.add(emailOK);

                            listaStudenti.add(emailOK); //lista degli studenti


                            listView.setAdapter(arrayAdapter);

                            System.out.println(listaStudenti);

                        } else {

                            Toast.makeText(getApplicationContext(), "Studente non presente", Toast.LENGTH_LONG).show();
                            listView.setAdapter(null);
                        }
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

                    aggiungi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //AGGIUNGERE QUI ARRAY STUDENTI/DOCENTI
                        }
                    });

                } else {
                    view.setSelected(false);
                    flagColor = false;
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }


    private Integer getTipo(DataSnapshot dataSnapshot, String userId) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            userInformation.setTipo(Integer.parseInt(ds.child(userId).child("tipo").getValue().toString()));
            tipo = userInformation.getTipo();
            Log.d("PUPU", tipo.toString());
        }
        return tipo;
}
}

