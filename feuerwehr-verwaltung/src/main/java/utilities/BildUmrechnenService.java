/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.Image
 */
package utilities;

import com.itextpdf.text.Image;
import javax.swing.ImageIcon;

public class BildUmrechnenService {
    public static int bildBreiteVerkleinernPDF(Image img, int sollbreite) {
        double breite;
        double hoehe = img.getHeight();
        if (hoehe >= (breite = (double)img.getWidth())) {
            int reduzierung = new Double(45000.0 / hoehe).intValue();
            int reduziertebreite = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
            return reduziertebreite;
        }
        int reduzierung = new Double((double)(sollbreite * 100) / breite).intValue();
        int reduziertebreite = new Double(breite / 100.0 * (double)reduzierung).intValue();
        return reduziertebreite;
    }

    public static int bildHoeheVerkleinernPDF(Image img, int sollbreite) {
        double breite;
        double hoehe = img.getHeight();
        if (hoehe >= (breite = (double)img.getWidth())) {
            int reduzierung = new Double(45000.0 / hoehe).intValue();
            int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
            return reduziertehoehe;
        }
        int reduzierung = new Double((double)(sollbreite * 100) / breite).intValue();
        int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
        return reduziertehoehe;
    }

    public static int bildBreiteVerkleinern(ImageIcon img, int sollbreite) {
        double breite;
        double hoehe = img.getIconHeight();
        if (hoehe >= (breite = (double)img.getIconWidth())) {
            int reduzierung = new Double(60000.0 / hoehe).intValue();
            int reduziertebreite = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
            return reduziertebreite;
        }
        int reduzierung = new Double((double)(sollbreite * 100) / breite).intValue();
        int reduziertebreite = new Double(breite / 100.0 * (double)reduzierung).intValue();
        return reduziertebreite;
    }

    public static int bildHoeheVerkleinern(ImageIcon img, int sollbreite) {
        double breite;
        double hoehe = img.getIconHeight();
        if (hoehe >= (breite = (double)img.getIconWidth())) {
            int reduzierung = new Double(60000.0 / hoehe).intValue();
            int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
            return reduziertehoehe;
        }
        int reduzierung = new Double((double)(sollbreite * 100) / breite).intValue();
        int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
        return reduziertehoehe;
    }
}

