package steuerung.prozesse;

import ao.mitglieder.MitgliederAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederAnlegenAO fenster = new MitgliederAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
