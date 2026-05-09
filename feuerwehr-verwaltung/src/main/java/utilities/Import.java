/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities;

import data.tabellen.karte.TabelleStrassen;
import go.Stra\u00dfe;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import logging.logging;

public class Import {
    public static void ImportData(String infile, String stadt) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(infile));
            String zeile = null;
            int dbcounter = 1;
            int durationcounter = 0;
            int durationcounter2 = 10;
            while ((zeile = in.readLine()) != null) {
                if (durationcounter == durationcounter2) {
                    Runtime.getRuntime().exec("cmd /c taskkill /f /im firefox.exe");
                    logging.logInfo((Object)"Firefox beenden: taskill /f /im firefox.exe");
                    Thread.sleep(2000L);
                    durationcounter2 += 10;
                }
                TabelleStrassen tabelleStrassen = new TabelleStrassen();
                Stra\u00dfe stra\u00dfe = new Stra\u00dfe();
                stra\u00dfe.setId(tabelleStrassen.getNextNummer());
                stra\u00dfe.setName(zeile);
                stra\u00dfe.setBild(String.valueOf(zeile) + ".jpg");
                stra\u00dfe.setAnfahrt("");
                stra\u00dfe.setInfo("");
                stra\u00dfe.setKoordinaten("");
                stra\u00dfe.setPLZ(stadt);
                tabelleStrassen.insert(stra\u00dfe);
                Desktop.getDesktop().browse(new URI("http://maps.google.de/maps?q=" + Import.getStringBuilder(zeile) + ",+" + stadt + "&hl=de&ie=UTF8"));
                Thread.sleep(5000L);
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write((RenderedImage)image, "jpg", new File("c:/windows/temp/screenshot.jpg"));
                BufferedImage img = ImageIO.read(new File("c:/windows/temp/screenshot.jpg"));
                BufferedImage partImg = img.getSubimage(380, 150, 980, 590);
                ImageIO.write((RenderedImage)partImg, "jpeg", new File("images/street/gro\u00df/" + zeile + ".jpg"));
                Thread.sleep(3000L);
                Robot rob = new Robot();
                rob.mouseMove(426, 317);
                rob.delay(1000);
                rob.mousePress(16);
                rob.mousePress(1024);
                rob.mouseRelease(16);
                Thread.sleep(3000L);
                BufferedImage imageKL = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write((RenderedImage)imageKL, "jpg", new File("c:/windows/temp/screenshot.jpg"));
                BufferedImage imgKL = ImageIO.read(new File("c:/windows/temp/screenshot.jpg"));
                BufferedImage partImgKL = imgKL.getSubimage(380, 150, 980, 590);
                ImageIO.write((RenderedImage)partImgKL, "jpeg", new File("images/street/klein/" + zeile + ".jpg"));
                ++dbcounter;
                ++durationcounter;
            }
            in.close();
            Runtime.getRuntime().exec("cmd /c taskkill /f /im firefox.exe");
            logging.logInfo((Object)"Firefox beenden: taskill /f /im firefox.exe");
            Thread.sleep(2000L);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (HeadlessException e) {
            e.printStackTrace();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getStringBuilder(String in) {
        StringBuilder out = new StringBuilder();
        String[] array = in.split(" ");
        int i = 0;
        while (i < array.length) {
            out.append(array[i]);
            out.append("+");
            ++i;
        }
        return out.toString();
    }
}

