package steuerung.prozesse;

import ao.listen.EinsatzListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinsatzListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      EinsatzListeAO fenster = new EinsatzListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
