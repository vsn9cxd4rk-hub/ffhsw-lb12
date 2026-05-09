/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugBelegungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugBelegungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        FahrzeugBelegungAO fenster = new FahrzeugBelegungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

