/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 */
package listener;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import run.images;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Konstante;

public class InfoListener
extends AbstractActionListener {
    public InfoListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        ImageIcon icon;
        if (runApplication.JavaWebStart == 1) {
            icon = new images().loadImagesFromJARStartbildschirmIconCloud();
            icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 150), BildUmrechnenService.bildHoeheVerkleinern(icon, 150), 0));
        } else {
            icon = new images().loadImagesFromJARStartbildschirmIcon();
            icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 150), BildUmrechnenService.bildHoeheVerkleinern(icon, 150), 0));
        }
        JOptionPane.showMessageDialog(null, String.valueOf(Konstante.PROGRAMM_INFO) + "Version: 3.21" + "\n\n", "Info", 1, icon);
    }
}

