/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.math.BigInteger;

public class luhnCheck {
    public static boolean luhnTest(String s) {
        int len = s.length();
        int[] ints = new int[len];
        int i = 0;
        while (i < len) {
            try {
                ints[i] = Integer.parseInt(s.substring(i, i + 1));
            }
            catch (NumberFormatException e) {
                System.err.println(e);
                return false;
            }
            ++i;
        }
        int sum = 0;
        while (len > 0) {
            sum += ints[len - 1];
            if (--len <= 0) continue;
            int digit = 2 * ints[len - 1];
            sum += digit > 9 ? digit - 9 : digit;
            --len;
        }
        return sum % 10 == 0;
    }

    public static String convertKnrBlzToIBAN(String knr, String blz) {
        BigInteger checkIBANSum;
        if (knr.length() < 10) {
            int anz = 10 - knr.length();
            int i = 0;
            while (i < anz) {
                knr = "0" + knr;
                ++i;
            }
        }
        String checkIBAN = String.valueOf(blz) + knr + "131400";
        try {
            checkIBANSum = new BigInteger(checkIBAN);
        }
        catch (Exception e) {
            return "Fehler";
        }
        BigInteger faktor = new BigInteger("97");
        long div = checkIBANSum.remainder(faktor).longValue();
        long pZiffer = 98L - div;
        String IBAN = "";
        IBAN = pZiffer < 10L ? "DE0" + pZiffer + blz + knr : "DE" + pZiffer + blz + knr;
        return IBAN;
    }
}

