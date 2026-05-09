/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.listen;

import ao.listen.GeburtstagListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class GeburtstagListeAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        GeburtstagListeAO fenster = new GeburtstagListeAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

