package steuerung.prozesse.abrechnung;

import ao.abrechnung.AbrechnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungAnzeigen extends Anzeige {

   public void ausfuehren() {
      AbrechnungAO fenster = new AbrechnungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
