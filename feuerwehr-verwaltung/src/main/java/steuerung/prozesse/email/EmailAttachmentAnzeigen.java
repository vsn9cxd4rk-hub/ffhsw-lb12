/*
 * Decompiled with CFR 0.152.
 */
package steuerung.prozesse.email;

import ao.email.EMailAttachmentAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EmailAttachmentAnzeigen
extends Anzeige {
    @Override
    public void ausfuehren() {
        EMailAttachmentAO fenster = new EMailAttachmentAO();
        fenster.fensterAnzeigen();
        this.setStatus(Status.ENDE);
    }
}

