/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import logging.logging;

public class XML {
    public static int count = 0;

    public static void createEinsatzBericht(String[] variableInFile, String[] toNewValue, String dateiname, String templateFile) {
        try {
            FileWriter outputfile = new FileWriter(dateiname);
            BufferedReader inFile = new BufferedReader(new FileReader(templateFile));
            String zeile = inFile.readLine();
            StringBuilder build = new StringBuilder();
            logging.logInfo((Object)"Starte Manipulation des XML-Templates");
            while (zeile != null) {
                build.setLength(0);
                int i = 0;
                while (i < toNewValue.length) {
                    if (zeile.contains(variableInFile[i])) {
                        logging.logInfo((Object)("Gefunden: " + variableInFile[i] + " Ersetze: " + toNewValue[i]));
                        int itemIndex = zeile.indexOf(variableInFile[i]);
                        build.append(zeile.substring(0, itemIndex));
                        build.append(toNewValue[i]);
                        if (variableInFile[i].equals("01.01.2000")) {
                            build.append(zeile.substring(itemIndex + 10, zeile.length()));
                        } else {
                            build.append(zeile.substring(itemIndex + 5, zeile.length()));
                        }
                    }
                    ++i;
                }
                if (build.length() == 0) {
                    outputfile.write(String.valueOf(zeile) + "\r\n");
                } else {
                    outputfile.write(String.valueOf(build.toString()) + "\r\n");
                }
                zeile = inFile.readLine();
            }
            outputfile.close();
            inFile.close();
            logging.logInfo((Object)"Einsatzbericht wurd erfolgreich erzeugt");
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

