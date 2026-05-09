/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.AusbildungsinhaltTauschenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungsinhalteTauschenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AusbildungsinhaltTauschenAO fenster = new AusbildungsinhaltTauschenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

