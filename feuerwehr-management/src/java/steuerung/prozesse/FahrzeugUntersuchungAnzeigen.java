package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugUntersuchungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugUntersuchungAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugUntersuchungAnlegenAO fenster = new FahrzeugUntersuchungAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
