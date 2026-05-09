/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jtattoo.plaf.acryl.AcrylLookAndFeel
 *  com.jtattoo.plaf.aero.AeroLookAndFeel
 *  com.jtattoo.plaf.aluminium.AluminiumLookAndFeel
 *  com.jtattoo.plaf.hifi.HiFiLookAndFeel
 *  com.jtattoo.plaf.luna.LunaLookAndFeel
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.MyProperties
 *  utilities.SbcUtils
 *  utilities.hash
 */
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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.statistik.TabelleStatistikMitglieder;
import go.Clients;
import go.StatistikMitglieder;
import go.email.DataForMail;
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
import logging.logging;
import run.images;
import run.update.Update;
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
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class runApplication {
    private static JFileChooser chooser;
    public static Dimension bildschirmgr\u00f6\u00dfe;
    public static int widthStatistikGUI;
    public static int higthStatistikGUI;
    public static int verarbeitungL\u00e4uft;
    public static int infoServiceL\u00e4uft;
    public static int ftpUploadL\u00e4uft;
    public static int dbUploadL\u00e4uft;
    public static int joomlaUploadL\u00e4uft;
    public static int ftpDownloadL\u00e4uft;
    public static int dbDownloadL\u00e4uft;
    public static String SCHLUESSEL;
    public static String clientID;
    public static HashMap<String, String> EINSTELLUNGEN;
    public static HashMap<String, String> EINSTELLUNGEN_GESPEICHERT;
    public static HashMap<String, String> PROPERTIES;
    public static String veranstaltungsAnzeigeZukunft;
    public static String veranstaltungsAnzeigeVergangenheit;
    public static DataForMail mailData;
    public static int BF;
    public static String mandantName;
    public static int ONLINE;
    public static int instanceofAusbildungsplanISRunning;
    public static int JavaWebStart;
    public static int widthGUIOffset;
    public static int hightGUIOffset;
    public static int unwetterwarnungStatus;
    public static int lastUnwetterwarnungID;
    public static String unwetterwarnungDatumBis;
    public static String unwetterwarnungUhrzeitBis;
    public static ImageIcon bannerHauptprogramm;
    public static ImageIcon dummyImage;
    public static ImageIcon icon;
    public static String letzterVeranstaltungsname;
    public static String loginName;
    public static String arbeitsverzeichnis;

    static {
        bildschirmgr\u00f6\u00dfe = Toolkit.getDefaultToolkit().getScreenSize();
        widthStatistikGUI = 1200;
        higthStatistikGUI = 830;
        verarbeitungL\u00e4uft = 0;
        infoServiceL\u00e4uft = 0;
        ftpUploadL\u00e4uft = 0;
        dbUploadL\u00e4uft = 0;
        joomlaUploadL\u00e4uft = 0;
        ftpDownloadL\u00e4uft = 0;
        dbDownloadL\u00e4uft = 0;
        SCHLUESSEL = "iuret623iuzred54kjn4873zrjsdhvrigt32fsdtf7sdtf76sdr54esa43wsatrvdjkhsdbgf8sdtg";
        clientID = null;
        mailData = new DataForMail();
        BF = 0;
        mandantName = null;
        ONLINE = -1;
        instanceofAusbildungsplanISRunning = 0;
        JavaWebStart = 0;
        widthGUIOffset = 0;
        hightGUIOffset = 0;
        unwetterwarnungStatus = 0;
        lastUnwetterwarnungID = -1;
        unwetterwarnungDatumBis = null;
        unwetterwarnungUhrzeitBis = null;
        bannerHauptprogramm = null;
        dummyImage = null;
        icon = null;
        letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
        loginName = "public";
        arbeitsverzeichnis = "";
    }

    public static void prepareStart(String[] args, String workingDirectory) throws Exception {
        arbeitsverzeichnis = workingDirectory;
        logging.logInfo((Object)("Setze Arbeitsverzeichnis: " + arbeitsverzeichnis));
        runApplication.checkRuntimeMethod();
        new images().loadImagesFromJAR();
        MyProperties programmProperties = new MyProperties(String.valueOf(arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
        if (!programmProperties.sourceFileExists()) {
            logging.logInfo((Object)"Konfiguration ist nicht vorhanden...Beginne Grundeinstellung...");
            new File("c:/ProgramData/FeuerwehrManagemantSystem").mkdirs();
            new File("c:/ProgramData/FeuerwehrManagemantSystem/properties").mkdirs();
            new File("c:/ProgramData/FeuerwehrManagemantSystem/exe").mkdirs();
            new File("c:/ProgramData/FeuerwehrManagemantSystem/data").mkdirs();
            new File("c:/ProgramData/FeuerwehrManagemantSystem/useHere").createNewFile();
            arbeitsverzeichnis = "c:/ProgramData/FeuerwehrManagemantSystem/";
            logging.logInfo((Object)"Setze neues Arbeitsverzeichnis und lege los...");
            Steuerung.setStatus(Status.NUTZUNGSHINWEISE);
            Steuerung.steuerung();
        } else {
            Utils.checkProperties(programmProperties);
            StartBildschirmAO.startDialogText.setText("Einstellungen werden geladen...");
            PROPERTIES = runApplication.lesePropertieDatei(programmProperties);
            EINSTELLUNGEN = new TabelleEinstellungen().getAllEinstellungen();
            runApplication.initLookAndFeel();
            try {
                EINSTELLUNGEN_GESPEICHERT = new TabelleEinstellungen_gespeichert().getAllEinstellungen();
            }
            catch (SQLException sQLException) {
                // empty catch block
            }
            runApplication.checkVersion();
            new TabelleDateisystem().mysqlEinstellungenSetzen();
            Update.initUpdate();
            try {
                if (Integer.parseInt(EINSTELLUNGEN.get("automatischesUpdate")) == 1) {
                    logging.logInfo((Object)"Updatedownload startet...");
                    runApplication.checkForUpdate();
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            runApplication.checkForNachricht();
            if (PROPERTIES.get("FTPUploadActiv").equals("true")) {
                FTPFileTransferservice.downloadService();
            } else if (PROPERTIES.get("BlobActiv").equals("true")) {
                DatabaseFileTransferService.downloadService();
            }
            BerechtigunsManager.ber = new TabelleBerechtigunggruppe().getAll(0, 1);
            BerechtigunsManager.ber2 = new TabelleBerechtigunggruppe().getAll(0, 2);
            logging.logInfo((Object)"Setzte GUI Berechtigungen");
            if (!runApplication.checkeLizenz()) {
                JOptionPane.showMessageDialog(null, Konstante.TESTVERSION_ABGELAUFEN, "Warnung", 2);
                Steuerung.setStatus(Status.LIZENZ_KEY);
                Steuerung.steuerung();
            } else {
                runApplication.executeStart(args);
            }
        }
    }

    public static void executeStart(String[] args) throws UnknownHostException, SQLException, InterruptedException {
        StartBildschirmAO.startDialogText.setText("Benutzeroberfl\u00e4che wird geladen...");
        logging.logInfo((Object)"Konfiguration vorhanden... Starte FeuerwehrManagementSystem Programm");
        clientID = PROPERTIES.get("ClientID");
        new TabelleClients().updateOnline(1);
        Joomla.nutzungFMS("Start");
        veranstaltungsAnzeigeVergangenheit = TimeCalculation.visablePastDateItemsInList();
        veranstaltungsAnzeigeZukunft = TimeCalculation.visableFutureDateItemsInList();
        logging.logInfo((Object)("Anzahl \u00dcbergabeparameter: " + args.length));
        if (args.length != 0) {
            runApplication.checkClientPermission();
            if (args[0].equals("AlarmInfo")) {
                logging.logInfo((Object)"Starte Mit \u00dcbergabeparameter --> AlarmInfo");
                MyEvent.setEvent((String)"0x0100");
                Steuerung.setStatus(Status.KARTE);
                Steuerung.steuerung();
            } else if (args[0].equals("EMailService")) {
                logging.logInfo((Object)"Starte Mit \u00dcbergabeparameter --> EMailService");
                runApplication.startEMailServericeOnly();
            } else if (args[0].equals("TerminDisplay")) {
                logging.logInfo((Object)"Starte Mit \u00dcbergabeparameter --> TerminDisplay");
                Steuerung.setStatus(Status.TERMIN_DISPLAY);
                Steuerung.steuerung();
            } else if (args[0].equals("ANWESENHEITSLISTE_DRUCKEN")) {
                logging.logInfo((Object)"Starte Mit \u00dcbergabeparameter --> ANWESENHEITSLISTE_DRUCKEN");
                MyEvent.setEvent((String)"0x0360");
                Steuerung.setStatus(Status.ANWESENHEIT_LISTE);
                Steuerung.steuerung();
            }
        } else {
            TabelleJahr tabJahr = new TabelleJahr();
            try {
                if (tabJahr.getJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"))) == 0) {
                    logging.logInfo((Object)"Das Aktuelle Jahr ist noch nicht vorhanden und  wird angelegt");
                    String neuesJahr = SbcUtils.timeStamp((String)"yyyy");
                    runApplication.ErstelleEinNeuesJahr(tabJahr, neuesJahr);
                } else if (SbcUtils.timeStamp((String)"ddMM").equals("3012") | SbcUtils.timeStamp((String)"ddMM").equals("3112") && tabJahr.getJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1) == 0) {
                    logging.logInfo((Object)"Wir haben den 3012 od. 3112, n\u00e4chstes Jahr wird anglegt...");
                    String neuesJahr = Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1);
                    runApplication.ErstelleEinNeuesJahr(tabJahr, neuesJahr);
                } else {
                    logging.logInfo((Object)"TabelleJahr ist auf dem aktuellen Stand!");
                }
                logbuchEingabe.NeuerEintag("Programm wird gestartet...");
                BF = new TabelleMandant().getBFStatus();
                logging.logInfo((Object)("Initialisiere Hauptprogramm mit BF == " + BF));
                mandantName = new TabelleMandant().getMandantName(Integer.parseInt(PROPERTIES.get("MandantID")));
                logging.logInfo((Object)("MandantName == " + mandantName));
                logging.logInfo((Object)"Setzte Systemwarnung zur\u00fcck...");
                SystemWarnungService.deleteAllSystemWarnung();
                Steuerung.setStatus(Status.HAUPTPROGRAMM);
                Steuerung.steuerung();
            }
            catch (NumberFormatException | SQLException e1) {
                logging.logPrintStackTrace((Exception)e1);
            }
        }
    }

    private static void checkRuntimeMethod() {
        logging.logInfo((Object)"checkRuntimeMethod()");
        if (!new File(String.valueOf(System.getProperty("user.dir")) + "/install").exists() && !new File(String.valueOf(System.getProperty("user.dir")) + "/images").exists()) {
            logging.logInfo((Object)"Programm l\u00e4uft als: JavaWebStart");
            JavaWebStart = 1;
        } else {
            logging.logInfo((Object)"Programm l\u00e4uft als: StandaloneAnwendung");
        }
    }

    private static void ErstelleEinNeuesJahr(TabelleJahr tabJahr, String neuesJahr) throws SQLException {
        tabJahr.insert(Integer.parseInt(neuesJahr));
        logging.logInfo((Object)("data Ordner wird um das Jahr " + neuesJahr + " erweitert..."));
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr, "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Berichte", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Brief", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Einsatzberichte", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Fahrzeugeinteilung", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Temp", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Verdienstausfall", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Beteiligung_uebersicht", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Mangel", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Lehrgangsmeldungen", "SYSTEM");
        Utils.ordnerErstellen(String.valueOf(arbeitsverzeichnis) + "data/" + neuesJahr + "/Schichten", "SYSTEM");
        runApplication.createMitgliederStatistik(Integer.parseInt(neuesJahr));
    }

    private static void checkClientPermission() throws SQLException, UnknownHostException {
        TabelleClients tabClienets = new TabelleClients();
        Clients clients = new Clients();
        if (tabClienets.getCountClientID(clientID) == 0) {
            logging.logInfo((Object)"F\u00fcrge neue Client ID Ohne Berechtigung hinzu...");
            clients.setZugelassen(0);
            clients.setId(tabClienets.getNextNummer());
            clients.setAlias(InetAddress.getLocalHost().toString());
            clients.setClientID(clientID);
            clients.setTyp("FMS");
            clients.setOnline(0);
            tabClienets.insert(clients);
            JOptionPane.showMessageDialog(null, Konstante.KEINE_BERECHTIGUNG_AUF_MANDANTID + "ClientID = " + clientID + "\nMandantID = " + PROPERTIES.get("MandantID") + " - " + new TabelleMandant().getMandantName(Integer.parseInt(PROPERTIES.get("MandantID"))), "Warnung", 2);
            System.exit(0);
        } else if (tabClienets.getZugelassenStatus(clientID) == 0) {
            logging.logWarning((Object)("ClientID: " + clientID + " hat keine Berechtigung auf dieser MandantID " + PROPERTIES.get("MandantID")));
            JOptionPane.showMessageDialog(null, Konstante.KEINE_BERECHTIGUNG_AUF_MANDANTID + "ClientID = " + clientID + "\nMandantID = " + PROPERTIES.get("MandantID") + " - " + new TabelleMandant().getMandantName(Integer.parseInt(PROPERTIES.get("MandantID"))), "Warnung", 2);
            System.exit(0);
        } else {
            logging.logInfo((Object)"Client ist ebrechtigt f\u00fcr diese MandantID...");
        }
    }

    public static void createMitgliederStatistik(int jahr) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleStatistikMitglieder tabStatistik = new TabelleStatistikMitglieder();
            StatistikMitglieder statistik = new StatistikMitglieder();
            int countOfMitglieder = tabMitglied.getMitgliederCountGruppe1();
            int countOfMitgliederOhneGeburtstagsInfo = tabMitglied.getMitgliederCountGruppe1OhneGeburtstag();
            if (countOfMitglieder != 0) {
                statistik.setId(tabStatistik.getNextNummer());
                statistik.setJahr(jahr);
                statistik.setAnzahl(countOfMitglieder);
                statistik.setAlter(countOfMitgliederOhneGeburtstagsInfo * jahr - tabMitglied.getSummiertesGebJahr());
                statistik.setErstellung(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabStatistik.insert(statistik);
                logging.logInfo((Object)"MitgliederStatistik wurde mit aktuellen Daten bef\u00fcllt...");
            } else {
                logging.logWarning((Object)"Mitgliedertabelle ist leer, keine erstellung der MitgliederStatistik!");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static HashMap<String, String> lesePropertieDatei(MyProperties programmProperties) {
        programmProperties.loadVars();
        logging.logInfo((Object)"Lade Properties Einstellungen...");
        HashMap<String, String> map = new HashMap<String, String>();
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
        logging.logInfo((Object)"Lesen der Properties Datei erfolgreich...");
        return map;
    }

    private static void startEMailServericeOnly() throws InterruptedException {
        logging.logInfo((Object)"startEMailServericeOnly()");
        StartBildschirmAO.startDialogText.setText("E-Mail Service l\u00e4uft...");
        logging.logInfo((Object)InformationService.checkInformationen());
        StartBildschirmAO.startDialogText.setText("Beenden wird vorbereitet...");
        Thread.sleep(2000L);
        new TabelleClients().updateOnline(0);
        logging.logInfo((Object)"startEMailServericeOnly() --> Beenden!");
        System.exit(0);
    }

    private static void initLookAndFeel() {
        try {
            if (!new File(String.valueOf(arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties").exists()) {
                logging.logInfo((Object)"Lade LookAndFeel: PROPERTIES NICHT VORHANDEN --> Standard");
                return;
            }
            TabelleEinstellungen tabEinstellungen = new TabelleEinstellungen();
            MyProperties properties = new MyProperties(String.valueOf(arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
            properties.loadVars();
            String LookAndFeel2 = tabEinstellungen.getEinstellungenWithMandantID("LookAndFeel", (String)properties.getVar("MandantID"));
            if (LookAndFeel2.equals("JAVA-CLASSIC")) {
                logging.logInfo((Object)"Lade: Java-Classic-Lock-and-Feel");
            } else if (LookAndFeel2.equals("JAVA-MODERN")) {
                UIManager.LookAndFeelInfo[] lookAndFeelInfoArray = UIManager.getInstalledLookAndFeels();
                int n = lookAndFeelInfoArray.length;
                int n2 = 0;
                while (n2 < n) {
                    UIManager.LookAndFeelInfo info = lookAndFeelInfoArray[n2];
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        logging.logInfo((Object)"Lade: Nimbus-Lock-and-Feel");
                        break;
                    }
                    ++n2;
                }
                hightGUIOffset = 40;
            } else if (LookAndFeel2.equals("ACRYL")) {
                AcrylLookAndFeel.setTheme((String)"Black", (String)"INSERT YOUR LICENSE KEY HERE", (String)"FeuerwehrManagementSystem");
                UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                hightGUIOffset = -20;
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.acryl.AcrylLookAndFeel-Lock-and-Feel");
            } else if (LookAndFeel2.equals("DARKBLACK")) {
                HiFiLookAndFeel.setTheme((String)"Default", (String)"INSERT YOUR LICENSE KEY HERE", (String)"FeuerwehrManagementSystem");
                UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                new images().loadImagesDummyBlack();
                hightGUIOffset = -20;
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.hifi.HiFiLookAndFeel-Lock-and-Feel");
            } else if (LookAndFeel2.equals("SMART")) {
                UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.smart.SmartLookAndFeel-Lock-and-Feel");
                hightGUIOffset = -20;
            } else if (LookAndFeel2.equals("AERO.SILVER")) {
                AeroLookAndFeel.setTheme((String)"Default", (String)"INSERT YOUR LICENSE KEY HERE", (String)"FeuerwehrManagementSystem");
                UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                hightGUIOffset = -20;
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.aero.AeroLookAndFeel-Lock-and-Feel");
            } else if (LookAndFeel2.equals("ALUMINIUM.SILVER")) {
                AluminiumLookAndFeel.setTheme((String)"Default", (String)"INSERT YOUR LICENSE KEY HERE", (String)"FeuerwehrManagementSystem");
                UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                hightGUIOffset = -20;
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.aluminium.AluminiumLookAndFeel-Lock-and-Feel");
            } else if (LookAndFeel2.equals("LUNA.BLUE")) {
                LunaLookAndFeel.setTheme((String)"Default", (String)"INSERT YOUR LICENSE KEY HERE", (String)"FeuerwehrManagementSystem");
                UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
                hightGUIOffset = -20;
                logging.logInfo((Object)"Lade:com.jtattoo.plaf.luna.LunaLookAndFeel-Lock-and-Feel");
            }
        }
        catch (Exception e) {
            logging.logError((Object)"Ausgew\u00e4hltes LookAndFeel kann nicht geladen werden...");
            logging.logError((Object)"Lade: Java-Classic-Lock-and-Feel");
        }
    }

    private static boolean checkeLizenz() throws SQLException {
        long days = 2592000000L;
        long value = System.currentTimeMillis() - days;
        TabelleKeyStore keyStore = new TabelleKeyStore();
        long ersterStart = Long.parseLong(hash.decodeHashCode((String)keyStore.get("Nummer1")));
        if (ersterStart == 0L) {
            logging.logInfo((Object)"Lizenzkey ist verf\u00fcgbar und G\u00fcltig");
            return true;
        }
        if (ersterStart <= value) {
            logging.logInfo((Object)"Lizenz ist abgelaufen");
            return false;
        }
        logging.logInfo((Object)("Verleibende Zeit der Testlizenz bis: " + TimeCalculation.millisecondsToDate(ersterStart + days)));
        logging.logInfo((Object)"Testlizenz ist noch g\u00fclitg ... Starte Programm");
        return true;
    }

    public static void checkForUpdate() {
        Thread threadDownload = new Thread(){

            @Override
            public void run() {
                try {
                    DownloadUpdateService.getVersionInfoFromServer();
                    File versionInfoServer = new File(String.valueOf(arbeitsverzeichnis) + "data/versioninfo.txt");
                    BufferedReader inFile = new BufferedReader(new FileReader(versionInfoServer));
                    String zeile = inFile.readLine();
                    logging.logInfo((Object)"Lese Versions Info aus versioninfo.txt (Info erhalten vom FTP-Server)");
                    if (!zeile.startsWith("Version: 3.21")) {
                        logging.logInfo((Object)("Neues Update verf\u00fcgbar (Update auf: " + zeile + ")"));
                        logging.logInfo((Object)"Informiere Benutzer \u00fcber das neue Update");
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.PROGRAMM_UPDATE_VERFUEGBAR, "Frage", 0);
                        inFile.close();
                        versionInfoServer.delete();
                        if (msg == 0) {
                            chooser = new JFileChooser();
                            chooser.setFileSelectionMode(1);
                            chooser.showSaveDialog(null);
                            String outputpath = chooser.getSelectedFile().getPath();
                            logging.logInfo((Object)"Benutzer l\u00e4dt das Update vom FTP-Server");
                            logging.logInfo((Object)("Speicherordner f\u00fcr Update: " + outputpath));
                            DownloadUpdateService.getNewUpdateFormServer(outputpath);
                        } else {
                            logging.logInfo((Object)"Benutzer ignoriert die neue Software Version...");
                        }
                    } else {
                        logging.logInfo((Object)"Auf dem Server gibt es kein neues Update");
                        inFile.close();
                        versionInfoServer.delete();
                    }
                }
                catch (IOException e) {
                    this.stop();
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadDownload.start();
    }

    public static void checkForNachricht() {
        Thread threadDownload = new Thread(){

            @Override
            public void run() {
                try {
                    DownloadUpdateService.getNachricht();
                    File info = new File(String.valueOf(arbeitsverzeichnis) + "data/info.txt");
                    BufferedReader in = new BufferedReader(new FileReader(String.valueOf(arbeitsverzeichnis) + "data/info.txt"));
                    StringBuilder build = new StringBuilder();
                    String zeile = null;
                    logging.logInfo((Object)"Lese Nachricht aus info.txt (Info erhalten vom FTP-Server)");
                    while ((zeile = in.readLine()) != null) {
                        build.append(zeile);
                        build.append("\n");
                    }
                    if (!build.toString().equals("")) {
                        TabelleEinstellungen_gespeichert tabGespeichert = new TabelleEinstellungen_gespeichert();
                        if (!EINSTELLUNGEN_GESPEICHERT.get("ErhalteneInfoMeldung").equals(build.toString())) {
                            ImageIcon icon;
                            if (JavaWebStart == 1) {
                                icon = new images().loadImagesFromJARStartbildschirmIconCloud();
                                icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 70), BildUmrechnenService.bildHoeheVerkleinern(icon, 70), 0));
                            } else {
                                icon = new images().loadImagesFromJARStartbildschirmIcon();
                                icon.setImage(icon.getImage().getScaledInstance(BildUmrechnenService.bildBreiteVerkleinern(icon, 70), BildUmrechnenService.bildHoeheVerkleinern(icon, 70), 0));
                            }
                            JOptionPane.showMessageDialog(null, build.toString(), "Info", 1, icon);
                            logging.logInfo((Object)"Speciher Infomeldung in DB");
                            tabGespeichert.update("ErhalteneInfoMeldung", build.toString());
                            logging.logInfo((Object)"Lese Tabelle EINSTELLUNG_GESPEICHERT...");
                            EINSTELLUNGEN_GESPEICHERT = tabGespeichert.getAllEinstellungen();
                        }
                    }
                    in.close();
                    info.delete();
                }
                catch (IOException | SQLException e) {
                    this.stop();
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadDownload.start();
    }

    private static void checkVersion() throws SQLException {
        logging.logInfo((Object)"Start VersionCheck...");
        String version = new TabelleEinstellungen().getVersion();
        logging.logInfo((Object)version);
        int dbr = Integer.parseInt(version.substring(version.length() - 4, version.length() - 3));
        int pr = Integer.parseInt("Version: 3.21".toString().substring("Version: 3.21".toString().length() - 4, "Version: 3.21".toString().length() - 3));
        int dbv = Integer.parseInt(version.substring(version.length() - 2, version.length()));
        int pv = Integer.parseInt("Version: 3.21".toString().substring("Version: 3.21".toString().length() - 2, "Version: 3.21".toString().length()));
        logging.logInfo((Object)(String.valueOf(dbr) + "." + dbv));
        logging.logInfo((Object)(String.valueOf(pr) + "." + pv));
        if (dbr < pr) {
            logging.logInfo((Object)"Progrmm Release ist gr\u00f6\u00dfer als DB --> Init Update");
            return;
        }
        if (dbv > pv | dbr > pr) {
            logging.logWarning((Object)"DB-Version ist gr\u00f6\u00dfer");
            JOptionPane.showMessageDialog(null, Konstante.SOTWAREVERSION_ZU_ALT + version + Konstante.SOTWAREVERSION_ZU_ALT2, "Fehlermeldung", 0);
            System.exit(0);
        } else {
            logging.logInfo((Object)"Programmversion ist gleich, Programm kann gestartet werden");
        }
    }

    public static void checkOnlieStatus() {
        Thread threadOnlineStatus = new Thread(){

            @Override
            public void run() {
                int durchlauf = 0;
                int warteZeit = 60000;
                if (EINSTELLUNGEN.get("getakteteInternetverbindung").equals("1")) {
                    warteZeit = 3600000;
                }
                logging.logInfo((Object)("Wartezeit auf Onlie Check: " + warteZeit + "ms"));
                URL bildRotURL = this.getClass().getClassLoader().getResource("images/statusRot.jpg");
                ImageIcon statusRot = new ImageIcon(bildRotURL);
                URL bildGr\u00fcnURL = this.getClass().getClassLoader().getResource("images/statusGr\u00fcn.jpg");
                ImageIcon statusGr\u00fcn = new ImageIcon(bildGr\u00fcnURL);
                while (true) {
                    try {
                        if (durchlauf != 0) {
                            Thread.sleep(warteZeit);
                        }
                        URL siteURL = new URL("http://www.feuerwehrmanagementsystem.de");
                        HttpURLConnection connection = (HttpURLConnection)siteURL.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            ONLINE = 1;
                            HauptprogrammAO.onlineStatus.setIcon(statusGr\u00fcn);
                            HauptprogrammAO.onlineStatus.setToolTipText("Online");
                        }
                    }
                    catch (IOException | InterruptedException e) {
                        ONLINE = 0;
                        HauptprogrammAO.onlineStatus.setIcon(statusRot);
                        HauptprogrammAO.onlineStatus.setToolTipText("keine Internetverbindung");
                    }
                    ++durchlauf;
                }
            }
        };
        threadOnlineStatus.start();
    }
}

