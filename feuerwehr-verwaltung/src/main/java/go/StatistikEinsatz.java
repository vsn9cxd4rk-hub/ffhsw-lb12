/*
 * Decompiled with CFR 0.152.
 */
package go;

public class StatistikEinsatz {
    private int id;
    private int veranstaltungID;
    private int einsatzID;
    private int jahr;
    private int stichwort;
    private int kategorie;
    private int ausrueckezeit;
    private int dauer;
    private int dauerAlarmfahrt;
    private int mannstunden;
    private int wochentag;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEinsatzID() {
        return this.einsatzID;
    }

    public void setEinsatzID(int einsatzID) {
        this.einsatzID = einsatzID;
    }

    public int getJahr() {
        return this.jahr;
    }

    public void setJahr(int jahr) {
        this.jahr = jahr;
    }

    public int getStichwort() {
        return this.stichwort;
    }

    public void setStichwort(int stichwort) {
        this.stichwort = stichwort;
    }

    public int getKategorie() {
        return this.kategorie;
    }

    public void setKategorie(int kategorie) {
        this.kategorie = kategorie;
    }

    public int getAusrueckezeit() {
        return this.ausrueckezeit;
    }

    public void setAusrueckezeit(int ausrueckezeit) {
        this.ausrueckezeit = ausrueckezeit;
    }

    public int getDauer() {
        return this.dauer;
    }

    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    public int getMannstunden() {
        return this.mannstunden;
    }

    public void setMannstunden(int mannstunden) {
        this.mannstunden = mannstunden;
    }

    public int getWochentag() {
        return this.wochentag;
    }

    public void setWochentag(int wochentag) {
        this.wochentag = wochentag;
    }

    public int getDauerAlarmfahrt() {
        return this.dauerAlarmfahrt;
    }

    public void setDauerAlarmfahrt(int dauerAlarmfahrt) {
        this.dauerAlarmfahrt = dauerAlarmfahrt;
    }

    public int getVeranstaltungID() {
        return this.veranstaltungID;
    }

    public void setVeranstaltungID(int veranstaltungID) {
        this.veranstaltungID = veranstaltungID;
    }
}

