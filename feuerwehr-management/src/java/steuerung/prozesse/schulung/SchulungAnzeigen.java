package steuerung.prozesse.schulung;

import ao.schulung.SchulungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungAO fenster = new SchulungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
