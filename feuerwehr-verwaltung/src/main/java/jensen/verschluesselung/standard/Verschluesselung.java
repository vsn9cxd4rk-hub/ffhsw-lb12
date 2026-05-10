/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package jensen.verschluesselung.standard;

import java.util.ArrayList;
import java.util.List;
import jensen.verschluesselung.standard.tools.Schluesselrolle;
import org.apache.commons.lang3.StringUtils;

public abstract class Verschluesselung {
    private static final int ANZAHL_SCHLUESSELROLLEN = 5;

    protected final String verschluesselnIntern(String object, String oeffentlicherSchluessel) {
        List<Schluesselrolle> rollen = this.generiereRollen(oeffentlicherSchluessel);
        int posRollen = 0;
        StringBuilder builder = new StringBuilder();
        int pos = 0;
        while (pos < object.length()) {
            builder.append(rollen.get(posRollen).getWert(StringUtils.mid((String)object, (int)pos, (int)1)));
            if (++posRollen >= rollen.size()) {
                posRollen = 0;
            }
            ++pos;
        }
        return builder.toString();
    }

    protected final String entschluesselnIntern(String verschluesseltesObjekt, String oeffentlicherSchluessel) {
        List<Schluesselrolle> privaterSchluessel = this.generiereRollen(oeffentlicherSchluessel);
        int posPrivaterSchluessel = 0;
        StringBuilder builder = new StringBuilder();
        int pos = 0;
        while (pos < verschluesseltesObjekt.length()) {
            builder.append(privaterSchluessel.get(posPrivaterSchluessel).getAusgangsWert(StringUtils.mid((String)verschluesseltesObjekt, (int)pos, (int)2)));
            if (++posPrivaterSchluessel >= privaterSchluessel.size()) {
                posPrivaterSchluessel = 0;
            }
            pos += 2;
        }
        return builder.toString();
    }

    private List<Schluesselrolle> generiereRollen(String oeffentlicherSchluessel) {
        ArrayList<Schluesselrolle> rollen = new ArrayList<Schluesselrolle>();
        int a = 0;
        while (a < 5) {
            rollen.add(new Schluesselrolle(oeffentlicherSchluessel.hashCode() % (39 - a)));
            ++a;
        }
        return rollen;
    }

    protected String defaultString(int laenge) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < laenge) {
            long index = Math.round(Math.random() * (double)"QWERTZUIOPASDFGHJKLYXCVBNM1234567890qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM1234567890qwertzuiopasdfghjklyxcvbnm".length());
            builder.append(StringUtils.mid((String)"QWERTZUIOPASDFGHJKLYXCVBNM1234567890qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM1234567890qwertzuiopasdfghjklyxcvbnm", (int)((int)index), (int)1));
            ++i;
        }
        return builder.toString();
    }
}

