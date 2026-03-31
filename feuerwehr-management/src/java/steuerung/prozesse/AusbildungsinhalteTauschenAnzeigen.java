package steuerung.prozesse;

import ao.ausbildung.AusbildungsinhaltTauschenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AusbildungsinhalteTauschenAnzeigen extends Anzeige {

   public void ausfuehren() {
      AusbildungsinhaltTauschenAO fenster = new AusbildungsinhaltTauschenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
