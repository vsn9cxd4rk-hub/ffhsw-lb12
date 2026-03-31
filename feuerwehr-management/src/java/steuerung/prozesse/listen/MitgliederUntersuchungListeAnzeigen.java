package steuerung.prozesse.listen;

import ao.listen.UntersuchungListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederUntersuchungListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      UntersuchungListeAO fenster = new UntersuchungListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
