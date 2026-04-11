package steuerung.prozesse;

import ao.ausbildung.BeförderungZuordnungAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class BeförderungAnzeigen extends Anzeige {

   public void ausfuehren() {
      BeförderungZuordnungAO fenster = new BeförderungZuordnungAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
