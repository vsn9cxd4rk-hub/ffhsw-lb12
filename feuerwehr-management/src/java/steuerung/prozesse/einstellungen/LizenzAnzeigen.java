package steuerung.prozesse.einstellungen;

import ao.einstellungen.NutzungsbedingungenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LizenzAnzeigen extends Anzeige {

   public void ausfuehren() {
      NutzungsbedingungenAO fenster = new NutzungsbedingungenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
