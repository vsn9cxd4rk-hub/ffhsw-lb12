/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.DienstgradAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class DienstgradAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        DienstgradAnlegenAO fenster = new DienstgradAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

