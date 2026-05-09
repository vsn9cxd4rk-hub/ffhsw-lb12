/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.ArtikelZuweisenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArtikelZuweisenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ArtikelZuweisenAO fenster = new ArtikelZuweisenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

