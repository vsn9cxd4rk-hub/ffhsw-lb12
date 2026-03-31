package steuerung.prozesse;

import ao.listen.MitgliederLaufbahnListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliedLaufbahnListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederLaufbahnListeAO fenster = new MitgliederLaufbahnListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
