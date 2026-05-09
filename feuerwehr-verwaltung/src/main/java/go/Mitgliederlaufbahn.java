/*
 * Decompiled with CFR 0.152.
 */
package go;

public class Mitgliederlaufbahn {
    private int id;
    private int mitgliederID;
    private String datumVon;
    private String datum;
    private String art;
    private int alterDienstgrad;
    private int neuerDienstgrad;
    private int lehrgang;
    private int ue;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getAlterDienstgrad() {
        return this.alterDienstgrad;
    }

    public void setAlterDienstgrad(int alterDienstgrad) {
        this.alterDienstgrad = alterDienstgrad;
    }

    public int getNeuerDienstgrad() {
        return this.neuerDienstgrad;
    }

    public void setNeuerDienstgrad(int neuerDienstgrad) {
        this.neuerDienstgrad = neuerDienstgrad;
    }

    public String getArt() {
        return this.art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public int getLehrgang() {
        return this.lehrgang;
    }

    public void setLehrgang(int lehrgang) {
        this.lehrgang = lehrgang;
    }

    public int getMitgliederID() {
        return this.mitgliederID;
    }

    public void setMitgliederID(int mitgliederID) {
        this.mitgliederID = mitgliederID;
    }

    public int getUe() {
        return this.ue;
    }

    public void setUe(int ue) {
        this.ue = ue;
    }

    public String getDatumVon() {
        return this.datumVon;
    }

    public void setDatumVon(String datumVon) {
        this.datumVon = datumVon;
    }
}

