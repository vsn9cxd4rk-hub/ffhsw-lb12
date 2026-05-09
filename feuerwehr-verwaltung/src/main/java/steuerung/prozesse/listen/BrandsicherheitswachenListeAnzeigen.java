/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.BrandsicherheitswachenListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BrandsicherheitswachenListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        BrandsicherheitswachenListeAO fenster = new BrandsicherheitswachenListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

