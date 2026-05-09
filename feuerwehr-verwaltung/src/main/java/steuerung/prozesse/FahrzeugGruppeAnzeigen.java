/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugGruppeAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugGruppeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        FahrzeugGruppeAnlegenAO fenster = new FahrzeugGruppeAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

