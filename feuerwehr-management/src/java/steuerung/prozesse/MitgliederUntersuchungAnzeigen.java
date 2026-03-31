package steuerung.prozesse;

import ao.mitglieder.MitgliederUntersuchungAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederUntersuchungAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederUntersuchungAnlegenAO fenster = new MitgliederUntersuchungAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
