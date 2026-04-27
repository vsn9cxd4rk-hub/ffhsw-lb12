package steuerung.prozesse;

import ao.mangelmeldung.MangelkommentarAnlegenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MängelmeldungKommentarAnzeigen extends Anzeige {

   public void ausfuehren() {
      MangelkommentarAnlegenAO fenster = new MangelkommentarAnlegenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
