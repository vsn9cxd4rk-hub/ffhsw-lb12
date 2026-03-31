package steuerung.prozesse.karte;

import ao.karte.AAOAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AAOAnzeigen extends Anzeige {

   public void ausfuehren() {
      AAOAO fenster = new AAOAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
