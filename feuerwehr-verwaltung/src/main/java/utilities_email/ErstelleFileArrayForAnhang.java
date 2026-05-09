/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities_email;

import java.io.File;
import logging.logging;

public class ErstelleFileArrayForAnhang {
    static File[] files;
    static int count;

    static {
        count = 0;
    }

    public static File[] analysiereString(String string) {
        int i = 0;
        while (i < string.length()) {
            if (string.substring(i, i + 1).equals(",")) {
                ++count;
            }
            ++i;
        }
        ErstelleFileArrayForAnhang.initArrays();
        ErstelleFileArrayForAnhang.fillFileArray(string);
        return files;
    }

    static void initArrays() {
        files = new File[count];
    }

    static void fillFileArray(String string) {
        int index = 0;
        int i = 0;
        while (i < string.length()) {
            if (string.substring(i, i + 1).equals(",")) {
                logging.logInfo((Object)("F\u00fcge Datei zur E-Mail hinzu: " + string.subSequence(i - index, i)));
                ErstelleFileArrayForAnhang.files[ErstelleFileArrayForAnhang.count - 1] = new File((String)string.subSequence(i - index, i));
                --count;
                index = -1;
            }
            ++index;
            ++i;
        }
    }
}

