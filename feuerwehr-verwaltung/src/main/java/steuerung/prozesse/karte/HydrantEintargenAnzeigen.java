/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.karte;

import ao.karte.HydrantEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class HydrantEintargenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        HydrantEintragenAO fenster = new HydrantEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

