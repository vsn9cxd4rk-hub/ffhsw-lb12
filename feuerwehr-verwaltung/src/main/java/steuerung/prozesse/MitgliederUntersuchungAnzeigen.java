/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederUntersuchungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederUntersuchungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederUntersuchungAnlegenAO fenster = new MitgliederUntersuchungAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

