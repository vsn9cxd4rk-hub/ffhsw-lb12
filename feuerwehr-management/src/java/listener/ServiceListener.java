package listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import listener.AbstractActionListener;
import steuerung.Status;
import steuerung.Steuerung;

public class ServiceListener extends AbstractActionListener {

   public ServiceListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      Steuerung.setStatus(Status.ANMELDUNG);
      Steuerung.steuerung();
   }
}
