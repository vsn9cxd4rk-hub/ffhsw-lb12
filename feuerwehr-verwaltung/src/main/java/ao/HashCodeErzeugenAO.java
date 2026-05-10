/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package ao;

import ao.AbstractFenster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import listener.BeendenListener;
import logging.logging;
import utilities.hash;

public class HashCodeErzeugenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JTextField toHashCodeField;
    private JLabel toHashCodeField_label;
    private JLabel HashCode_label;
    private JButton buttonAusfuehren;
    private JButton buttonBeenden;

    public HashCodeErzeugenAO() {
        super("HashCodeCreator");
        logging.logInfo((Object)"Starte: HashCodeErzeugenAO");
    }

    @Override
    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("HashCodeCreator");
        this.setSize(400, 170);
        this.setDefaultCloseOperation(2);
    }

    @Override
    protected void buttonErstellen() {
        this.toHashCodeField = new JTextField(30);
        this.toHashCodeField_label = new JLabel("Eingabe: ");
        this.HashCode_label = new JLabel("                                                                                                               ");
        this.buttonAusfuehren = new JButton("Erzeugen");
        this.buttonBeenden = new JButton("Beenden");
    }

    @Override
    protected void labelHinzufuegen() {
        this.add(this.toHashCodeField_label);
        this.add(this.toHashCodeField);
        this.add(this.HashCode_label);
        this.add(this.buttonAusfuehren);
        this.add(this.buttonBeenden);
    }

    @Override
    protected void buttonHinzufuegen() {
    }

    @Override
    protected void actionErzeugen() {
        this.buttonBeenden.addActionListener(new BeendenListener(this));
        this.buttonAusfuehren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                HashCodeErzeugenAO.this.HashCode_label.setText(hash.createHashCode(HashCodeErzeugenAO.this.toHashCodeField.getText()));
            }
        });
    }

    @Override
    protected void labelErstellen() {
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

