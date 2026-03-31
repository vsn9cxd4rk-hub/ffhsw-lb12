package ao.administrator;

import ao.AbstractFenster;
import ao.HauptprogrammAO;
import ao.utils.ProzessBarAO;
import ao.utils.SystemTrayInfo;
import data.tabellen.TabelleAbwesenheit;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleBrandsicherheitswache;
import data.tabellen.TabelleDateisystem;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleEinsatz_bericht_daten;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleSystemwarnung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleMandant;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import data.tabellen.statistik.TabelleStatistikbsw;
import go.Ausbildung_Kategorie;
import go.Ausbildung_Plan;
import go.Einsatz;
import go.Fahrzeug;
import go.Organisation;
import go.StatistikBSW;
import go.StatistikEinsatz;
import go.StatistikSonstigeVeranstaltung;
import go.Veranstaltung;
import go.Veranstaltung_Kategorie;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import run.update.UpdateDatenbank;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.MyProperties;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.facebook.Facebook;
import utilities.joomla.Joomla;

public class AdministratorAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonDebug;
   private JButton buttonLogbuch;
   private JButton buttonFTPRekalkulation;
   private JButton buttonClientVerwaltung;
   private JButton buttonTerminDisplay;
   private JButton buttonMandantInfo;
   private JButton buttonMandantNameAendern;
   private JButton buttonMandantIDAendern;
   private JButton buttonErstelleMitgliederStatistik;
   private JButton joomlaAusbildungsplanAuslagern;
   private JButton joomlaVeranstaltungenAuslagern;
   private JButton joomlaAusbildungsplanOnlineLöschen;
   private JButton joomlaVeranstaltungenOnlineLöschen;
   private JButton buttonLöscheAlleSystemWarnungen;
   private JButton buttonLöscheUnwetterwarnung;
   private JButton buttonEinsätzeAnEinsatzkomponenteAuslagern;
   private JButton buttonEinsatzkomponenteOrganisationen;
   private JButton buttonEinsatzkomponenteFahrzeug;
   private JButton buttonEinsatzkomonenteLöschen;
   private JButton buttonEinsatzkomonenteKompettNeuAuslagern;
   private JButton buttonEinsatzkomponenteEinenEinsatzAuslagern;
   private JButton buttonLöschenKomplett;
   private JButton buttonLöschenAnwesenheit;
   private JButton buttonLöschenAbwesenheit;
   private JComboBox veranstaltung;
   private JComboBox mitgliederGruppe;
   private JLabel veranstaltung_label;
   private JLabel mitgliederGruppe_label;
   private JButton buttonJahrNachtragen;
   private JLabel jahrNachtragen_label;
   private JTextField jahrNachtagen;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelAdministration;
   private JPanel panelJoomla;
   private JPanel panelEinsatzkomponente;
   private JPanel panelVeranstaltung;
   private JPanel panelJahr;
   private JPanel panelMandant;
   private JTabbedPane tabPane;


   public AdministratorAO() {
      super("FeuerwehrManagementSystem - Administrator");
      logging.logInfo("Starte: AdministratorAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonDebug = new JButton("Debugging");
      this.buttonLogbuch = new JButton("Logbuch");
      this.buttonFTPRekalkulation = new JButton("Dateien Katalogisieren");
      this.buttonLöscheAlleSystemWarnungen = new JButton("Alle Systemwarnungen zurücksetzen");
      this.buttonClientVerwaltung = new JButton("Client Verwaltung");
      this.buttonTerminDisplay = new JButton("Termin Display starten");
      this.buttonMandantInfo = new JButton("Mandant Info");
      this.buttonMandantNameAendern = new JButton("Mandant Namen ändern");
      this.buttonMandantIDAendern = new JButton("Mandant ID ändern");
      this.buttonErstelleMitgliederStatistik = new JButton("Erstelle Mitglieder Statistik");
      this.buttonErstelleMitgliederStatistik.setToolTipText("Statistiken: Mitgliederanzahl und Mitgliederdruchschnittsalter werden vom heutigen Tag berechnet");
      this.buttonLöscheUnwetterwarnung = new JButton("Lösche Aktive Unwetterwarnung");
      this.buttonLöschenKomplett = new JButton("Veranstaltung Komplett löschen");
      this.buttonLöschenAnwesenheit = new JButton("Anwesenheit löschen");
      this.buttonLöschenAbwesenheit = new JButton("Abwesenheit löschen");
      this.veranstaltung_label = new JLabel("Veranstaltung: ");
      this.mitgliederGruppe_label = new JLabel("Mitgliedergruppe: ");
      this.buttonJahrNachtragen = new JButton("Jahr anlegen");
      this.jahrNachtagen = new JTextField(20);
      this.jahrNachtragen_label = new JLabel("Jahr Nachtragen: ");
      this.joomlaVeranstaltungenAuslagern = new JButton("Veranstaltungen (Joomla Auslagerung)");
      this.joomlaVeranstaltungenAuslagern.setToolTipText("Alle Veranstaltungen auf Joomla Seite komplett neu auslagern");
      this.joomlaAusbildungsplanAuslagern = new JButton("Ausbildungsplan (Joomla Auslagerung)");
      this.joomlaAusbildungsplanAuslagern.setToolTipText("Gesamten Ausbildungsplan auf Joomla Seite komplett neu auslagern");
      this.joomlaAusbildungsplanOnlineLöschen = new JButton("Alle Ausbildungsplan Online Löschen");
      this.joomlaVeranstaltungenOnlineLöschen = new JButton("Alle Veranstaltungen Online Löschen");
      this.buttonEinsätzeAnEinsatzkomponenteAuslagern = new JButton("Einsatz (Einsatzkomponente)");
      this.buttonEinsätzeAnEinsatzkomponenteAuslagern.setToolTipText("Alle Einsätze an die Einsatzkomponente auslagern");
      this.buttonEinsatzkomponenteOrganisationen = new JButton("Organisationen  (Einsatzkomponente)");
      this.buttonEinsatzkomponenteFahrzeug = new JButton("Fahrzeuge (Einsatzkomponente)");
      this.buttonEinsatzkomonenteLöschen = new JButton("Einsatzkomponente leeren");
      this.buttonEinsatzkomonenteKompettNeuAuslagern = new JButton("Einsatzkomponente (Komplett Auslagern)");
      this.buttonEinsatzkomponenteEinenEinsatzAuslagern = new JButton("Einsatzkomponente (Einsatz auswählen zum Auslagern)");
      this.modulBeschreibung = new JLabel("Administrator Bereich");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.tabPane = new JTabbedPane();
   }

   protected void labelErstellen() {
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         int e = tabGruppe.getID(runApplication.mitgliederGruppe);
         String[] veranstaltungListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltungEinesJahres(SbcUtils.timeStamp("yyyy"), e));
         String[] mitgliederGruppeListe = Utils.listToArrayOnlyFORComboBoxes(tabGruppe.getAllGruppen());
         this.veranstaltung = new JComboBox(veranstaltungListe);
         this.mitgliederGruppe = new JComboBox(mitgliederGruppeListe);
         this.mitgliederGruppe.setSelectedItem(runApplication.mitgliederGruppe);
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      this.mitgliederGruppe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

            try {
               runApplication.mitgliederGruppe = AdministratorAO.this.mitgliederGruppe.getSelectedItem().toString();
               int e = tabGruppe.getID(AdministratorAO.this.mitgliederGruppe.getSelectedItem().toString());
               String[] veranstaltungListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltungEinesJahres(SbcUtils.timeStamp("yyyy"), e));
               AdministratorAO.this.veranstaltung.removeAllItems();

               for(int v = 0; v < veranstaltungListe.length; ++v) {
                  AdministratorAO.this.veranstaltung.addItem(veranstaltungListe[v]);
               }
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(670, 500);
      this.setTitle("FeuerwehrManagementSystem - Administratoren Bereich");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelAdministration = new JPanel(new GridLayout(10, 1));
      this.getContentPane().add("Center", this.panelAdministration);
      this.panelAdministration.add(this.buttonDebug);
      this.panelAdministration.add(this.buttonLogbuch);
      this.panelAdministration.add(this.buttonFTPRekalkulation);
      this.panelAdministration.add(this.buttonLöscheAlleSystemWarnungen);
      if(((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungModulAktiv")).equals("1")) {
         this.panelAdministration.add(this.buttonLöscheUnwetterwarnung);
      }

      this.panelAdministration.add(this.buttonClientVerwaltung);
      this.panelAdministration.add(this.buttonTerminDisplay);
      this.panelAdministration.add(this.buttonErstelleMitgliederStatistik);
      this.panelMandant = new JPanel(new GridLayout(10, 1));
      this.getContentPane().add("Center", this.panelMandant);
      this.panelMandant.add(this.buttonMandantInfo);
      this.panelMandant.add(this.buttonMandantNameAendern);
      this.panelMandant.add(this.buttonMandantIDAendern);
      this.panelVeranstaltung = new JPanel(new GridLayout(10, 1));
      this.getContentPane().add("Center", this.panelVeranstaltung);
      this.panelVeranstaltung.add(this.mitgliederGruppe_label);
      this.panelVeranstaltung.add(this.mitgliederGruppe);
      this.panelVeranstaltung.add(this.veranstaltung_label);
      this.panelVeranstaltung.add(this.veranstaltung);
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(this.buttonLöschenKomplett);
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(this.buttonLöschenAnwesenheit);
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(this.buttonLöschenAbwesenheit);
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelVeranstaltung.add(new JLabel());
      this.panelJahr = new JPanel(new GridLayout(10, 1));
      this.getContentPane().add("Center", this.panelJahr);
      this.panelJahr.add(this.jahrNachtragen_label);
      this.panelJahr.add(this.jahrNachtagen);
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(this.buttonJahrNachtragen);
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.panelJahr.add(new JLabel());
      this.tabPane.addTab("Administartion", this.panelAdministration);
      this.tabPane.addTab("Mandant       ", this.panelMandant);
      this.tabPane.addTab("Veranstaltung löschen", this.panelVeranstaltung);
      this.tabPane.addTab("Jahr hinzufügen", this.panelJahr);
      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden")).equals("1") | ((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("1")) {
         this.panelJoomla = new JPanel(new GridLayout(10, 1));
         this.getContentPane().add("Center", this.panelJoomla);
         this.panelJoomla.add(this.joomlaVeranstaltungenAuslagern);
         this.panelJoomla.add(this.joomlaAusbildungsplanAuslagern);
         this.panelJoomla.add(this.joomlaVeranstaltungenOnlineLöschen);
         this.panelJoomla.add(this.joomlaAusbildungsplanOnlineLöschen);
         this.tabPane.addTab("Joomla", this.panelJoomla);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente")).equals("1")) {
         this.panelEinsatzkomponente = new JPanel(new GridLayout(10, 1));
         this.getContentPane().add("Center", this.panelEinsatzkomponente);
         this.panelEinsatzkomponente.add(this.buttonEinsatzkomponenteOrganisationen);
         this.panelEinsatzkomponente.add(this.buttonEinsatzkomponenteFahrzeug);
         this.panelEinsatzkomponente.add(this.buttonEinsätzeAnEinsatzkomponenteAuslagern);
         this.panelEinsatzkomponente.add(this.buttonEinsatzkomonenteLöschen);
         this.panelEinsatzkomponente.add(this.buttonEinsatzkomonenteKompettNeuAuslagern);
         this.panelEinsatzkomponente.add(this.buttonEinsatzkomponenteEinenEinsatzAuslagern);
         this.tabPane.addTab("Einsatzkomponente", this.panelEinsatzkomponente);
      }

      this.tabPane.setPreferredSize(new Dimension(630, 350));
      this.add(this.tabPane);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden")).equals("0")) {
         this.joomlaVeranstaltungenAuslagern.setVisible(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("0")) {
         this.joomlaAusbildungsplanAuslagern.setVisible(false);
      }

   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonLöscheUnwetterwarnung.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               TabelleEinstellungen_gespeichert e1 = new TabelleEinstellungen_gespeichert();
               SystemTrayInfo trayInfo = new SystemTrayInfo();
               e1.update("unwetterwarnungDatumBis", "null");
               e1.update("unwetterwarnungUhrzeitBis", "null");
               runApplication.unwetterwarnungStatus = 0;
               runApplication.unwetterwarnungDatumBis = null;
               runApplication.unwetterwarnungUhrzeitBis = null;
               HauptprogrammAO.buttonUnwetterwarnung.setVisible(false);
               HauptprogrammAO.buttonUnwetterwarnung.setToolTipText((String)null);
               trayInfo.removeInfoIcon();
               logging.logInfo("Unwetterwarnung wurde manuell vom Administrator entfernt...");
               JOptionPane.showMessageDialog((Component)null, Konstante.UNWETTERWARUNG_GELÖSCHT);
               logbuchEingabe.NeuerEintag("Unwetterwarnung wurde manuell vom Administrator entfernt");
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
      this.buttonErstelleMitgliederStatistik.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            runApplication.createMitgliederStatistik(Integer.parseInt(SbcUtils.timeStamp("yyyy")));
         }
      });
      this.buttonMandantInfo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleMandant e = new TabelleMandant();
               String output = "MandantID:\n" + (String)runApplication.PROPERTIES.get("MandantID") + "\n\nMandant Name:\n" + e.getMandantName(Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID")));
               JOptionPane.showMessageDialog((Component)null, output);
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
      this.buttonEinsatzkomonenteKompettNeuAuslagern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.EINSTZKOMPONENTE_KOMPLETT_AUSLAGERN, "Frage", 0);
            if(msg == 0) {
               AdministratorAO.this.buttonEinsatzkomonenteLöschen.doClick();
               AdministratorAO.this.buttonEinsatzkomponenteOrganisationen.doClick();
               AdministratorAO.this.buttonEinsatzkomponenteFahrzeug.doClick();
               AdministratorAO.this.buttonEinsätzeAnEinsatzkomponenteAuslagern.doClick();
            }

         }
      });
      this.buttonEinsatzkomponenteEinenEinsatzAuslagern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
            TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();

            try {
               JFrame e = new JFrame("Frage");
               String[] veranstaltungsListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorie(1, 1));
               String neuerEinsatz = (String)JOptionPane.showInputDialog(e, Konstante.WELCHER_EINSATZ_SOLL_AUSGELAGERT_WERDEN, "Frage", 3, (Icon)null, veranstaltungsListe, veranstaltungsListe[0]);
               if(neuerEinsatz != null) {
                  logging.logInfo("Sende Einsatz an die Einsatzkomponente für VeranstaltungsID == " + neuerEinsatz);
                  int vID = tabVeranstaltung.getVeranstaltungID(neuerEinsatz);
                  Joomla.erstelleEinsatz(tabEinsatz.getData2(vID), tabStatistik.getData(vID), false, true);
               }
            } catch (SQLException var9) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var9);
            }

         }
      });
      this.buttonEinsatzkomonenteLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.EINSTZKOMPONENTE_LEEREN, "Frage", 0);
            if(msg == 0) {
               Joomla.einsatzkomoneteLeeren();
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            }

         }
      });
      this.buttonEinsatzkomponenteFahrzeug.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.FAHRZEUGE_AUSLAGERN, "Frage", 0);
            if(msg == 0) {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               Thread threadExecute = new Thread() {
                  public void run() {
                     TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();

                     try {
                        int[] e = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugIDsFromDataBase());
                        int count = e.length;

                        for(int i = 0; i < e.length; ++i) {
                           Fahrzeug fahrzeug = tabFahrzeug.getData(e[i]);
                           Joomla.erstelleFahrzeug(fahrzeug);
                           ProzessBarAO.progressbar.setValue(i * 100 / count);
                        }

                        MyEvent.setEvent("0x0030");
                     } catch (SQLException var6) {
                        logging.logPrintStackTrace(var6);
                     }

                  }
               };
               threadExecute.start();
            }

         }
      });
      this.buttonEinsatzkomponenteOrganisationen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.ORGANISATION_AUSLAGERN, "Frage", 0);
            if(msg == 0) {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               Thread threadExecute = new Thread() {
                  public void run() {
                     TabelleOrganisationen tabOrganisationen = new TabelleOrganisationen();

                     try {
                        int[] e = Utils.listToIntArray(tabOrganisationen.getAllOrganisationenIDs());
                        int count = e.length;

                        for(int i = 0; i < e.length; ++i) {
                           Organisation organisation = tabOrganisationen.getData(e[i]);
                           Joomla.erstelleOrganisation(organisation);
                           ProzessBarAO.progressbar.setValue(i * 100 / count);
                        }

                        MyEvent.setEvent("0x0030");
                     } catch (SQLException var6) {
                        logging.logPrintStackTrace(var6);
                     }

                  }
               };
               threadExecute.start();
            }

         }
      });
      this.buttonEinsätzeAnEinsatzkomponenteAuslagern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.EINSÄTZE_AUSLAGERN, "Frage", 0);
            if(msg == 0) {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               Thread threadExecute = new Thread() {
                  public void run() {
                     try {
                        TabelleEinsatz e = new TabelleEinsatz();
                        TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        String[] veranstaltungsListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieFromDB(1));
                        int count = veranstaltungsListe.length;

                        for(int i = 0; i < veranstaltungsListe.length; ++i) {
                           int vID = tabVeranstaltung.getVeranstaltungID(veranstaltungsListe[i]);
                           Joomla.erstelleEinsatz(e.getData2(vID), tabStatistik.getData(vID), false, true);
                           ProzessBarAO.progressbar.setValue(i * 100 / count);
                        }

                        MyEvent.setEvent("0x0030");
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     } catch (SQLException var8) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace(var8);
                     }

                  }
               };
               threadExecute.start();
            }

         }
      });
      this.buttonMandantNameAendern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleMandant e = new TabelleMandant();
               String msg = JOptionPane.showInputDialog("Bitte neuen MandantNamen eingeben:\n\nAktueller Mandant Name: " + runApplication.mandantName + "\n  ");
               if(msg != null) {
                  e.update(msg);
                  logbuchEingabe.NeuerEintag("MandantNamen geändert: ALT: " + runApplication.mandantName + " / NEU: " + msg);
                  runApplication.mandantName = msg;
                  logging.logInfo("MandantNamen geändert: " + msg);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               }
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
      this.buttonMandantIDAendern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMandant tabMandant = new TabelleMandant();
            UpdateDatenbank updateDatenbank = new UpdateDatenbank();
            MyProperties programmProperties = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
            String msg = JOptionPane.showInputDialog("!!! ACHTUNG !!!\n\nDie MandantID ist eines der elementarsten Konfigurationen vom\nFeuerwehrManagementSystem. Sollte bei diesem Vorgeng ein Fehler auftreten, kann dieser nicht mehr repariert werden.\n\nBitte machen Sie ein manuelles Backup ihrer datenbank bevor Sie die MandantID ändern!\n\n\nBitte neue MandantID eingeben:\nAktuelle MandantID: " + (String)runApplication.PROPERTIES.get("MandantID") + "\n  ");
            if(msg != null) {
               int msg2 = JOptionPane.showConfirmDialog((Component)null, Konstante.MandantID_AENDERN_BESTAETIGEN, "Frage", 0);
               if(msg2 == 0) {
                  try {
                     String[] e = Utils.listToArray(updateDatenbank.executeSqlWithReturn("show tables from " + (String)runApplication.PROPERTIES.get("DatenbankName") + ";"));

                     for(int i = 0; i < e.length; ++i) {
                        logging.logInfo(e[i]);
                        if(!e[i].equals("mandant") && !e[i].equals("atemschutzpass_einsatzart") && !e[i].equals("fahrzeug_beschreibung") && !e[i].equals("berechtigung_gruppe") && !e[i].equals("berechtigung_gruppe_name") && !e[i].equals("schulung") && !e[i].equals("schulung_details") && !e[i].equals("schulung_gruppe") && !e[i].equals("schulung_raum") && !e[i].equals("schulung_teilnehmer")) {
                           updateDatenbank.executeSql("update " + e[i] + " set mandantID = " + msg + " where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
                        } else {
                           logging.logInfo("Es wird keine MandantID in dieser Tabelle benötigt...");
                        }
                     }

                     tabMandant.update(Integer.parseInt(msg));
                     programmProperties.loadVars();
                     programmProperties.putVar("MandantID", msg);
                     programmProperties.saveVars();
                     runApplication.PROPERTIES.remove("MandantID");
                     runApplication.PROPERTIES.put("MandantID", msg);
                     logbuchEingabe.NeuerEintag("MandantID geändert: ALT: " + (String)runApplication.PROPERTIES.get("MandantID") + " / NEU: " + msg);
                     logging.logInfo("MandantID geändert: " + msg);
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  } catch (SQLException var9) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER);
                     logging.logPrintStackTrace(var9);
                  }
               }
            }

         }
      });
      this.buttonLöscheAlleSystemWarnungen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               int e = JOptionPane.showConfirmDialog((Component)null, Konstante.SYSTEMWARNUNGEN_WIRKLICH_LÖSCHEN, "Frage", 0);
               if(e == 0) {
                  (new TabelleSystemwarnung()).deleteAll();
                  JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
               }
            } catch (SQLException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonClientVerwaltung.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.CLIENTS);
            Steuerung.steuerung();
         }
      });
      this.buttonTerminDisplay.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog((Component)null, "DIESES SOFTWAREMODULE IST ABGEKÜNDIGT UND WIRD IN\nEINER DER NÄCHSTEN VERSION ENTFERNT!!!\n\n\nDas starten aus dem Administratormodul ist nur für Tests gedacht.\nDas Termindisplay wird mit der Dater \"FMS_TerminDisplay.exe\" gestartet", "Warnung", 2);
            Steuerung.setStatus(Status.TERMIN_DISPLAY);
            Steuerung.steuerung();
         }
      });
      this.joomlaAusbildungsplanOnlineLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Joomla.deleteAllAusbildungData();
            Joomla.deleteAllAusbildungKategorieData();
            JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
         }
      });
      this.joomlaAusbildungsplanAuslagern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            final TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
            final TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.JOOMLA_DB_UPDATE, "Frage", 0);
            if(msg == 0) {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               Thread threadExecute = new Thread() {
                  public void run() {
                     try {
                        Ausbildung_Plan e = new Ausbildung_Plan();
                        Ausbildung_Kategorie kategorie = new Ausbildung_Kategorie();
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        Joomla.deleteAllAusbildungData();
                        Joomla.deleteAllAusbildungKategorieData();
                        int[] planListe = Utils.listToIntArray(tabPlan.getAllID());
                        int[] kategorieListe = Utils.listToIntArray(tabKategorie.getAllKategorienID());
                        int count = planListe.length + kategorieListe.length;
                        HashMap mitgliederMap = tabMitglied.getMitgliederListe();

                        int i;
                        for(i = 0; i < planListe.length; ++i) {
                           HashMap map = tabPlan.getData(planListe[i]);
                           e.setId(Integer.parseInt((String)map.get("id")));
                           e.setJahr(Integer.parseInt((String)map.get("jahr")));
                           e.setVeranstaltungID(Integer.parseInt((String)map.get("veranstaltungID")));
                           e.setAusbildungKategorie(Integer.parseInt((String)map.get("ausbildungKategorie")));
                           e.setDetails((String)map.get("details"));
                           e.setAusbilder1(Integer.parseInt((String)map.get("ausbilder1")));
                           e.setAusbilder2(Integer.parseInt((String)map.get("ausbilder2")));
                           Joomla.erstelleAusbildungsplan(e, mitgliederMap);
                           ProzessBarAO.progressbar.setValue(i * 100 / count);
                        }

                        Thread.sleep(500L);

                        for(i = 0; i < kategorieListe.length; ++i) {
                           kategorie.setId(kategorieListe[i]);
                           kategorie.setName(tabKategorie.getNameByID(kategorieListe[i]));
                           Joomla.erstelleAusbildungKategorie(kategorie);
                           ProzessBarAO.progressbar.setValue((planListe.length + i) * 100 / count);
                        }

                        MyEvent.setEvent("0x0030");
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     } catch (InterruptedException var10) {
                        logging.logPrintStackTrace(var10);
                        MyEvent.setEvent("0x0030");
                     }

                  }
               };
               threadExecute.start();
            }

         }
      });
      this.joomlaVeranstaltungenOnlineLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Joomla.deleteAllVeranstaltungData();
            Joomla.deleteAllVeranstaltungKategorieData();
            JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
         }
      });
      this.joomlaVeranstaltungenAuslagern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            final TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            final TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.JOOMLA_DB_UPDATE, "Frage", 0);
            if(msg == 0) {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               Thread threadExecute = new Thread() {
                  public void run() {
                     try {
                        Veranstaltung e = new Veranstaltung();
                        Veranstaltung_Kategorie kategorie = new Veranstaltung_Kategorie();
                        Joomla.deleteAllVeranstaltungData();
                        Joomla.deleteAllVeranstaltungKategorieData();
                        int[] veranstaltungListe = Utils.listToIntArray(tabVeranstaltung.getAllVeranstaltungID());
                        int[] kategorieListe = Utils.listToIntArray(tabKategorie.getAllKategorienID());
                        int count = veranstaltungListe.length + kategorieListe.length;

                        int i;
                        for(i = 0; i < veranstaltungListe.length; ++i) {
                           HashMap map = tabVeranstaltung.getVeranstaltungData(veranstaltungListe[i]);
                           e.setId(Integer.parseInt((String)map.get("id")));
                           e.setName((String)map.get("name"));
                           e.setName2((String)map.get("name2"));
                           e.setDatum((String)map.get("datum"));
                           e.setZeit((String)map.get("zeit"));
                           e.setZeitEnde((String)map.get("zeitEnde"));
                           e.setKategorie(Integer.parseInt((String)map.get("kategorie")));
                           e.setFahrzeugeinteilung(Integer.parseInt((String)map.get("fahrzeugeinteilung")));
                           e.setInfoVersandt(Integer.parseInt((String)map.get("infoVersandt")));
                           Joomla.erstelleVeranstaltung(e);
                           ProzessBarAO.progressbar.setValue(i * 100 / count);
                        }

                        Thread.sleep(500L);

                        for(i = 0; i < kategorieListe.length; ++i) {
                           kategorie.setId(kategorieListe[i]);
                           kategorie.setName(tabKategorie.getName(kategorieListe[i]));
                           logging.logInfo(Integer.valueOf(kategorie.getId()));
                           logging.logInfo(kategorie.getName());
                           Joomla.erstelleVeranstaltungKategorie(kategorie);
                           ProzessBarAO.progressbar.setValue((veranstaltungListe.length + i) * 100 / count);
                        }

                        MyEvent.setEvent("0x0030");
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     } catch (InterruptedException var8) {
                        logging.logPrintStackTrace(var8);
                        MyEvent.setEvent("0x0030");
                     }

                  }
               };
               threadExecute.start();
            }

         }
      });
      this.buttonJahrNachtragen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleJahr jahr = new TabelleJahr();

            try {
               if(AdministratorAO.this.jahrNachtagen.getText().length() != 4) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR, "Warnung", 2);
               } else if(jahr.getJahr(Integer.parseInt(AdministratorAO.this.jahrNachtagen.getText())) == 1) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.JAHR_BEREITS_VORHANDEN, "Warnung", 2);
               } else {
                  logging.logInfo("Jahr wird nachgetragen : " + AdministratorAO.this.jahrNachtagen.getText());
                  jahr.insert(Integer.parseInt(AdministratorAO.this.jahrNachtagen.getText()));
                  logging.logInfo("data Ordner wird um das aktuelle Jahr erweitert");
                  String e = AdministratorAO.this.jahrNachtagen.getText();
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e, "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Berichte", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Brief", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Einsatzberichte", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Fahrzeugeinteilung", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Temp", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Verdienstausfall", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Beteiligung_uebersicht", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Mangel", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Lehrgangsmeldungen", "SYSTEM");
                  Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/" + e + "/Schichten", "SYSTEM");
                  AdministratorAO.this.jahrNachtagen.setText("");
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               }
            } catch (SQLException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var4);
            }

         }
      });
      this.buttonDebug.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.DEBUG);
            Steuerung.steuerung();
         }
      });
      this.buttonLogbuch.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.LOGBUCH);
            Steuerung.steuerung();
         }
      });
      this.buttonFTPRekalkulation.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.DATEN_NEU_KATALOGIESIEREN, "Frage", 0);
            if(msg == 0) {
               runApplication.verarbeitungLäuft = 1;
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               ProzessBarAO.progressbar.setStringPainted(false);
               ProzessBarAO.progressbar.setIndeterminate(true);
               ProzessBarAO.label_bitteWarten.setText("Daten werden Verarbeitet... Bitte haben sie einen Moment Geduld...");
               Thread threadFTP = new Thread() {
                  public void run() {
                     try {
                        (new TabelleFTPSync()).deleteAll();
                        (new TabelleDateisystem()).deleteAll();
                        Utils.rekatalogisiereDateien(runApplication.arbeitsverzeichnis + "data");
                     } catch (SQLException var2) {
                        runApplication.verarbeitungLäuft = 0;
                        MyEvent.setEvent("0x0030");
                        logging.logPrintStackTrace(var2);
                     }

                     MyEvent.setEvent("0x0030");
                     runApplication.verarbeitungLäuft = 0;
                  }
               };
               threadFTP.start();
            }

         }
      });
      this.buttonLöschenKomplett.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
            TabelleBrandsicherheitswache tabBSW = new TabelleBrandsicherheitswache();
            TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
            TabelleEinsatz_bericht tabBericht = new TabelleEinsatz_bericht();
            TabelleEinsatz_bericht_daten tabBerichtDaten = new TabelleEinsatz_bericht_daten();
            TabelleEinsatz_organisationen tabOrganisation = new TabelleEinsatz_organisationen();
            TabelleEinsatz_zeiten tabEinsatzZeiten = new TabelleEinsatz_zeiten();
            TabelleStatistikSonstigeVeranstaltung tabStatistikSonstigeVeranstaltung = new TabelleStatistikSonstigeVeranstaltung();
            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
            TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
            TabelleAusbildung_plan tabAusPlan = new TabelleAusbildung_plan();

            try {
               if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
               } else {
                  final int e = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                  int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.VERANSTALTUNG_WIRKLICH_LÖSCHEN, "Frage", 0);
                  if(msg == 0) {
                     int msg2 = JOptionPane.showConfirmDialog((Component)null, Konstante.VERANSTALTUNG_WIRKLICH_LÖSCHEN2, "Frage", 0);
                     if(msg2 == 0) {
                        String dateiname;
                        String datum;
                        int bswNummer;
                        String[] bswNummerListe;
                        int i;
                        if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                           datum = tabBericht.getJahr(e);
                           bswNummer = tabEinsatz.getEinsatzIDByVeranstaltungID(e);
                           bswNummerListe = Utils.listToArray(tabEinsatz.getEinsatzNummerListeForDelete(bswNummer, datum));
                           dateiname = runApplication.arbeitsverzeichnis + "data/" + datum + "/Einsatzberichte/" + tabBericht.getDateiname(e);
                           if((new File(dateiname)).exists()) {
                              logging.logInfo("Einsatzbericht löschen --> " + dateiname);
                              (new File(dateiname)).delete();
                              Utils.dateiKatalogisierenForDelete(dateiname);
                           }

                           tabVeranstaltung.deleteOne(e);
                           tabStatistikEinsatz.deleteOne(e);
                           tabEinsatz.delete(e);
                           tabBericht.delete(e);
                           tabBerichtDaten.delete(e);
                           tabOrganisation.delete(e);
                           tabEinsatzZeiten.deleteALL(e);
                           logging.logInfo("Einsatz Tabellen wurden vom Datensatz Bereinigt!");
                           logging.logInfo("Aktualisiere --> einsatzNummmer");

                           for(i = 0; i < bswNummerListe.length; ++i) {
                              tabEinsatz.updateEinsatzNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                              tabBericht.updateEinsatzNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                              tabBerichtDaten.updateEinsatzNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                              tabEinsatzZeiten.updateEinsatzNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                              tabStatistikEinsatz.updateEinsatzNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                           }

                           if(((String)runApplication.EINSTELLUNGEN.get("facebookAutoPostEinsatz")).equals("1")) {
                              Thread threadRemoveFacebookPost = new Thread() {
                                 public void run() {
                                    (new Facebook()).deletePublishedMessage(e);
                                 }
                              };
                              threadRemoveFacebookPost.start();
                           }
                        } else if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                           datum = Integer.toString(tabVeranstaltung.getJahrDerVeranstaltung(e));
                           bswNummer = tabBSW.getBswIDbyVeranstaltungID(e);
                           bswNummerListe = Utils.listToArray(tabBSW.getBSWNummerListeForDelete(bswNummer, datum));
                           tabVeranstaltung.deleteOne(e);
                           tabBSW.delete(e);
                           tabStatistikBSW.deleteOne(e);
                           logging.logInfo("BSW Tabellen wurden vom Datensatz Bereinigt!");
                           logging.logInfo("Aktualisiere --> bswNummmer");

                           for(i = 0; i < bswNummerListe.length; ++i) {
                              tabBSW.updateBSWNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                              tabStatistikBSW.updateBSWNummer(Integer.parseInt(bswNummerListe[i]) - 1, Integer.parseInt(bswNummerListe[i]), datum);
                           }
                        } else {
                           datum = tabVeranstaltung.getDatum(e);
                           dateiname = runApplication.arbeitsverzeichnis + "data/" + datum.substring(0, 4) + "/Fahrzeugeinteilung/Dienstabend_" + TimeCalculation.parseDateForGUI(datum) + "_ID_" + e + ".pdf";
                           if((new File(dateiname)).exists()) {
                              (new File(dateiname)).delete();
                              Utils.dateiKatalogisierenForDelete(dateiname);
                           }

                           tabVeranstaltung.deleteOne(e);
                           tabStatistikSonstigeVeranstaltung.deleteOne(e);
                           tabAusPlan.deleteOne(e);
                           logging.logInfo("Sonstige Veranstaltung Tabellen wurden vom Datensatz Bereinigt!");
                        }

                        tabAnwesenheit.delete(e);
                        tabAbwesenheit.delete(e);
                        logging.logInfo("An- / Abwesenheitstabellen wurden vom Datensatz bereinigt!");
                        logging.logInfo("Veranstaltung erfogreich entfernt!");
                        logbuchEingabe.NeuerEintag("Lösche Veranstaltung: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                        AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                        AdministratorAO.this.veranstaltung.setSelectedItem("<bitte wählen>");
                     }
                  }
               }
            } catch (SQLException var24) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var24);
            }

         }
      });
      this.buttonLöschenAnwesenheit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
            StatistikBSW statistikBSW = new StatistikBSW();
            TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
            Einsatz einsatz = new Einsatz();
            StatistikEinsatz statistikEinsatz = new StatistikEinsatz();
            TabelleStatistikSonstigeVeranstaltung tabStatistikSonstigeVeranstaltung = new TabelleStatistikSonstigeVeranstaltung();
            StatistikSonstigeVeranstaltung statistikSonstige = new StatistikSonstigeVeranstaltung();
            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();

            try {
               if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
               } else {
                  int e = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                  int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.ANWESENHEIT_WIRKLICH_LÖSCHEN, "Frage", 0);
                  if(msg == 0) {
                     int msg2 = JOptionPane.showConfirmDialog((Component)null, Konstante.VERANSTALTUNG_WIRKLICH_LÖSCHEN2, "Frage", 0);
                     if(msg2 == 0) {
                        if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                           statistikEinsatz.setVeranstaltungID(e);
                           statistikEinsatz.setMannstunden(0);
                           tabStatistikEinsatz.updateMannstunden(statistikEinsatz);
                           einsatz.setVeranstaltungID(e);
                           einsatz.setStaerkeFM(0);
                           einsatz.setStaerkeGF(0);
                           einsatz.setStaerkeZF(0);
                           tabEinsatz.updateStaerke(einsatz);
                           logging.logInfo("Anwesenheit Löschen --> Einsatz Tabellen wurden aktualisiert");
                        } else if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                           statistikBSW.setVeranstaltungID(e);
                           statistikBSW.setMannstunden(0);
                           tabStatistikBSW.updateMannstunden(statistikBSW);
                           logging.logInfo("Anwesenheit Löschen --> BSW Tabellen wurden aktualisiert");
                        } else {
                           statistikSonstige.setVeranstaltungID(e);
                           statistikSonstige.setMannstunden(0);
                           tabStatistikSonstigeVeranstaltung.updateMannstunden(statistikSonstige);
                           logging.logInfo("Anwesenheit Löschen --> Sonstige Satistik Tabellen wurden aktualisiert");
                        }

                        tabAnwesenheit.delete(e);
                        logbuchEingabe.NeuerEintag("Lösche Anwesenheit: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                        AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                        AdministratorAO.this.veranstaltung.setSelectedItem("<bitte wählen>");
                     }
                  }
               }
            } catch (SQLException var15) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var15);
            }

         }
      });
      this.buttonLöschenAbwesenheit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();

            try {
               if(AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
               } else {
                  int e = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                  int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.ABWESENHEIT_WIRKLICH_LÖSCHEN, "Frage", 0);
                  if(msg == 0) {
                     int msg2 = JOptionPane.showConfirmDialog((Component)null, Konstante.VERANSTALTUNG_WIRKLICH_LÖSCHEN2, "Frage", 0);
                     if(msg2 == 0) {
                        tabAbwesenheit.delete(e);
                        logging.logInfo("Abwesenheit Löschen --> Tabelle wurde bereinigt!");
                        logbuchEingabe.NeuerEintag("Lösche Abwesenheit: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                        AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                        AdministratorAO.this.veranstaltung.setSelectedItem("<bitte wählen>");
                     }
                  }
               }
            } catch (SQLException var7) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var7);
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
