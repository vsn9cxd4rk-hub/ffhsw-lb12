package steuerung.prozesse;

import ao.mangelmeldung.MängelmeldungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MängelmeldungAnzeigen extends Anzeige {

   public void ausfuehren() {
      MängelmeldungAO fenster = new MängelmeldungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
