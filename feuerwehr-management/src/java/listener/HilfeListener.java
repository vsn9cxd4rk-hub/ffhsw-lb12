package listener;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class HilfeListener extends AbstractActionListener {

   public HilfeListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      try {
         JFrame e1 = new JFrame("Frage");
         String[] listeAnleitungen = new String[]{"Bedienungsanleitung", "Dokumentation - Aktuelle Software Änderungen", "FMS - Webseite", "Schnittstellenbeschreibung - Ausbildungsplan", "Schnittstellenbeschreibung - Einsatzkomponente", "Schnittstellenbeschreibung - Veranstaltungen", "Tool-Installation", "FMS - Datenkonzepte"};
         String auswahlBox = (String)JOptionPane.showInputDialog(e1, "Hier finden Sie die Dokumentation des FeuerwehrManagementSystem:\n\n", "Frage", 3, (Icon)null, listeAnleitungen, listeAnleitungen[0]);
         if(auswahlBox != null) {
            String dateiname = "";
            if(runApplication.JavaWebStart == 1) {
               if(auswahlBox.equals("Bedienungsanleitung")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf";
               } else if(auswahlBox.equals("Dokumentation - Aktuelle Software Änderungen")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/aktuelleSoftwareänderungen.html";
               } else if(auswahlBox.equals("FMS - Webseite")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/homepage.html";
               } else if(auswahlBox.equals("Schnittstellenbeschreibung - Ausbildungsplan")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
               } else if(auswahlBox.equals("Schnittstellenbeschreibung - Einsatzkomponente")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
               } else if(auswahlBox.equals("Schnittstellenbeschreibung - Veranstaltungen")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
               } else if(auswahlBox.equals("Tool-Installation")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Tools.pdf";
               } else if(auswahlBox.equals("FMS - Datenkonzepte")) {
                  dateiname = runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf";
               }
            } else if(auswahlBox.equals("Bedienungsanleitung")) {
               dateiname = "Feuerwehr Management System - Bedienugsanleitung.pdf";
            } else if(auswahlBox.equals("Dokumentation - Aktuelle Software Änderungen")) {
               dateiname = "aktuelleSoftwareänderungen.html";
            } else if(auswahlBox.equals("FMS - Webseite")) {
               dateiname = "homepage.html";
            } else if(auswahlBox.equals("Schnittstellenbeschreibung - Ausbildungsplan")) {
               dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
            } else if(auswahlBox.equals("Schnittstellenbeschreibung - Einsatzkomponente")) {
               dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
            } else if(auswahlBox.equals("Schnittstellenbeschreibung - Veranstaltungen")) {
               dateiname = "Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
            } else if(auswahlBox.equals("Tool-Installation")) {
               dateiname = "Feuerwehr Management System - Tools.pdf";
            } else if(auswahlBox.equals("FMS - Datenkonzepte")) {
               dateiname = "FeuerwehrManagementSystem - Datenkonzept.pdf";
            }

            Desktop.getDesktop().open(new File(dateiname));
         }
      } catch (Exception var6) {
         logging.logPrintStackTrace(var6);
         JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
      }

   }
}
