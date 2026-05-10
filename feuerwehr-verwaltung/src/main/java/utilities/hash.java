/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  utilities.RandomGenerator
 *  utilities.RandomGenerator$Mode
 */
package utilities;

import java.text.NumberFormat;
import javax.swing.JOptionPane;
import utilities.RandomGenerator;

public class hash {
    public static String createHashCode(String toHash) {
        StringBuilder Hash = new StringBuilder();
        if (toHash.length() > 99999) {
            JOptionPane.showMessageDialog(null, "Der Hash Wert Kann nicht erzeugt werden", "Fehlermeldung", 0);
        } else {
            NumberFormat nf = NumberFormat.getIntegerInstance();
            nf.setMinimumIntegerDigits(5);
            nf.setGroupingUsed(false);
            Hash.append(nf.format(toHash.length()));
            Hash.append(RandomGenerator.generate((int)14, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHANUMERIC));
            int i = 0;
            while (i < toHash.length()) {
                if (toHash.substring(i, i + 1).equals("a")) {
                    Hash.append("x");
                } else if (toHash.substring(i, i + 1).equals("b")) {
                    Hash.append("y");
                } else if (toHash.substring(i, i + 1).equals("c")) {
                    Hash.append("\u00fc");
                } else if (toHash.substring(i, i + 1).equals("d")) {
                    Hash.append("r");
                } else if (toHash.substring(i, i + 1).equals("e")) {
                    Hash.append("v");
                } else if (toHash.substring(i, i + 1).equals("f")) {
                    Hash.append("a");
                } else if (toHash.substring(i, i + 1).equals("g")) {
                    Hash.append("p");
                } else if (toHash.substring(i, i + 1).equals("h")) {
                    Hash.append("b");
                } else if (toHash.substring(i, i + 1).equals("i")) {
                    Hash.append("q");
                } else if (toHash.substring(i, i + 1).equals("j")) {
                    Hash.append("t");
                } else if (toHash.substring(i, i + 1).equals("k")) {
                    Hash.append("\u00f6");
                } else if (toHash.substring(i, i + 1).equals("l")) {
                    Hash.append("k");
                } else if (toHash.substring(i, i + 1).equals("m")) {
                    Hash.append("e");
                } else if (toHash.substring(i, i + 1).equals("n")) {
                    Hash.append("u");
                } else if (toHash.substring(i, i + 1).equals("o")) {
                    Hash.append("\u00e4");
                } else if (toHash.substring(i, i + 1).equals("p")) {
                    Hash.append("f");
                } else if (toHash.substring(i, i + 1).equals("q")) {
                    Hash.append("i");
                } else if (toHash.substring(i, i + 1).equals("r")) {
                    Hash.append("g");
                } else if (toHash.substring(i, i + 1).equals("s")) {
                    Hash.append("c");
                } else if (toHash.substring(i, i + 1).equals("t")) {
                    Hash.append("j");
                } else if (toHash.substring(i, i + 1).equals("u")) {
                    Hash.append("n");
                } else if (toHash.substring(i, i + 1).equals("v")) {
                    Hash.append("d");
                } else if (toHash.substring(i, i + 1).equals("w")) {
                    Hash.append("h");
                } else if (toHash.substring(i, i + 1).equals("x")) {
                    Hash.append("m");
                } else if (toHash.substring(i, i + 1).equals("y")) {
                    Hash.append("s");
                } else if (toHash.substring(i, i + 1).equals("z")) {
                    Hash.append("o");
                } else if (toHash.substring(i, i + 1).equals("\u00e4")) {
                    Hash.append("w");
                } else if (toHash.substring(i, i + 1).equals("\u00f6")) {
                    Hash.append("z");
                } else if (toHash.substring(i, i + 1).equals("\u00fc")) {
                    Hash.append("l");
                } else if (toHash.substring(i, i + 1).equals("A")) {
                    Hash.append("X");
                } else if (toHash.substring(i, i + 1).equals("B")) {
                    Hash.append("Y");
                } else if (toHash.substring(i, i + 1).equals("C")) {
                    Hash.append("\u00dc");
                } else if (toHash.substring(i, i + 1).equals("D")) {
                    Hash.append("R");
                } else if (toHash.substring(i, i + 1).equals("E")) {
                    Hash.append("V");
                } else if (toHash.substring(i, i + 1).equals("F")) {
                    Hash.append("A");
                } else if (toHash.substring(i, i + 1).equals("G")) {
                    Hash.append("P");
                } else if (toHash.substring(i, i + 1).equals("H")) {
                    Hash.append("B");
                } else if (toHash.substring(i, i + 1).equals("I")) {
                    Hash.append("Q");
                } else if (toHash.substring(i, i + 1).equals("J")) {
                    Hash.append("T");
                } else if (toHash.substring(i, i + 1).equals("K")) {
                    Hash.append("\u00d6");
                } else if (toHash.substring(i, i + 1).equals("L")) {
                    Hash.append("K");
                } else if (toHash.substring(i, i + 1).equals("M")) {
                    Hash.append("E");
                } else if (toHash.substring(i, i + 1).equals("N")) {
                    Hash.append("U");
                } else if (toHash.substring(i, i + 1).equals("O")) {
                    Hash.append("\u00c4");
                } else if (toHash.substring(i, i + 1).equals("P")) {
                    Hash.append("F");
                } else if (toHash.substring(i, i + 1).equals("Q")) {
                    Hash.append("I");
                } else if (toHash.substring(i, i + 1).equals("R")) {
                    Hash.append("G");
                } else if (toHash.substring(i, i + 1).equals("S")) {
                    Hash.append("C");
                } else if (toHash.substring(i, i + 1).equals("T")) {
                    Hash.append("J");
                } else if (toHash.substring(i, i + 1).equals("U")) {
                    Hash.append("N");
                } else if (toHash.substring(i, i + 1).equals("V")) {
                    Hash.append("D");
                } else if (toHash.substring(i, i + 1).equals("W")) {
                    Hash.append("H");
                } else if (toHash.substring(i, i + 1).equals("X")) {
                    Hash.append("M");
                } else if (toHash.substring(i, i + 1).equals("Y")) {
                    Hash.append("S");
                } else if (toHash.substring(i, i + 1).equals("Z")) {
                    Hash.append("O");
                } else if (toHash.substring(i, i + 1).equals("\u00c4")) {
                    Hash.append("W");
                } else if (toHash.substring(i, i + 1).equals("\u00d6")) {
                    Hash.append("Z");
                } else if (toHash.substring(i, i + 1).equals("\u00dc")) {
                    Hash.append("L");
                } else if (toHash.substring(i, i + 1).equals("1")) {
                    Hash.append("8");
                } else if (toHash.substring(i, i + 1).equals("2")) {
                    Hash.append("9");
                } else if (toHash.substring(i, i + 1).equals("3")) {
                    Hash.append("0");
                } else if (toHash.substring(i, i + 1).equals("4")) {
                    Hash.append("6");
                } else if (toHash.substring(i, i + 1).equals("5")) {
                    Hash.append("1");
                } else if (toHash.substring(i, i + 1).equals("6")) {
                    Hash.append("5");
                } else if (toHash.substring(i, i + 1).equals("7")) {
                    Hash.append("4");
                } else if (toHash.substring(i, i + 1).equals("8")) {
                    Hash.append("2");
                } else if (toHash.substring(i, i + 1).equals("9")) {
                    Hash.append("3");
                } else if (toHash.substring(i, i + 1).equals("0")) {
                    Hash.append("7");
                } else {
                    Hash.append(toHash.substring(i, i + 1));
                }
                ++i;
            }
            Hash.append(RandomGenerator.generate((int)20, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHANUMERIC));
        }
        return Hash.toString();
    }

    public static String decodeHashCode(String toDecodeHash) throws NumberFormatException {
        StringBuilder Hash = new StringBuilder();
        int laenge = Integer.parseInt((String)toDecodeHash.subSequence(1, 5));
        int i = 19;
        while (i < 19 + laenge) {
            if (toDecodeHash.substring(i, i + 1).equals("x")) {
                Hash.append("a");
            } else if (toDecodeHash.substring(i, i + 1).equals("y")) {
                Hash.append("b");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00fc")) {
                Hash.append("c");
            } else if (toDecodeHash.substring(i, i + 1).equals("r")) {
                Hash.append("d");
            } else if (toDecodeHash.substring(i, i + 1).equals("v")) {
                Hash.append("e");
            } else if (toDecodeHash.substring(i, i + 1).equals("a")) {
                Hash.append("f");
            } else if (toDecodeHash.substring(i, i + 1).equals("p")) {
                Hash.append("g");
            } else if (toDecodeHash.substring(i, i + 1).equals("b")) {
                Hash.append("h");
            } else if (toDecodeHash.substring(i, i + 1).equals("q")) {
                Hash.append("i");
            } else if (toDecodeHash.substring(i, i + 1).equals("t")) {
                Hash.append("j");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00f6")) {
                Hash.append("k");
            } else if (toDecodeHash.substring(i, i + 1).equals("k")) {
                Hash.append("l");
            } else if (toDecodeHash.substring(i, i + 1).equals("e")) {
                Hash.append("m");
            } else if (toDecodeHash.substring(i, i + 1).equals("u")) {
                Hash.append("n");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00e4")) {
                Hash.append("o");
            } else if (toDecodeHash.substring(i, i + 1).equals("f")) {
                Hash.append("p");
            } else if (toDecodeHash.substring(i, i + 1).equals("i")) {
                Hash.append("q");
            } else if (toDecodeHash.substring(i, i + 1).equals("g")) {
                Hash.append("r");
            } else if (toDecodeHash.substring(i, i + 1).equals("c")) {
                Hash.append("s");
            } else if (toDecodeHash.substring(i, i + 1).equals("j")) {
                Hash.append("t");
            } else if (toDecodeHash.substring(i, i + 1).equals("n")) {
                Hash.append("u");
            } else if (toDecodeHash.substring(i, i + 1).equals("d")) {
                Hash.append("v");
            } else if (toDecodeHash.substring(i, i + 1).equals("h")) {
                Hash.append("w");
            } else if (toDecodeHash.substring(i, i + 1).equals("m")) {
                Hash.append("x");
            } else if (toDecodeHash.substring(i, i + 1).equals("s")) {
                Hash.append("y");
            } else if (toDecodeHash.substring(i, i + 1).equals("o")) {
                Hash.append("z");
            } else if (toDecodeHash.substring(i, i + 1).equals("w")) {
                Hash.append("\u00e4");
            } else if (toDecodeHash.substring(i, i + 1).equals("z")) {
                Hash.append("\u00f6");
            } else if (toDecodeHash.substring(i, i + 1).equals("l")) {
                Hash.append("\u00fc");
            } else if (toDecodeHash.substring(i, i + 1).equals("8")) {
                Hash.append("1");
            } else if (toDecodeHash.substring(i, i + 1).equals("X")) {
                Hash.append("A");
            } else if (toDecodeHash.substring(i, i + 1).equals("Y")) {
                Hash.append("B");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00dc")) {
                Hash.append("C");
            } else if (toDecodeHash.substring(i, i + 1).equals("R")) {
                Hash.append("D");
            } else if (toDecodeHash.substring(i, i + 1).equals("V")) {
                Hash.append("E");
            } else if (toDecodeHash.substring(i, i + 1).equals("A")) {
                Hash.append("F");
            } else if (toDecodeHash.substring(i, i + 1).equals("P")) {
                Hash.append("G");
            } else if (toDecodeHash.substring(i, i + 1).equals("B")) {
                Hash.append("H");
            } else if (toDecodeHash.substring(i, i + 1).equals("Q")) {
                Hash.append("I");
            } else if (toDecodeHash.substring(i, i + 1).equals("T")) {
                Hash.append("J");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00d6")) {
                Hash.append("K");
            } else if (toDecodeHash.substring(i, i + 1).equals("K")) {
                Hash.append("L");
            } else if (toDecodeHash.substring(i, i + 1).equals("E")) {
                Hash.append("M");
            } else if (toDecodeHash.substring(i, i + 1).equals("U")) {
                Hash.append("N");
            } else if (toDecodeHash.substring(i, i + 1).equals("\u00c4")) {
                Hash.append("O");
            } else if (toDecodeHash.substring(i, i + 1).equals("F")) {
                Hash.append("P");
            } else if (toDecodeHash.substring(i, i + 1).equals("I")) {
                Hash.append("Q");
            } else if (toDecodeHash.substring(i, i + 1).equals("G")) {
                Hash.append("R");
            } else if (toDecodeHash.substring(i, i + 1).equals("C")) {
                Hash.append("S");
            } else if (toDecodeHash.substring(i, i + 1).equals("J")) {
                Hash.append("T");
            } else if (toDecodeHash.substring(i, i + 1).equals("N")) {
                Hash.append("U");
            } else if (toDecodeHash.substring(i, i + 1).equals("D")) {
                Hash.append("V");
            } else if (toDecodeHash.substring(i, i + 1).equals("H")) {
                Hash.append("W");
            } else if (toDecodeHash.substring(i, i + 1).equals("M")) {
                Hash.append("X");
            } else if (toDecodeHash.substring(i, i + 1).equals("S")) {
                Hash.append("Y");
            } else if (toDecodeHash.substring(i, i + 1).equals("O")) {
                Hash.append("Z");
            } else if (toDecodeHash.substring(i, i + 1).equals("W")) {
                Hash.append("\u00c4");
            } else if (toDecodeHash.substring(i, i + 1).equals("Z")) {
                Hash.append("\u00d6");
            } else if (toDecodeHash.substring(i, i + 1).equals("L")) {
                Hash.append("\u00dc");
            } else if (toDecodeHash.substring(i, i + 1).equals("8")) {
                Hash.append("1");
            } else if (toDecodeHash.substring(i, i + 1).equals("9")) {
                Hash.append("2");
            } else if (toDecodeHash.substring(i, i + 1).equals("0")) {
                Hash.append("3");
            } else if (toDecodeHash.substring(i, i + 1).equals("6")) {
                Hash.append("4");
            } else if (toDecodeHash.substring(i, i + 1).equals("1")) {
                Hash.append("5");
            } else if (toDecodeHash.substring(i, i + 1).equals("5")) {
                Hash.append("6");
            } else if (toDecodeHash.substring(i, i + 1).equals("4")) {
                Hash.append("7");
            } else if (toDecodeHash.substring(i, i + 1).equals("2")) {
                Hash.append("8");
            } else if (toDecodeHash.substring(i, i + 1).equals("3")) {
                Hash.append("9");
            } else if (toDecodeHash.substring(i, i + 1).equals("7")) {
                Hash.append("0");
            } else {
                Hash.append(toDecodeHash.substring(i, i + 1));
            }
            ++i;
        }
        return Hash.toString();
    }
}

