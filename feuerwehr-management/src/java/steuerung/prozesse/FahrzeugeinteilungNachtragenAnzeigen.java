package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugeinteilungNachtragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugeinteilungNachtragenAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugeinteilungNachtragenAO fenster = new FahrzeugeinteilungNachtragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
