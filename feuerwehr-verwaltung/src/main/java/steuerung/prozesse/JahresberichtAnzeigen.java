/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.JahresberichtAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class JahresberichtAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        JahresberichtAO fenster = new JahresberichtAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

