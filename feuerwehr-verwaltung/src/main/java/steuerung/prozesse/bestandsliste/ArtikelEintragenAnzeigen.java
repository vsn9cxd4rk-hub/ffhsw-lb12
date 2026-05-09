/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.ArtikelBestandslisteEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArtikelEintragenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ArtikelBestandslisteEintragenAO fenster = new ArtikelBestandslisteEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

