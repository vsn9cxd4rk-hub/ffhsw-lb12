package steuerung.prozesse;

import ao.HauptprogrammAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class HauptprogrammAnzeigen extends Anzeige {

   public void ausfuehren() {
      HauptprogrammAO fenster = new HauptprogrammAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
