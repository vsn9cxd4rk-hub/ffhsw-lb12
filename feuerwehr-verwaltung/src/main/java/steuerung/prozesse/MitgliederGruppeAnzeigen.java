/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederGruppeAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederGruppeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederGruppeAnlegenAO fenster = new MitgliederGruppeAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

