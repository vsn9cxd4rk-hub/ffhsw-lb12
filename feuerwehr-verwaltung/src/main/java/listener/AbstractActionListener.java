/*
 * Decompiled with CFR 0.152.
 */
package listener;

import java.awt.event.ActionListener;
import javax.swing.JFrame;

public abstract class AbstractActionListener
implements ActionListener {
    private JFrame frame;

    public AbstractActionListener(JFrame frame) {
        this.setFrame(frame);
    }

    protected JFrame getFrame() {
        return this.frame;
    }

    private void setFrame(JFrame frame) {
        this.frame = frame;
    }
}

