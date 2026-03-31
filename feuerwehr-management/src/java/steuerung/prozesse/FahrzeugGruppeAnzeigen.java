package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugGruppeAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugGruppeAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugGruppeAnlegenAO fenster = new FahrzeugGruppeAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
