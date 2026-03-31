package steuerung.prozesse;

import ao.mitglieder.MitgliederHistoryListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederHistoryAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederHistoryListeAO fenster = new MitgliederHistoryListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
