/*
 * Decompiled with CFR 0.152.
 */
package go;

public class M\u00e4ngelmeldung_kommentar {
    private int mangelID;
    private int kommentarID;
    private String datum;
    private String zeit;
    private String kommentar;
    private String user;
    private int mandantID;

    public int getMangelID() {
        return this.mangelID;
    }

    public void setMangelID(int mangelID) {
        this.mangelID = mangelID;
    }

    public int getKommentarID() {
        return this.kommentarID;
    }

    public void setKommentarID(int kommentarID) {
        this.kommentarID = kommentarID;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getKommentar() {
        return this.kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getZeit() {
        return this.zeit;
    }

    public void setZeit(String zeit) {
        this.zeit = zeit;
    }

    public int getMandantID() {
        return this.mandantID;
    }

    public void setMandantID(int mandantID) {
        this.mandantID = mandantID;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

