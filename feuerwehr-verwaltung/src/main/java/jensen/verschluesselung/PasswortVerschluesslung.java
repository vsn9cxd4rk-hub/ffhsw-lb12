/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package jensen.verschluesselung;

import jensen.verschluesselung.standard.Verschluesselung;
import org.apache.commons.lang3.StringUtils;

public class PasswortVerschluesslung
extends Verschluesselung {
    private static final int STRING_LAENGE = 120;
    private static PasswortVerschluesslung instance;

    private PasswortVerschluesslung() {
    }

    public static final PasswortVerschluesslung get() {
        if (instance == null) {
            instance = new PasswortVerschluesslung();
        }
        return instance;
    }

    public final String verschluesselnPasswort(String benutzername, String passwort) {
        String wert = this.verschluesselnIntern(benutzername, passwort);
        String defaultString = this.defaultString(120 - wert.length());
        return String.valueOf(wert) + defaultString;
    }

    public final boolean isPasswortKorrekt(String benutzername, String passwort, String verschluesselterWert) {
        if (benutzername == null || passwort == null || verschluesselterWert == null) {
            return false;
        }
        String wert = this.verschluesselnIntern(benutzername, passwort);
        String pruefWert = StringUtils.left((String)verschluesselterWert, (int)wert.length());
        return wert.equals(pruefWert);
    }
}

