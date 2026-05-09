/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.ArbeitgeberListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArbeitgeberListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ArbeitgeberListeAO fenster = new ArbeitgeberListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

