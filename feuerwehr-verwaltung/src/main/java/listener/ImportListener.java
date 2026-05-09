/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 */
package listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import listener.AbstractActionListener;
import steuerung.Status;
import steuerung.Steuerung;

public class ImportListener
extends AbstractActionListener {
    public ImportListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        Steuerung.setStatus(Status.IMPORT);
        Steuerung.steuerung();
    }
}

