package steuerung.prozesse.listen;

import ao.listen.GeburtstagListeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class GeburtstagListeAnzeigen extends Anzeige {

   public void ausfuehren() {
      GeburtstagListeAO fenster = new GeburtstagListeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
