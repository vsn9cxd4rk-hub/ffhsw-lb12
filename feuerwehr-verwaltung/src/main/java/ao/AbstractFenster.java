/*
 * Decompiled with CFR 0.152.
 */
package ao;

import java.awt.FlowLayout;
import javax.swing.JFrame;

public abstract class AbstractFenster
extends JFrame {
    private static final long serialVersionUID = 1L;
    protected FlowLayout layout = new FlowLayout();

    public AbstractFenster(String name) {
        super(name);
        this.setTitle(name);
        this.buttonErstellen();
        this.setzeAuswahllisten();
        this.labelErstellen();
        this.layoutFestlegen();
        this.boxenHinzufuegen();
        this.labelHinzufuegen();
        this.buttonHinzufuegen();
        this.actionErzeugen();
    }

    protected abstract void actionErzeugen();

    protected abstract void labelHinzufuegen();

    protected void boxenHinzufuegen() {
    }

    protected abstract void buttonHinzufuegen();

    protected abstract void layoutFestlegen();

    protected abstract void labelErstellen();

    protected void setzeAuswahllisten() {
    }

    protected abstract void buttonErstellen();
}

