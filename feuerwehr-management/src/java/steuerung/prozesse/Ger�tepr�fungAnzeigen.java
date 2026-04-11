package steuerung.prozesse;

import ao.GeräteprüfungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class GeräteprüfungAnzeigen extends Anzeige {

   public void ausfuehren() {
      GeräteprüfungAO fenster = new GeräteprüfungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
