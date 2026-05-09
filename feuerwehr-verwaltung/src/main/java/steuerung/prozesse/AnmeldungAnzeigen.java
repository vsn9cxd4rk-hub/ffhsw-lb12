/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.AnmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnmeldungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AnmeldungAO fenster = new AnmeldungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

