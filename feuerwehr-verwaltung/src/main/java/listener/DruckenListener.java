/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.DocumentException
 *  listener.AbstractActionListener
 *  logging.logging
 *  utilities.SbcUtils
 */
package listener;

import ao.karte.KarteAO;
import com.itextpdf.text.DocumentException;
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

public class DruckenListener
extends AbstractActionListener {
    public DruckenListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        if (KarteAO.StrasseSuchen.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            logging.logInfo((Object)"Es wurde keine Stara\u00dfe ausgew\u00e4hlt");
            JOptionPane.showMessageDialog(null, Konstante.BITTE_STRASSE_WAEHLEN, "Warnung", 2);
        } else {
            String dateiName = String.valueOf(runApplication.arbeitsverzeichnis) + "data/AlarmInfo_" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + ".pdf";
            try {
                Utils.dateiKatalogisieren(dateiName);
                AlarmInfoPDFSchreiben.PDFdocumentErstellen(dateiName);
            }
            catch (DocumentException e1) {
                logging.logPrintStackTrace((Exception)((Object)e1));
            }
            catch (IOException e1) {
                logging.logPrintStackTrace((Exception)e1);
            }
            catch (SQLException e1) {
                logging.logPrintStackTrace((Exception)e1);
            }
            new PDFPrinter(dateiName);
            JOptionPane.showMessageDialog(null, Konstante.DRUCKAUFTRAG_VERSENDET);
        }
    }
}

