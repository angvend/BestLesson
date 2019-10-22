package com.example.angela.bestlesson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.angela.bestlesson.Utility.BaseActivity;
import com.example.angela.bestlesson.Utility.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicActivity extends BaseActivity {

    public static Activity fa;
    public int anno, mese;
    public static final String MyPREFERENCES = "MyPrefs" ;


    List<WeekViewEvent> eventi;
    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fa = this;
        getEventi();

        SystemClock.sleep(750);

        super.onCreate(savedInstanceState);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(final int newYear, final int newMonth) {

        return events;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void getEventi(){

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        // Populate the week view with some events.



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lezioniRef = rootRef.child("lezioni");

        final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String tipoUtente;
        tipoUtente = sharedpreferences.getString("tipo","");


        final ArrayList<Lezione> listaLezioni = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventi = new ArrayList<WeekViewEvent>();

                String email;
                listaLezioni.clear();

                if(tipoUtente.equals("1")){

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        email = ds.child("insegnante").getValue(String.class);

                        Lezione lezione= new Lezione();

                        if (email.equals(mAuth.getCurrentUser().getEmail())) {
                            lezione.setInsegnante(ds.child("insegnante").getValue().toString());
                            lezione.setStudente(ds.child("studente").getValue().toString());
                            lezione.setData(ds.child("data").getValue().toString());
                            lezione.setOraInizio(ds.child("oraInizio").getValue().toString());
                            lezione.setOraFine(ds.child("oraFine").getValue().toString());
                            lezione.setOreDiLezione(Integer.parseInt(ds.child("oreDiLezione").getValue().toString()));
                            listaLezioni.add(lezione);
                        }

                    }

                    for(int i = 0; i < listaLezioni.size(); i++){

                        String oraInizio = listaLezioni.get(i).getOraInizio();
                        String oraInizio1 = oraInizio.substring(0, Math.min(oraInizio.length(), 2));

                        String minutoInizio = listaLezioni.get(i).getOraInizio();
                        String minutoInizio1 = minutoInizio.substring(minutoInizio.lastIndexOf(":") + 1);

                        String oraFine = listaLezioni.get(i).getOraFine();
                        String oraFine1 = oraFine.substring(0, Math.min(oraFine.length(), 2));

                        String minutoFine = listaLezioni.get(i).getOraFine();
                        String minutoFine1 = minutoFine.substring(minutoFine.lastIndexOf(":") + 1);

                        String mese = listaLezioni.get(i).getData();
                        String mese1 = mese.substring(0, Math.min(mese.length(), 5));
                        String mese2 = mese1.substring(mese1.lastIndexOf("/")+1);

                        String anno = listaLezioni.get(i).getData();
                        String anno1 = anno.substring(anno.lastIndexOf("/")+1);

                        String giorno = listaLezioni.get(i).getData();
                        String giorno1 = giorno.substring(0, Math.min(giorno.length(), 2));


                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oraInizio1));
                        startTime.set(Calendar.MINUTE, Integer.parseInt(minutoInizio1));
                        startTime.set(Calendar.DATE, Integer.parseInt(giorno1));
                        startTime.set(Calendar.MONTH, Integer.parseInt(mese2));
                        startTime.set(Calendar.YEAR, Integer.parseInt(anno1));
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.add(Calendar.HOUR, listaLezioni.get(i).getOreDiLezione());
                        endTime.set(Calendar.MONTH, Integer.parseInt(mese2));
                        WeekViewEvent event = new WeekViewEvent(i, getEventTitle(startTime), startTime, endTime);
                        event.setColor(getResources().getColor(R.color.event_color_05));
                        event.setName(listaLezioni.get(i).getStudente() + "\n"+ listaLezioni.get(i).getOraInizio() + "-" + listaLezioni.get(i).getOraFine() + "\n" + listaLezioni.get(i).getOreDiLezione() + "h");
                        eventi.add(event);

                    }
                }

                if(tipoUtente.equals("2")) {

                    String userEmail = sharedpreferences.getString("nome", "");

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        email = ds.child("studente").getValue(String.class);


                        Lezione lezione = new Lezione();

                        if (email.equals(userEmail)) {

                            lezione.setInsegnante(ds.child("insegnante").getValue().toString());
                            lezione.setStudente(ds.child("studente").getValue().toString());
                            lezione.setData(ds.child("data").getValue().toString());
                            lezione.setOraInizio(ds.child("oraInizio").getValue().toString());
                            lezione.setOraFine(ds.child("oraFine").getValue().toString());
                            lezione.setOreDiLezione(Integer.parseInt(ds.child("oreDiLezione").getValue().toString()));
                            listaLezioni.add(lezione);
                        }

                    }

                    for (int i = 0; i < listaLezioni.size(); i++) {

                        String oraInizio = listaLezioni.get(i).getOraInizio();
                        String oraInizio1 = oraInizio.substring(0, Math.min(oraInizio.length(), 2));

                        String minutoInizio = listaLezioni.get(i).getOraInizio();
                        String minutoInizio1 = minutoInizio.substring(minutoInizio.lastIndexOf(":") + 1);

                        String oraFine = listaLezioni.get(i).getOraFine();
                        String oraFine1 = oraFine.substring(0, Math.min(oraFine.length(), 2));

                        String minutoFine = listaLezioni.get(i).getOraFine();
                        String minutoFine1 = minutoFine.substring(minutoFine.lastIndexOf(":") + 1);

                        String mese = listaLezioni.get(i).getData();
                        String mese1 = mese.substring(0, Math.min(mese.length(), 5));
                        String mese2 = mese1.substring(mese1.lastIndexOf("/")+1);

                        String anno = listaLezioni.get(i).getData();
                        String anno1 = anno.substring(anno.lastIndexOf("/")+1);

                        String giorno = listaLezioni.get(i).getData();
                        String giorno1 = giorno.substring(0, Math.min(giorno.length(), 2));


                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oraInizio1));
                        startTime.set(Calendar.MINUTE, Integer.parseInt(minutoInizio1));
                        startTime.set(Calendar.DATE, Integer.parseInt(giorno1));
                        startTime.set(Calendar.MONTH, Integer.parseInt(mese2));
                        startTime.set(Calendar.YEAR, Integer.parseInt(anno1));
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.add(Calendar.HOUR, listaLezioni.get(i).getOreDiLezione());
                        endTime.set(Calendar.MONTH, Integer.parseInt(mese2));
                        WeekViewEvent event = new WeekViewEvent(i, getEventTitle(startTime), startTime, endTime);
                        event.setColor(getResources().getColor(R.color.event_color_05));
                        event.setName(listaLezioni.get(i).getInsegnante() + "\n"+ listaLezioni.get(i).getOraInizio() + "-" + listaLezioni.get(i).getOraFine() + "\n" + listaLezioni.get(i).getOreDiLezione() + "h");
                        eventi.add(event);

                    }

                }

                for(int i=0; i < eventi.size(); i++){
                    events.add(eventi.get(i));
                }


            }




            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        lezioniRef.addListenerForSingleValueEvent(eventListener);


    }

}

