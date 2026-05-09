/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.AngehoerigenListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AngehoerigeListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AngehoerigenListeAO fenster = new AngehoerigenListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

