/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.einstellungen;

import ao.einstellungen.ProduktKeyEingebenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ProduktKeyEintragenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ProduktKeyEingebenAO fenster = new ProduktKeyEingebenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

