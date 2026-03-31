package steuerung.prozesse.listen;

import ao.listen.LehrgangListeOptionenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangListeOptionenAnzeigen extends Anzeige {

   public void ausfuehren() {
      LehrgangListeOptionenAO fenster = new LehrgangListeOptionenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
