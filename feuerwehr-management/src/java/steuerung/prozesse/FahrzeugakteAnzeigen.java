package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugakteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugakteAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugakteAO fenster = new FahrzeugakteAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
