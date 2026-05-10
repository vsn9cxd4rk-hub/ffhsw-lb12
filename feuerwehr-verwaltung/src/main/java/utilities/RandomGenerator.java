/*
 * Decompiled with CFR 0.152.
 */
package utilities;

public class RandomGenerator {
    public static String generate(int length, Mode mode) {
        try {
            StringBuffer buffer = new StringBuffer();
            String characters = "";
            switch (mode) {
                case ALPHA_SMALL_AND_BIG_SIGNS: {
                    characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    break;
                }
                case ALPHA_SMALL_SIGNS: {
                    characters = "abcdefghijklmnopqrstuvwxyz";
                    break;
                }
                case ALPHA_BIG_SIGNS: {
                    characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    break;
                }
                case ALPHANUMERIC: {
                    characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    break;
                }
                case ALPHANUMERIC_ONLY_BIG_SIGNS: {
                    characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    break;
                }
                case ALPHANUMERIC_ONLY_SMALL_SIGNS: {
                    characters = "abcdefghijklmnopqrstuvwxyz1234567890";
                    break;
                }
                case NUMERIC: {
                    characters = "1234567890";
                }
            }
            int charactersLength = characters.length();
            int i = 0;
            while (i < length) {
                double index = Math.random() * (double)charactersLength;
                buffer.append(characters.charAt((int)index));
                ++i;
            }
            return buffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static enum Mode {
        ALPHA_SMALL_AND_BIG_SIGNS,
        ALPHA_SMALL_SIGNS,
        ALPHA_BIG_SIGNS,
        ALPHANUMERIC,
        ALPHANUMERIC_ONLY_BIG_SIGNS,
        ALPHANUMERIC_ONLY_SMALL_SIGNS,
        NUMERIC;

    }
}

