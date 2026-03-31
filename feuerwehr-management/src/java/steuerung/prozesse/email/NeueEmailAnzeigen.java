package steuerung.prozesse.email;

import ao.email.NeueEMailAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class NeueEmailAnzeigen extends Anzeige {

   public void ausfuehren() {
      NeueEMailAO fenster = new NeueEMailAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
