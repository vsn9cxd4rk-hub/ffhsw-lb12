/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederArbeitAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArbeitgeberAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederArbeitAnlegenAO fenster = new MitgliederArbeitAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

