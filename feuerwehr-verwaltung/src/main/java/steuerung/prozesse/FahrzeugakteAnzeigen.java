/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugakteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugakteAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        FahrzeugakteAO fenster = new FahrzeugakteAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

