package steuerung.prozesse.listen;

import ao.listen.AngehoerigenListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AngehoerigeListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      AngehoerigenListeAO fenster = new AngehoerigenListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
