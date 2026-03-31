package steuerung.prozesse.listen;

import ao.listen.BeteiligungUebersichtListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BeteiligungUebersichtListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      BeteiligungUebersichtListeAO fenster = new BeteiligungUebersichtListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
