/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.LehrgangZuordnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangZuordnungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        LehrgangZuordnungAO fenster = new LehrgangZuordnungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

