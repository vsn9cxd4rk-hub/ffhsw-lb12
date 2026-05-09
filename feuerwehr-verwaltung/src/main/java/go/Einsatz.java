/*
 * Decompiled with CFR 0.152.
 */
package go;

public class Einsatz {
    private int id;
    private int einsatznummer;
    private String einsatznummerOffiziell;
    private int veranstaltungID;
    private String datum;
    private String zeitAlarm;
    private String zeitAusgerueckt;
    private String zeitEingetroffen;
    private String zeitEingerueckt;
    private String ort;
    private String stadtteil;
    private int stichwort;
    private String fahrzeug;
    private String fahrzeugID;
    private String beschreibung;
    private int staerkeGF;
    private int staerkeFM;
    private int einsatzleiter;
    private int staerkeZF;
    private String einsatzleiterBF;

    public int getEinsatznummer() {
        return this.einsatznummer;
    }

    public void setEinsatznummer(int einsatznummer) {
        this.einsatznummer = einsatznummer;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getZeitAlarm() {
        return this.zeitAlarm;
    }

    public void setZeitAlarm(String zeitAlarm) {
        this.zeitAlarm = zeitAlarm;
    }

    public String getZeitAusgerueckt() {
        return this.zeitAusgerueckt;
    }

    public void setZeitAusgerueckt(String zeitAusgerueckt) {
        this.zeitAusgerueckt = zeitAusgerueckt;
    }

    public String getZeitEingerueckt() {
        return this.zeitEingerueckt;
    }

    public void setZeitEingerueckt(String zeitEingerueckt) {
        this.zeitEingerueckt = zeitEingerueckt;
    }

    public String getOrt() {
        return this.ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getStichwort() {
        return this.stichwort;
    }

    public void setStichwort(int stichwort) {
        this.stichwort = stichwort;
    }

    public String getFahrzeug() {
        return this.fahrzeug;
    }

    public void setFahrzeug(String fahrzeug) {
        this.fahrzeug = fahrzeug;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getStaerkeGF() {
        return this.staerkeGF;
    }

    public void setStaerkeGF(int staerkeGF) {
        this.staerkeGF = staerkeGF;
    }

    public int getStaerkeFM() {
        return this.staerkeFM;
    }

    public void setStaerkeFM(int staerkeFM) {
        this.staerkeFM = staerkeFM;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVeranstaltungID() {
        return this.veranstaltungID;
    }

    public void setVeranstaltungID(int veranstaltungID) {
        this.veranstaltungID = veranstaltungID;
    }

    public String getEinsatznummerOffiziell() {
        return this.einsatznummerOffiziell;
    }

    public void setEinsatznummerOffiziell(String einsatznummerOffiziell) {
        this.einsatznummerOffiziell = einsatznummerOffiziell;
    }

    public String getZeitEingetroffen() {
        return this.zeitEingetroffen;
    }

    public void setZeitEingetroffen(String zeitEingetroffen) {
        this.zeitEingetroffen = zeitEingetroffen;
    }

    public String getStadtteil() {
        return this.stadtteil;
    }

    public void setStadtteil(String stadtteil) {
        this.stadtteil = stadtteil;
    }

    public int getEinsatzleiter() {
        return this.einsatzleiter;
    }

    public void setEinsatzleiter(int einsatzleiter) {
        this.einsatzleiter = einsatzleiter;
    }

    public int getStaerkeZF() {
        return this.staerkeZF;
    }

    public void setStaerkeZF(int staerkeZF) {
        this.staerkeZF = staerkeZF;
    }

    public String getEinsatzleiterBF() {
        return this.einsatzleiterBF;
    }

    public void setEinsatzleiterBF(String einsatzleiterBF) {
        this.einsatzleiterBF = einsatzleiterBF;
    }

    public String getFahrzeugID() {
        return this.fahrzeugID;
    }

    public void setFahrzeugID(String fahrzeugID) {
        this.fahrzeugID = fahrzeugID;
    }
}

