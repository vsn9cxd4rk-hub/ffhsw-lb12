package steuerung.prozesse.listen;

import ao.listen.ArbeitgeberListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArbeitgeberListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      ArbeitgeberListeAO fenster = new ArbeitgeberListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
