/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.statistik.StatistikAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class StatistikAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        StatistikAO fenster = new StatistikAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

