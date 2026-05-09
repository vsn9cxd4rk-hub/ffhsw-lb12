/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.schichtplaner.SchichtplanerAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtplanerAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        SchichtplanerAO fenster = new SchichtplanerAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

