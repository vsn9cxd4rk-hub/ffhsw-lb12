/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.einsatz.VerdienstausfallAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VerdienstausfallAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        VerdienstausfallAO fenster = new VerdienstausfallAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

