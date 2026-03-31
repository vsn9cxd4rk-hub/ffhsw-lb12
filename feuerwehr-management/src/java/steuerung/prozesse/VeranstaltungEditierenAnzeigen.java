package steuerung.prozesse;

import ao.veranstaltung.VeranstaltungEditierenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungEditierenAnzeigen extends Anzeige {

   public void ausfuehren() {
      VeranstaltungEditierenAO fenster = new VeranstaltungEditierenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
