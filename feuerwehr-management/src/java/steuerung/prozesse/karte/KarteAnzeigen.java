package steuerung.prozesse.karte;

import ao.karte.KarteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class KarteAnzeigen extends Anzeige {

   public void ausfuehren() {
      KarteAO fenster = new KarteAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
