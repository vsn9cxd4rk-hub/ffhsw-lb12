package steuerung.prozesse;

import ao.veranstaltung.VeranstaltungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class VeranstaltungAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      VeranstaltungAnlegenAO fenster = new VeranstaltungAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
