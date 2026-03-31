package steuerung.prozesse.email;

import ao.email.EMail_AdressbuchAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EMail_AdressbauchAnzeigen extends Anzeige {

   public void ausfuehren() {
      EMail_AdressbuchAO fenster = new EMail_AdressbuchAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
