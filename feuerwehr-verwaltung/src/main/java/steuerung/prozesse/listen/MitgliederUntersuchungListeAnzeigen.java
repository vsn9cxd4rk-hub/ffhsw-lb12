/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.UntersuchungListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederUntersuchungListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        UntersuchungListeAO fenster = new UntersuchungListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

