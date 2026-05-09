/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.karte;

import ao.karte.ImportAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ImportAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ImportAO fenster = new ImportAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

