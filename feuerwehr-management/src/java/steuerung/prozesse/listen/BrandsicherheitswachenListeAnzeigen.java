package steuerung.prozesse.listen;

import ao.listen.BrandsicherheitswachenListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BrandsicherheitswachenListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      BrandsicherheitswachenListeAO fenster = new BrandsicherheitswachenListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
