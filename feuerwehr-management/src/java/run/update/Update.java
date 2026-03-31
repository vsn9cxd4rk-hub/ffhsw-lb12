package run.update;

import ao.utils.StartBildschirmAO;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleClients;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleKeyStore;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung;
import data.tabellen.karte.TabelleAnfahrt;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import go.Mitgliederlaufbahn;
import go.karte.Anfahrt;
import java.awt.Component;
import java.io.File;
import java.net.InetAddress;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import run.update.UpdateDatenbank;
import utilities.Konstante;
import utilities.MyProperties;
import utilities.RandomGenerator;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.WinRegistry;
import utilities.hash;
import utilities.RandomGenerator.Mode;
import utilities.joomla.Joomla;

public class Update {

   public static void initUpdate() {
      try {
         if(!(new TabelleEinstellungen()).getVersion().equals("Version: 4.08")) {
            logging.logInfo("Datenbankversion und Programm Version Stimmen nicht überein");
            logging.logInfo("System wird aktualisiert");
            StartBildschirmAO.startDialogText.setText("Installiere Updates... Bitte haben sie einen Moment Geduld...");
            double e2 = (double)System.currentTimeMillis();
            executeUpdate();
            double endZeit = (double)System.currentTimeMillis() - e2;
            logging.logInfo("Update installiert in: " + endZeit + " ms");
         } else {
            logging.logInfo("System ist aktuell");
         }
      } catch (Exception var4) {
         JOptionPane.showMessageDialog((Component)null, Konstante.UPDATE_NICHT_ERFOLGREICH_INSTALLIERT, "Fehlermeldung", 0);
         logging.logError("Fehler beim Update");
         logging.logPrintStackTrace(var4);
      }

   }

   private static void executeUpdate() throws Exception {
      UpdateDatenbank updateDatenbank = new UpdateDatenbank();
      TabelleEinstellungen tabEinstellungen = new TabelleEinstellungen();
      int mantantenAnzahl = updateDatenbank.executeSqlWithReturnINT("Select count(*) from mandant;");
      logging.logInfo("MandatenAnzahl geladen: " + mantantenAnzahl);
      String dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.09")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.10");
         updateDatenbank.executeSql("INSERT INTO einsatz_kategorie (`id` ,`name`) VALUES (\'5\', \'Rettungsdienst\'),(\'6\', \'First Responder\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `maengelmeldung_kommentar` (`mangelID` int(11) NOT NULL,`kommentarID` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL, `kommentar` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder ADD kommentar TEXT NOT NULL");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'vCardSeperator\', \';\');");
         updateDatenbank.executeSql("ALTER TABLE briefe ADD template INT NOT NULL ");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.10\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.10");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.10")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.11");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `keystore` (`key` text NOT NULL,`wert` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO `keystore` (`key`, `wert`) VALUES (\'Nummer1\', \'" + WinRegistry.readString(-2147483647, "Software\\FeuerwehrManagementSystem", "Nummer1") + "\'),(\'Nummer2\', \'" + WinRegistry.readString(-2147483647, "Software\\FeuerwehrManagementSystem", "Nummer2") + "\');");

         try {
            WinRegistry.deleteKey(-2147483647, "Software\\FeuerwehrManagementSystem");
         } catch (Exception var31) {
            ;
         }

         if((new TabelleKeyStore()).get("Nummer1").equals("null")) {
            updateDatenbank.executeSql("delete from keystore");
            updateDatenbank.executeSql("INSERT INTO `keystore` (`key`, `wert`) VALUES (\'Nummer1\', \'00001yxoCXmQdi29wWX71sjOT5DCZQSAr3nAv5in\'),(\'Nummer2\', \'00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.11\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.11");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.11")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.12");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `ablaufLKW` TEXT NOT NULL ");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoAblaufLKW` INT NOT NULL ");
         updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES (\'ablaufLKWFührerscheinViaEMail\', \'0\');");
         updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES (\'ablaufLKWAnzeigen\', \'0\');");
         updateDatenbank.executeSql("ALTER TABLE `einsatz` ADD `einsatzleiter` INT NOT NULL ");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.12\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.12");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.12")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.13");
         updateDatenbank.executeSql("Update keystore set wert = \'" + hash.createHashCode("0") + "\' where `key` = \'Nummer1\';");
         updateDatenbank.executeSql("Update keystore set wert = \'00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ\' where `key` = \'Nummer2\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.13\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.13");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.13")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.14");
         updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES (\'automatischesUpdate\', \'1\');");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.14\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.14");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.14")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.15");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.15\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.15");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.15")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.16");
         updateDatenbank.executeSql("UPDATE lager SET name = \'Virtuelles- / Defektteile- / Ausmusterlager\' WHERE id = 9000;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder ADD loeschkenner INT NOT NULL ");
         updateDatenbank.executeSql("ALTER TABLE einsatz ADD staerkeZF INT NOT NULL ");
         updateDatenbank.executeSql("ALTER TABLE einsatz ADD einsatzleiterBF TEXT NOT NULL ");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'einsatzleiterBF\', \'0\');");
         (new File("data/Bestandsliste")).mkdir();
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.16\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.16");
      }

      dbVersion = tabEinstellungen.getVersion();
      String[] m;
      int tabAnfahrt;
      int anfahrtListe;
      if(dbVersion.equals("Version: 1.16")) {
         logging.logInfo("Starte Update: " + dbVersion + " --> Version: 1.17");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'bundesland\', \'Nordrhein-Westfalen\');");
         m = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from dienstgrad where id > 10"));

         for(tabAnfahrt = 0; tabAnfahrt < m.length; ++tabAnfahrt) {
            anfahrtListe = tabAnfahrt + 51;
            updateDatenbank.executeSql("Update dienstgrad set id = " + anfahrtListe + " where id = " + m[tabAnfahrt]);
            updateDatenbank.executeSql("Update mitglieder set dienstgrad = " + anfahrtListe + " where dienstgrad = " + m[tabAnfahrt]);
         }

         updateDatenbank.executeSql("Update mitglieder set dienstgrad = 11 where dienstgrad = 10");
         updateDatenbank.executeSql("Update mitglieder set dienstgrad = 10 where dienstgrad = 9");
         updateDatenbank.executeSql("delete from dienstgrad where id between 1 and 50;");
         updateDatenbank.executeSql("INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`) VALUES(1, \'FMA\', \'Feuerwehrmannanwärter\'),(2, \'FM\', \'Feuerwehrmann\'),(3, \'OFM\', \'Oberfeuerwehrmann\'),(4, \'HFM\', \'Hauptfeuerwehrmann\'),(5, \'UBM\', \'Unterbrandmeister\'),(6, \'BM\', \'Brandmeister\'),(7, \'OBM\', \'Oberbrandmeister\'),(8, \'HBM\', \'Hauptprandmeister\'),(9, \'HBM\', \'Hauptprandmeister m. Zulage\'),(10, \'BI\', \'Brandinspektor\'),(11, \'BOI\', \'Brandoberinspecktor\'),(12, \'StBI\', \'Stadtbrandinspektor\'),(13, \'BAR\', \'Brandamtsrat\'),(14, \'BOAR\', \'Brandoberamtsrat\'),(15, \'BR\', \'Brandrat\'),(16, \'OBR\', \'Oberbrandrat\'),(17, \'BD\', \'Branddirektor\'),(18, \'OBD\', \'Oberbranddirektor\'),(19, \'DdBF\', \'Direktor der Berufsfeuerwehr\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `atemschutzpass` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`zeit` int(11) NOT NULL,`einsatzart` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `atemschutzpass_einsatzart` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO `atemschutzpass_einsatzart` (`id`, `name`) VALUES (1, \'PA\'),(2, \'Filter\'),(3, \'CSA\');");
         updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `BR68` INT NOT NULL ,ADD `BR69` INT NOT NULL ,ADD `BR70` INT NOT NULL ,ADD `BR71` INT NOT NULL ,ADD `BR72` INT NOT NULL ,ADD `BR73` INT NOT NULL ,ADD `BR74` INT NOT NULL ,ADD `BR75` INT NOT NULL");
         updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR68` = \'1\', `BR69` = \'1\', `BR70` = \'1\', `BR71` = \'1\', `BR72` = \'1\', `BR73` = \'1\', `BR74` = \'1\', `BR75` = \'1\' WHERE `berechtigunggruppe`.`id` = 1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.17\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.17");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.17")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.18\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.18");
      }

      dbVersion = tabEinstellungen.getVersion();
      int strassenIDListe;
      String[] objektAnfahrtListe;
      int objektIDListe;
      String[] o;
      int anfahrt;
      String[] tabKategorie;
      int lehrgangListe;
      String[] var33;
      String[] var34;
      String[] var38;
      String[] var42;
      String[] var46;
      if(dbVersion.equals("Version: 1.18")) {
         updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `rh` INT NOT NULL AFTER `GF`");
         updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `rs` INT NOT NULL AFTER `rh`");
         updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `ra` INT NOT NULL AFTER `rs`");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `dlkmaschi` INT NOT NULL AFTER `Maschi`");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `korbsteuerung` INT NOT NULL AFTER `dlkmaschi`");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `ZF` INT NOT NULL AFTER `GF`");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'abrechnungModul\', \'1\');");
         (new File("data/Abrechnung")).mkdir();
         m = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from fahrzeug_beschreibung where id > 11"));

         for(tabAnfahrt = 0; tabAnfahrt < m.length; ++tabAnfahrt) {
            anfahrtListe = tabAnfahrt + 51;
            System.out.println(anfahrtListe);
            updateDatenbank.executeSql("Update fahrzeug_beschreibung set id = " + anfahrtListe + " where id = " + m[tabAnfahrt]);
            updateDatenbank.executeSql("Update fahrzeuge set beschreibung = " + anfahrtListe + " where beschreibung = " + m[tabAnfahrt]);
         }

         updateDatenbank.executeSql("INSERT INTO `fahrzeug_beschreibung` (`id`, `beschreibung`) VALUES (12, \'Einsatzleitwagen\'),(13, \'Rettungswagen\'),(14, \'Krankentransportwagen\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_konto` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO `abrechnung_konto` (`id`, `name`) VALUES (1, \'SYSTEM\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_artikelklassen` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO `abrechnung_artikelklassen` (`id`, `name`) VALUES(1, \'Einsatz\'),(2, \'Dienstabend\'),(3, \'BSW\'),(4, \'Sonstige\'),(100, \'SYSTEM\'),(101, \'RABATT\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_artikel` (`id` int(11) NOT NULL,`name` text NOT NULL,`klasse` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`wert` int(11) NOT NULL,`rabattwert` int(11) NOT NULL,`mwst` int(11) NOT NULL,`berechnungsart` int(11) NOT NULL,`berechnungsart2` int(11) NOT NULL,`rabattart` int(11) NOT NULL,`aktiv` int(11) NOT NULL,`von` text NOT NULL,`bis` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO `abrechnung_artikel` (`id`, `name`, `klasse`, `buchungskonto`, `zahlungsart`, `wert`, `rabattwert`, `mwst`, `berechnungsart`, `berechnungsart2`, `rabattart`, `aktiv`, `von`, `bis`) VALUES(4000, \'Vergütung Brandsicherheitswache\', 3, 1, 2, 0, 0, 1, 1, 1, 1, 0, \'2015-01-01\', \'2099-12-31\'),(4001, \'Vergütung Einsatz\', 1, 1, 2, 0, 0, 1, 1, 1, 0, 0, \'2015-01-01\', \'2099-12-31\'),(4002, \'Vergütung Dienstabend\', 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, \'2015-01-01\', \'2099-12-31\'),(4003, \'Vergütung Sonstige\', 4, 1, 2, 0, 0, 1, 2, 0, 1, 0, \'2015-01-01\', \'2099-12-31\');");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung` (`id` int(11) NOT NULL,`abrechnungID` int(11) NOT NULL,`artikelID` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`wert` int(11) NOT NULL,`menge` int(11) NOT NULL,`datum` text NOT NULL,`status` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         var33 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 1 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));
         var34 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 1 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));

         for(strassenIDListe = 0; strassenIDListe < var33.length; ++strassenIDListe) {
            updateDatenbank.executeSql("UPDATE statistikeinsatz SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(var34[strassenIDListe])) + " WHERE veranstaltungID = " + var33[strassenIDListe] + ";");
         }

         var38 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 2 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));
         objektAnfahrtListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 2 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));

         for(objektIDListe = 0; objektIDListe < var38.length; ++objektIDListe) {
            updateDatenbank.executeSql("UPDATE statistiksonstigeveranstaltung SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(objektAnfahrtListe[objektIDListe])) + " WHERE veranstaltungID = " + var38[objektIDListe] + ";");
         }

         var42 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie > 4 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));
         o = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie > 4 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));

         for(anfahrt = 0; anfahrt < var42.length; ++anfahrt) {
            updateDatenbank.executeSql("UPDATE statistiksonstigeveranstaltung SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(o[anfahrt])) + " WHERE veranstaltungID = " + var42[anfahrt] + ";");
         }

         var46 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 3 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));
         tabKategorie = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 3 and datum between \'2015-01-01\' and \'2020-12-31\' order by id;"));

         for(lehrgangListe = 0; lehrgangListe < var46.length; ++lehrgangListe) {
            updateDatenbank.executeSql("UPDATE statistikbsw SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(tabKategorie[lehrgangListe])) + " WHERE veranstaltungID = " + var46[lehrgangListe] + ";");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.19\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.19");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.19")) {
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `g30` TEXT NOT NULL");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoG30` INT NOT NULL ");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.20\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.20");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.20")) {
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `doppelkanister` TEXT NOT NULL AFTER `kettensaege`");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `infoEMail` INT NOT NULL");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES (\'geraetepruefungViaEMail\', \'0\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES (\'offeneMaengelAnzeigen\', \'1\');");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoTuev` INT NOT NULL");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoSP` INT NOT NULL");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoService` INT NOT NULL");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES (\'fahrzeugUntersuchungViaEMail\', \'1\');");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.21\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.21");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.21")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.22\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.22");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.22")) {
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES (\'mängelmeldungViaEMailVersenden\', \'1\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES (\'gebAnzeigeModus\', \'1\');");
         updateDatenbank.executeSql("ALTER TABLE `abrechnung` ADD `umbuchungID` INT NOT NULL");
         m = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT gebdatum FROM mitglieder where gebdatum != \'\' order by gebdatum;"));

         for(tabAnfahrt = 0; tabAnfahrt < m.length; ++tabAnfahrt) {
            updateDatenbank.executeSql("update mitglieder set gebdatum = \'" + TimeCalculation.parseDateForDatabase(m[tabAnfahrt]) + "\' where gebdatum = \'" + m[tabAnfahrt] + "\';");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.23\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.23");
      }

      dbVersion = tabEinstellungen.getVersion();
      String var32;
      String var35;
      String var37;
      String var39;
      String var40;
      String var43;
      String var47;
      if(dbVersion.equals("Version: 1.23")) {
         JOptionPane.showMessageDialog((Component)null, "HINWEIS:\n\nDas Update auf die Version: 1.24 wird einige Zeit in Anspruch nehmen.\nSie werden informiert sobald das Update abgeschlossen ist!");
         (new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties")).checkPropertiesEntry("ClientID", RandomGenerator.generate(20, Mode.ALPHANUMERIC));
         (new File("data/Ausbildungsunterlagen")).mkdir();
         (new File("data/KarteBilder")).mkdir();
         (new File("data/KarteBilder/groß")).mkdir();
         (new File("data/KarteBilder/klein")).mkdir();
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `abstusiset` TEXT NOT NULL AFTER `geraetepruefung_allgem`");
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `mitgliederakte_kommentar` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`kommentar` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'vorbelegungBSWTreffen\', \'12:15\'), (\'vorbelegungBSWVeranstaltungStart\', \'15:30\'), (\'vorbelegungBSWEnde\', \'18:15\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'EinsatzBerichtArt\', \'Word Schnittstelle\');");
         var32 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_elemente` (`id` int(11) NOT NULL,`gruppe` text NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "INSERT INTO `einsatz_bericht_elemente` (`id`, `gruppe`, `name`) VALUES(1, \'EinsatzArt\', \'Kleinbrand\'),(2, \'EinsatzArt\', \'Mittelbrand\'),(3, \'EinsatzArt\', \'Großbrand\'),(4, \'EinsatzArt\', \'Kaminbrand\'),(5, \'EinsatzArt\', \'Gasauströmung\'),(6, \'EinsatzArt\', \'Blinder Alarm\'),(7, \'EinsatzArt\', \'Böswilliger Alarm\'),(8, \'EinsatzArt\', \'Verkehrsunfall\'),(9, \'EinsatzArt\', \'Verkehrsstörung\'),(10, \'EinsatzArt\', \'Einsturz\'),(11, \'EinsatzArt\', \'Mensch in Notlage\'),(12, \'EinsatzArt\', \'Gefahrgut (GSG)\'),(13, \'EinsatzArt\', \'Ölspur\'),(14, \'EinsatzArt\', \'Tier in Notlage\'),(15, \'EinsatzArt\', \'Tiertranssport\'),(16, \'EinsatzArt\', \'Tierkadaver\'),(17, \'EinsatzArt\', \'Betriebsunfall\'),(18, \'EinsatzArt\', \'TH Baum\'),(19, \'EinsatzArt\', \'TH Wasser\'),(20, \'EinsatzArt\', \'keine Angaben\'),(101, \'Stelle\', \'Keller\'),(102, \'Stelle\', \'Erdgeschloß\'),(103, \'Stelle\', \'Obergeschoß\'),(104, \'Stelle\', \'Dachgeschoß\'),(105, \'Stelle\', \'eingeschloss. Gebäude\'),(106, \'Stelle\', \'Baustelle\'),(107, \'Stelle\', \'Freigelände\'),(108, \'Stelle\', \'Auf dem Wasser\'),(109, \'Stelle\', \'Straße\'),(110, \'Stelle\', \'Autobahn\'),(111, \'Stelle\', \'Landstraße\'),(112, \'Stelle\', \'Kraftfahrstraße\'),(113, \'Stelle\', \'keine Angaben\'),(201, \'Objekt\', \'Wohngebäude\'),(202, \'Objekt\', \'Verwaltungsgebäude\'),(203, \'Objekt\', \'Landwirdschaftl. Gebäude\'),(204, \'Objekt\', \'Industriebetrieb\'),(205, \'Objekt\', \'gewerbl. Betrieb\'),(206, \'Objekt\', \'Fahrzeug\'),(207, \'Objekt\', \'Wald\'),(208, \'Objekt\', \'Feld\'),(209, \'Objekt\', \'Grasnarbe\'),(210, \'Objekt\', \'keine Angaben\'),(301, \'Alamierung\', \'Digitaler Meldeempfänger (DME)\'),(302, \'Alamierung\', \'Leitstelle Feuerwehr\'),(303, \'Alamierung\', \'Leitstelle Polizei\'),(304, \'Alamierung\', \'Telefon\'),(305, \'Alamierung\', \'Sirene\'),(301, \'Ausdehnung\', \'Auf Entstehungrum begrenzt\'),(302, \'Ausdehnung\', \'vor Eintreffen (auf andere Räume übgergeriffen)\'),(303, \'Ausdehnung\', \'vor Eintreffen (auf andere Gebäude übgergeriffen)\'),(304, \'Ausdehnung\', \'während der Brandbekämpfung (auf andere Räume übgergeriffen)\'),(305, \'Ausdehnung\', \'während der Brandbekämpfung (auf andere Gebäude übgergeriffen)\'),(306, \'Ausdehnung\', \'keine Angaben\');";
         var35 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_daten` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`einsatzID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`einsatzArt` int(11) NOT NULL,`stelle` int(11) NOT NULL,`objekt` int(11) NOT NULL,`eigentuemerName` text NOT NULL,`eigentuemerAnschrift` text NOT NULL,`eigentuemerTelefon` text NOT NULL,`verursacherName` text NOT NULL,`verursacherAnschrift` text NOT NULL,`verursacherTelefon` text NOT NULL,`alamierung` int(11) NOT NULL,`meldenderName` text NOT NULL,`meldenderAnschrift` text NOT NULL,`meldenderTelefon` text NOT NULL,`lage` text NOT NULL,`verlauf` text NOT NULL,`eingesetzteGeraete` text NOT NULL,`verbrauchWasser` text NOT NULL,`verbrauchSchaum` text NOT NULL,`verbrauchPulver` text NOT NULL,`verbrauchBindemittel` text NOT NULL,`vorEintreffenGeloescht` int(11) NOT NULL,`schnellangriff` int(11) NOT NULL,`crohr` text NOT NULL,`brohr` text NOT NULL,`kleinloeschgeraet` text NOT NULL,`tragbareLeitern` int(11) NOT NULL,`atemschutzgeraet` text NOT NULL,`fluchthauben` text NOT NULL,`belueftungsgeraet` text NOT NULL,`rettungsgeraet` int(11) NOT NULL,`ausdehnung` int(11) NOT NULL,`entstehungsursache` text NOT NULL,`verletzte` text NOT NULL,`gerettete` text NOT NULL,`tote` text NOT NULL,`schadenhoehe` text NOT NULL,`brandwacheFahrzeug` int(11) NOT NULL,`staerke` text NOT NULL,`dauer` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var39 = "CREATE TABLE IF NOT EXISTS `ftpsync` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL,`ordner` text NOT NULL,`status` int(11) NOT NULL,`groesse` BIGINT NOT NULL DEFAULT \'0\') ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var40 = "INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`) VALUES(1, \'SYSTEM\', \'\', \'data\', 0),(2, \'SYSTEM\', \'\', \'data/Templates\', 0),(3, \'SYSTEM\', \'\', \'data/Papierkorb\', 0),(4, \'SYSTEM\', \'\', \'data/Eigene Dateien\', 0),(5, \'SYSTEM\', \'\', \'data/Mitgliederakte\', 0),(6, \'SYSTEM\', \'\', \'data/Fahrzeugakte\', 0),(7, \'SYSTEM\', \'\', \'data/EMail\', 0),(8, \'SYSTEM\', \'\', \'data/EMail/Anhang\', 0),(9, \'SYSTEM\', \'\', \'data/EMail/Anhang/Gesendet\', 0),(10, \'SYSTEM\', \'\', \'data/EMail/Anhang/Entwurf\', 0),(11, \'SYSTEM\', \'\', \'data/EMail/Anhang/Empfangende\', 0),(12, \'SYSTEM\', \'\', \'data/EMail/Temp\', 0),(13, \'SYSTEM\', \'\', \'data/EMail/Temp/original_nachricht\', 0),(14, \'SYSTEM\', \'\', \'data/DBBACKUP\', 0),(15, \'SYSTEM\', \'\', \'data/Bestandsliste\', 0),(16, \'SYSTEM\', \'\', \'data/Abrechnung\', 0),(17, \'SYSTEM\', \'\', \'data/Ausbildungsunterlagen\', 0),(18, \'SYSTEM\', \'data/Templates/Einsatzbericht.docx\', \'\', 0),(19, \'SYSTEM\', \'data/Templates/Einsatzbericht.xml\', \'\', 0),(20, \'SYSTEM\', \'data/Templates/Mängelmeldung.docx\', \'\', 0),(21, \'SYSTEM\', \'data/Templates/Mängelmeldung.xml\', \'\', 0),(22, \'SYSTEM\', \'data/Templates/Verdienstausfallbescheinigung.docx\', \'\', 0),(23, \'SYSTEM\', \'data/Templates/Verdienstausfallbescheinigung.docx\', \'\', 0);";
         var43 = "CREATE TABLE IF NOT EXISTS `ftpsync_del` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL, `status` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         updateDatenbank.executeSql(var35);
         updateDatenbank.executeSql(var39);
         updateDatenbank.executeSql(var40);
         updateDatenbank.executeSql(var43);
         File var44;
         if(!((String)runApplication.EINSTELLUNGEN.get("EinsatzBericht")).startsWith("data/Templates")) {
            var44 = new File((String)runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
            var47 = "data/Templates/Einsatzbericht/" + var44.getName();
            Utils.kopiereDateiInDataOrdner(var44, var47, "data/Templates/Einsatzbericht");
            updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = \'" + var47 + "\' WHERE `key` = \'EinsatzBericht\';");
         }

         if(!((String)runApplication.EINSTELLUNGEN.get("briefkopf")).startsWith("images")) {
            var44 = new File((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
            var47 = "data/Templates/Briefkopf/" + var44.getName();
            Utils.kopiereDateiInDataOrdner(var44, var47, "data/Templates/Briefkopf/");
            updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = \'" + var47 + "\' WHERE `key` = \'briefkopf\';");
         }

         if(!((String)runApplication.EINSTELLUNGEN.get("verdienstausfall")).startsWith("data/Templates")) {
            var44 = new File((String)runApplication.EINSTELLUNGEN.get("verdienstausfall"));
            var47 = "data/Templates/Verdienstausfall/" + var44.getName();
            Utils.kopiereDateiInDataOrdner(var44, var47, "data/Templates/Verdienstausfall");
            updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = \'" + var47 + "\' WHERE `key` = \'verdienstausfall\';");
         }

         if(!((String)runApplication.EINSTELLUNGEN.get("mängelmeldung")).startsWith("data/Templates")) {
            var44 = new File((String)runApplication.EINSTELLUNGEN.get("mängelmeldung"));
            var47 = "data/Templates/Mängelmeldung/" + var44.getName();
            Utils.kopiereDateiInDataOrdner(var44, var47, "data/Templates/Mängelmeldung");
            updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = \'" + var47 + "\' WHERE `key` = \'mängelmeldung\';");
         }

         o = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id from karte_strassen order by id;"));
         var46 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT bild from karte_strassen order by id;"));
         tabKategorie = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT bild2 from karte_strassen order by id;"));

         File l;
         String newBildFolder;
         for(lehrgangListe = 0; lehrgangListe < var46.length; ++lehrgangListe) {
            l = new File(var46[lehrgangListe]);
            newBildFolder = "data/KarteBilder/groß/" + l.getName();
            Utils.kopiereDateiInDataOrdner(l, newBildFolder, "data/KarteBilder/groß");
            updateDatenbank.executeSql("Update karte_strassen set bild = \'" + newBildFolder + "\' where id = " + o[lehrgangListe]);
         }

         for(lehrgangListe = 0; lehrgangListe < tabKategorie.length; ++lehrgangListe) {
            l = new File(tabKategorie[lehrgangListe]);
            newBildFolder = "data/KarteBilder/klein/" + l.getName();
            Utils.kopiereDateiInDataOrdner(l, newBildFolder, "data/KarteBilder/klein");
            updateDatenbank.executeSql("Update karte_strassen set bild2 = \'" + newBildFolder + "\' where id = " + o[lehrgangListe]);
         }

         (new TabelleFTPSync()).deleteAll();
         Utils.rekatalogisiereDateien(runApplication.arbeitsverzeichnis + "data");
         runApplication.EINSTELLUNGEN = (new TabelleEinstellungen()).getAllEinstellungen();
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.24\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.24");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.24")) {
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set name = \'Bungalow\' where id = 105;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.25\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.25");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.25")) {
         Utils.ordnerErstellen("data/" + SbcUtils.timeStamp("yyyy") + "/Schichten", "SYSTEM");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'Schichtplaner\', \'1\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'Fahrtenbuch\', \'1\');");
         var32 = "CREATE TABLE IF NOT EXISTS `schicht_mitglieder` (`schichtID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE IF NOT EXISTS `schicht_gruppen_mitglieder` (`gruppenID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var35 = "CREATE TABLE IF NOT EXISTS `schicht_gruppe` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var39 = "CREATE TABLE IF NOT EXISTS `fahrtenbuch` (`id` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`datumVon` text NOT NULL,`zeitVon` text NOT NULL,`datumBis` text NOT NULL,`zeitBis` text NOT NULL,`kmBeginn` int(11) NOT NULL,`kmEnde` int(11) NOT NULL,`distance` int(11) NOT NULL,`tanken` text NOT NULL,`pumpenbetrieb` text NOT NULL,`sonstiges` text NOT NULL,`fahrer` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var40 = "CREATE TABLE IF NOT EXISTS `mitglieder_laufbahn` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datum` text NOT NULL,`art` text NOT NULL,`alterDienstgrad` int(11) NOT NULL,`neuerDienstgrad` int(11) NOT NULL,`lehrgang` int(11) NOT NULL,`ue` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var43 = "CREATE TABLE IF NOT EXISTS `statistiklehrgang` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`lehrgangID` int(11) NOT NULL,`dauer` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         String var48 = "CREATE TABLE IF NOT EXISTS `schicht` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`name` text NOT NULL,`datumVon` text NOT NULL,`uhrVon` text NOT NULL,`datumBis` text NOT NULL,`uhrBis` text NOT NULL,`von` int(11) NOT NULL,`bis` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         updateDatenbank.executeSql(var35);
         updateDatenbank.executeSql(var39);
         updateDatenbank.executeSql(var40);
         updateDatenbank.executeSql(var43);
         updateDatenbank.executeSql(var48);
         updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `BR76` INT NOT NULL ,ADD `BR77` INT NOT NULL ,ADD `BR78` INT NOT NULL,ADD `BR79` INT NOT NULL,ADD `BR80` INT NOT NULL,ADD `BR81` INT NOT NULL,ADD `BR82` INT NOT NULL,ADD `BR83` INT NOT NULL,ADD `BR84` INT NOT NULL,ADD `BR85` INT NOT NULL,ADD `BR86` INT NOT NULL,ADD `BR87` INT NOT NULL");
         updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR76` = \'1\', `BR77` = \'1\', `BR78` = \'1\', `BR79` = \'1\', `BR80` = \'1\', `BR81` = \'1\', `BR82` = \'1\', `BR83` = \'1\', `BR84` = \'1\', `BR85` = \'1\', `BR86` = \'1\', `BR87` = \'1\' WHERE `berechtigunggruppe`.`id` = 1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.26\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.26");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.26")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.27\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.27");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.27")) {
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'SichtbarkeitVeranstaltungVergangenheit\', \'1\'), (\'SichtbarkeitVeranstaltungZukunft\', \'12\')");
         updateDatenbank.executeSql("Update berechtigunggruppe set BR81 = 1, BR82 = 1, BR83 = 1, BR84 = 1;");
         updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `ue` INT NOT NULL ");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.28\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.28");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.28")) {
         var32 = "CREATE TABLE IF NOT EXISTS `systemwarnung` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`info` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'JoomlaVeranstaltungSenden\', \'0\'), (\'JoomlaLink\', \'\'), (\'JoomlaAusbildungsplanSenden\', \'0\'), (\'AlwaysOnTop\', \'0\'), (\'JoomlaEinsatzkomponente\', \'0\'), (\'JoomlaEinsatzkomponenteVisible\', \'0\'), (\'JoomlaEinsatzkomponenteEMail\', \'0\'), (\'JoomlaEinsatzkomponenteEMailAn1\', \'0\'), (\'JoomlaEinsatzkomponenteEMailAn2\', \'0\'), (\'JoomlaEinsatzkomponenteEMailAn3\', \'0\'), (\'terminVersandtViaEMailConfig\', \'1\'),(\'joomlaEinsatzkomponenteConfig\', \'1\'), (\'joomlaEinsatzkomponenteSecretKey\', \'\'), (\'joomlaEinsatzkomponenteStichwort\', \'1\'),(\'Joomla_mod_Veranstaltung\', \'/modules/mod_Veranstaltung/veranstaltung.php\'),(\'Joomla_mod_Veranstaltung_update\', \'/modules/mod_Veranstaltung/veranstaltung_update.php\'),(\'Joomla_mod_VeranstaltungKategorie\', \'/modules/mod_Veranstaltung/veranstaltungKategorie.php\'),(\'Joomla_mod_Veranstaltung_delete\', \'/modules/mod_Veranstaltung/veranstaltung_delete.php\'),(\'Joomla_mod_Ausbildungsplan_delete\', \'/modules/mod_Ausbildungsplan/ausbildungsplan_delete.php\'),(\'Joomla_mod_VeranstaltungKategorie_delete\', \'/modules/mod_Veranstaltung/veranstaltungKategorie_delete.php\'),(\'Joomla_mod_AusbildungKategorie_delete\', \'/modules/mod_Ausbildungsplan/AusbildungKategorie_delete.php\'),(\'Joomla_mod_Ausbildungsplan\', \'/modules/mod_Ausbildungsplan/ausbildungsplan.php\'),(\'Joomla_mod_AusbildungKategorie\', \'/modules/mod_Ausbildungsplan/ausbildungKategorie.php\'),(\'Joomla_com_Einsatz\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway.php\'),(\'Joomla_com_Einsatz_Freischalten\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_freischalten.php\');");
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.29\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.29");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.29")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.30\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.30");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.30")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.31\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.31");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.31")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `ue` INT NOT NULL");
         } catch (Exception var30) {
            ;
         }

         updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `loeschbar` INT NOT NULL");
         updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `loeschkenner` INT NOT NULL");
         updateDatenbank.executeSql("UPDATE lehrgang_kategorie SET loeschbar = 1 WHERE id IN ( 1,2,12,26,6,8,15,19,20,21,22,23,24,25,27);");
         updateDatenbank.executeSql("ALTER TABLE `user` ADD `loeschkenner` INT NOT NULL");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `gaswartung` TEXT NOT NULL AFTER `service`");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoGas` INT NOT NULL");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'VerdienstausfallBerichtArt\', \'Word Schnittstelle\'), (\'MängelBerichtArt\', \'Word Schnittstelle\')");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.32\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.32");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.32")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.33\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.33");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.33")) {
         updateDatenbank.executeSql("UPDATE berechtigunggruppe set BR86 = 1");
         updateDatenbank.executeSql("UPDATE berechtigunggruppe set BR85 = 1 where id = 1");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.34\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.34");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.34")) {
         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `ftpsync_error` (`datei` text NOT NULL,`ordner` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.35\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.35");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.35")) {
         updateDatenbank.executeSql("UPDATE fahrzeug_beschreibung SET beschreibung = \'Teleskopmast\' WHERE id =5;");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'Urlaubsplaner\', \'1\');");
         var32 = "CREATE TABLE IF NOT EXISTS `urlaub` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datumVon` text NOT NULL,`datumBis` text NOT NULL, `loeschkenner` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         var33 = Utils.listToArray((new TabelleJahr()).getAllVerfügbarenJahre());

         for(anfahrtListe = 0; anfahrtListe < var33.length; ++anfahrtListe) {
            Utils.ordnerErstellen("data/" + var33[anfahrtListe] + "/Schichten", "SYSTEM");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.36\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.36");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.36")) {
         updateDatenbank.executeSql("ALTER TABLE `ftpsync_error` ADD `clientID` TEXT NOT NULL FIRST");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.37\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.37");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.37")) {
         updateDatenbank.executeSql("Update ftpsync set status = 0, clientID = \'SYSTEM\' where datei = \'data/Templates/Einsatzbericht.docx\';");
         updateDatenbank.executeSql("Update ftpsync set status = 0, clientID = \'SYSTEM\' where datei = \'data/Templates/Einsatzbericht.xml\';");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'emailTemplateEinsatzbericht\', \'\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'emailTemplateEinsatzberichtAN\', \'\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'emailTemplateEinsatzberichtCC\', \'\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'emailTemplateEinsatzberichtBCC\', \'\');");
         updateDatenbank.executeSql("ALTER TABLE `email_entwurf` CHANGE `anhang` `anhang` TEXT NOT NULL ");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.38\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.38");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.38")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.39\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.39");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.39")) {
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'globaleEMailEinheitsführungAktiviert\', \'0\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'globaleEMailGerätewarteAktiviert\', \'0\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'globaleEMailEinheitsführung\', \'\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'globaleEMailGerätewarte\', \'\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'Statistik2\', \'0\');");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.40\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.40");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.40")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.41\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.41");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.41")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.42\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.42");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.42")) {
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'EinsatznummerIstPflicht\', \'0\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'EinsatzLeiterBFIstPflicht\', \'0\');");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_ausbildungsplan/ausbildungsplan_delete.php\' where `key` = \'Joomla_mod_Ausbildungsplan_delete\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_ausbildungsplan/AusbildungKategorie_delete.php\' where `key` = \'Joomla_mod_AusbildungKategorie_delete\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_ausbildungsplan/ausbildungsplan.php\' where `key` = \'Joomla_mod_Ausbildungsplan\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_ausbildungsplan/ausbildungKategorie.php\' where `key` = \'Joomla_mod_AusbildungKategorie\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_veranstaltung/veranstaltung.php\' where `key` = \'Joomla_mod_Veranstaltung\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_veranstaltung/veranstaltung_update.php\' where `key` = \'Joomla_mod_Veranstaltung_update\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_veranstaltung/veranstaltungKategorie.php\' where `key` = \'Joomla_mod_VeranstaltungKategorie\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_veranstaltung/veranstaltung_delete.php\' where `key` = \'Joomla_mod_Veranstaltung_delete\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_veranstaltung/veranstaltungKategorie_delete.php\' where `key` = \'Joomla_mod_VeranstaltungKategorie_delete\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.43\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.43");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 1.43")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.44\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.44");
      }

      dbVersion = tabEinstellungen.getVersion();
      int var41;
      if(dbVersion.equals("Version: 1.44")) {
         var32 = "CREATE TABLE IF NOT EXISTS `beförderung_konfiguration` (`id` int(11) NOT NULL,`dienstgradID` int(11) NOT NULL,`dienstgradVoraussetzung` int(11) NOT NULL,`zeit` int(11) NOT NULL,`nurZeitBefoerderung` int(11) NOT NULL,`letzteStufe` int(11) NOT NULL,`auslassen` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE IF NOT EXISTS `beförderung_erforderlich` (`id` int(11) NOT NULL, `lehrgangID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql("ALTER TABLE `lehrgangsmeldung` ADD `art` TEXT NOT NULL;");
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         var34 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT * FROM mitglieder_laufbahn WHERE `id` > 11000"));
         strassenIDListe = updateDatenbank.executeSqlWithReturnINT("SELECT max(id) FROM `mitglieder_laufbahn` WHERE id < 11000") + 1;

         for(var41 = 0; var41 < var34.length; ++var41) {
            updateDatenbank.executeSql("Update mitglieder_laufbahn set mitgliederID = " + var34[var41] + " where id = " + var34[var41]);
            updateDatenbank.executeSql("Update mitglieder_laufbahn set id = " + strassenIDListe + " where id = " + var34[var41]);
            ++strassenIDListe;
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 1.45\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 1.45");
      }

      dbVersion = tabEinstellungen.getVersion();
      int var49;
      if(dbVersion.equals("Version: 1.45")) {
         JOptionPane.showMessageDialog((Component)null, Konstante.START_RELEASEUPDATE);
         var32 = "CREATE TABLE IF NOT EXISTS `mitglieder_history` (`changeDate` text NOT NULL,`changeTime` text NOT NULL,`benutzer` text NOT NULL, `id` int(11) NOT NULL,`mitgliederGruppe` int(11) NOT NULL,`anrede` int(11) NOT NULL,`name` text NOT NULL,`vorname` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefonPrivat` text NOT NULL,`telefonMobil` text NOT NULL,`telefonArbeit` text NOT NULL,`email` text NOT NULL,`email2` text NOT NULL,`dienstgrad` int(11) NOT NULL,`ausserDienst` int(11) NOT NULL,`mitgliedSeit` int(4) NOT NULL,`gebDatum` text NOT NULL,`kommentar` text NOT NULL,`loeschkenner` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE IF NOT EXISTS `dateisystem` (`id` int(11) NOT NULL, `dateiStream` longblob NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var35 = "CREATE TABLE IF NOT EXISTS `mandant` (`id` int(11) NOT NULL,`name` text NOT NULL, `bf` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var39 = "INSERT INTO mandant (`id` ,`name`, `bf`) VALUES (\'1\', \'" + RandomGenerator.generate(5, Mode.ALPHA_BIG_SIGNS) + "\', 0);";
         var40 = "CREATE TABLE IF NOT EXISTS `clients` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`alias` text NOT NULL, `zugelassen` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         updateDatenbank.executeSql(var35);
         updateDatenbank.executeSql(var39);
         updateDatenbank.executeSql(var40);
         var42 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT clientID FROM `ftpsync` where clientID != \'SYSTEM\' group by clientID"));

         for(var49 = 0; var49 < var42.length; ++var49) {
            updateDatenbank.executeSql("INSERT INTO clients (`id` ,`clientID`,`alias`, `zugelassen`) VALUES (1, \'" + var42[var49] + "\', \'" + InetAddress.getLocalHost() + "\', 1);");
         }

         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES (\'schutzziel1\', \'8\'), (\'schutzziel2\', \'13\'), (\'headerPrint\', \'1\'), (\'footerPrint\', \'0\'), (\'modulVeranstaltung\', \'1\'), (\'modulAusbildungsplan\', \'1\'), (\'modulFahrzeugeinteilung\', \'1\');");
         updateDatenbank.executeSql("ALTER TABLE ftpsync ADD `statusDB` INT NOT NULL AFTER `status` ");
         updateDatenbank.executeSql("UPDATE ftpsync set statusDB = 1 where ordner != \'\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'1\' where `key` = \'Statistik2\';");
         o = Utils.listToArray(updateDatenbank.executeSqlWithReturn("show tables from " + (String)runApplication.PROPERTIES.get("DatenbankName") + ";"));

         for(anfahrt = 0; anfahrt < o.length; ++anfahrt) {
            logging.logInfo(o[anfahrt]);
            if(!o[anfahrt].equals("mandant") && !o[anfahrt].equals("atemschutzpass_einsatzart") && !o[anfahrt].equals("fahrzeug_beschreibung")) {
               updateDatenbank.executeSql("ALTER TABLE " + o[anfahrt] + " ADD `mandantID` INT NOT NULL ");
               updateDatenbank.executeSql("Update " + o[anfahrt] + " set mandantID = 1");
            } else {
               logging.logInfo("Es wird keine MandantID in dieser Tabelle benötigt...");
            }
         }

         updateDatenbank.executeSql("ALTER TABLE maengelmeldung_kommentar ADD `user` TEXT NOT NULL AFTER `kommentar` ");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `eMailDeaktiv` INT NOT NULL AFTER `loeschkenner`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `eMailDeaktiv` INT NOT NULL AFTER `loeschkenner`;");
         var47 = "CREATE TABLE IF NOT EXISTS `statistikmitglieder` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`alterGes` int(11) NOT NULL,`anzahl` int(11) NOT NULL,`erstellung` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var47);
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'emailTemplateMängelmeldungAN\', \'\', \'1\'), (\'emailTemplateMängelmeldungCC\', \'\', \'1\'), (\'emailTemplateMängelmeldungBCC\', \'\', \'1\'), (\'emailTemplateMängelmeldung\', \'\', \'1\');");
         updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`)VALUES (\'verdienstausfallOptionen\', \'1\', \'1\');");
         MyProperties var51 = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementsystem.properties");
         var51.checkPropertiesEntry("MandantID", "1");
         var51.checkPropertiesEntry("BlobActiv", "false");
         if(((String)runApplication.PROPERTIES.get("DB_TYP")).equals("SSH")) {
            var51.checkPropertiesEntry("FTPUploadActiv", "true");
         } else {
            var51.checkPropertiesEntry("FTPUploadActiv", "false");
         }

         runApplication.PROPERTIES = runApplication.lesePropertieDatei(var51);
         runApplication.createMitgliederStatistik(Integer.parseInt(SbcUtils.timeStamp("yyyy")));
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.00\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.00");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.00")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.01\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.01");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.01")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.02\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.02");
      }

      dbVersion = tabEinstellungen.getVersion();
      int var36;
      if(dbVersion.equals("Version: 2.02")) {
         updateDatenbank.executeSql("ALTER TABLE jahresberichte ADD `autoBericht` INT NOT NULL AFTER `erstelldatum`");
         updateDatenbank.executeSql("Update jahresberichte set autoBericht = 1 where title like \'Automatischer erstellter Bericht vom%\';");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`, `mandantID`) VALUES (\'eMailName\', \'\', " + var36 + ");");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.03\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.03");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.03")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.04\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.04");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.04")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'/modules/mod_ausbildungsplan/ausbildungKategorie_delete.php\' where `key` = \'Joomla_mod_AusbildungKategorie_delete\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.05\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.05");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.05")) {
         updateDatenbank.executeSql("UPDATE lehrgang_kategorie set art = \'Fü\' where id in (1,2,3);");
         updateDatenbank.executeSql("ALTER TABLE mitglieder_untersuchung ADD `g41` TEXT NOT NULL AFTER `infoG30` ,ADD `g42` TEXT NOT NULL AFTER `g41` ");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'zeilenhöheDruck\', \'30\', " + var36 + "), (\'zeilenhöheAnsicht\', \'30\', " + var36 + ");");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.06\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.06");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.06")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.07\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.07");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.07")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.08\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.08");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.08")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.09\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.09");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.09")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE jahresberichte ADD `autoBericht` INT NOT NULL AFTER `erstelldatum`");
         } catch (Exception var29) {
            ;
         }

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'bswHitliste\', \'1\', " + var36 + "), (\'getakteteInternetverbindung\', \'0\', " + var36 + "), (\'onlineStatus\', \'1\', " + var36 + ");");
         }

         updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `php` (`id` int(11) NOT NULL,`typ` text NOT NULL,`adresse` text NOT NULL,`parameter` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.10\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.10");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.10")) {
         var32 = "CREATE TABLE IF NOT EXISTS `protokoll` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`title` text NOT NULL,`protokolltext` text NOT NULL,`erstelldatum` text NOT NULL,`mandantID` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE berechtigunggruppe ADD `BR88` INT NOT NULL AFTER `BR87` ,ADD `BR89` INT NOT NULL AFTER `BR88` ,ADD `BR90` INT NOT NULL AFTER `BR89` ,ADD `BR91` INT NOT NULL AFTER `BR90` ,ADD `BR92` INT NOT NULL AFTER `BR91` ,ADD `BR93` INT NOT NULL AFTER `BR92` ,ADD `BR94` INT NOT NULL AFTER `BR93` ,ADD `BR95` INT NOT NULL AFTER `BR94`;");
         updateDatenbank.executeSql("Update berechtigunggruppe set BR88 = 1, BR89 = 1,BR90 = 1,BR91 = 1,BR92 = 1,BR93 = 1,BR94 = 1,BR95 = 1 where id = 1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.11\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.11");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.11")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES (\'JoomlaEinsatzKomponenteNurAlamierungÜbertragen\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.12\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.12");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.12")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES (\'JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.13\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.13");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.13")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES (\'Joomla_com_Einsatz_Bericht\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_bericht.php\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.14\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.14");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.14")) {
         var32 = "CREATE TABLE IF NOT EXISTS `einsatz_organisationen` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`organisationID` int(11) NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE IF NOT EXISTS `organisationen` (`id` int(11) NOT NULL,`name` text NOT NULL,`sortierung` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);

         for(anfahrtListe = 1; anfahrtListe < mantantenAnzahl + 1; ++anfahrtListe) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES (\'WeitereOrganisationen\', \'1\', \'" + anfahrtListe + "\'),(\'Joomla_com_Einsatz_Orgaisation\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_organisation.php\', \'" + anfahrtListe + "\'),(\'Joomla_com_Einsatz_Fahrzeug\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_fahrzeug.php\', \'" + anfahrtListe + "\'),(\'Joomla_com_Einsatz_Delete\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_delete.php\', \'" + anfahrtListe + "\'),(\'FullBackupInZip\', \'0\', \'" + anfahrtListe + "\'),(\'FullBackupPath\', \'\', \'" + anfahrtListe + "\');");
            var39 = "INSERT INTO `organisationen` (`id`, `name`, `sortierung`, `mandantID`) VALUES(1, \'\', 0, " + anfahrtListe + ");";
            updateDatenbank.executeSql(var39);
         }

         updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR90` = \'1\'");
         updateDatenbank.executeSql("ALTER TABLE `jahresberichte` ADD `statistiken` TEXT NOT NULL AFTER `dateiname`");
         updateDatenbank.executeSql("update jahresberichte set statistiken = \'leer\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.15\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.15");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.15")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.16\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.16");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.16")) {
         updateDatenbank.executeSql("Update einsatz_kategorie set name = \'Wachbesetzung\' where id = 3;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.17\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.17");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.17")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.18\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.18");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.18")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.19\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.19");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.19")) {
         updateDatenbank.executeSql("ALTER TABLE `einsatz` ADD `fahrzeugID` TEXT NOT NULL AFTER `Fahrzeug`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.20\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.20");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.20")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.21\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.21");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.21")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.22\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.22");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.22")) {
         updateDatenbank.executeSql("ALTER TABLE mitglieder CHANGE `mitgliedSeit` `mitgliedSeit` TEXT NOT NULL;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder_history CHANGE `mitgliedSeit` `mitgliedSeit` TEXT NOT NULL;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder ADD `hochzeit` TEXT NOT NULL AFTER `gebDatum`;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder_history ADD `hochzeit` TEXT NOT NULL AFTER `gebDatum`;");
         updateDatenbank.executeSql("ALTER TABLE mitglieder_laufbahn ADD `datumVon` TEXT NOT NULL AFTER `mitgliederID`;");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'mitgliedSeitFormat\', \'yyyy\', \'" + var36 + "\');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'hochzeitFeldFuerMitglieder\', \'0\', \'" + var36 + "\');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'mitgliedSeitPflichtEintrag\', \'1\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 2.23\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 2.23");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 2.23")) {
         JOptionPane.showMessageDialog((Component)null, Konstante.START_RELEASEUPDATE);
         (new File(runApplication.arbeitsverzeichnis + "data/EMail/Temp/original_nachricht_unwetter")).mkdir();
         var32 = "CREATE TABLE  IF NOT EXISTS `ehrungen_konfiguration` (`id` int(11) NOT NULL,`ehrungID` int(11) NOT NULL, `zeit` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE  IF NOT EXISTS `email_unwetterwarnung` (`id` int(11) NOT NULL,`sender` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`date` text NOT NULL,`size` int(11) NOT NULL,`anhang` int(11) NOT NULL,`gelesen` int(11) NOT NULL,`art` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR93` = \'1\', `BR94` = \'1\';");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `ablaufDienstausweis` TEXT NOT NULL AFTER `infoAblaufLKW`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoAblaufDienstausweis` int(11) NOT NULL AFTER `ablaufDienstausweis`;");
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);

         for(anfahrtListe = 1; anfahrtListe < mantantenAnzahl + 1; ++anfahrtListe) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'LehrgangEintragenAusMitgliederVerwaltungMode\', \'0\', \'" + anfahrtListe + "\'),(\'unwetterwarnungPop3\', \'\', \'" + anfahrtListe + "\'), (\'unwetterwarnungEMail\', \'\', \'" + anfahrtListe + "\'), (\'unwetterwarnungPopPort\', \'\', \'" + anfahrtListe + "\'), (\'unwetterwarnungPasswort\', \'\', \'" + anfahrtListe + "\'), (\'unwetterwarnungSSL\', \'0\', \'" + anfahrtListe + "\'), (\'unwetterwarnungModulAktiv\', \'0\', \'" + anfahrtListe + "\'),(\'unwetterwarnungDatumBis\', \'null\', \'" + anfahrtListe + "\'), (\'unwetterwarnungUrzeitBis\', \'null\', \'" + anfahrtListe + "\');");
            var38 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from lehrgang_kategorie where mandantID = " + anfahrtListe + ";"));
            objektAnfahrtListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select art from lehrgang_kategorie where mandantID = " + anfahrtListe + ";"));

            for(objektIDListe = 0; objektIDListe < var38.length; ++objektIDListe) {
               updateDatenbank.executeSql("Update mitglieder_laufbahn set art = \'" + objektAnfahrtListe[objektIDListe] + "\' where lehrgang = " + var38[objektIDListe] + "  and mandantID = " + anfahrtListe + ";");
            }

            var42 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + anfahrtListe + ";"));

            for(var49 = 0; var49 < var42.length; ++var49) {
               logging.logInfo("Mitglied: " + var42[var49]);
               Mitgliederlaufbahn var53 = new Mitgliederlaufbahn();
               TabelleLehrgang_kategorie var52 = new TabelleLehrgang_kategorie();
               String[] var55 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select lehrgangID from lehrgang where mitgliedID = " + var42[var49] + " and status = 1 and mandantID = " + anfahrtListe + ";"));

               for(int var54 = 0; var54 < var55.length; ++var54) {
                  if(updateDatenbank.executeSqlWithReturnINT("Select count(*) from mitglieder_laufbahn where lehrgang = " + var55[var54] + " and mitgliederID = " + var42[var49] + " and mandantID = " + anfahrtListe + ";") == 0) {
                     logging.logInfo("Lehrgang: " + var55[var54]);
                     var53.setId((new TabelleMitglieder_laufbahn()).getNextNumber());
                     var53.setAlterDienstgrad(0);
                     var53.setNeuerDienstgrad(0);
                     var53.setArt(var52.getArt(Integer.parseInt(var55[var54])));
                     var53.setDatum("");
                     var53.setDatumVon("");
                     var53.setLehrgang(Integer.parseInt(var55[var54]));
                     var53.setMitgliederID(Integer.parseInt(var42[var49]));
                     var53.setUe(0);
                     (new TabelleMitglieder_laufbahn()).insert(var53);
                  } else {
                     logging.logInfo("Lehrgnag in der Laufbahn bereits vorhanden...");
                  }
               }

               logging.logInfo("Fertig für: " + var42[var49]);
            }
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.00\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.00");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.00")) {
         var32 = "CREATE TABLE  IF NOT EXISTS `berechtigung` (`id` int(11) NOT NULL,`name` text NOT NULL,`gruppe` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `beförderung_konfiguration` ADD `dienstZeit` INT NOT NULL AFTER `zeit`;");
         updateDatenbank.executeSql("update `beförderung_konfiguration` set `dienstZeit` = -1;");

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES (\'vorwarnungAblaufDienstausweis\', \'2\', \'" + tabAnfahrt + "\'), (\'ablaufDienstausweisAnzeigen\', \'0\', \'" + tabAnfahrt + "\'),(\'ablaufDienstausweisViaEMail\', \'0\', \'" + tabAnfahrt + "\');");
            var34 = new String[]{"Dienstgrad anlegen", "Mitgliederverwaltung", "Mitglied Löschen / Außer Dienst", "Mitglieder Liste + Geburtstagsliste", "Einsatz Liste", "Brandsicherheitswache Liste", "Lehrgangsliste", "Anwesenheitsliste", "Arbeitgeber Liste", "Angehörigen Liste", "Untersuchung Liste", "Beteiligungsübersicht Liste", "Mitglieder Bankverbindung Liste", "Veranstaltungsliste", "Dokumentenexplorer", "Einsatz Bericht erstellen", "Verdienstausfallbescheinigung", "Jahresbericht erstellen", "Brief Erstellen", "Mängelmeldung", "Anwesenheit Gesamt", "Anwesenheit Einsatz", "Anwesenheit Dienstabend", "Anwesenheit BSW", "Abwesenheitsstatistik", "Einsatzart / Stichwort Statistik", "Ausrückezeiten", "Einsatzdauer", "Mannstunden Einsatz", "Einsatz Pro Monat", "Einsatz Pro Stunde", "Einsatz Pro Woche", "BSW Mannstunden", "Fehlalarme", "Beteiligung bei...", "Ausbildungsstatistik", "Fahrzeug Statistik", "Alarmfahrtdauer", "Fahrzeuggruppe anlegen", "Stichwort anlegen", "Veranstaltungskategorie anlegen", "Fahrzeug Außer Dienst", "Abwesenheitgrund erstellen", "Programmeinstellungen", "Anwesenheit eintragen", "Abwesenehit eintragen", "Mitglieder Gruppe anlegen", "Ausbildungsinhalte eintragen", "Fahrezugverwaltung", "Geräteprüfng", "Fahrzeugeinteilung eintragen", "Benutzerverwaltung", "Karte / Einsatzgebiet editieren", "Beför. / Lehrgangsmeldung", "Mitglieder Anrede anlegen", "E-Mail senden/schreiben", "Bestandsverwaltung", "Bestandsverwaltung organisieren", "Bestandsverwaltung Artikel anlegen", "Mitgliederakte", "Fahrzeugakte", "Beteiligungszeit", "Datensicherung", "Ausbildungsplan erstellen", "Ausbildungsplan Liste", "Virtuelles Lager leeren", "Mängelmeldung bearbeiten", "Lehrgang anlegen", "Atemschutzpass Eintragen", "Atemschutzpass", "Abrechnung", "Abrechnung - Aktikel", "Abrechnung - Konto", "Abrechnung - Manuelle Verbuchung", "Veranstaltung editieren", "Fahrtenbuch eintrag", "Schichtplaner", "Laufbahn Pflegen", "Fahrtenbuch Liste", "Schichtplaner Liste", "Laufbahn Liste", "Einsatz anlegen", "Dienstabend anlegen", "BSW anlegen", "Sonstige Veranstaltung", "Anwesenheit löschen", "Lehrgänge Mitgliedern hinzufügen", "Urlaubsplaner", "Protokoll schreiben / aktulisieren", "Protokoll lesen", "Organisationen erstellen", "Stichwort-Kategorie", "Mitgliederlaufbahn Eintrag Löschen", "Mitgliederverwaltung editieren", "Mitgliederuntersuchung", "Einsatzbericht neu erstellen"};

            for(strassenIDListe = 0; strassenIDListe < var34.length; ++strassenIDListe) {
               updateDatenbank.executeSql("INSERT INTO `berechtigung` (`id`, `name`, `gruppe`, `mandantID`) VALUES (\'" + strassenIDListe + "\', \'" + var34[strassenIDListe] + "\', \'0\', \'" + tabAnfahrt + "\');");
            }
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.01\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.01");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.01")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.02\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.02");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.02")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'prüfungDerFahrerlaubnis\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `pruefungDerFahrberechtigung` TEXT NOT NULL AFTER `infoAblaufDienstausweis`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoPruefungDerFahrberechtigung` INT NOT NULL AFTER `pruefungDerFahrberechtigung`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.03\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.03");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.03")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'darstellungLehrgängeMitgliederverwaltung\', \'CheckBox\', \'" + var36 + "\'),(\'ablaufFahrberechtigungAnzeigen\', \'0\', \'" + var36 + "\'), (\'vorwarnungAblaufFahrberechtigung\', \'2\', \'" + var36 + "\'),(\'ablaufFahrberechtigungViaEMail\', \'0\', \'" + var36 + "\'),(\'druckAnwesenheitsListeMode\', \'1\', \'" + var36 + "\'),(\'dienstgradAufAnwesenheitsliste\', \'1\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `beruf` TEXT NOT NULL AFTER `email2`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `beruf` TEXT NOT NULL AFTER `email2`;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 401 where gruppe = \'Ausdehnung\' and id = 301;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 402 where gruppe = \'Ausdehnung\' and id = 302;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 403 where gruppe = \'Ausdehnung\' and id = 303;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 404 where gruppe = \'Ausdehnung\' and id = 304;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 405 where gruppe = \'Ausdehnung\' and id = 305;");
         updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 406 where gruppe = \'Ausdehnung\' and id = 306;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 401 where ausdehnung = 301;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 402 where ausdehnung = 302;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 403 where ausdehnung = 303;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 404 where ausdehnung = 304;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 405 where ausdehnung = 305;");
         updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 406 where ausdehnung = 306;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.04\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.04");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.04")) {
         var32 = "CREATE TABLE  IF NOT EXISTS `mitglieder_verfuegbarkeit` (`id` int(11) NOT NULL,`mitgliedID` int(11) NOT NULL,`telegrammID` text NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `telegrammID` text NOT NULL AFTER `telefonArbeit`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `telegrammID` text NOT NULL AFTER `telefonArbeit`;");

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'modulMitgliederVerfügbarkeit\', \'1\', \'" + tabAnfahrt + "\'),(\'schfiftgrößeAnwesenheitsliste\', \'26\', \'" + tabAnfahrt + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.05\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.05");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.05")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` CHANGE `pruefungDerFahrerlaubnis` `pruefungDerFahrberechtigung` TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` CHANGE `infoPruefungDerFahrerlaubnis` `infoPruefungDerFahrberechtigung` INT( 11 ) NOT NULL;");
         } catch (SQLException var28) {
            ;
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.06\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.06");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.06")) {
         updateDatenbank.executeSql("ALTER TABLE `clients` ADD `typ` TEXT NOT NULL AFTER `alias`;");
         updateDatenbank.executeSql("Update clients set typ = \'FMS\';");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'bestaetignungFreistellungEinsatzArt\', \'PDF (intern)\', \'" + var36 + "\'), (\'bestaetignungFreistellungEinsatz\', \'data/Templates/BescheinigungEinsatzTeilnahme.xml\', \'" + var36 + "\'), (\'bestaetignungFreistellungEinsatzAktiv\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.07\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.07");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.07")) {
         updateDatenbank.executeSql("update berechtigung set name = \'Geräteprüfung\' where id = 49;");
         updateDatenbank.executeSql("update berechtigung set name = \'Fahrzeugverwaltung\' where id = 48;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `fuehrerscheinNummer` TEXT NOT NULL AFTER `kommentar`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `fahrberechtigungNummer` TEXT NOT NULL AFTER `fuehrerscheinNummer`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `dienstausweisNummer` TEXT NOT NULL AFTER `fahrberechtigungNummer`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.08\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.08");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.08")) {
         updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung` ADD `jahr` INT NOT NULL AFTER `veranstaltungID`;");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung` ADD `kategorie` INT NOT NULL AFTER `veranstaltungID`;");
         m = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT veranstaltungID FROM fahrzeugeinteilung group by veranstaltungID;"));

         for(tabAnfahrt = 0; tabAnfahrt < m.length; ++tabAnfahrt) {
            TabelleVeranstaltung var45 = new TabelleVeranstaltung();
            strassenIDListe = Integer.parseInt(m[tabAnfahrt]);
            (new TabelleFahrzeugeinteilung()).updateKategorie(var45.getVeranstaltungKategorieID(strassenIDListe), strassenIDListe);
            updateDatenbank.executeSql("update fahrzeugeinteilung set jahr = " + var45.getJahrDerVeranstaltung(strassenIDListe) + " where veranstaltungID = " + strassenIDListe + ";");
         }

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'modulEinsatzgebiet\', \'1\', \'" + tabAnfahrt + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.09\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.09");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.09")) {
         var32 = "CREATE TABLE  IF NOT EXISTS `einstellungen_gespeichert` (`key` text NOT NULL,`wert` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'globaleEMailG25Aktiviert\', \'0\', \'" + tabAnfahrt + "\'), (\'globaleEMailG25\', \'\', \'" + tabAnfahrt + "\'), (\'globaleEMailG26Aktiviert\', \'0\', \'" + tabAnfahrt + "\'), (\'globaleEMailG26\', \'\', \'" + tabAnfahrt + "\'), (\'globaleEMailFahrberechtigungAktiviert\', \'0\', \'" + tabAnfahrt + "\'), (\'globaleEMailFahrberechtigung\', \'\', \'" + tabAnfahrt + "\'), (\'globaleEMailDienstausweisAktiviert\', \'0\', \'" + tabAnfahrt + "\'), (\'globaleEMailDienstausweis\', \'\', \'" + tabAnfahrt + "\');");
            updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES (\'G25\', \'\', \'" + tabAnfahrt + "\'), (\'G26\', \'\', \'" + tabAnfahrt + "\'),(\'Dienstausweis\', \'\', \'" + tabAnfahrt + "\'), (\'Fahrberechtigung\', \'\', \'" + tabAnfahrt + "\'),(\'G30\', \'\', \'" + tabAnfahrt + "\'),(\'unwetterwarnungDatumBis\', \'null\', \'" + tabAnfahrt + "\'), (\'unwetterwarnungUhrzeitBis\', \'null\', \'" + tabAnfahrt + "\');");
         }

         updateDatenbank.executeSql("delete from einstellungen where `key`= \'unwetterwarnungDatumBis\';");
         updateDatenbank.executeSql("delete from einstellungen where `key`= \'unwetterwarnungUrzeitBis\';");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.10\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.10");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.10")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.11\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.11");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.11")) {
         updateDatenbank.executeSql("ALTER TABLE `fahrzeuge` ADD `trupp` INT NOT NULL AFTER `anhaenger`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_berichte` ADD `fahrzeugbelegung` INT NOT NULL AFTER `dateiname`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_berichte` ADD `atemschutz` INT NOT NULL AFTER `fahrzeugbelegung`;");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'EinsatzBerichtFahrzeugbelegungHinzufügen\', \'0\', \'" + var36 + "\'), (\'EinsatzBerichtAtemschutzpassHinzufügen\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.12\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.12");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.12")) {
         updateDatenbank.executeSql("ALTER TABLE `atemschutzpass` ADD `truppZuordnung` INT NOT NULL AFTER `einsatzart`;");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'EinsatzBerichtEinsatzleiterMitDienstgrad\', \'1\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.13\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.13");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.13")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.14\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.14");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.14")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `berechtigung` ADD `seite` INT NOT NULL AFTER `name`;");
            updateDatenbank.executeSql("update berechtigung set seite = 1;");
            updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `seite` INT NOT NULL AFTER `id`;");
            updateDatenbank.executeSql("update berechtigunggruppe set seite = 1;");
         } catch (Exception var27) {
            ;
         }

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            if(updateDatenbank.executeSqlWithReturnINT("Select count(*) from berechtigung where seite = 2;") == 0) {
               updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'feldEintreffenAusblenden\', \'0\', \'" + var36 + "\'), (\'feldStadtteilAusblenden\', \'0\', \'" + var36 + "\'), (\'langesDatumsformatUntersuchungsliste\', \'0\', \'" + var36 + "\');");
               updateDatenbank.executeSql("INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`, `mandantID`) VALUES(0, \'Informationsbereich - Termine anzeigen\', 2, 0, " + var36 + "),(1, \'Informationsbereich - Geburtstage anzeigen\', 2, 0, " + var36 + "),(2, \'Informationsbereich - G26 anzeigen\', 2, 0, " + var36 + "),(3, \'Informationsbereich - G25 anzeigen\', 2, 0, " + var36 + "),(4, \'Informationsbereich - G30 anzeigen\', 2, 0, " + var36 + "),(5, \'Informationsbereich - LKW Führerschein Ablauf anzeigen\', 2, 0, " + var36 + "),(6, \'Informationsbereich - AGT Training anzeigen\', 2, 0, " + var36 + "),(7, \'Informationsbereich - Ablauf Dienstausweis anzeigen\', 2, 0, " + var36 + "),(8, \'Informationsbereich - Ablauf Fahrberechtigung anzeigen\', 2, 0, " + var36 + "),(9, \'Informationsbereich - Abgelaufene G26 anzeigen\', 2, 0, " + var36 + "),(10, \'Informationsbereich - Abgelaufene G25 anzeigen\', 2, 0, " + var36 + "),(11, \'Informationsbereich - Abgelaufene G30 anzeigen\', 2, 0, " + var36 + "),(12, \'Informationsbereich - Abgelaufenes AGT Training anzeigen\', 2, 0, " + var36 + "),(13, \'Informationsbereich - Abgelaufene LKW Führerscheine anzeigen\', 2, 0, " + var36 + "),(14, \'Informationsbereich - Abgelaufene Dienstausweise anzeigen\', 2, 0, " + var36 + "),(15, \'Informationsbereich - Abgelaufene Fahrberechtigung anzeigen\', 2, 0, " + var36 + "),(16, \'Informationsbereich - TÜV anzeigen\', 2, 0, " + var36 + "),(17, \'Informationsbereich - Sicherheitsprüfung anzeigen\', 2, 0, " + var36 + "),(18, \'Informationsbereich - Fahrzeug Wartung anzeigen\', 2, 0, " + var36 + "),(19, \'Informationsbereich - Gas Wartung anzeigen\', 2, 0, " + var36 + "),(20, \'Informationsbereich - Abgelaufener TÜV anzeigen\', 2, 0, " + var36 + "),(21, \'Informationsbereich - Abgelaufener SP anzeigen\', 2, 0, " + var36 + "),(22, \'Informationsbereich - Abgelaufene Wartung anzeigen\', 2, 0, " + var36 + "),(23, \'Informationsbereich - Abgelaufener Gas Wartung anzeigen\', 2, 0, " + var36 + "),(24, \'Informationsbereich - Geräteprüfung anzeigen\', 2, 0, " + var36 + "),(25, \'Informationsbereich - Mängelmeldungen anzeigen\', 2, 0, " + var36 + "),(26, \'Statistik - Anwesenheit Sonstige Veranstaltung\', 2, 0, " + var36 + "),(27, \'Statistik - Verfügbarkeit Einsatz\', 2, 0, " + var36 + "),(28, \'Statistik - Einsatz - Stadtteilstatistik\', 2, 0, " + var36 + "),(29, \'Statistik - Schutzzielstatistik\', 2, 0, " + var36 + "),(30, \'Statistik - Tag / Nacht Einsätze\', 2, 0, " + var36 + "),(31, \'Statistik - Fahrzeugbelegung (Einsatz)\', 2, 0, " + var36 + "),(32, \'Statistik - Atemschutzstatistik\', 2, 0, " + var36 + "),(33, \'Statistik - Veranstaltungzählung\', 2, 0, " + var36 + "),(34, \'Statistik - Durchscnittsalter\', 2, 0, " + var36 + "),(35, \'Statistik - Mitgliederzahlen\', 2, 0, " + var36 + "),(36, \'Statistik - Mitglieder Dienstgrad\', 2, 0, " + var36 + "),(37, \'Statistik - Mitglieder Funktionen (Anzahl)\', 2, 0, " + var36 + "),(38, \'Veranstaltung Editieren - Einsatz bearbeiten\', 2, 0, " + var36 + "),(39, \'D.-Explorer - Abrechnungen anzeigen\', 2, 0, " + var36 + "),(40, \'D.-Explorer - Ausbildungunterlagen anzeigen\', 2, 0, " + var36 + "),(41, \'D.-Explorer - Bestandslisten\', 2, 0, " + var36 + "),(42, \'D.-Explorer - Eigene Dateien\', 2, 0, " + var36 + "),(43, \'D.-Explorer - Verdienstausfallb.\', 2, 0, " + var36 + "),(44, \'D.-Explorer - Mängelmeldungen\', 2, 0, " + var36 + "),(45, \'D.-Explorer - Lehrgangsmeldungen\', 2, 0, " + var36 + "),(46, \'D.-Expolrer - Fahrzeugeinteilung\', 2, 0, " + var36 + "),(47, \'D.-Explorer - Einsatzbereichte\', 2, 0, " + var36 + "),(48, \'D.-Explorer - Briefe\', 2, 0, " + var36 + "),(49, \'D.-Explorer - Beteiligungsübersicht\', 2, 0, " + var36 + "),(50, \'D.-Explorer - Berichte\', 2, 0, " + var36 + "),(51, \'frei51\', 2, 0, " + var36 + "),(52, \'frei52\', 2, 0, " + var36 + "),(53, \'frei53\', 2, 0, " + var36 + "),(54, \'frei54\', 2, 0, " + var36 + "),(55, \'frei55\', 2, 0, " + var36 + "),(56, \'frei56\', 2, 0, " + var36 + "),(57, \'frei57\', 2, 0, " + var36 + "),(58, \'frei58\', 2, 0, " + var36 + "),(59, \'frei59\', 2, 0, " + var36 + "),(60, \'frei60\', 2, 0, " + var36 + "),(61, \'frei61\', 2, 0, " + var36 + "),(62, \'frei62\', 2, 0, " + var36 + "),(63, \'frei63\', 2, 0, " + var36 + "),(64, \'frei64\', 2, 0, " + var36 + "),(65, \'frei65\', 2, 0, " + var36 + "),(66, \'frei66\', 2, 0, " + var36 + "),(67, \'frei67\', 2, 0, " + var36 + "),(68, \'frei68\', 2, 0, " + var36 + "),(69, \'frei69\', 2, 0, " + var36 + "),(70, \'frei70\', 2, 0, " + var36 + "),(71, \'frei71\', 2, 0, " + var36 + "),(72, \'frei72\', 2, 0, " + var36 + "),(73, \'frei73\', 2, 0, " + var36 + "),(74, \'frei74\', 2, 0, " + var36 + "),(75, \'frei75\', 2, 0, " + var36 + "),(76, \'frei76\', 2, 0, " + var36 + "),(77, \'frei77\', 2, 0, " + var36 + "),(78, \'frei78\', 2, 0, " + var36 + "),(79, \'frei79\', 2, 0, " + var36 + "),(80, \'frei80\', 2, 0, " + var36 + "),(81, \'frei81\', 2, 0, " + var36 + "),(82, \'frei82\', 2, 0, " + var36 + "),(83, \'frei83\', 2, 0, " + var36 + "),(84, \'frei84\', 2, 0, " + var36 + "),(85, \'frei85\', 2, 0, " + var36 + "),(86, \'frei86\', 2, 0, " + var36 + "),(87, \'frei87\', 2, 0, " + var36 + "),(88, \'frei88\', 2, 0, " + var36 + "),(89, \'frei89\', 2, 0, " + var36 + "),(90, \'frei90\', 2, 0, " + var36 + "),(91, \'frei91\', 2, 0, " + var36 + "),(92, \'frei92\', 2, 0, " + var36 + "),(93, \'frei93\', 2, 0, " + var36 + "),(94, \'frei94\', 2, 0, " + var36 + "),(95, \'frei95\', 2, 0, " + var36 + ");");
            }

            var33 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select name from berechtigunggruppe where mandantID = " + var36 + ";"));

            for(anfahrtListe = 0; anfahrtListe < var33.length; ++anfahrtListe) {
               updateDatenbank.executeSql("INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`, `BR88`, `BR89`, `BR90`, `BR91`, `BR92`, `BR93`, `BR94`, `BR95`, `mandantID`) VALUES (" + anfahrtListe + ", 2, \'" + var33[anfahrtListe] + "\', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, " + var36 + ");");
            }
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.15\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.15");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.15")) {
         updateDatenbank.executeSql("Update berechtigung set name = \'D.-Explorer - Atemschutz\' where seite = 2 and id = 51;");
         updateDatenbank.executeSql("Update berechtigung set name = \'Mitgliederliste - Zausatzdaten\' where seite = 2 and id = 52;");
         Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Atemschutz", "SYSTEM");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.16\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.16");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.16")) {
         updateDatenbank.executeSql("ALTER TABLE `berechtigung` DROP `mandantID`;");
         updateDatenbank.executeSql("delete from berechtigung;");
         var32 = "INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`) VALUES(0, \'Dienstgrad anlegen\', 1, 6),(1, \'Mitgliederverwaltung\', 1, 2),(2, \'Mitglied löschen / außer Dienst\', 1, 2),(3, \'Mitgliederliste + Geburtstagsliste\', 1, 3),(4, \'Einsatzliste\', 1, 3),(5, \'Brandsicherheitswachliste\', 1, 3),(6, \'Lehrgangsliste\', 1, 3),(7, \'Anwesenheitsliste\', 1, 3),(8, \'Arbeitgeberliste\', 1, 3),(9, \'Angehörigenliste\', 1, 3),(10, \'Untersuchungsliste\', 1, 3),(11, \'Beteiligungsübersicht\', 1, 3),(12, \'Bankverbindungsliste\', 1, 3),(13, \'Veranstaltungsliste\', 1, 3),(14, \'Dokumentenexplorer\', 1, 4),(15, \'Einsatzbericht erstellen\', 1, 1),(16, \'Verdienstausfallbescheinigung\', 1, 4),(17, \'Jahresbericht erstellen\', 1, 4),(18, \'Brief erstellen\', 1, 4),(19, \'Mängelmeldung\', 1, 4),(20, \'Anwesenheit Gesamt\', 1, 5),(21, \'Anwesenheit Einsatz\', 1, 5),(22, \'Anwesenheit Dienstabend\', 1, 5),(23, \'Anwesenheit BSW\', 1, 5),(24, \'Abwesenheitsstatistik\', 1, 5),(25, \'Einsatzart / Stichwort\', 1, 5),(26, \'Ausrückezeiten\', 1, 5),(27, \'Einsatzdauer\', 1, 5),(28, \'Mannstunden Einsatz\', 1, 5),(29, \'Einsatz pro Monat\', 1, 5),(30, \'Einsatz pro Stunde\', 1, 5),(31, \'Einsatz pro Woche\', 1, 5),(32, \'BSW Mannstunden\', 1, 5),(33, \'Fehlalarme\', 1, 5),(34, \'Beteiligung bei...\', 1, 5),(35, \'Ausbildungsstatistik\', 1, 5),(36, \'Fahrzeug Statistik\', 1, 5),(37, \'Alarmfahrtdauer\', 1, 5),(38, \'Fahrzeuggruppe anlegen\', 1, 6),(39, \'Stichwort anlegen\', 1, 6),(40, \'Veranstaltungskategorie anlegen\', 1, 6),(41, \'Fahrzeug außer Dienst\', 1, 2),(42, \'Abwesenheitgrund erstellen\', 1, 6),(43, \'Programmeinstellungen\', 1, 6),(44, \'Anwesenheit eintragen\', 1, 1),(45, \'Abwesenehit eintragen\', 1, 1),(46, \'Mitgliedergruppe erstellen / löschen\', 1, 6),(47, \'Ausbildungsinhalte eintragen\', 1, 1),(48, \'Fahrzeugverwaltung\', 1, 2),(49, \'Geräteprüfung\', 1, 6),(50, \'Fahrzeugeinteilung eintragen\', 1, 1),(51, \'Benutzerverwaltung\', 1, 6),(52, \'Karte / Einsatzgebiet editieren\', 1, 6),(53, \'Beför. / Lehrgangsmeldung\', 1, 3),(54, \'Mitgliederanrede erstellen\', 1, 6),(55, \'E-Mail senden/schreiben\', 1, 4),(56, \'Bestandsverwaltung\', 1, 4),(57, \'Bestandsverwaltung organisieren\', 1, 4),(58, \'Bestandsverwaltung Artikel anlegen\', 1, 4),(59, \'Mitgliederakte\', 1, 2),(60, \'Fahrzeugakte\', 1, 2),(61, \'Beteiligungszeit\', 1, 5),(62, \'Datensicherung\', 1, 6),(63, \'Ausbildungsplan erstellen\', 1, 4),(64, \'Ausbildungsplan\', 1, 3),(65, \'virtuelles Lager leeren\', 1, 4),(66, \'Mängelmeldung bearbeiten\', 1, 4),(67, \'Lehrgang anlegen\', 1, 6),(68, \'Atemschutzpass eintragen\', 1, 1),(69, \'Atemschutzpass\', 1, 3),(70, \'Abrechnung\', 1, 2),(71, \'Abrechnung - Artikel\', 1, 2),(72, \'Abrechnung - Konto\', 1, 2),(73, \'Abrechnung - manuelle Verbuchung\', 1, 2),(74, \'Veranstaltung editieren (24 Std.)\', 1, 1),(75, \'Fahrtenbuch eintrag\', 1, 1),(76, \'Schichtplaner\', 1, 1),(77, \'Mitgliederlaufbahn pflegen\', 1, 2),(78, \'Fahrtenbuchliste\', 1, 3),(79, \'Schichtplanerliste\', 1, 3),(80, \'Mitgliederlaufbahnliste\', 1, 3),(81, \'Einsatz anlegen\', 1, 1),(82, \'Dienstabend anlegen\', 1, 1),(83, \'BSW anlegen\', 1, 1),(84, \'Sonstige Veranstaltung\', 1, 1),(85, \'Anwesenheit löschen\', 1, 1),(86, \'Lehrgänge Mitgliedern hinzufügen\', 1, 2),(87, \'Urlaubsplaner\', 1, 1),(88, \'Protokoll / Tätigkeitsbericht\', 1, 4),(89, \'Protokoll lesen\', 1, 4),(90, \'Organisationen erstellen\', 1, 6),(91, \'Stichwort-Kategorie\', 1, 6),(92, \'Mitgliederlaufbahnen editieren\', 1, 2),(93, \'Mitgliederverwaltung editieren\', 1, 2),(94, \'Mitgliederuntersuchung\', 1, 2),(95, \'Einsatzbericht neu erstellen\', 1, 1),(0, \'Termine anzeigen\', 2, 7),(1, \'Geburtstage anzeigen\', 2, 7),(2, \'G26 anzeigen\', 2, 7),(3, \'G25 anzeigen\', 2, 7),(4, \'G30 anzeigen\', 2, 7),(5, \'LKW Führerschein Ablauf anzeigen\', 2, 7),(6, \'AGT Training anzeigen\', 2, 7),(7, \'Ablauf Dienstausweis anzeigen\', 2, 7),(8, \'Ablauf Fahrberechtigung anzeigen\', 2, 7),(9, \'Abgelaufene G26 anzeigen\', 2, 7),(10, \'Abgelaufene G25 anzeigen\', 2, 7),(11, \'Abgelaufene G30 anzeigen\', 2, 7),(12, \'Abgelaufenes AGT Training anzeigen\', 2, 7),(13, \'Abgelaufene LKW Führerscheine anzeigen\', 2, 7),(14, \'Abgelaufene Dienstausweise anzeigen\', 2, 7),(15, \'Abgelaufene Fahrberechtigung anzeigen\', 2, 7),(16, \'TÜV anzeigen\', 2, 7),(17, \'Sicherheitsprüfung anzeigen\', 2, 7),(18, \'Fahrzeug Wartung anzeigen\', 2, 7),(19, \'Gaswartung anzeigen\', 2, 7),(20, \'Abgelaufener TÜV anzeigen\', 2, 7),(21, \'Abgelaufener SP anzeigen\', 2, 7),(22, \'Abgelaufene Wartung anzeigen\', 2, 7),(23, \'Abgelaufener Gaswartung anzeigen\', 2, 7),(24, \'Geräteprüfung anzeigen\', 2, 7),(25, \'Mängelmeldungen anzeigen\', 2, 7),(26, \'Anwesenheit Sonstige Veranstaltung\', 2, 5),(27, \'Verfügbarkeit Einsatz\', 2, 5),(28, \'Einsatz - Stadtteilstatistik\', 2, 5),(29, \'Schutzzielstatistik\', 2, 5),(30, \'Tag / Nacht Einsätze\', 2, 5),(31, \'Fahrzeugbelegung (Einsatz)\', 2, 5),(32, \'Atemschutzstatistik\', 2, 5),(33, \'Veranstaltungzählung\', 2, 5),(34, \'Durchscnittsalter\', 2, 5),(35, \'Mitgliederzahlen\', 2, 5),(36, \'Mitglieder Dienstgrad\', 2, 5),(37, \'Mitglieder Funktionen (Anzahl)\', 2, 5),(38, \'Veranstaltung Editieren - Einsatz bearbeiten\', 2, 1),(39, \'D.-Explorer - Abrechnungen anzeigen\', 2, 4),(40, \'D.-Explorer - Ausbildungunterlagen anzeigen\', 2, 4),(41, \'D.-Explorer - Bestandslisten\', 2, 4),(42, \'D.-Explorer - Eigene Dateien\', 2, 4),(43, \'D.-Explorer - Verdienstausfallb.\', 2, 4),(44, \'D.-Explorer - Mängelmeldungen\', 2, 4),(45, \'D.-Explorer - Lehrgangsmeldungen\', 2, 4),(46, \'D.-Expolrer - Fahrzeugeinteilung\', 2, 4),(47, \'D.-Explorer - Einsatzbereichte\', 2, 4),(48, \'D.-Explorer - Briefe\', 2, 4),(49, \'D.-Explorer - Beteiligungsübersicht\', 2, 4),(50, \'D.-Explorer - Berichte\', 2, 4),(51, \'D.-Explorer - Atemschutz\', 2, 4),(52, \'Mitgliederliste - Zausatzdaten\', 2, 3),(53, \'Sonstige Mannstunden\', 2, 5),(54, \'frei54\', 2, 0),(55, \'frei55\', 2, 0),(56, \'frei56\', 2, 0),(57, \'frei57\', 2, 0),(58, \'frei58\', 2, 0),(59, \'frei59\', 2, 0),(60, \'frei60\', 2, 0),(61, \'frei61\', 2, 0),(62, \'frei62\', 2, 0),(63, \'frei63\', 2, 0),(64, \'frei64\', 2, 0),(65, \'frei65\', 2, 0),(66, \'frei66\', 2, 0),(67, \'frei67\', 2, 0),(68, \'frei68\', 2, 0),(69, \'frei69\', 2, 0),(70, \'frei70\', 2, 0),(71, \'frei71\', 2, 0),(72, \'frei72\', 2, 0),(73, \'frei73\', 2, 0),(74, \'frei74\', 2, 0),(75, \'frei75\', 2, 0),(76, \'frei76\', 2, 0),(77, \'frei77\', 2, 0),(78, \'frei78\', 2, 0),(79, \'frei79\', 2, 0),(80, \'frei80\', 2, 0),(81, \'frei81\', 2, 0),(82, \'frei82\', 2, 0),(83, \'frei83\', 2, 0),(84, \'frei84\', 2, 0),(85, \'frei85\', 2, 0),(86, \'frei86\', 2, 0),(87, \'frei87\', 2, 0),(88, \'frei88\', 2, 0),(89, \'frei89\', 2, 0),(90, \'frei90\', 2, 0),(91, \'frei91\', 2, 0),(92, \'frei92\', 2, 0),(93, \'frei93\', 2, 0),(94, \'frei94\', 2, 0),(95, \'frei95\', 2, 0);";
         var37 = "CREATE TABLE  IF NOT EXISTS `berechtigung_gruppe_name` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var35 = "INSERT INTO `berechtigung_gruppe_name` (`id`, `name`) VALUES (\'1\', \'Veranstaltungen / Anwesenheit\'), (\'2\', \'Mitglieder- / Fahrzeugverwaltung\'), (\'3\', \'Listen\'), (\'4\', \'Berichte / Dokumente\'), (\'5\', \'Statistik\'), (\'6\', \'Optionen / Verwaltung\'), (\'7\', \'Informationsbereich\');";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         updateDatenbank.executeSql(var35);

         for(strassenIDListe = 1; strassenIDListe < mantantenAnzahl + 1; ++strassenIDListe) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'LookAndFeel\', \'JAVA-CLASSIC\', \'" + strassenIDListe + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.17\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.17");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.17")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES (\'ErhalteneInfoMeldung\', \'\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.18\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.18");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.18")) {
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `mitgliedBis` TEXT NOT NULL AFTER `mitgliedSeit`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `mitgliedBis` TEXT NOT NULL AFTER `mitgliedSeit`;");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'anmeldungSpeichernErlauben\', \'0\', \'" + var36 + "\'),(\'terminVersandtViaEMailFolgeMonat\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.19\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.19");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.19")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.20\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.20");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.20")) {
         try {
            updateDatenbank.executeSql("Update clients set typ = \'FMS\';");
         } catch (SQLException var26) {
            ;
         }

         updateDatenbank.executeSql("ALTER TABLE `ftpsync_del` ADD `statusDB` INT NOT NULL AFTER `status`;");
         updateDatenbank.executeSql("ALTER TABLE `clients` ADD `online` INT NOT NULL AFTER `typ`;");
         (new TabelleClients()).updateOnline(1);

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES (\'ZyklischerEMailAuftrag\', \'0\', \'" + var36 + "\');");
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'TerminDisplay_AnzeigeAnazahlVeranstaltungen\', \'6\', \'" + var36 + "\'), (\'TerminDisplay_AnzeigeAnazahlVeranstaltungListe\', \'10\', \'" + var36 + "\'), (\'TerminDisplay_AnzeigeDauerVeranstaltungen\', \'60\', \'" + var36 + "\'), (\'TerminDisplay_AnzeigeDauerUhr\', \'10\', \'" + var36 + "\'), (\'TerminDisplay_AnzeigenLetzenEinsatz\', \'1\', \'" + var36 + "\'), (\'TerminDisplay_HintergrundBild\', \'\', \'" + var36 + "\'), (\'TerminDisplay_HintergrundBildAktivieren\', \'0\', \'" + var36 + "\'), (\'TerminDisplay_LetzterEinsatzOrtAnzeigen\', \'1\', \'" + var36 + "\'), (\'Anwesenheitsliste_DirektDruck_HeaderText\', \'Anwesenheitsliste -  Einsatz ________________\', \'" + var36 + "\'), (\'Anwesenheitsliste_DirektDruck_HeaderText_MitDatum\', \'1\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.21\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.21");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.21")) {
         var32 = "CREATE TABLE IF NOT EXISTS `schulung_details` (`id` int(11) NOT NULL,`schulungID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`datum` text NOT NULL,`raumID` int(11) NOT NULL,`inhalt` text NOT NULL, `fahrzeug1` int(11) NOT NULL,`fahrzeug2` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var37 = "CREATE TABLE IF NOT EXISTS `schulung_gruppe` (`id` int(11) NOT NULL, `name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var35 = "CREATE TABLE IF NOT EXISTS `schulung_gruppen_mandanten` (`gruppenID` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var39 = "CREATE TABLE IF NOT EXISTS `schulung_raum` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var40 = "CREATE TABLE IF NOT EXISTS `schulung_teilnehmer` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`teilnehmerMandant` int(11) NOT NULL,`schulungID` int(11) NOT NULL,`status` int(11) NOT NULL,`statusGrund` text NOT NULL,`statusDatum` text NOT NULL,`statusZeit` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         var43 = "CREATE TABLE IF NOT EXISTS `schulung` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`name` text NOT NULL,`gruppenID` int(11) NOT NULL,`minTeilnehmer` int(11) NOT NULL,`maxTeilnehmer` int(11) NOT NULL,`startDatum` text NOT NULL, `endeDatum` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql(var37);
         updateDatenbank.executeSql(var35);
         updateDatenbank.executeSql(var39);
         updateDatenbank.executeSql(var40);
         updateDatenbank.executeSql(var43);
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `verbandsmaterial` TEXT NOT NULL AFTER `abstusiset`;");

         for(var49 = 1; var49 < mantantenAnzahl + 1; ++var49) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'schulungAdminModul\', \'0\', \'" + var49 + "\'), (\'schulungClientModul\', \'0\', \'" + var49 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.22\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.22");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.22")) {
         updateDatenbank.executeSql("ALTER TABLE `karte_strassen` ADD `GPS_N` TEXT NOT NULL AFTER `koordinaten`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_strassen` ADD `GPS_O` TEXT NOT NULL AFTER `GPS_N`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_artikel` ADD `loeschkenner` INT NOT NULL AFTER `EAN`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.23\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.23");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.23")) {
         var32 = "CREATE TABLE  IF NOT EXISTS `einsatz_uebernahme` (`id` int(11) NOT NULL,`straße` text NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`stichwort` text NOT NULL,`stadtteil` text NOT NULL,`einsatznummerOffiziell` text NOT NULL,`beschreibung` text NOT NULL,`meldung` text NOT NULL,`uebernommen` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);

         try {
            updateDatenbank.executeSql("ALTER TABLE `karte_hydranten` ADD `hausnummerID` INT NOT NULL AFTER `hausnummer`;");
         } catch (SQLException var25) {
            ;
         }

         updateDatenbank.executeSql("ALTER TABLE `karte_hydranten` ADD `GPS_N` TEXT NOT NULL AFTER `nennweite`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_hydranten` ADD `GPS_O` TEXT NOT NULL AFTER `GPS_N`;");

         try {
            updateDatenbank.executeSql("ALTER TABLE `karte_hydranten` ADD `mandantID` INT NOT NULL AFTER `GPS_O`;");
         } catch (SQLException var24) {
            ;
         }

         try {
            updateDatenbank.executeSql("ALTER TABLE `karte_strassen` ADD `mandantID` INT NOT NULL AFTER `PLZ`;");
         } catch (SQLException var23) {
            ;
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.24\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.24");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.24")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.25\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.25");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.25")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `kettensaege2` TEXT NOT NULL AFTER `kettensaege`;");
         } catch (Exception var22) {
            ;
         }

         try {
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `trennschleifer` TEXT NOT NULL AFTER `kettensaege2`;");
         } catch (Exception var21) {
            ;
         }

         try {
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `elektrosaege` TEXT NOT NULL AFTER `trennschleifer`;");
         } catch (Exception var20) {
            ;
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.26\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.26");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.26")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.27\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.27");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.27")) {
         updateDatenbank.executeSql("ALTER TABLE `fahrzeuge` ADD `homepageBild` TEXT NOT NULL AFTER `sortierung`;");
         updateDatenbank.executeSql("ALTER TABLE `fahrzeuge` ADD `homepageLink` TEXT NOT NULL AFTER `homepageBild`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `staat` TEXT NOT NULL AFTER `beruf`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `staat` TEXT NOT NULL AFTER `beruf`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_artikel` ADD `hersteller` TEXT NOT NULL AFTER `name`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_artikel` ADD `typ` TEXT NOT NULL AFTER `hersteller`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_artikel` ADD `pruefung` TEXT NOT NULL AFTER `beschreibung`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `tauchpumpe` TEXT NOT NULL AFTER `verbandsmaterial`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `hebekissen` TEXT NOT NULL AFTER `tauchpumpe`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `luefter` TEXT NOT NULL AFTER `hebekissen`;");
         var32 = "CREATE TABLE  IF NOT EXISTS `fahrzeugakte_kommentar` ( `id` int(11) NOT NULL, `datum` text NOT NULL, `zeit` text NOT NULL, `kommentar` text NOT NULL, `mandantID` int(11) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `lager_zugewiesen` ADD `produktion` TEXT NOT NULL AFTER `ort`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_zugewiesen` ADD `identifikation` TEXT NOT NULL AFTER `produktion`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_zugewiesen` ADD `inDienst` TEXT NOT NULL AFTER `identifikation`;");
         updateDatenbank.executeSql("ALTER TABLE `lager_zugewiesen` ADD `pruefung` TEXT NOT NULL AFTER `inDienst`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.28\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.28");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.28")) {
         updateDatenbank.executeSql("Update berechtigung set name = \'Sonstige Mannstunden\', gruppe = 4 where id = 53 and seite = 2;");
         updateDatenbank.executeSql("Update berechtigung set name = \'Bestandsverwaltung - Lager löschen\', gruppe = 4 where id = 54 and seite = 2;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `straße2` TEXT NOT NULL AFTER `straße`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `stadt` TEXT NOT NULL AFTER `stadtteil`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `sachverhalt` TEXT NOT NULL AFTER `einsatznummerOffiziell`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `route` TEXT NOT NULL AFTER `meldung`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `openFireMapLink` TEXT NOT NULL AFTER `route`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `GPS` TEXT NOT NULL AFTER `straße2`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `googleLink` TEXT NOT NULL AFTER `openFireMapLink`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `straßenID` INT NOT NULL AFTER `id`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `FEP` TEXT NOT NULL AFTER `sachverhalt`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.29\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.29");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.29")) {
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `agteinsatztraining` TEXT NOT NULL AFTER `agttraining`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.30\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.30");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.30")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.31\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.31");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.31")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.32\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.32");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.32")) {
         updateDatenbank.executeSql("ALTER TABLE `atemschutzpass` ADD `einsatzDetails` TEXT NOT NULL AFTER `truppZuordnung`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.33\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.33");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.33")) {
         var32 = "CREATE TABLE  IF NOT EXISTS `karte_objekte` (`id` int(11) NOT NULL,`name` text NOT NULL,`objektNummer` text NOT NULL,`anfahrt` text NOT NULL,`straße` int(11) NOT NULL,`hausnummer` int(11) NOT NULL,`gefaerdungen` text NOT NULL,`beschreibung` text NOT NULL,`GPS_N` text NOT NULL,`GPS_O` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `veranstaltungID` INT NOT NULL AFTER `id`;");
         updateDatenbank.executeSql("ALTER TABLE `einsatz_uebernahme` ADD `FMSObjektID` INT NOT NULL AFTER `veranstaltungID`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.34\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.34");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.34")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `hausnummer` INT NOT NULL AFTER `straße`;");
         } catch (SQLException var19) {
            ;
         }

         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `ansprechpartner` TEXT NOT NULL AFTER `GPS_O`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `bewohner` TEXT NOT NULL AFTER `ansprechpartner`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `personal` TEXT NOT NULL AFTER `bewohner`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `oeffnungszeiten` TEXT NOT NULL AFTER `personal`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.35\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.35");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.35")) {
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `fuehrerscheinAusstelldatum` TEXT NOT NULL AFTER `infoAblaufLKW`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `fuehrerscheinAblaufDatum` TEXT NOT NULL AFTER `fuehrerscheinAusstelldatum`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `fuehrerscheinAblaufC1` TEXT NOT NULL AFTER `fuehrerscheinAblaufDatum`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `fuehrerscheinAblaufC1E` TEXT NOT NULL AFTER `fuehrerscheinAblaufC1`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `fuehrerscheinAblaufCE` TEXT NOT NULL AFTER `fuehrerscheinAblaufC1E`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.36\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.36");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.36")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.37\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.37");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.37")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.38\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.38");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.38")) {
         try {
            updateDatenbank.executeSql("ALTER TABLE `berechtigung` ADD `mandantID` INT NOT NULL AFTER `gruppe`;");
            updateDatenbank.executeSql("update berechtigung set mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         } catch (SQLException var18) {
            ;
         }

         try {
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_gruppe` ADD `berechtigung` INT NOT NULL AFTER `name`;");
            updateDatenbank.executeSql("update mitglieder_gruppe set berechtigung = -1;");
         } catch (SQLException var17) {
            ;
         }

         updateDatenbank.executeSql("INSERT INTO `berechtigung_gruppe_name` (`id`, `name`) VALUES (\'100\', \'Benutzerdefiniert\');");

         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`, `mandantID`) VALUES (0, \'frei0\', 3, 100, " + var36 + "),(1, \'frei1\', 3, 100, " + var36 + "),(2, \'frei2\', 3, 100, " + var36 + "),(3, \'frei3\', 3, 100, " + var36 + "),(4, \'frei4\', 3, 100, " + var36 + "),(5, \'frei5\', 3, 100, " + var36 + "),(6, \'frei6\', 3, 100, " + var36 + "),(7, \'frei7\', 3, 100, " + var36 + "),(8, \'frei8\', 3, 100, " + var36 + "),(9, \'frei9\', 3, 100, " + var36 + "),(10, \'frei10\', 3, 100, " + var36 + "),(11, \'frei11\', 3, 100, " + var36 + "),(12, \'frei12\', 3, 100, " + var36 + "),(13, \'frei13\', 3, 100, " + var36 + "),(14, \'frei14\', 3, 100, " + var36 + "),(15, \'frei15\', 3, 100, " + var36 + "),(16, \'frei16\', 3, 100, " + var36 + "),(17, \'frei17\', 3, 100, " + var36 + "),(18, \'frei18\', 3, 100, " + var36 + "),(19, \'frei19\', 3, 100, " + var36 + "),(20, \'frei20\', 3, 100, " + var36 + "),(21, \'frei21\', 3, 100, " + var36 + "),(22, \'frei22\', 3, 100, " + var36 + "),(23, \'frei23\', 3, 100, " + var36 + "),(24, \'frei24\', 3, 100, " + var36 + "),(25, \'frei25\', 3, 100, " + var36 + "),(26, \'frei26\', 3, 100, " + var36 + "),(27, \'frei27\', 3, 100, " + var36 + "),(28, \'frei28\', 3, 100, " + var36 + "),(29, \'frei29\', 3, 100, " + var36 + "),(30, \'frei30\', 3, 100, " + var36 + "),(31, \'frei31\', 3, 100, " + var36 + "),(32, \'frei32\', 3, 100, " + var36 + "),(33, \'frei33\', 3, 100, " + var36 + "),(34, \'frei34\', 3, 100, " + var36 + "),(35, \'frei35\', 3, 100, " + var36 + "),(36, \'frei36\', 3, 100, " + var36 + "),(37, \'frei37\', 3, 100, " + var36 + "),(38, \'frei38\', 3, 100, " + var36 + "),(39, \'frei39\', 3, 100, " + var36 + "),(40, \'frei40\', 3, 100, " + var36 + "),(41, \'frei41\', 3, 100, " + var36 + "),(42, \'frei42\', 3, 100, " + var36 + "),(43, \'frei43\', 3, 100, " + var36 + "),(44, \'frei44\', 3, 100, " + var36 + "),(45, \'frei45\', 3, 100, " + var36 + "),(46, \'frei46\', 3, 100, " + var36 + "),(47, \'frei47\', 3, 100, " + var36 + "),(48, \'frei48\', 3, 100, " + var36 + "),(49, \'frei49\', 3, 100, " + var36 + "),(50, \'frei50\', 3, 100, " + var36 + "),(51, \'frei51\', 3, 100, " + var36 + "),(52, \'frei52\', 3, 100, " + var36 + "),(53, \'frei53\', 3, 100, " + var36 + "),(54, \'frei54\', 3, 100, " + var36 + "),(55, \'frei55\', 3, 100, " + var36 + "),(56, \'frei56\', 3, 100, " + var36 + "),(57, \'frei57\', 3, 100, " + var36 + "),(58, \'frei58\', 3, 100, " + var36 + "),(59, \'frei59\', 3, 100, " + var36 + "),(60, \'frei60\', 3, 100, " + var36 + "),(61, \'frei61\', 3, 100, " + var36 + "),(62, \'frei62\', 3, 100, " + var36 + "),(63, \'frei63\', 3, 100, " + var36 + "),(64, \'frei64\', 3, 100, " + var36 + "),(65, \'frei65\', 3, 100, " + var36 + "),(66, \'frei66\', 3, 100, " + var36 + "),(67, \'frei67\', 3, 100, " + var36 + "),(68, \'frei68\', 3, 100, " + var36 + "),(69, \'frei69\', 3, 100, " + var36 + "),(70, \'frei70\', 3, 100, " + var36 + "),(71, \'frei71\', 3, 100, " + var36 + "),(72, \'frei72\', 3, 100, " + var36 + "),(73, \'frei73\', 3, 100, " + var36 + "),(74, \'frei74\', 3, 100, " + var36 + "),(75, \'frei75\', 3, 100, " + var36 + "),(76, \'frei76\', 3, 100, " + var36 + "),(77, \'frei77\', 3, 100, " + var36 + "),(78, \'frei78\', 3, 100, " + var36 + "),(79, \'frei79\', 3, 100, " + var36 + "),(80, \'frei80\', 3, 100, " + var36 + "),(81, \'frei81\', 3, 100, " + var36 + "),(82, \'frei82\', 3, 100, " + var36 + "),(83, \'frei83\', 3, 100, " + var36 + "),(84, \'frei84\', 3, 100, " + var36 + "),(85, \'frei85\', 3, 100, " + var36 + "),(86, \'frei86\', 3, 100, " + var36 + "),(87, \'frei87\', 3, 100, " + var36 + "),(88, \'frei88\', 3, 100, " + var36 + "),(89, \'frei89\', 3, 100, " + var36 + "),(90, \'frei90\', 3, 100, " + var36 + "),(91, \'frei91\', 3, 100, " + var36 + "),(92, \'frei92\', 3, 100, " + var36 + "),(93, \'frei93\', 3, 100, " + var36 + "),(94, \'frei94\', 3, 100, " + var36 + "),(95, \'frei95\', 3, 100, " + var36 + ");");
            var33 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select name from berechtigunggruppe where mandantID = " + var36 + ";"));

            for(anfahrtListe = 0; anfahrtListe < var33.length; ++anfahrtListe) {
               updateDatenbank.executeSql("INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`, `BR88`, `BR89`, `BR90`, `BR91`, `BR92`, `BR93`, `BR94`, `BR95`, `mandantID`) VALUES (" + anfahrtListe + ", 3, \'" + var33[anfahrtListe] + "\', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, " + var36 + ");");
            }
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.39\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.39");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.39")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'facebookAccessToken\', \'\', \'" + var36 + "\'),(\'facebookAutoPostEinsatz\', \'0\', \'" + var36 + "\'),(\'facebookAppID\', \'\', \'" + var36 + "\'), (\'facebookAppGeheimCode\', \'\', \'" + var36 + "\'), (\'facebookPostTemplateEinsatz\', \'+++ Ihre Feuerwehr ist für Sie im Einsatz +++\n<<EINSATZ_NUMMER>> / <<EINSATZ_JAHR>>\nPowered by FeuerwehrManagementSystem - www.feuerwehrmanagementsystem.de\', \'" + var36 + "\'),(\'facebookPostTemplateEinsatzBild\', \'\', \'" + var36 + "\'),(\'facebookEMail\', \'0\', \'" + var36 + "\'), (\'facebookEMailAn1\', \'0\', \'" + var36 + "\'), (\'facebookEMailAn2\', \'0\', \'" + var36 + "\'), (\'facebookEMailAn3\', \'0\', \'" + var36 + "\'),(\'mitgliederGesundheitTab\', \'0\', \'" + var36 + "\'),(\'facebookPostTemplateProtokoll\', \'+++ Nachtrag Einsatz +++\n<<EINSATZ_NUMMER>> / <<EINSATZ_JAHR>>\nPowered by FeuerwehrManagementSystem - www.feuerwehrmanagementsystem.de\', \'" + var36 + "\'),(\'facebookPostTemplateProtokollBild\', \'\', \'" + var36 + "\');");
            updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES (\'facebookAccessTokenExpiereDate\', \'\', \'" + var36 + "\');");
            updateDatenbank.executeSql("update berechtigung set name = \'Facebook-Post Konfiguration\', gruppe = 4 where seite = 2 and id = 55 and mandantID = " + var36 + ";");
            updateDatenbank.executeSql("update berechtigung set name = \'Facebook-API-Key Konfiguration\', gruppe = 4 where seite = 2 and id = 56 and mandantID = " + var36 + ";");
            updateDatenbank.executeSql("update berechtigung set name = \'Facebook-Post Protokoll / Tätigkeitsbericht\', gruppe = 4 where seite = 2 and id = 57 and mandantID = " + var36 + ";");
         }

         var32 = "CREATE TABLE  IF NOT EXISTS `facebook` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`postTyp` text NOT NULL,`postText` text NOT NULL,`fbMessageID` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `krankenkasse` TEXT NOT NULL AFTER `dienstausweisNummer`");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `medikamente` TEXT NOT NULL AFTER `krankenkasse`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `krankheiten` TEXT NOT NULL AFTER `medikamente`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `schwimmabzeichen` TEXT NOT NULL AFTER `krankheiten`;");
         updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `sportabzeichen` TEXT NOT NULL AFTER `schwimmabzeichen`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.40\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.40");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.40")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'Joomla_com_Einsatz_update\', \'/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_update_Einsatz.php\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `pa1` TEXT NOT NULL AFTER `luefter`, ADD `pa2` TEXT NOT NULL AFTER `pa1`, ADD `pa3` TEXT NOT NULL AFTER `pa2`, ADD `pa4` TEXT NOT NULL AFTER `pa3`, ADD `pa5` TEXT NOT NULL AFTER `pa4`, ADD `pa6` TEXT NOT NULL AFTER `pa5`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `schnittschutz` TEXT NOT NULL AFTER `kettensaege2`, ADD `schnittschutz2` TEXT NOT NULL AFTER `schnittschutz`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `luefter2` TEXT NOT NULL AFTER `luefter`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `multileiter` TEXT NOT NULL AFTER `schiebleiter`;");

         try {
            updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `ansprechpartner` TEXT NOT NULL AFTER `GPS_O`;");
            updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `bewohner` TEXT NOT NULL AFTER `ansprechpartner`;");
            updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `personal` TEXT NOT NULL AFTER `bewohner`;");
            updateDatenbank.executeSql("ALTER TABLE `karte_objekte` ADD `oeffnungszeiten` TEXT NOT NULL AFTER `personal`;");
         } catch (SQLException var16) {
            ;
         }

         (new MyProperties(runApplication.arbeitsverzeichnis + "properties/user.properties")).checkPropertiesEntry("osAnmeldung", "false");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.41\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.41");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.41")) {
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `tauchpumpe2` TEXT NOT NULL AFTER `tauchpumpe`;");
         updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `rollgliss` TEXT NOT NULL AFTER `abstusiset`;");
         var32 = "CREATE TABLE IF NOT EXISTS `geraetepruefungID` (`id` int(11) NOT NULL,`stromerzeuger` text NOT NULL,`steckleiter` text NOT NULL,`schiebleiter` text NOT NULL,`multileiter` text NOT NULL,`hydraulik` text NOT NULL,`pumpe` text NOT NULL,`kettensaege` text NOT NULL,`kettensaege2` text NOT NULL,`schnittschutz` text NOT NULL,`schnittschutz2` text NOT NULL,`trennschleifer` text NOT NULL,`elektrosaege` text NOT NULL,`doppelkanister` text NOT NULL,`geraetepruefung_allgem` text NOT NULL,`abstusiset` text NOT NULL,`rollgliss` text NOT NULL, `verbandsmaterial` text NOT NULL, `tauchpumpe` text NOT NULL, `tauchpumpe2` text NOT NULL, `hebekissen` text NOT NULL, `luefter` text NOT NULL, `luefter2` text NOT NULL, `pa1` text NOT NULL, `pa2` text NOT NULL, `pa3` text NOT NULL, `pa4` text NOT NULL, `pa5` text NOT NULL, `pa6` text NOT NULL, `infoEMail` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         (new MyProperties(runApplication.arbeitsverzeichnis + "properties/user.properties")).checkPropertiesEntry("osAnmeldung", "false");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.42\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.42");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.42")) {
         Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Objektakte", "SYSTEM");
         var32 = "CREATE TABLE  IF NOT EXISTS `karte_objekthydranten` (`ID` int(11) NOT NULL,`objektID` int(11) NOT NULL,`hydrantID` int(11) NOT NULL,`entfernung` text NOT NULL,`beschreibung` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'automatischeFrageNachAtemschutzEinsatz\', \'0\', \'" + tabAnfahrt + "\'),(\'google_api_code\', \'\', \'" + tabAnfahrt + "\'), (\'default_location\', \'\', \'" + tabAnfahrt + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.43\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.43");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.43")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 3.44\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 3.44");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 3.44")) {
         JOptionPane.showMessageDialog((Component)null, Konstante.START_RELEASEUPDATE);
         updateDatenbank.executeSql("ALTER TABLE `veranstaltung` ADD `mitgliederGruppe` INT NOT NULL AFTER `kategorie`;");
         updateDatenbank.executeSql("update veranstaltung set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `statistiksonstigeveranstaltung` ADD `mitgliederGruppe` INT NOT NULL AFTER `kategorie`;");
         updateDatenbank.executeSql("update statistiksonstigeveranstaltung set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `statistikmitglieder` ADD `mitgliederGruppe` INT NOT NULL AFTER `erstellung`;");
         updateDatenbank.executeSql("update statistikmitglieder set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `statistikmitglieder` ADD `anzahlGebTage` INT NOT NULL AFTER `alterGes`;");
         updateDatenbank.executeSql("update statistikmitglieder set anzahlGebTage = anzahl;");
         updateDatenbank.executeSql("ALTER TABLE `ausbildung_plan` ADD `mitgliederGruppe` INT NOT NULL AFTER `veranstaltungID`;");
         updateDatenbank.executeSql("update ausbildung_plan set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `ausbildung` ADD `mitgliederGruppe` INT NOT NULL AFTER `veranstaltungID`;");
         updateDatenbank.executeSql("update ausbildung set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `urlaub` ADD `mitgliederGruppe` INT NOT NULL AFTER `mitgliederID`;");
         updateDatenbank.executeSql("update urlaub set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.00\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.00");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.00")) {
         updateDatenbank.executeSql("ALTER TABLE `jahresberichte` ADD `mitgliederGruppe` INT NOT NULL AFTER `statistiken`;");
         updateDatenbank.executeSql("update jahresberichte set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `protokoll` ADD `mitgliederGruppe` INT NOT NULL AFTER `erstelldatum`;");
         updateDatenbank.executeSql("update protokoll set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("ALTER TABLE `briefe` ADD `mitgliederGruppe` INT NOT NULL AFTER `template`;");
         updateDatenbank.executeSql("update briefe set mitgliederGruppe = 1;");
         updateDatenbank.executeSql("update `berechtigung` set `name` = \'Facebook-API-Key Konfiguration\', `gruppe` = 4 where `id` = 56 and `seite` = 2;");
         updateDatenbank.executeSql("update `berechtigung` set `name` = \'Facebook-Post Protokoll / Tätigkeitsbericht\', `gruppe` = 4 where `id` = 57 and `seite` = 2;");
         updateDatenbank.executeSql("update `berechtigung` set `name` = \'Mitglieder Verfügbarkeit\', `gruppe` = 1 where `id` = 58 and `seite` = 2;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.01\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.01");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.01")) {
         var32 = "CREATE TABLE IF NOT EXISTS `aao` (`id` int(11) NOT NULL,`stichwortID` int(11) NOT NULL,`strassenID` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`reihenfolge` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);
         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` CHANGE `hausnummer` `hausnummer` TEXT NOT NULL;");
         updateDatenbank.executeSql("Update berechtigung set name = \'AAO editieren\', gruppe = 6 where id = 59 and seite = 2;");
         updateDatenbank.executeSql("Update berechtigung set name = \'Schulung beantragen\', gruppe = 1 where id = 60 and seite = 2;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.02\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.02");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.02")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.03\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.03");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.03")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.04\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.04");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.04")) {
         var32 = "CREATE TABLE IF NOT EXISTS `karte_anfahrt` (`id` int(11) NOT NULL,`strassenID` int(11) NOT NULL,`objektID` int(11) NOT NULL,`anfahrt` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
         updateDatenbank.executeSql(var32);

         for(tabAnfahrt = 1; tabAnfahrt < mantantenAnzahl + 1; ++tabAnfahrt) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'externeDatenbankFürKartendaten\', \'0\', \'" + tabAnfahrt + "\'),(\'externeKartenDB_Typ\', \'SSH\', \'" + tabAnfahrt + "\'), (\'externeKartenDatenbankPort\', \'\', \'" + tabAnfahrt + "\'), (\'externeKartenDatenbankIP\', \'\', \'" + tabAnfahrt + "\'), (\'externeKartenDatenbankName\', \'\', \'" + tabAnfahrt + "\'), (\'externeKartenDatenbankUser\', \'00000yu7dVcPb7f9evozU7Uw8UMYamYzbDPgIkJ\', \'" + tabAnfahrt + "\'), (\'externeKartenDatenbankPasswort\', \'00000yu7dVcPb7f9evozU7Uw8UMYamYzbDPgIkJ\', \'" + tabAnfahrt + "\'), (\'externeKartenSSHUser\', \'00000yu7dVcPb7f9evozU7Uw8UMYamYzbDPgIkJ\', \'" + tabAnfahrt + "\'), (\'externeKartenSSHPasswort\', \'00000yu7dVcPb7f9evozU7Uw8UMYamYzbDPgIkJ\', \'" + tabAnfahrt + "\'), (\'externeKartenSSHServer\', \'\', \'" + tabAnfahrt + "\'), (\'externeKartenSSHServerPort\', \'\', \'" + tabAnfahrt + "\'), (\'externeKartenSSHTunnel\', \'\', \'" + tabAnfahrt + "\');");
         }

         runApplication.EINSTELLUNGEN = tabEinstellungen.getAllEinstellungen();
         TabelleAnfahrt var57 = new TabelleAnfahrt();
         var34 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT anfahrt FROM `karte_strassen` WHERE anfahrt != \'\' order by id;"));
         var38 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `karte_strassen` WHERE anfahrt != \'\' order by id;"));

         for(var41 = 0; var41 < var34.length; ++var41) {
            Anfahrt var50 = new Anfahrt();
            var50.setId(var57.getNextNummer());
            var50.setObjektID(0);
            var50.setStrassenID(Integer.parseInt(var38[var41]));
            var50.setAnfahrt(var34[var41]);
            var57.insert(var50);
         }

         objektAnfahrtListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT anfahrt FROM `karte_objekte` WHERE anfahrt != \'\' order by id;"));
         var42 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `karte_objekte` WHERE anfahrt != \'\' order by id;"));

         for(var49 = 0; var49 < objektAnfahrtListe.length; ++var49) {
            Anfahrt var56 = new Anfahrt();
            var56.setId(var57.getNextNummer());
            var56.setObjektID(Integer.parseInt(var42[var49]));
            var56.setStrassenID(0);
            var56.setAnfahrt(objektAnfahrtListe[var49]);
            var57.insert(var56);
         }

         updateDatenbank.executeSql("ALTER TABLE `karte_objekte` DROP `anfahrt`;");
         updateDatenbank.executeSql("ALTER TABLE `karte_strassen` DROP `anfahrt`;");
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.05\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.05");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.05")) {
         for(var36 = 1; var36 < mantantenAnzahl + 1; ++var36) {
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES (\'externeKartenDatenbankLokalesBackup\', \'0\', \'" + var36 + "\');");
         }

         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.06\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.06");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.06")) {
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.07\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.07");
      }

      dbVersion = tabEinstellungen.getVersion();
      if(dbVersion.equals("Version: 4.07")) {
         updateDatenbank.executeSql("update mandant set id = " + (String)runApplication.PROPERTIES.get("MandantID"));
         updateDatenbank.executeSql("Update einstellungen set wert = \'Version: 4.08\' where `key` = \'version\';");
         logging.logInfo("Aktualisierung fertig --> Version: 4.08");
      }

      Joomla.nutzungFMS("Update beendet! --> Aktuelle Version ist jetzt: Version: 4.08");
      JOptionPane.showMessageDialog((Component)null, Konstante.UPDATE_ERFOLGREICH_INSTALLIERT);
      StartBildschirmAO.startDialogText.setText("Update Installation Abgeschlossen... Lade neue Einstellungen...");
      runApplication.EINSTELLUNGEN = tabEinstellungen.getAllEinstellungen();
      runApplication.EINSTELLUNGEN_GESPEICHERT = (new TabelleEinstellungen_gespeichert()).getAllEinstellungen();
      StartBildschirmAO.startDialogText.setText("Update Installation Abgeschlossen... Lade Hauptprogramm...");
   }
}
