package listener;

import ao.karte.KarteAO;
import com.itextpdf.text.DocumentException;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import pdfdocumente.karte.AlarmInfoPDFSchreiben;
import run.runApplication;
import utilities.Konstante;
import utilities.PDFPrinter;
import utilities.SbcUtils;
import utilities.Utils;

public class DruckenListener extends AbstractActionListener {

   public DruckenListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      if(KarteAO.StrasseSuchen.getSelectedItem().equals("<bitte wählen>")) {
         logging.logInfo("Es wurde keine Staraße ausgewählt");
         JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_STRASSE_WAEHLEN, "Warnung", 2);
      } else {
         String dateiName = runApplication.arbeitsverzeichnis + "data/AlarmInfo_" + SbcUtils.timeStamp("yyyy-MM-dd") + ".pdf";

         try {
            Utils.dateiKatalogisieren(dateiName);
            AlarmInfoPDFSchreiben.PDFdocumentErstellen(dateiName);
         } catch (DocumentException var4) {
            logging.logPrintStackTrace(var4);
         } catch (IOException var5) {
            logging.logPrintStackTrace(var5);
         } catch (SQLException var6) {
            logging.logPrintStackTrace(var6);
         }

         new PDFPrinter(dateiName);
         JOptionPane.showMessageDialog((Component)null, Konstante.DRUCKAUFTRAG_VERSENDET);
      }

   }
}
