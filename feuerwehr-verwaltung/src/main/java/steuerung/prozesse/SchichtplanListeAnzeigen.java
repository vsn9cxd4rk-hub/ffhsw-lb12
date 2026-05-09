/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.SchichtplanListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtplanListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        SchichtplanListeAO fenster = new SchichtplanListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

