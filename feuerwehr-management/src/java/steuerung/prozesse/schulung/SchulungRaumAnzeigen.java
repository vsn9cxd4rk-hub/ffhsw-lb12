package steuerung.prozesse.schulung;

import ao.schulung.SchulungRaumAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungRaumAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungRaumAO fenster = new SchulungRaumAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
