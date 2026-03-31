package steuerung.prozesse;

import ao.einsatz.AtemschutzpassEinsatzDetailsAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AtemschutzpassEinsatzDetailsAnzeigen extends Anzeige {

   public void ausfuehren() {
      AtemschutzpassEinsatzDetailsAO fenster = new AtemschutzpassEinsatzDetailsAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
