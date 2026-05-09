/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.AusbildungsInhaltEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AusbildungsInhaltEintragenAO fenster = new AusbildungsInhaltEintragenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

