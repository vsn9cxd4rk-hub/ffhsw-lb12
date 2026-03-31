package steuerung.prozesse.listen;

import ao.listen.MitgliederListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederListeAO fenster = new MitgliederListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
