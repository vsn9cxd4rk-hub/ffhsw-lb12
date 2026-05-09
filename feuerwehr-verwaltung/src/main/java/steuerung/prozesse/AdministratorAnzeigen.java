/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.administrator.AdministratorAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AdministratorAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AdministratorAO fenster = new AdministratorAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

