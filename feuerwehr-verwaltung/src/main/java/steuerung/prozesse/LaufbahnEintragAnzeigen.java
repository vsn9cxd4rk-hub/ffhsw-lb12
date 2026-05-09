/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederLaufbahnPflegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LaufbahnEintragAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederLaufbahnPflegenAO fenster = new MitgliederLaufbahnPflegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

