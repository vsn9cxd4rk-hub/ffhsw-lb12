/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.einsatz.EinsatzBerichtAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinsatzBerichtAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EinsatzBerichtAO fenster = new EinsatzBerichtAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

