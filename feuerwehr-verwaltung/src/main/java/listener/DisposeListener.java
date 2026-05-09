/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  utilities.MyEvent
 */
package listener;

import ao.einstellungen.BenutzerAnlegenAO;
import ao.einstellungen.BerechtigungAnlegenAO;
import data.tabellen.einstellungen.TabelleClients;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import listener.AbstractActionListener;
import run.runApplication;
import utilities.MyEvent;

public class DisposeListener
extends AbstractActionListener {
    public DisposeListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        if (MyEvent.event.equals("0x0024")) {
            BenutzerAnlegenAO.berechtigungsgruppe.addItem(BerechtigungAnlegenAO.letzteBerechtigungsgruppe);
        }
        if (MyEvent.event.equals("0x0100")) {
            new TabelleClients().updateOnline(0);
        }
        MyEvent.setEvent((String)"0");
        runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
        this.getFrame().dispose();
    }
}

