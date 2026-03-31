package steuerung.prozesse.administrator;

import ao.administrator.DebugAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class DebugAnzeigen extends Anzeige {

   public void ausfuehren() {
      DebugAO fenster = new DebugAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
