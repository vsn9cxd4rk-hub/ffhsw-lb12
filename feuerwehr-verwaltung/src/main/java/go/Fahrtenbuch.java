/*
 * Decompiled with CFR 0.152.
 */
package go;

public class Fahrtenbuch {
    private int id;
    private int fahrzeugID;
    private int veranstaltungID;
    private String datumVon;
    private String zeitVon;
    private String datumBis;
    private String zeitBis;
    private int kmBeginn;
    private int kmEnde;
    private int distance;
    private String tanken;
    private String pumpenbetrieb;
    private String sonstiges;
    private int fahrer;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFahrzeugID() {
        return this.fahrzeugID;
    }

    public void setFahrzeugID(int fahrzeugID) {
        this.fahrzeugID = fahrzeugID;
    }

    public int getVeranstaltungID() {
        return this.veranstaltungID;
    }

    public void setVeranstaltungID(int veranstaltungID) {
        this.veranstaltungID = veranstaltungID;
    }

    public String getDatumVon() {
        return this.datumVon;
    }

    public void setDatumVon(String datumVon) {
        this.datumVon = datumVon;
    }

    public String getZeitVon() {
        return this.zeitVon;
    }

    public void setZeitVon(String zeitVon) {
        this.zeitVon = zeitVon;
    }

    public String getDatumBis() {
        return this.datumBis;
    }

    public void setDatumBis(String datumBis) {
        this.datumBis = datumBis;
    }

    public String getZeitBis() {
        return this.zeitBis;
    }

    public void setZeitBis(String zeitBis) {
        this.zeitBis = zeitBis;
    }

    public int getKmBeginn() {
        return this.kmBeginn;
    }

    public void setKmBeginn(int kmBeginn) {
        this.kmBeginn = kmBeginn;
    }

    public int getKmEnde() {
        return this.kmEnde;
    }

    public void setKmEnde(int kmEnde) {
        this.kmEnde = kmEnde;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTanken() {
        return this.tanken;
    }

    public void setTanken(String tanken) {
        this.tanken = tanken;
    }

    public String getPumpenbetrieb() {
        return this.pumpenbetrieb;
    }

    public void setPumpenbetrieb(String pumpenbetrieb) {
        this.pumpenbetrieb = pumpenbetrieb;
    }

    public String getSonstiges() {
        return this.sonstiges;
    }

    public void setSonstiges(String sonstiges) {
        this.sonstiges = sonstiges;
    }

    public int getFahrer() {
        return this.fahrer;
    }

    public void setFahrer(int fahrer) {
        this.fahrer = fahrer;
    }
}

