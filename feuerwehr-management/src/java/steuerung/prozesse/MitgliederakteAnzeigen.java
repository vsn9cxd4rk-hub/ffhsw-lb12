package steuerung.prozesse;

import ao.mitglieder.MitgliederakteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederakteAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederakteAO fenster = new MitgliederakteAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
