package steuerung.prozesse.administrator;

import ao.administrator.LogbuchAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LogbuchAnzeigen extends Anzeige {

   public void ausfuehren() {
      LogbuchAO fenster = new LogbuchAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
