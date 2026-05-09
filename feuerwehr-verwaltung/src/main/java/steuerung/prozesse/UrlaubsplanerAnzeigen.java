/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.urlaubsplaner.UrlaubsplanerAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class UrlaubsplanerAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        UrlaubsplanerAO fenster = new UrlaubsplanerAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

