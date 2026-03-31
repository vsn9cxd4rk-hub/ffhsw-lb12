package thread;

import ao.administrator.DebugAO;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import logging.logging;
import run.runApplication;
import utilities.MyEvent;

public class DebugThread {

   public static void run() {
      try {
         DebugAO.textarea.setText("Lade Debug...");
         Thread.sleep(2000L);
         File e = new File(runApplication.arbeitsverzeichnis + "log/" + "FeuerwehrManagementSystem" + "_log.log");

         while(!MyEvent.event.equals("0x0401")) {
            DebugAO.textarea.read(new FileReader(e), "");
            DebugAO.pane.getVerticalScrollBar().setValue(DebugAO.pane.getVerticalScrollBar().getMaximum());
            Thread.sleep(5000L);
         }
      } catch (IOException var1) {
         logging.logPrintStackTrace(var1);
      }

   }

   public static void stop() {
      try {
         File e = new File(runApplication.arbeitsverzeichnis + "log/" + "FeuerwehrManagementSystem" + "_log.log");
         DebugAO.textarea.read(new FileReader(e), "");
         DebugAO.pane.getVerticalScrollBar().setValue(DebugAO.pane.getVerticalScrollBar().getMaximum());
      } catch (IOException var1) {
         logging.logPrintStackTrace(var1);
      }

   }
}
