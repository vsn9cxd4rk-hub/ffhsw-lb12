package run;

import ao.HauptprogrammAO;
import ao.utils.StartBildschirmAO;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import data.tabellen.TabelleDateisystem;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleClients;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleKeyStore;
import data.tabellen.einstellungen.TabelleMandant;
import data.tabellen.karte.TabelleAnfahrt;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleObjekte;
import data.tabellen.karte.TabelleObjekthydranten;
import data.tabellen.karte.TabelleStrassen;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.statistik.TabelleStatistikMitglieder;
import go.Clients;
import go.StatistikMitglieder;
import go.email.DataForMail;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import logging.logging;
import run.images;
import run.update.Update;
import run.update.UpdateDatenbank;
import service.BerechtigunsManager;
import service.DatabaseFileTransferService;
import service.DownloadUpdateService;
import service.FTPFileTransferservice;
import service.InformationService;
import service.SystemWarnungService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.BildUmrechnenService;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.MyProperties;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.hash;
import utilities.logbuchEingabe;
import utilities.joomla.Joomla;

public class runApplication {

   private static JFileChooser chooser;
   public static Dimension bildschirmgröße = Toolkit.getDefaultToolkit().getScreenSize();
   public static int widthStatistikGUI = 1200;
   public static int higthStatistikGUI = 830;
   public static int verarbeitungLäuft = 0;
   public static int infoServiceLäuft = 0;
   public static int ftpUploadLäuft = 0;
   public static int dbUploadLäuft = 0;
   public static int joomlaUploadLäuft = 0;
   public static int ftpDownloadLäuft = 0;
   public static int dbDownloadLäuft = 0;
   public static String SCHLUESSEL = "iuret623iuzred54kjn4873zrjsdhvrigt32fsdtf7sdtf76sdr54esa43wsatrvdjkhsdbgf8sdtg";
   public static String clientID = null;
   public static HashMap EINSTELLUNGEN;
   public static HashMap EINSTELLUNGEN_GESPEICHERT;
   public static HashMap PROPERTIES;
   public static String veranstaltungsAnzeigeZukunft;
   public static String veranstaltungsAnzeigeVergangenheit;
   public static DataForMail mailData = new DataForMail();
   public static int BF = 0;
   public static String mandantName = null;
   public static int ONLINE = -1;
   public static int instanceofAusbildungsplanISRunning = 0;
   public static int JavaWebStart = 0;
   public static int widthGUIOffset = 0;
   public static int hightGUIOffset = 0;
   public static String mitgliederGruppe;
   public static int mitgliederGruppeID;
   public static int unwetterwarnungStatus = 0;
   public static int lastUnwetterwarnungID = -1;
   public static String unwetterwarnungDatumBis = null;
   public static String unwetterwarnungUhrzeitBis = null;
   public static ImageIcon bannerHauptprogramm = null;
   public static ImageIcon dummyImage = null;
   public static ImageIcon icon = null;
   public static String letzterVeranstaltungsname = "<bitte wählen>";
   public static String loginName = "public";
   public static String arbeitsverzeichnis = "";


   public static void prepareStart(String[] args, String workingDirectory) throws Exception {
      arbeitsverzeichnis = workingDirectory;
      logging.logInfo("Setze Arbeitsverzeichnis: " + arbeitsverzeichnis);
      checkRuntimeMethod();
      (new images()).loadImagesFromJAR();
      MyProperties programmProperties = new MyProperties(arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
      if(!programmProperties.sourceFileExists()) {
         logging.logInfo("Konfiguration ist nicht vorhanden...Beginne Grundeinstellung...");
         (new File("c:/ProgramData/FeuerwehrManagemantSystem")).mkdirs();
         (new File("c:/ProgramData/FeuerwehrManagemantSystem/properties")).mkdirs();
         (new File("c:/ProgramData/FeuerwehrManagemantSystem/exe")).mkdirs();
         (new File("c:/ProgramData/FeuerwehrManagemantSystem/data")).mkdirs();
         (new File("c:/ProgramData/FeuerwehrManagemantSystem/useHere")).createNewFile();
         (new File("c:/ProgramData/FeuerwehrManagemantSystem/properties/logging.properties")).createNewFile();
         arbeitsverzeichnis = "c:/ProgramData/FeuerwehrManagemantSystem/";
         logging.logInfo("Setze neues Arbeitsverzeichnis und lege los...");
         Steuerung.setStatus(Status.NUTZUNGSHINWEISE);
         Steuerung.steuerung();
      } else {
         Utils.checkProperties(programmProperties);
         StartBildschirmAO.startDialogText.setText("Einstellungen werden geladen...");
         PROPERTIES = lesePropertieDatei(programmProperties);
         EINSTELLUNGEN = (new TabelleEinstellungen()).getAllEinstellungen();
         initLookAndFeel();

         try {
            EINSTELLUNGEN_GESPEICHERT = (new TabelleEinstellungen_gespeichert()).getAllEinstellungen();
         } catch (SQLException var5) {
            ;
         }

         checkVersion();
         if(((String)PROPERTIES.get("BlobActiv")).equals("true")) {
            (new TabelleDateisystem()).mysqlEinstellungenSetzen();
         }

         Update.initUpdate();

         try {
            if(Integer.parseInt((String)EINSTELLUNGEN.get("automatischesUpdate")) == 1) {
               logging.logInfo("Updatedownload startet...");
               checkForUpdate();
            }
         } catch (NumberFormatException var4) {
            ;
         }

         checkForNachricht();
         checkForDownloadFiles();
         BerechtigunsManager.ber = (new TabelleBerechtigunggruppe()).getAll(0, 1);
         BerechtigunsManager.ber2 = (new TabelleBerechtigunggruppe()).getAll(0, 2);
         BerechtigunsManager.ber3 = (new TabelleBerechtigunggruppe()).getAll(0, 3);
         logging.logInfo("Setzte GUI Berechtigungen");
         if(!checkeLizenz()) {
            JOptionPane.showMessageDialog((Component)null, Konstante.TESTVERSION_ABGELAUFEN, "Warnung", 2);
            Steuerung.setStatus(Status.LIZENZ_KEY);
            Steuerung.steuerung();
         } else {
            executeStart(args);
         }
      }

   }

   public static void executeStart(String[] args) throws UnknownHostException, SQLException, InterruptedException {
      StartBildschirmAO.startDialogText.setText("Benutzeroberfläche wird geladen...");
      logging.logInfo("Konfiguration vorhanden... Starte FeuerwehrManagementSystem Programm");
      clientID = (String)PROPERTIES.get("ClientID");
      (new TabelleClients()).updateOnline(1);
      Joomla.nutzungFMS("Start");
      BerechtigunsManager.setBevorzugteMitgliederGruppe();
      veranstaltungsAnzeigeVergangenheit = TimeCalculation.visablePastDateItemsInList();
      veranstaltungsAnzeigeZukunft = TimeCalculation.visableFutureDateItemsInList();
      checkClientPermission();
      logging.logInfo("Anzahl Übergabeparameter: " + args.length);
      if(args.length != 0) {
         if(args[0].equals("AlarmInfo")) {
            logging.logInfo("Starte Mit Übergabeparameter --> AlarmInfo");
            MyEvent.setEvent("0x0100");
            Steuerung.setStatus(Status.KARTE);
            Steuerung.steuerung();
         } else if(args[0].equals("EMailService")) {
            logging.logInfo("Starte Mit Übergabeparameter --> EMailService");
            startEMailServericeOnly();
         } else if(args[0].equals("TerminDisplay")) {
            logging.logInfo("Starte Mit Übergabeparameter --> TerminDisplay");
            Steuerung.setStatus(Status.TERMIN_DISPLAY);
            Steuerung.steuerung();
         } else if(args[0].equals("ANWESENHEITSLISTE_DRUCKEN")) {
            logging.logInfo("Starte Mit Übergabeparameter --> ANWESENHEITSLISTE_DRUCKEN");
            MyEvent.setEvent("0x0360");
            Steuerung.setStatus(Status.ANWESENHEIT_LISTE);
            Steuerung.steuerung();
         }
      } else {
         TabelleJahr tabJahr = new TabelleJahr();

         try {
            String e1;
            if(tabJahr.getJahr(Integer.parseInt(SbcUtils.timeStamp("yyyy"))) == 0) {
               logging.logInfo("Das Aktuelle Jahr ist noch nicht vorhanden und  wird angelegt");
               e1 = SbcUtils.timeStamp("yyyy");
               ErstelleEinNeuesJahr(tabJahr, e1);
            } else if(SbcUtils.timeStamp("ddMM").equals("3012") | SbcUtils.timeStamp("ddMM").equals("3112") && tabJahr.getJahr(Integer.parseInt(SbcUtils.timeStamp("yyyy")) + 1) == 0) {
               logging.logInfo("Wir haben den 3012 od. 3112, nächstes Jahr wird anglegt...");
               e1 = Integer.toString(Integer.parseInt(SbcUtils.timeStamp("yyyy")) + 1);
               ErstelleEinNeuesJahr(tabJahr, e1);
            } else {
               logging.logInfo("TabelleJahr ist auf dem aktuellen Stand!");
            }

            logbuchEingabe.NeuerEintag("Programm wird gestartet...");
            BF = (new TabelleMandant()).getBFStatus();
            logging.logInfo("Initialisiere Hauptprogramm mit BF == " + BF);
            mandantName = (new TabelleMandant()).getMandantName(Integer.parseInt((String)PROPERTIES.get("MandantID")));
            logging.logInfo("MandantName == " + mandantName);
            logging.logInfo("Setzte Systemwarnung zurück...");
            SystemWarnungService.deleteAllSystemWarnung();
            externKartenDBLokalBackup();
            Steuerung.setStatus(Status.HAUPTPROGRAMM);
            Steuerung.steuerung();
         } catch (SQLException var3) {
            logging.logPrintStackTrace(var3);
         }
      }

   }

   private static void checkRuntimeMethod() {
      logging.logInfo("checkRuntimeMethod()");
      if(!(new File(System.getProperty("user.dir") + "/install")).exists() && !(new File(System.getProperty("user.dir") + "/images")).exists()) {
         logging.logInfo("Programm läuft als: JavaWebStart");
         JavaWebStart = 1;
      } else {
         logging.logInfo("Programm läuft als: StandaloneAnwendung");
      }

   }

   private static void ErstelleEinNeuesJahr(TabelleJahr tabJahr, String neuesJahr) throws SQLException {
      tabJahr.insert(Integer.parseInt(neuesJahr));
      logging.logInfo("data Ordner wird um das Jahr " + neuesJahr + " erweitert...");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr, "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Berichte", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Brief", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Einsatzberichte", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Fahrzeugeinteilung", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Temp", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Verdienstausfall", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Beteiligung_uebersicht", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Mangel", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Lehrgangsmeldungen", "SYSTEM");
      Utils.ordnerErstellen(arbeitsverzeichnis + "data/" + neuesJahr + "/Schichten", "SYSTEM");
      createMitgliederStatistik(Integer.parseInt(neuesJahr));
   }

   private static void checkClientPermission() throws SQLException, UnknownHostException {
      TabelleClients tabClienets = new TabelleClients();
      Clients clients = new Clients();
      if(tabClienets.getCountClientID(clientID) == 0) {
         logging.logInfo("Fürge neue Client ID Ohne Berechtigung hinzu...");
         clients.setZugelassen(0);
         clients.setId(tabClienets.getNextNummer());
         clients.setAlias(InetAddress.getLocalHost().toString());
         clients.setClientID(clientID);
         clients.setTyp("FMS");
         clients.setOnline(0);
         tabClienets.insert(clients);
         JOptionPane.showMessageDialog((Component)null, Konstante.KEINE_BERECHTIGUNG_AUF_MANDANTID + "ClientID = " + clientID + "\nMandantID = " + (String)PROPERTIES.get("MandantID") + " - " + (new TabelleMandant()).getMandantName(Integer.parseInt((String)PROPERTIES.get("MandantID"))), "Warnung", 2);
         (new TabelleClients()).updateOnline(0);
         logging.logWarning("Dieser Client ist nicht berechtigung - System wird beendet!");
         System.exit(0);
      } else if(tabClienets.getZugelassenStatus(clientID) == 0) {
         logging.logWarning("ClientID: " + clientID + " hat keine Berechtigung auf dieser MandantID " + (String)PROPERTIES.get("MandantID"));
         JOptionPane.showMessageDialog((Component)null, Konstante.KEINE_BERECHTIGUNG_AUF_MANDANTID + "ClientID = " + clientID + "\nMandantID = " + (String)PROPERTIES.get("MandantID") + " - " + (new TabelleMandant()).getMandantName(Integer.parseInt((String)PROPERTIES.get("MandantID"))), "Warnung", 2);
         (new TabelleClients()).updateOnline(0);
         logging.logWarning("Dieser Client ist nicht berechtigung - System wird beendet!");
         System.exit(0);
      } else {
         logging.logInfo("Client ist ebrechtigt für diese MandantID...");
      }

   }

   public static void createMitgliederStatistik(int jahr) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleStatistikMitglieder tabStatistik = new TabelleStatistikMitglieder();
         TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
         StatistikMitglieder statistik = new StatistikMitglieder();
         String[] mitgliederGruppenListe = Utils.listToArray(tabGruppe.getAllGruppen());

         for(int mg = 0; mg < mitgliederGruppenListe.length; ++mg) {
            int mGruppe = tabGruppe.getID(mitgliederGruppenListe[mg]);
            int countOfMitglieder = e.getMitglierderGruppenCount(mGruppe);
            int countOfMitgliederMitGeburtstagsInfo = e.getMitgliederCountMitGeburtstag(mGruppe);
            if(countOfMitglieder != 0) {
               statistik.setId(tabStatistik.getNextNummer());
               statistik.setJahr(jahr);
               statistik.setAnzahlGebTage(countOfMitgliederMitGeburtstagsInfo);
               statistik.setAnzahl(countOfMitglieder);
               statistik.setAlter(countOfMitgliederMitGeburtstagsInfo * jahr - e.getSummiertesGebJahr(mGruppe));
               statistik.setErstellung(SbcUtils.timeStamp("yyyy-MM-dd"));
               statistik.setMitgliederGruppe(mGruppe);
               tabStatistik.insert(statistik);
               logging.logInfo("MitgliederStatistik wurde mit aktuellen Daten befüllt für Mitgliedergruppe == " + mitgliederGruppenListe[mg] + "...");
            } else {
               logging.logWarning("Mitgliedertabelle ist leer, keine erstellung der MitgliederStatistik! (Mitgliedergruppe == " + mitgliederGruppenListe[mg] + ")");
            }
         }
      } catch (SQLException var10) {
         logging.logPrintStackTrace(var10);
      }

   }

   public static HashMap lesePropertieDatei(MyProperties programmProperties) {
      programmProperties.loadVars();
      logging.logInfo("Lade Properties Einstellungen...");
      HashMap map = new HashMap();
      map.put("SSHTunnel", (String)programmProperties.getVar("SSHTunnel"));
      map.put("FTPServer", (String)programmProperties.getVar("FTPServer"));
      map.put("DatenbankIP", (String)programmProperties.getVar("DatenbankIP"));
      map.put("ClientID", (String)programmProperties.getVar("ClientID"));
      map.put("SSHPasswort", (String)programmProperties.getVar("SSHPasswort"));
      map.put("SSHServerPort", (String)programmProperties.getVar("SSHServerPort"));
      map.put("logmax", (String)programmProperties.getVar("logmax"));
      map.put("FTPPort", (String)programmProperties.getVar("FTPPort"));
      map.put("DatenbankName", (String)programmProperties.getVar("DatenbankName"));
      map.put("SSHServer", (String)programmProperties.getVar("SSHServer"));
      map.put("DatenbankPasswort", (String)programmProperties.getVar("DatenbankPasswort"));
      map.put("FTPUser", (String)programmProperties.getVar("FTPUser"));
      map.put("DatenbankPort", (String)programmProperties.getVar("DatenbankPort"));
      map.put("DatenbankUser", (String)programmProperties.getVar("DatenbankUser"));
      map.put("FTPPasswort", (String)programmProperties.getVar("FTPPasswort"));
      map.put("SSHUser", (String)programmProperties.getVar("SSHUser"));
      map.put("DB_TYP", (String)programmProperties.getVar("DB_TYP"));
      map.put("Organisation", (String)programmProperties.getVar("Organisation"));
      map.put("MandantID", (String)programmProperties.getVar("MandantID"));
      map.put("FTPUploadActiv", (String)programmProperties.getVar("FTPUploadActiv"));
      map.put("BlobActiv", (String)programmProperties.getVar("BlobActiv"));
      map.put("DateiDownload247", (String)programmProperties.getVar("DateiDownload247"));
      map.put("FTPServerPfad", (String)programmProperties.getVar("FTPServerPfad"));
      logging.logInfo("Lesen der Properties Datei erfolgreich...");
      return map;
   }

   private static void startEMailServericeOnly() throws InterruptedException {
      logging.logInfo("startEMailServericeOnly()");
      StartBildschirmAO.startDialogText.setText("E-Mail Service läuft...");
      logging.logInfo(InformationService.checkInformationen());
      StartBildschirmAO.startDialogText.setText("E-Mails werden verschickt...");

      while(verarbeitungLäuft == 1) {
         logging.logInfo("Warte auf Senden der E-Mail(s)...");
         Thread.sleep(1000L);
      }

      StartBildschirmAO.startDialogText.setText("Beenden wird vorbereitet...");
      Thread.sleep(2000L);
      (new TabelleClients()).updateOnline(0);
      logging.logInfo("startEMailServericeOnly() --> Beenden!");
      System.exit(0);
   }

   private static void initLookAndFeel() {
      try {
         if(!(new File(arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties")).exists()) {
            logging.logInfo("Lade LookAndFeel: PROPERTIES NICHT VORHANDEN --> Standard");
            return;
         }

         TabelleEinstellungen e = new TabelleEinstellungen();
         MyProperties properties = new MyProperties(arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
         properties.loadVars();
         String LookAndFeel = e.getEinstellungenWithMandantID("LookAndFeel", (String)properties.getVar("MandantID"));
         if(LookAndFeel.equals("JAVA-CLASSIC")) {
            logging.logInfo("Lade: Java-Classic-Lock-and-Feel");
         } else if(LookAndFeel.equals("JAVA-MODERN")) {
            LookAndFeelInfo[] var6;
            int var5 = (var6 = UIManager.getInstalledLookAndFeels()).length;

            for(int var4 = 0; var4 < var5; ++var4) {
               LookAndFeelInfo info = var6[var4];
               if("Nimbus".equals(info.getName())) {
                  UIManager.setLookAndFeel(info.getClassName());
                  logging.logInfo("Lade: Nimbus-Lock-and-Feel");
                  break;
               }
            }

            hightGUIOffset = 40;
         } else if(LookAndFeel.equals("ACRYL")) {
            AcrylLookAndFeel.setTheme("Black", "INSERT YOUR LICENSE KEY HERE", "FeuerwehrManagementSystem");
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            hightGUIOffset = -20;
            logging.logInfo("Lade:com.jtattoo.plaf.acryl.AcrylLookAndFeel-Lock-and-Feel");
         } else if(LookAndFeel.equals("DARKBLACK")) {
            HiFiLookAndFeel.setTheme("Default", "INSERT YOUR LICENSE KEY HERE", "FeuerwehrManagementSystem");
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            (new images()).loadImagesDummyBlack();
            hightGUIOffset = -20;
            logging.logInfo("Lade:com.jtattoo.plaf.hifi.HiFiLookAndFeel-Lock-and-Feel");
         } else if(LookAndFeel.equals("SMART")) {
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
            logging.logInfo("Lade:com.jtattoo.plaf.smart.SmartLookAndFeel-Lock-and-Feel");
            hightGUIOffset = -20;
         } else if(LookAndFeel.equals("AERO.SILVER")) {
            AeroLookAndFeel.setTheme("Default", "INSERT YOUR LICENSE KEY HERE", "FeuerwehrManagementSystem");
            UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
            hightGUIOffset = -20;
            logging.logInfo("Lade:com.jtattoo.plaf.aero.AeroLookAndFeel-Lock-and-Feel");
         } else if(LookAndFeel.equals("ALUMINIUM.SILVER")) {
            AluminiumLookAndFeel.setTheme("Default", "INSERT YOUR LICENSE KEY HERE", "FeuerwehrManagementSystem");
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            hightGUIOffset = -20;
            logging.logInfo("Lade:com.jtattoo.plaf.aluminium.AluminiumLookAndFeel-Lock-and-Feel");
         } else if(LookAndFeel.equals("LUNA.BLUE")) {
            LunaLookAndFeel.setTheme("Default", "INSERT YOUR LICENSE KEY HERE", "FeuerwehrManagementSystem");
            UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
            hightGUIOffset = -20;
            logging.logInfo("Lade:com.jtattoo.plaf.luna.LunaLookAndFeel-Lock-and-Feel");
         }
      } catch (Exception var7) {
         logging.logError("Ausgewähltes LookAndFeel kann nicht geladen werden...");
         logging.logError("Lade: Java-Classic-Lock-and-Feel");
      }

   }

   private static boolean checkeLizenz() throws SQLException {
      long days = 2592000000L;
      long value = System.currentTimeMillis() - days;
      TabelleKeyStore keyStore = new TabelleKeyStore();
      long ersterStart = Long.parseLong(hash.decodeHashCode(keyStore.get("Nummer1")));
      if(ersterStart == 0L) {
         logging.logInfo("Lizenzkey ist verfügbar und Gültig");
         return true;
      } else if(ersterStart <= value) {
         logging.logInfo("Lizenz ist abgelaufen");
         return false;
      } else {
         logging.logInfo("Verleibende Zeit der Testlizenz bis: " + TimeCalculation.millisecondsToDate(ersterStart + days));
         logging.logInfo("Testlizenz ist noch gülitg ... Starte Programm");
         return true;
      }
   }

   public static void checkForUpdate() {
      Thread threadDownload = new Thread() {
         public void run() {
            try {
               DownloadUpdateService.getVersionInfoFromServer();
               File e = new File(runApplication.arbeitsverzeichnis + "data/versioninfo.txt");
               BufferedReader inFile = new BufferedReader(new FileReader(e));
               String zeile = inFile.readLine();
               logging.logInfo("Lese Versions Info aus versioninfo.txt (Info erhalten vom FTP-Server)");
               if(!zeile.startsWith("Version: 4.08")) {
                  logging.logInfo("Neues Update verfügbar (Update auf: " + zeile + ")");
                  logging.logInfo("Informiere Benutzer über das neue Update");
                  int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.PROGRAMM_UPDATE_VERFUEGBAR, "Frage", 0);
                  inFile.close();
                  e.delete();
                  if(msg == 0) {
                     runApplication.chooser = new JFileChooser();
                     runApplication.chooser.setFileSelectionMode(1);
                     runApplication.chooser.showSaveDialog((Component)null);
                     String outputpath = runApplication.chooser.getSelectedFile().getPath();
                     logging.logInfo("Benutzer lädt das Update vom FTP-Server");
                     logging.logInfo("Speicherordner für Update: " + outputpath);
                     DownloadUpdateService.getNewUpdateFormServer(outputpath);
                  } else {
                     logging.logInfo("Benutzer ignoriert die neue Software Version...");
                  }
               } else {
                  logging.logInfo("Auf dem Server gibt es kein neues Update");
                  inFile.close();
                  e.delete();
               }
            } catch (IOException var6) {
               this.stop();
               logging.logPrintStackTrace(var6);
            }

         }
      };
      threadDownload.start();
   }

   public static void checkForNachricht() {
      Thread threadDownload = new Thread() {
         public void run() {
            try {
               DownloadUpdateService.getNachricht();
               File e = new File(runApplication.arbeitsverzeichnis + "data/info.txt");
               BufferedReader in = new BufferedReader(new FileReader(runApplication.arbeitsverzeichnis + "data/info.txt"));
               StringBuilder build = new StringBuilder();
               String zeile = null;
               logging.logInfo("Lese Nachricht aus info.txt (Info erhalten vom FTP-Server)");

               while((zeile = in.readLine()) != null) {
                  build.append(zeile);
                  build.append("\n");
               }

               if(!build.toString().equals("")) {
                  TabelleEinstellungen_gespeichert tabGespeichert = new TabelleEinstellungen_gespeichert();
                  if(!((String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("ErhalteneInfoMeldung")).equals(build.toString())) {
                     ImageIcon icon;
                     if(runApplication.JavaWebStart == 1) {
                        icon = (new images()).loadImagesFromJARStartbildschirmIconCloud();
                        icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 70), BildUmrechnenService.bildHoeheVerkleinern(icon, 70), 0));
                     } else {
                        icon = (new images()).loadImagesFromJARStartbildschirmIcon();
                        icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 70), BildUmrechnenService.bildHoeheVerkleinern(icon, 70), 0));
                     }

                     JOptionPane.showMessageDialog((Component)null, build.toString(), "Info", 1, icon);
                     logging.logInfo("Speciher Infomeldung in DB");
                     tabGespeichert.update("ErhalteneInfoMeldung", build.toString());
                     logging.logInfo("Lese Tabelle EINSTELLUNG_GESPEICHERT...");
                     runApplication.EINSTELLUNGEN_GESPEICHERT = tabGespeichert.getAllEinstellungen();
                  }
               }

               in.close();
               e.delete();
            } catch (SQLException var7) {
               this.stop();
               logging.logPrintStackTrace(var7);
            }

         }
      };
      threadDownload.start();
   }

   private static void checkVersion() throws SQLException {
      logging.logInfo("Start VersionCheck...");
      String version = (new TabelleEinstellungen()).getVersion();
      logging.logInfo(version);
      int dbr = Integer.parseInt(version.substring(version.length() - 4, version.length() - 3));
      int pr = Integer.parseInt("Version: 4.08".toString().substring("Version: 4.08".toString().length() - 4, "Version: 4.08".toString().length() - 3));
      int dbv = Integer.parseInt(version.substring(version.length() - 2, version.length()));
      int pv = Integer.parseInt("Version: 4.08".toString().substring("Version: 4.08".toString().length() - 2, "Version: 4.08".toString().length()));
      logging.logInfo(dbr + "." + dbv);
      logging.logInfo(pr + "." + pv);
      if(dbr < pr) {
         logging.logInfo("Progrmm Release ist größer als DB --> Init Update");
      } else {
         if(dbv > pv | dbr > pr) {
            logging.logWarning("DB-Version ist größer");
            JOptionPane.showMessageDialog((Component)null, Konstante.SOTWAREVERSION_ZU_ALT + version + Konstante.SOTWAREVERSION_ZU_ALT2, "Fehlermeldung", 0);
            System.exit(0);
         } else {
            logging.logInfo("Programmversion ist gleich, Programm kann gestartet werden");
         }

      }
   }

   public static void checkOnlieStatus() {
      Thread threadOnlineStatus = new Thread() {
         public void run() {
            int durchlauf = 0;
            int warteZeit = '\uea60';
            if(((String)runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung")).equals("1")) {
               warteZeit = 3600000;
            }

            logging.logInfo("Wartezeit auf Onlie Check: " + warteZeit + "ms");
            URL bildRotURL = this.getClass().getClassLoader().getResource("images/statusRot.jpg");
            ImageIcon statusRot = new ImageIcon(bildRotURL);
            URL bildGrünURL = this.getClass().getClassLoader().getResource("images/statusGrün.jpg");
            ImageIcon statusGrün = new ImageIcon(bildGrünURL);

            while(true) {
               try {
                  if(durchlauf != 0) {
                     Thread.sleep((long)warteZeit);
                  }

                  URL e = new URL("http://www.feuerwehrmanagementsystem.de");
                  HttpURLConnection connection = (HttpURLConnection)e.openConnection();
                  connection.setRequestMethod("GET");
                  connection.connect();
                  int code = connection.getResponseCode();
                  if(code == 200) {
                     runApplication.ONLINE = 1;
                     HauptprogrammAO.onlineStatus.setIcon(statusGrün);
                     HauptprogrammAO.onlineStatus.setToolTipText("Online");
                  }
               } catch (InterruptedException var10) {
                  runApplication.ONLINE = 0;
                  HauptprogrammAO.onlineStatus.setIcon(statusRot);
                  HauptprogrammAO.onlineStatus.setToolTipText("keine Internetverbindung");
               }

               ++durchlauf;
            }
         }
      };
      threadOnlineStatus.start();
   }

   private static void checkForDownloadFiles() {
      if(((String)PROPERTIES.get("DateiDownload247")).equals("true")) {
         Thread threadBlob247Download;
         if(((String)PROPERTIES.get("FTPUploadActiv")).equals("true")) {
            threadBlob247Download = new Thread() {
               public void run() {
                  while(true) {
                     try {
                        while(true) {
                           if(runApplication.ftpDownloadLäuft == 0) {
                              logging.logInfo("DateiDownload247 - Dateien vom FTP Server werden aktualisiert...");
                              FTPFileTransferservice.downloadService();
                           }

                           Thread.sleep(3600000L);
                        }
                     } catch (InterruptedException var2) {
                        logging.logPrintStackTrace(var2);
                     }
                  }
               }
            };
            threadBlob247Download.start();
         } else if(((String)PROPERTIES.get("BlobActiv")).equals("true")) {
            threadBlob247Download = new Thread() {
               public void run() {
                  while(true) {
                     try {
                        while(true) {
                           if(runApplication.dbDownloadLäuft == 0) {
                              logging.logInfo("DateiDownload247 - Dateien aus der Datenbank (Blob) werden aktualisiert...");
                              DatabaseFileTransferService.downloadService();
                           }

                           Thread.sleep(3600000L);
                        }
                     } catch (InterruptedException var2) {
                        logging.logPrintStackTrace(var2);
                     }
                  }
               }
            };
            threadBlob247Download.start();
         }
      } else if(((String)PROPERTIES.get("FTPUploadActiv")).equals("true")) {
         FTPFileTransferservice.downloadService();
      } else if(((String)PROPERTIES.get("BlobActiv")).equals("true")) {
         DatabaseFileTransferService.downloadService();
      }

   }

   private static void externKartenDBLokalBackup() {
      Thread threadKartenDownload = new Thread() {
         public void run() {
            logging.logInfo("externKartenDBLokalBackup() - Speichere externe Kartendaten Lokal als Rückfallebene...");

            try {
               TabelleStrassen e = new TabelleStrassen();
               TabelleAnfahrt tabAnfahrt = new TabelleAnfahrt();
               TabelleObjekte tabObjekte = new TabelleObjekte();
               TabelleObjekthydranten tabObjektHydranten = new TabelleObjekthydranten();
               TabelleHydranten tabHydranten = new TabelleHydranten();
               UpdateDatenbank updateDatenbank = new UpdateDatenbank();
               String sqlStraßen = e.getDataForStrassenLokalBackup();
               String sqlHydranten = tabHydranten.getDataForHydrantenLokalBackup();
               String sqlObjekte = tabObjekte.getDataForObjekteLokalBackup();
               String sqlObjektHydranten = tabObjektHydranten.getDataForObjekteHydrantenLokalBackup();
               String sqlAnfahrt = tabAnfahrt.getDataForAnfahrtLokalBackup();
               e.deleteLokal();
               tabHydranten.deleteLokal();
               tabObjekte.deleteLokal();
               tabObjektHydranten.deleteLokal();
               tabAnfahrt.deleteLokal();
               logging.logInfo("externKartenDBLokalBackup() - Lokale Daten gelöscht...");
               if(sqlStraßen.endsWith(")")) {
                  updateDatenbank.executeSql(sqlStraßen);
               }

               if(sqlAnfahrt.endsWith(")")) {
                  updateDatenbank.executeSql(sqlAnfahrt);
               }

               if(sqlHydranten.endsWith(")")) {
                  updateDatenbank.executeSql(sqlHydranten);
               }

               if(sqlObjekte.endsWith(")")) {
                  updateDatenbank.executeSql(sqlObjekte);
               }

               if(sqlObjektHydranten.endsWith(")")) {
                  updateDatenbank.executeSql(sqlObjektHydranten);
               }

               logging.logInfo("externKartenDBLokalBackup() - Backup in der Lokalen Datenbank gespeichert...");
            } catch (SQLException var12) {
               logging.logPrintStackTrace(var12);
               System.exit(0);
            }

         }
      };
      if(((String)EINSTELLUNGEN.get("externeDatenbankFürKartendaten")).equals("1") && ((String)EINSTELLUNGEN.get("externeKartenDatenbankLokalesBackup")).equals("1")) {
         threadKartenDownload.start();
      }

   }
}
