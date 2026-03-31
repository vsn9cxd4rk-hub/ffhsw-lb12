package steuerung.prozesse;

import ao.einstellungen.BenutzerAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BenutzerAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      BenutzerAnlegenAO fenster = new BenutzerAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
