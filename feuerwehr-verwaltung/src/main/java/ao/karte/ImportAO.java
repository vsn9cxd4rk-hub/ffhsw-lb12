/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.karte;

import ao.AbstractFenster;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import utilities.Import;

public class ImportAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonAusfuehren;
    private JButton buttonOrdnerStra\u00dfenverzeichnis;
    private JButton buttonOrdnerHydrantenverzeichnis;
    private JLabel ueberschrift;
    private JLabel label_star\u00dfenverzeichnis;
    private JLabel label_hydrantenverzeichnis;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    public static JTextField textfieldstar\u00dfen;
    public static JTextField textfieldhydranten;
    public static JTextField textfieldStadt;
    private JFileChooser chooser;

    public ImportAO() {
        super("FeuerwehrManagementSystem Version: 3.21");
        logging.logInfo((Object)"Starte: ImportAO");
    }

    protected void buttonErstellen() {
        this.buttonAusfuehren = new JButton("Ausf\u00fchren");
        this.buttonAusfuehren.setToolTipText("Import ausf\u00fchren");
        this.buttonOrdnerStra\u00dfenverzeichnis = new JButton("...");
        this.buttonOrdnerStra\u00dfenverzeichnis.setToolTipText("Ordner w\u00e4hlen");
        this.buttonOrdnerHydrantenverzeichnis = new JButton("...");
        this.buttonOrdnerHydrantenverzeichnis.setToolTipText("Ordner w\u00e4hlen");
        this.ueberschrift = new JLabel("Import");
        this.label_star\u00dfenverzeichnis = new JLabel("Stra\u00dfenverzeichnis:");
        this.label_hydrantenverzeichnis = new JLabel("Hydrantenverzeichnis:");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        textfieldstar\u00dfen = new JTextField(25);
        textfieldhydranten = new JTextField(25);
        textfieldStadt = new JTextField("Moenchengladbach", 25);
        this.chooser = new JFileChooser();
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Import");
        this.setSize(380, 300);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.ueberschrift);
        this.add(this.dummy);
        this.add(this.label_star\u00dfenverzeichnis);
        this.add(textfieldstar\u00dfen);
        this.add(this.buttonOrdnerStra\u00dfenverzeichnis);
        this.add(this.label_hydrantenverzeichnis);
        this.add(textfieldhydranten);
        this.add(this.buttonOrdnerHydrantenverzeichnis);
        this.add(textfieldStadt);
        this.add(this.dummy2);
        this.add(this.buttonAusfuehren);
        this.add(this.dummy3);
        textfieldstar\u00dfen.setEditable(false);
        textfieldhydranten.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonAusfuehren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Import.ImportData(textfieldstar\u00dfen.getText(), textfieldStadt.getText());
                JOptionPane.showMessageDialog(null, "Import fertig");
                logging.logInfo((Object)"Import Fertig & Beenden");
            }
        });
        this.buttonOrdnerStra\u00dfenverzeichnis.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ImportAO.this.chooser.setFileSelectionMode(2);
                ImportAO.this.chooser.showSaveDialog(null);
                textfieldstar\u00dfen.setText(ImportAO.this.chooser.getSelectedFile().getPath());
            }
        });
        this.buttonOrdnerHydrantenverzeichnis.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ImportAO.this.chooser.setFileSelectionMode(2);
                ImportAO.this.chooser.showSaveDialog(null);
                textfieldhydranten.setText(ImportAO.this.chooser.getSelectedFile().getPath());
            }
        });
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

