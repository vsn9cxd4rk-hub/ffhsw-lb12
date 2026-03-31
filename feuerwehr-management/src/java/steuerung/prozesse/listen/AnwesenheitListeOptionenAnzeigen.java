package steuerung.prozesse.listen;

import ao.listen.AnwesenheitListeOptionenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitListeOptionenAnzeigen extends Anzeige {

   public void ausfuehren() {
      AnwesenheitListeOptionenAO fenster = new AnwesenheitListeOptionenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
