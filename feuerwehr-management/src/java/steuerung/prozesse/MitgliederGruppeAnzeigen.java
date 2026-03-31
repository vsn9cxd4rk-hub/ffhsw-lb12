package steuerung.prozesse;

import ao.mitglieder.MitgliederGruppeAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederGruppeAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederGruppeAnlegenAO fenster = new MitgliederGruppeAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
