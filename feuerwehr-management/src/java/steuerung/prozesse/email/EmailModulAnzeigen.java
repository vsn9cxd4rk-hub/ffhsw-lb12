package steuerung.prozesse.email;

import ao.email.EMailModulAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EmailModulAnzeigen extends Anzeige {

   public void ausfuehren() {
      EMailModulAO fenster = new EMailModulAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
