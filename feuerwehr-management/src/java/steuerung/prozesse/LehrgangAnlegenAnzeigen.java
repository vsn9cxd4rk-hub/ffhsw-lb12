package steuerung.prozesse;

import ao.ausbildung.MitgliederfunktionenVerwaltenAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangAnlegenAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederfunktionenVerwaltenAO fenster = new MitgliederfunktionenVerwaltenAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
