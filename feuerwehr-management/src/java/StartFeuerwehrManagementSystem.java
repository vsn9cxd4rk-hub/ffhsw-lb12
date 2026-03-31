import ao.utils.StartBildschirmAO;
import java.awt.Component;
import java.io.File;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;

public class StartFeuerwehrManagementSystem {

   public static void main(String[] args) {
      String workingDirectory;
      if((new File("c:/ProgramData/FeuerwehrManagemantSystem/useHere")).exists()) {
         workingDirectory = "c:/ProgramData/FeuerwehrManagemantSystem/";
      } else {
         workingDirectory = "";
      }

      try {
         StartBildschirmAO.start();
         logging.logInit(workingDirectory + "log", workingDirectory + "properties", "FeuerwehrManagementSystem", "Version: 4.08");
         logging.logInfo("##########################");
         MyEvent.initEvent();
         logging.logInfo("Programm wird ausgeführt aus: " + System.getProperty("user.dir"));
         logging.logInfo("Windows Benutzer Name / IP: " + System.getProperty("user.name") + " / " + InetAddress.getLocalHost());
         logging.logInfo("Java Version: " + System.getProperty("java.version"));
         logging.logInfo("Betriebssystem: " + System.getProperty("os.name"));
         logging.logInfo("Betriebssystem Architktur: " + System.getProperty("os.arch"));
         logging.logInfo("Betriebssystem Form : " + System.getenv("ProgramW6432"));
         runApplication.prepareStart(args, workingDirectory);
      } catch (Exception var3) {
         StartBildschirmAO.startDialog.setVisible(false);
         logging.logPrintStackTrace(var3);
         JOptionPane.showMessageDialog((Component)null, Konstante.SCHWERWIEGENDER_AUSNAHMEFAHLER + "\n\n" + var3 + "\n\nBitte kontaktieren Sie Ihren Systemadministrator um das Problem zu beheben!", "Fehlermeldung", 0);
         System.exit(0);
      }

   }
}
