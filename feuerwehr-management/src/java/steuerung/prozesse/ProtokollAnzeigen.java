package steuerung.prozesse;

import ao.ProtokollAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ProtokollAnzeigen extends Anzeige {

   public void ausfuehren() {
      ProtokollAO fenster = new ProtokollAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
