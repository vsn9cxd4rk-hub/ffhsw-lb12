package steuerung.prozesse;

import ao.veranstaltung.EMailNotificationAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class EMailNotificationAnzeigen extends Anzeige {

   public void ausfuehren() {
      EMailNotificationAO fenster = new EMailNotificationAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
