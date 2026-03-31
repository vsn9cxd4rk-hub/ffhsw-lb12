package steuerung.prozesse;

import ao.einstellungen.MeinPasswortAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MeinPasswortAnzeigen extends Anzeige {

   public void ausfuehren() {
      MeinPasswortAO fenster = new MeinPasswortAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
