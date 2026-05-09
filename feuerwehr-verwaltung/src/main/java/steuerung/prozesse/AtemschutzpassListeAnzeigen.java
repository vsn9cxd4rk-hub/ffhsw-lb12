/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.AtemschutzpassListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AtemschutzpassListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AtemschutzpassListeAO fenster = new AtemschutzpassListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

