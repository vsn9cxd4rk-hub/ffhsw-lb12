/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.email;

import ao.email.NeueEMailAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class NeueEmailAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        NeueEMailAO fenster = new NeueEMailAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

