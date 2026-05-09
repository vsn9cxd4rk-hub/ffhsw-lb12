/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.Datei
 */
package ao.mitglieder;

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
import java.sql.SQLException;
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
import utilities.Utils;

public class MitgliederakteAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAnsehen;
    private JButton buttonDrucken;
    private JButton buttonAlsEMailSenden;
    private JButton buttonKommentar;
    private JButton buttonHochladen;
    private JFileChooser chooser;
    public static JList liste;
    private JScrollPane pane_liste;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private static String aktuellerOrdner;

    public MitgliederakteAO() {
        super("FeuerwehrManagementSystem - Mitgliederakte");
        logging.logInfo((Object)"Starte: AttachmentAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAnsehen = new JButton("\u00d6ffnen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonAlsEMailSenden = new JButton("Als E-Mail senden");
        this.buttonKommentar = new JButton("Kommentar einf\u00fcgen");
        this.buttonHochladen = new JButton("Datei einf\u00fcgen");
        this.modulBeschreibung = new JLabel("Mitgliederakte");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        tree = new JTree(CreateTrees.CreateTreeMitgliederListe(null));
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
    }

    protected void setzeAuswahllisten() {
        liste = new JList();
        liste.setVisibleRowCount(15);
        liste.setToolTipText("Liste der verf\u00fcgbaren Anh\u00e4nge");
        this.pane_liste = new JScrollPane(liste);
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
        this.setTitle("FeuerwehrManagementSystem - Mitgliederakte");
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
        this.add(this.buttonKommentar);
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
                    int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    File ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID);
                    File[] files = ordner.listFiles();
                    String[] fileName = new String[files.length];
                    int i = 0;
                    while (i < files.length) {
                        fileName[i] = files[i].getName();
                        ++i;
                    }
                    aktuellerOrdner = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/";
                    liste.setListData(fileName);
                }
                catch (StringIndexOutOfBoundsException ex) {
                    tree.expandPath(tree.getSelectionPath());
                    liste.setListData(new String[0]);
                }
                catch (NullPointerException ex) {
                    liste.setListData(new String[0]);
                }
                catch (NumberFormatException ex) {
                    liste.setListData(new String[0]);
                }
            }
        });
        liste.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    MitgliederakteAO.this.buttonAnsehen.doClick();
                }
            }
        });
        this.buttonKommentar.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    Steuerung.setStatus(Status.MITGLIEDERAKTE_KOMMENTAR);
                    Steuerung.steuerung();
                }
                catch (StringIndexOutOfBoundsException e) {
                    tree.expandPath(tree.getSelectionPath());
                }
                catch (NullPointerException e) {
                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                }
            }
        });
        this.buttonAlsEMailSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    runApplication.mailData.setStatus(1);
                    runApplication.mailData.setAnhang(String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString() + ",");
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
                    String dateiname = String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString();
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
                    String dateiname = String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString();
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
                MitgliederakteAO.this.chooser = new JFileChooser();
                int returnVal = MitgliederakteAO.this.chooser.showOpenDialog(MitgliederakteAO.this.chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + MitgliederakteAO.this.chooser.getSelectedFile().getPath()));
                }
                try {
                    int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    String mitgliederAktePath = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + MitgliederakteAO.this.chooser.getSelectedFile().getName();
                    if (new File(mitgliederAktePath).exists()) {
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                        if (msg == 0) {
                            logging.logInfo((Object)"Datei existiert bereits und Benutzer m\u00f6chte sie ersetzen...");
                            Datei.copyFileAusf\u00fchren((File)new File(MitgliederakteAO.this.chooser.getSelectedFile().getPath()), (String)mitgliederAktePath);
                            Utils.dateiKatalogisieren(mitgliederAktePath);
                        }
                    } else {
                        logging.logInfo((Object)"Datei existiert nicht, es wird kopiert");
                        Datei.copyFileAusf\u00fchren((File)new File(MitgliederakteAO.this.chooser.getSelectedFile().getPath()), (String)mitgliederAktePath);
                        Utils.dateiKatalogisieren(mitgliederAktePath);
                    }
                    File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID);
                    File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                    String[] fileName = new String[dateilisteBeteiligung.length];
                    int i = 0;
                    while (i < dateilisteBeteiligung.length) {
                        fileName[i] = dateilisteBeteiligung[i].getName();
                        ++i;
                    }
                    aktuellerOrdner = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/";
                    liste.setListData(fileName);
                }
                catch (IOException | SQLException e) {
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

