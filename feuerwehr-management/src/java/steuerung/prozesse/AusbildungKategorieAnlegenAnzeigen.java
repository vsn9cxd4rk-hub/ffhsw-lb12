package steuerung.prozesse;

import ao.ausbildung.AusbildungKategorieAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungKategorieAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      AusbildungKategorieAnlegenAO fenster = new AusbildungKategorieAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
