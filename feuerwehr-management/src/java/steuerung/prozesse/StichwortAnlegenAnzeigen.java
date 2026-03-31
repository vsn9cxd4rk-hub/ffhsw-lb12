package steuerung.prozesse;

import ao.einsatz.StichwortAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class StichwortAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      StichwortAnlegenAO fenster = new StichwortAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
