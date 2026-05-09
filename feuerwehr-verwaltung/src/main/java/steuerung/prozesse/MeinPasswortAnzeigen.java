/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.einstellungen.MeinPasswortAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MeinPasswortAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MeinPasswortAO fenster = new MeinPasswortAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

