/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.einsatz.Verdienstausfall_ZeitenAnpassenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class Verdienstausfall_ZeitenAnpassenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        Verdienstausfall_ZeitenAnpassenAO fenster = new Verdienstausfall_ZeitenAnpassenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

