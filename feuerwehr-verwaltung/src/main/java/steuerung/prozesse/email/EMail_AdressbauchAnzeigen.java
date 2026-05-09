/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.email;

import ao.email.EMail_AdressbuchAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EMail_AdressbauchAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EMail_AdressbuchAO fenster = new EMail_AdressbuchAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

