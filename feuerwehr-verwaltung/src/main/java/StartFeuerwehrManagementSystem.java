/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.MyEvent
 */
import ao.utils.StartBildschirmAO;
import java.io.File;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;

public class StartFeuerwehrManagementSystem {
    public static void main(String[] args) {
        String workingDirectory = new File("c:/ProgramData/FeuerwehrManagemantSystem/useHere").exists() ? "c:/ProgramData/FeuerwehrManagemantSystem/" : "";
        try {
            StartBildschirmAO.start();
            logging.logInit((String)(String.valueOf(workingDirectory) + "log"), (String)(String.valueOf(workingDirectory) + "properties"), (String)"FeuerwehrManagementSystem", (String)"Version: 3.21");
            logging.logInfo((Object)"##########################");
            MyEvent.initEvent();
            logging.logInfo((Object)("Programm wird ausgef\u00fchrt aus: " + System.getProperty("user.dir")));
            logging.logInfo((Object)("Windows Benutzer Name / IP: " + System.getProperty("user.name") + " / " + InetAddress.getLocalHost()));
            logging.logInfo((Object)("Java Version: " + System.getProperty("java.version")));
            logging.logInfo((Object)("Betriebssystem: " + System.getProperty("os.name")));
            logging.logInfo((Object)("Betriebssystem Architktur: " + System.getProperty("os.arch")));
            logging.logInfo((Object)("Betriebssystem Form : " + System.getenv("ProgramW6432")));
            runApplication.prepareStart(args, workingDirectory);
        }
        catch (Exception e) {
            StartBildschirmAO.startDialog.setVisible(false);
            logging.logPrintStackTrace((Exception)e);
            JOptionPane.showMessageDialog(null, Konstante.SCHWERWIEGENDER_AUSNAHMEFAHLER + "\n\n" + e + "\n\nBitte kontaktieren Sie Ihren Systemadministrator um das Problem zu beheben!", "Fehlermeldung", 0);
            System.exit(0);
        }
    }
}

