/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.LehrgangListeOptionenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangListeOptionenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        LehrgangListeOptionenAO fenster = new LehrgangListeOptionenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

