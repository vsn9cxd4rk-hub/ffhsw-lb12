package steuerung.prozesse;

import ao.ausbildung.LehrgangZuordnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class LehrgangZuordnungAnzeigen extends Anzeige {

   public void ausfuehren() {
      LehrgangZuordnungAO fenster = new LehrgangZuordnungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
