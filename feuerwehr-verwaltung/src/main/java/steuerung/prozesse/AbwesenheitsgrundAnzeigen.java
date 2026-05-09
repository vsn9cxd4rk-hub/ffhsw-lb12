/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.AbwesenheitsgrundAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbwesenheitsgrundAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        AbwesenheitsgrundAnlegenAO fenster = new AbwesenheitsgrundAnlegenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

