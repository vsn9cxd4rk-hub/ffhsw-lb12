package listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import listener.AbstractActionListener;
import steuerung.Status;
import steuerung.Steuerung;

public class UnwetterwarnungListener extends AbstractActionListener {

   public UnwetterwarnungListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      Steuerung.setStatus(Status.UNWETTERWARNUNG);
      Steuerung.steuerung();
   }
}
