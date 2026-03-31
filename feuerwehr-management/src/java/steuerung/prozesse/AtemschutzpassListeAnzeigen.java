package steuerung.prozesse;

import ao.listen.AtemschutzpassListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AtemschutzpassListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      AtemschutzpassListeAO fenster = new AtemschutzpassListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
