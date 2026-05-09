/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package ao.utils;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import logging.logging;
import run.images;
import steuerung.Status;
import steuerung.Steuerung;

public class SystemTrayInfo {
    public static SystemTray tray;
    public static TrayIcon icon;

    public void InfoIconFTP(final String text) throws Exception {
        if (!SystemTray.isSupported()) {
            logging.logInfo((Object)"SystemTray wird nicht unterst\u00fctzt");
            return;
        }
        tray = SystemTray.getSystemTray();
        Image image = new images().loadImagesFromJarFtpIcon().getImage();
        PopupMenu menu = new PopupMenu();
        icon = new TrayIcon(image, "Nachricht", menu);
        MenuItem messageItem = new MenuItem("Nachricht anzeigen");
        messageItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, text);
            }
        });
        MenuItem closeItem = new MenuItem("Nachricht l\u00f6schen");
        closeItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(icon);
            }
        });
        menu.add(messageItem);
        menu.add(closeItem);
        icon.setImageAutoSize(true);
        tray.add(icon);
    }

    public void InfoIconUnwetterwarnung(String unwtterwarnungText) throws Exception {
        if (!SystemTray.isSupported()) {
            logging.logInfo((Object)"SystemTray wird nicht unterst\u00fctzt");
            return;
        }
        tray = SystemTray.getSystemTray();
        Image image = new images().loadImagesFromJarFtpIcon().getImage();
        PopupMenu menu = new PopupMenu();
        icon = new TrayIcon(image, unwtterwarnungText, menu);
        MenuItem messageItem = new MenuItem("Unwetterwarnung anzeigen");
        messageItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Steuerung.setStatus(Status.UNWETTERWARNUNG);
                Steuerung.steuerung();
            }
        });
        MenuItem closeItem = new MenuItem("Unwetterwarnung l\u00f6schen");
        closeItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(icon);
            }
        });
        MenuItem dwdSeite = new MenuItem("Internetseite - dwd.de");
        dwdSeite.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("http://www.dwd.de"));
                }
                catch (IOException | URISyntaxException e1) {
                    logging.logError((Object)"Beim \u00f6ffnen des Browsers ist ein Fehler aufgetreten...");
                }
            }
        });
        menu.add(messageItem);
        menu.add(dwdSeite);
        menu.add(closeItem);
        icon.setImageAutoSize(true);
        tray.add(icon);
    }

    public void removeInfoIcon() {
        tray.remove(icon);
    }
}

