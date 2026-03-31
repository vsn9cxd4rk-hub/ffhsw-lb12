package steuerung.prozesse;

import ao.listen.LehrgangsmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangsmeldungAnzeigen extends Anzeige {

   public void ausfuehren() {
      LehrgangsmeldungAO fenster = new LehrgangsmeldungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
