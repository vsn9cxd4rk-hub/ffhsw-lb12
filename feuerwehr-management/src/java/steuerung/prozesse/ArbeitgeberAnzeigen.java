package steuerung.prozesse;

import ao.mitglieder.MitgliederArbeitAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArbeitgeberAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederArbeitAnlegenAO fenster = new MitgliederArbeitAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
