/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package jensen.verschluesselung;

import jensen.verschluesselung.standard.Verschluesselung;
import org.apache.commons.lang3.StringUtils;

public class StringVerschluesslung
extends Verschluesselung {
    private static final String TRENNSTRING_LAENGE = "\u00ed";
    private static StringVerschluesslung instance;

    private StringVerschluesslung() {
    }

    public static final StringVerschluesslung get() {
        if (instance == null) {
            instance = new StringVerschluesslung();
        }
        return instance;
    }

    public final String verschluesseln(String objekt, String oeffentlicherSchluessel, int maximaleFeldLaenge) {
        String laenge = this.verschluesselnIntern(Integer.toString(objekt.length()), oeffentlicherSchluessel);
        String wert = this.verschluesselnIntern(objekt, oeffentlicherSchluessel);
        String defaultString = this.defaultString(maximaleFeldLaenge - wert.length());
        return String.valueOf(laenge) + TRENNSTRING_LAENGE + wert + defaultString;
    }

    public final String entschluesseln(String objekt, String oeffentlicherSchluessel) {
        int pos = StringUtils.indexOf((String)objekt, (String)TRENNSTRING_LAENGE);
        String objektLaenge = StringUtils.left((String)objekt, (int)pos);
        int laenge = Integer.parseInt(this.entschluesselnIntern(objektLaenge, oeffentlicherSchluessel)) * 2;
        return this.entschluesselnIntern(StringUtils.mid((String)objekt, (int)(pos + 1), (int)laenge), oeffentlicherSchluessel);
    }
}

