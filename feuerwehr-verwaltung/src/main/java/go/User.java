/*
 * Decompiled with CFR 0.152.
 */
package go;

public class User {
    private String user;
    private String passwort;
    private String usergruppe;
    private int admin;
    private int deaktiv;
    private int loeschkenner;

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getUsergruppe() {
        return this.usergruppe;
    }

    public void setUsergruppe(String usergruppe) {
        this.usergruppe = usergruppe;
    }

    public int getAdmin() {
        return this.admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getDeaktiv() {
        return this.deaktiv;
    }

    public void setDeaktiv(int deaktiv) {
        this.deaktiv = deaktiv;
    }

    public int getLoeschkenner() {
        return this.loeschkenner;
    }

    public void setLoeschkenner(int loeschkenner) {
        this.loeschkenner = loeschkenner;
    }
}

