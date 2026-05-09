/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  com.itextpdf.text.Font$FontFamily
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import data.tabellen.TabelleBriefe;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.Briefe;
import go.DokumentLayoutOptions;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
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
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.BriefPDFSchreiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class BriefAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonVorschau;
    private JButton buttonVorlageEntfernen;
    private JLabel title_label;
    public static JTextArea textfield;
    public static JTextField title;
    private JScrollPane pane;
    private JCheckBox template;
    private JCheckBox[] jCheckboxArray;
    private JButton[] buttonSelektiereBenutzerGruppen;
    private JButton buttonSelektiereAlle;
    private JButton buttonDeSelektiereAlle;
    private JScrollPane paneJCheckBoxArray;
    private JComboBox<String> schriftart;
    private JLabel schriftart_label;
    private JComboBox<String> schriftgr\u00f6\u00dfe;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public BriefAO() {
        super("FeuerwehrManagementSystem - Brief");
        logging.logInfo((Object)"Starte: BriefAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern & Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonVorschau = new JButton("Vorschau");
        this.buttonSelektiereAlle = new JButton("Alle");
        this.buttonDeSelektiereAlle = new JButton("Entferne Alle");
        this.buttonVorlageEntfernen = new JButton("Vorlage l\u00f6schen");
        textfield = new JTextArea(23, 50);
        textfield.setLineWrap(true);
        textfield.setWrapStyleWord(true);
        this.pane = new JScrollPane(textfield);
        this.pane.setVerticalScrollBarPolicy(22);
        this.template = new JCheckBox("Als Vorlage Speichern");
        title = new JTextField("Anscheiben vom ?????", 90);
        this.title_label = new JLabel("Betreff / \u00dcberschrift: ");
        tree = new JTree(CreateTrees.CreateTreeBriefeTemplates());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
        this.modulBeschreibung = new JLabel("Brief erstellen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        String[] schriftarten = new String[]{"Helvetica", "TimesNewRoman", "Curier"};
        String[] schriftgr\u00f6\u00dfen = new String[]{"8", "9", "10", "11", "12", "13", "14"};
        this.schriftart = new JComboBox<String>(schriftarten);
        this.schriftart_label = new JLabel("Schriftart Text:");
        this.schriftgr\u00f6\u00dfe = new JComboBox<String>(schriftgr\u00f6\u00dfen);
        this.schriftgr\u00f6\u00dfe.setSelectedItem("12");
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
        this.setSize(1170, 750);
        this.setTitle("FeuerwehrManagementSystem - Brief");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.title_label);
        this.add(title);
        this.add(this.schriftart_label);
        this.add(this.schriftart);
        this.add(this.schriftgr\u00f6\u00dfe);
        this.add(this.dummy3);
        this.scrollPaneTree.setPreferredSize(new Dimension(300, 480));
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder rahmenTree = BorderFactory.createTitledBorder(lowerEtched, "Vorlagen");
        this.scrollPaneTree.setBorder(rahmenTree);
        this.add(this.scrollPaneTree);
        TitledBorder rahmen = BorderFactory.createTitledBorder(lowerEtched, "Text");
        this.pane.setBorder(rahmen);
        this.pane.setPreferredSize(new Dimension(550, 480));
        this.add(this.pane);
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        this.paneJCheckBoxArray = new JScrollPane(panel);
        this.paneJCheckBoxArray.setVerticalScrollBarPolicy(22);
        this.paneJCheckBoxArray.setPreferredSize(new Dimension(250, 480));
        TitledBorder rahmen2 = BorderFactory.createTitledBorder(lowerEtched, "Empf\u00e4nger Liste");
        this.paneJCheckBoxArray.setBorder(rahmen2);
        try {
            String[] mitgliederName = Utils.listToArray(tabMitglied.getAllMitgliederFromDataBase());
            int[] mitgleiderGruppenID = Utils.listToIntArray(tabMitglied.getAllMitgliederGruppenFromDataBaseByMitglied());
            int letzteMitgleiderGruppe = 0;
            String[] buttonLabels = Utils.listToArray(tabGruppe.getAllGruppen());
            this.jCheckboxArray = new JCheckBox[tabMitglied.getAllMitgliederCount()];
            this.buttonSelektiereBenutzerGruppen = new JButton[tabGruppe.count()];
            panel.add(this.buttonSelektiereAlle);
            panel.add(this.buttonDeSelektiereAlle);
            int g = 0;
            while (g < buttonLabels.length) {
                this.buttonSelektiereBenutzerGruppen[g] = new JButton(buttonLabels[g]);
                this.buttonSelektiereBenutzerGruppen[g].setName(buttonLabels[g]);
                panel.add(this.buttonSelektiereBenutzerGruppen[g]);
                this.buttonSelektiereBenutzerGruppen[g].addActionListener(this.createActionListener(tabGruppe.getID(buttonLabels[g])));
                ++g;
            }
            int x = 0;
            while (x < mitgliederName.length) {
                if (letzteMitgleiderGruppe != mitgleiderGruppenID[x]) {
                    JLabel trennung1 = new JLabel();
                    JLabel nameMitgliedergruppe = new JLabel(String.valueOf(tabGruppe.getGruppenName(mitgleiderGruppenID[x])) + ":");
                    panel.add(trennung1);
                    panel.add(nameMitgliedergruppe);
                }
                this.jCheckboxArray[x] = new JCheckBox();
                this.jCheckboxArray[x].setText(mitgliederName[x]);
                this.jCheckboxArray[x].setName(Integer.toString(mitgleiderGruppenID[x]));
                letzteMitgleiderGruppe = mitgleiderGruppenID[x];
                panel.add(this.jCheckboxArray[x]);
                ++x;
            }
            this.add(this.paneJCheckBoxArray);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.template);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonVorlageEntfernen);
        this.add(this.buttonVorschau);
        this.add(this.buttonSpeichern);
        this.buttonVorlageEntfernen.setEnabled(false);
        this.buttonSelektiereAlle.setBackground(Color.cyan);
        this.buttonDeSelektiereAlle.setBackground(Color.cyan);
    }

    protected void boxenHinzufuegen() {
    }

    private ActionListener createActionListener(final int index) {
        ActionListener action = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    logging.logInfo((Object)("Selektiere MitgliederGruppe: " + index));
                    int mCount = tabMitglied.getAllMitgliederCount();
                    int i = 0;
                    while (i < mCount) {
                        if (index == -1) {
                            BriefAO.this.jCheckboxArray[i].setSelected(true);
                        } else if (index == -2) {
                            BriefAO.this.jCheckboxArray[i].setSelected(false);
                        } else if (Integer.parseInt(BriefAO.this.jCheckboxArray[i].getName()) == index) {
                            BriefAO.this.jCheckboxArray[i].setSelected(true);
                        }
                        ++i;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        return action;
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSelektiereAlle.addActionListener(this.createActionListener(-1));
        this.buttonDeSelektiereAlle.addActionListener(this.createActionListener(-2));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                try {
                    TabelleBriefe tabBriefe = new TabelleBriefe();
                    textfield.setText(tabBriefe.getText(tree.getSelectionPath().getLastPathComponent().toString()));
                    title.setText(tree.getSelectionPath().getLastPathComponent().toString());
                    BriefAO.this.buttonVorlageEntfernen.setEnabled(true);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
        });
        this.buttonVorschau.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Brief wird erstellt... Bitte haben sie einen Moment Geduld...");
                Thread thread = new Thread(){

                    @Override
                    public void run() {
                        try {
                            StringBuilder buildVorschau = new StringBuilder();
                            buildVorschau.setLength(0);
                            TabelleMitglied tabMitglieder = new TabelleMitglied();
                            int count = 0;
                            int mitgliederListe = tabMitglieder.getAllMitgliederCount();
                            int i = 0;
                            while (i < mitgliederListe) {
                                if (BriefAO.this.jCheckboxArray[i].isSelected()) {
                                    buildVorschau.append(BriefAO.this.jCheckboxArray[i].getText());
                                    buildVorschau.append("\n");
                                    ++count;
                                }
                                ++i;
                            }
                            if (count == 0) {
                                JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                            } else {
                                DokumentLayoutOptions dokOptions = new DokumentLayoutOptions();
                                dokOptions.setNormalSchriftgr\u00f6\u00dfe(Integer.parseInt(BriefAO.this.schriftgr\u00f6\u00dfe.getSelectedItem().toString()));
                                if (BriefAO.this.schriftart.getSelectedItem().toString().equals("TimesNewRoman")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.TIMES_ROMAN);
                                } else if (BriefAO.this.schriftart.getSelectedItem().toString().equals("Helvetica")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.HELVETICA);
                                } else if (BriefAO.this.schriftart.getSelectedItem().toString().equals("Curier")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.COURIER);
                                }
                                BriefPDFSchreiben.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/brief/temp.pdf", buildVorschau.toString(), dokOptions);
                                Desktop.getDesktop().open(new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/brief/temp.pdf"));
                            }
                        }
                        catch (DocumentException | IOException | SQLException e) {
                            JOptionPane.showMessageDialog(null, Konstante.ERSTELLEN_FEHLER, "Fehlermeldung", 0);
                            MyEvent.setEvent((String)"0x0030");
                            logging.logPrintStackTrace((Exception)e);
                        }
                        MyEvent.setEvent((String)"0x0030");
                    }
                };
                thread.start();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Brief wird erstellt... Bitte haben sie einen Moment Geduld...");
                Thread thread = new Thread(){

                    @Override
                    public void run() {
                        try {
                            StringBuilder build = new StringBuilder();
                            TabelleMitglied tabMitglieder = new TabelleMitglied();
                            TabelleBriefe tabBriefe = new TabelleBriefe();
                            Briefe brief = new Briefe();
                            int count = 0;
                            int mitgliederListe = tabMitglieder.getAllMitgliederCount();
                            int i = 0;
                            while (i < mitgliederListe) {
                                if (BriefAO.this.jCheckboxArray[i].isSelected()) {
                                    build.append(BriefAO.this.jCheckboxArray[i].getText());
                                    build.append("\n");
                                    ++count;
                                }
                                ++i;
                            }
                            if (count == 0) {
                                JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                            } else {
                                int briefID = tabBriefe.getNextNummer();
                                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/brief/Brief_" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_ID_" + briefID + ".pdf";
                                brief.setId(briefID);
                                brief.setBericht(textfield.getText());
                                brief.setDateiname(dateiname);
                                brief.setErstelldatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                                brief.setEmpfaenger(build.toString());
                                brief.setJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
                                brief.setTitle(title.getText());
                                brief.setTemplate(BriefAO.this.template.isSelected() ? 1 : 0);
                                tabBriefe.insert(brief);
                                if (BriefAO.this.template.isSelected()) {
                                    tree.setModel(CreateTrees.CreateTreeBriefeTemplates());
                                }
                                Utils.dateiKatalogisieren(dateiname);
                                DokumentLayoutOptions dokOptions = new DokumentLayoutOptions();
                                dokOptions.setNormalSchriftgr\u00f6\u00dfe(Integer.parseInt(BriefAO.this.schriftgr\u00f6\u00dfe.getSelectedItem().toString()));
                                if (BriefAO.this.schriftart.getSelectedItem().toString().equals("TimesNewRoman")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.TIMES_ROMAN);
                                } else if (BriefAO.this.schriftart.getSelectedItem().toString().equals("Helvetica")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.HELVETICA);
                                } else if (BriefAO.this.schriftart.getSelectedItem().toString().equals("Curier")) {
                                    dokOptions.setNormalSchriftart(Font.FontFamily.COURIER);
                                }
                                BriefPDFSchreiben.PDFdocumentErstellen(dateiname, build.toString(), dokOptions);
                                logging.logInfo((Object)"Der Brief wurde erfolgreich erstellt");
                                Desktop.getDesktop().open(new File(dateiname));
                                BriefAO.this.buttonSpeichern.setEnabled(false);
                            }
                            MyEvent.setEvent((String)"0x0030");
                        }
                        catch (DocumentException | IOException | SQLException e) {
                            JOptionPane.showMessageDialog(null, Konstante.ERSTELLEN_FEHLER, "Fehlermeldung", 64);
                            MyEvent.setEvent((String)"0x0030");
                            logging.logPrintStackTrace((Exception)e);
                        }
                    }
                };
                thread.start();
            }
        });
        this.buttonVorlageEntfernen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleBriefe tabBrief = new TabelleBriefe();
                    tabBrief.updateTemplate(tree.getSelectionPath().getLastPathComponent().toString());
                    tree.setModel(CreateTrees.CreateTreeBriefeTemplates());
                    tree.setSelectionRow(0);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 64);
                    logging.logPrintStackTrace((Exception)e);
                }
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

