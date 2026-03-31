package steuerung.prozesse;

import ao.KategorienEditierenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class KategorienEditierenAnzeigen extends Anzeige {

   public void ausfuehren() {
      KategorienEditierenAO fenster = new KategorienEditierenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
