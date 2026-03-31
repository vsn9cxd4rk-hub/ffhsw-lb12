package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugAkteKomentarAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugakteKomentarAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugAkteKomentarAO fenster = new FahrzeugAkteKomentarAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
