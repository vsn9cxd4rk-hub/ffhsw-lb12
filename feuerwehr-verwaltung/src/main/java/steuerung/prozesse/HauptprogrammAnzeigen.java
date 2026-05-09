/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.HauptprogrammAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class HauptprogrammAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        HauptprogrammAO fenster = new HauptprogrammAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

