/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

public class SbcUtils {
    public static String[] listToArray(List<String> liste) {
        String[] result = new String[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static String listToString(List<String> liste) {
        String[] result = new String[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result.toString();
    }

    public static int bildBreiteVerkleinern(ImageIcon img, int sollbreite) {
        double breite;
        double hoehe = img.getIconHeight();
        if (hoehe >= (breite = (double)img.getIconWidth())) {
            int reduzierung = new Double((double)(sollbreite * 100) / breite).intValue();
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
            int reduzierung = new Double((double)(sollbreite * 100) / hoehe).intValue();
            int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
            return reduziertehoehe;
        }
        int reduzierung = new Double((double)(sollbreite * 100) / hoehe).intValue();
        int reduziertehoehe = new Double(hoehe / 100.0 * (double)reduzierung).intValue();
        return reduziertehoehe;
    }

    public static String timeStamp(String format) {
        Date systemzeit = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(systemzeit);
    }
}

