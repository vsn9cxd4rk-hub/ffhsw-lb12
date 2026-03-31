package steuerung.prozesse;

import ao.FacebookPostKonfigurationAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FacebookPostKonfigurationAnzeigen extends Anzeige {

   public void ausfuehren() {
      FacebookPostKonfigurationAO fenster = new FacebookPostKonfigurationAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
