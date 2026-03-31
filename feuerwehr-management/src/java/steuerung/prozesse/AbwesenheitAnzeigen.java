package steuerung.prozesse;

import ao.AbwesenheitAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AbwesenheitAnzeigen extends Anzeige {

   public void ausfuehren() {
      AbwesenheitAO fenster = new AbwesenheitAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
