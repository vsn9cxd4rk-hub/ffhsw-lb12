/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.EinsatzListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinsatzListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EinsatzListeAO fenster = new EinsatzListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

