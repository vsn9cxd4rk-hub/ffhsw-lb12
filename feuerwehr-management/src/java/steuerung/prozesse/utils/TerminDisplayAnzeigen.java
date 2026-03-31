package steuerung.prozesse.utils;

import ao.terminDisplay.TerminDisplayAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class TerminDisplayAnzeigen extends Anzeige {

   public void ausfuehren() {
      TerminDisplayAO fenster = new TerminDisplayAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
