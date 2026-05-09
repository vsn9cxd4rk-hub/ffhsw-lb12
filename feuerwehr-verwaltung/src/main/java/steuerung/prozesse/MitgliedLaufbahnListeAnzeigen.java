/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.MitgliederLaufbahnListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliedLaufbahnListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederLaufbahnListeAO fenster = new MitgliederLaufbahnListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

