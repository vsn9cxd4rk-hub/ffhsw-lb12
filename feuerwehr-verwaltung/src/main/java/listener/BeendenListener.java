/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  logging.logging
 */
package listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class BeendenListener
extends AbstractActionListener {
    public BeendenListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        if (runApplication.verarbeitungL\u00e4uft == 1) {
            JOptionPane.showMessageDialog(null, Konstante.VERARBEITUNG_L\u00c4UFT, "Warnung", 2);
        } else {
            logging.logInfo((Object)"Programm wird beendet...");
            System.exit(0);
        }
    }
}

