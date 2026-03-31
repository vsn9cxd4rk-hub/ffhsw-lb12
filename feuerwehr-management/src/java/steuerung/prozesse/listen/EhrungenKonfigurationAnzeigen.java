package steuerung.prozesse.listen;

import ao.ausbildung.EhrungenKonfigurationAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EhrungenKonfigurationAnzeigen extends Anzeige {

   public void ausfuehren() {
      EhrungenKonfigurationAO fenster = new EhrungenKonfigurationAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
