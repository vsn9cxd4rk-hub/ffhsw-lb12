package steuerung.prozesse.schulung;

import ao.schulung.SchulungListeOptionenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungListeOptionenAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungListeOptionenAO fenster = new SchulungListeOptionenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
