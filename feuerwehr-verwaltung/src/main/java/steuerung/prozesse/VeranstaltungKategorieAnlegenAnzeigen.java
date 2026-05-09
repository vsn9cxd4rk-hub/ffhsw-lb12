/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.veranstaltung.VeranstaltungKategorieAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungKategorieAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        VeranstaltungKategorieAnlegenAO fenster = new VeranstaltungKategorieAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

