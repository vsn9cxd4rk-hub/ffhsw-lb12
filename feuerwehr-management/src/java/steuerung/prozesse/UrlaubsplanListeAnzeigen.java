package steuerung.prozesse;

import ao.listen.UrlaubsListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class UrlaubsplanListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      UrlaubsListeAO fenster = new UrlaubsListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
