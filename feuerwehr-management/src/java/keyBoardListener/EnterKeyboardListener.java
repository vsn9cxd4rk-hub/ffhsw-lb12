package keyBoardListener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import keyBoardListener.AbstractKeyboardListener;
import listener.LoginListener;

public class EnterKeyboardListener extends AbstractKeyboardListener {

   public EnterKeyboardListener(JFrame frame) {
      super(frame);
   }

   public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      if(key == 10) {
         (new LoginListener(this.getFrame())).actionPerformed((ActionEvent)null);
      }

   }

   public void keyReleased(KeyEvent e) {}

   public void keyTyped(KeyEvent e) {}
}
