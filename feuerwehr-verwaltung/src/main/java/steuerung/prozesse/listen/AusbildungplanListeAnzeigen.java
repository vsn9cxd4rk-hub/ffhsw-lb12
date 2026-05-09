/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.AusbildungsplanListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungplanListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AusbildungsplanListeAO fenster = new AusbildungsplanListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

