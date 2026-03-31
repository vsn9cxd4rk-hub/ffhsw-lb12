package steuerung.prozesse;

import ao.AnmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class AnmeldungAnzeigen extends Anzeige {

   public void ausfuehren() {
      AnmeldungAO fenster = new AnmeldungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
