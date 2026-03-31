package steuerung.prozesse;

import ao.fahrzeuge.FahrtenbuchAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrtenbuchAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrtenbuchAO fenster = new FahrtenbuchAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
