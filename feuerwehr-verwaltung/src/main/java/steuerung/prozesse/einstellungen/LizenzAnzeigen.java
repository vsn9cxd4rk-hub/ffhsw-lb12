/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.einstellungen;

import ao.einstellungen.NutzungsbedingungenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LizenzAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        NutzungsbedingungenAO fenster = new NutzungsbedingungenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

