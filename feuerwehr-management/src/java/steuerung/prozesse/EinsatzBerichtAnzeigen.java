package steuerung.prozesse;

import ao.einsatz.EinsatzBerichtAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinsatzBerichtAnzeigen extends Anzeige {

   public void ausfuehren() {
      EinsatzBerichtAO fenster = new EinsatzBerichtAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
