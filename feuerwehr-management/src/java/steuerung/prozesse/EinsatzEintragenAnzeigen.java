package steuerung.prozesse;

import ao.einsatz.EinsatzEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinsatzEintragenAnzeigen extends Anzeige {

   public void ausfuehren() {
      EinsatzEintragenAO fenster = new EinsatzEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
