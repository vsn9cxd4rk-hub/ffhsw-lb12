/*
 * Decompiled with CFR 0.152.
 */
package go.email;

public class Gesendet {
    private int id;
    private String an;
    private String cc;
    private String bcc;
    private String betreff;
    private String nachricht;
    private int anhang;
    private String date;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAn() {
        return this.an;
    }

    public void setAn(String empfaenger) {
        this.an = empfaenger;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return this.bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getBetreff() {
        return this.betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getNachricht() {
        return this.nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public int getAnhang() {
        return this.anhang;
    }

    public void setAnhang(int anhang) {
        this.anhang = anhang;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

