/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.einsatz.AtemschutzpassAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AtemschutzpassEintragAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AtemschutzpassAO fenster = new AtemschutzpassAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

