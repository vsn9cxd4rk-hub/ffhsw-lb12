/*
 * Decompiled with CFR 0.152.
 */
package steuerung.unwetterwarnung;

import ao.unwetterwarnung.UnwetterwarnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class UnwetterwarnungAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        UnwetterwarnungAO fenster = new UnwetterwarnungAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

