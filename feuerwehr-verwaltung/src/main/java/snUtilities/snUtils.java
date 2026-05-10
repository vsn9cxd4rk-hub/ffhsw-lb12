/*
 * Decompiled with CFR 0.152.
 */
package snUtilities;

import java.text.NumberFormat;
import utilities.luhnCheck;

public class snUtils {
    public static boolean checkKey(String pKey) {
        if (!luhnCheck.luhnTest(pKey.substring(0, 6))) {
            return false;
        }
        if (Integer.parseInt(snUtils.createPruefziffen(pKey.substring(0, 6))) == Integer.parseInt(pKey.substring(17, 19))) {
            if (Integer.parseInt(snUtils.createPruefziffen(pKey.substring(13, 19))) == Integer.parseInt(pKey.substring(24, 26))) {
                return Integer.parseInt(pKey.substring(20, 21)) == 0 && Integer.parseInt(snUtils.createPruefziffen2(pKey.substring(0, 19))) == Integer.parseInt(pKey.substring(20, 24));
            }
            return false;
        }
        return false;
    }

    private static String createPruefziffen(String number) {
        int summe = 0;
        int i = 0;
        while (i < number.length()) {
            summe += Integer.parseInt(number.substring(i, i + 1));
            ++i;
        }
        return Integer.toString(summe);
    }

    private static String createPruefziffen2(String number) {
        int summe = 0;
        int i = 0;
        while (i < number.length()) {
            try {
                summe += Integer.parseInt(number.substring(i, i + 1));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            ++i;
        }
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(4);
        nf.setGroupingUsed(false);
        return nf.format(summe);
    }
}

