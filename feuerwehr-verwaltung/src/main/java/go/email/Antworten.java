/*
 * Decompiled with CFR 0.152.
 */
package go.email;

public class Antworten {
    private int id;
    private String sender;
    private String betreff;
    private Object nachricht;
    private String date;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBetreff() {
        return this.betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public Object getNachricht() {
        return this.nachricht;
    }

    public void setNachricht(Object nachricht) {
        this.nachricht = nachricht;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

