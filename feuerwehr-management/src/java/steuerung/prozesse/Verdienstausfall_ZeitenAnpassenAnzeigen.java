package steuerung.prozesse;

import ao.einsatz.Verdienstausfall_ZeitenAnpassenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class Verdienstausfall_ZeitenAnpassenAnzeigen extends Anzeige {

   public void ausfuehren() {
      Verdienstausfall_ZeitenAnpassenAO fenster = new Verdienstausfall_ZeitenAnpassenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
