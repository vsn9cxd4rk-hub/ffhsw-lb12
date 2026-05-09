/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.einstellungen;

import ao.einstellungen.EinstellungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinstellungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EinstellungAO fenster = new EinstellungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

