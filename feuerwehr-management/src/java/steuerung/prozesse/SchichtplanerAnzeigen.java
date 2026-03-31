package steuerung.prozesse;

import ao.schichtplaner.SchichtplanerAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchichtplanerAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchichtplanerAO fenster = new SchichtplanerAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
