package steuerung.prozesse.karte;

import ao.karte.ObjekthydrantenDetailsAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ObjekthydrantenDetailsAnzeigen extends Anzeige {

   public void ausfuehren() {
      ObjekthydrantenDetailsAO fenster = new ObjekthydrantenDetailsAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
