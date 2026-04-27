package steuerung.prozesse.karte;

import ao.karte.StraßeEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class StraßenEintargenAnzeigen extends Anzeige {

   public void ausfuehren() {
      StraßeEintragenAO fenster = new StraßeEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
