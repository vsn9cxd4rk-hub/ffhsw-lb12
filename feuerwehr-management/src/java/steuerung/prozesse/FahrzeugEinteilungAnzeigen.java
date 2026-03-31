package steuerung.prozesse;

import ao.fahrzeuge.FahrzeugEinteilungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class FahrzeugEinteilungAnzeigen extends Anzeige {

   public void ausfuehren() {
      FahrzeugEinteilungAO fenster = new FahrzeugEinteilungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
