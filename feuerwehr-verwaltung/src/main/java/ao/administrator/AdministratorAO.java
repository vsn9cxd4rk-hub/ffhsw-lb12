/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.administrator;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
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
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleStatistikbsw;
import data.tabellen.TabelleSystemwarnung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleMandant;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
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
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class AdministratorAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDebug;
    private JButton buttonLogbuch;
    private JButton buttonFTPRekalkulation;
    private JButton buttonClientVerwaltung;
    private JButton buttonTerminDisplay;
    private JButton buttonMandantInfo;
    private JButton buttonMandantNameAendern;
    private JButton buttonErstelleMitgliederStatistik;
    private JButton joomlaAusbildungsplanAuslagern;
    private JButton joomlaVeranstaltungenAuslagern;
    private JButton joomlaAusbildungsplanOnlineL\u00f6schen;
    private JButton joomlaVeranstaltungenOnlineL\u00f6schen;
    private JButton buttonL\u00f6scheAlleSystemWarnungen;
    private JButton buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern;
    private JButton buttonEinsatzkomponenteOrganisationen;
    private JButton buttonEinsatzkomponenteFahrzeug;
    private JButton buttonEinsatzkomonenteL\u00f6schen;
    private JButton buttonEinsatzkomonenteKompettNeuAuslagern;
    private JButton buttonL\u00f6schenKomplett;
    private JButton buttonL\u00f6schenAnwesenheit;
    private JButton buttonL\u00f6schenAbwesenheit;
    private JComboBox<String> veranstaltung;
    private JLabel veranstaltung_label;
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
    private JTabbedPane tabPane;

    public AdministratorAO() {
        super("FeuerwehrManagementSystem - Administrator");
        logging.logInfo((Object)"Starte: AdministratorAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDebug = new JButton("Debugging");
        this.buttonLogbuch = new JButton("Logbuch");
        this.buttonFTPRekalkulation = new JButton("Dateien Katalogisieren");
        this.buttonL\u00f6scheAlleSystemWarnungen = new JButton("Alle Systemwarnungen zur\u00fccksetzen");
        this.buttonClientVerwaltung = new JButton("Client Verwaltung");
        this.buttonTerminDisplay = new JButton("Termin Display starten");
        this.buttonMandantInfo = new JButton("Mandant Info");
        this.buttonMandantNameAendern = new JButton("Mandant Namen \u00e4ndern");
        this.buttonErstelleMitgliederStatistik = new JButton("Erstelle Mitglieder Statistik");
        this.buttonErstelleMitgliederStatistik.setToolTipText("Statistiken: Mitgliederanzahl und Mitgliederdruchschnittsalter werden vom heutigen Tag berechnet");
        this.buttonL\u00f6schenKomplett = new JButton("Veranstaltung Komplett l\u00f6schen");
        this.buttonL\u00f6schenAnwesenheit = new JButton("Anwesenheit l\u00f6schen");
        this.buttonL\u00f6schenAbwesenheit = new JButton("Abwesenheit l\u00f6schen");
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
        this.buttonJahrNachtragen = new JButton("Jahr anlegen");
        this.jahrNachtagen = new JTextField(20);
        this.jahrNachtragen_label = new JLabel("Jahr Nachtragen: ");
        this.joomlaVeranstaltungenAuslagern = new JButton("Veranstaltungen (Joomla Auslagerung)");
        this.joomlaVeranstaltungenAuslagern.setToolTipText("Alle Veranstaltungen auf Joomla Seite komplett neu auslagern");
        this.joomlaAusbildungsplanAuslagern = new JButton("Ausbildungsplan (Joomla Auslagerung)");
        this.joomlaAusbildungsplanAuslagern.setToolTipText("Gesamten Ausbildungsplan auf Joomla Seite komplett neu auslagern");
        this.joomlaAusbildungsplanOnlineL\u00f6schen = new JButton("Alle Ausbildungsplan Online L\u00f6schen");
        this.joomlaVeranstaltungenOnlineL\u00f6schen = new JButton("Alle Veranstaltungen Online L\u00f6schen");
        this.buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern = new JButton("Einsatz (Einsatzkomponente)");
        this.buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern.setToolTipText("Alle Eins\u00e4tze an die Einsatzkomponente auslagern");
        this.buttonEinsatzkomponenteOrganisationen = new JButton("Organisationen  (Einsatzkomponente)");
        this.buttonEinsatzkomponenteFahrzeug = new JButton("Fahrzeuge (Einsatzkomponente)");
        this.buttonEinsatzkomonenteL\u00f6schen = new JButton("Einsatzkomponente leeren");
        this.buttonEinsatzkomonenteKompettNeuAuslagern = new JButton("Einsatzkomponente (Komplett Auslagern)");
        this.modulBeschreibung = new JLabel("Administrator Bereich");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.tabPane = new JTabbedPane();
    }

    protected void labelErstellen() {
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        try {
            String[] veranstaltungListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltungEinesJahres(SbcUtils.timeStamp((String)"yyyy")));
            this.veranstaltung = new JComboBox<String>(veranstaltungListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.panelAdministration.add(this.buttonL\u00f6scheAlleSystemWarnungen);
        this.panelAdministration.add(this.buttonClientVerwaltung);
        this.panelAdministration.add(this.buttonTerminDisplay);
        this.panelAdministration.add(this.buttonErstelleMitgliederStatistik);
        this.panelAdministration.add(this.buttonMandantInfo);
        this.panelAdministration.add(this.buttonMandantNameAendern);
        this.panelVeranstaltung = new JPanel(new GridLayout(10, 1));
        this.getContentPane().add("Center", this.panelVeranstaltung);
        this.panelVeranstaltung.add(this.veranstaltung_label);
        this.panelVeranstaltung.add(this.veranstaltung);
        this.panelVeranstaltung.add(new JLabel());
        this.panelVeranstaltung.add(this.buttonL\u00f6schenKomplett);
        this.panelVeranstaltung.add(new JLabel());
        this.panelVeranstaltung.add(this.buttonL\u00f6schenAnwesenheit);
        this.panelVeranstaltung.add(new JLabel());
        this.panelVeranstaltung.add(this.buttonL\u00f6schenAbwesenheit);
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
        this.tabPane.addTab("Veranstaltung l\u00f6schen", this.panelVeranstaltung);
        this.tabPane.addTab("Jahr hinzuf\u00fcgen", this.panelJahr);
        if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1") | runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden").equals("1")) {
            this.panelJoomla = new JPanel(new GridLayout(10, 1));
            this.getContentPane().add("Center", this.panelJoomla);
            this.panelJoomla.add(this.joomlaVeranstaltungenAuslagern);
            this.panelJoomla.add(this.joomlaAusbildungsplanAuslagern);
            this.panelJoomla.add(this.joomlaVeranstaltungenOnlineL\u00f6schen);
            this.panelJoomla.add(this.joomlaAusbildungsplanOnlineL\u00f6schen);
            this.tabPane.addTab("Joomla", this.panelJoomla);
        }
        if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente").equals("1")) {
            this.panelEinsatzkomponente = new JPanel(new GridLayout(10, 1));
            this.getContentPane().add("Center", this.panelEinsatzkomponente);
            this.panelEinsatzkomponente.add(this.buttonEinsatzkomponenteOrganisationen);
            this.panelEinsatzkomponente.add(this.buttonEinsatzkomponenteFahrzeug);
            this.panelEinsatzkomponente.add(this.buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern);
            this.panelEinsatzkomponente.add(this.buttonEinsatzkomonenteL\u00f6schen);
            this.panelEinsatzkomponente.add(this.buttonEinsatzkomonenteKompettNeuAuslagern);
            this.tabPane.addTab("Einsatzkomponente", this.panelEinsatzkomponente);
        }
        this.tabPane.setPreferredSize(new Dimension(630, 350));
        this.add(this.tabPane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("0")) {
            this.joomlaVeranstaltungenAuslagern.setVisible(false);
        }
        if (runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden").equals("0")) {
            this.joomlaAusbildungsplanAuslagern.setVisible(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonErstelleMitgliederStatistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.createMitgliederStatistik(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        this.buttonMandantInfo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleMandant tabMandant = new TabelleMandant();
                    String output = "MandantID:\n" + runApplication.PROPERTIES.get("MandantID") + "\n\nMandant Name:\n" + tabMandant.getMandantName(Integer.parseInt(runApplication.PROPERTIES.get("MandantID")));
                    JOptionPane.showMessageDialog(null, output);
                }
                catch (NumberFormatException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEinsatzkomonenteKompettNeuAuslagern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.EINSTZKOMPONENTE_KOMPLETT_AUSLAGERN, "Frage", 0);
                if (msg == 0) {
                    AdministratorAO.this.buttonEinsatzkomonenteL\u00f6schen.doClick();
                    AdministratorAO.this.buttonEinsatzkomponenteOrganisationen.doClick();
                    AdministratorAO.this.buttonEinsatzkomponenteFahrzeug.doClick();
                    AdministratorAO.this.buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern.doClick();
                }
            }
        });
        this.buttonEinsatzkomonenteL\u00f6schen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.EINSTZKOMPONENTE_LEEREN, "Frage", 0);
                if (msg == 0) {
                    Joomla.einsatzkomoneteLeeren();
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
            }
        });
        this.buttonEinsatzkomponenteFahrzeug.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.FAHRZEUGE_AUSLAGERN, "Frage", 0);
                if (msg == 0) {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    Thread threadExecute = new Thread(){

                        @Override
                        public void run() {
                            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                            try {
                                int[] fahrzeugIdListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugIDsFromDataBase());
                                int count = fahrzeugIdListe.length;
                                int i = 0;
                                while (i < fahrzeugIdListe.length) {
                                    Fahrzeug fahrzeug = tabFahrzeug.getData(fahrzeugIdListe[i]);
                                    Joomla.erstelleFahrzeug(fahrzeug);
                                    ProzessBarAO.progressbar.setValue(i * 100 / count);
                                    ++i;
                                }
                                MyEvent.setEvent((String)"0x0030");
                            }
                            catch (SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    threadExecute.start();
                }
            }
        });
        this.buttonEinsatzkomponenteOrganisationen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.ORGANISATION_AUSLAGERN, "Frage", 0);
                if (msg == 0) {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    Thread threadExecute = new Thread(){

                        @Override
                        public void run() {
                            TabelleOrganisationen tabOrganisationen = new TabelleOrganisationen();
                            try {
                                int[] organisationIdListe = Utils.listToIntArray(tabOrganisationen.getAllOrganisationenIDs());
                                int count = organisationIdListe.length;
                                int i = 0;
                                while (i < organisationIdListe.length) {
                                    Organisation organisation = tabOrganisationen.getData(organisationIdListe[i]);
                                    Joomla.erstelleOrganisation(organisation);
                                    ProzessBarAO.progressbar.setValue(i * 100 / count);
                                    ++i;
                                }
                                MyEvent.setEvent((String)"0x0030");
                            }
                            catch (SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    threadExecute.start();
                }
            }
        });
        this.buttonEins\u00e4tzeAnEinsatzkomponenteAuslagern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.EINS\u00c4TZE_AUSLAGERN, "Frage", 0);
                if (msg == 0) {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    Thread threadExecute = new Thread(){

                        @Override
                        public void run() {
                            try {
                                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                                TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
                                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                                String[] veranstaltungsListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieFromDB(1));
                                int count = veranstaltungsListe.length;
                                int i = 0;
                                while (i < veranstaltungsListe.length) {
                                    int vID = tabVeranstaltung.getVeranstaltungID(veranstaltungsListe[i]);
                                    Joomla.erstelleEinsatz(tabEinsatz.getData2(vID), tabStatistik.getData(vID), false, true);
                                    ProzessBarAO.progressbar.setValue(i * 100 / count);
                                    ++i;
                                }
                                MyEvent.setEvent((String)"0x0030");
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                            }
                            catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    threadExecute.start();
                }
            }
        });
        this.buttonMandantNameAendern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleMandant tabMandant = new TabelleMandant();
                    String msg = JOptionPane.showInputDialog("Bitte neuen MandantNamen eingeben:\n\nAktueller Mandant Name: " + runApplication.mandantName + "\n  ");
                    if (msg != null) {
                        tabMandant.update(msg);
                        logbuchEingabe.NeuerEintag("MandantNamen ge\u00e4ndert: ALT: " + runApplication.mandantName + " / NEU: " + msg);
                        runApplication.mandantName = msg;
                        logging.logInfo((Object)("MandantNamen ge\u00e4ndert: " + msg));
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (NumberFormatException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonL\u00f6scheAlleSystemWarnungen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.SYSTEMWARNUNGEN_WIRKLICH_L\u00d6SCHEN, "Frage", 0);
                    if (msg == 0) {
                        new TabelleSystemwarnung().deleteAll();
                        JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonClientVerwaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.CLIENTS);
                Steuerung.steuerung();
            }
        });
        this.buttonTerminDisplay.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, "Das starten aus dem Administratormodul ist nur f\u00fcr Tests gedacht.\nDas Termindisplay wird mit der Dater \"FMS_TerminDisplay.exe\" gestartet", "Warnung", 2);
                Steuerung.setStatus(Status.TERMIN_DISPLAY);
                Steuerung.steuerung();
            }
        });
        this.joomlaAusbildungsplanOnlineL\u00f6schen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Joomla.deleteAllAusbildungData();
                Joomla.deleteAllAusbildungKategorieData();
                JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
            }
        });
        this.joomlaAusbildungsplanAuslagern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                final TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                int msg = JOptionPane.showConfirmDialog(null, Konstante.JOOMLA_DB_UPDATE, "Frage", 0);
                if (msg == 0) {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    Thread threadExecute = new Thread(){

                        @Override
                        public void run() {
                            try {
                                Ausbildung_Plan plan = new Ausbildung_Plan();
                                Ausbildung_Kategorie kategorie = new Ausbildung_Kategorie();
                                TabelleMitglied tabMitglied = new TabelleMitglied();
                                Joomla.deleteAllAusbildungData();
                                Joomla.deleteAllAusbildungKategorieData();
                                int[] planListe = Utils.listToIntArray(tabPlan.getAllID());
                                int[] kategorieListe = Utils.listToIntArray(tabKategorie.getAllKategorienID());
                                int count = planListe.length + kategorieListe.length;
                                HashMap<Integer, String> mitgliederMap = tabMitglied.getMitgliederListe();
                                int i = 0;
                                while (i < planListe.length) {
                                    HashMap<String, String> map = tabPlan.getData(planListe[i]);
                                    plan.setId(Integer.parseInt(map.get("id")));
                                    plan.setJahr(Integer.parseInt(map.get("jahr")));
                                    plan.setVeranstaltungID(Integer.parseInt(map.get("veranstaltungID")));
                                    plan.setAusbildungKategorie(Integer.parseInt(map.get("ausbildungKategorie")));
                                    plan.setDetails(map.get("details"));
                                    plan.setAusbilder1(Integer.parseInt(map.get("ausbilder1")));
                                    plan.setAusbilder2(Integer.parseInt(map.get("ausbilder2")));
                                    Joomla.erstelleAusbildungsplan(plan, mitgliederMap);
                                    ProzessBarAO.progressbar.setValue(i * 100 / count);
                                    ++i;
                                }
                                Thread.sleep(500L);
                                i = 0;
                                while (i < kategorieListe.length) {
                                    kategorie.setId(kategorieListe[i]);
                                    kategorie.setName(tabKategorie.getNameByID(kategorieListe[i]));
                                    Joomla.erstelleAusbildungKategorie(kategorie);
                                    ProzessBarAO.progressbar.setValue((planListe.length + i) * 100 / count);
                                    ++i;
                                }
                                MyEvent.setEvent((String)"0x0030");
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                            }
                            catch (InterruptedException | SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                                MyEvent.setEvent((String)"0x0030");
                            }
                        }
                    };
                    threadExecute.start();
                }
            }
        });
        this.joomlaVeranstaltungenOnlineL\u00f6schen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Joomla.deleteAllVeranstaltungData();
                Joomla.deleteAllVeranstaltungKategorieData();
                JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
            }
        });
        this.joomlaVeranstaltungenAuslagern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                final TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
                int msg = JOptionPane.showConfirmDialog(null, Konstante.JOOMLA_DB_UPDATE, "Frage", 0);
                if (msg == 0) {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    Thread threadExecute = new Thread(){

                        @Override
                        public void run() {
                            try {
                                Veranstaltung veranstaltung = new Veranstaltung();
                                Veranstaltung_Kategorie kategorie = new Veranstaltung_Kategorie();
                                Joomla.deleteAllVeranstaltungData();
                                Joomla.deleteAllVeranstaltungKategorieData();
                                int[] veranstaltungListe = Utils.listToIntArray(tabVeranstaltung.getAllVeranstaltungID());
                                int[] kategorieListe = Utils.listToIntArray(tabKategorie.getAllKategorienID());
                                int count = veranstaltungListe.length + kategorieListe.length;
                                int i = 0;
                                while (i < veranstaltungListe.length) {
                                    HashMap<String, String> map = tabVeranstaltung.getVeranstaltungData(veranstaltungListe[i]);
                                    veranstaltung.setId(Integer.parseInt(map.get("id")));
                                    veranstaltung.setName(map.get("name"));
                                    veranstaltung.setName2(map.get("name2"));
                                    veranstaltung.setDatum(map.get("datum"));
                                    veranstaltung.setZeit(map.get("zeit"));
                                    veranstaltung.setZeitEnde(map.get("zeitEnde"));
                                    veranstaltung.setKategorie(Integer.parseInt(map.get("kategorie")));
                                    veranstaltung.setFahrzeugeinteilung(Integer.parseInt(map.get("fahrzeugeinteilung")));
                                    veranstaltung.setInfoVersandt(Integer.parseInt(map.get("infoVersandt")));
                                    Joomla.erstelleVeranstaltung(veranstaltung);
                                    ProzessBarAO.progressbar.setValue(i * 100 / count);
                                    ++i;
                                }
                                Thread.sleep(500L);
                                i = 0;
                                while (i < kategorieListe.length) {
                                    kategorie.setId(kategorieListe[i]);
                                    kategorie.setName(tabKategorie.getName(kategorieListe[i]));
                                    logging.logInfo((Object)kategorie.getId());
                                    logging.logInfo((Object)kategorie.getName());
                                    Joomla.erstelleVeranstaltungKategorie(kategorie);
                                    ProzessBarAO.progressbar.setValue((veranstaltungListe.length + i) * 100 / count);
                                    ++i;
                                }
                                MyEvent.setEvent((String)"0x0030");
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                            }
                            catch (InterruptedException | SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                                MyEvent.setEvent((String)"0x0030");
                            }
                        }
                    };
                    threadExecute.start();
                }
            }
        });
        this.buttonJahrNachtragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleJahr jahr = new TabelleJahr();
                try {
                    if (AdministratorAO.this.jahrNachtagen.getText().length() != 4) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR, "Warnung", 2);
                    } else if (jahr.getJahr(Integer.parseInt(AdministratorAO.this.jahrNachtagen.getText())) == 1) {
                        JOptionPane.showMessageDialog(null, Konstante.JAHR_BEREITS_VORHANDEN, "Warnung", 2);
                    } else {
                        logging.logInfo((Object)("Jahr wird nachgetragen : " + AdministratorAO.this.jahrNachtagen.getText()));
                        jahr.insert(Integer.parseInt(AdministratorAO.this.jahrNachtagen.getText()));
                        logging.logInfo((Object)"data Ordner wird um das aktuelle Jahr erweitert");
                        String jahrOrdner = AdministratorAO.this.jahrNachtagen.getText();
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner, "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Berichte", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Brief", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Einsatzberichte", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Fahrzeugeinteilung", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Temp", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Verdienstausfall", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Beteiligung_uebersicht", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Mangel", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Lehrgangsmeldungen", "SYSTEM");
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahrOrdner + "/Schichten", "SYSTEM");
                        AdministratorAO.this.jahrNachtagen.setText("");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (NumberFormatException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonDebug.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.DEBUG);
                Steuerung.steuerung();
            }
        });
        this.buttonLogbuch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LOGBUCH);
                Steuerung.steuerung();
            }
        });
        this.buttonFTPRekalkulation.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.DATEN_NEU_KATALOGIESIEREN, "Frage", 0);
                if (msg == 0) {
                    runApplication.verarbeitungL\u00e4uft = 1;
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    ProzessBarAO.progressbar.setStringPainted(false);
                    ProzessBarAO.progressbar.setIndeterminate(true);
                    ProzessBarAO.label_bitteWarten.setText("Daten werden Verarbeitet... Bitte haben sie einen Moment Geduld...");
                    Thread threadFTP = new Thread(){

                        @Override
                        public void run() {
                            try {
                                new TabelleFTPSync().deleteAll();
                                new TabelleDateisystem().deleteAll();
                                Utils.rekatalogisiereDateien(String.valueOf(runApplication.arbeitsverzeichnis) + "data");
                            }
                            catch (SQLException e) {
                                runApplication.verarbeitungL\u00e4uft = 0;
                                MyEvent.setEvent((String)"0x0030");
                                logging.logPrintStackTrace((Exception)e);
                            }
                            MyEvent.setEvent((String)"0x0030");
                            runApplication.verarbeitungL\u00e4uft = 0;
                        }
                    };
                    threadFTP.start();
                }
            }
        });
        this.buttonL\u00f6schenKomplett.addActionListener(new ActionListener(){

            @Override
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
                    if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else {
                        int msg2;
                        int vID = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.VERANSTALTUNG_WIRKLICH_L\u00d6SCHEN, "Frage", 0);
                        if (msg == 0 && (msg2 = JOptionPane.showConfirmDialog(null, Konstante.VERANSTALTUNG_WIRKLICH_L\u00d6SCHEN2, "Frage", 0)) == 0) {
                            if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/Einsatzberichte/" + tabBericht.getDateiname(vID);
                                if (new File(dateiname).exists()) {
                                    logging.logInfo((Object)("Einsatzbericht l\u00f6schen --> " + dateiname));
                                    new File(dateiname).delete();
                                    Utils.dateiKatalogisierenForDelete(dateiname);
                                }
                                tabVeranstaltung.deleteOne(vID);
                                tabStatistikEinsatz.deleteOne(vID);
                                tabEinsatz.delete(vID);
                                tabBericht.delete(vID);
                                tabBerichtDaten.delete(vID);
                                tabOrganisation.delete(vID);
                                tabEinsatzZeiten.deleteALL(vID);
                                logging.logInfo((Object)"Einsatz Tabellen wurden vom Datensatz Bereinigt!");
                            } else if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                                tabVeranstaltung.deleteOne(vID);
                                tabBSW.delete(vID);
                                tabStatistikBSW.deleteOne(vID);
                                logging.logInfo((Object)"BSW Tabellen wurden vom Datensatz Bereinigt!");
                            } else {
                                String datum = tabVeranstaltung.getDatum(vID);
                                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + datum.substring(0, 4) + "/Fahrzeugeinteilung/Dienstabend_" + TimeCalculation.parseDateForGUI(datum) + "_ID_" + vID + ".pdf";
                                if (new File(dateiname).exists()) {
                                    new File(dateiname).delete();
                                    Utils.dateiKatalogisierenForDelete(dateiname);
                                }
                                tabVeranstaltung.deleteOne(vID);
                                tabStatistikSonstigeVeranstaltung.deleteOne(vID);
                                tabAusPlan.deleteOne(vID);
                                logging.logInfo((Object)"Sonstige Veranstaltung Tabellen wurden vom Datensatz Bereinigt!");
                            }
                            tabAnwesenheit.delete(vID);
                            tabAbwesenheit.delete(vID);
                            logging.logInfo((Object)"An- / Abwesenheitstabellen wurden vom Datensatz bereinigt!");
                            logbuchEingabe.NeuerEintag("L\u00f6sche Veranstaltung: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                            AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                            AdministratorAO.this.veranstaltung.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonL\u00f6schenAnwesenheit.addActionListener(new ActionListener(){

            @Override
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
                    if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else {
                        int msg2;
                        int vID = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.ANWESENHEIT_WIRKLICH_L\u00d6SCHEN, "Frage", 0);
                        if (msg == 0 && (msg2 = JOptionPane.showConfirmDialog(null, Konstante.VERANSTALTUNG_WIRKLICH_L\u00d6SCHEN2, "Frage", 0)) == 0) {
                            if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                                statistikEinsatz.setVeranstaltungID(vID);
                                statistikEinsatz.setMannstunden(0);
                                tabStatistikEinsatz.updateMannstunden(statistikEinsatz);
                                einsatz.setVeranstaltungID(vID);
                                einsatz.setStaerkeFM(0);
                                einsatz.setStaerkeGF(0);
                                einsatz.setStaerkeZF(0);
                                tabEinsatz.updateStaerke(einsatz);
                                logging.logInfo((Object)"Anwesenheit L\u00f6schen --> Einsatz Tabellen wurden aktualisiert");
                            } else if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                                statistikBSW.setVeranstaltungID(vID);
                                statistikBSW.setMannstunden(0);
                                tabStatistikBSW.updateMannstunden(statistikBSW);
                                logging.logInfo((Object)"Anwesenheit L\u00f6schen --> BSW Tabellen wurden aktualisiert");
                            } else {
                                statistikSonstige.setVeranstaltungID(vID);
                                statistikSonstige.setMannstunden(0);
                                tabStatistikSonstigeVeranstaltung.updateMannstunden(statistikSonstige);
                                logging.logInfo((Object)"Anwesenheit L\u00f6schen --> Sonstige Satistik Tabellen wurden aktualisiert");
                            }
                            tabAnwesenheit.delete(vID);
                            logbuchEingabe.NeuerEintag("L\u00f6sche Anwesenheit: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                            AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                            AdministratorAO.this.veranstaltung.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonL\u00f6schenAbwesenheit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
                try {
                    if (AdministratorAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else {
                        int msg2;
                        int vID = tabVeranstaltung.getVeranstaltungID(AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.ABWESENHEIT_WIRKLICH_L\u00d6SCHEN, "Frage", 0);
                        if (msg == 0 && (msg2 = JOptionPane.showConfirmDialog(null, Konstante.VERANSTALTUNG_WIRKLICH_L\u00d6SCHEN2, "Frage", 0)) == 0) {
                            tabAbwesenheit.delete(vID);
                            logging.logInfo((Object)"Abwesenheit L\u00f6schen --> Tabelle wurde bereinigt!");
                            logbuchEingabe.NeuerEintag("L\u00f6sche Abwesenheit: " + AdministratorAO.this.veranstaltung.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                            AdministratorAO.this.veranstaltung.removeItem(AdministratorAO.this.veranstaltung.getSelectedItem());
                            AdministratorAO.this.veranstaltung.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
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

