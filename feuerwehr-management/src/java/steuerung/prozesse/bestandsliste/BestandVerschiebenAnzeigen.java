package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.BestandVerschiebenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BestandVerschiebenAnzeigen extends Anzeige {

   public void ausfuehren() {
      BestandVerschiebenAO fenster = new BestandVerschiebenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
