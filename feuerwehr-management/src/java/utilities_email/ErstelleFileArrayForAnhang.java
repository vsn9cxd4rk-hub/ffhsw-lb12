package utilities_email;

import java.io.File;
import logging.logging;

public class ErstelleFileArrayForAnhang {

   static File[] files;
   static int count = 0;


   public static File[] analysiereString(String string) {
      for(int i = 0; i < string.length(); ++i) {
         if(string.substring(i, i + 1).equals(",")) {
            ++count;
         }
      }

      initArrays();
      fillFileArray(string);
      return files;
   }

   static void initArrays() {
      files = new File[count];
   }

   static void fillFileArray(String string) {
      int index = 0;

      for(int i = 0; i < string.length(); ++i) {
         if(string.substring(i, i + 1).equals(",")) {
            logging.logInfo("Füge Datei zur E-Mail hinzu: " + string.subSequence(i - index, i));
            files[count - 1] = new File((String)string.subSequence(i - index, i));
            --count;
            index = -1;
         }

         ++index;
      }

   }
}
