package listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import run.runApplication;
import service.DatensicherungService;
import utilities.Konstante;
import utilities.SbcUtils;

public class DbBackupListener extends AbstractActionListener {

   public DbBackupListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog((Component)null, Konstante.DB_BACKUP_AUSFUEHREN);
      DatensicherungService.ausfuehrenDBSave(runApplication.arbeitsverzeichnis + "data/DBBACKUP/databasebackup_" + SbcUtils.timeStamp("yyyy-MM-dd") + ".sql");
   }
}
