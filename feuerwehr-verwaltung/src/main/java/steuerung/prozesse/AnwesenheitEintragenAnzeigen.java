/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.AnwesenheitEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitEintragenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AnwesenheitEintragenAO fenster = new AnwesenheitEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

