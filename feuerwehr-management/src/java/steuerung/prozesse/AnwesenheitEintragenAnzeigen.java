package steuerung.prozesse;

import ao.AnwesenheitEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitEintragenAnzeigen extends Anzeige {

   public void ausfuehren() {
      AnwesenheitEintragenAO fenster = new AnwesenheitEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
