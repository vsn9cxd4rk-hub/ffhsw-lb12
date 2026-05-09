/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.schichtplaner.SchichtAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        SchichtAnlegenAO fenster = new SchichtAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

