package steuerung.prozesse;

import ao.einstellungen.BerechtigungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BerechtigungAnzeigen extends Anzeige {

   public void ausfuehren() {
      BerechtigungAnlegenAO fenster = new BerechtigungAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
