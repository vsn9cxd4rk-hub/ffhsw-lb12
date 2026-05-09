/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.BestandVerschiebenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BestandVerschiebenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        BestandVerschiebenAO fenster = new BestandVerschiebenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

