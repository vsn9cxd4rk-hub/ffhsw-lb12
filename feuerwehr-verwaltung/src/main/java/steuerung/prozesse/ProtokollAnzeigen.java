/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ProtokollAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ProtokollAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ProtokollAO fenster = new ProtokollAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

