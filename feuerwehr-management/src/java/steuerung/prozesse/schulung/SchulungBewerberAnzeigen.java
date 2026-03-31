package steuerung.prozesse.schulung;

import ao.schulung.SchulungBewerberAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungBewerberAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungBewerberAO fenster = new SchulungBewerberAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
