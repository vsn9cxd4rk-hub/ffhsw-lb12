package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugBelegungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugBelegungAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugBelegungAO fenster = new FahrzeugBelegungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
