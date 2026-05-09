/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.email;

import ao.email.EMailModulSignaturAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EMailModuleSignaturAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EMailModulSignaturAO fenster = new EMailModulSignaturAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

