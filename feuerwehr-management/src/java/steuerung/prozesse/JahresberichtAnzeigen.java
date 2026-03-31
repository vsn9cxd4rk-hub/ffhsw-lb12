package steuerung.prozesse;

import ao.JahresberichtAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class JahresberichtAnzeigen extends Anzeige {

   public void ausfuehren() {
      JahresberichtAO fenster = new JahresberichtAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
