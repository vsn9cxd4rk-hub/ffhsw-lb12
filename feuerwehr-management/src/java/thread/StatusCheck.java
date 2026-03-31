package thread;

import java.io.File;
import logging.logging;

public class StatusCheck {

   public static void StatusCheckEinsatz() {
      // $FF: Couldn't be decompiled
   }

   public static void deleteEinsatzInfo(File inputdata) throws InterruptedException {
      inputdata.delete();
      logging.logInfo("lösche letzte Einsatzdatei");
   }
}
