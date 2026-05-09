/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.karte;

import ao.AbstractFenster;
import ao.utils.StartBildschirmAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.karte.TabelleStrassen;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import listener.DisposeListener;
import listener.DruckenListener;
import listener.ImportListener;
import logging.logging;
import pdfdocumente.karte.PDFHydrantenverzeichnis;
import run.runApplication;
import service.BerechtigunsManager;
import service.HydrantService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class KarteAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonStra\u00dfeAktualisieren;
    private JButton buttonStra\u00dfeEintragen;
    private JButton buttonHydrantEintargen;
    private JButton buttonImport;
    private JButton buttonDrucken;
    private JButton buttonGro\u00df;
    private JButton buttonKlein;
    private JButton buttonZurueck;
    private JButton hydrantenverzeichnisDrucken;
    private JLabel \u00fcberschriftstrassensuche;
    public static JComboBox<String> StrasseSuchen;
    private String strassendb;
    public static JTextArea anfahrtInfo;
    private JScrollPane anfahrtInfoPane;
    private JLabel anfahrtInfo_label;
    public static JTextArea hydrant;
    private JLabel hydrant_label;
    private JScrollPane hydrantPane;
    public static JTextArea koordinaten;
    private JLabel koordinaten_label;
    private JScrollPane koordinatenPane;
    public static JTextArea stra\u00dfenInfo;
    private JLabel stra\u00dfenInfo_label;
    private JScrollPane stra\u00dfenInfoPane;
    public static JTextArea alamierungsInfo;
    private JLabel alarmierungsInfo_label;
    private JScrollPane alamierungsInfoPane;
    private JLabel karte;
    private JTextField fieldSuche;
    private JLabel fieldSuche_label;
    public static JEditorPane browserFenster;
    private JLabel dummy;
    private JLabel dummy1;
    private JPanel panel16_9;
    private JPanel panel4_3;
    private JPanel panelkarte;
    private HydrantService service;

    public KarteAO() {
        super("FeuerwehrManagementSystem Version: 3.21");
        logging.logInfo((Object)"Starte: HauptprogrammAO");
    }

    protected void buttonErstellen() {
        this.service = new HydrantService();
        this.buttonStra\u00dfeAktualisieren = new JButton("Stra\u00dfe beareiten");
        this.buttonStra\u00dfeEintragen = new JButton("Stra\u00dfe anlegen");
        this.buttonHydrantEintargen = new JButton("Hydranten eintragen");
        this.buttonImport = new JButton("Import");
        this.buttonGro\u00df = new JButton("Ansicht kleiner (+)");
        this.buttonGro\u00df.setToolTipText("Ansicht hereinzoomen");
        this.buttonKlein = new JButton("Ansicht gr\u00f6\u00dfer (-)");
        this.buttonKlein.setToolTipText("Ansicht herauszoomen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.hydrantenverzeichnisDrucken = new JButton("Hydrantenverzeichnis Drucken");
        this.\u00fcberschriftstrassensuche = new JLabel("Direkt Suche:");
        this.\u00fcberschriftstrassensuche.setFont(new Font("Arial", 1, 26));
        this.alarmierungsInfo_label = new JLabel("Alamierungsinfo:");
        this.alarmierungsInfo_label.setFont(new Font("Arial", 1, 16));
        this.anfahrtInfo_label = new JLabel("Anfahrt:");
        this.anfahrtInfo_label.setFont(new Font("Arial", 1, 16));
        this.hydrant_label = new JLabel("Hydranten:");
        this.hydrant_label.setFont(new Font("Arial", 1, 16));
        this.koordinaten_label = new JLabel("Koordinaten Stadtplan:");
        this.koordinaten_label.setFont(new Font("Arial", 1, 16));
        this.stra\u00dfenInfo_label = new JLabel("Stra\u00dfen Info:");
        this.stra\u00dfenInfo_label.setFont(new Font("Arial", 1, 16));
        this.fieldSuche = new JTextField(46);
        this.fieldSuche_label = new JLabel("Text Suche: ");
        this.fieldSuche_label.setFont(new Font("Arial", 1, 26));
        this.fieldSuche.setPreferredSize(new Dimension(500, 35));
        anfahrtInfo = new JTextArea(4, 28);
        this.anfahrtInfoPane = new JScrollPane(anfahrtInfo);
        this.anfahrtInfoPane.setVerticalScrollBarPolicy(22);
        hydrant = new JTextArea(4, 28);
        this.hydrantPane = new JScrollPane(hydrant);
        this.hydrantPane.setVerticalScrollBarPolicy(22);
        koordinaten = new JTextArea(4, 28);
        this.koordinatenPane = new JScrollPane(koordinaten);
        this.koordinatenPane.setVerticalScrollBarPolicy(22);
        alamierungsInfo = new JTextArea(4, 28);
        this.alamierungsInfoPane = new JScrollPane(alamierungsInfo);
        this.alamierungsInfoPane.setVerticalScrollBarPolicy(22);
        stra\u00dfenInfo = new JTextArea(4, 28);
        this.stra\u00dfenInfoPane = new JScrollPane(stra\u00dfenInfo);
        this.stra\u00dfenInfoPane.setVerticalScrollBarPolicy(22);
        URL url = ((Object)((Object)this)).getClass().getClassLoader().getResource("images/EigeneStadt.jpg");
        ImageIcon bildEigeneStadt = new ImageIcon(url);
        this.karte = new JLabel(bildEigeneStadt);
        browserFenster = new JEditorPane();
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy1 = new JLabel(runApplication.dummyImage);
        this.panel16_9 = new JPanel();
        this.panel4_3 = new JPanel();
        this.panelkarte = new JPanel();
    }

    protected void setzeAuswahllisten() {
        TabelleStrassen dbsta\u00dfen = new TabelleStrassen();
        String[] projektindexdb = null;
        this.strassendb = null;
        try {
            projektindexdb = Utils.listToArrayOnlyFORComboBoxes(dbsta\u00dfen.getStra\u00dfenListe());
        }
        catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Der Datenbank Zugiff ist Fehlgeschlagen\nBitte \u00fcberpr\u00fcfen Sie ob der Datenbank Service l\u00e4uft.", "Fehlermeldung", 0);
        }
        StrasseSuchen = new JComboBox<String>(projektindexdb);
        StrasseSuchen.setPreferredSize(new Dimension(500, 35));
        StrasseSuchen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    KarteAO.this.strassendb = (String)StrasseSuchen.getSelectedItem();
                    TabelleStrassen stra\u00dfendata = new TabelleStrassen();
                    try {
                        if (stra\u00dfendata.getStrassenCount((String)StrasseSuchen.getSelectedItem()) != 1) {
                            JOptionPane.showMessageDialog(null, Konstante.STRA\u00dfE_NICHT_GEFUNDEN, "Warnung", 2);
                        } else {
                            anfahrtInfo.setText(stra\u00dfendata.getAnfahrtInfo((String)StrasseSuchen.getSelectedItem()));
                            stra\u00dfenInfo.setText(stra\u00dfendata.getStra\u00dfenInfo((String)StrasseSuchen.getSelectedItem()));
                            hydrant.setText(KarteAO.this.service.getHydrantListe((String)StrasseSuchen.getSelectedItem()));
                            koordinaten.setText(stra\u00dfendata.getStrassenKoordinaten((String)StrasseSuchen.getSelectedItem()));
                            ImageIcon neuekarte = new ImageIcon(stra\u00dfendata.getStrassenBild((String)StrasseSuchen.getSelectedItem()));
                            File neuekarteFile = new File(stra\u00dfendata.getStrassenBild((String)StrasseSuchen.getSelectedItem()));
                            if (!neuekarteFile.exists()) {
                                URL url2 = this.getClass().getClassLoader().getResource("images/noImage.jpg");
                                ImageIcon bildKeineKarte = new ImageIcon(url2);
                                KarteAO.this.karte.setIcon(bildKeineKarte);
                                KarteAO.this.buttonGro\u00df.setEnabled(false);
                                KarteAO.this.buttonKlein.setEnabled(false);
                            } else {
                                KarteAO.this.karte.setIcon(neuekarte);
                                KarteAO.this.buttonGro\u00df.setEnabled(false);
                                KarteAO.this.buttonKlein.setEnabled(true);
                            }
                        }
                    }
                    catch (SQLException e1) {
                        logging.logPrintStackTrace((Exception)e1);
                    }
                }
            }
        });
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Einsatzgebiet");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
    }

    protected void boxenHinzufuegen() {
        this.buttonGro\u00df.setEnabled(false);
        this.buttonKlein.setEnabled(false);
        this.add(this.\u00fcberschriftstrassensuche);
        this.add(StrasseSuchen);
        this.add(this.buttonGro\u00df);
        this.add(this.buttonKlein);
        this.add(this.dummy);
        Dimension bildschirmgr\u00f6\u00dfe = Toolkit.getDefaultToolkit().getScreenSize();
        if (bildschirmgr\u00f6\u00dfe.getWidth() >= 1440.0 && bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
            logging.logInfo((Object)"Starte GUI mit: gr\u00f6\u00dfer 1440x900");
            this.setSize(1366, 828);
            this.panel16_9 = new JPanel(new GridLayout(10, 1));
            this.getContentPane().add("Center", this.panel16_9);
            this.panel16_9.add(this.alarmierungsInfo_label);
            this.panel16_9.add(this.alamierungsInfoPane);
            this.panel16_9.add(this.stra\u00dfenInfo_label);
            this.panel16_9.add(this.stra\u00dfenInfoPane);
            this.panel16_9.add(this.anfahrtInfo_label);
            this.panel16_9.add(this.anfahrtInfoPane);
            this.panel16_9.add(this.hydrant_label);
            this.panel16_9.add(this.hydrantPane);
            this.panel16_9.add(this.koordinaten_label);
            this.panel16_9.add(this.koordinatenPane);
        } else if (bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            logging.logInfo((Object)"Starte GUI mit: 1366x768");
            this.setSize(1366, 768);
            this.panel16_9 = new JPanel(new GridLayout(8, 1));
            this.getContentPane().add("Center", this.panel16_9);
            this.panel16_9.add(this.stra\u00dfenInfo_label);
            this.panel16_9.add(this.stra\u00dfenInfoPane);
            this.panel16_9.add(this.anfahrtInfo_label);
            this.panel16_9.add(this.anfahrtInfoPane);
            this.panel16_9.add(this.hydrant_label);
            this.panel16_9.add(this.hydrantPane);
            this.panel16_9.add(this.koordinaten_label);
            this.panel16_9.add(this.koordinatenPane);
        } else if (bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            logging.logInfo((Object)"Starte GUI mit: 1280x1024");
            this.setSize(1280, 1024);
            this.panel4_3 = new JPanel(new GridLayout(4, 2));
            this.getContentPane().add("Center", this.panel4_3);
            this.panel4_3.add(this.stra\u00dfenInfo_label);
            this.panel4_3.add(this.stra\u00dfenInfoPane);
            this.panel4_3.add(this.anfahrtInfo_label);
            this.panel4_3.add(this.anfahrtInfoPane);
            this.panel4_3.add(this.hydrant_label);
            this.panel4_3.add(this.hydrantPane);
            this.panel4_3.add(this.koordinaten_label);
            this.panel4_3.add(this.koordinatenPane);
        } else {
            JOptionPane.showMessageDialog(null, Konstante.AUFLOESUNG_NICHT_UNTERSTUETZT, "Warnung", 2);
        }
        this.panelkarte = new JPanel(new GridLayout(1, 1));
        this.getContentPane().add("Center", this.panelkarte);
        this.panelkarte.add(this.karte);
        this.add(this.dummy1);
        this.add(this.buttonZurueck);
        this.add(this.buttonDrucken);
        this.add(this.buttonStra\u00dfeEintragen);
        this.add(this.buttonStra\u00dfeAktualisieren);
        this.add(this.buttonHydrantEintargen);
        this.add(this.hydrantenverzeichnisDrucken);
    }

    protected void labelErstellen() {
        alamierungsInfo.setEditable(false);
        alamierungsInfo.setLineWrap(true);
        alamierungsInfo.setWrapStyleWord(true);
        stra\u00dfenInfo.setEditable(false);
        stra\u00dfenInfo.setLineWrap(true);
        stra\u00dfenInfo.setWrapStyleWord(true);
        anfahrtInfo.setEditable(false);
        anfahrtInfo.setLineWrap(true);
        anfahrtInfo.setWrapStyleWord(true);
        hydrant.setEditable(false);
        hydrant.setLineWrap(true);
        hydrant.setWrapStyleWord(true);
        koordinaten.setEditable(false);
        koordinaten.setLineWrap(true);
        koordinaten.setWrapStyleWord(true);
        if (BerechtigunsManager.ber[52] == 1) {
            this.buttonImport.setVisible(true);
            this.buttonStra\u00dfeAktualisieren.setVisible(true);
            this.buttonStra\u00dfeEintragen.setVisible(true);
            this.buttonHydrantEintargen.setVisible(true);
        } else {
            this.buttonImport.setVisible(false);
            this.buttonStra\u00dfeAktualisieren.setVisible(false);
            this.buttonStra\u00dfeEintragen.setVisible(false);
            this.buttonHydrantEintargen.setVisible(false);
        }
        if (MyEvent.event.equals("0x0100")) {
            this.buttonZurueck.setVisible(false);
        }
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonImport.addActionListener((ActionListener)((Object)new ImportListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener((ActionListener)((Object)new DruckenListener((JFrame)((Object)this))));
        this.hydrantenverzeichnisDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                Thread threadHydrantenverzeichnisErstellen = new Thread(){

                    @Override
                    public void run() {
                        try {
                            String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Hydrantenverzeichnis_" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + ".pdf";
                            Utils.dateiKatalogisieren(dateiname);
                            PDFHydrantenverzeichnis.PDFdocumentErstellen(dateiname);
                            MyEvent.setEvent((String)"0x0030");
                            Desktop.getDesktop().open(new File(dateiname));
                        }
                        catch (DocumentException | IOException | SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                    }
                };
                threadHydrantenverzeichnisErstellen.start();
            }
        });
        this.buttonStra\u00dfeEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0");
                Steuerung.setStatus(Status.STRA\u00dfE_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonGro\u00df.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleStrassen stra\u00dfendata = new TabelleStrassen();
                try {
                    ImageIcon neuekarte = new ImageIcon(stra\u00dfendata.getStrassenBild((String)StrasseSuchen.getSelectedItem()));
                    File neuekarteFile = new File(stra\u00dfendata.getStrassenBild((String)StrasseSuchen.getSelectedItem()));
                    if (!neuekarteFile.exists()) {
                        URL url2 = this.getClass().getClassLoader().getResource("images/noImage.jpg");
                        ImageIcon bildKeineKarte = new ImageIcon(url2);
                        KarteAO.this.karte.setIcon(bildKeineKarte);
                    } else {
                        KarteAO.this.karte.setIcon(neuekarte);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                KarteAO.this.buttonGro\u00df.setEnabled(false);
                KarteAO.this.buttonKlein.setEnabled(true);
            }
        });
        this.buttonKlein.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleStrassen stra\u00dfendata = new TabelleStrassen();
                try {
                    ImageIcon neuekarte = new ImageIcon(stra\u00dfendata.getStrassenBild2((String)StrasseSuchen.getSelectedItem()));
                    File neuekarteFile = new File(stra\u00dfendata.getStrassenBild2((String)StrasseSuchen.getSelectedItem()));
                    if (!neuekarteFile.exists() || neuekarteFile.getPath() == null) {
                        URL url2 = this.getClass().getClassLoader().getResource("images/noImage.jpg");
                        ImageIcon bildKeineKarte = new ImageIcon(url2);
                        KarteAO.this.karte.setIcon(bildKeineKarte);
                    } else {
                        KarteAO.this.karte.setIcon(neuekarte);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                KarteAO.this.buttonGro\u00df.setEnabled(true);
                KarteAO.this.buttonKlein.setEnabled(false);
            }
        });
        this.buttonHydrantEintargen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.HYDRANT_EINTARGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonStra\u00dfeAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0101");
                Steuerung.setStatus(Status.STRA\u00dfE_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                if (MyEvent.event.equals("0x0100")) {
                    KarteAO.this.buttonZurueck.setVisible(false);
                    System.exit(0);
                } else {
                    KarteAO.this.dispose();
                }
            }
        });
    }

    public void fensterAnzeigen() {
        if (MyEvent.event.endsWith("0x0100")) {
            StartBildschirmAO.startDialog.setVisible(false);
        }
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

