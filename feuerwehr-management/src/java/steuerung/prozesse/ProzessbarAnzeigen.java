package steuerung.prozesse;

import ao.utils.ProzessBarAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ProzessbarAnzeigen extends Anzeige {

   public void ausfuehren() {
      ProzessBarAO fenster = new ProzessBarAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
