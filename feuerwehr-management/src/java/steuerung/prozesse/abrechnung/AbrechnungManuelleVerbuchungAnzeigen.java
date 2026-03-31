package steuerung.prozesse.abrechnung;

import ao.abrechnung.ManuelleVerbuchungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbrechnungManuelleVerbuchungAnzeigen extends Anzeige {

   public void ausfuehren() {
      ManuelleVerbuchungAO fenster = new ManuelleVerbuchungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
