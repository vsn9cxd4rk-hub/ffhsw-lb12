/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mangelmeldung.M\u00e4ngelmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class M\u00e4ngelmeldungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        M\u00e4ngelmeldungAO fenster = new M\u00e4ngelmeldungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

