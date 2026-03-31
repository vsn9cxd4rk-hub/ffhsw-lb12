package ao.einstellungen;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.einstellungen.CreateDatabase;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleMandant;
import data.tabellen.mitglied.TabelleMitglied;
import go.Mandant;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import listener.DisposeListener;
import logging.logging;
import run.images;
import run.runApplication;
import run.update.UpdateDatenbank;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.hash;
import utilities.facebook.Facebook;
import utilities.joomla.Joomla;
import utilities_email.ErstelleFileArrayForAnhang;
import utilities_email.SendeOpperation;

public class EinstellungAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonspeichern;
   private JButton buttonEinsatzberichtAuswahl;
   private JButton buttonVerdienstausfallAuswahl;
   private JButton buttonMängelmeldungAuswahl;
   private JButton buttonBestaetignungFreistellungEinsatzAuswahl;
   private JButton buttonBriefkopfAuswahl;
   private JButton buttonTestEMail;
   private JButton buttonZyklischenEMailAuftragErstellen;
   private JButton buttonTerminDisplayHintergrund;
   private JButton buttonExtendFacebookAccessToken;
   private JButton buttonFacebookDevelpmentPage;
   private JLabel kurzbeschreibung;
   private JLabel kurzbeschreibung2;
   private JLabel einsatzbericht_label;
   private JLabel mängelmeldung_label;
   private JLabel name_label;
   private JLabel stadt_label;
   private JLabel straße_label;
   private JLabel tel_label;
   private JLabel plz_label;
   private JLabel bundesland_label;
   private JLabel verdienstausfall_label;
   private JLabel fehlalarm_label;
   private JLabel briefkopf_label;
   private JLabel vorwarnunUntersuchung_label;
   private JLabel vorwarnungFahrzuegUntersuchung_label;
   private JLabel vorwarnungGeräteprüfung_label;
   private JLabel gebAnzeigen_label;
   private JLabel geburtstageNurHeute_label;
   private JLabel geburtstageNurGanzerMonat_label;
   private JLabel terminAnzeigen_label;
   private JLabel agttrainingAnzeigen_label;
   private JLabel autoBerichtAktiv_label;
   private JLabel zeitAutoBericht_label;
   private JLabel lehrgangsmeldungen_label;
   private JLabel smtpServer_label;
   private JLabel smtpServerPort_label;
   private JLabel popServer_label;
   private JLabel popServerPort_label;
   private JLabel emailAdresse_label;
   private JLabel emailName_label;
   private JLabel emailPasswort_label;
   private JLabel emailModulAktivieren_label;
   private JLabel useSSL_label;
   private JLabel vorbelegungDienstabendStart_label;
   private JLabel vorbelegungDienstabendEnde_label;
   private JLabel vorbelegungBSWTreffen_label;
   private JLabel vorbelegungBSWVeranstaltungStart_label;
   private JLabel vorbelegungBSWEnde_label;
   private JLabel untersuchungViaEMail_label;
   private JLabel untersuchungViaEMailChefBCC_label;
   private JLabel terminVersandtViaEMail_label;
   private JLabel terminVersandtViaEMailConfig_label;
   private JLabel vCardSeperator_label;
   private JLabel ablaufLKWFührerscheinViaEMail_label;
   private JLabel ablaufLKWFuehrerscheinAnzeigen_label;
   private JLabel automatischesProgrammUpdate_label;
   private JLabel einsatzleiterBFAnzeigen_label;
   private JLabel bswHitliste_label;
   private JLabel abrechnungModuleAnzeigen_label;
   private JLabel geraetepruefungenViaEMail_label;
   private JLabel offeneMängelAnzeigen_label;
   private JLabel fahrzeugUntersuchungViaEMail_label;
   private JLabel mängelmeldungenViaEMailVersenden_label;
   private JLabel schichtModul_label;
   private JLabel urlaubModul_label;
   private JLabel fahrtenbuchModul_label;
   private JLabel sichtbarkeitVonVeranstaltungenZukunft_label;
   private JLabel sichtbarkeitVonVeranstaltungenVergangenheit_label;
   private JLabel joomlaLink_label;
   private JLabel joomlaVeranstaltung_label;
   private JLabel joomlaAusbildungsplan_label;
   private JLabel joomlaEinsatzkomponente_label;
   private JLabel joomlaEinsatzkomponente_Visible_label;
   private JLabel joomlaEinsatzkomponente_Config_label;
   private JLabel joomlaEinsatzkomponenteEMail_label;
   private JLabel joomlaEinsatzkomponenteEMailAn1_label;
   private JLabel joomlaEinsatzkomponenteEMailAn2_label;
   private JLabel joomlaEinsatzkomponenteEMailAn3_label;
   private JLabel joomlaEinsatzkomponenteSecretKey_label;
   private JLabel joomlaEinsatzkomponenteStichwort_label;
   private JLabel alwaysOnTop_label;
   private JLabel globaleEMailGerätewarte_label;
   private JLabel globaleEMailEinheitsführung_label;
   private JLabel globaleEMailGerätewarteAktivieren_label;
   private JLabel globaleEMailEinheitsführungAktivieren_label;
   private JLabel statistik2_label;
   private JLabel einsatznummerPflichteintrag_label;
   private JLabel einsatzleiterBFPflichteintrag_label;
   private JLabel schutzziel1_label;
   private JLabel schutzziel2_label;
   private JLabel datenbankTyp_label;
   private JLabel datenbankIP_label;
   private JLabel datenbankName_label;
   private JLabel datenbankPasswort_label;
   private JLabel datenbankPasswort2_label;
   private JLabel organisation_label;
   private JLabel sshServer_label;
   private JLabel ftpServer_label;
   private JLabel datenbankUser_label;
   private JLabel einsatzBerichtArt_label;
   private JLabel verdienstausfallArt_label;
   private JLabel maengelmeldungArt_label;
   private JLabel verdienstausfallEinsatzbeschreibungOption_label;
   private JLabel headerPrint_label;
   private JLabel footerPrint_label;
   private JLabel zeilenhöheAnsicht_label;
   private JLabel zeilenhöheDruck_label;
   private JLabel modulVeranstaltung_label;
   private JLabel modulAusbildungsplan_label;
   private JLabel modulFahrzeugeinteilung_label;
   private JLabel getakteteInternetverbindung_label;
   private JLabel onlineStatus_label;
   private JLabel JoomlaEinsatzKomponenteNurAlamierungÜbertragen_label;
   private JLabel JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln_label;
   private JLabel organisationen_label;
   private JLabel fullBackupPath_label;
   private JLabel fullBackupInZip_label;
   private JLabel mitgliedSeitFormat_label;
   private JLabel hochzeitFeldFuerMitglieder_label;
   private JLabel mitgliedSeitPflichtEintrag_label;
   private JLabel LehrgangEintragenAusMitgliederVerwaltungMode_label;
   private JLabel unwetterwarnungPop3_label;
   private JLabel unwetterwarnungEMail_label;
   private JLabel unwetterwarnungPopPort_label;
   private JLabel unwetterwarnungPasswort_label;
   private JLabel unwetterwarnungSSL_label;
   private JLabel unwetterwarnungModulAktiv_label;
   private JLabel unwetterwarnungWebKonfig_label;
   private JLabel vorwarnungAblaufDienstausweis_label;
   private JLabel ablaufDienstausweisAnzeigen_label;
   private JLabel ablaufDienstausweisViaEMail_label;
   private JLabel prüfungDerFahrerlaubnis_label;
   private JLabel darstellungLehrgängeMitgliederverwaltung_label;
   private JLabel ablaufFahrberechtigungAnzeigen_label;
   private JLabel vorwarnungAblaufFahrberechtigung_label;
   private JLabel ablaufFahrberechtigungViaEMail_label;
   private JLabel druckAnwesenheitsListeMode1_label;
   private JLabel druckAnwesenheitsListeMode2_label;
   private JLabel dienstgradAufAnwesenheitsliste_label;
   private JLabel modulMitgliederVerfügbarkeit_label;
   private JLabel schfiftgrößeAnwesenheitsliste_label;
   private JLabel bestaetignungFreistellungEinsatzArt_label;
   private JLabel bestaetignungFreistellungEinsatzAktiv_label;
   private JLabel bestaetignungFreistellungEinsatz_label;
   private JLabel modulEinsatzgebiet_label;
   private JLabel globaleEMailG25Aktiviert_label;
   private JLabel globaleEMailG25_label;
   private JLabel globaleEMailG26Aktiviert_label;
   private JLabel globaleEMailG26_label;
   private JLabel globaleEMailFahrberechtigungAktiviert_label;
   private JLabel globaleEMailFahrberechtigung_label;
   private JLabel globaleEMailDienstausweisAktiviert_label;
   private JLabel globaleEMailDienstausweis_label;
   private JLabel EinsatzBerichtFahrzeugbelegungHinzufügen_label;
   private JLabel EinsatzBerichtAtemschutzpassHinzufügen_label;
   private JLabel EinsatzBerichtEinsatzleiterMitDienstgrad_label;
   private JLabel feldEintreffenAusblenden_label;
   private JLabel feldStadtteilAusblenden_label;
   private JLabel langesDatumsformatUntersuchungsliste_label;
   private JLabel LookAndFeel_label;
   private JLabel anmeldungSpeichernErlauben_label;
   private JLabel terminVersandtViaEMailFolgeMonat_label;
   private JLabel TerminDisplay_AnzeigeAnazahlVeranstaltungen_label;
   private JLabel TerminDisplay_AnzeigeAnazahlVeranstaltungListe_label;
   private JLabel TerminDisplay_AnzeigeDauerVeranstaltungen_label;
   private JLabel TerminDisplay_AnzeigeDauerUhr_label;
   private JLabel TerminDisplay_AnzeigenLetzenEinsatz_label;
   private JLabel TerminDisplay_HintergrundBildAktivieren_label;
   private JLabel TerminDisplay_LetzterEinsatzOrtAnzeigen_label;
   private JLabel TerminDisplay_HintergrundBild_label;
   private JLabel schulungAdminModul_label;
   private JLabel schulungClientModule_label;
   private JLabel facebookAccessToken_label;
   private JLabel facebookAutoPostEinsatz_label;
   private JLabel facebookAppID_label;
   private JLabel facebookAppGeheimCode_label;
   private JLabel facebookEMail_label;
   private JLabel facebookEMailAn1_label;
   private JLabel facebookEMailAn2_label;
   private JLabel facebookEMailAn3_label;
   private JLabel mitgliederGesundheitTab_label;
   private JCheckBox automatischeFrageNachAtemschutzEinsatz;
   private JTextField google_api_code;
   private JTextField default_location;
   private JCheckBox externeDatenbankFürKartendaten;
   private JComboBox externeKartenDB_Typ;
   private JTextField externeKartenDatenbankPort;
   private JTextField externeKartenDatenbankIP;
   private JTextField externeKartenDatenbankName;
   private JTextField externeKartenDatenbankUser;
   private JPasswordField externeKartenDatenbankPasswort;
   private JTextField externeKartenSSHUser;
   private JPasswordField externeKartenSSHPasswort;
   private JTextField externeKartenSSHServer;
   private JTextField externeKartenSSHServerPort;
   private JTextField externeKartenSSHTunnel;
   private JCheckBox externeKartenDatenbankLokalesBackup;
   private JTextField verdienstausfall;
   private JTextField einsatzbericht;
   private JTextField mängelmeldung;
   private JTextField name;
   private JTextField stadt;
   private JTextField straße;
   private JTextField tel;
   private JTextField plz;
   private JComboBox bundesland;
   private JTextField fehlalarm;
   private JTextField briefkopf;
   private JTextField vorwarnunUntersuchung;
   private JTextField vorwarnungFahrzeugUntersuchung;
   private JTextField vorwarnungGeräteprüfung;
   private JCheckBox gebAnzeigen;
   private JRadioButton geburtstageNurHeute;
   private JRadioButton geburtstageNurGanzerMonat;
   private ButtonGroup BgGeburtstage;
   private JCheckBox terminAnzeigen;
   private JCheckBox agttrainingAnzeigen;
   private JCheckBox autoBerichtAktiv;
   private JCheckBox emailModulAktivieren;
   private JCheckBox useSSL;
   private JTextField zeitAutoBericht;
   private JTextField lehrgangsmeldungen;
   private JTextField smtpServer;
   private JTextField popServer;
   private JTextField smtpServerPort;
   private JTextField popServerPort;
   private JTextField emailAdresse;
   private JTextField emailName;
   private JPasswordField emailPasswort;
   private JTextField vorbelegungDienstabendStart;
   private JTextField vorbelegungDienstabendEnde;
   private JTextField vorbelegungBSWTreffen;
   private JTextField vorbelegungBSWVeranstaltungStart;
   private JTextField vorbelegungBSWEnde;
   private JCheckBox schnittstellenAktivierung;
   private JLabel schnittstellenAktivierung_label;
   private JCheckBox autoDBsave;
   private JLabel autoDBsave_label;
   private JTextField autoDBsaveTage;
   private JLabel autoDBsaveTage_label;
   private JCheckBox untersuchungViaEMail;
   private JCheckBox ablaufLKWFührerscheinViaEMail;
   private JCheckBox ablaufLKWFuehrerscheinAnzeigen;
   private JCheckBox untersuchungViaEMailChefBCC;
   private JCheckBox terminVersandtViaEMail;
   private JComboBox terminVersandtViaEMailConfig;
   private JComboBox vCardSeperator;
   private JCheckBox automatischesProgrammUpdate;
   private JCheckBox einsatzleiterBFAnzeigen;
   private JCheckBox bswHitliste;
   private JCheckBox abrechnungModuleAnzeigen;
   private JCheckBox geraetepruefungenViaEMail;
   private JCheckBox offeneMängelAnzeigen;
   private JTextField datenbankTyp;
   private JTextField datenbankIP;
   private JTextField datenbankName;
   private JPasswordField datenbankPasswort;
   private JPasswordField datenbankPasswort2;
   private JTextField datenbankUser;
   private JTextField organisation;
   private JTextField sshServer;
   private JTextField ftpServer;
   private JCheckBox fahrzeugUntersuchungViaEMail;
   private JCheckBox mängelmeldungViaEMailVersenden;
   private JComboBox einsatzberichtArt;
   private JComboBox verdienstausfallArt;
   private JComboBox maengelmeldungArt;
   private JCheckBox schichtModul;
   private JCheckBox urlaubModul;
   private JCheckBox fahrtenbuchModul;
   private JComboBox sichtbarkeitVonVeranstaltungenZukunft;
   private JComboBox sichtbarkeitVonVeranstaltungenVergangenheit;
   private JTextField joomlaLink;
   private JCheckBox joomlaVeranstaltung;
   private JCheckBox joomlaAusbildungsplan;
   private JCheckBox joomlaEinsatzkomponente;
   private JCheckBox joomlaEinsatzkomponente_Visible;
   private JComboBox joomlaEinsatzkomponente_Config;
   private JCheckBox joomlaEinsatzkomponenteEMail;
   private JComboBox joomlaEinsatzkomponenteEMailAn1;
   private JComboBox joomlaEinsatzkomponenteEMailAn2;
   private JComboBox joomlaEinsatzkomponenteEMailAn3;
   private JTextField joomlaEinsatzkomponenteSecretKey;
   private JComboBox joomlaEinsatzkomponenteStichwort;
   private JCheckBox alwaysOnTop;
   private JTextField globaleEMailGerätewarte;
   private JTextField globaleEMailEinheitsführung;
   private JCheckBox globaleEMailGerätewarteAktivieren;
   private JCheckBox globaleEMailEinheitsführungAktivieren;
   private JCheckBox statistik2;
   private JCheckBox einsatznummerPflichteintrag;
   private JCheckBox einsatzLeiterBFPflichteintrag;
   private JTextField schutzziel1;
   private JTextField schutzziel2;
   private JComboBox verdienstausfallEinsatzbeschreibungOption;
   private JCheckBox headerPrint;
   private JCheckBox footerPrint;
   private JTextField zeilenhöheDruck;
   private JTextField zeilenhöheAnsicht;
   private JCheckBox modulVeranstaltung;
   private JCheckBox modulAusbildungsplan;
   private JCheckBox modulFahrzeugeinteilung;
   private JCheckBox getakteteInternetverbindung;
   private JCheckBox onlineStatus;
   private JCheckBox JoomlaEinsatzKomponenteNurAlamierungÜbertragen;
   private JCheckBox JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln;
   private JCheckBox organisationen;
   private JTextField fullBackupPath;
   private JCheckBox fullBackupInZip;
   private JComboBox mitgliedSeitFormat;
   private JCheckBox hochzeitFeldFuerMitglieder;
   private JCheckBox mitgliedSeitPflichtEintrag;
   private JCheckBox LehrgangEintragenAusMitgliederVerwaltungMode;
   private JTextField unwetterwarnungPop3;
   private JTextField unwetterwarnungEMail;
   private JTextField unwetterwarnungPopPort;
   private JTextField unwetterwarnungPasswort;
   private JCheckBox unwetterwarnungSSL;
   private JCheckBox unwetterwarnungModulAktiv;
   private JButton unwetterwarnungWebKonfig;
   private JTextField vorwarnungAblaufDienstausweis;
   private JCheckBox ablaufDienstausweisAnzeigen;
   private JCheckBox ablaufDienstausweisViaEMail;
   private JCheckBox prüfungDerFahrerlaubnis;
   private JComboBox darstellungLehrgängeMitgliederverwaltung;
   private JCheckBox ablaufFahrberechtigungAnzeigen;
   private JTextField vorwarnungAblaufFahrberechtigung;
   private JCheckBox ablaufFahrberechtigungViaEMail;
   private JRadioButton druckAnwesenheitsListeMode1;
   private JRadioButton druckAnwesenheitsListeMode2;
   private ButtonGroup BgDruckAnwesenheitsListeMode;
   private JCheckBox dienstgradAufAnwesenheitsliste;
   private JCheckBox modulMitgliederVerfügbarkeit;
   private JComboBox schfiftgrößeAnwesenheitsliste;
   private JComboBox bestaetignungFreistellungEinsatzArt;
   private JCheckBox bestaetignungFreistellungEinsatzAktiv;
   private JTextField bestaetignungFreistellungEinsatz;
   private JCheckBox modulEinsatzgebiet;
   private JCheckBox globaleEMailG25Aktiviert;
   private JTextField globaleEMailG25;
   private JCheckBox globaleEMailG26Aktiviert;
   private JTextField globaleEMailG26;
   private JCheckBox globaleEMailFahrberechtigungAktiviert;
   private JTextField globaleEMailFahrberechtigung;
   private JCheckBox globaleEMailDienstausweisAktiviert;
   private JTextField globaleEMailDienstausweis;
   private JCheckBox EinsatzBerichtFahrzeugbelegungHinzufügen;
   private JCheckBox EinsatzBerichtAtemschutzpassHinzufügen;
   private JCheckBox EinsatzBerichtEinsatzleiterMitDienstgrad;
   private JCheckBox feldEintreffenAusblenden;
   private JCheckBox feldStadtteilAusblenden;
   private JCheckBox langesDatumsformatUntersuchungsliste;
   private JComboBox LookAndFeel;
   private JCheckBox anmeldungSpeichernErlauben;
   private JCheckBox terminVersandtViaEMailFolgeMonat;
   private JComboBox TerminDisplay_AnzeigeAnazahlVeranstaltungen;
   private JComboBox TerminDisplay_AnzeigeAnazahlVeranstaltungListe;
   private JTextField TerminDisplay_AnzeigeDauerVeranstaltungen;
   private JTextField TerminDisplay_AnzeigeDauerUhr;
   private JCheckBox TerminDisplay_AnzeigenLetzenEinsatz;
   private JCheckBox TerminDisplay_HintergrundBildAktivieren;
   private JCheckBox TerminDisplay_LetzterEinsatzOrtAnzeigen;
   private JTextField TerminDisplay_HintergrundBild;
   private JCheckBox schulungAdminModul;
   private JCheckBox schulungClientModul;
   private JTextField facebookAccessToken;
   private JCheckBox facebookAutoPostEinsatz;
   private JTextField facebookAppID;
   private JTextField facebookAppGeheimCode;
   private JCheckBox facebookEMail;
   private JComboBox facebookEMailAn1;
   private JComboBox facebookEMailAn2;
   private JComboBox facebookEMailAn3;
   private JCheckBox mitgliederGesundheitTab;
   private JLabel automatischeFrageNachAtemschutzEinsatz_label;
   private JLabel google_api_code_label;
   private JLabel default_location_label;
   private JLabel externeDatenbankFürKartendaten_label;
   private JLabel externeKartenDB_Typ_label;
   private JLabel externeKartenDatenbankPort_label;
   private JLabel externeKartenDatenbankIP_label;
   private JLabel externeKartenDatenbankName_label;
   private JLabel externeKartenDatenbankUser_label;
   private JLabel externeKartenDatenbankPasswort_label;
   private JLabel externeKartenSSHUser_label;
   private JLabel externeKartenSSHPasswort_label;
   private JLabel externeKartenSSHServer_label;
   private JLabel externeKartenSSHServerPort_label;
   private JLabel externeKartenSSHTunnel_label;
   private JLabel externeKartenDatenbankLokalesBackup_label;
   private JTabbedPane tabPane;
   private JPanel panelEinsatzBericht;
   private JPanel panelAnschrift;
   private JPanel panelStatistikKonfiguration;
   private JPanel panelAlarmKonfiguration;
   private JPanel panelAutoBericht;
   private JPanel panelLehrgang;
   private JPanel panelEMail;
   private JPanel panelVeranstaltung;
   private JPanel panelVeranstaltungBerichte;
   private JPanel panelSonstiges;
   private JPanel panelDbSicherung;
   private JPanel panelDatenbankEinstellungen;
   private JPanel panelEMailInformation;
   private JPanel panelModule;
   private JPanel panelJoomla;
   private JPanel panelFacebook;
   private JPanel panelGlobaleEMailAdressen;
   private JPanel panelDruckOptionen;
   private JPanel panelMitgliederverwaltung;
   private JPanel panelDWDUnwetterwarnung;
   private JPanel panelTerminDisplay;
   private JPanel panelExterneKartenDatenbank;
   private JFileChooser chooserXML;
   private JFileChooser chooserJPEG;
   private FileNameExtensionFilter filterJPEG;
   private FileNameExtensionFilter filterXML;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public EinstellungAO() {
      super("FeuerwehrManagementSystem - Einstellungen");
      logging.logInfo("Starte: EinstellungAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonspeichern = new JButton("Speichern");
      this.buttonEinsatzberichtAuswahl = new JButton("...");
      this.buttonVerdienstausfallAuswahl = new JButton("...");
      this.buttonBriefkopfAuswahl = new JButton("...");
      this.buttonMängelmeldungAuswahl = new JButton("...");
      this.buttonBestaetignungFreistellungEinsatzAuswahl = new JButton("...");
      this.buttonTestEMail = new JButton("Test E-Mail verschicken");
      this.buttonZyklischenEMailAuftragErstellen = new JButton();
      this.buttonTerminDisplayHintergrund = new JButton("...");
      this.buttonExtendFacebookAccessToken = new JButton("Facebook API Einstellungen prüfen / Gültigeit verlängern");
      this.buttonFacebookDevelpmentPage = new JButton("Facebook Development Seite - AccessToken");
      if(((String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("ZyklischerEMailAuftrag")).equals("0")) {
         this.buttonZyklischenEMailAuftragErstellen.setText("Zyklischen E-Mail Auftrag erstellen");
      } else {
         this.buttonZyklischenEMailAuftragErstellen.setText("Zyklischen E-Mail Auftrag löschen");
      }

      this.kurzbeschreibung = new JLabel("** E-Mails werden versendet, sobald die Informationen im Informationsberich angezeigt werden. Diese Einstellung ist unter \"Informationskonfiguration\" konfigurierbar.");
      this.kurzbeschreibung2 = new JLabel("*** Nach dem ändern dieser Einstellungen ist ein Neustart, des FeuerwehrManagementSystem erforderlich, damit alle Änderungen übernommen werden können!");
      this.einsatzbericht_label = new JLabel("Einsatzbericht: ");
      this.name_label = new JLabel("Name der Einheit:");
      this.stadt_label = new JLabel("Stadt: ");
      this.verdienstausfall_label = new JLabel("Verdienstausfallbescheinigung: ");
      this.fehlalarm_label = new JLabel("Fehlalarmauswertung (Dauer für Fehlalarmwertung in Minuten): ");
      this.tel_label = new JLabel("Telefon: ");
      this.plz_label = new JLabel("PLZ: ");
      this.straße_label = new JLabel("Straße: ");
      this.briefkopf_label = new JLabel("Briefkopf (Größe: 1000x150 Pixel):");
      this.vorwarnunUntersuchung_label = new JLabel("Vorwarnung für Mitglieder-Untersuchungen / Ablauf LKW Führerschein in Monaten: ");
      this.vorwarnungFahrzuegUntersuchung_label = new JLabel("Vorwarnung für TÜV, SP, Wartung in Monaten: ");
      this.gebAnzeigen_label = new JLabel("Geburtstage anzeigen: ");
      this.terminAnzeigen_label = new JLabel("Termine anzeigen: ");
      this.agttrainingAnzeigen_label = new JLabel("Atemschutztraining anzeigen: ");
      this.autoBerichtAktiv_label = new JLabel("Archivierung aktivieren: ");
      this.zeitAutoBericht_label = new JLabel("Häufigkeit in Tagen: ");
      this.mängelmeldung_label = new JLabel("Mängelmeldung: ");
      this.vorwarnungGeräteprüfung_label = new JLabel("Vorwarnung für Geräteprüfung in Monaten: ");
      this.lehrgangsmeldungen_label = new JLabel("Lehrgangsmeldungen pro Jahr: ");
      this.smtpServer_label = new JLabel("SMTP-Server: ");
      this.popServer_label = new JLabel("POP3-Server: ");
      this.smtpServerPort_label = new JLabel("SMTP-Server Port: ");
      this.popServerPort_label = new JLabel("POP3-Server Port: ");
      this.emailAdresse_label = new JLabel("E-Mail Adresse: ");
      this.emailName_label = new JLabel("Angezeigter Name: ");
      this.emailPasswort_label = new JLabel("E-Mail Passwort: ");
      this.emailModulAktivieren_label = new JLabel("Email Modul Aktivieren *** : ");
      this.useSSL_label = new JLabel("SSL/TLS: ");
      this.vorbelegungDienstabendStart_label = new JLabel("Vorbelegung Dienstanfang: ");
      this.vorbelegungDienstabendEnde_label = new JLabel("Vorbelegung Dienstende: ");
      this.vorbelegungBSWTreffen_label = new JLabel("Vorbelegung BSW Treffen: ");
      this.vorbelegungBSWVeranstaltungStart_label = new JLabel("Vorbelegung BSW Veranstaltung Start: ");
      this.vorbelegungBSWEnde_label = new JLabel("Vorbelegung BSW Ende: ");
      this.schnittstellenAktivierung_label = new JLabel("Einsatz Schnittstelle:");
      this.schnittstellenAktivierung_label.setToolTipText("Wenn die Einsatzschnittstelle aktiviert ist wird im Falle eines Einsatzes Automatisch die Karte mit allesn informationen aufgerufen");
      this.autoDBsave_label = new JLabel("Automatische Datenbanksicherung *** : ");
      this.autoDBsaveTage_label = new JLabel("Datenbanksicherung in Tagen:");
      this.untersuchungViaEMail_label = new JLabel("Mitglieder über G25, G26/3, G30 per E-Mail informieren ** : ");
      this.untersuchungViaEMail_label.setToolTipText("Mitgieder werden automatisch per E-Mail über die anstehenden Untersuchungen Infomiert.");
      this.untersuchungViaEMailChefBCC_label = new JLabel("E-Mail an die Wehrleiter in BCC: ");
      this.terminVersandtViaEMail_label = new JLabel("Termine automatisch per E-Mail versenden: ");
      this.terminVersandtViaEMailConfig_label = new JLabel("  -- Termine versenden konfiguration: ");
      this.vCardSeperator_label = new JLabel("Trennzeichen für vCard Export: ");
      this.ablaufLKWFührerscheinViaEMail_label = new JLabel("Mitglied über den Ablauf LKW Führerschein per E-Mail informieren ** : ");
      this.ablaufLKWFuehrerscheinAnzeigen_label = new JLabel("Ablauf LKW Führerschein anzeigen: ");
      this.automatischesProgrammUpdate_label = new JLabel("automatisches Programmupdate: ");
      this.einsatzleiterBFAnzeigen_label = new JLabel("Einsatzleiter BF anzeigen: ");
      this.bswHitliste_label = new JLabel("Hitliste für BSW\'s anzeigen (nach dem Erstellen einer BSW): ");
      this.bundesland_label = new JLabel("Bundesland: ");
      this.abrechnungModuleAnzeigen_label = new JLabel("Abrechnungsmodul aktivieren *** : ");
      this.geraetepruefungenViaEMail_label = new JLabel("Gerätewarte über Geräteprüfung per E-Mail informieren ** : ");
      this.datenbankTyp_label = new JLabel("Datenbank Typ: ");
      this.datenbankIP_label = new JLabel("Datenbank IP: ");
      this.datenbankName_label = new JLabel("Datenbank Name: ");
      this.datenbankUser_label = new JLabel("Datenbank User: ");
      this.datenbankPasswort_label = new JLabel("Passwort: ");
      this.datenbankPasswort2_label = new JLabel("Passwort wdh.: ");
      this.organisation_label = new JLabel("Organisationstyp: ");
      this.sshServer_label = new JLabel("SSH Server IP: ");
      this.ftpServer_label = new JLabel("FTP Server IP: ");
      this.offeneMängelAnzeigen_label = new JLabel("Offene Mängelmeldungen anzeigen: ");
      this.fahrzeugUntersuchungViaEMail_label = new JLabel("Gerätewarte über anstehende Fahrzeuguntersuchungen per E-Mail informieren ** : ");
      this.mängelmeldungenViaEMailVersenden_label = new JLabel("Erstelle Mängelmeldungen per E-Mail an die Gerätewarte senden:");
      this.geburtstageNurHeute_label = new JLabel("Geburtstage von Heute anzeigen: ");
      this.geburtstageNurGanzerMonat_label = new JLabel("Geburtstage vom ganzen Monat anzeigen: ");
      this.einsatzBerichtArt_label = new JLabel("Form des Einsatzberichtes: ");
      this.maengelmeldungArt_label = new JLabel("Form der Mängelmeldung: ");
      this.verdienstausfallArt_label = new JLabel("Form der Verdienstausfallbescheinigung: ");
      this.schichtModul_label = new JLabel("Schichtplaner aktivieren *** : ");
      this.urlaubModul_label = new JLabel("Urlaubsplaner aktivieren *** : ");
      this.fahrtenbuchModul_label = new JLabel("Fahrtenbuch aktivieren *** : ");
      this.sichtbarkeitVonVeranstaltungenVergangenheit_label = new JLabel("Sichtbarkeit von Veranstaltungen rückwirkend in Monaten: ");
      this.sichtbarkeitVonVeranstaltungenZukunft_label = new JLabel("Sichtbarkeit von zukünftigen Veranstaltungen in Monaten: ");
      this.joomlaLink_label = new JLabel("Link zur Joomla Seite:");
      this.joomlaVeranstaltung_label = new JLabel("Veranstaltungen auf Joomla übertragen:");
      this.joomlaAusbildungsplan_label = new JLabel("Ausbildungsplan auf Joomla übertragen");
      this.joomlaEinsatzkomponente_label = new JLabel("Einsatz an Joomla Einsatzkomponente übertragen: ");
      this.joomlaEinsatzkomponente_Visible_label = new JLabel("  -- Einsatz direkt veröffentlichen (Deaktiviert nur hochladen): ");
      this.joomlaEinsatzkomponente_Config_label = new JLabel("  -- Ort Optionen: ");
      this.joomlaEinsatzkomponenteStichwort_label = new JLabel("  -- Stichwort Optionen:");
      this.joomlaEinsatzkomponenteEMail_label = new JLabel("  -- E-Mail senden bei neuem Einsatz für die Einsatzkomponente: ");
      this.joomlaEinsatzkomponenteEMailAn1_label = new JLabel("  --  -- E-Mail an senden (1): ");
      this.joomlaEinsatzkomponenteEMailAn2_label = new JLabel("  --  -- E-Mail an senden (2): ");
      this.joomlaEinsatzkomponenteEMailAn3_label = new JLabel("  --  -- E-Mail an senden (3): ");
      this.joomlaEinsatzkomponenteSecretKey_label = new JLabel(" -- Sicherheitsschlüssel (muss übereinstimmen): ");
      this.alwaysOnTop_label = new JLabel("Programm immer im Vordergrund anzeigen: ");
      this.globaleEMailGerätewarte_label = new JLabel("Globale E-Mail Adresse der Gerätewarten: ");
      this.globaleEMailEinheitsführung_label = new JLabel("Globale E-Mail Adresse der Wehrleitung: ");
      this.globaleEMailGerätewarteAktivieren_label = new JLabel("Gobale E-Mail Adresse Gerätewarte aktivieren (Deaktive - Private): ");
      this.globaleEMailEinheitsführungAktivieren_label = new JLabel("Gobale E-Mail Adresse Wehrleitung aktivieren (Deaktive - Private): ");
      this.statistik2_label = new JLabel("Neue Statistik Oberfläche aktivieren: ");
      this.einsatznummerPflichteintrag_label = new JLabel("Einsatznummer als Pflichteingabe deklarieren (Einsatz anlegen): ");
      this.einsatzleiterBFPflichteintrag_label = new JLabel("Einsatzleiter BF als Pflichteingabe deklarieren (Einsatz anlegen): ");
      this.schutzziel1_label = new JLabel("Schutzziel (Hilfsfrist) Stufe 1 nach AGBF: ");
      this.schutzziel2_label = new JLabel("Schutzziel (Hilfsfrist) Stufe 2 nach AGBF (Nachrückende Kräfte): ");
      this.verdienstausfallEinsatzbeschreibungOption_label = new JLabel("Darstellung Einsatzinformationen(Verdienstausfallbescheinigung): ");
      this.headerPrint_label = new JLabel("Listendruck - Kopfzeile: ");
      this.footerPrint_label = new JLabel("Listendruck - Fußzeile: ");
      this.zeilenhöheAnsicht_label = new JLabel("Zeilenhöhe in der Ansicht: ");
      this.zeilenhöheDruck_label = new JLabel("Zeilenhöhe der Tabelle beim Druck: ");
      this.modulVeranstaltung_label = new JLabel("Veranstaltungsmodul aktivieren *** : ");
      this.modulAusbildungsplan_label = new JLabel("Ausbildungsmodul aktivieren *** : ");
      this.modulFahrzeugeinteilung_label = new JLabel("Fahrzeugeinteilungsmodul aktivieren *** : ");
      this.getakteteInternetverbindung_label = new JLabel("Getaktete oder langsame Internetverbindung: ");
      this.getakteteInternetverbindung_label.setToolTipText("Wenn Sie diese Einstellung aktiviert haben, werden die Skripte für die Joomla Schnittstelle nicht sofort ausgefürt. Sie werden beim Beenden des Programms ausgeführt!");
      this.onlineStatus_label = new JLabel("Online Status anzeigen *** : ");
      this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen_label = new JLabel(" -- Unterdrücken der Ausrücke- u. Einsatzendezeiten: ");
      this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln_label = new JLabel(" -- Einsatzbericht aus dem \"Protokoll / Tätigkeitsbericht\" übermitteln: ");
      this.organisationen_label = new JLabel("Weitere Einheiten / Organisationen: ");
      this.fullBackupInZip_label = new JLabel("Komplettes Backup aktivieren: ");
      this.fullBackupPath_label = new JLabel("Speicherort der Backup Datei: ");
      this.mitgliedSeitFormat_label = new JLabel("Format \"Mitglied Seit\": ");
      this.hochzeitFeldFuerMitglieder_label = new JLabel("Feld Hochzeitstag für Mitglieder aktivieren: ");
      this.mitgliedSeitPflichtEintrag_label = new JLabel("\"Mitglied Seit\" als Pflichteintrag setzen: ");
      this.LehrgangEintragenAusMitgliederVerwaltungMode_label = new JLabel("Komplexe Laufbahnpflege aus der Mitgliederverwaltung: ");
      this.unwetterwarnungEMail_label = new JLabel("E-Mail Adresse für Unwetterwarnungen: ");
      this.unwetterwarnungModulAktiv_label = new JLabel("Unwettermodul einschalten *** : ");
      this.unwetterwarnungPasswort_label = new JLabel("E-Mail Passwort: ");
      this.unwetterwarnungPop3_label = new JLabel("POP3 Server: ");
      this.unwetterwarnungPopPort_label = new JLabel("POP3-Port: ");
      this.unwetterwarnungSSL_label = new JLabel("SSL aktivieren: ");
      this.unwetterwarnungWebKonfig_label = new JLabel("DWD Web Konfiguration für E-Mail Benachrichtigung: ");
      this.vorwarnungAblaufDienstausweis_label = new JLabel("Vorwarnung Ablauf Dienstausweis: ");
      this.ablaufDienstausweisViaEMail_label = new JLabel("Mitglied über den Ablauf Dienstausweises per E-Mail informieren ** : ");
      this.ablaufDienstausweisAnzeigen_label = new JLabel("Ablauf des Dienstausweises Anzeigen: ");
      this.prüfungDerFahrerlaubnis_label = new JLabel("Feld Ablauf der Fahrberechtigung: ");
      this.darstellungLehrgängeMitgliederverwaltung_label = new JLabel("Darstellung der Lehrgänge, Führerscheine, etc. in der Mitgliederverwaltung: ");
      this.ablaufFahrberechtigungAnzeigen_label = new JLabel("Ablauf der Fahrberechtigung Anzeigen: ");
      this.vorwarnungAblaufFahrberechtigung_label = new JLabel("Vorwarnung Ablauf Fahrberechtigung: ");
      this.ablaufFahrberechtigungViaEMail_label = new JLabel("Mitglied über den Ablauf der Fahrberechtigung per E-Mail informieren ** : ");
      this.druckAnwesenheitsListeMode1_label = new JLabel("Druckmodus der Kopfzeile \"Freitext\": ");
      this.druckAnwesenheitsListeMode2_label = new JLabel("Druckmodus der Kofzeile \"Veranstaltungsauswahl:\"");
      this.dienstgradAufAnwesenheitsliste_label = new JLabel("Dienstgrad in Listen Anzeigen: ");
      this.modulMitgliederVerfügbarkeit_label = new JLabel("Modul Mitglieder Verfügbarkeit aktivieren *** : ");
      this.schfiftgrößeAnwesenheitsliste_label = new JLabel("Schriftgröße der Anwesenheitsliste (Standard ist 26): ");
      this.bestaetignungFreistellungEinsatzArt_label = new JLabel("Form der \"Bestätigng der Teilnahme am Einsatz\": ");
      this.bestaetignungFreistellungEinsatzAktiv_label = new JLabel("Bestätigng der Teilnahme am Einsatz Aktivieren: ");
      this.bestaetignungFreistellungEinsatz_label = new JLabel("Bestätigung der Teilnahme am Einsatz: ");
      this.modulEinsatzgebiet_label = new JLabel("Modul Einsatzgebiet *** : ");
      this.globaleEMailG25Aktiviert_label = new JLabel("Gobale E-Mail Adresse G25-Untersuchung aktivieren (Deaktive - Private): ");
      this.globaleEMailG25_label = new JLabel("Globale E-Mail Adresse der G25: ");
      this.globaleEMailG26Aktiviert_label = new JLabel("Gobale E-Mail Adresse G26-,G30-Untersuchung aktivieren (Deaktive - Private): ");
      this.globaleEMailG26_label = new JLabel("Globale E-Mail Adresse der G26, G30: ");
      this.globaleEMailFahrberechtigungAktiviert_label = new JLabel("Gobale E-Mail Adresse Fahrberechtigung aktivieren (Deaktive - Private): ");
      this.globaleEMailFahrberechtigung_label = new JLabel("Globale E-Mail Adresse der Fahrberechtigung: ");
      this.globaleEMailDienstausweisAktiviert_label = new JLabel("Gobale E-Mail Adresse Dienstausweis aktivieren (Deaktive - Private): ");
      this.globaleEMailDienstausweis_label = new JLabel("Globale E-Mail Adresse Dienstausweis: ");
      this.EinsatzBerichtFahrzeugbelegungHinzufügen_label = new JLabel("Fahrzeugbelegung zum Einsatzbericht hinzufügen: ");
      this.EinsatzBerichtAtemschutzpassHinzufügen_label = new JLabel("Atemschutzgeräteträger zum Einsatzbericht hinzufügen: ");
      this.EinsatzBerichtEinsatzleiterMitDienstgrad_label = new JLabel("Einsatzleiter mit Dienstgard im Einsatzbericht: ");
      this.feldEintreffenAusblenden_label = new JLabel("Einsatz anlegen - Eintreffen ausblenden: ");
      this.feldStadtteilAusblenden_label = new JLabel("Einsatz anlegen - Stadtteilfeld ausblenden: ");
      this.langesDatumsformatUntersuchungsliste_label = new JLabel("Langes Datumsformat in der Untersuchungsliste anzeigen: ");
      this.LookAndFeel_label = new JLabel("Programmansicht (LookAndFeel)***: ");
      this.anmeldungSpeichernErlauben_label = new JLabel("Option \"Angemeldet bleiben\" freigeben: ");
      this.terminVersandtViaEMailFolgeMonat_label = new JLabel("  -- Termine auch für den Folgemonat versenden: ");
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungen_label = new JLabel("Anzahl der Veranstaltungen (Standard = 6): ");
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe_label = new JLabel("Anzahl der Veranstaltungen in der Liste (Standard = 10): ");
      this.TerminDisplay_AnzeigeDauerVeranstaltungen_label = new JLabel("Anzeigedauer der Veranstaltungen in Sekunden: ");
      this.TerminDisplay_AnzeigeDauerUhr_label = new JLabel("Anzeigedauer der Uhr in Sekunden: ");
      this.TerminDisplay_AnzeigenLetzenEinsatz_label = new JLabel("Anzeigen des Letzten Einsatzes: ");
      this.TerminDisplay_HintergrundBildAktivieren_label = new JLabel("Soll ein Hintergrundbild angezeigt werden: ");
      this.TerminDisplay_LetzterEinsatzOrtAnzeigen_label = new JLabel("  -- Anzeigen des EinsatzOrtes: ");
      this.TerminDisplay_HintergrundBild_label = new JLabel("  -- Hintergrundbild: ");
      this.schulungAdminModul_label = new JLabel("Schulungsadministration (nur BF!) *** : ");
      this.schulungClientModule_label = new JLabel("Schulungsmodul *** : ");
      this.facebookAccessToken_label = new JLabel("Facebook Access Token: ");
      this.facebookAutoPostEinsatz_label = new JLabel("Einsatz Automatisch auf Facebook posten *** : ");
      this.facebookAppID_label = new JLabel("Facebook App-ID: ");
      this.facebookAppGeheimCode_label = new JLabel("Facebook Geheim-Code: ");
      this.facebookEMail_label = new JLabel("E-Mail senden bei neuem Post für Facebook: ");
      this.facebookEMailAn1_label = new JLabel("  -- E-Mail an senden (1): ");
      this.facebookEMailAn2_label = new JLabel("  -- E-Mail an senden (2): ");
      this.facebookEMailAn3_label = new JLabel("  -- E-Mail an senden (3): ");
      this.mitgliederGesundheitTab_label = new JLabel("Mitglieder Gesundheit einblenden (Krankenkasse, Krankheiten, Medikamente, usw.): ");
      this.automatischeFrageNachAtemschutzEinsatz_label = new JLabel("Automatische Abfrage Atemschutzeinsatz? bei Einsätzen länger als 45min.: ");
      this.google_api_code_label = new JLabel("Google API Key: ");
      this.default_location_label = new JLabel("GPS Standort Gerätehaus (Eingabe: GPS Nord, GPS Ost): ");
      this.externeDatenbankFürKartendaten_label = new JLabel("Externe Datenbank für Karten aktivieren: ");
      this.externeKartenDB_Typ_label = new JLabel("DatenbankTyp: ");
      this.externeKartenDatenbankPort_label = new JLabel("Datenbank Port: ");
      this.externeKartenDatenbankIP_label = new JLabel("Datenbank IP: ");
      this.externeKartenDatenbankName_label = new JLabel("Datenbank Name: ");
      this.externeKartenDatenbankUser_label = new JLabel("Datenbank Benutzer: ");
      this.externeKartenDatenbankPasswort_label = new JLabel("Datenbank Passwort: ");
      this.externeKartenSSHUser_label = new JLabel("SSH Benutzer: ");
      this.externeKartenSSHPasswort_label = new JLabel("SSH Passwort: ");
      this.externeKartenSSHServer_label = new JLabel("SSH Server: ");
      this.externeKartenSSHServerPort_label = new JLabel("SSH Server Port: ");
      this.externeKartenSSHTunnel_label = new JLabel("SSH Tunnel Port: ");
      this.externeKartenDatenbankLokalesBackup_label = new JLabel("Soll von den externen Karten Daten eine Lokales Backup als Rückfallebene angelegt werden: ");
      this.einsatzbericht = new JTextField(30);
      this.mängelmeldung = new JTextField(30);
      this.verdienstausfall = new JTextField(30);
      this.briefkopf = new JTextField(30);
      this.tel = new JTextField(25);
      this.straße = new JTextField(25);
      this.plz = new JTextField(25);
      this.name = new JTextField(25);
      this.stadt = new JTextField(25);
      this.fehlalarm = new JTextField(25);
      this.vorwarnunUntersuchung = new JTextField(25);
      this.vorwarnungFahrzeugUntersuchung = new JTextField(25);
      this.vorwarnungGeräteprüfung = new JTextField(25);
      this.gebAnzeigen = new JCheckBox();
      this.terminAnzeigen = new JCheckBox();
      this.agttrainingAnzeigen = new JCheckBox();
      this.autoBerichtAktiv = new JCheckBox();
      this.emailModulAktivieren = new JCheckBox();
      this.useSSL = new JCheckBox();
      this.zeitAutoBericht = new JTextField(25);
      this.lehrgangsmeldungen = new JTextField(25);
      this.smtpServer = new JTextField(25);
      this.popServer = new JTextField(25);
      this.smtpServerPort = new JTextField(25);
      this.popServerPort = new JTextField(25);
      this.emailAdresse = new JTextField(25);
      this.emailName = new JTextField(25);
      this.emailPasswort = new JPasswordField(25);
      this.vorbelegungDienstabendStart = new JTextField(25);
      this.vorbelegungDienstabendEnde = new JTextField(25);
      this.vorbelegungBSWTreffen = new JTextField(25);
      this.vorbelegungBSWVeranstaltungStart = new JTextField(25);
      this.vorbelegungBSWEnde = new JTextField(25);
      this.schnittstellenAktivierung = new JCheckBox();
      this.autoDBsave = new JCheckBox();
      this.autoDBsaveTage = new JTextField(25);
      this.untersuchungViaEMail = new JCheckBox();
      this.untersuchungViaEMailChefBCC = new JCheckBox();
      this.terminVersandtViaEMail = new JCheckBox();
      this.ablaufLKWFührerscheinViaEMail = new JCheckBox();
      this.ablaufLKWFuehrerscheinAnzeigen = new JCheckBox();
      this.automatischesProgrammUpdate = new JCheckBox();
      this.einsatzleiterBFAnzeigen = new JCheckBox();
      this.bswHitliste = new JCheckBox();
      this.abrechnungModuleAnzeigen = new JCheckBox();
      this.geraetepruefungenViaEMail = new JCheckBox();
      this.datenbankTyp = new JTextField(25);
      this.datenbankIP = new JTextField(25);
      this.datenbankName = new JTextField(25);
      this.datenbankPasswort = new JPasswordField(25);
      this.datenbankPasswort2 = new JPasswordField(25);
      this.datenbankUser = new JTextField(25);
      this.organisation = new JTextField(25);
      this.sshServer = new JTextField(25);
      this.ftpServer = new JTextField(25);
      this.offeneMängelAnzeigen = new JCheckBox();
      this.fahrzeugUntersuchungViaEMail = new JCheckBox();
      this.mängelmeldungViaEMailVersenden = new JCheckBox();
      this.geburtstageNurGanzerMonat = new JRadioButton();
      this.geburtstageNurHeute = new JRadioButton();
      this.BgGeburtstage = new ButtonGroup();
      this.BgGeburtstage.add(this.geburtstageNurGanzerMonat);
      this.BgGeburtstage.add(this.geburtstageNurHeute);
      this.schichtModul = new JCheckBox();
      this.urlaubModul = new JCheckBox();
      this.fahrtenbuchModul = new JCheckBox();
      this.joomlaLink = new JTextField(20);
      this.joomlaVeranstaltung = new JCheckBox();
      this.joomlaAusbildungsplan = new JCheckBox();
      this.joomlaEinsatzkomponente = new JCheckBox();
      this.joomlaEinsatzkomponente_Visible = new JCheckBox();
      this.joomlaEinsatzkomponenteEMail = new JCheckBox();
      this.joomlaEinsatzkomponenteSecretKey = new JTextField();
      this.alwaysOnTop = new JCheckBox();
      this.globaleEMailEinheitsführung = new JTextField(25);
      this.globaleEMailGerätewarte = new JTextField(25);
      this.globaleEMailGerätewarteAktivieren = new JCheckBox();
      this.globaleEMailEinheitsführungAktivieren = new JCheckBox();
      this.statistik2 = new JCheckBox();
      this.einsatznummerPflichteintrag = new JCheckBox();
      this.einsatzLeiterBFPflichteintrag = new JCheckBox();
      this.schutzziel1 = new JTextField(20);
      this.schutzziel2 = new JTextField(20);
      this.headerPrint = new JCheckBox();
      this.footerPrint = new JCheckBox();
      this.zeilenhöheAnsicht = new JTextField(20);
      this.zeilenhöheDruck = new JTextField(20);
      this.modulAusbildungsplan = new JCheckBox();
      this.modulFahrzeugeinteilung = new JCheckBox();
      this.modulVeranstaltung = new JCheckBox();
      this.getakteteInternetverbindung = new JCheckBox();
      this.onlineStatus = new JCheckBox();
      this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen = new JCheckBox();
      this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln = new JCheckBox();
      this.organisationen = new JCheckBox();
      this.fullBackupInZip = new JCheckBox();
      this.fullBackupPath = new JTextField(20);
      this.hochzeitFeldFuerMitglieder = new JCheckBox();
      this.mitgliedSeitPflichtEintrag = new JCheckBox();
      this.LehrgangEintragenAusMitgliederVerwaltungMode = new JCheckBox();
      this.unwetterwarnungEMail = new JTextField(25);
      this.unwetterwarnungModulAktiv = new JCheckBox();
      this.unwetterwarnungPasswort = new JPasswordField();
      this.unwetterwarnungPop3 = new JTextField(25);
      this.unwetterwarnungPopPort = new JTextField(25);
      this.unwetterwarnungSSL = new JCheckBox();
      this.unwetterwarnungWebKonfig = new JButton("Hier DWD-EMail Benachrichtigun konfigurieren...");
      this.ablaufDienstausweisAnzeigen = new JCheckBox();
      this.ablaufDienstausweisViaEMail = new JCheckBox();
      this.vorwarnungAblaufDienstausweis = new JTextField();
      this.prüfungDerFahrerlaubnis = new JCheckBox();
      this.ablaufLKWFuehrerscheinAnzeigen = new JCheckBox();
      this.vorwarnungFahrzeugUntersuchung = new JTextField(25);
      this.vorwarnungAblaufFahrberechtigung = new JTextField(25);
      this.ablaufFahrberechtigungAnzeigen = new JCheckBox();
      this.ablaufFahrberechtigungViaEMail = new JCheckBox();
      this.druckAnwesenheitsListeMode1 = new JRadioButton();
      this.druckAnwesenheitsListeMode2 = new JRadioButton();
      this.BgDruckAnwesenheitsListeMode = new ButtonGroup();
      this.BgDruckAnwesenheitsListeMode.add(this.druckAnwesenheitsListeMode1);
      this.BgDruckAnwesenheitsListeMode.add(this.druckAnwesenheitsListeMode2);
      this.dienstgradAufAnwesenheitsliste = new JCheckBox();
      this.modulMitgliederVerfügbarkeit = new JCheckBox();
      this.bestaetignungFreistellungEinsatzAktiv = new JCheckBox();
      this.bestaetignungFreistellungEinsatz = new JTextField(25);
      this.modulEinsatzgebiet = new JCheckBox();
      this.globaleEMailG25Aktiviert = new JCheckBox();
      this.globaleEMailG25 = new JTextField(25);
      this.globaleEMailG26Aktiviert = new JCheckBox();
      this.globaleEMailG26 = new JTextField(25);
      this.globaleEMailFahrberechtigungAktiviert = new JCheckBox();
      this.globaleEMailFahrberechtigung = new JTextField(25);
      this.globaleEMailDienstausweisAktiviert = new JCheckBox();
      this.globaleEMailDienstausweis = new JTextField(25);
      this.EinsatzBerichtAtemschutzpassHinzufügen = new JCheckBox();
      this.EinsatzBerichtFahrzeugbelegungHinzufügen = new JCheckBox();
      this.EinsatzBerichtEinsatzleiterMitDienstgrad = new JCheckBox();
      this.feldEintreffenAusblenden = new JCheckBox();
      this.feldStadtteilAusblenden = new JCheckBox();
      this.langesDatumsformatUntersuchungsliste = new JCheckBox();
      this.anmeldungSpeichernErlauben = new JCheckBox();
      this.terminVersandtViaEMailFolgeMonat = new JCheckBox();
      this.TerminDisplay_AnzeigeDauerVeranstaltungen = new JTextField();
      this.TerminDisplay_AnzeigeDauerUhr = new JTextField();
      this.TerminDisplay_AnzeigenLetzenEinsatz = new JCheckBox();
      this.TerminDisplay_HintergrundBild = new JTextField();
      this.TerminDisplay_HintergrundBildAktivieren = new JCheckBox();
      this.TerminDisplay_LetzterEinsatzOrtAnzeigen = new JCheckBox();
      this.schulungAdminModul = new JCheckBox();
      this.schulungClientModul = new JCheckBox();
      this.facebookAccessToken = new JTextField(25);
      this.facebookAutoPostEinsatz = new JCheckBox();
      this.facebookAppID = new JTextField(25);
      this.facebookAppGeheimCode = new JTextField(25);
      this.facebookEMail = new JCheckBox();
      this.mitgliederGesundheitTab = new JCheckBox();
      this.automatischeFrageNachAtemschutzEinsatz = new JCheckBox();
      this.google_api_code = new JTextField();
      this.default_location = new JTextField();
      this.externeDatenbankFürKartendaten = new JCheckBox();
      this.externeKartenDatenbankPort = new JTextField();
      this.externeKartenDatenbankIP = new JTextField();
      this.externeKartenDatenbankName = new JTextField();
      this.externeKartenDatenbankUser = new JTextField();
      this.externeKartenDatenbankPasswort = new JPasswordField();
      this.externeKartenSSHUser = new JTextField();
      this.externeKartenSSHPasswort = new JPasswordField();
      this.externeKartenSSHServer = new JTextField();
      this.externeKartenSSHServerPort = new JTextField();
      this.externeKartenSSHTunnel = new JTextField();
      this.externeKartenDatenbankLokalesBackup = new JCheckBox();
      String[] anzahl1 = new String[]{"1", "2", "3", "4", "5", "6", "7"};
      String[] anzahl2 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
      String[] dbTypen = new String[]{"Lokal", "SSH"};
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungen = new JComboBox(anzahl1);
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe = new JComboBox(anzahl2);
      String[] einsatzBerichte = new String[]{"<bitte wählen>", "PDF (intern)", "Word Schnittstelle", "Eigene Vorlage / Dateienvorlage"};
      this.einsatzberichtArt = new JComboBox(einsatzBerichte);
      this.verdienstausfallArt = new JComboBox(einsatzBerichte);
      this.maengelmeldungArt = new JComboBox(einsatzBerichte);
      this.bestaetignungFreistellungEinsatzArt = new JComboBox(einsatzBerichte);
      this.verdienstausfallArt.removeItem("Eigene Vorlage / Dateienvorlage");
      this.maengelmeldungArt.removeItem("Eigene Vorlage / Dateienvorlage");
      this.bestaetignungFreistellungEinsatzArt.removeItem("Eigene Vorlage / Dateienvorlage");
      this.externeKartenDB_Typ = new JComboBox(dbTypen);
      String[] bundeslandListe = new String[]{"<bitte wählen>", "Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen"};
      this.bundesland = new JComboBox(bundeslandListe);
      String[] mitgliedSeitFormatListe = new String[]{"yyyy", "dd.MM.yyyy"};
      this.mitgliedSeitFormat = new JComboBox(mitgliedSeitFormatListe);
      String[] seperatorOptions = new String[]{",", ";", ":", ".", "\\t", "\\n"};
      this.vCardSeperator = new JComboBox(seperatorOptions);
      String[] monate = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "Alle", "aktuelles Jahr"};
      this.sichtbarkeitVonVeranstaltungenVergangenheit = new JComboBox(monate);
      this.sichtbarkeitVonVeranstaltungenZukunft = new JComboBox(monate);
      String[] verdienstausfallOptionen = new String[]{"Einsatznummer + Stichwort + Straße / Ort", "Einsatznummer + Stichwort", "Einsatznummer", "Einsatznummer + Einsatzkategorie", "Stichwort", "Freitext"};
      this.verdienstausfallEinsatzbeschreibungOption = new JComboBox(verdienstausfallOptionen);

      String[] veransdConfig;
      try {
         veransdConfig = Utils.listToArrayOnlyFORComboBoxes((new TabelleMitglied()).getMitgliederGruppe1());
         this.joomlaEinsatzkomponenteEMailAn1 = new JComboBox(veransdConfig);
         this.joomlaEinsatzkomponenteEMailAn2 = new JComboBox(veransdConfig);
         this.joomlaEinsatzkomponenteEMailAn3 = new JComboBox(veransdConfig);
         this.facebookEMailAn1 = new JComboBox(veransdConfig);
         this.facebookEMailAn2 = new JComboBox(veransdConfig);
         this.facebookEMailAn3 = new JComboBox(veransdConfig);
      } catch (SQLException var16) {
         logging.logPrintStackTrace(var16);
      }

      veransdConfig = new String[]{"Immer senden", "Nur einmal im Monat"};
      this.terminVersandtViaEMailConfig = new JComboBox(veransdConfig);
      String[] joomlaEinsatzkomponenteConfigOptionen = new String[]{"Hausnummern anzeigen", "Hausnummern ausblenden", "Hausnummern ausblenden + Stadtteil anzeigen", "Hausnummern anzeigen + Stadtteil anzeigen"};
      this.joomlaEinsatzkomponente_Config = new JComboBox(joomlaEinsatzkomponenteConfigOptionen);
      String[] joomlaEinsatzkomponenteConfigStichwort = new String[]{"Stichwort anzeigen", "Einsatzkategorie anzeigen", "Stichwort + Einsatzkategorie anzeigen"};
      this.joomlaEinsatzkomponenteStichwort = new JComboBox(joomlaEinsatzkomponenteConfigStichwort);
      String[] darstellungLehrgängeMitgliederverwaltungOptionen = new String[]{"CheckBox", "Liste"};
      this.darstellungLehrgängeMitgliederverwaltung = new JComboBox(darstellungLehrgängeMitgliederverwaltungOptionen);
      String[] schfiftgröße = new String[]{"10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32"};
      this.schfiftgrößeAnwesenheitsliste = new JComboBox(schfiftgröße);
      String[] lookAndFeelListe = new String[]{"ACRYL", "AERO.SILVER", "ALUMINIUM.SILVER", "DARKBLACK", "JAVA-CLASSIC", "JAVA-MODERN", "LUNA.BLUE", "SMART"};
      this.LookAndFeel = new JComboBox(lookAndFeelListe);
      this.chooserXML = new JFileChooser();
      this.chooserJPEG = new JFileChooser();
      this.filterJPEG = new FileNameExtensionFilter("JPG", new String[]{"jpg"});
      this.filterXML = new FileNameExtensionFilter("XML", new String[]{"xml"});
      this.tabPane = new JTabbedPane();
      this.modulBeschreibung = new JLabel("Einstellungen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      this.einsatzbericht.setText((String)runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
      this.name.setText((String)runApplication.EINSTELLUNGEN.get("Name"));
      this.stadt.setText((String)runApplication.EINSTELLUNGEN.get("Stadt"));
      this.verdienstausfall.setText((String)runApplication.EINSTELLUNGEN.get("verdienstausfall"));
      this.fehlalarm.setText((String)runApplication.EINSTELLUNGEN.get("fehlalarm"));
      this.tel.setText((String)runApplication.EINSTELLUNGEN.get("telefon"));
      this.plz.setText((String)runApplication.EINSTELLUNGEN.get("plz"));
      this.straße.setText((String)runApplication.EINSTELLUNGEN.get("strasse"));
      this.briefkopf.setText((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      this.vorwarnunUntersuchung.setText((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      this.vorwarnungFahrzeugUntersuchung.setText((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));
      this.zeitAutoBericht.setText((String)runApplication.EINSTELLUNGEN.get("ZeitAutoBericht"));
      this.mängelmeldung.setText((String)runApplication.EINSTELLUNGEN.get("mängelmeldung"));
      this.vorwarnungGeräteprüfung.setText((String)runApplication.EINSTELLUNGEN.get("vorwarnungGeräte"));
      this.lehrgangsmeldungen.setText((String)runApplication.EINSTELLUNGEN.get("WieVieleLehrgangsmeldungenProJahr"));
      this.smtpServerPort.setText((String)runApplication.EINSTELLUNGEN.get("smtpPort"));
      this.smtpServer.setText((String)runApplication.EINSTELLUNGEN.get("smtpServer"));
      this.popServer.setText((String)runApplication.EINSTELLUNGEN.get("pop3Server"));
      this.popServerPort.setText((String)runApplication.EINSTELLUNGEN.get("pop3Port"));
      this.emailAdresse.setText((String)runApplication.EINSTELLUNGEN.get("emailAdresse"));
      this.emailName.setText((String)runApplication.EINSTELLUNGEN.get("eMailName"));
      this.vorbelegungDienstabendStart.setText((String)runApplication.EINSTELLUNGEN.get("vorbelegungDienstStart"));
      this.vorbelegungDienstabendEnde.setText((String)runApplication.EINSTELLUNGEN.get("vorbelegungDienstEnde"));
      this.autoDBsaveTage.setText((String)runApplication.EINSTELLUNGEN.get("autoDBsaveTage"));
      this.vCardSeperator.setSelectedItem(runApplication.EINSTELLUNGEN.get("vCardSeperator"));
      this.bundesland.setSelectedItem(runApplication.EINSTELLUNGEN.get("bundesland"));
      this.vorbelegungBSWTreffen.setText((String)runApplication.EINSTELLUNGEN.get("vorbelegungBSWTreffen"));
      this.vorbelegungBSWVeranstaltungStart.setText((String)runApplication.EINSTELLUNGEN.get("vorbelegungBSWVeranstaltungStart"));
      this.vorbelegungBSWEnde.setText((String)runApplication.EINSTELLUNGEN.get("vorbelegungBSWEnde"));
      this.einsatzberichtArt.setSelectedItem(runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt"));
      this.verdienstausfallArt.setSelectedItem(runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt"));
      this.maengelmeldungArt.setSelectedItem(runApplication.EINSTELLUNGEN.get("MängelBerichtArt"));
      this.einsatzberichtArt.setSelectedItem(runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt"));
      this.sichtbarkeitVonVeranstaltungenVergangenheit.setSelectedItem(runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit"));
      this.sichtbarkeitVonVeranstaltungenZukunft.setSelectedItem(runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft"));
      this.joomlaLink.setText((String)runApplication.EINSTELLUNGEN.get("JoomlaLink"));
      this.joomlaEinsatzkomponenteSecretKey.setText((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteSecretKey"));
      this.globaleEMailEinheitsführung.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsführung"));
      this.globaleEMailGerätewarte.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailGerätewarte"));
      this.schutzziel1.setText((String)runApplication.EINSTELLUNGEN.get("schutzziel1"));
      this.schutzziel2.setText((String)runApplication.EINSTELLUNGEN.get("schutzziel2"));
      this.zeilenhöheAnsicht.setText((String)runApplication.EINSTELLUNGEN.get("zeilenhöheAnsicht"));
      this.zeilenhöheDruck.setText((String)runApplication.EINSTELLUNGEN.get("zeilenhöheDruck"));
      this.fullBackupPath.setText((String)runApplication.EINSTELLUNGEN.get("FullBackupPath"));
      this.mitgliedSeitFormat.setSelectedItem(runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat"));
      this.unwetterwarnungEMail.setText((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungEMail"));
      this.unwetterwarnungPop3.setText((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPop3"));
      this.unwetterwarnungPopPort.setText((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPopPort"));
      this.vorwarnungAblaufDienstausweis.setText((String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufDienstausweis"));
      this.darstellungLehrgängeMitgliederverwaltung.setSelectedItem(runApplication.EINSTELLUNGEN.get("darstellungLehrgängeMitgliederverwaltung"));
      this.vorwarnungAblaufFahrberechtigung.setText((String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufFahrberechtigung"));
      this.schfiftgrößeAnwesenheitsliste.setSelectedItem(runApplication.EINSTELLUNGEN.get("schfiftgrößeAnwesenheitsliste"));
      this.bestaetignungFreistellungEinsatzArt.setSelectedItem(runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzArt"));
      this.bestaetignungFreistellungEinsatz.setText((String)runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatz"));
      this.globaleEMailG25.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailG25"));
      this.globaleEMailG26.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26"));
      this.globaleEMailFahrberechtigung.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigung"));
      this.globaleEMailDienstausweis.setText((String)runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweis"));
      this.LookAndFeel.setSelectedItem(runApplication.EINSTELLUNGEN.get("LookAndFeel"));
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungen.setSelectedItem(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeAnazahlVeranstaltungen"));
      this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe.setSelectedItem(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeAnazahlVeranstaltungListe"));
      this.TerminDisplay_AnzeigeDauerVeranstaltungen.setText((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeDauerVeranstaltungen"));
      this.TerminDisplay_AnzeigeDauerUhr.setText((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeDauerUhr"));
      this.TerminDisplay_HintergrundBild.setText((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_HintergrundBild"));
      this.facebookAccessToken.setText((String)runApplication.EINSTELLUNGEN.get("facebookAccessToken"));
      this.facebookAppID.setText((String)runApplication.EINSTELLUNGEN.get("facebookAppID"));
      this.facebookAppGeheimCode.setText((String)runApplication.EINSTELLUNGEN.get("facebookAppGeheimCode"));
      this.google_api_code.setText((String)runApplication.EINSTELLUNGEN.get("google_api_code"));
      this.default_location.setText((String)runApplication.EINSTELLUNGEN.get("default_location"));
      this.externeKartenDB_Typ.setSelectedItem(runApplication.EINSTELLUNGEN.get("externeKartenDB_Typ"));
      this.externeKartenDatenbankPort.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankPort"));
      this.externeKartenDatenbankIP.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankIP"));
      this.externeKartenDatenbankName.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankName"));
      this.externeKartenDatenbankUser.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankUser")));
      this.externeKartenDatenbankPasswort.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankPasswort")));
      this.externeKartenSSHUser.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHUser")));
      this.externeKartenSSHPasswort.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHPasswort")));
      this.externeKartenSSHServer.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHServer"));
      this.externeKartenSSHServerPort.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHServerPort"));
      this.externeKartenSSHTunnel.setText((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHTunnel"));
      if(((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankLokalesBackup")).equals("1")) {
         this.externeKartenDatenbankLokalesBackup.setSelected(true);
      } else {
         this.externeKartenDatenbankLokalesBackup.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("externeDatenbankFürKartendaten")).equals("1")) {
         this.externeDatenbankFürKartendaten.setSelected(true);
      } else {
         this.externeDatenbankFürKartendaten.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("automatischeFrageNachAtemschutzEinsatz")).equals("1")) {
         this.automatischeFrageNachAtemschutzEinsatz.setSelected(true);
      } else {
         this.automatischeFrageNachAtemschutzEinsatz.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("mitgliederGesundheitTab")).equals("1")) {
         this.mitgliederGesundheitTab.setSelected(true);
      } else {
         this.mitgliederGesundheitTab.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("facebookEMail")).equals("1")) {
         this.facebookEMail.setSelected(true);
      } else {
         this.facebookEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("facebookAutoPostEinsatz")).equals("1")) {
         this.facebookAutoPostEinsatz.setSelected(true);
      } else {
         this.facebookAutoPostEinsatz.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("schulungAdminModul")).equals("1")) {
         this.schulungAdminModul.setSelected(true);
      } else {
         this.schulungAdminModul.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("schulungClientModul")).equals("1")) {
         this.schulungClientModul.setSelected(true);
      } else {
         this.schulungClientModul.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_HintergrundBildAktivieren")).equals("1")) {
         this.TerminDisplay_HintergrundBildAktivieren.setSelected(true);
      } else {
         this.TerminDisplay_HintergrundBildAktivieren.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_LetzterEinsatzOrtAnzeigen")).equals("1")) {
         this.TerminDisplay_LetzterEinsatzOrtAnzeigen.setSelected(true);
      } else {
         this.TerminDisplay_LetzterEinsatzOrtAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigenLetzenEinsatz")).equals("1")) {
         this.TerminDisplay_AnzeigenLetzenEinsatz.setSelected(true);
      } else {
         this.TerminDisplay_AnzeigenLetzenEinsatz.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailFolgeMonat")).equals("1")) {
         this.terminVersandtViaEMailFolgeMonat.setSelected(true);
      } else {
         this.terminVersandtViaEMailFolgeMonat.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("anmeldungSpeichernErlauben")).equals("1")) {
         this.anmeldungSpeichernErlauben.setSelected(true);
      } else {
         this.anmeldungSpeichernErlauben.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
         this.langesDatumsformatUntersuchungsliste.setSelected(true);
      } else {
         this.langesDatumsformatUntersuchungsliste.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("feldEintreffenAusblenden")).equals("1")) {
         this.feldEintreffenAusblenden.setSelected(true);
      } else {
         this.feldEintreffenAusblenden.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("feldStadtteilAusblenden")).equals("1")) {
         this.feldStadtteilAusblenden.setSelected(true);
      } else {
         this.feldStadtteilAusblenden.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("EinsatzBerichtEinsatzleiterMitDienstgrad")).equals("1")) {
         this.EinsatzBerichtEinsatzleiterMitDienstgrad.setSelected(true);
      } else {
         this.EinsatzBerichtEinsatzleiterMitDienstgrad.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("EinsatzBerichtFahrzeugbelegungHinzufügen")).equals("1")) {
         this.EinsatzBerichtFahrzeugbelegungHinzufügen.setSelected(true);
      } else {
         this.EinsatzBerichtFahrzeugbelegungHinzufügen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("EinsatzBerichtAtemschutzpassHinzufügen")).equals("1")) {
         this.EinsatzBerichtAtemschutzpassHinzufügen.setSelected(true);
      } else {
         this.EinsatzBerichtAtemschutzpassHinzufügen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailG25Aktiviert")).equals("1")) {
         this.globaleEMailG25Aktiviert.setSelected(true);
      } else {
         this.globaleEMailG25Aktiviert.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert")).equals("1")) {
         this.globaleEMailG26Aktiviert.setSelected(true);
      } else {
         this.globaleEMailG26Aktiviert.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigungAktiviert")).equals("1")) {
         this.globaleEMailFahrberechtigungAktiviert.setSelected(true);
      } else {
         this.globaleEMailFahrberechtigungAktiviert.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweisAktiviert")).equals("1")) {
         this.globaleEMailDienstausweisAktiviert.setSelected(true);
      } else {
         this.globaleEMailDienstausweisAktiviert.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("modulEinsatzgebiet")).equals("1")) {
         this.modulEinsatzgebiet.setSelected(true);
      } else {
         this.modulEinsatzgebiet.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzAktiv")).equals("1")) {
         this.bestaetignungFreistellungEinsatzAktiv.setSelected(true);
      } else {
         this.bestaetignungFreistellungEinsatzAktiv.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("modulMitgliederVerfügbarkeit")).equals("1")) {
         this.modulMitgliederVerfügbarkeit.setSelected(true);
      } else {
         this.modulMitgliederVerfügbarkeit.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste")).equals("1")) {
         this.dienstgradAufAnwesenheitsliste.setSelected(true);
      } else {
         this.dienstgradAufAnwesenheitsliste.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungViaEMail")).equals("1")) {
         this.ablaufFahrberechtigungViaEMail.setSelected(true);
      } else {
         this.ablaufFahrberechtigungViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen")).equals("1")) {
         this.ablaufFahrberechtigungAnzeigen.setSelected(true);
      } else {
         this.ablaufFahrberechtigungAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisAnzeigen")).equals("1")) {
         this.ablaufDienstausweisAnzeigen.setSelected(true);
      } else {
         this.ablaufDienstausweisAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisViaEMail")).equals("1")) {
         this.ablaufDienstausweisViaEMail.setSelected(true);
      } else {
         this.ablaufDienstausweisViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("prüfungDerFahrerlaubnis")).equals("1")) {
         this.prüfungDerFahrerlaubnis.setSelected(true);
      } else {
         this.prüfungDerFahrerlaubnis.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungModulAktiv")).equals("1")) {
         this.unwetterwarnungModulAktiv.setSelected(true);
      } else {
         this.unwetterwarnungModulAktiv.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungSSL")).equals("1")) {
         this.unwetterwarnungSSL.setSelected(true);
      } else {
         this.unwetterwarnungSSL.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("autoBerichtAktiv")).equals("1")) {
         this.autoBerichtAktiv.setSelected(true);
      } else {
         this.autoBerichtAktiv.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("termineAnzeigen")).equals("1")) {
         this.terminAnzeigen.setSelected(true);
      } else {
         this.terminAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigen")).equals("1")) {
         this.gebAnzeigen.setSelected(true);
      } else {
         this.gebAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("agtTrainingAnzeigen")).equals("1")) {
         this.agttrainingAnzeigen.setSelected(true);
      } else {
         this.agttrainingAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("emailModul")).equals("1")) {
         this.emailModulAktivieren.setSelected(true);
      } else {
         this.emailModulAktivieren.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("useSSL")).equals("1")) {
         this.useSSL.setSelected(true);
      } else {
         this.useSSL.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("einsatzSchnittstelle")).equals("1")) {
         this.schnittstellenAktivierung.setSelected(true);
      } else {
         this.schnittstellenAktivierung.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("autoDBsave")).equals("1")) {
         this.autoDBsave.setSelected(true);
      } else {
         this.autoDBsave.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")).equals("1")) {
         this.untersuchungViaEMail.setSelected(true);
      } else {
         this.untersuchungViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMailChefBCC")).equals("1")) {
         this.untersuchungViaEMailChefBCC.setSelected(true);
      } else {
         this.untersuchungViaEMailChefBCC.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMail")).equals("1")) {
         this.terminVersandtViaEMail.setSelected(true);
      } else {
         this.terminVersandtViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufLKWFührerscheinViaEMail")).equals("1")) {
         this.ablaufLKWFührerscheinViaEMail.setSelected(true);
      } else {
         this.ablaufLKWFührerscheinViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("ablaufLKWAnzeigen")).equals("1")) {
         this.ablaufLKWFuehrerscheinAnzeigen.setSelected(true);
      } else {
         this.ablaufLKWFuehrerscheinAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("automatischesUpdate")).equals("1")) {
         this.automatischesProgrammUpdate.setSelected(true);
      } else {
         this.automatischesProgrammUpdate.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("einsatzleiterBF")).equals("1")) {
         this.einsatzleiterBFAnzeigen.setSelected(true);
      } else {
         this.einsatzleiterBFAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("bswHitliste")).equals("1")) {
         this.bswHitliste.setSelected(true);
      } else {
         this.bswHitliste.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("abrechnungModul")).equals("1")) {
         this.abrechnungModuleAnzeigen.setSelected(true);
      } else {
         this.abrechnungModuleAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("geraetepruefungViaEMail")).equals("1")) {
         this.geraetepruefungenViaEMail.setSelected(true);
      } else {
         this.geraetepruefungenViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("offeneMaengelAnzeigen")).equals("1")) {
         this.offeneMängelAnzeigen.setSelected(true);
      } else {
         this.offeneMängelAnzeigen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")).equals("1")) {
         this.fahrzeugUntersuchungViaEMail.setSelected(true);
      } else {
         this.fahrzeugUntersuchungViaEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("mängelmeldungViaEMailVersenden")).equals("1")) {
         this.mängelmeldungViaEMailVersenden.setSelected(true);
      } else {
         this.mängelmeldungViaEMailVersenden.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigeModus")).equals("1")) {
         this.geburtstageNurHeute.setSelected(true);
      } else if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigeModus")).equals("2")) {
         this.geburtstageNurGanzerMonat.setSelected(true);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("druckAnwesenheitsListeMode")).equals("1")) {
         this.druckAnwesenheitsListeMode1.setSelected(true);
      } else if(((String)runApplication.EINSTELLUNGEN.get("druckAnwesenheitsListeMode")).equals("2")) {
         this.druckAnwesenheitsListeMode2.setSelected(true);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("Schichtplaner")).equals("1")) {
         this.schichtModul.setSelected(true);
      } else {
         this.schichtModul.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("Urlaubsplaner")).equals("1")) {
         this.urlaubModul.setSelected(true);
      } else {
         this.urlaubModul.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("Fahrtenbuch")).equals("1")) {
         this.fahrtenbuchModul.setSelected(true);
      } else {
         this.fahrtenbuchModul.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("modulVeranstaltung")).equals("1")) {
         this.modulVeranstaltung.setSelected(true);
      } else {
         this.modulVeranstaltung.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("modulAusbildungsplan")).equals("1")) {
         this.modulAusbildungsplan.setSelected(true);
      } else {
         this.modulAusbildungsplan.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("modulFahrzeugeinteilung")).equals("1")) {
         this.modulFahrzeugeinteilung.setSelected(true);
      } else {
         this.modulFahrzeugeinteilung.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden")).equals("1")) {
         this.joomlaVeranstaltung.setSelected(true);
      } else {
         this.joomlaVeranstaltung.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("1")) {
         this.joomlaAusbildungsplan.setSelected(true);
      } else {
         this.joomlaAusbildungsplan.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.alwaysOnTop.setSelected(true);
      } else {
         this.alwaysOnTop.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente")).equals("1")) {
         this.joomlaEinsatzkomponente.setSelected(true);
      } else {
         this.joomlaEinsatzkomponente.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible")).equals("1")) {
         this.joomlaEinsatzkomponente_Visible.setSelected(true);
      } else {
         this.joomlaEinsatzkomponente_Visible.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMail")).equals("1")) {
         this.joomlaEinsatzkomponenteEMail.setSelected(true);
      } else {
         this.joomlaEinsatzkomponenteEMail.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailConfig")).equals("1")) {
         this.terminVersandtViaEMailConfig.setSelectedItem("Immer senden");
      } else if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailConfig")).equals("2")) {
         this.terminVersandtViaEMailConfig.setSelectedItem("Nur einmal im Monat");
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailGerätewarteAktiviert")).equals("1")) {
         this.globaleEMailGerätewarteAktivieren.setSelected(true);
      } else {
         this.globaleEMailGerätewarteAktivieren.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsführungAktiviert")).equals("1")) {
         this.globaleEMailEinheitsführungAktivieren.setSelected(true);
      } else {
         this.globaleEMailEinheitsführungAktivieren.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("Statistik2")).equals("1")) {
         this.statistik2.setSelected(true);
      } else {
         this.statistik2.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("EinsatznummerIstPflicht")).equals("1")) {
         this.einsatznummerPflichteintrag.setSelected(true);
      } else {
         this.einsatznummerPflichteintrag.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("EinsatzLeiterBFIstPflicht")).equals("1")) {
         this.einsatzLeiterBFPflichteintrag.setSelected(true);
      } else {
         this.einsatzLeiterBFPflichteintrag.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("headerPrint")).equals("1")) {
         this.headerPrint.setSelected(true);
      } else {
         this.headerPrint.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("footerPrint")).equals("1")) {
         this.footerPrint.setSelected(true);
      } else {
         this.footerPrint.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung")).equals("1")) {
         this.getakteteInternetverbindung.setSelected(true);
      } else {
         this.getakteteInternetverbindung.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("onlineStatus")).equals("1")) {
         this.onlineStatus.setSelected(true);
      } else {
         this.onlineStatus.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteNurAlamierungÜbertragen")).equals("1")) {
         this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen.setSelected(true);
      } else {
         this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln")).equals("1")) {
         this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln.setSelected(true);
      } else {
         this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("WeitereOrganisationen")).equals("1")) {
         this.organisationen.setSelected(true);
      } else {
         this.organisationen.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder")).equals("1")) {
         this.hochzeitFeldFuerMitglieder.setSelected(true);
      } else {
         this.hochzeitFeldFuerMitglieder.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("LehrgangEintragenAusMitgliederVerwaltungMode")).equals("1")) {
         this.LehrgangEintragenAusMitgliederVerwaltungMode.setSelected(true);
      } else {
         this.LehrgangEintragenAusMitgliederVerwaltungMode.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag")).equals("1")) {
         this.mitgliedSeitPflichtEintrag.setSelected(true);
      } else {
         this.mitgliedSeitPflichtEintrag.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("FullBackupInZip")).equals("1")) {
         this.fullBackupInZip.setSelected(true);
      } else {
         this.fullBackupInZip.setSelected(false);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("1")) {
         this.joomlaEinsatzkomponente_Config.setSelectedItem("Hausnummern anzeigen");
      } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("2")) {
         this.joomlaEinsatzkomponente_Config.setSelectedItem("Hausnummern ausblenden");
      } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("3")) {
         this.joomlaEinsatzkomponente_Config.setSelectedItem("Hausnummern ausblenden + Stadtteil anzeigen");
      } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("4")) {
         this.joomlaEinsatzkomponente_Config.setSelectedItem("Hausnummern anzeigen + Stadtteil anzeigen");
      }

      if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("1")) {
         this.joomlaEinsatzkomponenteStichwort.setSelectedItem("Stichwort anzeigen");
      } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("2")) {
         this.joomlaEinsatzkomponenteStichwort.setSelectedItem("Einsatzkategorie anzeigen");
      } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("3")) {
         this.joomlaEinsatzkomponenteStichwort.setSelectedItem("Stichwort + Einsatzkategorie anzeigen");
      }

      if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("1")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Einsatznummer + Stichwort + Straße / Ort");
      } else if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("2")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Einsatznummer + Stichwort");
      } else if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("3")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Einsatznummer");
      } else if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("4")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Stichwort");
      } else if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("5")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Freitext");
      } else if(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("6")) {
         this.verdienstausfallEinsatzbeschreibungOption.setSelectedItem("Einsatznummer + Einsatzkategorie");
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1")).equals("0")) {
            this.joomlaEinsatzkomponenteEMailAn1.setSelectedItem("<bitte wählen>");
         } else {
            this.joomlaEinsatzkomponenteEMailAn1.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1"))));
         }
      } catch (SQLException var9) {
         logging.logPrintStackTrace(var9);
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2")).equals("0")) {
            this.joomlaEinsatzkomponenteEMailAn2.setSelectedItem("<bitte wählen>");
         } else {
            this.joomlaEinsatzkomponenteEMailAn2.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2"))));
         }
      } catch (SQLException var8) {
         logging.logPrintStackTrace(var8);
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3")).equals("0")) {
            this.joomlaEinsatzkomponenteEMailAn3.setSelectedItem("<bitte wählen>");
         } else {
            this.joomlaEinsatzkomponenteEMailAn3.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3"))));
         }
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn1")).equals("0")) {
            this.facebookEMailAn1.setSelectedItem("<bitte wählen>");
         } else {
            this.facebookEMailAn1.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn1"))));
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn2")).equals("0")) {
            this.facebookEMailAn2.setSelectedItem("<bitte wählen>");
         } else {
            this.facebookEMailAn2.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn2"))));
         }
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

      try {
         if(((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn3")).equals("0")) {
            this.facebookEMailAn3.setSelectedItem("<bitte wählen>");
         } else {
            this.facebookEMailAn3.setSelectedItem((new TabelleMitglied()).getNameVornameByID(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("facebookEMailAn3"))));
         }
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

      try {
         this.emailPasswort.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("emailPasswort")));
      } catch (StringIndexOutOfBoundsException var3) {
         ;
      }

      try {
         this.unwetterwarnungPasswort.setText(hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPasswort")));
      } catch (StringIndexOutOfBoundsException var2) {
         ;
      }

      this.datenbankTyp.setText((String)runApplication.PROPERTIES.get("DB_TYP"));
      this.datenbankIP.setText((String)runApplication.PROPERTIES.get("DatenbankIP"));
      this.datenbankName.setText((String)runApplication.PROPERTIES.get("DatenbankName"));
      this.datenbankUser.setText(hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankUser")));
      this.datenbankPasswort.setText(hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankPasswort")));
      this.datenbankPasswort2.setText(hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankPasswort")));
      this.organisation.setText((String)runApplication.PROPERTIES.get("Organisation"));
      this.sshServer.setText((String)runApplication.PROPERTIES.get("SSHServer"));
      this.ftpServer.setText((String)runApplication.PROPERTIES.get("FTPServer"));
   }

   protected void labelHinzufuegen() {
      this.emailModulAktivieren.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            if(EinstellungAO.this.emailModulAktivieren.isSelected()) {
               EinstellungAO.this.buttonTestEMail.setVisible(true);
            } else {
               EinstellungAO.this.buttonTestEMail.setVisible(false);
            }

         }
      });
   }

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Einstellungen");
      this.setSize(1090, 810);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {}

   protected void boxenHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelAnschrift = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelAnschrift);
      this.panelAnschrift.add(this.organisation_label);
      this.panelAnschrift.add(this.organisation);
      this.panelAnschrift.add(this.name_label);
      this.panelAnschrift.add(this.name);
      this.panelAnschrift.add(this.straße_label);
      this.panelAnschrift.add(this.straße);
      this.panelAnschrift.add(this.plz_label);
      this.panelAnschrift.add(this.plz);
      this.panelAnschrift.add(this.stadt_label);
      this.panelAnschrift.add(this.stadt);
      this.panelAnschrift.add(this.bundesland_label);
      this.panelAnschrift.add(this.bundesland);
      this.panelAnschrift.add(this.tel_label);
      this.panelAnschrift.add(this.tel);
      this.panelAnschrift.add(new JLabel());
      this.panelAnschrift.add(new JLabel());
      this.panelAnschrift.add(this.default_location_label);
      this.panelAnschrift.add(this.default_location);
      Border lowerEtched = BorderFactory.createEtchedBorder(1);
      TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Anschrift / Adresse der Organisation");
      this.panelAnschrift.setBorder(title);
      this.panelEMail = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelEMail);
      this.panelEMail.add(this.emailModulAktivieren_label);
      this.panelEMail.add(this.emailModulAktivieren);
      this.panelEMail.add(this.emailAdresse_label);
      this.panelEMail.add(this.emailAdresse);
      this.panelEMail.add(this.emailName_label);
      this.panelEMail.add(this.emailName);
      this.panelEMail.add(this.emailPasswort_label);
      this.panelEMail.add(this.emailPasswort);
      this.panelEMail.add(this.popServer_label);
      this.panelEMail.add(this.popServer);
      this.panelEMail.add(this.popServerPort_label);
      this.panelEMail.add(this.popServerPort);
      this.panelEMail.add(this.smtpServer_label);
      this.panelEMail.add(this.smtpServer);
      this.panelEMail.add(this.smtpServerPort_label);
      this.panelEMail.add(this.smtpServerPort);
      this.panelEMail.add(this.useSSL_label);
      this.panelEMail.add(this.useSSL);
      TitledBorder titleEmail = BorderFactory.createTitledBorder(lowerEtched, "E-Mail Server Konfiguration");
      this.panelEMail.setBorder(titleEmail);
      this.panelStatistikKonfiguration = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelStatistikKonfiguration);
      this.panelStatistikKonfiguration.add(this.fehlalarm_label);
      this.panelStatistikKonfiguration.add(this.fehlalarm);
      this.panelStatistikKonfiguration.add(this.schutzziel1_label);
      this.panelStatistikKonfiguration.add(this.schutzziel1);
      this.panelStatistikKonfiguration.add(this.schutzziel2_label);
      this.panelStatistikKonfiguration.add(this.schutzziel2);
      TitledBorder title3 = BorderFactory.createTitledBorder(lowerEtched, "Statistik Konfiguration");
      this.panelStatistikKonfiguration.setBorder(title3);
      this.panelLehrgang = new JPanel(new GridLayout(13, 2));
      this.getContentPane().add("Center", this.panelLehrgang);
      this.panelLehrgang.add(this.lehrgangsmeldungen_label);
      this.panelLehrgang.add(this.lehrgangsmeldungen);
      TitledBorder title6 = BorderFactory.createTitledBorder(lowerEtched, "Lehrgang");
      this.panelLehrgang.setBorder(title6);
      this.panelVeranstaltung = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelVeranstaltung);
      this.panelVeranstaltung.add(this.vorbelegungDienstabendStart_label);
      this.panelVeranstaltung.add(this.vorbelegungDienstabendStart);
      this.panelVeranstaltung.add(this.vorbelegungDienstabendEnde_label);
      this.panelVeranstaltung.add(this.vorbelegungDienstabendEnde);
      this.panelVeranstaltung.add(this.vorbelegungBSWTreffen_label);
      this.panelVeranstaltung.add(this.vorbelegungBSWTreffen);
      this.panelVeranstaltung.add(this.vorbelegungBSWVeranstaltungStart_label);
      this.panelVeranstaltung.add(this.vorbelegungBSWVeranstaltungStart);
      this.panelVeranstaltung.add(this.vorbelegungBSWEnde_label);
      this.panelVeranstaltung.add(this.vorbelegungBSWEnde);
      this.panelVeranstaltung.add(this.einsatzleiterBFAnzeigen_label);
      this.panelVeranstaltung.add(this.einsatzleiterBFAnzeigen);
      this.panelVeranstaltung.add(this.einsatzleiterBFPflichteintrag_label);
      this.panelVeranstaltung.add(this.einsatzLeiterBFPflichteintrag);
      this.panelVeranstaltung.add(this.einsatznummerPflichteintrag_label);
      this.panelVeranstaltung.add(this.einsatznummerPflichteintrag);
      this.panelVeranstaltung.add(this.feldEintreffenAusblenden_label);
      this.panelVeranstaltung.add(this.feldEintreffenAusblenden);
      this.panelVeranstaltung.add(this.feldStadtteilAusblenden_label);
      this.panelVeranstaltung.add(this.feldStadtteilAusblenden);
      this.panelVeranstaltung.add(this.automatischeFrageNachAtemschutzEinsatz_label);
      this.panelVeranstaltung.add(this.automatischeFrageNachAtemschutzEinsatz);
      this.panelVeranstaltung.add(this.bswHitliste_label);
      this.panelVeranstaltung.add(this.bswHitliste);
      this.panelVeranstaltung.add(this.sichtbarkeitVonVeranstaltungenVergangenheit_label);
      this.panelVeranstaltung.add(this.sichtbarkeitVonVeranstaltungenVergangenheit);
      this.panelVeranstaltung.add(this.sichtbarkeitVonVeranstaltungenZukunft_label);
      this.panelVeranstaltung.add(this.sichtbarkeitVonVeranstaltungenZukunft);
      TitledBorder titleDienst = BorderFactory.createTitledBorder(lowerEtched, "Dienstzeitvorbelegung");
      this.panelVeranstaltung.setBorder(titleDienst);
      this.panelAutoBericht = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelAutoBericht);
      this.panelAutoBericht.add(this.autoBerichtAktiv_label);
      this.panelAutoBericht.add(this.autoBerichtAktiv);
      this.panelAutoBericht.add(this.zeitAutoBericht_label);
      this.panelAutoBericht.add(this.zeitAutoBericht);
      TitledBorder title5 = BorderFactory.createTitledBorder(lowerEtched, "Archivierung / automatischer Bericht erstellung");
      this.panelAutoBericht.setBorder(title5);
      this.panelAlarmKonfiguration = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelAlarmKonfiguration);
      this.panelAlarmKonfiguration.add(this.vorwarnunUntersuchung_label);
      this.panelAlarmKonfiguration.add(this.vorwarnunUntersuchung);
      this.panelAlarmKonfiguration.add(this.vorwarnungAblaufDienstausweis_label);
      this.panelAlarmKonfiguration.add(this.vorwarnungAblaufDienstausweis);
      this.panelAlarmKonfiguration.add(this.vorwarnungAblaufFahrberechtigung_label);
      this.panelAlarmKonfiguration.add(this.vorwarnungAblaufFahrberechtigung);
      this.panelAlarmKonfiguration.add(this.vorwarnungFahrzuegUntersuchung_label);
      this.panelAlarmKonfiguration.add(this.vorwarnungFahrzeugUntersuchung);
      this.panelAlarmKonfiguration.add(this.vorwarnungGeräteprüfung_label);
      this.panelAlarmKonfiguration.add(this.vorwarnungGeräteprüfung);
      this.panelAlarmKonfiguration.add(this.gebAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.gebAnzeigen);
      this.panelAlarmKonfiguration.add(this.geburtstageNurHeute_label);
      this.panelAlarmKonfiguration.add(this.geburtstageNurHeute);
      this.panelAlarmKonfiguration.add(this.geburtstageNurGanzerMonat_label);
      this.panelAlarmKonfiguration.add(this.geburtstageNurGanzerMonat);
      this.panelAlarmKonfiguration.add(this.ablaufDienstausweisAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.ablaufDienstausweisAnzeigen);
      this.panelAlarmKonfiguration.add(this.ablaufFahrberechtigungAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.ablaufFahrberechtigungAnzeigen);
      this.panelAlarmKonfiguration.add(this.ablaufLKWFuehrerscheinAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.ablaufLKWFuehrerscheinAnzeigen);
      this.panelAlarmKonfiguration.add(this.terminAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.terminAnzeigen);
      this.panelAlarmKonfiguration.add(this.agttrainingAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.agttrainingAnzeigen);
      this.panelAlarmKonfiguration.add(this.offeneMängelAnzeigen_label);
      this.panelAlarmKonfiguration.add(this.offeneMängelAnzeigen);
      TitledBorder title4 = BorderFactory.createTitledBorder(lowerEtched, "Informationskonfiguration");
      this.panelAlarmKonfiguration.setBorder(title4);
      this.panelSonstiges = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelSonstiges);
      this.panelSonstiges.add(this.schnittstellenAktivierung_label);
      this.panelSonstiges.add(this.schnittstellenAktivierung);
      this.panelSonstiges.add(this.vCardSeperator_label);
      this.panelSonstiges.add(this.vCardSeperator);
      this.panelSonstiges.add(this.automatischesProgrammUpdate_label);
      this.panelSonstiges.add(this.automatischesProgrammUpdate);
      this.panelSonstiges.add(this.alwaysOnTop_label);
      this.panelSonstiges.add(this.alwaysOnTop);
      this.alwaysOnTop.setEnabled(false);
      this.panelSonstiges.add(this.getakteteInternetverbindung_label);
      this.panelSonstiges.add(this.getakteteInternetverbindung);
      this.panelSonstiges.add(this.onlineStatus_label);
      this.panelSonstiges.add(this.onlineStatus);
      this.panelSonstiges.add(this.anmeldungSpeichernErlauben_label);
      this.panelSonstiges.add(this.anmeldungSpeichernErlauben);
      this.panelSonstiges.add(this.LookAndFeel_label);
      this.panelSonstiges.add(this.LookAndFeel);
      this.panelSonstiges.add(this.google_api_code_label);
      this.panelSonstiges.add(this.google_api_code);
      TitledBorder title7 = BorderFactory.createTitledBorder(lowerEtched, "Sonstige Einstellungen");
      this.panelSonstiges.setBorder(title7);
      this.panelDbSicherung = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelDbSicherung);
      this.panelDbSicherung.add(this.autoDBsave_label);
      this.panelDbSicherung.add(this.autoDBsave);
      this.panelDbSicherung.add(this.autoDBsaveTage_label);
      this.panelDbSicherung.add(this.autoDBsaveTage);
      this.panelDbSicherung.add(this.fullBackupInZip_label);
      this.panelDbSicherung.add(this.fullBackupInZip);
      this.panelDbSicherung.add(this.fullBackupPath_label);
      this.panelDbSicherung.add(this.fullBackupPath);
      TitledBorder title8 = BorderFactory.createTitledBorder(lowerEtched, "Datensicherung");
      this.panelDbSicherung.setBorder(title8);
      this.panelEMailInformation = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelEMailInformation);
      this.panelEMailInformation.add(this.terminVersandtViaEMail_label);
      this.panelEMailInformation.add(this.terminVersandtViaEMail);
      this.panelEMailInformation.add(this.terminVersandtViaEMailFolgeMonat_label);
      this.panelEMailInformation.add(this.terminVersandtViaEMailFolgeMonat);
      this.panelEMailInformation.add(this.terminVersandtViaEMailConfig_label);
      this.panelEMailInformation.add(this.terminVersandtViaEMailConfig);
      this.panelEMailInformation.add(this.untersuchungViaEMail_label);
      this.panelEMailInformation.add(this.untersuchungViaEMail);
      this.panelEMailInformation.add(this.ablaufDienstausweisViaEMail_label);
      this.panelEMailInformation.add(this.ablaufDienstausweisViaEMail);
      this.panelEMailInformation.add(this.ablaufFahrberechtigungViaEMail_label);
      this.panelEMailInformation.add(this.ablaufFahrberechtigungViaEMail);
      this.panelEMailInformation.add(this.ablaufLKWFührerscheinViaEMail_label);
      this.panelEMailInformation.add(this.ablaufLKWFührerscheinViaEMail);
      this.panelEMailInformation.add(this.geraetepruefungenViaEMail_label);
      this.panelEMailInformation.add(this.geraetepruefungenViaEMail);
      this.panelEMailInformation.add(this.fahrzeugUntersuchungViaEMail_label);
      this.panelEMailInformation.add(this.fahrzeugUntersuchungViaEMail);
      this.panelEMailInformation.add(this.mängelmeldungenViaEMailVersenden_label);
      this.panelEMailInformation.add(this.mängelmeldungViaEMailVersenden);
      this.panelEMailInformation.add(this.untersuchungViaEMailChefBCC_label);
      this.panelEMailInformation.add(this.untersuchungViaEMailChefBCC);
      if(runApplication.JavaWebStart == 0) {
         this.panelEMailInformation.add(new JLabel());
         this.panelEMailInformation.add(this.buttonZyklischenEMailAuftragErstellen);
      }

      TitledBorder title9 = BorderFactory.createTitledBorder(lowerEtched, "E-Mail Informationen");
      this.panelEMailInformation.setBorder(title9);
      this.panelGlobaleEMailAdressen = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelGlobaleEMailAdressen);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailEinheitsführungAktivieren_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailEinheitsführungAktivieren);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailEinheitsführung_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailEinheitsführung);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailGerätewarteAktivieren_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailGerätewarteAktivieren);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailGerätewarte_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailGerätewarte);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG25Aktiviert_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG25Aktiviert);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG25_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG25);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG26Aktiviert_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG26Aktiviert);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG26_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailG26);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailFahrberechtigungAktiviert_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailFahrberechtigungAktiviert);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailFahrberechtigung_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailFahrberechtigung);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailDienstausweisAktiviert_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailDienstausweisAktiviert);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailDienstausweis_label);
      this.panelGlobaleEMailAdressen.add(this.globaleEMailDienstausweis);
      TitledBorder title11 = BorderFactory.createTitledBorder(lowerEtched, "Globale E-Mail Adressen");
      this.panelGlobaleEMailAdressen.setBorder(title11);
      this.panelDatenbankEinstellungen = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelDatenbankEinstellungen);
      this.panelDatenbankEinstellungen.add(this.datenbankTyp_label);
      this.panelDatenbankEinstellungen.add(this.datenbankTyp);
      this.panelDatenbankEinstellungen.add(this.datenbankName_label);
      this.panelDatenbankEinstellungen.add(this.datenbankName);
      this.panelDatenbankEinstellungen.add(this.datenbankIP_label);
      this.panelDatenbankEinstellungen.add(this.datenbankIP);
      this.panelDatenbankEinstellungen.add(this.datenbankUser_label);
      this.panelDatenbankEinstellungen.add(this.datenbankUser);
      this.panelDatenbankEinstellungen.add(this.datenbankIP_label);
      this.panelDatenbankEinstellungen.add(this.datenbankIP);
      this.panelDatenbankEinstellungen.add(this.datenbankPasswort_label);
      this.panelDatenbankEinstellungen.add(this.datenbankPasswort);
      this.panelDatenbankEinstellungen.add(this.datenbankPasswort2_label);
      this.panelDatenbankEinstellungen.add(this.datenbankPasswort2);
      if(((String)runApplication.PROPERTIES.get("DB_TYP")).equals("SSH")) {
         this.panelDatenbankEinstellungen.add(this.sshServer_label);
         this.panelDatenbankEinstellungen.add(this.sshServer);
         this.panelDatenbankEinstellungen.add(this.ftpServer_label);
         this.panelDatenbankEinstellungen.add(this.ftpServer);
      }

      TitledBorder title10 = BorderFactory.createTitledBorder(lowerEtched, "Datenbank Einstellungen");
      this.panelDatenbankEinstellungen.setBorder(title10);
      this.panelModule = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelModule);
      this.panelModule.add(this.modulVeranstaltung_label);
      this.panelModule.add(this.modulVeranstaltung);
      this.panelModule.add(this.modulAusbildungsplan_label);
      this.panelModule.add(this.modulAusbildungsplan);
      this.panelModule.add(this.schichtModul_label);
      this.panelModule.add(this.schichtModul);
      this.panelModule.add(this.urlaubModul_label);
      this.panelModule.add(this.urlaubModul);
      this.panelModule.add(this.abrechnungModuleAnzeigen_label);
      this.panelModule.add(this.abrechnungModuleAnzeigen);
      this.panelModule.add(this.fahrtenbuchModul_label);
      this.panelModule.add(this.fahrtenbuchModul);
      this.panelModule.add(this.modulFahrzeugeinteilung_label);
      this.panelModule.add(this.modulFahrzeugeinteilung);
      this.panelModule.add(this.organisationen_label);
      this.panelModule.add(this.organisationen);
      this.panelModule.add(this.modulMitgliederVerfügbarkeit_label);
      this.panelModule.add(this.modulMitgliederVerfügbarkeit);
      this.panelModule.add(this.modulEinsatzgebiet_label);
      this.panelModule.add(this.modulEinsatzgebiet);
      this.panelModule.add(this.schulungAdminModul_label);
      this.panelModule.add(this.schulungAdminModul);
      this.panelModule.add(this.schulungClientModule_label);
      this.panelModule.add(this.schulungClientModul);
      TitledBorder title12 = BorderFactory.createTitledBorder(lowerEtched, "Module");
      this.panelModule.setBorder(title12);
      this.panelJoomla = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelJoomla);
      this.panelJoomla.add(this.joomlaLink_label);
      this.panelJoomla.add(this.joomlaLink);
      this.panelJoomla.add(this.joomlaVeranstaltung_label);
      this.panelJoomla.add(this.joomlaVeranstaltung);
      this.panelJoomla.add(this.joomlaAusbildungsplan_label);
      this.panelJoomla.add(this.joomlaAusbildungsplan);
      this.panelJoomla.add(this.joomlaEinsatzkomponente_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponente);
      this.panelJoomla.add(this.joomlaEinsatzkomponente_Visible_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponente_Visible);
      this.panelJoomla.add(this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen_label);
      this.panelJoomla.add(this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen);
      this.panelJoomla.add(this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln_label);
      this.panelJoomla.add(this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln);
      this.panelJoomla.add(this.joomlaEinsatzkomponente_Config_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponente_Config);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteStichwort_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteStichwort);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteSecretKey_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteSecretKey);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMail_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMail);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn1_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn1);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn2_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn2);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn3_label);
      this.panelJoomla.add(this.joomlaEinsatzkomponenteEMailAn3);
      TitledBorder title13 = BorderFactory.createTitledBorder(lowerEtched, "Internet - Joomla Seite");
      this.panelJoomla.setBorder(title13);
      this.panelFacebook = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelFacebook);
      this.panelFacebook.add(this.facebookAccessToken_label);
      this.panelFacebook.add(this.facebookAccessToken);
      this.panelFacebook.add(this.facebookAppGeheimCode_label);
      this.panelFacebook.add(this.facebookAppGeheimCode);
      this.panelFacebook.add(this.facebookAppID_label);
      this.panelFacebook.add(this.facebookAppID);
      this.panelFacebook.add(this.facebookAutoPostEinsatz_label);
      this.panelFacebook.add(this.facebookAutoPostEinsatz);
      this.panelFacebook.add(this.facebookEMail_label);
      this.panelFacebook.add(this.facebookEMail);
      this.panelFacebook.add(this.facebookEMailAn1_label);
      this.panelFacebook.add(this.facebookEMailAn1);
      this.panelFacebook.add(this.facebookEMailAn2_label);
      this.panelFacebook.add(this.facebookEMailAn2);
      this.panelFacebook.add(this.facebookEMailAn3_label);
      this.panelFacebook.add(this.facebookEMailAn3);
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(this.buttonExtendFacebookAccessToken);
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(this.buttonFacebookDevelpmentPage);
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(new JLabel());
      this.panelFacebook.add(new JLabel());
      TitledBorder title20 = BorderFactory.createTitledBorder(lowerEtched, "Facebook");
      this.panelFacebook.setBorder(title20);
      this.panelVeranstaltungBerichte = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelVeranstaltungBerichte);
      this.panelVeranstaltungBerichte.add(this.einsatzBerichtArt_label);
      this.panelVeranstaltungBerichte.add(this.einsatzberichtArt);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtFahrzeugbelegungHinzufügen_label);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtFahrzeugbelegungHinzufügen);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtAtemschutzpassHinzufügen_label);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtAtemschutzpassHinzufügen);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtEinsatzleiterMitDienstgrad_label);
      this.panelVeranstaltungBerichte.add(this.EinsatzBerichtEinsatzleiterMitDienstgrad);
      this.panelVeranstaltungBerichte.add(this.verdienstausfallArt_label);
      this.panelVeranstaltungBerichte.add(this.verdienstausfallArt);
      this.panelVeranstaltungBerichte.add(this.verdienstausfallEinsatzbeschreibungOption_label);
      this.panelVeranstaltungBerichte.add(this.verdienstausfallEinsatzbeschreibungOption);
      this.panelVeranstaltungBerichte.add(this.bestaetignungFreistellungEinsatzAktiv_label);
      this.panelVeranstaltungBerichte.add(this.bestaetignungFreistellungEinsatzAktiv);
      this.panelVeranstaltungBerichte.add(this.bestaetignungFreistellungEinsatzArt_label);
      this.panelVeranstaltungBerichte.add(this.bestaetignungFreistellungEinsatzArt);
      this.panelVeranstaltungBerichte.add(this.maengelmeldungArt_label);
      this.panelVeranstaltungBerichte.add(this.maengelmeldungArt);
      TitledBorder title14 = BorderFactory.createTitledBorder(lowerEtched, "Veranstaltungsoptionen - Berichte");
      this.panelVeranstaltungBerichte.setBorder(title14);
      this.panelMitgliederverwaltung = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelMitgliederverwaltung);
      this.panelMitgliederverwaltung.add(this.mitgliedSeitPflichtEintrag_label);
      this.panelMitgliederverwaltung.add(this.mitgliedSeitPflichtEintrag);
      this.panelMitgliederverwaltung.add(this.mitgliedSeitFormat_label);
      this.panelMitgliederverwaltung.add(this.mitgliedSeitFormat);
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(this.hochzeitFeldFuerMitglieder_label);
      this.panelMitgliederverwaltung.add(this.hochzeitFeldFuerMitglieder);
      this.panelMitgliederverwaltung.add(this.prüfungDerFahrerlaubnis_label);
      this.panelMitgliederverwaltung.add(this.prüfungDerFahrerlaubnis);
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(this.mitgliederGesundheitTab_label);
      this.panelMitgliederverwaltung.add(this.mitgliederGesundheitTab);
      this.panelMitgliederverwaltung.add(this.LehrgangEintragenAusMitgliederVerwaltungMode_label);
      this.panelMitgliederverwaltung.add(this.LehrgangEintragenAusMitgliederVerwaltungMode);
      this.panelMitgliederverwaltung.add(this.darstellungLehrgängeMitgliederverwaltung_label);
      this.panelMitgliederverwaltung.add(this.darstellungLehrgängeMitgliederverwaltung);
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      this.panelMitgliederverwaltung.add(new JLabel());
      TitledBorder title15 = BorderFactory.createTitledBorder(lowerEtched, "Mitgliederverwaltungsoptionen");
      this.panelMitgliederverwaltung.setBorder(title15);
      this.panelDruckOptionen = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelDruckOptionen);
      this.panelDruckOptionen.add(this.footerPrint_label);
      this.panelDruckOptionen.add(this.footerPrint);
      this.panelDruckOptionen.add(this.headerPrint_label);
      this.panelDruckOptionen.add(this.headerPrint);
      this.panelDruckOptionen.add(this.zeilenhöheAnsicht_label);
      this.panelDruckOptionen.add(this.zeilenhöheAnsicht);
      this.panelDruckOptionen.add(this.zeilenhöheDruck_label);
      this.panelDruckOptionen.add(this.zeilenhöheDruck);
      this.panelDruckOptionen.add(this.druckAnwesenheitsListeMode1_label);
      this.panelDruckOptionen.add(this.druckAnwesenheitsListeMode1);
      this.panelDruckOptionen.add(this.druckAnwesenheitsListeMode2_label);
      this.panelDruckOptionen.add(this.druckAnwesenheitsListeMode2);
      this.panelDruckOptionen.add(this.dienstgradAufAnwesenheitsliste_label);
      this.panelDruckOptionen.add(this.dienstgradAufAnwesenheitsliste);
      this.panelDruckOptionen.add(this.schfiftgrößeAnwesenheitsliste_label);
      this.panelDruckOptionen.add(this.schfiftgrößeAnwesenheitsliste);
      this.panelDruckOptionen.add(this.langesDatumsformatUntersuchungsliste_label);
      this.panelDruckOptionen.add(this.langesDatumsformatUntersuchungsliste);
      TitledBorder title16 = BorderFactory.createTitledBorder(lowerEtched, "Liste Optionen");
      this.panelDruckOptionen.setBorder(title16);
      this.panelDWDUnwetterwarnung = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelDWDUnwetterwarnung);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungModulAktiv_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungModulAktiv);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungEMail_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungEMail);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPasswort_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPasswort);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPop3_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPop3);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPopPort_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungPopPort);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungSSL_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungSSL);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungWebKonfig_label);
      this.panelDWDUnwetterwarnung.add(this.unwetterwarnungWebKonfig);
      TitledBorder title17 = BorderFactory.createTitledBorder(lowerEtched, "DWD Unwettermodul");
      this.panelDWDUnwetterwarnung.setBorder(title17);
      this.panelTerminDisplay = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelTerminDisplay);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeAnazahlVeranstaltungen_label);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeAnazahlVeranstaltungen);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe_label);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeDauerVeranstaltungen_label);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeDauerVeranstaltungen);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeDauerUhr_label);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigeDauerUhr);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigenLetzenEinsatz_label);
      this.panelTerminDisplay.add(this.TerminDisplay_AnzeigenLetzenEinsatz);
      this.panelTerminDisplay.add(this.TerminDisplay_LetzterEinsatzOrtAnzeigen_label);
      this.panelTerminDisplay.add(this.TerminDisplay_LetzterEinsatzOrtAnzeigen);
      this.panelTerminDisplay.add(this.TerminDisplay_HintergrundBildAktivieren_label);
      this.panelTerminDisplay.add(this.TerminDisplay_HintergrundBildAktivieren);
      this.panelTerminDisplay.add(this.TerminDisplay_HintergrundBild_label);
      this.panelTerminDisplay.add(this.TerminDisplay_HintergrundBild);
      this.panelTerminDisplay.add(new JLabel());
      this.panelTerminDisplay.add(this.buttonTerminDisplayHintergrund);
      TitledBorder title18 = BorderFactory.createTitledBorder(lowerEtched, "Termin Display");
      this.panelTerminDisplay.setBorder(title18);
      this.panelExterneKartenDatenbank = new JPanel(new GridLayout(14, 2));
      this.getContentPane().add("Center", this.panelExterneKartenDatenbank);
      this.panelExterneKartenDatenbank.add(this.externeDatenbankFürKartendaten_label);
      this.panelExterneKartenDatenbank.add(this.externeDatenbankFürKartendaten);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankLokalesBackup_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankLokalesBackup);
      this.panelExterneKartenDatenbank.add(this.externeKartenDB_Typ_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDB_Typ);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankIP_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankIP);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankPort_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankPort);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankName_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankName);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankUser_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankUser);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankPasswort_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenDatenbankPasswort);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHServer_label);
      this.panelExterneKartenDatenbank.add(new JLabel());
      this.panelExterneKartenDatenbank.add(new JLabel());
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHServer_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHServer);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHServerPort_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHServerPort);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHUser_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHUser);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHPasswort_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHPasswort);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHTunnel_label);
      this.panelExterneKartenDatenbank.add(this.externeKartenSSHTunnel);
      TitledBorder title19 = BorderFactory.createTitledBorder(lowerEtched, "Externe Karten Datenbank");
      this.panelExterneKartenDatenbank.setBorder(title19);
      this.tabPane.addTab("Sonstiges              ", this.panelSonstiges);
      this.tabPane.addTab("Internet - Joomla Seite", this.panelJoomla);
      this.tabPane.addTab("Facebook               ", this.panelFacebook);
      this.tabPane.addTab("Lehrgang               ", this.panelLehrgang);
      this.tabPane.addTab("Statistik              ", this.panelStatistikKonfiguration);
      this.tabPane.addTab("Automatischer Bericht  ", this.panelAutoBericht);
      this.tabPane.addTab("Datensicherung         ", this.panelDbSicherung);
      this.tabPane.addTab("Datenbank Einstellungen", this.panelDatenbankEinstellungen);
      this.tabPane.addTab("Module                 ", this.panelModule);
      this.tabPane.addTab("Informationskonfiguration", this.panelAlarmKonfiguration);
      this.tabPane.addTab("Veranstaltungsoptionen ", this.panelVeranstaltung);
      this.tabPane.addTab("Mitgliederverwaltungsoptionen ", this.panelMitgliederverwaltung);
      this.tabPane.addTab("Veranstaltungsoptionen - Berichte ", this.panelVeranstaltungBerichte);
      this.tabPane.addTab("E-Mail Information     ", this.panelEMailInformation);
      this.tabPane.addTab("E-Mail Konfiguration   ", this.panelEMail);
      this.tabPane.addTab("Globale E-Mail Adressen", this.panelGlobaleEMailAdressen);
      this.tabPane.addTab("Anschrift              ", this.panelAnschrift);
      this.tabPane.addTab("Listen- / Druckoptionen ", this.panelDruckOptionen);
      this.tabPane.addTab("DWD Unwettermodul      ", this.panelDWDUnwetterwarnung);
      this.tabPane.addTab("Termin Display         ", this.panelTerminDisplay);
      this.tabPane.addTab("Externe Karten Datenbank", this.panelExterneKartenDatenbank);
      this.tabPane.setPreferredSize(new Dimension(1010, 430));
      this.add(this.tabPane);
      this.panelEinsatzBericht = new JPanel(new GridLayout(5, 3));
      this.getContentPane().add("Center", this.panelEinsatzBericht);
      this.panelEinsatzBericht.add(this.einsatzbericht_label);
      this.panelEinsatzBericht.add(this.einsatzbericht);
      this.panelEinsatzBericht.add(this.buttonEinsatzberichtAuswahl);
      this.panelEinsatzBericht.add(this.verdienstausfall_label);
      this.panelEinsatzBericht.add(this.verdienstausfall);
      this.panelEinsatzBericht.add(this.buttonVerdienstausfallAuswahl);
      this.panelEinsatzBericht.add(this.bestaetignungFreistellungEinsatz_label);
      this.panelEinsatzBericht.add(this.bestaetignungFreistellungEinsatz);
      this.panelEinsatzBericht.add(this.buttonBestaetignungFreistellungEinsatzAuswahl);
      this.panelEinsatzBericht.add(this.mängelmeldung_label);
      this.panelEinsatzBericht.add(this.mängelmeldung);
      this.panelEinsatzBericht.add(this.buttonMängelmeldungAuswahl);
      this.panelEinsatzBericht.add(this.briefkopf_label);
      this.panelEinsatzBericht.add(this.briefkopf);
      this.panelEinsatzBericht.add(this.buttonBriefkopfAuswahl);
      TitledBorder title2 = BorderFactory.createTitledBorder(lowerEtched, "Dokumente / Berichte");
      this.panelEinsatzBericht.setBorder(title2);
      this.add(this.kurzbeschreibung);
      this.add(this.kurzbeschreibung2);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonTestEMail);
      this.add(this.buttonspeichern);
      if(this.emailModulAktivieren.isSelected()) {
         this.buttonTestEMail.setVisible(true);
      } else {
         this.buttonTestEMail.setVisible(false);
      }

      this.datenbankTyp.setEditable(false);
      this.datenbankIP.setEditable(false);
      this.datenbankName.setEditable(false);
      this.datenbankUser.setEditable(false);
      this.datenbankPasswort.setEditable(false);
      this.datenbankPasswort2.setEditable(false);
      this.organisation.setEditable(false);
      this.sshServer.setEditable(false);
      this.ftpServer.setEditable(false);
   }

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonFacebookDevelpmentPage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               Desktop.getDesktop().browse(new URI("https://developers.facebook.com/tools-and-support/"));
            } catch (URISyntaxException var3) {
               logging.logError("Beim öffnen des Browsers ist ein Fehler aufgetreten...");
            }

         }
      });
      this.buttonExtendFacebookAccessToken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               if(EinstellungAO.this.facebookAccessToken.getText().equals("") | EinstellungAO.this.facebookAppID.getText().equals("") | EinstellungAO.this.facebookAppGeheimCode.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.FACEBOOK_API_PRUEFEN, "Warnung", 2);
               } else {
                  ImageIcon e1 = (new images()).loadImagesFromJARFacebookIcon();
                  e1.setImage(e1.getImage().getScaledInstance(100, 100, 5));
                  TabelleEinstellungen einstellungen = new TabelleEinstellungen();
                  einstellungen.update("facebookAccessToken", EinstellungAO.this.facebookAccessToken.getText());
                  einstellungen.update("facebookAppID", EinstellungAO.this.facebookAppID.getText());
                  einstellungen.update("facebookAppGeheimCode", EinstellungAO.this.facebookAppGeheimCode.getText());
                  Facebook fb = new Facebook();
                  fb.getExtendedAccesToken();
                  EinstellungAO.this.facebookAccessToken.setText((String)runApplication.EINSTELLUNGEN.get("facebookAccessToken"));
                  JOptionPane.showMessageDialog((Component)null, "Die Facebook-API ist richtig konfiguriert!\n\n" + fb.getUserInformations() + Konstante.FACEBOOK_ACCESSTOKEN_GUELTIGKEIT + "\n" + (String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("facebookAccessTokenExpiereDate"), "Facebook-API-Info", 1, e1);
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonTestEMail.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleEinstellungen e = new TabelleEinstellungen();
               e.update("smtpPort", EinstellungAO.this.smtpServerPort.getText());
               e.update("smtpServer", EinstellungAO.this.smtpServer.getText());
               e.update("pop3Server", EinstellungAO.this.popServer.getText());
               e.update("pop3Server", EinstellungAO.this.popServer.getText());
               e.update("emailAdresse", EinstellungAO.this.emailAdresse.getText());
               e.update("eMailName", EinstellungAO.this.emailName.getText());
               e.update("emailPasswort", hash.createHashCode(EinstellungAO.this.emailPasswort.getText()));
               if(EinstellungAO.this.useSSL.isSelected()) {
                  e.update("useSSL", "1");
               } else {
                  e.update("useSSL", "0");
               }

               runApplication.EINSTELLUNGEN = e.getAllEinstellungen();
               logging.logInfo("E-Mail Einstellungen wurden aktualiesiert, jetzt kann die Verbindung getestet werden");
               if(EinstellungAO.this.popServer.getText().equals("") | EinstellungAO.this.smtpServer.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_POSTEINGENG_UND_POSTAUSGANG_KONFIGURIEREN, "Warnung", 2);
               } else if(EinstellungAO.this.popServerPort.getText().equals("") | EinstellungAO.this.smtpServerPort.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_POSTEINGENG_UND_POSTAUSGANGPORT_KONFIGURIEREN, "Warnung", 2);
               } else {
                  SendeOpperation.senden(EinstellungAO.this.emailAdresse.getText(), "", "", "Testnachricht", "Testnachricht vom FeuerwehrManagementSystem", ErstelleFileArrayForAnhang.analysiereString(""));
                  JOptionPane.showMessageDialog((Component)null, Konstante.SENDEN_ERFOLGREICH);
               }
            } catch (UnsupportedEncodingException var3) {
               JOptionPane.showMessageDialog((Component)null, "Das senden der E-Mail ist Fehlgeschlagen\nEin Grund hierfür könnte sein, dass die Servereinstellungen nicht richtig sind.\nBitte geben sie die richtigen Serveradressen und Ports an.\nDiese Informationen erhalten Sie von Ihrem Provider.\n\n\nGrund (Details):\n" + var3, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.unwetterwarnungWebKonfig.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               Desktop.getDesktop().browse(new URI("http://www.dwd.de/DE/service/newsletter/newsletter_amtliche_warnungen_node.html"));
            } catch (URISyntaxException var3) {
               logging.logError("Beim öffnen des Browsers ist ein Fehler aufgetreten...");
            }

         }
      });
      this.buttonBriefkopfAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserJPEG.setFileFilter(EinstellungAO.this.filterJPEG);
            int returnVal = EinstellungAO.this.chooserJPEG.showOpenDialog(EinstellungAO.this.chooserJPEG);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserJPEG.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/" + EinstellungAO.this.chooserJPEG.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserJPEG.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf");
            EinstellungAO.this.briefkopf.setText(runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/" + EinstellungAO.this.chooserJPEG.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonEinsatzberichtAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserXML.setFileFilter(EinstellungAO.this.filterXML);
            int returnVal = EinstellungAO.this.chooserXML.showOpenDialog(EinstellungAO.this.chooserXML);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserXML.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Einsatzbericht/" + EinstellungAO.this.chooserXML.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserXML.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Einsatzbericht");
            EinstellungAO.this.einsatzbericht.setText(runApplication.arbeitsverzeichnis + "data/Templates/Einsatzbericht/" + EinstellungAO.this.chooserXML.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Einsatzbericht/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonMängelmeldungAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserXML.setFileFilter(EinstellungAO.this.filterXML);
            int returnVal = EinstellungAO.this.chooserXML.showOpenDialog(EinstellungAO.this.chooserXML);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserXML.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Mängelmeldung/" + EinstellungAO.this.chooserXML.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserXML.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Mängelmeldung");
            EinstellungAO.this.mängelmeldung.setText(runApplication.arbeitsverzeichnis + "data/Templates/Mängelmeldung/" + EinstellungAO.this.chooserXML.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Mängelmeldung/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonVerdienstausfallAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserXML.setFileFilter(EinstellungAO.this.filterXML);
            int returnVal = EinstellungAO.this.chooserXML.showOpenDialog(EinstellungAO.this.chooserXML);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserXML.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Verdienstausfall/" + EinstellungAO.this.chooserXML.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserXML.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Verdienstausfall");
            EinstellungAO.this.verdienstausfall.setText(runApplication.arbeitsverzeichnis + "data/Templates/Verdienstausfall/" + EinstellungAO.this.chooserXML.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Verdienstausfall/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonBestaetignungFreistellungEinsatzAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserXML.setFileFilter(EinstellungAO.this.filterXML);
            int returnVal = EinstellungAO.this.chooserXML.showOpenDialog(EinstellungAO.this.chooserXML);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserXML.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Bestaetigung/" + EinstellungAO.this.chooserXML.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserXML.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Bestaetigung");
            EinstellungAO.this.bestaetignungFreistellungEinsatz.setText(runApplication.arbeitsverzeichnis + "data/Templates/Bestaetigung/" + EinstellungAO.this.chooserXML.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Bestaetigung/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonTerminDisplayHintergrund.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinstellungAO.this.chooserXML.setFileFilter(EinstellungAO.this.filterJPEG);
            int returnVal = EinstellungAO.this.chooserXML.showOpenDialog(EinstellungAO.this.chooserXML);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + EinstellungAO.this.chooserXML.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/" + EinstellungAO.this.chooserXML.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(EinstellungAO.this.chooserXML.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf");
            EinstellungAO.this.TerminDisplay_HintergrundBild.setText(runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/" + EinstellungAO.this.chooserXML.getSelectedFile().getName());

            try {
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Briefkopf/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonZyklischenEMailAuftragErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(((String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("ZyklischerEMailAuftrag")).equals("0")) {
               JFrame executeCmd = new JFrame("Frage");
               String[] runtimeProcess = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
               String ex = (String)JOptionPane.showInputDialog(executeCmd, Konstante.ZYKLISCHER_EMAIL_AUFTRAG, "Frage", 3, (Icon)null, runtimeProcess, runtimeProcess[0]);
               if(ex != null) {
                  String[] executeCmd1 = new String[]{"schtasks", "/create", "/tn", "\"FeuerwehrManagementSystem - E-Mail Auftrag\"", "/tr", "\"" + System.getProperty("user.dir") + "\\FMS_EMailService.exe" + "\"", "/sc", "daily", "/st", ex};

                  try {
                     logging.logInfo("Lege Zyklischen E-Mail Auftrag an...");
                     logging.logInfo("Ausführende Datei: " + System.getProperty("user.dir") + "\\FMS_EMailService.exe");
                     Process runtimeProcess1 = Runtime.getRuntime().exec(executeCmd1);
                     int ex1 = runtimeProcess1.waitFor();
                     if(ex1 == 0) {
                        logging.logInfo("E-Mail Auftrag erfolgreich angelegt");
                        (new TabelleEinstellungen_gespeichert()).update("ZyklischerEMailAuftrag", "1");
                        runApplication.EINSTELLUNGEN_GESPEICHERT = (new TabelleEinstellungen_gespeichert()).getAllEinstellungen();
                        EinstellungAO.this.buttonZyklischenEMailAuftragErstellen.setText("Zyklischen E-Mail Auftrag löschen");
                     } else {
                        logging.logError("E-Mail Auftrag nicht erfolgreich angelegt");
                     }
                  } catch (Exception var9) {
                     logging.logPrintStackTrace(var9);
                  }
               }
            } else {
               String[] executeCmd2 = new String[]{"schtasks", "/delete", "/tn", "\"FeuerwehrManagementSystem - E-Mail Auftrag\"", "/F"};

               try {
                  logging.logInfo("Lösche Zyklischen E-Mail Auftrag an...");
                  Process runtimeProcess2 = Runtime.getRuntime().exec(executeCmd2);
                  int ex2 = runtimeProcess2.waitFor();
                  if(ex2 == 0) {
                     logging.logInfo("E-Mail Auftrag erfolgreich gelöscht");
                     (new TabelleEinstellungen_gespeichert()).update("ZyklischerEMailAuftrag", "0");
                     runApplication.EINSTELLUNGEN_GESPEICHERT = (new TabelleEinstellungen_gespeichert()).getAllEinstellungen();
                     EinstellungAO.this.buttonZyklischenEMailAuftragErstellen.setText("Zyklischen E-Mail Auftrag erstellen");
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     logging.logError("E-Mail Auftrag nicht erfolgreich gelöscht");
                  }
               } catch (Exception var8) {
                  logging.logPrintStackTrace(var8);
               }
            }

         }
      });
      this.buttonspeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(Integer.parseInt(EinstellungAO.this.vorwarnunUntersuchung.getText()) >= 13) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VORWARNUNG, "Warnung", 2);
            } else if(EinstellungAO.this.bundesland.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_BUNDESLAND_WAEHLEN, "Warnung", 2);
            } else {
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               ProzessBarAO.progressbar.setStringPainted(false);
               ProzessBarAO.progressbar.setIndeterminate(true);
               ProzessBarAO.label_bitteWarten.setText("Einstellungen werden gespeichert... Bitte warten...");
               Thread threadSpeichern = new Thread() {
                  public void run() {
                     TabelleEinstellungen einstellungen = new TabelleEinstellungen();

                     try {
                        einstellungen.update("EinsatzBericht", Utils.removeBackSlashFromString(EinstellungAO.this.einsatzbericht.getText()));
                        einstellungen.update("Name", EinstellungAO.this.name.getText());
                        einstellungen.update("Stadt", EinstellungAO.this.stadt.getText());
                        einstellungen.update("verdienstausfall", Utils.removeBackSlashFromString(EinstellungAO.this.verdienstausfall.getText()));
                        einstellungen.update("fehlalarm", EinstellungAO.this.fehlalarm.getText());
                        einstellungen.update("telefon", EinstellungAO.this.tel.getText());
                        einstellungen.update("strasse", EinstellungAO.this.straße.getText());
                        einstellungen.update("plz", EinstellungAO.this.plz.getText());
                        einstellungen.update("briefkopf", Utils.removeBackSlashFromString(EinstellungAO.this.briefkopf.getText()));
                        einstellungen.update("untersuchungVorwarnung", EinstellungAO.this.vorwarnunUntersuchung.getText());
                        einstellungen.update("untersuchungVorwarnungFahrzeug", EinstellungAO.this.vorwarnungFahrzeugUntersuchung.getText());
                        einstellungen.update("ZeitAutoBericht", EinstellungAO.this.zeitAutoBericht.getText());
                        einstellungen.update("mängelmeldung", Utils.removeBackSlashFromString(EinstellungAO.this.mängelmeldung.getText()));
                        einstellungen.update("vorwarnungGeräte", EinstellungAO.this.vorwarnungGeräteprüfung.getText());
                        einstellungen.update("WieVieleLehrgangsmeldungenProJahr", EinstellungAO.this.lehrgangsmeldungen.getText());
                        einstellungen.update("smtpPort", EinstellungAO.this.smtpServerPort.getText());
                        einstellungen.update("smtpServer", EinstellungAO.this.smtpServer.getText());
                        einstellungen.update("pop3Server", EinstellungAO.this.popServer.getText());
                        einstellungen.update("pop3Server", EinstellungAO.this.popServer.getText());
                        einstellungen.update("emailAdresse", EinstellungAO.this.emailAdresse.getText());
                        einstellungen.update("eMailName", EinstellungAO.this.emailName.getText());
                        einstellungen.update("emailPasswort", hash.createHashCode(EinstellungAO.this.emailPasswort.getText()));
                        einstellungen.update("vorbelegungDienstStart", EinstellungAO.this.vorbelegungDienstabendStart.getText());
                        einstellungen.update("vorbelegungDienstEnde", EinstellungAO.this.vorbelegungDienstabendEnde.getText());
                        einstellungen.update("autoDBsaveTage", EinstellungAO.this.autoDBsaveTage.getText());
                        einstellungen.update("vCardSeperator", EinstellungAO.this.vCardSeperator.getSelectedItem().toString());
                        einstellungen.update("vorbelegungBSWTreffen", EinstellungAO.this.vorbelegungBSWTreffen.getText());
                        einstellungen.update("vorbelegungBSWVeranstaltungStart", EinstellungAO.this.vorbelegungBSWVeranstaltungStart.getText());
                        einstellungen.update("vorbelegungBSWEnde", EinstellungAO.this.vorbelegungBSWEnde.getText());
                        einstellungen.update("EinsatzBerichtArt", EinstellungAO.this.einsatzberichtArt.getSelectedItem().toString());
                        einstellungen.update("SichtbarkeitVeranstaltungVergangenheit", EinstellungAO.this.sichtbarkeitVonVeranstaltungenVergangenheit.getSelectedItem().toString());
                        einstellungen.update("SichtbarkeitVeranstaltungZukunft", EinstellungAO.this.sichtbarkeitVonVeranstaltungenZukunft.getSelectedItem().toString());
                        einstellungen.update("JoomlaLink", EinstellungAO.this.joomlaLink.getText());
                        einstellungen.update("joomlaEinsatzkomponenteSecretKey", EinstellungAO.this.joomlaEinsatzkomponenteSecretKey.getText());
                        einstellungen.update("VerdienstausfallBerichtArt", EinstellungAO.this.verdienstausfallArt.getSelectedItem().toString());
                        einstellungen.update("MängelBerichtArt", EinstellungAO.this.maengelmeldungArt.getSelectedItem().toString());
                        einstellungen.update("globaleEMailEinheitsführung", EinstellungAO.this.globaleEMailEinheitsführung.getText());
                        einstellungen.update("globaleEMailGerätewarte", EinstellungAO.this.globaleEMailGerätewarte.getText());
                        einstellungen.update("schutzziel1", EinstellungAO.this.schutzziel1.getText());
                        einstellungen.update("schutzziel2", EinstellungAO.this.schutzziel2.getText());
                        einstellungen.update("zeilenhöheDruck", EinstellungAO.this.zeilenhöheDruck.getText());
                        einstellungen.update("zeilenhöheAnsicht", EinstellungAO.this.zeilenhöheAnsicht.getText());
                        einstellungen.update("unwetterwarnungPop3", EinstellungAO.this.unwetterwarnungPop3.getText());
                        einstellungen.update("unwetterwarnungEMail", EinstellungAO.this.unwetterwarnungEMail.getText());
                        einstellungen.update("unwetterwarnungPopPort", EinstellungAO.this.unwetterwarnungPopPort.getText());
                        einstellungen.update("unwetterwarnungPasswort", hash.createHashCode(EinstellungAO.this.unwetterwarnungPasswort.getText()));
                        einstellungen.update("vorwarnungAblaufDienstausweis", EinstellungAO.this.vorwarnungAblaufDienstausweis.getText());
                        einstellungen.update("darstellungLehrgängeMitgliederverwaltung", EinstellungAO.this.darstellungLehrgängeMitgliederverwaltung.getSelectedItem().toString());
                        einstellungen.update("vorwarnungAblaufFahrberechtigung", EinstellungAO.this.vorwarnungAblaufFahrberechtigung.getText());
                        einstellungen.update("schfiftgrößeAnwesenheitsliste", EinstellungAO.this.schfiftgrößeAnwesenheitsliste.getSelectedItem().toString());
                        einstellungen.update("bestaetignungFreistellungEinsatzArt", EinstellungAO.this.bestaetignungFreistellungEinsatzArt.getSelectedItem().toString());
                        einstellungen.update("bestaetignungFreistellungEinsatz", EinstellungAO.this.bestaetignungFreistellungEinsatz.getText());
                        einstellungen.update("globaleEMailG25", EinstellungAO.this.globaleEMailG25.getText());
                        einstellungen.update("globaleEMailG26", EinstellungAO.this.globaleEMailG26.getText());
                        einstellungen.update("globaleEMailFahrberechtigung", EinstellungAO.this.globaleEMailFahrberechtigung.getText());
                        einstellungen.update("globaleEMailDienstausweis", EinstellungAO.this.globaleEMailDienstausweis.getText());
                        einstellungen.update("LookAndFeel", EinstellungAO.this.LookAndFeel.getSelectedItem().toString());
                        einstellungen.update("TerminDisplay_AnzeigeAnazahlVeranstaltungen", EinstellungAO.this.TerminDisplay_AnzeigeAnazahlVeranstaltungen.getSelectedItem().toString());
                        einstellungen.update("TerminDisplay_AnzeigeAnazahlVeranstaltungListe", EinstellungAO.this.TerminDisplay_AnzeigeAnazahlVeranstaltungListe.getSelectedItem().toString());
                        einstellungen.update("TerminDisplay_AnzeigeDauerVeranstaltungen", EinstellungAO.this.TerminDisplay_AnzeigeDauerVeranstaltungen.getText());
                        einstellungen.update("TerminDisplay_AnzeigeDauerUhr", EinstellungAO.this.TerminDisplay_AnzeigeDauerUhr.getText());
                        einstellungen.update("TerminDisplay_HintergrundBild", EinstellungAO.this.TerminDisplay_HintergrundBild.getText());
                        einstellungen.update("facebookAccessToken", EinstellungAO.this.facebookAccessToken.getText());
                        einstellungen.update("facebookAppID", EinstellungAO.this.facebookAppID.getText());
                        einstellungen.update("facebookAppGeheimCode", EinstellungAO.this.facebookAppGeheimCode.getText());
                        einstellungen.update("google_api_code", EinstellungAO.this.google_api_code.getText());
                        einstellungen.update("default_location", EinstellungAO.this.default_location.getText());
                        einstellungen.update("externeKartenDB_Typ", EinstellungAO.this.externeKartenDB_Typ.getSelectedItem().toString());
                        einstellungen.update("externeKartenDatenbankPort", EinstellungAO.this.externeKartenDatenbankPort.getText());
                        einstellungen.update("externeKartenDatenbankIP", EinstellungAO.this.externeKartenDatenbankIP.getText());
                        einstellungen.update("externeKartenDatenbankName", EinstellungAO.this.externeKartenDatenbankName.getText());
                        einstellungen.update("externeKartenDatenbankUser", hash.createHashCode(EinstellungAO.this.externeKartenDatenbankUser.getText()));
                        einstellungen.update("externeKartenDatenbankPasswort", hash.createHashCode(EinstellungAO.this.externeKartenDatenbankPasswort.getText()));
                        einstellungen.update("externeKartenSSHUser", hash.createHashCode(EinstellungAO.this.externeKartenSSHUser.getText()));
                        einstellungen.update("externeKartenSSHPasswort", hash.createHashCode(EinstellungAO.this.externeKartenSSHPasswort.getText()));
                        einstellungen.update("externeKartenSSHServer", EinstellungAO.this.externeKartenSSHServer.getText());
                        einstellungen.update("externeKartenSSHServerPort", EinstellungAO.this.externeKartenSSHServerPort.getText());
                        einstellungen.update("externeKartenSSHTunnel", EinstellungAO.this.externeKartenSSHTunnel.getText());
                        if(EinstellungAO.this.externeKartenDatenbankLokalesBackup.isSelected()) {
                           einstellungen.update("externeKartenDatenbankLokalesBackup", "1");
                        } else {
                           einstellungen.update("externeKartenDatenbankLokalesBackup", "0");
                        }

                        if(EinstellungAO.this.externeDatenbankFürKartendaten.isSelected()) {
                           einstellungen.update("externeDatenbankFürKartendaten", "1");
                        } else {
                           einstellungen.update("externeDatenbankFürKartendaten", "0");
                        }

                        if(EinstellungAO.this.automatischeFrageNachAtemschutzEinsatz.isSelected()) {
                           einstellungen.update("automatischeFrageNachAtemschutzEinsatz", "1");
                        } else {
                           einstellungen.update("automatischeFrageNachAtemschutzEinsatz", "0");
                        }

                        if(EinstellungAO.this.mitgliederGesundheitTab.isSelected()) {
                           einstellungen.update("mitgliederGesundheitTab", "1");
                        } else {
                           einstellungen.update("mitgliederGesundheitTab", "0");
                        }

                        if(EinstellungAO.this.facebookEMail.isSelected()) {
                           einstellungen.update("facebookEMail", "1");
                        } else {
                           einstellungen.update("facebookEMail", "0");
                        }

                        if(EinstellungAO.this.facebookAutoPostEinsatz.isSelected()) {
                           einstellungen.update("facebookAutoPostEinsatz", "1");
                        } else {
                           einstellungen.update("facebookAutoPostEinsatz", "0");
                        }

                        if(EinstellungAO.this.schulungAdminModul.isSelected()) {
                           einstellungen.update("schulungAdminModul", "1");
                        } else {
                           einstellungen.update("schulungAdminModul", "0");
                        }

                        if(EinstellungAO.this.schulungClientModul.isSelected()) {
                           einstellungen.update("schulungClientModul", "1");
                        } else {
                           einstellungen.update("schulungClientModul", "0");
                        }

                        if(EinstellungAO.this.TerminDisplay_AnzeigenLetzenEinsatz.isSelected()) {
                           einstellungen.update("TerminDisplay_AnzeigenLetzenEinsatz", "1");
                        } else {
                           einstellungen.update("TerminDisplay_AnzeigenLetzenEinsatz", "0");
                        }

                        if(EinstellungAO.this.TerminDisplay_HintergrundBildAktivieren.isSelected()) {
                           einstellungen.update("TerminDisplay_HintergrundBildAktivieren", "1");
                        } else {
                           einstellungen.update("TerminDisplay_HintergrundBildAktivieren", "0");
                        }

                        if(EinstellungAO.this.TerminDisplay_LetzterEinsatzOrtAnzeigen.isSelected()) {
                           einstellungen.update("TerminDisplay_LetzterEinsatzOrtAnzeigen", "1");
                        } else {
                           einstellungen.update("TerminDisplay_LetzterEinsatzOrtAnzeigen", "0");
                        }

                        if(EinstellungAO.this.terminVersandtViaEMailFolgeMonat.isSelected()) {
                           einstellungen.update("terminVersandtViaEMailFolgeMonat", "1");
                        } else {
                           einstellungen.update("terminVersandtViaEMailFolgeMonat", "0");
                        }

                        if(EinstellungAO.this.anmeldungSpeichernErlauben.isSelected()) {
                           einstellungen.update("anmeldungSpeichernErlauben", "1");
                        } else {
                           einstellungen.update("anmeldungSpeichernErlauben", "0");
                        }

                        if(EinstellungAO.this.langesDatumsformatUntersuchungsliste.isSelected()) {
                           einstellungen.update("langesDatumsformatUntersuchungsliste", "1");
                        } else {
                           einstellungen.update("langesDatumsformatUntersuchungsliste", "0");
                        }

                        if(EinstellungAO.this.feldEintreffenAusblenden.isSelected()) {
                           einstellungen.update("feldEintreffenAusblenden", "1");
                        } else {
                           einstellungen.update("feldEintreffenAusblenden", "0");
                        }

                        if(EinstellungAO.this.feldStadtteilAusblenden.isSelected()) {
                           einstellungen.update("feldStadtteilAusblenden", "1");
                        } else {
                           einstellungen.update("feldStadtteilAusblenden", "0");
                        }

                        if(EinstellungAO.this.EinsatzBerichtEinsatzleiterMitDienstgrad.isSelected()) {
                           einstellungen.update("EinsatzBerichtEinsatzleiterMitDienstgrad", "1");
                        } else {
                           einstellungen.update("EinsatzBerichtEinsatzleiterMitDienstgrad", "0");
                        }

                        if(EinstellungAO.this.EinsatzBerichtFahrzeugbelegungHinzufügen.isSelected()) {
                           einstellungen.update("EinsatzBerichtFahrzeugbelegungHinzufügen", "1");
                        } else {
                           einstellungen.update("EinsatzBerichtFahrzeugbelegungHinzufügen", "0");
                        }

                        if(EinstellungAO.this.EinsatzBerichtAtemschutzpassHinzufügen.isSelected()) {
                           einstellungen.update("EinsatzBerichtAtemschutzpassHinzufügen", "1");
                        } else {
                           einstellungen.update("EinsatzBerichtAtemschutzpassHinzufügen", "0");
                        }

                        if(EinstellungAO.this.globaleEMailG25Aktiviert.isSelected()) {
                           einstellungen.update("globaleEMailG25Aktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailG25Aktiviert", "0");
                        }

                        if(EinstellungAO.this.globaleEMailG26Aktiviert.isSelected()) {
                           einstellungen.update("globaleEMailG26Aktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailG26Aktiviert", "0");
                        }

                        if(EinstellungAO.this.globaleEMailFahrberechtigungAktiviert.isSelected()) {
                           einstellungen.update("globaleEMailFahrberechtigungAktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailFahrberechtigungAktiviert", "0");
                        }

                        if(EinstellungAO.this.globaleEMailDienstausweisAktiviert.isSelected()) {
                           einstellungen.update("globaleEMailDienstausweisAktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailDienstausweisAktiviert", "0");
                        }

                        if(EinstellungAO.this.modulEinsatzgebiet.isSelected()) {
                           einstellungen.update("modulEinsatzgebiet", "1");
                        } else {
                           einstellungen.update("modulEinsatzgebiet", "0");
                        }

                        if(EinstellungAO.this.bestaetignungFreistellungEinsatzAktiv.isSelected()) {
                           einstellungen.update("bestaetignungFreistellungEinsatzAktiv", "1");
                        } else {
                           einstellungen.update("bestaetignungFreistellungEinsatzAktiv", "0");
                        }

                        if(EinstellungAO.this.modulMitgliederVerfügbarkeit.isSelected()) {
                           einstellungen.update("modulMitgliederVerfügbarkeit", "1");
                        } else {
                           einstellungen.update("modulMitgliederVerfügbarkeit", "0");
                        }

                        if(EinstellungAO.this.dienstgradAufAnwesenheitsliste.isSelected()) {
                           einstellungen.update("dienstgradAufAnwesenheitsliste", "1");
                        } else {
                           einstellungen.update("dienstgradAufAnwesenheitsliste", "0");
                        }

                        if(EinstellungAO.this.ablaufFahrberechtigungViaEMail.isSelected()) {
                           einstellungen.update("ablaufFahrberechtigungViaEMail", "1");
                        } else {
                           einstellungen.update("ablaufFahrberechtigungViaEMail", "0");
                        }

                        if(EinstellungAO.this.ablaufFahrberechtigungAnzeigen.isSelected()) {
                           einstellungen.update("ablaufFahrberechtigungAnzeigen", "1");
                        } else {
                           einstellungen.update("ablaufFahrberechtigungAnzeigen", "0");
                        }

                        if(EinstellungAO.this.ablaufDienstausweisAnzeigen.isSelected()) {
                           einstellungen.update("ablaufDienstausweisAnzeigen", "1");
                        } else {
                           einstellungen.update("ablaufDienstausweisAnzeigen", "0");
                        }

                        if(EinstellungAO.this.prüfungDerFahrerlaubnis.isSelected()) {
                           einstellungen.update("prüfungDerFahrerlaubnis", "1");
                        } else {
                           einstellungen.update("prüfungDerFahrerlaubnis", "0");
                        }

                        if(EinstellungAO.this.ablaufDienstausweisViaEMail.isSelected()) {
                           einstellungen.update("ablaufDienstausweisViaEMail", "1");
                        } else {
                           einstellungen.update("ablaufDienstausweisViaEMail", "0");
                        }

                        if(EinstellungAO.this.unwetterwarnungModulAktiv.isSelected()) {
                           einstellungen.update("unwetterwarnungModulAktiv", "1");
                        } else {
                           einstellungen.update("unwetterwarnungModulAktiv", "0");
                        }

                        if(EinstellungAO.this.unwetterwarnungSSL.isSelected()) {
                           einstellungen.update("unwetterwarnungSSL", "1");
                        } else {
                           einstellungen.update("unwetterwarnungSSL", "0");
                        }

                        if(EinstellungAO.this.fullBackupPath.getText().endsWith("/") | EinstellungAO.this.fullBackupPath.getText().endsWith("\\")) {
                           einstellungen.update("FullBackupPath", Utils.removeBackSlashFromString(EinstellungAO.this.fullBackupPath.getText()));
                        } else {
                           einstellungen.update("FullBackupPath", Utils.removeBackSlashFromString(EinstellungAO.this.fullBackupPath.getText() + "\\"));
                        }

                        einstellungen.update("mitgliedSeitFormat", EinstellungAO.this.mitgliedSeitFormat.getSelectedItem().toString());
                        if(EinstellungAO.this.autoBerichtAktiv.isSelected()) {
                           einstellungen.update("autoBerichtAktiv", "1");
                        } else {
                           einstellungen.update("autoBerichtAktiv", "0");
                        }

                        if(EinstellungAO.this.terminAnzeigen.isSelected()) {
                           einstellungen.update("termineAnzeigen", "1");
                        } else {
                           einstellungen.update("termineAnzeigen", "0");
                        }

                        if(EinstellungAO.this.gebAnzeigen.isSelected()) {
                           einstellungen.update("gebAnzeigen", "1");
                        } else {
                           einstellungen.update("gebAnzeigen", "0");
                        }

                        if(EinstellungAO.this.agttrainingAnzeigen.isSelected()) {
                           einstellungen.update("agtTrainingAnzeigen", "1");
                        } else {
                           einstellungen.update("agtTrainingAnzeigen", "0");
                        }

                        if(EinstellungAO.this.emailModulAktivieren.isSelected()) {
                           einstellungen.update("emailModul", "1");
                        } else {
                           einstellungen.update("emailModul", "0");
                        }

                        if(EinstellungAO.this.useSSL.isSelected()) {
                           einstellungen.update("useSSL", "1");
                        } else {
                           einstellungen.update("useSSL", "0");
                        }

                        if(EinstellungAO.this.schnittstellenAktivierung.isSelected()) {
                           einstellungen.update("einsatzSchnittstelle", "1");
                        } else {
                           einstellungen.update("einsatzSchnittstelle", "0");
                        }

                        if(EinstellungAO.this.autoDBsave.isSelected()) {
                           einstellungen.update("autoDBsave", "1");
                        } else {
                           einstellungen.update("autoDBsave", "0");
                        }

                        if(EinstellungAO.this.untersuchungViaEMail.isSelected()) {
                           einstellungen.update("untersuchungViaEMail", "1");
                        } else {
                           einstellungen.update("untersuchungViaEMail", "0");
                        }

                        if(EinstellungAO.this.untersuchungViaEMailChefBCC.isSelected()) {
                           einstellungen.update("untersuchungViaEMailChefBCC", "1");
                        } else {
                           einstellungen.update("untersuchungViaEMailChefBCC", "0");
                        }

                        if(EinstellungAO.this.terminVersandtViaEMail.isSelected()) {
                           einstellungen.update("terminVersandtViaEMail", "1");
                        } else {
                           einstellungen.update("terminVersandtViaEMail", "0");
                        }

                        if(EinstellungAO.this.ablaufLKWFührerscheinViaEMail.isSelected()) {
                           einstellungen.update("ablaufLKWFührerscheinViaEMail", "1");
                        } else {
                           einstellungen.update("ablaufLKWFührerscheinViaEMail", "0");
                        }

                        if(EinstellungAO.this.ablaufLKWFuehrerscheinAnzeigen.isSelected()) {
                           einstellungen.update("ablaufLKWAnzeigen", "1");
                        } else {
                           einstellungen.update("ablaufLKWAnzeigen", "0");
                        }

                        if(EinstellungAO.this.automatischesProgrammUpdate.isSelected()) {
                           einstellungen.update("automatischesUpdate", "1");
                        } else {
                           einstellungen.update("automatischesUpdate", "0");
                        }

                        if(EinstellungAO.this.einsatzleiterBFAnzeigen.isSelected()) {
                           einstellungen.update("einsatzleiterBF", "1");
                        } else {
                           einstellungen.update("einsatzleiterBF", "0");
                        }

                        if(EinstellungAO.this.bswHitliste.isSelected()) {
                           einstellungen.update("bswHitliste", "1");
                        } else {
                           einstellungen.update("bswHitliste", "0");
                        }

                        if(EinstellungAO.this.abrechnungModuleAnzeigen.isSelected()) {
                           einstellungen.update("abrechnungModul", "1");
                        } else {
                           einstellungen.update("abrechnungModul", "0");
                        }

                        if(EinstellungAO.this.geraetepruefungenViaEMail.isSelected()) {
                           einstellungen.update("geraetepruefungViaEMail", "1");
                        } else {
                           einstellungen.update("geraetepruefungViaEMail", "0");
                        }

                        if(EinstellungAO.this.offeneMängelAnzeigen.isSelected()) {
                           einstellungen.update("offeneMaengelAnzeigen", "1");
                        } else {
                           einstellungen.update("offeneMaengelAnzeigen", "0");
                        }

                        if(EinstellungAO.this.fahrzeugUntersuchungViaEMail.isSelected()) {
                           einstellungen.update("fahrzeugUntersuchungViaEMail", "1");
                        } else {
                           einstellungen.update("fahrzeugUntersuchungViaEMail", "0");
                        }

                        if(EinstellungAO.this.geburtstageNurHeute.isSelected()) {
                           einstellungen.update("gebAnzeigeModus", "1");
                        } else if(EinstellungAO.this.geburtstageNurGanzerMonat.isSelected()) {
                           einstellungen.update("gebAnzeigeModus", "2");
                        }

                        if(EinstellungAO.this.druckAnwesenheitsListeMode1.isSelected()) {
                           einstellungen.update("druckAnwesenheitsListeMode", "1");
                        } else if(EinstellungAO.this.druckAnwesenheitsListeMode2.isSelected()) {
                           einstellungen.update("druckAnwesenheitsListeMode", "2");
                        }

                        if(EinstellungAO.this.mängelmeldungViaEMailVersenden.isSelected()) {
                           einstellungen.update("mängelmeldungViaEMailVersenden", "1");
                        } else {
                           einstellungen.update("mängelmeldungViaEMailVersenden", "0");
                        }

                        if(EinstellungAO.this.schichtModul.isSelected()) {
                           einstellungen.update("Schichtplaner", "1");
                        } else {
                           einstellungen.update("Schichtplaner", "0");
                        }

                        if(EinstellungAO.this.urlaubModul.isSelected()) {
                           einstellungen.update("Urlaubsplaner", "1");
                        } else {
                           einstellungen.update("Urlaubsplaner", "0");
                        }

                        if(EinstellungAO.this.fahrtenbuchModul.isSelected()) {
                           einstellungen.update("Fahrtenbuch", "1");
                        } else {
                           einstellungen.update("Fahrtenbuch", "0");
                        }

                        if(EinstellungAO.this.modulVeranstaltung.isSelected()) {
                           einstellungen.update("modulVeranstaltung", "1");
                        } else {
                           einstellungen.update("modulVeranstaltung", "0");
                        }

                        if(EinstellungAO.this.modulAusbildungsplan.isSelected()) {
                           einstellungen.update("modulAusbildungsplan", "1");
                        } else {
                           einstellungen.update("modulAusbildungsplan", "0");
                        }

                        if(EinstellungAO.this.modulFahrzeugeinteilung.isSelected()) {
                           einstellungen.update("modulFahrzeugeinteilung", "1");
                        } else {
                           einstellungen.update("modulFahrzeugeinteilung", "0");
                        }

                        if(EinstellungAO.this.terminVersandtViaEMailConfig.getSelectedItem().toString().equals("Immer senden")) {
                           einstellungen.update("terminVersandtViaEMailConfig", "1");
                        } else if(EinstellungAO.this.terminVersandtViaEMailConfig.getSelectedItem().toString().equals("Nur einmal im Monat")) {
                           einstellungen.update("terminVersandtViaEMailConfig", "2");
                        }

                        if(EinstellungAO.this.joomlaVeranstaltung.isSelected()) {
                           einstellungen.update("JoomlaVeranstaltungSenden", "1");
                        } else {
                           einstellungen.update("JoomlaVeranstaltungSenden", "0");
                        }

                        if(EinstellungAO.this.joomlaAusbildungsplan.isSelected()) {
                           einstellungen.update("JoomlaAusbildungsplanSenden", "1");
                        } else {
                           einstellungen.update("JoomlaAusbildungsplanSenden", "0");
                        }

                        if(EinstellungAO.this.alwaysOnTop.isSelected()) {
                           einstellungen.update("AlwaysOnTop", "1");
                        } else {
                           einstellungen.update("AlwaysOnTop", "0");
                        }

                        if(EinstellungAO.this.hochzeitFeldFuerMitglieder.isSelected()) {
                           einstellungen.update("hochzeitFeldFuerMitglieder", "1");
                        } else {
                           einstellungen.update("hochzeitFeldFuerMitglieder", "0");
                        }

                        if(EinstellungAO.this.mitgliedSeitPflichtEintrag.isSelected()) {
                           einstellungen.update("mitgliedSeitPflichtEintrag", "1");
                        } else {
                           einstellungen.update("mitgliedSeitPflichtEintrag", "0");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponente.isSelected()) {
                           einstellungen.update("JoomlaEinsatzkomponente", "1");
                           if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente")).equals("0")) {
                              Joomla.erstelleOrganisation((new TabelleOrganisationen()).getData(1));
                           }
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponente", "0");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponente_Visible.isSelected()) {
                           einstellungen.update("JoomlaEinsatzkomponenteVisible", "1");
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponenteVisible", "0");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponenteEMail.isSelected()) {
                           einstellungen.update("JoomlaEinsatzkomponenteEMail", "1");
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponenteEMail", "0");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn1.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn1", "0");
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn1", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn1.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn2.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn2", "0");
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn2", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn2.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn3.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn3", "0");
                        } else {
                           einstellungen.update("JoomlaEinsatzkomponenteEMailAn3", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.joomlaEinsatzkomponenteEMailAn3.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.facebookEMailAn1.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("facebookEMailAn1", "0");
                        } else {
                           einstellungen.update("facebookEMailAn1", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.facebookEMailAn1.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.facebookEMailAn2.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("facebookEMailAn2", "0");
                        } else {
                           einstellungen.update("facebookEMailAn2", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.facebookEMailAn2.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.facebookEMailAn3.getSelectedItem().toString().equals("<bitte wählen>")) {
                           einstellungen.update("facebookEMailAn3", "0");
                        } else {
                           einstellungen.update("facebookEMailAn3", Integer.toString((new TabelleMitglied()).getIdByGuiString(EinstellungAO.this.facebookEMailAn3.getSelectedItem().toString())));
                        }

                        if(EinstellungAO.this.globaleEMailEinheitsführungAktivieren.isSelected()) {
                           einstellungen.update("globaleEMailEinheitsführungAktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailEinheitsführungAktiviert", "0");
                        }

                        if(EinstellungAO.this.globaleEMailGerätewarteAktivieren.isSelected()) {
                           einstellungen.update("globaleEMailGerätewarteAktiviert", "1");
                        } else {
                           einstellungen.update("globaleEMailGerätewarteAktiviert", "0");
                        }

                        if(EinstellungAO.this.statistik2.isSelected()) {
                           einstellungen.update("Statistik2", "1");
                        } else {
                           einstellungen.update("Statistik2", "0");
                        }

                        if(EinstellungAO.this.einsatznummerPflichteintrag.isSelected()) {
                           einstellungen.update("EinsatznummerIstPflicht", "1");
                        } else {
                           einstellungen.update("EinsatznummerIstPflicht", "0");
                        }

                        if(EinstellungAO.this.einsatzLeiterBFPflichteintrag.isSelected()) {
                           einstellungen.update("EinsatzLeiterBFIstPflicht", "1");
                        } else {
                           einstellungen.update("EinsatzLeiterBFIstPflicht", "0");
                        }

                        if(EinstellungAO.this.headerPrint.isSelected()) {
                           einstellungen.update("headerPrint", "1");
                        } else {
                           einstellungen.update("headerPrint", "0");
                        }

                        if(EinstellungAO.this.footerPrint.isSelected()) {
                           einstellungen.update("footerPrint", "1");
                        } else {
                           einstellungen.update("footerPrint", "0");
                        }

                        if(EinstellungAO.this.LehrgangEintragenAusMitgliederVerwaltungMode.isSelected()) {
                           einstellungen.update("LehrgangEintragenAusMitgliederVerwaltungMode", "1");
                        } else {
                           einstellungen.update("LehrgangEintragenAusMitgliederVerwaltungMode", "0");
                        }

                        if(EinstellungAO.this.getakteteInternetverbindung.isSelected()) {
                           einstellungen.update("getakteteInternetverbindung", "1");
                        } else {
                           einstellungen.update("getakteteInternetverbindung", "0");
                        }

                        if(EinstellungAO.this.onlineStatus.isSelected()) {
                           einstellungen.update("onlineStatus", "1");
                        } else {
                           einstellungen.update("onlineStatus", "0");
                        }

                        if(EinstellungAO.this.JoomlaEinsatzKomponenteNurAlamierungÜbertragen.isSelected()) {
                           einstellungen.update("JoomlaEinsatzKomponenteNurAlamierungÜbertragen", "1");
                        } else {
                           einstellungen.update("JoomlaEinsatzKomponenteNurAlamierungÜbertragen", "0");
                        }

                        if(EinstellungAO.this.JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln.isSelected()) {
                           einstellungen.update("JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln", "1");
                        } else {
                           einstellungen.update("JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln", "0");
                        }

                        if(EinstellungAO.this.organisationen.isSelected()) {
                           einstellungen.update("WeitereOrganisationen", "1");
                        } else {
                           einstellungen.update("WeitereOrganisationen", "0");
                        }

                        if(EinstellungAO.this.fullBackupInZip.isSelected()) {
                           einstellungen.update("FullBackupInZip", "1");
                        } else {
                           einstellungen.update("FullBackupInZip", "0");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponente_Config.getSelectedItem().toString().equals("Hausnummern anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteConfig", "1");
                        } else if(EinstellungAO.this.joomlaEinsatzkomponente_Config.getSelectedItem().toString().equals("Hausnummern ausblenden")) {
                           einstellungen.update("joomlaEinsatzkomponenteConfig", "2");
                        } else if(EinstellungAO.this.joomlaEinsatzkomponente_Config.getSelectedItem().toString().equals("Hausnummern ausblenden + Stadtteil anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteConfig", "3");
                        } else if(EinstellungAO.this.joomlaEinsatzkomponente_Config.getSelectedItem().toString().equals("Hausnummern anzeigen + Stadtteil anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteConfig", "4");
                        }

                        if(EinstellungAO.this.joomlaEinsatzkomponenteStichwort.getSelectedItem().toString().equals("Stichwort anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteStichwort", "1");
                        } else if(EinstellungAO.this.joomlaEinsatzkomponenteStichwort.getSelectedItem().toString().equals("Einsatzkategorie anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteStichwort", "2");
                        } else if(EinstellungAO.this.joomlaEinsatzkomponenteStichwort.getSelectedItem().toString().equals("Stichwort + Einsatzkategorie anzeigen")) {
                           einstellungen.update("joomlaEinsatzkomponenteStichwort", "3");
                        }

                        if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Einsatznummer + Stichwort + Straße / Ort")) {
                           einstellungen.update("verdienstausfallOptionen", "1");
                        } else if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Einsatznummer + Stichwort")) {
                           einstellungen.update("verdienstausfallOptionen", "2");
                        } else if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Einsatznummer")) {
                           einstellungen.update("verdienstausfallOptionen", "3");
                        } else if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Stichwort")) {
                           einstellungen.update("verdienstausfallOptionen", "4");
                        } else if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Freitext")) {
                           einstellungen.update("verdienstausfallOptionen", "5");
                        } else if(EinstellungAO.this.verdienstausfallEinsatzbeschreibungOption.getSelectedItem().equals("Einsatznummer + Einsatzkategorie")) {
                           einstellungen.update("verdienstausfallOptionen", "6");
                        }

                        if(!((String)runApplication.EINSTELLUNGEN.get("bundesland")).equals(EinstellungAO.this.bundesland.getSelectedItem().toString())) {
                           int e = JOptionPane.showConfirmDialog((Component)null, Konstante.BUNDESLAND_VERAENDERT, "Frage", 0);
                           if(e == 0) {
                              CreateDatabase database = new CreateDatabase();
                              logging.logInfo("Dienstgrad wird gelöscht und für Bundesland: " + EinstellungAO.this.bundesland.getSelectedItem().toString() + " angelegt");
                              database.deleteDienstgrad();
                              Mandant mandant = new Mandant();
                              mandant.setId(Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID")));
                              mandant.setName((new TabelleMandant()).getMandantName(Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID"))));
                              (new UpdateDatenbank()).executeSql(database.createDienstgradItems(EinstellungAO.this.bundesland.getSelectedItem().toString(), mandant));
                              einstellungen.update("bundesland", EinstellungAO.this.bundesland.getSelectedItem().toString());
                           }
                        }

                        runApplication.EINSTELLUNGEN = einstellungen.getAllEinstellungen();
                        runApplication.veranstaltungsAnzeigeVergangenheit = TimeCalculation.visablePastDateItemsInList();
                        runApplication.veranstaltungsAnzeigeZukunft = TimeCalculation.visableFutureDateItemsInList();
                        MyEvent.setEvent("0x0030");
                        logging.logInfo("Speichern der Einstellungen erfogreich");
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     } catch (SQLException var5) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace(var5);
                     }

                  }
               };
               threadSpeichern.start();
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
}
