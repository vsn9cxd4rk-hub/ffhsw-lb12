/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.LehrgangListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        LehrgangListeAO fenster = new LehrgangListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

