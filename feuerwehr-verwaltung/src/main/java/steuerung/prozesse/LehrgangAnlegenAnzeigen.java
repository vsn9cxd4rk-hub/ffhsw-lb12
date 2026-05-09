/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse;

import ao.ausbildung.MitgliederfunktionenVerwaltenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangAnlegenAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        MitgliederfunktionenVerwaltenAO fenster = new MitgliederfunktionenVerwaltenAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

