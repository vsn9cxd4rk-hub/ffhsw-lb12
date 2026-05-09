/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.MitgliederBankverbindungListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederBankverbindungListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederBankverbindungListeAO fenster = new MitgliederBankverbindungListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

