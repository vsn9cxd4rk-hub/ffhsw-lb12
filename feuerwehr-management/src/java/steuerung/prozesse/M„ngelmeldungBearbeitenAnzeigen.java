package steuerung.prozesse;

import ao.mangelmeldung.MängelmeldungBearbeitenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MängelmeldungBearbeitenAnzeigen extends Anzeige {

   public void ausfuehren() {
      MängelmeldungBearbeitenAO fenster = new MängelmeldungBearbeitenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
