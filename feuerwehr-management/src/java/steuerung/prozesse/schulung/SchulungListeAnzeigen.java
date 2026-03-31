package steuerung.prozesse.schulung;

import ao.schulung.SchulungListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungListeAO fenster = new SchulungListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
