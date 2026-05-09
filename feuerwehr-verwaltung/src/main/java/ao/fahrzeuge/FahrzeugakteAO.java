/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.Datei
 */
package ao.fahrzeuge;

import ao.AbstractFenster;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Datei;
import utilities.Konstante;

public class FahrzeugakteAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAnsehen;
    private JButton buttonDrucken;
    private JButton buttonAlsEMailSenden;
    private JButton buttonHochladen;
    private JFileChooser chooser;
    private JList liste;
    private JScrollPane pane_liste;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private static String aktuellerOrdner;

    static {
        aktuellerOrdner = null;
    }

    public FahrzeugakteAO() {
        super("FeuerwehrManagementSystem - Fahrzeugakte");
        logging.logInfo((Object)("Starte: " + this.getName()));
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAnsehen = new JButton("\u00d6ffnen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonAlsEMailSenden = new JButton("Als E-Mail senden");
        this.buttonHochladen = new JButton("Datei einf\u00fcgen");
        this.modulBeschreibung = new JLabel("Fahrzeugakte");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        tree = new JTree(CreateTrees.CreateTreeFahrzeugListe());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
    }

    protected void setzeAuswahllisten() {
        this.liste = new JList();
        this.liste.setVisibleRowCount(15);
        this.liste.setToolTipText("Liste der verf\u00fcgbaren Anh\u00e4nge");
        this.pane_liste = new JScrollPane(this.liste);
        this.pane_liste.setVerticalScrollBarPolicy(22);
        this.pane_liste.setPreferredSize(new Dimension(600, 200));
    }

    protected void boxenHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Fahrzeugakte");
        this.setSize(1020, 620);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.scrollPaneTree.setPreferredSize(new Dimension(330, 450));
        this.add(this.scrollPaneTree);
        this.pane_liste.setPreferredSize(new Dimension(630, 450));
        this.add(this.pane_liste);
        this.add(this.buttonAnsehen);
        this.add(this.buttonDrucken);
        this.add(this.buttonHochladen);
        try {
            if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
                this.add(this.buttonAlsEMailSenden);
            }
        }
        catch (NumberFormatException e) {
            logging.logInfo((Object)e);
        }
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                try {
                    int fID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    File ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID);
                    File[] files = ordner.listFiles();
                    String[] fileName = new String[files.length];
                    int i = 0;
                    while (i < files.length) {
                        fileName[i] = files[i].getName();
                        ++i;
                    }
                    aktuellerOrdner = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/";
                    FahrzeugakteAO.this.liste.setListData(fileName);
                }
                catch (StringIndexOutOfBoundsException ex) {
                    tree.expandPath(tree.getSelectionPath());
                    FahrzeugakteAO.this.liste.setListData(new String[0]);
                }
                catch (NullPointerException ex) {
                    FahrzeugakteAO.this.liste.setListData(new String[0]);
                }
                catch (NumberFormatException ex) {
                    FahrzeugakteAO.this.liste.setListData(new String[0]);
                }
            }
        });
        this.liste.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    FahrzeugakteAO.this.buttonAnsehen.doClick();
                }
            }
        });
        this.buttonAlsEMailSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    runApplication.mailData.setStatus(1);
                    runApplication.mailData.setAnhang(String.valueOf(aktuellerOrdner) + FahrzeugakteAO.this.liste.getSelectedValue().toString() + ",");
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
                }
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String dateiname = String.valueOf(aktuellerOrdner) + FahrzeugakteAO.this.liste.getSelectedValue().toString();
                    Desktop.getDesktop().print(new File(dateiname));
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String dateiname = String.valueOf(aktuellerOrdner) + FahrzeugakteAO.this.liste.getSelectedValue().toString();
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonHochladen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FahrzeugakteAO.this.chooser = new JFileChooser();
                int returnVal = FahrzeugakteAO.this.chooser.showOpenDialog(FahrzeugakteAO.this.chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + FahrzeugakteAO.this.chooser.getSelectedFile().getPath()));
                }
                try {
                    int fID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    String name = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + FahrzeugakteAO.this.chooser.getSelectedFile().getName();
                    if (new File(name).exists()) {
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                        if (msg == 0) {
                            logging.logInfo((Object)"Datei existiert bereits und Benutzer m\u00f6chte sie ersetzen...");
                            Datei.copyFileAusf\u00fchren((File)new File(FahrzeugakteAO.this.chooser.getSelectedFile().getPath()), (String)name);
                        }
                    } else {
                        logging.logInfo((Object)"Datei existiert nicht, es wird kopiert");
                        Datei.copyFileAusf\u00fchren((File)new File(FahrzeugakteAO.this.chooser.getSelectedFile().getPath()), (String)name);
                    }
                    File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID);
                    File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                    String[] fileName = new String[dateilisteBeteiligung.length];
                    int i = 0;
                    while (i < dateilisteBeteiligung.length) {
                        fileName[i] = dateilisteBeteiligung[i].getName();
                        ++i;
                    }
                    aktuellerOrdner = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/";
                    FahrzeugakteAO.this.liste.setListData(fileName);
                }
                catch (IOException e) {
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

