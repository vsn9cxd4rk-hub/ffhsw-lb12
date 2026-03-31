package steuerung.prozesse.listen;

import ao.listen.AnwesenheitListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      AnwesenheitListeAO fenster = new AnwesenheitListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
