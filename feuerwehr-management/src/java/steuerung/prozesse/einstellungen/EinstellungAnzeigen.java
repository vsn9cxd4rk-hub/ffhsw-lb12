package steuerung.prozesse.einstellungen;

import ao.einstellungen.EinstellungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EinstellungAnzeigen extends Anzeige {

   public void ausfuehren() {
      EinstellungAO fenster = new EinstellungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
