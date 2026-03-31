package ao.karte;

import ao.AbstractFenster;
import ao.karte.StraßeEintragenAO;
import ao.utils.ProzessBarAO;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleStrassen;
import go.karte.Hydrant;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import service.DirectionParser;
import service.ImportService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class ImportAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonImportStraßen;
   private JButton buttonImportHydranten;
   private JButton buttonOrdnerStraßenverzeichnis;
   private JButton buttonOrdnerHydrantenverzeichnis;
   private JButton buttonImportHydrantenKoordinaten;
   private JButton buttonExportStraßen;
   private JButton buttonExportHydranten;
   private JButton buttonExportHydrantenOSM_Server;
   private JButton buttonZurueck;
   private JLabel ueberschrift;
   private JLabel label_starßenverzeichnis;
   private JLabel label_hydrantenverzeichnis;
   private JTextArea hinweis;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;
   private JLabel dummy4;
   public static JTextField textfieldstarßen;
   public static JTextField textfieldhydranten;
   private JFileChooser chooser;


   public ImportAO() {
      super("FeuerwehrManagementSystem Version: 4.08");
      logging.logInfo("Starte: ImportAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonImportStraßen = new JButton("Import Straßen");
      this.buttonImportStraßen.setToolTipText("Import ausführen");
      this.buttonImportHydranten = new JButton("Import Wasserentnahmestellen");
      this.buttonImportHydranten.setToolTipText("Import ausführen");
      this.buttonImportHydrantenKoordinaten = new JButton("Import Wasserentnahmestellen Koordinaten");
      this.buttonImportHydrantenKoordinaten.setToolTipText("Lädt as dem Internet die Koordinaten der Hydranten...");
      this.buttonExportStraßen = new JButton("Export Straßen");
      this.buttonExportHydranten = new JButton("Export Wasserentnahmestellen");
      this.buttonExportHydrantenOSM_Server = new JButton("Export Wasserentnahmestellen (OSM-Server)");
      this.buttonExportHydrantenOSM_Server.setToolTipText("Export für den OSM Tile Server, dieser Export kann in die Karte eingebettet werden...");
      this.buttonOrdnerStraßenverzeichnis = new JButton("...");
      this.buttonOrdnerStraßenverzeichnis.setToolTipText("Ordner wählen");
      this.buttonOrdnerHydrantenverzeichnis = new JButton("...");
      this.buttonOrdnerHydrantenverzeichnis.setToolTipText("Ordner wählen");
      this.ueberschrift = new JLabel("Einsatzgebiet - Import / Export");
      this.label_starßenverzeichnis = new JLabel("Straßenverzeichnis:");
      this.label_hydrantenverzeichnis = new JLabel("Hydrantenverzeichnis:");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
      this.dummy4 = new JLabel(runApplication.dummyImage);
      textfieldstarßen = new JTextField(35);
      textfieldhydranten = new JTextField(35);
      this.hinweis = new JTextArea(8, 50);
      this.hinweis.setText("HINWEIS:\nHier kann das Straßen- und Hydrantenverzeichnis importiert werden!\nDer Import erfolgt im CSV Formt (WICHTIG nicht UTF-8!)\n\nKopfzeilen Straße (muss in der ersten Zeile vorhanden sein!):\nStraßenname;Postleitzahl;Stadt;Anfahrt;Informationen;Koordinaten;GPS_N;GPS_O)\n\nKopfzeilen Hydranten (muss in der ersten Zeile vorhanden sein!):\nID;Straße;Beschreibung;Hausnummer;Nenweite;Lage;GPS_N;GPS_O");
      this.hinweis.setLineWrap(true);
      this.hinweis.setEditable(false);
      this.chooser = new JFileChooser();
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Import / Export");
      this.setSize(600, 490);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.ueberschrift);
      this.add(this.dummy);
      this.add(this.hinweis);
      this.add(this.dummy3);
      this.add(this.label_starßenverzeichnis);
      this.add(textfieldstarßen);
      this.add(this.buttonOrdnerStraßenverzeichnis);
      this.add(this.buttonImportStraßen);
      this.add(this.buttonExportStraßen);
      this.add(this.dummy2);
      this.add(this.label_hydrantenverzeichnis);
      this.add(textfieldhydranten);
      this.add(this.buttonOrdnerHydrantenverzeichnis);
      this.add(this.buttonImportHydranten);
      this.add(this.buttonImportHydrantenKoordinaten);
      this.add(this.buttonExportHydranten);
      this.add(this.buttonExportHydrantenOSM_Server);
      this.add(this.dummy4);
      this.add(this.buttonZurueck);
      textfieldstarßen.setEditable(false);
      textfieldhydranten.setEditable(false);
   }

   protected void boxenHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            ImportAO.this.buttonZurueck.doClick();
         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            logging.logInfo("Schließe ImportAO");
            StraßeEintragenAO.tree.setModel(CreateTrees.CreateTreeStraßenHydranten());
            ImportAO.this.dispose();
         }
      });
      this.buttonImportStraßen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Straßen werden Importiert... Bitte haben sie einen Moment Geduld...");
            Thread thread = new Thread() {
               public void run() {
                  try {
                     ImportService.importStraßenDaten(ImportAO.textfieldstarßen.getText());
                  } catch (SQLException var2) {
                     logging.logError("Fehler beim import --> " + var2);
                     logging.logPrintStackTrace(var2);
                  }

               }
            };
            if(ImportAO.textfieldstarßen.getText().equals("")) {
               MyEvent.setEvent("0x0030");
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_IMPORT_DATEI_WAEHLEN, "Warnung", 2);
            } else {
               thread.start();
            }

         }
      });
      this.buttonExportStraßen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Straßen werden Exportiert... Bitte haben sie einen Moment Geduld...");
            Thread thread = new Thread() {
               public void run() {
                  try {
                     TabelleStrassen e = new TabelleStrassen();
                     String dateiname = ImportAO.this.chooser.getSelectedFile().getPath() + "/straßenverzeichnis.csv";
                     File file = new File(dateiname);
                     file.createNewFile();
                     logging.logInfo("Exportiere Straßenverezeichnis in : " + dateiname);
                     String[] liste = Utils.listToArray(e.getStraßenExport());
                     FileWriter writer = new FileWriter(file, true);
                     writer.write("Straßenname;Postleitzahl;Stadt;Anfahrt;Informationen;Koordinaten;GPS_N;GPS_O");
                     writer.write(System.getProperty("line.separator"));

                     for(int i = 0; i < liste.length; ++i) {
                        writer.write(liste[i]);
                        writer.write(System.getProperty("line.separator"));
                     }

                     writer.flush();
                     writer.close();
                     MyEvent.setEvent("0x0030");
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  } catch (SQLException var7) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.EXPORT_FEHELR, "Fehlermeldung", 0);
                     logging.logPrintStackTrace(var7);
                  }

               }
            };
            ImportAO.this.chooser.setFileSelectionMode(1);
            ImportAO.this.chooser.showSaveDialog((Component)null);
            thread.start();
         }
      });
      this.buttonImportHydranten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Wasserentnahmestellen werden Importiert... Bitte Warten...");
            Thread thread = new Thread() {
               public void run() {
                  try {
                     ImportService.importHydrantenDaten(ImportAO.textfieldhydranten.getText());
                  } catch (SQLException var2) {
                     logging.logError("Fehler beim import --> " + var2);
                     logging.logPrintStackTrace(var2);
                  }

               }
            };
            if(ImportAO.textfieldhydranten.getText().equals("")) {
               MyEvent.setEvent("0x0030");
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_IMPORT_DATEI_WAEHLEN, "Warnung", 2);
            } else {
               thread.start();
            }

         }
      });
      this.buttonExportHydranten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Wasserentnahmestellen werden Exportiert... Bitte Warten...");
            Thread thread = new Thread() {
               public void run() {
                  try {
                     TabelleHydranten e = new TabelleHydranten();
                     String dateiname = ImportAO.this.chooser.getSelectedFile().getPath() + "/hydrantenverzeichnis.csv";
                     File file = new File(dateiname);
                     file.createNewFile();
                     logging.logInfo("Exportiere Hydrantenverezeichnis in : " + dateiname);
                     String[] liste = Utils.listToArray(e.getHydrantenExport());
                     FileWriter writer = new FileWriter(file, true);
                     writer.write("ID;Straße;Beschreibung;Hausnummer;Nenweite;Lage;GPS_N;GPS_O");
                     writer.write(System.getProperty("line.separator"));

                     for(int i = 0; i < liste.length; ++i) {
                        writer.write(liste[i]);
                        writer.write(System.getProperty("line.separator"));
                     }

                     writer.flush();
                     writer.close();
                     MyEvent.setEvent("0x0030");
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  } catch (SQLException var7) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.EXPORT_FEHELR, "Fehlermeldung", 0);
                     logging.logPrintStackTrace(var7);
                  }

               }
            };
            ImportAO.this.chooser.setFileSelectionMode(1);
            ImportAO.this.chooser.showSaveDialog((Component)null);
            thread.start();
         }
      });
      this.buttonExportHydrantenOSM_Server.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Wasserentnahmestellen werden Exportiert... Bitte Warten...");
            Thread thread = new Thread() {
               public void run() {
                  try {
                     TabelleHydranten e = new TabelleHydranten();
                     String[] listeNennweiten = Utils.listToArray(e.getAllNennweiten());

                     for(int n = 0; n < listeNennweiten.length; ++n) {
                        String dateiname = ImportAO.this.chooser.getSelectedFile().getPath() + "/hydrantenverzeichnis_" + "FMS" + "_H" + listeNennweiten[n] + ".txt";
                        File file = new File(dateiname);
                        file.createNewFile();
                        logging.logInfo("Exportiere Hydrantenverezeichnis in : " + dateiname);
                        String[] liste = Utils.listToArray(e.getHydrantenExportOSMServer(listeNennweiten[n]));
                        FileWriter writer = new FileWriter(file, true);
                        writer.write("lat\tlon\ttitle\tdescription\ticon\ticonSize\ticonOffset");
                        writer.write(System.getProperty("line.separator"));

                        for(int i = 0; i < liste.length; ++i) {
                           writer.write(liste[i]);
                           writer.write(System.getProperty("line.separator"));
                        }

                        writer.flush();
                        writer.close();
                     }

                     MyEvent.setEvent("0x0030");
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  } catch (SQLException var9) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.EXPORT_FEHELR, "Fehlermeldung", 0);
                     logging.logPrintStackTrace(var9);
                  }

               }
            };
            ImportAO.this.chooser.setFileSelectionMode(1);
            ImportAO.this.chooser.showSaveDialog((Component)null);
            thread.start();
         }
      });
      this.buttonImportHydrantenKoordinaten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.label_bitteWarten.setText("Wasserentnahmestellen GPS-Daten werden Importiert...");
            Thread thread = new Thread() {
               public void run() {
                  TabelleHydranten tabHydranten = new TabelleHydranten();
                  Hydrant hydrant = new Hydrant();

                  try {
                     int[] e = Utils.listToIntArray(tabHydranten.getIDListe());
                     int counterFehler = 0;
                     int counterErfolgreich = 0;

                     for(int i = 0; i < e.length; ++i) {
                        try {
                           String e1 = DirectionParser.DirectionParsers((String)runApplication.EINSTELLUNGEN.get("Stadt") + ", " + tabHydranten.getStraßeUndHausnummerByHydrantID(e[i]));
                           String[] gpsData = e1.split(",");
                           hydrant.setGPS_N(gpsData[0]);
                           hydrant.setGPS_O(gpsData[1]);
                           hydrant.setId(e[i]);
                           tabHydranten.updateHydrantenKoordinaten(hydrant);
                           ++counterErfolgreich;
                        } catch (ArrayIndexOutOfBoundsException var9) {
                           logging.logWarning("NullPointerException od. ArrayIndexOutOfBoundsException --> Straße wurde über die Google API nicht gefunden...");
                           logging.logWarning(var9);
                           ++counterFehler;
                        }

                        ProzessBarAO.progressbar.setValue(100 * i / e.length);
                     }

                     logging.logInfo("GPS-Koordinaten Import erfolgreich beendet");
                     JOptionPane.showMessageDialog((Component)null, Konstante.IMPORT_ERFOLGREICH + "\n\nErfolgreich: " + counterErfolgreich + "\nFehlerhaft: " + counterFehler);
                     MyEvent.setEvent("0x0030");
                  } catch (SQLException var10) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                     logging.logError(var10);
                     logging.logPrintStackTrace(var10);
                  }

               }
            };
            if(((String)runApplication.EINSTELLUNGEN.get("google_api_code")).equals("") | ((String)runApplication.EINSTELLUNGEN.get("default_location")).equals("")) {
               logging.logInfo("Google API oder Gerätehaus Koordinaten fehlen...");
               MyEvent.setEvent("0x0030");
               JOptionPane.showMessageDialog((Component)null, Konstante.GOOGLE_API_KEY, "Warnung", 2);
            } else {
               thread.start();
            }

         }
      });
      this.buttonOrdnerStraßenverzeichnis.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            ImportAO.this.chooser.setFileSelectionMode(2);
            ImportAO.this.chooser.showSaveDialog((Component)null);
            ImportAO.textfieldstarßen.setText(ImportAO.this.chooser.getSelectedFile().getPath());
         }
      });
      this.buttonOrdnerHydrantenverzeichnis.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            ImportAO.this.chooser.setFileSelectionMode(2);
            ImportAO.this.chooser.showSaveDialog((Component)null);
            ImportAO.textfieldhydranten.setText(ImportAO.this.chooser.getSelectedFile().getPath());
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
