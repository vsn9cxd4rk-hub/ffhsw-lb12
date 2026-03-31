package steuerung.prozesse;

import ao.mitglieder.MitgliederVerfuegbarkeitAO;
import steuerung.Status;
import steuerung.prozesse.Anzeige;

public class MitgliederVerfuegbarkeiAnzeigen extends Anzeige {

   public void ausfuehren() {
      MitgliederVerfuegbarkeitAO fenster = new MitgliederVerfuegbarkeitAO();
      fenster.fensterAnzeigen();
      this.setStatus(Status.ENDE);
   }
}
