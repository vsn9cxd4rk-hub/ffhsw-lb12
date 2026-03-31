package steuerung.prozesse;

import ao.mitglieder.MitgliederAnredeAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederAnredeAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederAnredeAnlegenAO fenster = new MitgliederAnredeAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
