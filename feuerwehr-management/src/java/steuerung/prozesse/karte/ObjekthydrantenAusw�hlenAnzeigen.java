package steuerung.prozesse.karte;

import ao.karte.ObjekthydrantenAuswählenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ObjekthydrantenAuswählenAnzeigen extends Anzeige {

   public void ausfuehren() {
      ObjekthydrantenAuswählenAO fenster = new ObjekthydrantenAuswählenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
