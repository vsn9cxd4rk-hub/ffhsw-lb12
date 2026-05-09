/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.Bef\u00f6rderungZuordnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class Bef\u00f6rderungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        Bef\u00f6rderungZuordnungAO fenster = new Bef\u00f6rderungZuordnungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

