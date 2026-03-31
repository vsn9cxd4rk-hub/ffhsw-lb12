package ao.utils;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
      if(!SystemTray.isSupported()) {
         logging.logInfo("SystemTray wird nicht unterstützt");
      } else {
         tray = SystemTray.getSystemTray();
         Image image = (new images()).loadImagesFromJarFtpIcon().getImage();
         PopupMenu menu = new PopupMenu();
         icon = new TrayIcon(image, "Nachricht", menu);
         MenuItem messageItem = new MenuItem("Nachricht anzeigen");
         messageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog((Component)null, text);
            }
         });
         MenuItem closeItem = new MenuItem("Nachricht löschen");
         closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               SystemTrayInfo.tray.remove(SystemTrayInfo.icon);
            }
         });
         menu.add(messageItem);
         menu.add(closeItem);
         icon.setImageAutoSize(true);
         tray.add(icon);
      }
   }

   public void InfoIconUnwetterwarnung(String unwtterwarnungText) throws Exception {
      if(!SystemTray.isSupported()) {
         logging.logInfo("SystemTray wird nicht unterstützt");
      } else {
         tray = SystemTray.getSystemTray();
         Image image = (new images()).loadImagesFromJarFtpIcon().getImage();
         PopupMenu menu = new PopupMenu();
         icon = new TrayIcon(image, unwtterwarnungText, menu);
         MenuItem messageItem = new MenuItem("Unwetterwarnung anzeigen");
         messageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               Steuerung.setStatus(Status.UNWETTERWARNUNG);
               Steuerung.steuerung();
            }
         });
         MenuItem closeItem = new MenuItem("Unwetterwarnung löschen");
         closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               SystemTrayInfo.tray.remove(SystemTrayInfo.icon);
            }
         });
         MenuItem dwdSeite = new MenuItem("Internetseite - dwd.de");
         dwdSeite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  Desktop.getDesktop().browse(new URI("http://www.dwd.de"));
               } catch (URISyntaxException var3) {
                  logging.logError("Beim öffnen des Browsers ist ein Fehler aufgetreten...");
               }

            }
         });
         menu.add(messageItem);
         menu.add(dwdSeite);
         menu.add(closeItem);
         icon.setImageAutoSize(true);
         tray.add(icon);
      }
   }

   public void removeInfoIcon() {
      tray.remove(icon);
   }
}
