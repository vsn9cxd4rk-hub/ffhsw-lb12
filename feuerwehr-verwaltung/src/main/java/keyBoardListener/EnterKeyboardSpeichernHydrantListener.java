/*
 * Decompiled with CFR 0.152.
 */
package keyBoardListener;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import keyBoardListener.AbstractKeyboardListener;
import listener.HydrantSpeichernListener;

public class EnterKeyboardSpeichernHydrantListener
extends AbstractKeyboardListener {
    public EnterKeyboardSpeichernHydrantListener(JFrame frame) {
        super(frame);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == 10) {
            new HydrantSpeichernListener(this.getFrame()).actionPerformed(null);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

