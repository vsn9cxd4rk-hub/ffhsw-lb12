/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.mangelmeldung.MangelkommentarAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class M\u00e4ngelmeldungKommentarAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MangelkommentarAnlegenAO fenster = new MangelkommentarAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

