/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.abrechnung;

import ao.abrechnung.KontoAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungKontoAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        KontoAnlegenAO fenster = new KontoAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

