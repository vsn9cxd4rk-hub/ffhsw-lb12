/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederAngehoerigenAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AngehoerigeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederAngehoerigenAnlegenAO fenster = new MitgliederAngehoerigenAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

