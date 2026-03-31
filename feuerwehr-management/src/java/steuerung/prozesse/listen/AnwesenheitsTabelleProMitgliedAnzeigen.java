package steuerung.prozesse.listen;

import ao.listen.AnwesenheitsTabelleProMitgliedAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnwesenheitsTabelleProMitgliedAnzeigen extends Anzeige {

   public void ausfuehren() {
      AnwesenheitsTabelleProMitgliedAO fenster = new AnwesenheitsTabelleProMitgliedAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
