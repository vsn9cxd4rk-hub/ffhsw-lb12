/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.karte;

import ao.karte.KarteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class KarteAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        KarteAO fenster = new KarteAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

