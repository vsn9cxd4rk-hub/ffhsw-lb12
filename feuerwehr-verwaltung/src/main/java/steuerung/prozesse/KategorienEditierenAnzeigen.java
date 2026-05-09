/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.KategorienEditierenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class KategorienEditierenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        KategorienEditierenAO fenster = new KategorienEditierenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

