/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  logging.logging
 */
package listener;

import ao.karte.HydrantEintragenAO;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleStrassen;
import go.Hydrant;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import utilities.Konstante;

public class HydrantSpeichernListener
extends AbstractActionListener {
    public HydrantSpeichernListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        TabelleHydranten tabHydrant = new TabelleHydranten();
        TabelleStrassen tabStrassen = new TabelleStrassen();
        Hydrant hydrant = new Hydrant();
        try {
            int nextid = tabHydrant.getNextIndex();
            if (HydrantEintragenAO.StrassenName2.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                hydrant.setHausnummer("Haus-Nr: " + HydrantEintragenAO.hausnummer.getText());
            } else {
                hydrant.setHausnummer(HydrantEintragenAO.lageHydrant.getSelectedItem() + " " + HydrantEintragenAO.StrassenName.getSelectedItem() + " / " + HydrantEintragenAO.StrassenName2.getSelectedItem());
            }
            hydrant.setId(nextid);
            hydrant.setNennweite(Integer.parseInt(HydrantEintragenAO.nennweite.getText()));
            hydrant.setStrassenid(tabStrassen.getStrassenNumber((String)HydrantEintragenAO.StrassenName.getSelectedItem()));
            tabHydrant.insert(hydrant);
            HydrantEintragenAO.insertErfolgreich.setText("Eintrag erstellt: HydrantId: " + hydrant.getId() + ", Stra\u00dfe: " + HydrantEintragenAO.StrassenName.getSelectedItem() + ", " + hydrant.getHausnummer() + ", Nennweite: " + hydrant.getNennweite());
            logging.logInfo((Object)("Eintrag erstellt: HydrantId: " + hydrant.getId() + ", " + hydrant.getHausnummer() + ", Nennweite: " + hydrant.getNennweite()));
        }
        catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e1);
        }
    }
}

