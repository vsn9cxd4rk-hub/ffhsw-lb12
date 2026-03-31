package steuerung.prozesse.abrechnung;

import ao.abrechnung.ArtikelAbrechnungEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungArtikelAnzeigen extends Anzeige {

   public void ausfuehren() {
      ArtikelAbrechnungEintragenAO fenster = new ArtikelAbrechnungEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
