/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederAnlegenAO fenster = new MitgliederAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

