package steuerung.prozesse.bestandsliste;

import ao.bestandsliste.ArtikelBestandslisteEintragenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class ArtikelEintragenAnzeigen extends Anzeige {

   public void ausfuehren() {
      ArtikelBestandslisteEintragenAO fenster = new ArtikelBestandslisteEintragenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
