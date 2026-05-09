/*
 * Decompiled with CFR 0.152.
 */
package ao.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import run.images;

public class StartBildschirmAO {
    public static JDialog startDialog = new JDialog();
    public static JLabel startDialogText;

    public static void start() throws InterruptedException {
        Thread thread = new Thread(){

            @Override
            public void run() {
                startDialog.setTitle("FeuerwehrManagementSystem Version: 3.21");
                startDialogText = new JLabel("Initialisiere FeuerwehrManagementSystem...");
                startDialogText.setFont(new Font("Arial", 1, 16));
                System.out.println("Initialisiere StartbildschirmAO");
                images images2 = new images();
                startDialog.setIconImage(images2.loadImagesFromJARStartbildschirmIcon().getImage());
                startDialog.setLayout(new FlowLayout());
                startDialog.add(new JLabel(images2.loadImagesFromJARStartbildschirm()));
                startDialog.setBackground(Color.WHITE);
                startDialog.getContentPane().setBackground(Color.white);
                startDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
                startDialog.add(startDialogText);
                startDialog.setSize(1210, 240);
                System.out.println("StartbildschirmAO - setPosition()");
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                int x = (screenSize.width - startDialog.getWidth()) / 2;
                int y = (screenSize.height - startDialog.getHeight()) / 2;
                startDialog.setLocation(x, y);
                startDialog.setDefaultCloseOperation(0);
                startDialog.setModal(true);
                startDialog.setUndecorated(true);
                startDialog.setVisible(true);
                System.out.println("StartbildschirmAO - startDialog.setVisible(true)");
            }
        };
        thread.start();
        Thread.sleep(800L);
    }
}

