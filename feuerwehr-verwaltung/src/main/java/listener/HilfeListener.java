/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  logging.logging
 */
package listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class HilfeListener
extends AbstractActionListener {
    public HilfeListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JFrame frame = new JFrame("Frage");
            Object[] listeAnleitungen = new String[]{"Bedienungsanleitung", "Dokumentation - Aktuelle Software \u00c4nderungen", "FMS - Webseite", "Schnittstellenbeschreibung - Ausbildungsplan", "Schnittstellenbeschreibung - Einsatzkomponente", "Schnittstellenbeschreibung - Veranstaltungen", "Tool-Installation", "FMS - Datenkonzepte"};
            String auswahlBox = (String)JOptionPane.showInputDialog(frame, "Hier finden Sie die Dokumentation des FeuerwehrManagementSystem:\n\n", "Frage", 3, null, listeAnleitungen, listeAnleitungen[0]);
            if (auswahlBox != null) {
                String dateiname = "";
                if (runApplication.JavaWebStart == 1) {
                    if (auswahlBox.equals("Bedienungsanleitung")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf";
                    } else if (auswahlBox.equals("Dokumentation - Aktuelle Software \u00c4nderungen")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/aktuelleSoftware\u00e4nderungen.html";
                    } else if (auswahlBox.equals("FMS - Webseite")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/homepage.html";
                    } else if (auswahlBox.equals("Schnittstellenbeschreibung - Ausbildungsplan")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
                    } else if (auswahlBox.equals("Schnittstellenbeschreibung - Einsatzkomponente")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
                    } else if (auswahlBox.equals("Schnittstellenbeschreibung - Veranstaltungen")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
                    } else if (auswahlBox.equals("Tool-Installation")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Tools.pdf";
                    } else if (auswahlBox.equals("FMS - Datenkonzepte")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf";
                    }
                } else if (auswahlBox.equals("Bedienungsanleitung")) {
                    dateiname = "Feuerwehr Management System - Bedienugsanleitung.pdf";
                } else if (auswahlBox.equals("Dokumentation - Aktuelle Software \u00c4nderungen")) {
                    dateiname = "aktuelleSoftware\u00e4nderungen.html";
                } else if (auswahlBox.equals("FMS - Webseite")) {
                    dateiname = "homepage.html";
                } else if (auswahlBox.equals("Schnittstellenbeschreibung - Ausbildungsplan")) {
                    dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
                } else if (auswahlBox.equals("Schnittstellenbeschreibung - Einsatzkomponente")) {
                    dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
                } else if (auswahlBox.equals("Schnittstellenbeschreibung - Veranstaltungen")) {
                    dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
                } else if (auswahlBox.equals("Tool-Installation")) {
                    dateiname = "Feuerwehr Management System - Tools.pdf";
                } else if (auswahlBox.equals("FMS - Datenkonzepte")) {
                    dateiname = "FeuerwehrManagementSystem - Datenkonzept.pdf";
                }
                Desktop.getDesktop().open(new File(dateiname));
            }
        }
        catch (Exception e1) {
            logging.logPrintStackTrace((Exception)e1);
            JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
        }
    }
}

