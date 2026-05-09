/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.email;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleEinstellungen;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class EMailModulSignaturAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> type;
    private JLabel type_label;
    private JTextArea textfiled;
    private JScrollPane scrollPane;
    private JTextField textfiel_to;
    private JTextField textfiel_cc;
    private JTextField textfiel_bcc;
    private JLabel textfiel_to_label;
    private JLabel textfiel_cc_label;
    private JLabel textfiel_bcc_label;
    private JCheckBox signaturHinzufuegen;
    private JLabel signaturHinzufuegen_label;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public EMailModulSignaturAO() {
        super("FeuerwehrManagementSystem");
        logging.logInfo((Object)"Starte: EMailModulSignaturAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.textfiled = new JTextArea();
        this.scrollPane = new JScrollPane(this.textfiled);
        this.scrollPane.setVerticalScrollBarPolicy(22);
        this.scrollPane.setPreferredSize(new Dimension(500, 300));
        this.signaturHinzufuegen = new JCheckBox();
        this.signaturHinzufuegen_label = new JLabel("Standard Signatur beim Speichern hinzuf\u00fcgen: ");
        this.textfiel_to = new JTextField(22);
        this.textfiel_cc = new JTextField(22);
        this.textfiel_bcc = new JTextField(22);
        this.textfiel_to_label = new JLabel("Standard An: ");
        this.textfiel_cc_label = new JLabel("Standard CC: ");
        this.textfiel_bcc_label = new JLabel("Standard BCC: ");
        this.modulBeschreibung = new JLabel("E-MailSignatur / Template Konfigurator");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        String[] typeListe = new String[]{"Signatur", "Einsatzbericht", "M\u00e4ngelmeldung"};
        this.type = new JComboBox<String>(typeListe);
        this.type_label = new JLabel("Signatur / Templatetyp: ");
        this.type.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                EMailModulSignaturAO.this.textfiel_bcc.setVisible(true);
                EMailModulSignaturAO.this.textfiel_cc.setVisible(true);
                EMailModulSignaturAO.this.textfiel_to.setVisible(true);
                EMailModulSignaturAO.this.textfiel_to_label.setVisible(true);
                EMailModulSignaturAO.this.textfiel_cc_label.setVisible(true);
                EMailModulSignaturAO.this.textfiel_bcc_label.setVisible(true);
                EMailModulSignaturAO.this.signaturHinzufuegen.setVisible(true);
                EMailModulSignaturAO.this.signaturHinzufuegen_label.setVisible(true);
                if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Signatur")) {
                    EMailModulSignaturAO.this.textfiled.setText(runApplication.EINSTELLUNGEN.get("emailSignatur"));
                    EMailModulSignaturAO.this.textfiel_bcc.setVisible(false);
                    EMailModulSignaturAO.this.textfiel_cc.setVisible(false);
                    EMailModulSignaturAO.this.textfiel_to.setVisible(false);
                    EMailModulSignaturAO.this.textfiel_to_label.setVisible(false);
                    EMailModulSignaturAO.this.textfiel_cc_label.setVisible(false);
                    EMailModulSignaturAO.this.textfiel_bcc_label.setVisible(false);
                    EMailModulSignaturAO.this.signaturHinzufuegen.setVisible(false);
                    EMailModulSignaturAO.this.signaturHinzufuegen_label.setVisible(false);
                } else if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Einsatzbericht")) {
                    EMailModulSignaturAO.this.textfiled.setText(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzbericht"));
                    EMailModulSignaturAO.this.textfiel_bcc.setText(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtBCC"));
                    EMailModulSignaturAO.this.textfiel_cc.setText(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtCC"));
                    EMailModulSignaturAO.this.textfiel_to.setText(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtAN"));
                } else if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("M\u00e4ngelmeldung")) {
                    EMailModulSignaturAO.this.textfiled.setText(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldung"));
                    EMailModulSignaturAO.this.textfiel_bcc.setText(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungBCC"));
                    EMailModulSignaturAO.this.textfiel_cc.setText(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungCC"));
                    EMailModulSignaturAO.this.textfiel_to.setText(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungAN"));
                }
            }
        });
        this.textfiled.setText(runApplication.EINSTELLUNGEN.get("emailSignatur"));
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - E-Mail Modul");
        this.setSize(560, 590);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                logging.logInfo((Object)"E-Mail Modul beenden");
            }
        });
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.type_label);
        this.add(this.type);
        this.add(this.scrollPane);
        this.add(this.dummy2);
        this.panel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.textfiel_to_label);
        this.panel.add(this.textfiel_to);
        this.panel.add(this.textfiel_cc_label);
        this.panel.add(this.textfiel_cc);
        this.panel.add(this.textfiel_bcc_label);
        this.panel.add(this.textfiel_bcc);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.textfiel_bcc.setVisible(false);
        this.textfiel_cc.setVisible(false);
        this.textfiel_to.setVisible(false);
        this.textfiel_to_label.setVisible(false);
        this.textfiel_cc_label.setVisible(false);
        this.textfiel_bcc_label.setVisible(false);
        this.signaturHinzufuegen.setVisible(false);
        this.signaturHinzufuegen_label.setVisible(false);
        this.textfiled.setCaretPosition(0);
        this.textfiled.setWrapStyleWord(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleEinstellungen tabEinstellungen = new TabelleEinstellungen();
                try {
                    if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Signatur")) {
                        tabEinstellungen.update("emailSignatur", EMailModulSignaturAO.this.textfiled.getText());
                    } else if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Einsatzbericht")) {
                        tabEinstellungen.update("emailTemplateEinsatzberichtAN", EMailModulSignaturAO.this.textfiel_to.getText());
                        tabEinstellungen.update("emailTemplateEinsatzberichtCC", EMailModulSignaturAO.this.textfiel_cc.getText());
                        tabEinstellungen.update("emailTemplateEinsatzberichtBCC", EMailModulSignaturAO.this.textfiel_bcc.getText());
                        if (EMailModulSignaturAO.this.signaturHinzufuegen.isSelected()) {
                            tabEinstellungen.update("emailTemplateEinsatzbericht", String.valueOf(EMailModulSignaturAO.this.textfiled.getText()) + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                        } else {
                            tabEinstellungen.update("emailTemplateEinsatzbericht", EMailModulSignaturAO.this.textfiled.getText());
                        }
                    } else if (EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("M\u00e4ngelmeldung")) {
                        tabEinstellungen.update("emailTemplateM\u00e4ngelmeldungAN", EMailModulSignaturAO.this.textfiel_to.getText());
                        tabEinstellungen.update("emailTemplateM\u00e4ngelmeldungCC", EMailModulSignaturAO.this.textfiel_cc.getText());
                        tabEinstellungen.update("emailTemplateM\u00e4ngelmeldungBCC", EMailModulSignaturAO.this.textfiel_bcc.getText());
                        if (EMailModulSignaturAO.this.signaturHinzufuegen.isSelected()) {
                            tabEinstellungen.update("emailTemplateM\u00e4ngelmeldung", String.valueOf(EMailModulSignaturAO.this.textfiled.getText()) + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                        } else {
                            tabEinstellungen.update("emailTemplateM\u00e4ngelmeldung", EMailModulSignaturAO.this.textfiled.getText());
                        }
                    }
                    runApplication.EINSTELLUNGEN = tabEinstellungen.getAllEinstellungen();
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}

