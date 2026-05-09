/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.abrechnung;

import ao.abrechnung.ManuelleVerbuchungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungManuelleVerbuchungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        ManuelleVerbuchungAO fenster = new ManuelleVerbuchungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

