package listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class BeendenListener extends AbstractActionListener {

   public BeendenListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      if(runApplication.verarbeitungLäuft == 1) {
         JOptionPane.showMessageDialog((Component)null, Konstante.VERARBEITUNG_LÄUFT, "Warnung", 2);
      } else {
         logging.logInfo("Programm wird beendet...");
         System.exit(0);
      }

   }
}
