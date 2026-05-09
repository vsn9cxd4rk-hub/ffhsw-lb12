/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.email;

import ao.email.EMailModulAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EmailModulAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EMailModulAO fenster = new EMailModulAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

