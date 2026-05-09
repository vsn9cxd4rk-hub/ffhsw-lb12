/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  utilities.SbcUtils
 */
package listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import run.runApplication;
import service.DatensicherungService;
import utilities.Konstante;
import utilities.SbcUtils;

public class DbBackupListener
extends AbstractActionListener {
    public DbBackupListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, Konstante.DB_BACKUP_AUSFUEHREN);
        DatensicherungService.ausfuehrenDBSave(String.valueOf(runApplication.arbeitsverzeichnis) + "data/DBBACKUP/databasebackup_" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + ".sql");
    }
}

