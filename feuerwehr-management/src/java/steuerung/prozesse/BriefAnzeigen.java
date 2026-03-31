package steuerung.prozesse;

import ao.BriefAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BriefAnzeigen extends Anzeige {

   public void ausfuehren() {
      BriefAO fenster = new BriefAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
