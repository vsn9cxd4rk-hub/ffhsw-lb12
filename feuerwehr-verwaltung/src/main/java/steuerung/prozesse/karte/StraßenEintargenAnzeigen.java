/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.karte;

import ao.karte.Stra\u00dfeEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class Stra\u00dfenEintargenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        Stra\u00dfeEintragenAO fenster = new Stra\u00dfeEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

