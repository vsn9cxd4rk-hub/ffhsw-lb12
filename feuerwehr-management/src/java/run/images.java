package run;

import java.net.URL;
import javax.swing.ImageIcon;
import logging.logging;
import run.runApplication;

public class images {

   public void loadImagesFromJAR() {
      logging.logInfo("Lade Bilder aus JAR Datei...");
      URL dummy = this.getClass().getClassLoader().getResource("images/dummy.jpg");
      logging.logInfo("URL1 --> " + dummy);
      ImageIcon dummyBild = new ImageIcon(dummy);
      runApplication.dummyImage = dummyBild;
      URL progarmmIcon = this.getClass().getClassLoader().getResource("images/icon.png");
      logging.logInfo("URL2 --> " + progarmmIcon);
      ImageIcon bildIcon = new ImageIcon(progarmmIcon);
      runApplication.icon = bildIcon;
      URL banner;
      ImageIcon bannerBild;
      if(runApplication.bildschirmgröße.getWidth() == 1366.0D && runApplication.bildschirmgröße.getHeight() == 768.0D) {
         banner = this.getClass().getClassLoader().getResource("images/banner_1366x768_2.jpg");
         logging.logInfo("URL3 --> " + banner);
         bannerBild = new ImageIcon(banner);
         runApplication.bannerHauptprogramm = bannerBild;
      } else if(runApplication.bildschirmgröße.getWidth() == 1280.0D && runApplication.bildschirmgröße.getHeight() == 800.0D) {
         banner = this.getClass().getClassLoader().getResource("images/banner_1366x768_2.jpg");
         logging.logInfo("URL3 --> " + banner);
         bannerBild = new ImageIcon(banner);
         runApplication.bannerHauptprogramm = bannerBild;
      } else {
         banner = this.getClass().getClassLoader().getResource("images/banner.jpg");
         logging.logInfo("URL3 --> " + banner);
         bannerBild = new ImageIcon(banner);
         runApplication.bannerHauptprogramm = bannerBild;
      }

      logging.logInfo("Bilder aus JAR Datei erfolgreich geladen...");
   }

   public void loadImagesDummyBlack() {
      URL dummy = this.getClass().getClassLoader().getResource("images/dummy_sw.jpg");
      logging.logInfo("URL1_sw --> " + dummy);
      ImageIcon dummyBild = new ImageIcon(dummy);
      runApplication.dummyImage = dummyBild;
   }

   public ImageIcon loadImagesFromJARStartbildschirmIcon() {
      URL progarmmIcon = this.getClass().getClassLoader().getResource("images/icon.png");
      System.out.println("image().loadImagesFromJARStartbildschirmIcon() --> " + progarmmIcon);
      ImageIcon bildIcon = new ImageIcon(progarmmIcon);
      return bildIcon;
   }

   public ImageIcon loadImagesFromJARFacebookIcon() {
      URL progarmmIcon = this.getClass().getClassLoader().getResource("images/facebook.png");
      System.out.println("image().loadImagesFromJARFacebookIcon() --> " + progarmmIcon);
      ImageIcon bildIcon = new ImageIcon(progarmmIcon);
      return bildIcon;
   }

   public ImageIcon loadImagesFromJARStartbildschirmIconCloud() {
      URL progarmmIcon = this.getClass().getClassLoader().getResource("images/icon_cloud2.png");
      System.out.println("image().loadImagesFromJARStartbildschirmIconCloud() --> " + progarmmIcon);
      ImageIcon bildIcon = new ImageIcon(progarmmIcon);
      return bildIcon;
   }

   public ImageIcon loadImagesFromJARStartbildschirm() {
      URL banner = this.getClass().getClassLoader().getResource("images/banner.jpg");
      System.out.println("image().loadImagesFromJARStartbildschirm() --> " + banner);
      ImageIcon bannerBild = new ImageIcon(banner);
      return bannerBild;
   }

   public ImageIcon loadImagesFromJarFtpIcon() {
      URL banner = this.getClass().getClassLoader().getResource("images/icon_cloud.png");
      logging.logInfo("URL3 --> " + banner);
      ImageIcon bannerBild = new ImageIcon(banner);
      return bannerBild;
   }

   public ImageIcon statusRot() {
      URL statusRot = this.getClass().getClassLoader().getResource("images/statusRot.jpg");
      logging.logInfo("statusRot --> " + statusRot);
      ImageIcon statusRotBild = new ImageIcon(statusRot);
      return statusRotBild;
   }

   public ImageIcon statusGrün() {
      URL statusGrün = this.getClass().getClassLoader().getResource("images/statusGrün.jpg");
      logging.logInfo("statusGrün --> " + statusGrün);
      ImageIcon statusGrünBild = new ImageIcon(statusGrün);
      return statusGrünBild;
   }

   public ImageIcon statusGelb() {
      URL statusGelb = this.getClass().getClassLoader().getResource("images/statusGelb.jpg");
      logging.logInfo("statusGelb --> " + statusGelb);
      ImageIcon statusGelbBild = new ImageIcon(statusGelb);
      return statusGelbBild;
   }

   public ImageIcon iconBrandEinsatz() {
      URL iconBrandEinsatz = this.getClass().getClassLoader().getResource("images/Bild_Feuer.jpg");
      logging.logInfo("BildFeuer --> " + iconBrandEinsatz);
      ImageIcon iconBrandEinsatzBild = new ImageIcon(iconBrandEinsatz);
      return iconBrandEinsatzBild;
   }

   public ImageIcon iconTHEinsatz() {
      URL iconTHEinsatz = this.getClass().getClassLoader().getResource("images/Bild_TH2.jpg");
      logging.logInfo("BildTH --> " + iconTHEinsatz);
      ImageIcon iconTHEinsatzBild = new ImageIcon(iconTHEinsatz);
      return iconTHEinsatzBild;
   }

   public ImageIcon iconWBEinsatz() {
      URL iconWBEinsatz = this.getClass().getClassLoader().getResource("images/Bild_Wachbesetzung.jpg");
      logging.logInfo("BildWachbesetzung --> " + iconWBEinsatz);
      ImageIcon iconWBEinsatzBild = new ImageIcon(iconWBEinsatz);
      return iconWBEinsatzBild;
   }

   public ImageIcon iconDienst() {
      URL iconDienst = this.getClass().getClassLoader().getResource("images/Bild_Dienstabend.jpg");
      logging.logInfo("BildDienst --> " + iconDienst);
      ImageIcon iconDienstBild = new ImageIcon(iconDienst);
      return iconDienstBild;
   }

   public ImageIcon iconBSW() {
      URL iconBSW = this.getClass().getClassLoader().getResource("images/Bild_BSW2.jpg");
      logging.logInfo("BildBSW --> " + iconBSW);
      ImageIcon iconBSWBild = new ImageIcon(iconBSW);
      return iconBSWBild;
   }

   public ImageIcon iconSonstiges() {
      URL iconSonstiges = this.getClass().getClassLoader().getResource("images/Bild_SonstigeTermine.jpg");
      logging.logInfo("BildSonstigesTermine --> " + iconSonstiges);
      ImageIcon iconSonstigesBild = new ImageIcon(iconSonstiges);
      return iconSonstigesBild;
   }

   public ImageIcon iconUhr() {
      URL iconUhr = this.getClass().getClassLoader().getResource("images/Bild_Uhr.jpg");
      logging.logInfo("BildSonstigesTermine --> " + iconUhr);
      ImageIcon iconUhrBild = new ImageIcon(iconUhr);
      return iconUhrBild;
   }

   public ImageIcon iconKalender() {
      URL iconKalender = this.getClass().getClassLoader().getResource("images/Bild_Kalender.jpg");
      logging.logInfo("BildKalender --> " + iconKalender);
      ImageIcon iconKalenderBild = new ImageIcon(iconKalender);
      return iconKalenderBild;
   }
}
