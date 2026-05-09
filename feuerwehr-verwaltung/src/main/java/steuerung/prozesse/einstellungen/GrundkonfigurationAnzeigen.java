/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.einstellungen;

import ao.einstellungen.GrundkonfigurationAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class GrundkonfigurationAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        GrundkonfigurationAO fenster = new GrundkonfigurationAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

