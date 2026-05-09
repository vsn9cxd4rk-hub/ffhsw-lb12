/*
 * Decompiled with CFR 0.152.
 */
package keyBoardListener;

import java.awt.event.KeyListener;
import javax.swing.JFrame;

public abstract class AbstractKeyboardListener
implements KeyListener {
    private JFrame frame;

    public AbstractKeyboardListener(JFrame frame) {
        this.setFrame(frame);
    }

    protected JFrame getFrame() {
        return this.frame;
    }

    private void setFrame(JFrame frame) {
        this.frame = frame;
    }
}

