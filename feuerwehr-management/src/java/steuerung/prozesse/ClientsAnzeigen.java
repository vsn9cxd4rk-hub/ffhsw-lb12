package steuerung.prozesse;

import ao.administrator.ClientAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ClientsAnzeigen extends Anzeige {

   public void ausfuehren() {
      ClientAO fenster = new ClientAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
