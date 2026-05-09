/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederHistoryListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederHistoryAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederHistoryListeAO fenster = new MitgliederHistoryListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

