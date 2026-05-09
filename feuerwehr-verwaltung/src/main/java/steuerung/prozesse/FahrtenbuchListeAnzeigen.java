/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.FahrtenbuchListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrtenbuchListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        FahrtenbuchListeAO fenster = new FahrtenbuchListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

