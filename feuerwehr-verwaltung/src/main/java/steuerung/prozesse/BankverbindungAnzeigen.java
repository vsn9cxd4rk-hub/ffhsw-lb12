/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mitglieder.MitgliederBankverbindungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BankverbindungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederBankverbindungAnlegenAO fenster = new MitgliederBankverbindungAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

