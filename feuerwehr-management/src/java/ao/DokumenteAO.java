package ao;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.Protokoll;
import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.ProtokollPDFScheiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Datei;
import utilities.Konstante;
import utilities.Utils;

public class DokumenteAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private StringBuilder aktuellerOrdner;
   private JButton buttonZurueck;
   private JButton buttonAnsehen;
   private JButton buttonHochladen;
   private JButton buttonEntfernen;
   private JButton buttonSendenEMail;
   private JButton buttonSpeichernUnter;
   private JList liste;
   private JTextField ordnerLeiste;
   private JScrollPane pane_liste;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private JFileChooser chooser;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;


   public DokumenteAO() {
      super("FeuerwehrManagementSystem - Dokumente");
      logging.logInfo("Starte: DokumentenAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonAnsehen = new JButton("Öffnen");
      this.buttonHochladen = new JButton("Einfügen");
      this.buttonEntfernen = new JButton("Löschen");
      this.buttonSendenEMail = new JButton("Als E-Mail senden");
      this.buttonSpeichernUnter = new JButton("Speichern unter");
      this.chooser = new JFileChooser();
      this.modulBeschreibung = new JLabel("Liste der Dokumente");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
      this.ordnerLeiste = new JTextField("Ordner Liste", 83);
      tree = new JTree(CreateTrees.CreateTreeDokumentenListe());
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      tree.setSelectionRow(0);
   }

   protected void setzeAuswahllisten() {
      this.liste = new JList();
      this.liste.setVisibleRowCount(15);
      this.liste.setToolTipText("Liste der verfügbaren Berichte");
      this.pane_liste = new JScrollPane(this.liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
   }

   protected void boxenHinzufuegen() {
      if(runApplication.ftpDownloadLäuft != 0) {
         JOptionPane.showMessageDialog((Component)null, Konstante.FTP_DOWNLOAD_LAEUFT, "Hinweis", 2);
      }

   }

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Dokumente");
      this.setSize(950, 670);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.ordnerLeiste);
      this.scrollPaneTree.setPreferredSize(new Dimension(300, 450));
      this.add(this.scrollPaneTree);
      this.pane_liste.setPreferredSize(new Dimension(600, 450));
      this.add(this.pane_liste);
      this.add(this.dummy3);
      this.add(this.buttonAnsehen);
      this.add(this.buttonHochladen);
      this.add(this.buttonEntfernen);
      this.add(this.buttonSpeichernUnter);

      try {
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
            this.add(this.buttonSendenEMail);
         }
      } catch (NumberFormatException var2) {
         logging.logPrintStackTrace(var2);
      }

      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.buttonHochladen.setVisible(false);
      this.buttonEntfernen.setVisible(false);
      this.buttonSpeichernUnter.setVisible(false);
      this.ordnerLeiste.setEditable(false);
   }

   protected void labelHinzufuegen() {}

   protected void labelErstellen() {}

   private void erstelleOrdnerLeiste() {
      StringBuilder build = new StringBuilder();
      this.aktuellerOrdner = new StringBuilder();
      Object[] treeListe = tree.getSelectionPath().getPath();
      build.append("data >> ");
      this.aktuellerOrdner.append("data/");

      for(int o = 1; o < treeListe.length; ++o) {
         build.append(treeListe[o]);
         if(treeListe.length - 1 != o) {
            build.append(" >> ");
         }

         if(treeListe[o].toString().equals("Briefe")) {
            this.aktuellerOrdner.append("Brief");
         } else if(treeListe[o].toString().equals("Mängelmeldungen")) {
            this.aktuellerOrdner.append("Mangel");
         } else if(treeListe[o].toString().equals("Verdienstausfillbescheinigungen")) {
            this.aktuellerOrdner.append("Verdienstausfall");
         } else if(treeListe[o].toString().equals("Beteiligungsübersicht")) {
            this.aktuellerOrdner.append("Beteiligung_uebersicht");
         } else {
            this.aktuellerOrdner.append(treeListe[o]);
         }

         this.aktuellerOrdner.append("/");
      }

      this.ordnerLeiste.setText(build.toString());
      logging.logInfo("Aktueller Ordner: " + runApplication.arbeitsverzeichnis + this.aktuellerOrdner.toString());
   }

   private String[] prepareFileNameForList(File[] files) {
      String[] fileName = new String[files.length];

      for(int i = 0; i < files.length; ++i) {
         fileName[i] = files[i].getName();
      }

      return fileName;
   }

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               DokumenteAO.this.buttonAnsehen.doClick();
            }

         }
      });
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            Object[] jahresAuswahl = DokumenteAO.tree.getSelectionPath().getPath();
            DokumenteAO.this.erstelleOrdnerLeiste();
            DokumenteAO.this.buttonHochladen.setVisible(false);
            DokumenteAO.this.buttonEntfernen.setVisible(false);
            DokumenteAO.this.buttonSpeichernUnter.setVisible(false);
            if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
               TabelleJahresbericht ordnerBeteiligung = new TabelleJahresbericht();

               try {
                  int dateilisteBeteiligung = Integer.parseInt(jahresAuswahl[1].toString());
                  String[] berichteListe = Utils.listToArray(ordnerBeteiligung.getAllVerfügbarenBerichte(dateilisteBeteiligung));
                  DokumenteAO.this.liste.setListData(berichteListe);
               } catch (SQLException var10) {
                  logging.logPrintStackTrace(var10);
               }

               DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
            } else {
               File ordnerBeteiligung1;
               File[] dateilisteBeteiligung1;
               if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Fahrzeugeinteilung")) {
                  try {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     if(dateilisteBeteiligung1.length != 0) {
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     } else {
                        DokumenteAO.this.liste.setListData(new String[0]);
                     }
                  } catch (NullPointerException var9) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.KEIN_DOKUMENT_VORHANDEN);
                  }
               } else {
                  String[] dateilisteBeteiligung2;
                  if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                     TabelleVeranstaltung ordnerBeteiligung2 = new TabelleVeranstaltung();

                     try {
                        dateilisteBeteiligung2 = Utils.listToArray(ordnerBeteiligung2.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(jahresAuswahl[1].toString()), 1));
                        DokumenteAO.this.liste.setListData(dateilisteBeteiligung2);
                     } catch (SQLException var8) {
                        logging.logPrintStackTrace(var8);
                     }

                     DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Briefe")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     DokumenteAO.this.buttonEntfernen.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Beteiligungsübersicht")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     if(dateilisteBeteiligung1.length != 0) {
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     } else {
                        DokumenteAO.this.liste.setListData(new Object[0]);
                     }
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Schichten")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     if(dateilisteBeteiligung1.length != 0) {
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     } else {
                        DokumenteAO.this.liste.setListData(new Object[0]);
                     }
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Verdienstausfallbescheinigung")) {
                     Steuerung.setStatus(Status.VERDIENSTAUSFALL);
                     Steuerung.steuerung();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Mängelmeldungen")) {
                     TabelleMaengelmeldung ordnerBeteiligung3 = new TabelleMaengelmeldung();

                     try {
                        dateilisteBeteiligung2 = Utils.listToArray(ordnerBeteiligung3.getWann(Integer.parseInt(jahresAuswahl[1].toString())));
                        DokumenteAO.this.liste.setListData(dateilisteBeteiligung2);
                     } catch (SQLException var7) {
                        logging.logPrintStackTrace(var7);
                     }

                     DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Eigene Dateien")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     DokumenteAO.this.buttonHochladen.setVisible(true);
                     DokumenteAO.this.buttonEntfernen.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Ausbildungsunterlagen")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     DokumenteAO.this.buttonHochladen.setVisible(true);
                     DokumenteAO.this.buttonEntfernen.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Bestandsliste")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     DokumenteAO.this.buttonEntfernen.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Lehrgangsmeldungen")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     if(dateilisteBeteiligung1.length != 0) {
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     } else {
                        DokumenteAO.this.liste.setListData(new String[0]);
                     }
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     if(dateilisteBeteiligung1.length != 0) {
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     } else {
                        DokumenteAO.this.liste.setListData(new String[0]);
                     }

                     DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                     try {
                        TabelleProtokoll ordnerBeteiligung4 = new TabelleProtokoll();
                        dateilisteBeteiligung2 = Utils.listToArray(ordnerBeteiligung4.getAlleTitel(Integer.parseInt(jahresAuswahl[1].toString())));
                        DokumenteAO.this.liste.setListData(dateilisteBeteiligung2);
                        DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                     } catch (SQLException var6) {
                        logging.logPrintStackTrace(var6);
                     }
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Atemschutz")) {
                     ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
                     dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
                     DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
                     DokumenteAO.this.buttonHochladen.setVisible(true);
                     DokumenteAO.this.buttonEntfernen.setVisible(true);
                  }
               }
            }

         }
      });
      this.buttonSendenEMail.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleJahresbericht tabBericht = new TabelleJahresbericht();
            TabelleEinsatz_bericht tabEinsatzBericht = new TabelleEinsatz_bericht();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
            TabelleProtokoll tabProtokoll = new TabelleProtokoll();

            try {
               runApplication.mailData.setStatus(1);
               if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                  runApplication.mailData.setAnhang(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + tabBericht.getBerichtDateiname(DokumenteAO.this.liste.getSelectedValue().toString()));
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                  runApplication.mailData.setAnhang(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + tabEinsatzBericht.getDateiname(tabVeranstaltung.getVeranstaltungID(DokumenteAO.this.liste.getSelectedValue().toString())) + ",");
                  runApplication.mailData.setAn((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtAN"));
                  runApplication.mailData.setCc((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtCC"));
                  runApplication.mailData.setBcc((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtBCC"));
                  runApplication.mailData.setBetreff("Einsatzbericht: " + DokumenteAO.this.liste.getSelectedValue().toString());
                  runApplication.mailData.seteMailText((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzbericht") + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Mängelmeldungen")) {
                  runApplication.mailData.setAn((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungAN"));
                  runApplication.mailData.setCc((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungCC"));
                  runApplication.mailData.setBcc((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungBCC"));
                  runApplication.mailData.setBetreff("Mängelmeldung: " + DokumenteAO.this.liste.getSelectedValue().toString());
                  runApplication.mailData.seteMailText((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldung") + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
                  runApplication.mailData.setAnhang(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + tabMangel.getDateiname(DokumenteAO.this.liste.getSelectedValue().toString()) + ",");
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                  int e = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                  String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, e);
                  int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                  new Protokoll();
                  Protokoll protokoll = tabProtokoll.getData(vID);
                  String dateiname = runApplication.arbeitsverzeichnis + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                  ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                  Utils.dateiKatalogisieren(dateiname);
                  runApplication.mailData.setAnhang(dateiname);
               } else {
                  runApplication.mailData.setAnhang(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString() + ",");
               }

               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (SQLException var12) {
               logging.logPrintStackTrace(var12);
            } catch (NullPointerException var13) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
            } catch (DocumentException var14) {
               logging.logPrintStackTrace(var14);
            } catch (IOException var15) {
               logging.logPrintStackTrace(var15);
            }

         }
      });
      this.buttonSpeichernUnter.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               DokumenteAO.this.chooser = new JFileChooser();
               DokumenteAO.this.chooser.setFileSelectionMode(1);
               DokumenteAO.this.chooser.showSaveDialog((Component)null);
               String e = null;
               String outout = null;
               if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Mängelmeldungen")) {
                  e = (new TabelleMaengelmeldung()).getDateiname(DokumenteAO.this.liste.getSelectedValue().toString());
                  outout = DokumenteAO.this.chooser.getSelectedFile().getPath() + "/" + e;
                  Datei.copyFileAusführen(new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + e), outout);
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                  e = (new TabelleEinsatz_bericht()).getDateiname((new TabelleVeranstaltung()).getVeranstaltungID(DokumenteAO.this.liste.getSelectedValue().toString()));
                  outout = DokumenteAO.this.chooser.getSelectedFile().getPath() + "/" + e;
                  Datei.copyFileAusführen(new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + e), outout);
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                  e = (new TabelleJahresbericht()).getBerichtDateiname(DokumenteAO.this.liste.getSelectedValue().toString());
                  outout = DokumenteAO.this.chooser.getSelectedFile().getPath() + "/" + e;
                  Datei.copyFileAusführen(new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + e), outout);
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                  e = DokumenteAO.this.liste.getSelectedValue().toString();
                  outout = DokumenteAO.this.chooser.getSelectedFile().getPath() + "/" + e;
                  Datei.copyFileAusführen(new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + e), outout);
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                  TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                  TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                  int komma = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                  String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, komma);
                  int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                  new Protokoll();
                  Protokoll protokoll = tabProtokoll.getData(vID);
                  String dateiname = runApplication.arbeitsverzeichnis + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                  ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                  Utils.dateiKatalogisieren(dateiname);
                  outout = DokumenteAO.this.chooser.getSelectedFile().getPath() + "/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                  Datei.copyFileAusführen(new File(dateiname), outout);
               }

               logging.logInfo("Datei wurde erfolgreich kopiert");
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            } catch (SQLException var11) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var11);
            } catch (IOException var12) {
               JOptionPane.showMessageDialog((Component)null, Konstante.DATEI_NICHT_GEFUNDEN, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var12);
            } catch (DocumentException var13) {
               logging.logPrintStackTrace(var13);
            }

         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleJahresbericht tabBericht = new TabelleJahresbericht();
            Object[] jahresAuswahl = DokumenteAO.tree.getSelectionPath().getPath();
            String dateiname = null;

            try {
               if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + tabBericht.getBerichtDateiname((String)DokumenteAO.this.liste.getSelectedValue());
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Fahrzeugeinteilung")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Briefe")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Beteiligungsübersicht")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Schichten")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
               } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                  dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
               } else {
                  TabelleVeranstaltung tabVeranstaltung;
                  if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                     TabelleEinsatz_bericht e = new TabelleEinsatz_bericht();
                     tabVeranstaltung = new TabelleVeranstaltung();
                     dateiname = runApplication.arbeitsverzeichnis + "data/" + jahresAuswahl[1].toString() + "/einsatzberichte/" + e.getDateiname(tabVeranstaltung.getVeranstaltungID((String)DokumenteAO.this.liste.getSelectedValue()));
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Mängelmeldungen")) {
                     TabelleMaengelmeldung e1 = new TabelleMaengelmeldung();
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + e1.getDateiname((String)DokumenteAO.this.liste.getSelectedValue());
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Eigene Dateien")) {
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Atemschutz")) {
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Ausbildungsunterlagen")) {
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Bestandsliste")) {
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Lehrgangsmeldungen")) {
                     dateiname = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                  } else if(DokumenteAO.tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                     TabelleProtokoll e2 = new TabelleProtokoll();
                     tabVeranstaltung = new TabelleVeranstaltung();
                     int komma = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                     String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, komma);
                     int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                     new Protokoll();
                     Protokoll protokoll = e2.getData(vID);
                     dateiname = runApplication.arbeitsverzeichnis + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                     ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                     Utils.dateiKatalogisieren(dateiname);
                  }
               }

               Desktop.getDesktop().open(new File(dateiname));
               (new TabelleFTPSync()).updateFTPSync_StatusResert(dateiname, runApplication.clientID);
            } catch (DocumentException var11) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var11);
            }

         }
      });
      this.buttonHochladen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            DokumenteAO.this.chooser = new JFileChooser();
            DokumenteAO.this.chooser.setFileSelectionMode(2);
            int returnVal = DokumenteAO.this.chooser.showOpenDialog(DokumenteAO.this.chooser);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + DokumenteAO.this.chooser.getSelectedFile().getPath());
            }

            try {
               String ordnerBeteiligung = runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.chooser.getSelectedFile().getName();
               if((new File(ordnerBeteiligung)).exists()) {
                  int dateilisteBeteiligung = JOptionPane.showConfirmDialog((Component)null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                  if(dateilisteBeteiligung == 0) {
                     logging.logInfo("Datei existiert bereits und Benutzer möchte sie ersetzen...");
                     Datei.copyFileAusführen(new File(DokumenteAO.this.chooser.getSelectedFile().getPath()), ordnerBeteiligung);
                  }
               } else {
                  logging.logInfo("Datei existiert nicht, es wird kopiert");
                  Datei.copyFileAusführen(new File(DokumenteAO.this.chooser.getSelectedFile().getPath()), ordnerBeteiligung);
               }

               Utils.dateiKatalogisieren(ordnerBeteiligung);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

            File ordnerBeteiligung1 = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString());
            File[] dateilisteBeteiligung1 = ordnerBeteiligung1.listFiles();
            DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung1));
         }
      });
      this.buttonEntfernen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            File file = new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString());

            try {
               int e = JOptionPane.showConfirmDialog((Component)null, Konstante.WIRKLICH_LOESCHEN, "Frage", 0);
               if(e == 0) {
                  TabelleFTPSync tabSync = new TabelleFTPSync();
                  Datei.copyFileAusführen(file, runApplication.arbeitsverzeichnis + "data/papierkorb/" + file.getName());
                  file.delete();
                  Utils.dateiKatalogisierenForDelete(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString());
                  tabSync.deleteOneFile(Utils.removeBackSlashFromString(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString()));
                  logging.logInfo("Datei: " + file.toString() + " wurde in den Papierkorb verschoben");
                  File[] dateilisteBeteiligung = (new File(runApplication.arbeitsverzeichnis + DokumenteAO.this.aktuellerOrdner.toString())).listFiles();
                  DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
               }
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
