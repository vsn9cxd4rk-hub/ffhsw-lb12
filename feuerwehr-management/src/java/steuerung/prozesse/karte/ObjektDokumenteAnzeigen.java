package steuerung.prozesse.karte;

import ao.karte.ObjektDokumenteAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ObjektDokumenteAnzeigen extends Anzeige {

   public void ausfuehren() {
      ObjektDokumenteAO fenster = new ObjektDokumenteAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
