package steuerung.prozesse;

import ao.schichtplaner.SchichtAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchichtAnlegenAO fenster = new SchichtAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
