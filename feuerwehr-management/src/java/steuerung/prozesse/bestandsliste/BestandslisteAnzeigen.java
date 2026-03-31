package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.BestandslisteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BestandslisteAnzeigen extends Anzeige {

   public void ausfuehren() {
      BestandslisteAO fenster = new BestandslisteAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
