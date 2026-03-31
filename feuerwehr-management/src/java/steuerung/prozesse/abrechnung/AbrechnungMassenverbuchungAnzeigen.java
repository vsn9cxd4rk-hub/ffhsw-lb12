package steuerung.prozesse.abrechnung;

import ao.abrechnung.MassenverbuchungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungMassenverbuchungAnzeigen extends Anzeige {

   public void ausfuehren() {
      MassenverbuchungAO fenster = new MassenverbuchungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
