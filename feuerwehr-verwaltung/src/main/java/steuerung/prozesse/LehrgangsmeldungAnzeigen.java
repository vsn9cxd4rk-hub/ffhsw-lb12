/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.listen.LehrgangsmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangsmeldungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        LehrgangsmeldungAO fenster = new LehrgangsmeldungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

