/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.AnwesenheitListeOptionenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitListeOptionenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AnwesenheitListeOptionenAO fenster = new AnwesenheitListeOptionenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

