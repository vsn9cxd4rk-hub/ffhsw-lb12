package steuerung.prozesse.schulung;

import ao.schulung.SchulungGruppeAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class SchulungGruppeAnzeigen extends Anzeige {

   public void ausfuehren() {
      SchulungGruppeAO fenster = new SchulungGruppeAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
