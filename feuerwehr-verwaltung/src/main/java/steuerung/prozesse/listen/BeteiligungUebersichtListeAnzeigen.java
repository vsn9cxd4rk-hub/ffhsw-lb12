/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.BeteiligungUebersichtListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BeteiligungUebersichtListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        BeteiligungUebersichtListeAO fenster = new BeteiligungUebersichtListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

