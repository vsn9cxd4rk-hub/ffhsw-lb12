package steuerung.prozesse;

import ao.listen.FahrtenbuchListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrtenbuchListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrtenbuchListeAO fenster = new FahrtenbuchListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
