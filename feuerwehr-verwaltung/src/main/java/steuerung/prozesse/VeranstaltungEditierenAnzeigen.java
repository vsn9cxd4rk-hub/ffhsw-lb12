/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.veranstaltung.VeranstaltungEditierenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungEditierenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        VeranstaltungEditierenAO fenster = new VeranstaltungEditierenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

