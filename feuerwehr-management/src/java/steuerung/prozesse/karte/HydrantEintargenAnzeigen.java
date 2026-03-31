package steuerung.prozesse.karte;

import ao.karte.HydrantEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class HydrantEintargenAnzeigen extends Anzeige {

   public void ausfuehren() {
      HydrantEintragenAO fenster = new HydrantEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
