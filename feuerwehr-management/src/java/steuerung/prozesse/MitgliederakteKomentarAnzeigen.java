package steuerung.prozesse;

import ao.mitglieder.MitgliederAkteKomentarAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederakteKomentarAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederAkteKomentarAO fenster = new MitgliederAkteKomentarAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
