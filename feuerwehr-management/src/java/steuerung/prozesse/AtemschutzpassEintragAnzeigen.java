package steuerung.prozesse;

import ao.einsatz.AtemschutzpassAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AtemschutzpassEintragAnzeigen extends Anzeige {

   public void ausfuehren() {
      AtemschutzpassAO fenster = new AtemschutzpassAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
