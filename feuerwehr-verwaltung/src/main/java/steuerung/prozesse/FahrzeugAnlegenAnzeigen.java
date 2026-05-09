/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        FahrzeugAnlegenAO fenster = new FahrzeugAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

