package steuerung.prozesse;

import ao.mitglieder.MitgliedAusserDienstAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliedAusserDienstAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliedAusserDienstAO fenster = new MitgliedAusserDienstAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
