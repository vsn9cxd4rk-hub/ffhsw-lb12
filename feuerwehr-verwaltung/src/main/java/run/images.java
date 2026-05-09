/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package run;

import java.net.URL;
import javax.swing.ImageIcon;
import logging.logging;
import run.runApplication;

public class images {
    public void loadImagesFromJAR() {
        ImageIcon bildIcon;
        ImageIcon dummyBild;
        logging.logInfo((Object)"Lade Bilder aus JAR Datei...");
        URL dummy = this.getClass().getClassLoader().getResource("images/dummy.jpg");
        logging.logInfo((Object)("URL1 --> " + dummy));
        runApplication.dummyImage = dummyBild = new ImageIcon(dummy);
        URL progarmmIcon = this.getClass().getClassLoader().getResource("images/icon.png");
        logging.logInfo((Object)("URL2 --> " + progarmmIcon));
        runApplication.icon = bildIcon = new ImageIcon(progarmmIcon);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            ImageIcon bannerBild;
            URL banner = this.getClass().getClassLoader().getResource("images/banner_1366x768_2.jpg");
            logging.logInfo((Object)("URL3 --> " + banner));
            runApplication.bannerHauptprogramm = bannerBild = new ImageIcon(banner);
        } else {
            ImageIcon bannerBild;
            URL banner = this.getClass().getClassLoader().getResource("images/banner.jpg");
            logging.logInfo((Object)("URL3 --> " + banner));
            runApplication.bannerHauptprogramm = bannerBild = new ImageIcon(banner);
        }
        logging.logInfo((Object)"Bilder aus JAR Datei erfolgreich geladen...");
    }

    public void loadImagesDummyBlack() {
        ImageIcon dummyBild;
        URL dummy = this.getClass().getClassLoader().getResource("images/dummy_sw.jpg");
        logging.logInfo((Object)("URL1_sw --> " + dummy));
        runApplication.dummyImage = dummyBild = new ImageIcon(dummy);
    }

    public ImageIcon loadImagesFromJARStartbildschirmIcon() {
        URL progarmmIcon = this.getClass().getClassLoader().getResource("images/icon.png");
        System.out.println("image().loadImagesFromJARStartbildschirmIcon() --> " + progarmmIcon);
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
        logging.logInfo((Object)("URL3 --> " + banner));
        ImageIcon bannerBild = new ImageIcon(banner);
        return bannerBild;
    }

    public ImageIcon statusRot() {
        URL statusRot = this.getClass().getClassLoader().getResource("images/statusRot.jpg");
        logging.logInfo((Object)("statusRot --> " + statusRot));
        ImageIcon statusRotBild = new ImageIcon(statusRot);
        return statusRotBild;
    }

    public ImageIcon statusGr\u00fcn() {
        URL statusGr\u00fcn = this.getClass().getClassLoader().getResource("images/statusGr\u00fcn.jpg");
        logging.logInfo((Object)("statusGr\u00fcn --> " + statusGr\u00fcn));
        ImageIcon statusGr\u00fcnBild = new ImageIcon(statusGr\u00fcn);
        return statusGr\u00fcnBild;
    }

    public ImageIcon statusGelb() {
        URL statusGelb = this.getClass().getClassLoader().getResource("images/statusGelb.jpg");
        logging.logInfo((Object)("statusGelb --> " + statusGelb));
        ImageIcon statusGelbBild = new ImageIcon(statusGelb);
        return statusGelbBild;
    }

    public ImageIcon iconBrandEinsatz() {
        URL iconBrandEinsatz = this.getClass().getClassLoader().getResource("images/Bild_Feuer.jpg");
        logging.logInfo((Object)("BildFeuer --> " + iconBrandEinsatz));
        ImageIcon iconBrandEinsatzBild = new ImageIcon(iconBrandEinsatz);
        return iconBrandEinsatzBild;
    }

    public ImageIcon iconTHEinsatz() {
        URL iconTHEinsatz = this.getClass().getClassLoader().getResource("images/Bild_TH2.jpg");
        logging.logInfo((Object)("BildTH --> " + iconTHEinsatz));
        ImageIcon iconTHEinsatzBild = new ImageIcon(iconTHEinsatz);
        return iconTHEinsatzBild;
    }

    public ImageIcon iconWBEinsatz() {
        URL iconWBEinsatz = this.getClass().getClassLoader().getResource("images/Bild_Wachbesetzung.jpg");
        logging.logInfo((Object)("BildWachbesetzung --> " + iconWBEinsatz));
        ImageIcon iconWBEinsatzBild = new ImageIcon(iconWBEinsatz);
        return iconWBEinsatzBild;
    }

    public ImageIcon iconDienst() {
        URL iconDienst = this.getClass().getClassLoader().getResource("images/Bild_Dienstabend.jpg");
        logging.logInfo((Object)("BildDienst --> " + iconDienst));
        ImageIcon iconDienstBild = new ImageIcon(iconDienst);
        return iconDienstBild;
    }

    public ImageIcon iconBSW() {
        URL iconBSW = this.getClass().getClassLoader().getResource("images/Bild_BSW2.jpg");
        logging.logInfo((Object)("BildBSW --> " + iconBSW));
        ImageIcon iconBSWBild = new ImageIcon(iconBSW);
        return iconBSWBild;
    }

    public ImageIcon iconSonstiges() {
        URL iconSonstiges = this.getClass().getClassLoader().getResource("images/Bild_SonstigeTermine.jpg");
        logging.logInfo((Object)("BildSonstigesTermine --> " + iconSonstiges));
        ImageIcon iconSonstigesBild = new ImageIcon(iconSonstiges);
        return iconSonstigesBild;
    }

    public ImageIcon iconUhr() {
        URL iconUhr = this.getClass().getClassLoader().getResource("images/Bild_Uhr.jpg");
        logging.logInfo((Object)("BildSonstigesTermine --> " + iconUhr));
        ImageIcon iconUhrBild = new ImageIcon(iconUhr);
        return iconUhrBild;
    }

    public ImageIcon iconKalender() {
        URL iconKalender = this.getClass().getClassLoader().getResource("images/Bild_Kalender.jpg");
        logging.logInfo((Object)("BildKalender --> " + iconKalender));
        ImageIcon iconKalenderBild = new ImageIcon(iconKalender);
        return iconKalenderBild;
    }
}

