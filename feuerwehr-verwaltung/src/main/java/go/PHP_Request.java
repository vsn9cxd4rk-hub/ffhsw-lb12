/*
 * Decompiled with CFR 0.152.
 */
package go;

public class PHP_Request {
    private int id;
    private String typ;
    private String adresse;
    private String parameter;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getParameter() {
        return this.parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getTyp() {
        return this.typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}

