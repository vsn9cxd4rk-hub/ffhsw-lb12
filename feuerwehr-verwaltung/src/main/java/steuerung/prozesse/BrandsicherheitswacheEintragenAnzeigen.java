/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.BrandsicherheitswacheEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BrandsicherheitswacheEintragenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        BrandsicherheitswacheEintragenAO fenster = new BrandsicherheitswacheEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

