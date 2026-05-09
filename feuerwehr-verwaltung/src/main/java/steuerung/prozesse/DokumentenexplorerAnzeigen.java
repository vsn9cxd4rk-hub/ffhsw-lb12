/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.DokumenteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class DokumentenexplorerAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        DokumenteAO fenster = new DokumenteAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

