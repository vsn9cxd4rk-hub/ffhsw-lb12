package steuerung.prozesse.karte;

import ao.karte.ObjektEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ObjektEintargenAnzeigen extends Anzeige {

   public void ausfuehren() {
      ObjektEintragenAO fenster = new ObjektEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
