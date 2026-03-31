package steuerung.prozesse;

import ao.einsatz.VerdienstausfallAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VerdienstausfallAnzeigen extends Anzeige {

   public void ausfuehren() {
      VerdienstausfallAO fenster = new VerdienstausfallAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
