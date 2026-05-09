/*
 * Decompiled with CFR 0.152.
 */
package go.email;

public class Ausgang {
    private String an;
    private String cc;
    private String bcc;
    private String betreff;
    private String nachricht;
    private String anhang;
    private String date;

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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnhang() {
        return this.anhang;
    }

    public void setAnhang(String anhang) {
        this.anhang = anhang;
    }
}

