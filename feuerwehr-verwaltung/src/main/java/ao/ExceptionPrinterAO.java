/*
 * Decompiled with CFR 0.152.
 */
package ao;

import ao.AbstractFenster;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import logging.logging;

public class ExceptionPrinterAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static JTextArea textfield;
    private JLabel label;

    public ExceptionPrinterAO() {
        super("ServiceBasicClasses - Exception Printer");
        logging.logBasicClassesInfo("Starte: ExceptionPrinterAO");
    }

    @Override
    protected void buttonErstellen() {
        this.label = new JLabel("Es ist eine Schwerwigender Ausnahmefehler aufgetreten:");
        textfield = new JTextArea(20, 20);
    }

    @Override
    protected void setzeAuswahllisten() {
    }

    @Override
    protected void labelHinzufuegen() {
    }

    @Override
    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("ServiceBasicClasses - Exception Printer");
        this.pack();
        this.setDefaultCloseOperation(2);
    }

    @Override
    protected void buttonHinzufuegen() {
        this.add(this.label);
        this.add(textfield);
    }

    @Override
    protected void boxenHinzufuegen() {
    }

    @Override
    protected void labelErstellen() {
    }

    @Override
    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                ExceptionPrinterAO.this.dispose();
            }
        });
    }

    public void fensterAnzeigen() {
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

