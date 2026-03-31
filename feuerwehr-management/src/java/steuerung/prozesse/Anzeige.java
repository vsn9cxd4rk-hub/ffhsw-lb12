package steuerung.prozesse;

import steuerung.Status;
import steuerung.Steuerung;

public abstract class Anzeige {

   protected final void setStatus(Status status) {
      Steuerung.setStatus(status);
   }

   protected final void getStatus() {
      Steuerung.getStatus();
   }

   public abstract void ausfuehren();
}
