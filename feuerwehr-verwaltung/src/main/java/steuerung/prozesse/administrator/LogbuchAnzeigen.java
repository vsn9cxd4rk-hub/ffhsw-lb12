/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.administrator;

import ao.administrator.LogbuchAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LogbuchAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        LogbuchAO fenster = new LogbuchAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

