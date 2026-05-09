/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.VeranstaltungListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        VeranstaltungListeAO fenster = new VeranstaltungListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

