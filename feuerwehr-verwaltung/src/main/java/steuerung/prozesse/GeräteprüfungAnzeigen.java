/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.Ger\u00e4tepr\u00fcfungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class Ger\u00e4tepr\u00fcfungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        Ger\u00e4tepr\u00fcfungAO fenster = new Ger\u00e4tepr\u00fcfungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

