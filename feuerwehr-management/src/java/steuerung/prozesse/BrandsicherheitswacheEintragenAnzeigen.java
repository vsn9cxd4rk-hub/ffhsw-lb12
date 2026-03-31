package steuerung.prozesse;

import ao.BrandsicherheitswacheEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BrandsicherheitswacheEintragenAnzeigen extends Anzeige {

   public void ausfuehren() {
      BrandsicherheitswacheEintragenAO fenster = new BrandsicherheitswacheEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
