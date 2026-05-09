/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.schichtplaner.SchichtGruppeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtGruppeAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        SchichtGruppeAO fenster = new SchichtGruppeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

