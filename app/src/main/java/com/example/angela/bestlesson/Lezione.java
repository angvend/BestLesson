package com.example.angela.bestlesson;

public class Lezione {
    String studente;
    String insegnante;
    String data;
    String oraInizio;
    String oraFine;
    int oreDiLezione;

    public int getOreDiLezione() {
        return oreDiLezione;
    }

    public void setOreDiLezione(int oreDiLezione) {
        this.oreDiLezione = oreDiLezione;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStudente() {
        return studente;
    }

    public void setStudente(String studente) {
        this.studente = studente;
    }

    public String getInsegnante() {
        return insegnante;
    }

    public void setInsegnante(String insegnante) {
        this.insegnante = insegnante;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }

    public void setOraFine(String oraFine) {
        this.oraFine = oraFine;
    }
}
