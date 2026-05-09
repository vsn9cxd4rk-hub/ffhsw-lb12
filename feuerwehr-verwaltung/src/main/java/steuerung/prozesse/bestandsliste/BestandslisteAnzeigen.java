/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.BestandslisteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BestandslisteAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        BestandslisteAO fenster = new BestandslisteAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

