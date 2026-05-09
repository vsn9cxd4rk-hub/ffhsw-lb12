/*
 * Decompiled with CFR 0.152.
 */
package go;

public class Fahrzeug {
    private int id;
    private String name;
    private int beschreibung;
    private String kennzeichen;
    private String funkrufname;
    private int sitzplaetze;
    private int minBesatzung;
    private int maxBesatzung;
    private String fuehrerschein;
    private int ausserDienst;
    private int anhaenger;
    private int trupp;
    private int sortierung;
    private int mandantID;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSitzplaetze() {
        return this.sitzplaetze;
    }

    public void setSitzplaetze(int sitzplaetze) {
        this.sitzplaetze = sitzplaetze;
    }

    public int getMinBesatzung() {
        return this.minBesatzung;
    }

    public void setMinBesatzung(int minBesatzung) {
        this.minBesatzung = minBesatzung;
    }

    public int getMaxBesatzung() {
        return this.maxBesatzung;
    }

    public void setMaxBesatzung(int maxBesatzung) {
        this.maxBesatzung = maxBesatzung;
    }

    public String getFuehrerschein() {
        return this.fuehrerschein;
    }

    public void setFuehrerschein(String fuehrerschein) {
        this.fuehrerschein = fuehrerschein;
    }

    public int getAusserDienst() {
        return this.ausserDienst;
    }

    public void setAusserDienst(int ausserDienst) {
        this.ausserDienst = ausserDienst;
    }

    public String getKennzeichen() {
        return this.kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getFunkrufname() {
        return this.funkrufname;
    }

    public void setFunkrufname(String funkrufname) {
        this.funkrufname = funkrufname;
    }

    public int getAnhaenger() {
        return this.anhaenger;
    }

    public void setAnhaenger(int anhaenger) {
        this.anhaenger = anhaenger;
    }

    public int getSortierung() {
        return this.sortierung;
    }

    public void setSortierung(int sortierung) {
        this.sortierung = sortierung;
    }

    public int getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(int beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getMandantID() {
        return this.mandantID;
    }

    public void setMandantID(int mandantID) {
        this.mandantID = mandantID;
    }

    public int getTrupp() {
        return this.trupp;
    }

    public void setTrupp(int trupp) {
        this.trupp = trupp;
    }
}

