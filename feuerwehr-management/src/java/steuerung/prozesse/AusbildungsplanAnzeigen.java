package steuerung.prozesse;

import ao.ausbildung.AusbildungsplanAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungsplanAnzeigen extends Anzeige {

   public void ausfuehren() {
      AusbildungsplanAO fenster = new AusbildungsplanAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
