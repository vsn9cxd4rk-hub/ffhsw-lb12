/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.veranstaltung.VeranstaltungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        VeranstaltungAnlegenAO fenster = new VeranstaltungAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

