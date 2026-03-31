package steuerung.prozesse.einstellungen;

import ao.einstellungen.GrundkonfigurationJWSAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class GrundkonfigurationJWSAnzeigen extends Anzeige {

   public void ausfuehren() {
      GrundkonfigurationJWSAO fenster = new GrundkonfigurationJWSAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
