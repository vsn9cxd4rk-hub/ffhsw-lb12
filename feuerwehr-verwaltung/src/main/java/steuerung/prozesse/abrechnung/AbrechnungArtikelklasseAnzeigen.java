/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.abrechnung;

import ao.abrechnung.ArtikelklasseAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungArtikelklasseAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ArtikelklasseAnlegenAO fenster = new ArtikelklasseAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

