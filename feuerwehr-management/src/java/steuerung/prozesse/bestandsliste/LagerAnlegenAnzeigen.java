package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.LagerAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LagerAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      LagerAnlegenAO fenster = new LagerAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
