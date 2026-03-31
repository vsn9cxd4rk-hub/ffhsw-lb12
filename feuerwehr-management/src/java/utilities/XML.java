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
         FileWriter e = new FileWriter(dateiname);
         BufferedReader inFile = new BufferedReader(new FileReader(templateFile));
         String zeile = inFile.readLine();
         StringBuilder build = new StringBuilder();
         logging.logInfo("Starte Manipulation des XML-Templates");

         for(; zeile != null; zeile = inFile.readLine()) {
            build.setLength(0);

            for(int i = 0; i < toNewValue.length; ++i) {
               if(zeile.contains(variableInFile[i])) {
                  logging.logInfo("Gefunden: " + variableInFile[i] + " Ersetze: " + toNewValue[i]);
                  int itemIndex = zeile.indexOf(variableInFile[i]);
                  build.append(zeile.substring(0, itemIndex));
                  build.append(toNewValue[i]);
                  if(variableInFile[i].equals("01.01.2000")) {
                     build.append(zeile.substring(itemIndex + 10, zeile.length()));
                  } else {
                     build.append(zeile.substring(itemIndex + 5, zeile.length()));
                  }
               }
            }

            if(build.length() == 0) {
               e.write(zeile + "\r\n");
            } else {
               e.write(build.toString() + "\r\n");
            }
         }

         e.close();
         inFile.close();
         logging.logInfo("Einsatzbericht wurd erfolgreich erzeugt");
      } catch (IOException var10) {
         logging.logPrintStackTrace(var10);
      }

   }
}
