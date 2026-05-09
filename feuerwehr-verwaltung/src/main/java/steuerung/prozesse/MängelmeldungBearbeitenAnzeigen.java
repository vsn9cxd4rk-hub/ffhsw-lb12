/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mangelmeldung.M\u00e4ngelmeldungBearbeitenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class M\u00e4ngelmeldungBearbeitenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        M\u00e4ngelmeldungBearbeitenAO fenster = new M\u00e4ngelmeldungBearbeitenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

