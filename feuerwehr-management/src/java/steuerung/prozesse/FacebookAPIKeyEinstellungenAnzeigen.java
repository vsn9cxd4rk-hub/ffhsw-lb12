package steuerung.prozesse;

import ao.FacebookAPIKeyAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FacebookAPIKeyEinstellungenAnzeigen extends Anzeige {

   public void ausfuehren() {
      FacebookAPIKeyAO fenster = new FacebookAPIKeyAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
