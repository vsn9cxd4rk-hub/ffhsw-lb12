/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.MyProperties
 *  utilities.RandomGenerator
 *  utilities.RandomGenerator$Mode
 *  utilities.SbcUtils
 *  utilities.WinRegistry
 *  utilities.hash
 */
package run.update;

import ao.utils.StartBildschirmAO;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleClients;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleKeyStore;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import go.Mitgliederlaufbahn;
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
import utilities.joomla.Joomla;

public class Update {
    public static void initUpdate() {
        try {
            if (!new TabelleEinstellungen().getVersion().equals("Version: 3.21")) {
                logging.logInfo((Object)"Datenbankversion und Programm Version Stimmen nicht \u00fcberein");
                logging.logInfo((Object)"System wird aktualisiert");
                StartBildschirmAO.startDialogText.setText("Installiere Updates... Bitte haben sie einen Moment Geduld...");
                double startZeit = System.currentTimeMillis();
                Update.executeUpdate();
                double endZeit = (double)System.currentTimeMillis() - startZeit;
                logging.logInfo((Object)("Update installiert in: " + endZeit + " ms"));
            } else {
                logging.logInfo((Object)"System ist aktuell");
            }
        }
        catch (Exception e2) {
            JOptionPane.showMessageDialog(null, Konstante.UPDATE_NICHT_ERFOLGREICH_INSTALLIERT, "Fehlermeldung", 0);
            logging.logError((Object)"Fehler beim Update");
            logging.logPrintStackTrace((Exception)e2);
        }
    }

    private static void executeUpdate() throws Exception {
        int newID;
        int i;
        String[] liste;
        UpdateDatenbank updateDatenbank = new UpdateDatenbank();
        TabelleEinstellungen tabEinstellungen = new TabelleEinstellungen();
        int mantantenAnzahl = updateDatenbank.executeSqlWithReturnINT("Select count(*) from mandant;");
        logging.logInfo((Object)("MandatenAnzahl geladen: " + mantantenAnzahl));
        String dbVersion = tabEinstellungen.getVersion();
        if (dbVersion.equals("Version: 1.09")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.10"));
            updateDatenbank.executeSql("INSERT INTO einsatz_kategorie (`id` ,`name`) VALUES ('5', 'Rettungsdienst'),('6', 'First Responder');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `maengelmeldung_kommentar` (`mangelID` int(11) NOT NULL,`kommentarID` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL, `kommentar` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder ADD kommentar TEXT NOT NULL");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('vCardSeperator', ';');");
            updateDatenbank.executeSql("ALTER TABLE briefe ADD template INT NOT NULL ");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.10' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.10");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.10")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.11"));
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `keystore` (`key` text NOT NULL,`wert` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO `keystore` (`key`, `wert`) VALUES ('Nummer1', '" + WinRegistry.readString((int)-2147483647, (String)"Software\\FeuerwehrManagementSystem", (String)"Nummer1") + "'),('Nummer2', '" + WinRegistry.readString((int)-2147483647, (String)"Software\\FeuerwehrManagementSystem", (String)"Nummer2") + "');");
            try {
                WinRegistry.deleteKey((int)-2147483647, (String)"Software\\FeuerwehrManagementSystem");
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (new TabelleKeyStore().get("Nummer1").equals("null")) {
                updateDatenbank.executeSql("delete from keystore");
                updateDatenbank.executeSql("INSERT INTO `keystore` (`key`, `wert`) VALUES ('Nummer1', '00001yxoCXmQdi29wWX71sjOT5DCZQSAr3nAv5in'),('Nummer2', '00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ');");
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.11' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.11");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.11")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.12"));
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `ablaufLKW` TEXT NOT NULL ");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoAblaufLKW` INT NOT NULL ");
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES ('ablaufLKWF\u00fchrerscheinViaEMail', '0');");
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES ('ablaufLKWAnzeigen', '0');");
            updateDatenbank.executeSql("ALTER TABLE `einsatz` ADD `einsatzleiter` INT NOT NULL ");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.12' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.12");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.12")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.13"));
            updateDatenbank.executeSql("Update keystore set wert = '" + hash.createHashCode((String)"0") + "' where `key` = 'Nummer1';");
            updateDatenbank.executeSql("Update keystore set wert = '00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ' where `key` = 'Nummer2';");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.13' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.13");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.13")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.14"));
            updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key` ,`wert`) VALUES ('automatischesUpdate', '1');");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.14' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.14");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.14")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.15"));
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.15' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.15");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.15")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.16"));
            updateDatenbank.executeSql("UPDATE lager SET name = 'Virtuelles- / Defektteile- / Ausmusterlager' WHERE id = 9000;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder ADD loeschkenner INT NOT NULL ");
            updateDatenbank.executeSql("ALTER TABLE einsatz ADD staerkeZF INT NOT NULL ");
            updateDatenbank.executeSql("ALTER TABLE einsatz ADD einsatzleiterBF TEXT NOT NULL ");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('einsatzleiterBF', '0');");
            new File("data/Bestandsliste").mkdir();
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.16' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.16");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.16")) {
            logging.logInfo((Object)("Starte Update: " + dbVersion + " --> Version: 1.17"));
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('bundesland', 'Nordrhein-Westfalen');");
            liste = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from dienstgrad where id > 10"));
            i = 0;
            while (i < liste.length) {
                newID = i + 51;
                updateDatenbank.executeSql("Update dienstgrad set id = " + newID + " where id = " + liste[i]);
                updateDatenbank.executeSql("Update mitglieder set dienstgrad = " + newID + " where dienstgrad = " + liste[i]);
                ++i;
            }
            updateDatenbank.executeSql("Update mitglieder set dienstgrad = 11 where dienstgrad = 10");
            updateDatenbank.executeSql("Update mitglieder set dienstgrad = 10 where dienstgrad = 9");
            updateDatenbank.executeSql("delete from dienstgrad where id between 1 and 50;");
            updateDatenbank.executeSql("INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`) VALUES(1, 'FMA', 'Feuerwehrmannanw\u00e4rter'),(2, 'FM', 'Feuerwehrmann'),(3, 'OFM', 'Oberfeuerwehrmann'),(4, 'HFM', 'Hauptfeuerwehrmann'),(5, 'UBM', 'Unterbrandmeister'),(6, 'BM', 'Brandmeister'),(7, 'OBM', 'Oberbrandmeister'),(8, 'HBM', 'Hauptprandmeister'),(9, 'HBM', 'Hauptprandmeister m. Zulage'),(10, 'BI', 'Brandinspektor'),(11, 'BOI', 'Brandoberinspecktor'),(12, 'StBI', 'Stadtbrandinspektor'),(13, 'BAR', 'Brandamtsrat'),(14, 'BOAR', 'Brandoberamtsrat'),(15, 'BR', 'Brandrat'),(16, 'OBR', 'Oberbrandrat'),(17, 'BD', 'Branddirektor'),(18, 'OBD', 'Oberbranddirektor'),(19, 'DdBF', 'Direktor der Berufsfeuerwehr');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `atemschutzpass` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`zeit` int(11) NOT NULL,`einsatzart` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `atemschutzpass_einsatzart` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO `atemschutzpass_einsatzart` (`id`, `name`) VALUES (1, 'PA'),(2, 'Filter'),(3, 'CSA');");
            updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `BR68` INT NOT NULL ,ADD `BR69` INT NOT NULL ,ADD `BR70` INT NOT NULL ,ADD `BR71` INT NOT NULL ,ADD `BR72` INT NOT NULL ,ADD `BR73` INT NOT NULL ,ADD `BR74` INT NOT NULL ,ADD `BR75` INT NOT NULL");
            updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR68` = '1', `BR69` = '1', `BR70` = '1', `BR71` = '1', `BR72` = '1', `BR73` = '1', `BR74` = '1', `BR75` = '1' WHERE `berechtigunggruppe`.`id` = 1;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.17' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.17");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.17")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.18' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.18");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.18")) {
            updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `rh` INT NOT NULL AFTER `GF`");
            updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `rs` INT NOT NULL AFTER `rh`");
            updateDatenbank.executeSql("ALTER TABLE fahrzeugeinteilung_temp ADD `ra` INT NOT NULL AFTER `rs`");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `dlkmaschi` INT NOT NULL AFTER `Maschi`");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `korbsteuerung` INT NOT NULL AFTER `dlkmaschi`");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung_temp` ADD `ZF` INT NOT NULL AFTER `GF`");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('abrechnungModul', '1');");
            new File("data/Abrechnung").mkdir();
            liste = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from fahrzeug_beschreibung where id > 11"));
            i = 0;
            while (i < liste.length) {
                newID = i + 51;
                System.out.println(newID);
                updateDatenbank.executeSql("Update fahrzeug_beschreibung set id = " + newID + " where id = " + liste[i]);
                updateDatenbank.executeSql("Update fahrzeuge set beschreibung = " + newID + " where beschreibung = " + liste[i]);
                ++i;
            }
            updateDatenbank.executeSql("INSERT INTO `fahrzeug_beschreibung` (`id`, `beschreibung`) VALUES (12, 'Einsatzleitwagen'),(13, 'Rettungswagen'),(14, 'Krankentransportwagen');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_konto` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO `abrechnung_konto` (`id`, `name`) VALUES (1, 'SYSTEM');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_artikelklassen` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO `abrechnung_artikelklassen` (`id`, `name`) VALUES(1, 'Einsatz'),(2, 'Dienstabend'),(3, 'BSW'),(4, 'Sonstige'),(100, 'SYSTEM'),(101, 'RABATT');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung_artikel` (`id` int(11) NOT NULL,`name` text NOT NULL,`klasse` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`wert` int(11) NOT NULL,`rabattwert` int(11) NOT NULL,`mwst` int(11) NOT NULL,`berechnungsart` int(11) NOT NULL,`berechnungsart2` int(11) NOT NULL,`rabattart` int(11) NOT NULL,`aktiv` int(11) NOT NULL,`von` text NOT NULL,`bis` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO `abrechnung_artikel` (`id`, `name`, `klasse`, `buchungskonto`, `zahlungsart`, `wert`, `rabattwert`, `mwst`, `berechnungsart`, `berechnungsart2`, `rabattart`, `aktiv`, `von`, `bis`) VALUES(4000, 'Verg\u00fctung Brandsicherheitswache', 3, 1, 2, 0, 0, 1, 1, 1, 1, 0, '2015-01-01', '2099-12-31'),(4001, 'Verg\u00fctung Einsatz', 1, 1, 2, 0, 0, 1, 1, 1, 0, 0, '2015-01-01', '2099-12-31'),(4002, 'Verg\u00fctung Dienstabend', 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31'),(4003, 'Verg\u00fctung Sonstige', 4, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31');");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `abrechnung` (`id` int(11) NOT NULL,`abrechnungID` int(11) NOT NULL,`artikelID` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`wert` int(11) NOT NULL,`menge` int(11) NOT NULL,`datum` text NOT NULL,`status` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            String[] veranstaltungsListeEinsatz = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 1 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            String[] veranstaltungsDatumEinsatz = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 1 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            int v = 0;
            while (v < veranstaltungsListeEinsatz.length) {
                updateDatenbank.executeSql("UPDATE statistikeinsatz SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(veranstaltungsDatumEinsatz[v])) + " WHERE veranstaltungID = " + veranstaltungsListeEinsatz[v] + ";");
                ++v;
            }
            String[] veranstaltungsListeSonstige = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 2 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            String[] veranstaltungsDatumSonstige = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 2 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            int v2 = 0;
            while (v2 < veranstaltungsListeSonstige.length) {
                updateDatenbank.executeSql("UPDATE statistiksonstigeveranstaltung SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(veranstaltungsDatumSonstige[v2])) + " WHERE veranstaltungID = " + veranstaltungsListeSonstige[v2] + ";");
                ++v2;
            }
            String[] veranstaltungsListeSonstige2 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie > 4 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            String[] veranstaltungsDatumSonstige2 = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie > 4 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            int v3 = 0;
            while (v3 < veranstaltungsListeSonstige2.length) {
                updateDatenbank.executeSql("UPDATE statistiksonstigeveranstaltung SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(veranstaltungsDatumSonstige2[v3])) + " WHERE veranstaltungID = " + veranstaltungsListeSonstige2[v3] + ";");
                ++v3;
            }
            String[] veranstaltungsListeBSW = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM `veranstaltung` WHERE kategorie = 3 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            String[] veranstaltungsDatumBSW = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT datum FROM `veranstaltung` WHERE kategorie = 3 and datum between '2015-01-01' and '2020-12-31' order by id;"));
            int v4 = 0;
            while (v4 < veranstaltungsListeBSW.length) {
                updateDatenbank.executeSql("UPDATE statistikbsw SET wochentag = " + TimeCalculation.wochentagErmitteln(TimeCalculation.parseDateForGUI(veranstaltungsDatumBSW[v4])) + " WHERE veranstaltungID = " + veranstaltungsListeBSW[v4] + ";");
                ++v4;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.19' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.19");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.19")) {
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `g30` TEXT NOT NULL");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoG30` INT NOT NULL ");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.20' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.20");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.20")) {
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `doppelkanister` TEXT NOT NULL AFTER `kettensaege`");
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `infoEMail` INT NOT NULL");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES ('geraetepruefungViaEMail', '0');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES ('offeneMaengelAnzeigen', '1');");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoTuev` INT NOT NULL");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoSP` INT NOT NULL");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoService` INT NOT NULL");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES ('fahrzeugUntersuchungViaEMail', '1');");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.21' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.21");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.21")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.22' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.22");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.22")) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES ('m\u00e4ngelmeldungViaEMailVersenden', '1');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`)VALUES ('gebAnzeigeModus', '1');");
            updateDatenbank.executeSql("ALTER TABLE `abrechnung` ADD `umbuchungID` INT NOT NULL");
            String[] gebListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT gebdatum FROM mitglieder where gebdatum != '' order by gebdatum;"));
            int g = 0;
            while (g < gebListe.length) {
                updateDatenbank.executeSql("update mitglieder set gebdatum = '" + TimeCalculation.parseDateForDatabase(gebListe[g]) + "' where gebdatum = '" + gebListe[g] + "';");
                ++g;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.23' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.23");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.23")) {
            String newBildFolder;
            File bild;
            JOptionPane.showMessageDialog(null, "HINWEIS:\n\nDas Update auf die Version: 1.24 wird einige Zeit in Anspruch nehmen.\nSie werden informiert sobald das Update abgeschlossen ist!");
            new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties").checkPropertiesEntry("ClientID", (Object)RandomGenerator.generate((int)20, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHANUMERIC));
            new File("data/Ausbildungsunterlagen").mkdir();
            new File("data/KarteBilder").mkdir();
            new File("data/KarteBilder/gro\u00df").mkdir();
            new File("data/KarteBilder/klein").mkdir();
            updateDatenbank.executeSql("ALTER TABLE `geraetepruefung` ADD `abstusiset` TEXT NOT NULL AFTER `geraetepruefung_allgem`");
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `mitgliederakte_kommentar` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`kommentar` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('vorbelegungBSWTreffen', '12:15'), ('vorbelegungBSWVeranstaltungStart', '15:30'), ('vorbelegungBSWEnde', '18:15');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('EinsatzBerichtArt', 'Word Schnittstelle');");
            String sql81 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_elemente` (`id` int(11) NOT NULL,`gruppe` text NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql82 = "INSERT INTO `einsatz_bericht_elemente` (`id`, `gruppe`, `name`) VALUES(1, 'EinsatzArt', 'Kleinbrand'),(2, 'EinsatzArt', 'Mittelbrand'),(3, 'EinsatzArt', 'Gro\u00dfbrand'),(4, 'EinsatzArt', 'Kaminbrand'),(5, 'EinsatzArt', 'Gasaustr\u00f6mung'),(6, 'EinsatzArt', 'Blinder Alarm'),(7, 'EinsatzArt', 'B\u00f6swilliger Alarm'),(8, 'EinsatzArt', 'Verkehrsunfall'),(9, 'EinsatzArt', 'Verkehrsst\u00f6rung'),(10, 'EinsatzArt', 'Einsturz'),(11, 'EinsatzArt', 'Mensch in Notlage'),(12, 'EinsatzArt', 'Gefahrgut (GSG)'),(13, 'EinsatzArt', '\u00d6lspur'),(14, 'EinsatzArt', 'Tier in Notlage'),(15, 'EinsatzArt', 'Tiertranssport'),(16, 'EinsatzArt', 'Tierkadaver'),(17, 'EinsatzArt', 'Betriebsunfall'),(18, 'EinsatzArt', 'TH Baum'),(19, 'EinsatzArt', 'TH Wasser'),(20, 'EinsatzArt', 'keine Angaben'),(101, 'Stelle', 'Keller'),(102, 'Stelle', 'Erdgeschlo\u00df'),(103, 'Stelle', 'Obergescho\u00df'),(104, 'Stelle', 'Dachgescho\u00df'),(105, 'Stelle', 'eingeschloss. Geb\u00e4ude'),(106, 'Stelle', 'Baustelle'),(107, 'Stelle', 'Freigel\u00e4nde'),(108, 'Stelle', 'Auf dem Wasser'),(109, 'Stelle', 'Stra\u00dfe'),(110, 'Stelle', 'Autobahn'),(111, 'Stelle', 'Landstra\u00dfe'),(112, 'Stelle', 'Kraftfahrstra\u00dfe'),(113, 'Stelle', 'keine Angaben'),(201, 'Objekt', 'Wohngeb\u00e4ude'),(202, 'Objekt', 'Verwaltungsgeb\u00e4ude'),(203, 'Objekt', 'Landwirdschaftl. Geb\u00e4ude'),(204, 'Objekt', 'Industriebetrieb'),(205, 'Objekt', 'gewerbl. Betrieb'),(206, 'Objekt', 'Fahrzeug'),(207, 'Objekt', 'Wald'),(208, 'Objekt', 'Feld'),(209, 'Objekt', 'Grasnarbe'),(210, 'Objekt', 'keine Angaben'),(301, 'Alamierung', 'Digitaler Meldeempf\u00e4nger (DME)'),(302, 'Alamierung', 'Leitstelle Feuerwehr'),(303, 'Alamierung', 'Leitstelle Polizei'),(304, 'Alamierung', 'Telefon'),(305, 'Alamierung', 'Sirene'),(301, 'Ausdehnung', 'Auf Entstehungrum begrenzt'),(302, 'Ausdehnung', 'vor Eintreffen (auf andere R\u00e4ume \u00fcbgergeriffen)'),(303, 'Ausdehnung', 'vor Eintreffen (auf andere Geb\u00e4ude \u00fcbgergeriffen)'),(304, 'Ausdehnung', 'w\u00e4hrend der Brandbek\u00e4mpfung (auf andere R\u00e4ume \u00fcbgergeriffen)'),(305, 'Ausdehnung', 'w\u00e4hrend der Brandbek\u00e4mpfung (auf andere Geb\u00e4ude \u00fcbgergeriffen)'),(306, 'Ausdehnung', 'keine Angaben');";
            String sql83 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_daten` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`einsatzID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`einsatzArt` int(11) NOT NULL,`stelle` int(11) NOT NULL,`objekt` int(11) NOT NULL,`eigentuemerName` text NOT NULL,`eigentuemerAnschrift` text NOT NULL,`eigentuemerTelefon` text NOT NULL,`verursacherName` text NOT NULL,`verursacherAnschrift` text NOT NULL,`verursacherTelefon` text NOT NULL,`alamierung` int(11) NOT NULL,`meldenderName` text NOT NULL,`meldenderAnschrift` text NOT NULL,`meldenderTelefon` text NOT NULL,`lage` text NOT NULL,`verlauf` text NOT NULL,`eingesetzteGeraete` text NOT NULL,`verbrauchWasser` text NOT NULL,`verbrauchSchaum` text NOT NULL,`verbrauchPulver` text NOT NULL,`verbrauchBindemittel` text NOT NULL,`vorEintreffenGeloescht` int(11) NOT NULL,`schnellangriff` int(11) NOT NULL,`crohr` text NOT NULL,`brohr` text NOT NULL,`kleinloeschgeraet` text NOT NULL,`tragbareLeitern` int(11) NOT NULL,`atemschutzgeraet` text NOT NULL,`fluchthauben` text NOT NULL,`belueftungsgeraet` text NOT NULL,`rettungsgeraet` int(11) NOT NULL,`ausdehnung` int(11) NOT NULL,`entstehungsursache` text NOT NULL,`verletzte` text NOT NULL,`gerettete` text NOT NULL,`tote` text NOT NULL,`schadenhoehe` text NOT NULL,`brandwacheFahrzeug` int(11) NOT NULL,`staerke` text NOT NULL,`dauer` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql84 = "CREATE TABLE IF NOT EXISTS `ftpsync` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL,`ordner` text NOT NULL,`status` int(11) NOT NULL,`groesse` BIGINT NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql85 = "INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`) VALUES(1, 'SYSTEM', '', 'data', 0),(2, 'SYSTEM', '', 'data/Templates', 0),(3, 'SYSTEM', '', 'data/Papierkorb', 0),(4, 'SYSTEM', '', 'data/Eigene Dateien', 0),(5, 'SYSTEM', '', 'data/Mitgliederakte', 0),(6, 'SYSTEM', '', 'data/Fahrzeugakte', 0),(7, 'SYSTEM', '', 'data/EMail', 0),(8, 'SYSTEM', '', 'data/EMail/Anhang', 0),(9, 'SYSTEM', '', 'data/EMail/Anhang/Gesendet', 0),(10, 'SYSTEM', '', 'data/EMail/Anhang/Entwurf', 0),(11, 'SYSTEM', '', 'data/EMail/Anhang/Empfangende', 0),(12, 'SYSTEM', '', 'data/EMail/Temp', 0),(13, 'SYSTEM', '', 'data/EMail/Temp/original_nachricht', 0),(14, 'SYSTEM', '', 'data/DBBACKUP', 0),(15, 'SYSTEM', '', 'data/Bestandsliste', 0),(16, 'SYSTEM', '', 'data/Abrechnung', 0),(17, 'SYSTEM', '', 'data/Ausbildungsunterlagen', 0),(18, 'SYSTEM', 'data/Templates/Einsatzbericht.docx', '', 0),(19, 'SYSTEM', 'data/Templates/Einsatzbericht.xml', '', 0),(20, 'SYSTEM', 'data/Templates/M\u00e4ngelmeldung.docx', '', 0),(21, 'SYSTEM', 'data/Templates/M\u00e4ngelmeldung.xml', '', 0),(22, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0),(23, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0);";
            String sql86 = "CREATE TABLE IF NOT EXISTS `ftpsync_del` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL, `status` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql81);
            updateDatenbank.executeSql(sql82);
            updateDatenbank.executeSql(sql83);
            updateDatenbank.executeSql(sql84);
            updateDatenbank.executeSql(sql85);
            updateDatenbank.executeSql(sql86);
            if (!runApplication.EINSTELLUNGEN.get("EinsatzBericht").startsWith("data/Templates")) {
                File eBericht = new File(runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
                String eBerichtNewPath = "data/Templates/Einsatzbericht/" + eBericht.getName();
                Utils.kopiereDateiInDataOrdner(eBericht, eBerichtNewPath, "data/Templates/Einsatzbericht");
                updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = '" + eBerichtNewPath + "' WHERE `key` = 'EinsatzBericht';");
            }
            if (!runApplication.EINSTELLUNGEN.get("briefkopf").startsWith("images")) {
                File briefkopf = new File(runApplication.EINSTELLUNGEN.get("briefkopf"));
                String briefkopfNewPath = "data/Templates/Briefkopf/" + briefkopf.getName();
                Utils.kopiereDateiInDataOrdner(briefkopf, briefkopfNewPath, "data/Templates/Briefkopf/");
                updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = '" + briefkopfNewPath + "' WHERE `key` = 'briefkopf';");
            }
            if (!runApplication.EINSTELLUNGEN.get("verdienstausfall").startsWith("data/Templates")) {
                File verdienstausfall = new File(runApplication.EINSTELLUNGEN.get("verdienstausfall"));
                String verdienstausfallNewPath = "data/Templates/Verdienstausfall/" + verdienstausfall.getName();
                Utils.kopiereDateiInDataOrdner(verdienstausfall, verdienstausfallNewPath, "data/Templates/Verdienstausfall");
                updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = '" + verdienstausfallNewPath + "' WHERE `key` = 'verdienstausfall';");
            }
            if (!runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldung").startsWith("data/Templates")) {
                File mangel = new File(runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldung"));
                String mangelNewPath = "data/Templates/M\u00e4ngelmeldung/" + mangel.getName();
                Utils.kopiereDateiInDataOrdner(mangel, mangelNewPath, "data/Templates/M\u00e4ngelmeldung");
                updateDatenbank.executeSql("UPDATE einstellungen SET `wert` = '" + mangelNewPath + "' WHERE `key` = 'm\u00e4ngelmeldung';");
            }
            String[] karteID = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id from karte_strassen order by id;"));
            String[] karteBilderGro\u00df = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT bild from karte_strassen order by id;"));
            String[] karteBilderKlein = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT bild2 from karte_strassen order by id;"));
            int g = 0;
            while (g < karteBilderGro\u00df.length) {
                bild = new File(karteBilderGro\u00df[g]);
                newBildFolder = "data/KarteBilder/gro\u00df/" + bild.getName();
                Utils.kopiereDateiInDataOrdner(bild, newBildFolder, "data/KarteBilder/gro\u00df");
                updateDatenbank.executeSql("Update karte_strassen set bild = '" + newBildFolder + "' where id = " + karteID[g]);
                ++g;
            }
            g = 0;
            while (g < karteBilderKlein.length) {
                bild = new File(karteBilderKlein[g]);
                newBildFolder = "data/KarteBilder/klein/" + bild.getName();
                Utils.kopiereDateiInDataOrdner(bild, newBildFolder, "data/KarteBilder/klein");
                updateDatenbank.executeSql("Update karte_strassen set bild2 = '" + newBildFolder + "' where id = " + karteID[g]);
                ++g;
            }
            new TabelleFTPSync().deleteAll();
            Utils.rekatalogisiereDateien(String.valueOf(runApplication.arbeitsverzeichnis) + "data");
            runApplication.EINSTELLUNGEN = new TabelleEinstellungen().getAllEinstellungen();
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.24' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.24");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.24")) {
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set name = 'Bungalow' where id = 105;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.25' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.25");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.25")) {
            Utils.ordnerErstellen("data/" + SbcUtils.timeStamp((String)"yyyy") + "/Schichten", "SYSTEM");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('Schichtplaner', '1');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('Fahrtenbuch', '1');");
            String sql87 = "CREATE TABLE IF NOT EXISTS `schicht_mitglieder` (`schichtID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql88 = "CREATE TABLE IF NOT EXISTS `schicht_gruppen_mitglieder` (`gruppenID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql89 = "CREATE TABLE IF NOT EXISTS `schicht_gruppe` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql90 = "CREATE TABLE IF NOT EXISTS `fahrtenbuch` (`id` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`datumVon` text NOT NULL,`zeitVon` text NOT NULL,`datumBis` text NOT NULL,`zeitBis` text NOT NULL,`kmBeginn` int(11) NOT NULL,`kmEnde` int(11) NOT NULL,`distance` int(11) NOT NULL,`tanken` text NOT NULL,`pumpenbetrieb` text NOT NULL,`sonstiges` text NOT NULL,`fahrer` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql91 = "CREATE TABLE IF NOT EXISTS `mitglieder_laufbahn` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datum` text NOT NULL,`art` text NOT NULL,`alterDienstgrad` int(11) NOT NULL,`neuerDienstgrad` int(11) NOT NULL,`lehrgang` int(11) NOT NULL,`ue` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql92 = "CREATE TABLE IF NOT EXISTS `statistiklehrgang` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`lehrgangID` int(11) NOT NULL,`dauer` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql93 = "CREATE TABLE IF NOT EXISTS `schicht` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`name` text NOT NULL,`datumVon` text NOT NULL,`uhrVon` text NOT NULL,`datumBis` text NOT NULL,`uhrBis` text NOT NULL,`von` int(11) NOT NULL,`bis` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql87);
            updateDatenbank.executeSql(sql88);
            updateDatenbank.executeSql(sql89);
            updateDatenbank.executeSql(sql90);
            updateDatenbank.executeSql(sql91);
            updateDatenbank.executeSql(sql92);
            updateDatenbank.executeSql(sql93);
            updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `BR76` INT NOT NULL ,ADD `BR77` INT NOT NULL ,ADD `BR78` INT NOT NULL,ADD `BR79` INT NOT NULL,ADD `BR80` INT NOT NULL,ADD `BR81` INT NOT NULL,ADD `BR82` INT NOT NULL,ADD `BR83` INT NOT NULL,ADD `BR84` INT NOT NULL,ADD `BR85` INT NOT NULL,ADD `BR86` INT NOT NULL,ADD `BR87` INT NOT NULL");
            updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR76` = '1', `BR77` = '1', `BR78` = '1', `BR79` = '1', `BR80` = '1', `BR81` = '1', `BR82` = '1', `BR83` = '1', `BR84` = '1', `BR85` = '1', `BR86` = '1', `BR87` = '1' WHERE `berechtigunggruppe`.`id` = 1;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.26' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.26");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.26")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.27' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.27");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.27")) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('SichtbarkeitVeranstaltungVergangenheit', '1'), ('SichtbarkeitVeranstaltungZukunft', '12')");
            updateDatenbank.executeSql("Update berechtigunggruppe set BR81 = 1, BR82 = 1, BR83 = 1, BR84 = 1;");
            updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `ue` INT NOT NULL ");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.28' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.28");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.28")) {
            String sql94 = "CREATE TABLE IF NOT EXISTS `systemwarnung` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`info` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('JoomlaVeranstaltungSenden', '0'), ('JoomlaLink', ''), ('JoomlaAusbildungsplanSenden', '0'), ('AlwaysOnTop', '0'), ('JoomlaEinsatzkomponente', '0'), ('JoomlaEinsatzkomponenteVisible', '0'), ('JoomlaEinsatzkomponenteEMail', '0'), ('JoomlaEinsatzkomponenteEMailAn1', '0'), ('JoomlaEinsatzkomponenteEMailAn2', '0'), ('JoomlaEinsatzkomponenteEMailAn3', '0'), ('terminVersandtViaEMailConfig', '1'),('joomlaEinsatzkomponenteConfig', '1'), ('joomlaEinsatzkomponenteSecretKey', ''), ('joomlaEinsatzkomponenteStichwort', '1'),('Joomla_mod_Veranstaltung', '/modules/mod_Veranstaltung/veranstaltung.php'),('Joomla_mod_Veranstaltung_update', '/modules/mod_Veranstaltung/veranstaltung_update.php'),('Joomla_mod_VeranstaltungKategorie', '/modules/mod_Veranstaltung/veranstaltungKategorie.php'),('Joomla_mod_Veranstaltung_delete', '/modules/mod_Veranstaltung/veranstaltung_delete.php'),('Joomla_mod_Ausbildungsplan_delete', '/modules/mod_Ausbildungsplan/ausbildungsplan_delete.php'),('Joomla_mod_VeranstaltungKategorie_delete', '/modules/mod_Veranstaltung/veranstaltungKategorie_delete.php'),('Joomla_mod_AusbildungKategorie_delete', '/modules/mod_Ausbildungsplan/AusbildungKategorie_delete.php'),('Joomla_mod_Ausbildungsplan', '/modules/mod_Ausbildungsplan/ausbildungsplan.php'),('Joomla_mod_AusbildungKategorie', '/modules/mod_Ausbildungsplan/ausbildungKategorie.php'),('Joomla_com_Einsatz', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway.php'),('Joomla_com_Einsatz_Freischalten', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_freischalten.php');");
            updateDatenbank.executeSql(sql94);
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.29' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.29");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.29")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.30' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.30");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.30")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.31' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.31");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.31")) {
            try {
                updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `ue` INT NOT NULL");
            }
            catch (Exception sql94) {
                // empty catch block
            }
            updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `loeschbar` INT NOT NULL");
            updateDatenbank.executeSql("ALTER TABLE `lehrgang_kategorie` ADD `loeschkenner` INT NOT NULL");
            updateDatenbank.executeSql("UPDATE lehrgang_kategorie SET loeschbar = 1 WHERE id IN ( 1,2,12,26,6,8,15,19,20,21,22,23,24,25,27);");
            updateDatenbank.executeSql("ALTER TABLE `user` ADD `loeschkenner` INT NOT NULL");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `gaswartung` TEXT NOT NULL AFTER `service`");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeug_untersuchung` ADD `infoGas` INT NOT NULL");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('VerdienstausfallBerichtArt', 'Word Schnittstelle'), ('M\u00e4ngelBerichtArt', 'Word Schnittstelle')");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.32' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.32");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.32")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.33' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.33");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.33")) {
            updateDatenbank.executeSql("UPDATE berechtigunggruppe set BR86 = 1");
            updateDatenbank.executeSql("UPDATE berechtigunggruppe set BR85 = 1 where id = 1");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.34' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.34");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.34")) {
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `ftpsync_error` (`datei` text NOT NULL,`ordner` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.35' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.35");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.35")) {
            updateDatenbank.executeSql("UPDATE fahrzeug_beschreibung SET beschreibung = 'Teleskopmast' WHERE id =5;");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('Urlaubsplaner', '1');");
            String sql96 = "CREATE TABLE IF NOT EXISTS `urlaub` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datumVon` text NOT NULL,`datumBis` text NOT NULL, `loeschkenner` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql96);
            String[] jahre = Utils.listToArray(new TabelleJahr().getAllVerf\u00fcgbarenJahre());
            int j = 0;
            while (j < jahre.length) {
                Utils.ordnerErstellen("data/" + jahre[j] + "/Schichten", "SYSTEM");
                ++j;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.36' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.36");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.36")) {
            updateDatenbank.executeSql("ALTER TABLE `ftpsync_error` ADD `clientID` TEXT NOT NULL FIRST");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.37' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.37");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.37")) {
            updateDatenbank.executeSql("Update ftpsync set status = 0, clientID = 'SYSTEM' where datei = 'data/Templates/Einsatzbericht.docx';");
            updateDatenbank.executeSql("Update ftpsync set status = 0, clientID = 'SYSTEM' where datei = 'data/Templates/Einsatzbericht.xml';");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('emailTemplateEinsatzbericht', '');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('emailTemplateEinsatzberichtAN', '');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('emailTemplateEinsatzberichtCC', '');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('emailTemplateEinsatzberichtBCC', '');");
            updateDatenbank.executeSql("ALTER TABLE `email_entwurf` CHANGE `anhang` `anhang` TEXT NOT NULL ");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.38' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.38");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.38")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.39' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.39");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.39")) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('globaleEMailEinheitsf\u00fchrungAktiviert', '0');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('globaleEMailGer\u00e4tewarteAktiviert', '0');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('globaleEMailEinheitsf\u00fchrung', '');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('globaleEMailGer\u00e4tewarte', '');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('Statistik2', '0');");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.40' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.40");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.40")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.41' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.41");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.41")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.42' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.42");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.42")) {
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('EinsatznummerIstPflicht', '0');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('EinsatzLeiterBFIstPflicht', '0');");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_ausbildungsplan/ausbildungsplan_delete.php' where `key` = 'Joomla_mod_Ausbildungsplan_delete';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_ausbildungsplan/AusbildungKategorie_delete.php' where `key` = 'Joomla_mod_AusbildungKategorie_delete';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_ausbildungsplan/ausbildungsplan.php' where `key` = 'Joomla_mod_Ausbildungsplan';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_ausbildungsplan/ausbildungKategorie.php' where `key` = 'Joomla_mod_AusbildungKategorie';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_veranstaltung/veranstaltung.php' where `key` = 'Joomla_mod_Veranstaltung';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_veranstaltung/veranstaltung_update.php' where `key` = 'Joomla_mod_Veranstaltung_update';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_veranstaltung/veranstaltungKategorie.php' where `key` = 'Joomla_mod_VeranstaltungKategorie';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_veranstaltung/veranstaltung_delete.php' where `key` = 'Joomla_mod_Veranstaltung_delete';");
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_veranstaltung/veranstaltungKategorie_delete.php' where `key` = 'Joomla_mod_VeranstaltungKategorie_delete';");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.43' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.43");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.43")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.44' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.44");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.44")) {
            String sql97 = "CREATE TABLE IF NOT EXISTS `bef\u00f6rderung_konfiguration` (`id` int(11) NOT NULL,`dienstgradID` int(11) NOT NULL,`dienstgradVoraussetzung` int(11) NOT NULL,`zeit` int(11) NOT NULL,`nurZeitBefoerderung` int(11) NOT NULL,`letzteStufe` int(11) NOT NULL,`auslassen` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql98 = "CREATE TABLE IF NOT EXISTS `bef\u00f6rderung_erforderlich` (`id` int(11) NOT NULL, `lehrgangID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql("ALTER TABLE `lehrgangsmeldung` ADD `art` TEXT NOT NULL;");
            updateDatenbank.executeSql(sql97);
            updateDatenbank.executeSql(sql98);
            String[] ids = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT * FROM mitglieder_laufbahn WHERE `id` > 11000"));
            int newMinID = updateDatenbank.executeSqlWithReturnINT("SELECT max(id) FROM `mitglieder_laufbahn` WHERE id < 11000") + 1;
            int i2 = 0;
            while (i2 < ids.length) {
                updateDatenbank.executeSql("Update mitglieder_laufbahn set mitgliederID = " + ids[i2] + " where id = " + ids[i2]);
                updateDatenbank.executeSql("Update mitglieder_laufbahn set id = " + newMinID + " where id = " + ids[i2]);
                ++newMinID;
                ++i2;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 1.45' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 1.45");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 1.45")) {
            JOptionPane.showMessageDialog(null, Konstante.START_RELEASEUPDATE);
            String sql99 = "CREATE TABLE IF NOT EXISTS `mitglieder_history` (`changeDate` text NOT NULL,`changeTime` text NOT NULL,`benutzer` text NOT NULL, `id` int(11) NOT NULL,`mitgliederGruppe` int(11) NOT NULL,`anrede` int(11) NOT NULL,`name` text NOT NULL,`vorname` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefonPrivat` text NOT NULL,`telefonMobil` text NOT NULL,`telefonArbeit` text NOT NULL,`email` text NOT NULL,`email2` text NOT NULL,`dienstgrad` int(11) NOT NULL,`ausserDienst` int(11) NOT NULL,`mitgliedSeit` int(4) NOT NULL,`gebDatum` text NOT NULL,`kommentar` text NOT NULL,`loeschkenner` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql100 = "CREATE TABLE IF NOT EXISTS `dateisystem` (`id` int(11) NOT NULL, `dateiStream` longblob NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql101 = "CREATE TABLE IF NOT EXISTS `mandant` (`id` int(11) NOT NULL,`name` text NOT NULL, `bf` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql102 = "INSERT INTO mandant (`id` ,`name`, `bf`) VALUES ('1', '" + RandomGenerator.generate((int)5, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHA_BIG_SIGNS) + "', 0);";
            String sql103 = "CREATE TABLE IF NOT EXISTS `clients` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`alias` text NOT NULL, `zugelassen` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql99);
            updateDatenbank.executeSql(sql100);
            updateDatenbank.executeSql(sql101);
            updateDatenbank.executeSql(sql102);
            updateDatenbank.executeSql(sql103);
            String[] alleClienten = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT clientID FROM `ftpsync` where clientID != 'SYSTEM' group by clientID"));
            int c = 0;
            while (c < alleClienten.length) {
                updateDatenbank.executeSql("INSERT INTO clients (`id` ,`clientID`,`alias`, `zugelassen`) VALUES (1, '" + alleClienten[c] + "', '" + InetAddress.getLocalHost() + "', 1);");
                ++c;
            }
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`) VALUES ('schutzziel1', '8'), ('schutzziel2', '13'), ('headerPrint', '1'), ('footerPrint', '0'), ('modulVeranstaltung', '1'), ('modulAusbildungsplan', '1'), ('modulFahrzeugeinteilung', '1');");
            updateDatenbank.executeSql("ALTER TABLE ftpsync ADD `statusDB` INT NOT NULL AFTER `status` ");
            updateDatenbank.executeSql("UPDATE ftpsync set statusDB = 1 where ordner != '';");
            updateDatenbank.executeSql("Update einstellungen set wert = '1' where `key` = 'Statistik2';");
            String[] alleTabellen = Utils.listToArray(updateDatenbank.executeSqlWithReturn("show tables from " + runApplication.PROPERTIES.get("DatenbankName") + ";"));
            int i3 = 0;
            while (i3 < alleTabellen.length) {
                logging.logInfo((Object)alleTabellen[i3]);
                if (alleTabellen[i3].equals("mandant") || alleTabellen[i3].equals("atemschutzpass_einsatzart") || alleTabellen[i3].equals("fahrzeug_beschreibung")) {
                    logging.logInfo((Object)"Es wird keine MandantID in dieser Tabelle ben\u00f6tigt...");
                } else {
                    updateDatenbank.executeSql("ALTER TABLE " + alleTabellen[i3] + " ADD `mandantID` INT NOT NULL ");
                    updateDatenbank.executeSql("Update " + alleTabellen[i3] + " set mandantID = 1");
                }
                ++i3;
            }
            updateDatenbank.executeSql("ALTER TABLE maengelmeldung_kommentar ADD `user` TEXT NOT NULL AFTER `kommentar` ");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `eMailDeaktiv` INT NOT NULL AFTER `loeschkenner`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `eMailDeaktiv` INT NOT NULL AFTER `loeschkenner`;");
            String sql105 = "CREATE TABLE IF NOT EXISTS `statistikmitglieder` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`alterGes` int(11) NOT NULL,`anzahl` int(11) NOT NULL,`erstellung` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql105);
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('emailTemplateM\u00e4ngelmeldungAN', '', '1'), ('emailTemplateM\u00e4ngelmeldungCC', '', '1'), ('emailTemplateM\u00e4ngelmeldungBCC', '', '1'), ('emailTemplateM\u00e4ngelmeldung', '', '1');");
            updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`)VALUES ('verdienstausfallOptionen', '1', '1');");
            MyProperties programmeinstellungen = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementsystem.properties");
            programmeinstellungen.checkPropertiesEntry("MandantID", (Object)"1");
            programmeinstellungen.checkPropertiesEntry("BlobActiv", (Object)"false");
            if (runApplication.PROPERTIES.get("DB_TYP").equals("SSH")) {
                programmeinstellungen.checkPropertiesEntry("FTPUploadActiv", (Object)"true");
            } else {
                programmeinstellungen.checkPropertiesEntry("FTPUploadActiv", (Object)"false");
            }
            runApplication.PROPERTIES = runApplication.lesePropertieDatei(programmeinstellungen);
            runApplication.createMitgliederStatistik(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.00' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.00");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.00")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.01' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.01");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.01")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.02' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.02");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.02")) {
            updateDatenbank.executeSql("ALTER TABLE jahresberichte ADD `autoBericht` INT NOT NULL AFTER `erstelldatum`");
            updateDatenbank.executeSql("Update jahresberichte set autoBericht = 1 where title like 'Automatischer erstellter Bericht vom%';");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert`, `mandantID`) VALUES ('eMailName', '', " + m + ");");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.03' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.03");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.03")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.04' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.04");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.04")) {
            updateDatenbank.executeSql("Update einstellungen set wert = '/modules/mod_ausbildungsplan/ausbildungKategorie_delete.php' where `key` = 'Joomla_mod_AusbildungKategorie_delete';");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.05' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.05");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.05")) {
            updateDatenbank.executeSql("UPDATE lehrgang_kategorie set art = 'F\u00fc' where id in (1,2,3);");
            updateDatenbank.executeSql("ALTER TABLE mitglieder_untersuchung ADD `g41` TEXT NOT NULL AFTER `infoG30` ,ADD `g42` TEXT NOT NULL AFTER `g41` ");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('zeilenh\u00f6heDruck', '30', " + m + "), ('zeilenh\u00f6heAnsicht', '30', " + m + ");");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.06' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.06");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.06")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.07' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.07");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.07")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.08' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.08");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.08")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.09' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.09");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.09")) {
            try {
                updateDatenbank.executeSql("ALTER TABLE jahresberichte ADD `autoBericht` INT NOT NULL AFTER `erstelldatum`");
            }
            catch (Exception m) {
                // empty catch block
            }
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('bswHitliste', '1', " + m + "), ('getakteteInternetverbindung', '0', " + m + "), ('onlineStatus', '1', " + m + ");");
                ++m;
            }
            updateDatenbank.executeSql("CREATE TABLE IF NOT EXISTS `php` (`id` int(11) NOT NULL,`typ` text NOT NULL,`adresse` text NOT NULL,`parameter` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.10' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.10");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.10")) {
            String sql107 = "CREATE TABLE IF NOT EXISTS `protokoll` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`title` text NOT NULL,`protokolltext` text NOT NULL,`erstelldatum` text NOT NULL,`mandantID` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql107);
            updateDatenbank.executeSql("ALTER TABLE berechtigunggruppe ADD `BR88` INT NOT NULL AFTER `BR87` ,ADD `BR89` INT NOT NULL AFTER `BR88` ,ADD `BR90` INT NOT NULL AFTER `BR89` ,ADD `BR91` INT NOT NULL AFTER `BR90` ,ADD `BR92` INT NOT NULL AFTER `BR91` ,ADD `BR93` INT NOT NULL AFTER `BR92` ,ADD `BR94` INT NOT NULL AFTER `BR93` ,ADD `BR95` INT NOT NULL AFTER `BR94`;");
            updateDatenbank.executeSql("Update berechtigunggruppe set BR88 = 1, BR89 = 1,BR90 = 1,BR91 = 1,BR92 = 1,BR93 = 1,BR94 = 1,BR95 = 1 where id = 1;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.11' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.11");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.11")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES ('JoomlaEinsatzKomponenteNurAlamierung\u00dcbertragen', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.12' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.12");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.12")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES ('JoomlaEinsatzKomponenteEinsatzBericht\u00dcbermitteln', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.13' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.13");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.13")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES ('Joomla_com_Einsatz_Bericht', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_bericht.php', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.14' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.14");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.14")) {
            String sql108 = "CREATE TABLE IF NOT EXISTS `einsatz_organisationen` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`organisationID` int(11) NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql109 = "CREATE TABLE IF NOT EXISTS `organisationen` (`id` int(11) NOT NULL,`name` text NOT NULL,`sortierung` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql108);
            updateDatenbank.executeSql(sql109);
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key` ,`wert` ,`mandantID`) VALUES ('WeitereOrganisationen', '1', '" + m + "'),('Joomla_com_Einsatz_Orgaisation', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_organisation.php', '" + m + "'),('Joomla_com_Einsatz_Fahrzeug', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_fahrzeug.php', '" + m + "'),('Joomla_com_Einsatz_Delete', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_delete.php', '" + m + "'),('FullBackupInZip', '0', '" + m + "'),('FullBackupPath', '', '" + m + "');");
                String sql110 = "INSERT INTO `organisationen` (`id`, `name`, `sortierung`, `mandantID`) VALUES(1, '', 0, " + m + ");";
                updateDatenbank.executeSql(sql110);
                ++m;
            }
            updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR90` = '1'");
            updateDatenbank.executeSql("ALTER TABLE `jahresberichte` ADD `statistiken` TEXT NOT NULL AFTER `dateiname`");
            updateDatenbank.executeSql("update jahresberichte set statistiken = 'leer';");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.15' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.15");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.15")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.16' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.16");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.16")) {
            updateDatenbank.executeSql("Update einsatz_kategorie set name = 'Wachbesetzung' where id = 3;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.17' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.17");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.17")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.18' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.18");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.18")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.19' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.19");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.19")) {
            updateDatenbank.executeSql("ALTER TABLE `einsatz` ADD `fahrzeugID` TEXT NOT NULL AFTER `Fahrzeug`;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.20' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.20");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.20")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.21' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.21");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.21")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.22' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.22");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.22")) {
            updateDatenbank.executeSql("ALTER TABLE mitglieder CHANGE `mitgliedSeit` `mitgliedSeit` TEXT NOT NULL;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder_history CHANGE `mitgliedSeit` `mitgliedSeit` TEXT NOT NULL;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder ADD `hochzeit` TEXT NOT NULL AFTER `gebDatum`;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder_history ADD `hochzeit` TEXT NOT NULL AFTER `gebDatum`;");
            updateDatenbank.executeSql("ALTER TABLE mitglieder_laufbahn ADD `datumVon` TEXT NOT NULL AFTER `mitgliederID`;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('mitgliedSeitFormat', 'yyyy', '" + m + "');");
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('hochzeitFeldFuerMitglieder', '0', '" + m + "');");
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('mitgliedSeitPflichtEintrag', '1', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 2.23' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 2.23");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 2.23")) {
            JOptionPane.showMessageDialog(null, Konstante.START_RELEASEUPDATE);
            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Temp/original_nachricht_unwetter").mkdir();
            String sql111 = "CREATE TABLE `ehrungen_konfiguration` (`id` int(11) NOT NULL,`ehrungID` int(11) NOT NULL, `zeit` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql112 = "CREATE TABLE `email_unwetterwarnung` (`id` int(11) NOT NULL,`sender` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`date` text NOT NULL,`size` int(11) NOT NULL,`anhang` int(11) NOT NULL,`gelesen` int(11) NOT NULL,`art` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql("UPDATE berechtigunggruppe SET `BR93` = '1', `BR94` = '1';");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `ablaufDienstausweis` TEXT NOT NULL AFTER `infoAblaufLKW`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoAblaufDienstausweis` int(11) NOT NULL AFTER `ablaufDienstausweis`;");
            updateDatenbank.executeSql(sql111);
            updateDatenbank.executeSql(sql112);
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('LehrgangEintragenAusMitgliederVerwaltungMode', '0', '" + m + "'),('unwetterwarnungPop3', '', '" + m + "'), ('unwetterwarnungEMail', '', '" + m + "'), ('unwetterwarnungPopPort', '', '" + m + "'), ('unwetterwarnungPasswort', '', '" + m + "'), ('unwetterwarnungSSL', '0', '" + m + "'), ('unwetterwarnungModulAktiv', '0', '" + m + "'),('unwetterwarnungDatumBis', 'null', '" + m + "'), ('unwetterwarnungUrzeitBis', 'null', '" + m + "');");
                String[] lergangKategorieListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select id from lehrgang_kategorie where mandantID = " + m + ";"));
                String[] lergangArtListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select art from lehrgang_kategorie where mandantID = " + m + ";"));
                int l = 0;
                while (l < lergangKategorieListe.length) {
                    updateDatenbank.executeSql("Update mitglieder_laufbahn set art = '" + lergangArtListe[l] + "' where lehrgang = " + lergangKategorieListe[l] + "  and mandantID = " + m + ";");
                    ++l;
                }
                String[] mitgliederListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + m + ";"));
                int i4 = 0;
                while (i4 < mitgliederListe.length) {
                    logging.logInfo((Object)("Mitglied: " + mitgliederListe[i4]));
                    Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                    TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                    String[] lehrgangListe = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select lehrgangID from lehrgang where mitgliedID = " + mitgliederListe[i4] + " and status = 1 and mandantID = " + m + ";"));
                    int l2 = 0;
                    while (l2 < lehrgangListe.length) {
                        if (updateDatenbank.executeSqlWithReturnINT("Select count(*) from mitglieder_laufbahn where lehrgang = " + lehrgangListe[l2] + " and mitgliederID = " + mitgliederListe[i4] + " and mandantID = " + m + ";") == 0) {
                            logging.logInfo((Object)("Lehrgang: " + lehrgangListe[l2]));
                            laufbahn.setId(new TabelleMitglieder_laufbahn().getNextNumber());
                            laufbahn.setAlterDienstgrad(0);
                            laufbahn.setNeuerDienstgrad(0);
                            laufbahn.setArt(tabKategorie.getArt(Integer.parseInt(lehrgangListe[l2])));
                            laufbahn.setDatum("");
                            laufbahn.setDatumVon("");
                            laufbahn.setLehrgang(Integer.parseInt(lehrgangListe[l2]));
                            laufbahn.setMitgliederID(Integer.parseInt(mitgliederListe[i4]));
                            laufbahn.setUe(0);
                            new TabelleMitglieder_laufbahn().insert(laufbahn);
                        } else {
                            logging.logInfo((Object)"Lehrgnag in der Laufbahn bereits vorhanden...");
                        }
                        ++l2;
                    }
                    logging.logInfo((Object)("Fertig f\u00fcr: " + mitgliederListe[i4]));
                    ++i4;
                }
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.00' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.00");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.00")) {
            String sql113 = "CREATE TABLE `berechtigung` (`id` int(11) NOT NULL,`name` text NOT NULL,`gruppe` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql113);
            updateDatenbank.executeSql("ALTER TABLE `bef\u00f6rderung_konfiguration` ADD `dienstZeit` INT NOT NULL AFTER `zeit`;");
            updateDatenbank.executeSql("update `bef\u00f6rderung_konfiguration` set `dienstZeit` = -1;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO einstellungen (`key`, `wert`, `mandantID`) VALUES ('vorwarnungAblaufDienstausweis', '2', '" + m + "'), ('ablaufDienstausweisAnzeigen', '0', '" + m + "'),('ablaufDienstausweisViaEMail', '0', '" + m + "');");
                String[] berechtigungName = new String[]{"Dienstgrad anlegen", "Mitgliederverwaltung", "Mitglied L\u00f6schen / Au\u00dfer Dienst", "Mitglieder Liste + Geburtstagsliste", "Einsatz Liste", "Brandsicherheitswache Liste", "Lehrgangsliste", "Anwesenheitsliste", "Arbeitgeber Liste", "Angeh\u00f6rigen Liste", "Untersuchung Liste", "Beteiligungs\u00fcbersicht Liste", "Mitglieder Bankverbindung Liste", "Veranstaltungsliste", "Dokumentenexplorer", "Einsatz Bericht erstellen", "Verdienstausfallbescheinigung", "Jahresbericht erstellen", "Brief Erstellen", "M\u00e4ngelmeldung", "Anwesenheit Gesamt", "Anwesenheit Einsatz", "Anwesenheit Dienstabend", "Anwesenheit BSW", "Abwesenheitsstatistik", "Einsatzart / Stichwort Statistik", "Ausr\u00fcckezeiten", "Einsatzdauer", "Mannstunden Einsatz", "Einsatz Pro Monat", "Einsatz Pro Stunde", "Einsatz Pro Woche", "BSW Mannstunden", "Fehlalarme", "Beteiligung bei...", "Ausbildungsstatistik", "Fahrzeug Statistik", "Alarmfahrtdauer", "Fahrzeuggruppe anlegen", "Stichwort anlegen", "Veranstaltungskategorie anlegen", "Fahrzeug Au\u00dfer Dienst", "Abwesenheitgrund erstellen", "Programmeinstellungen", "Anwesenheit eintragen", "Abwesenehit eintragen", "Mitglieder Gruppe anlegen", "Ausbildungsinhalte eintragen", "Fahrezugverwaltung", "Ger\u00e4tepr\u00fcfng", "Fahrzeugeinteilung eintragen", "Benutzerverwaltung", "Karte / Einsatzgebiet editieren", "Bef\u00f6r. / Lehrgangsmeldung", "Mitglieder Anrede anlegen", "E-Mail senden/schreiben", "Bestandsverwaltung", "Bestandsverwaltung organisieren", "Bestandsverwaltung Artikel anlegen", "Mitgliederakte", "Fahrzeugakte", "Beteiligungszeit", "Datensicherung", "Ausbildungsplan erstellen", "Ausbildungsplan Liste", "Virtuelles Lager leeren", "M\u00e4ngelmeldung bearbeiten", "Lehrgang anlegen", "Atemschutzpass Eintragen", "Atemschutzpass", "Abrechnung", "Abrechnung - Aktikel", "Abrechnung - Konto", "Abrechnung - Manuelle Verbuchung", "Veranstaltung editieren", "Fahrtenbuch eintrag", "Schichtplaner", "Laufbahn Pflegen", "Fahrtenbuch Liste", "Schichtplaner Liste", "Laufbahn Liste", "Einsatz anlegen", "Dienstabend anlegen", "BSW anlegen", "Sonstige Veranstaltung", "Anwesenheit l\u00f6schen", "Lehrg\u00e4nge Mitgliedern hinzuf\u00fcgen", "Urlaubsplaner", "Protokoll schreiben / aktulisieren", "Protokoll lesen", "Organisationen erstellen", "Stichwort-Kategorie", "Mitgliederlaufbahn Eintrag L\u00f6schen", "Mitgliederverwaltung editieren", "Mitgliederuntersuchung", "Einsatzbericht neu erstellen"};
                int b = 0;
                while (b < berechtigungName.length) {
                    updateDatenbank.executeSql("INSERT INTO `berechtigung` (`id`, `name`, `gruppe`, `mandantID`) VALUES ('" + b + "', '" + berechtigungName[b] + "', '0', '" + m + "');");
                    ++b;
                }
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.01' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.01");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.01")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.02' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.02");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.02")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('pr\u00fcfungDerFahrerlaubnis', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `pruefungDerFahrberechtigung` TEXT NOT NULL AFTER `infoAblaufDienstausweis`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` ADD `infoPruefungDerFahrberechtigung` INT NOT NULL AFTER `pruefungDerFahrberechtigung`;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.03' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.03");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.03")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('darstellungLehrg\u00e4ngeMitgliederverwaltung', 'CheckBox', '" + m + "'),('ablaufFahrberechtigungAnzeigen', '0', '" + m + "'), ('vorwarnungAblaufFahrberechtigung', '2', '" + m + "'),('ablaufFahrberechtigungViaEMail', '0', '" + m + "'),('druckAnwesenheitsListeMode', '1', '" + m + "'),('dienstgradAufAnwesenheitsliste', '1', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `beruf` TEXT NOT NULL AFTER `email2`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `beruf` TEXT NOT NULL AFTER `email2`;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 401 where gruppe = 'Ausdehnung' and id = 301;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 402 where gruppe = 'Ausdehnung' and id = 302;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 403 where gruppe = 'Ausdehnung' and id = 303;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 404 where gruppe = 'Ausdehnung' and id = 304;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 405 where gruppe = 'Ausdehnung' and id = 305;");
            updateDatenbank.executeSql("Update einsatz_bericht_elemente set id = 406 where gruppe = 'Ausdehnung' and id = 306;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 401 where ausdehnung = 301;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 402 where ausdehnung = 302;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 403 where ausdehnung = 303;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 404 where ausdehnung = 304;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 405 where ausdehnung = 305;");
            updateDatenbank.executeSql("Update einsatz_bericht_daten set ausdehnung = 406 where ausdehnung = 306;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.04' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.04");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.04")) {
            String sql115 = "CREATE TABLE `mitglieder_verfuegbarkeit` (`id` int(11) NOT NULL,`mitgliedID` int(11) NOT NULL,`telegrammID` text NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql115);
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `telegrammID` text NOT NULL AFTER `telefonArbeit`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `telegrammID` text NOT NULL AFTER `telefonArbeit`;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('modulMitgliederVerf\u00fcgbarkeit', '1', '" + m + "'),('schfiftgr\u00f6\u00dfeAnwesenheitsliste', '26', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.05' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.05");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.05")) {
            try {
                updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` CHANGE `pruefungDerFahrerlaubnis` `pruefungDerFahrberechtigung` TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;");
                updateDatenbank.executeSql("ALTER TABLE `mitglieder_untersuchung` CHANGE `infoPruefungDerFahrerlaubnis` `infoPruefungDerFahrberechtigung` INT( 11 ) NOT NULL;");
            }
            catch (SQLException sql115) {
                // empty catch block
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.06' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.06");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.06")) {
            updateDatenbank.executeSql("ALTER TABLE `clients` ADD `typ` TEXT NOT NULL AFTER `alias`;");
            updateDatenbank.executeSql("Update clients set typ = 'FMS';");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('bestaetignungFreistellungEinsatzArt', 'PDF (intern)', '" + m + "'), ('bestaetignungFreistellungEinsatz', 'data/Templates/BescheinigungEinsatzTeilnahme.xml', '" + m + "'), ('bestaetignungFreistellungEinsatzAktiv', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.07' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.07");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.07")) {
            updateDatenbank.executeSql("update berechtigung set name = 'Ger\u00e4tepr\u00fcfung' where id = 49;");
            updateDatenbank.executeSql("update berechtigung set name = 'Fahrzeugverwaltung' where id = 48;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `fuehrerscheinNummer` TEXT NOT NULL AFTER `kommentar`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `fahrberechtigungNummer` TEXT NOT NULL AFTER `fuehrerscheinNummer`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `dienstausweisNummer` TEXT NOT NULL AFTER `fahrberechtigungNummer`;");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.08' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.08");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.08")) {
            updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung` ADD `jahr` INT NOT NULL AFTER `veranstaltungID`;");
            updateDatenbank.executeSql("ALTER TABLE `fahrzeugeinteilung` ADD `kategorie` INT NOT NULL AFTER `veranstaltungID`;");
            String[] vIDs = Utils.listToArray(updateDatenbank.executeSqlWithReturn("SELECT veranstaltungID FROM fahrzeugeinteilung group by veranstaltungID;"));
            int v = 0;
            while (v < vIDs.length) {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                int vID = Integer.parseInt(vIDs[v]);
                new TabelleFahrzeugeinteilung().updateKategorie(tabVeranstaltung.getVeranstaltungKategorieID(vID), vID);
                updateDatenbank.executeSql("update fahrzeugeinteilung set jahr = " + tabVeranstaltung.getJahrDerVeranstaltung(vID) + " where veranstaltungID = " + vID + ";");
                ++v;
            }
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('modulEinsatzgebiet', '1', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.09' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.09");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.09")) {
            String sql116 = "CREATE TABLE `einstellungen_gespeichert` (`key` text NOT NULL,`wert` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            updateDatenbank.executeSql(sql116);
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('globaleEMailG25Aktiviert', '0', '" + m + "'), ('globaleEMailG25', '', '" + m + "'), ('globaleEMailG26Aktiviert', '0', '" + m + "'), ('globaleEMailG26', '', '" + m + "'), ('globaleEMailFahrberechtigungAktiviert', '0', '" + m + "'), ('globaleEMailFahrberechtigung', '', '" + m + "'), ('globaleEMailDienstausweisAktiviert', '0', '" + m + "'), ('globaleEMailDienstausweis', '', '" + m + "');");
                updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES ('G25', '', '" + m + "'), ('G26', '', '" + m + "'),('Dienstausweis', '', '" + m + "'), ('Fahrberechtigung', '', '" + m + "'),('G30', '', '" + m + "'),('unwetterwarnungDatumBis', 'null', '" + m + "'), ('unwetterwarnungUhrzeitBis', 'null', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("delete from einstellungen where `key`= 'unwetterwarnungDatumBis';");
            updateDatenbank.executeSql("delete from einstellungen where `key`= 'unwetterwarnungUrzeitBis';");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.10' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.10");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.10")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.11' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.11");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.11")) {
            updateDatenbank.executeSql("ALTER TABLE `fahrzeuge` ADD `trupp` INT NOT NULL AFTER `anhaenger`;");
            updateDatenbank.executeSql("ALTER TABLE `einsatz_berichte` ADD `fahrzeugbelegung` INT NOT NULL AFTER `dateiname`;");
            updateDatenbank.executeSql("ALTER TABLE `einsatz_berichte` ADD `atemschutz` INT NOT NULL AFTER `fahrzeugbelegung`;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('EinsatzBerichtFahrzeugbelegungHinzuf\u00fcgen', '0', '" + m + "'), ('EinsatzBerichtAtemschutzpassHinzuf\u00fcgen', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.12' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.12");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.12")) {
            updateDatenbank.executeSql("ALTER TABLE `atemschutzpass` ADD `truppZuordnung` INT NOT NULL AFTER `einsatzart`;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('EinsatzBerichtEinsatzleiterMitDienstgrad', '1', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.13' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.13");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.13")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.14' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.14");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.14")) {
            try {
                updateDatenbank.executeSql("ALTER TABLE `berechtigung` ADD `seite` INT NOT NULL AFTER `name`;");
                updateDatenbank.executeSql("update berechtigung set seite = 1;");
                updateDatenbank.executeSql("ALTER TABLE `berechtigunggruppe` ADD `seite` INT NOT NULL AFTER `id`;");
                updateDatenbank.executeSql("update berechtigunggruppe set seite = 1;");
            }
            catch (Exception m) {
                // empty catch block
            }
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                if (updateDatenbank.executeSqlWithReturnINT("Select count(*) from berechtigung where seite = 2;") == 0) {
                    updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('feldEintreffenAusblenden', '0', '" + m + "'), ('feldStadtteilAusblenden', '0', '" + m + "'), ('langesDatumsformatUntersuchungsliste', '0', '" + m + "');");
                    updateDatenbank.executeSql("INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`, `mandantID`) VALUES(0, 'Informationsbereich - Termine anzeigen', 2, 0, " + m + "),(1, 'Informationsbereich - Geburtstage anzeigen', 2, 0, " + m + "),(2, 'Informationsbereich - G26 anzeigen', 2, 0, " + m + "),(3, 'Informationsbereich - G25 anzeigen', 2, 0, " + m + "),(4, 'Informationsbereich - G30 anzeigen', 2, 0, " + m + "),(5, 'Informationsbereich - LKW F\u00fchrerschein Ablauf anzeigen', 2, 0, " + m + "),(6, 'Informationsbereich - AGT Training anzeigen', 2, 0, " + m + "),(7, 'Informationsbereich - Ablauf Dienstausweis anzeigen', 2, 0, " + m + "),(8, 'Informationsbereich - Ablauf Fahrberechtigung anzeigen', 2, 0, " + m + "),(9, 'Informationsbereich - Abgelaufene G26 anzeigen', 2, 0, " + m + "),(10, 'Informationsbereich - Abgelaufene G25 anzeigen', 2, 0, " + m + "),(11, 'Informationsbereich - Abgelaufene G30 anzeigen', 2, 0, " + m + "),(12, 'Informationsbereich - Abgelaufenes AGT Training anzeigen', 2, 0, " + m + "),(13, 'Informationsbereich - Abgelaufene LKW F\u00fchrerscheine anzeigen', 2, 0, " + m + "),(14, 'Informationsbereich - Abgelaufene Dienstausweise anzeigen', 2, 0, " + m + "),(15, 'Informationsbereich - Abgelaufene Fahrberechtigung anzeigen', 2, 0, " + m + "),(16, 'Informationsbereich - T\u00dcV anzeigen', 2, 0, " + m + "),(17, 'Informationsbereich - Sicherheitspr\u00fcfung anzeigen', 2, 0, " + m + "),(18, 'Informationsbereich - Fahrzeug Wartung anzeigen', 2, 0, " + m + "),(19, 'Informationsbereich - Gas Wartung anzeigen', 2, 0, " + m + "),(20, 'Informationsbereich - Abgelaufener T\u00dcV anzeigen', 2, 0, " + m + "),(21, 'Informationsbereich - Abgelaufener SP anzeigen', 2, 0, " + m + "),(22, 'Informationsbereich - Abgelaufene Wartung anzeigen', 2, 0, " + m + "),(23, 'Informationsbereich - Abgelaufener Gas Wartung anzeigen', 2, 0, " + m + "),(24, 'Informationsbereich - Ger\u00e4tepr\u00fcfung anzeigen', 2, 0, " + m + "),(25, 'Informationsbereich - M\u00e4ngelmeldungen anzeigen', 2, 0, " + m + "),(26, 'Statistik - Anwesenheit Sonstige Veranstaltung', 2, 0, " + m + "),(27, 'Statistik - Verf\u00fcgbarkeit Einsatz', 2, 0, " + m + "),(28, 'Statistik - Einsatz - Stadtteilstatistik', 2, 0, " + m + "),(29, 'Statistik - Schutzzielstatistik', 2, 0, " + m + "),(30, 'Statistik - Tag / Nacht Eins\u00e4tze', 2, 0, " + m + "),(31, 'Statistik - Fahrzeugbelegung (Einsatz)', 2, 0, " + m + "),(32, 'Statistik - Atemschutzstatistik', 2, 0, " + m + "),(33, 'Statistik - Veranstaltungz\u00e4hlung', 2, 0, " + m + "),(34, 'Statistik - Durchscnittsalter', 2, 0, " + m + "),(35, 'Statistik - Mitgliederzahlen', 2, 0, " + m + "),(36, 'Statistik - Mitglieder Dienstgrad', 2, 0, " + m + "),(37, 'Statistik - Mitglieder Funktionen (Anzahl)', 2, 0, " + m + "),(38, 'Veranstaltung Editieren - Einsatz bearbeiten', 2, 0, " + m + "),(39, 'D.-Explorer - Abrechnungen anzeigen', 2, 0, " + m + "),(40, 'D.-Explorer - Ausbildungunterlagen anzeigen', 2, 0, " + m + "),(41, 'D.-Explorer - Bestandslisten', 2, 0, " + m + "),(42, 'D.-Explorer - Eigene Dateien', 2, 0, " + m + "),(43, 'D.-Explorer - Verdienstausfallb.', 2, 0, " + m + "),(44, 'D.-Explorer - M\u00e4ngelmeldungen', 2, 0, " + m + "),(45, 'D.-Explorer - Lehrgangsmeldungen', 2, 0, " + m + "),(46, 'D.-Expolrer - Fahrzeugeinteilung', 2, 0, " + m + "),(47, 'D.-Explorer - Einsatzbereichte', 2, 0, " + m + "),(48, 'D.-Explorer - Briefe', 2, 0, " + m + "),(49, 'D.-Explorer - Beteiligungs\u00fcbersicht', 2, 0, " + m + "),(50, 'D.-Explorer - Berichte', 2, 0, " + m + "),(51, 'frei51', 2, 0, " + m + "),(52, 'frei52', 2, 0, " + m + "),(53, 'frei53', 2, 0, " + m + "),(54, 'frei54', 2, 0, " + m + "),(55, 'frei55', 2, 0, " + m + "),(56, 'frei56', 2, 0, " + m + "),(57, 'frei57', 2, 0, " + m + "),(58, 'frei58', 2, 0, " + m + "),(59, 'frei59', 2, 0, " + m + "),(60, 'frei60', 2, 0, " + m + "),(61, 'frei61', 2, 0, " + m + "),(62, 'frei62', 2, 0, " + m + "),(63, 'frei63', 2, 0, " + m + "),(64, 'frei64', 2, 0, " + m + "),(65, 'frei65', 2, 0, " + m + "),(66, 'frei66', 2, 0, " + m + "),(67, 'frei67', 2, 0, " + m + "),(68, 'frei68', 2, 0, " + m + "),(69, 'frei69', 2, 0, " + m + "),(70, 'frei70', 2, 0, " + m + "),(71, 'frei71', 2, 0, " + m + "),(72, 'frei72', 2, 0, " + m + "),(73, 'frei73', 2, 0, " + m + "),(74, 'frei74', 2, 0, " + m + "),(75, 'frei75', 2, 0, " + m + "),(76, 'frei76', 2, 0, " + m + "),(77, 'frei77', 2, 0, " + m + "),(78, 'frei78', 2, 0, " + m + "),(79, 'frei79', 2, 0, " + m + "),(80, 'frei80', 2, 0, " + m + "),(81, 'frei81', 2, 0, " + m + "),(82, 'frei82', 2, 0, " + m + "),(83, 'frei83', 2, 0, " + m + "),(84, 'frei84', 2, 0, " + m + "),(85, 'frei85', 2, 0, " + m + "),(86, 'frei86', 2, 0, " + m + "),(87, 'frei87', 2, 0, " + m + "),(88, 'frei88', 2, 0, " + m + "),(89, 'frei89', 2, 0, " + m + "),(90, 'frei90', 2, 0, " + m + "),(91, 'frei91', 2, 0, " + m + "),(92, 'frei92', 2, 0, " + m + "),(93, 'frei93', 2, 0, " + m + "),(94, 'frei94', 2, 0, " + m + "),(95, 'frei95', 2, 0, " + m + ");");
                }
                String[] berechtigungsgruppen = Utils.listToArray(updateDatenbank.executeSqlWithReturn("Select name from berechtigunggruppe where mandantID = " + m + ";"));
                int b = 0;
                while (b < berechtigungsgruppen.length) {
                    updateDatenbank.executeSql("INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`, `BR88`, `BR89`, `BR90`, `BR91`, `BR92`, `BR93`, `BR94`, `BR95`, `mandantID`) VALUES (" + b + ", 2, '" + berechtigungsgruppen[b] + "', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, " + m + ");");
                    ++b;
                }
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.15' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.15");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.15")) {
            updateDatenbank.executeSql("Update berechtigung set name = 'D.-Explorer - Atemschutz' where seite = 2 and id = 51;");
            updateDatenbank.executeSql("Update berechtigung set name = 'Mitgliederliste - Zausatzdaten' where seite = 2 and id = 52;");
            Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Atemschutz", "SYSTEM");
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.16' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.16");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.16")) {
            updateDatenbank.executeSql("ALTER TABLE `berechtigung` DROP `mandantID`;");
            updateDatenbank.executeSql("delete from berechtigung;");
            String sql118 = "INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`) VALUES(0, 'Dienstgrad anlegen', 1, 6),(1, 'Mitgliederverwaltung', 1, 2),(2, 'Mitglied l\u00f6schen / au\u00dfer Dienst', 1, 2),(3, 'Mitgliederliste + Geburtstagsliste', 1, 3),(4, 'Einsatzliste', 1, 3),(5, 'Brandsicherheitswachliste', 1, 3),(6, 'Lehrgangsliste', 1, 3),(7, 'Anwesenheitsliste', 1, 3),(8, 'Arbeitgeberliste', 1, 3),(9, 'Angeh\u00f6rigenliste', 1, 3),(10, 'Untersuchungsliste', 1, 3),(11, 'Beteiligungs\u00fcbersicht', 1, 3),(12, 'Bankverbindungsliste', 1, 3),(13, 'Veranstaltungsliste', 1, 3),(14, 'Dokumentenexplorer', 1, 4),(15, 'Einsatzbericht erstellen', 1, 1),(16, 'Verdienstausfallbescheinigung', 1, 4),(17, 'Jahresbericht erstellen', 1, 4),(18, 'Brief erstellen', 1, 4),(19, 'M\u00e4ngelmeldung', 1, 4),(20, 'Anwesenheit Gesamt', 1, 5),(21, 'Anwesenheit Einsatz', 1, 5),(22, 'Anwesenheit Dienstabend', 1, 5),(23, 'Anwesenheit BSW', 1, 5),(24, 'Abwesenheitsstatistik', 1, 5),(25, 'Einsatzart / Stichwort', 1, 5),(26, 'Ausr\u00fcckezeiten', 1, 5),(27, 'Einsatzdauer', 1, 5),(28, 'Mannstunden Einsatz', 1, 5),(29, 'Einsatz pro Monat', 1, 5),(30, 'Einsatz pro Stunde', 1, 5),(31, 'Einsatz pro Woche', 1, 5),(32, 'BSW Mannstunden', 1, 5),(33, 'Fehlalarme', 1, 5),(34, 'Beteiligung bei...', 1, 5),(35, 'Ausbildungsstatistik', 1, 5),(36, 'Fahrzeug Statistik', 1, 5),(37, 'Alarmfahrtdauer', 1, 5),(38, 'Fahrzeuggruppe anlegen', 1, 6),(39, 'Stichwort anlegen', 1, 6),(40, 'Veranstaltungskategorie anlegen', 1, 6),(41, 'Fahrzeug au\u00dfer Dienst', 1, 2),(42, 'Abwesenheitgrund erstellen', 1, 6),(43, 'Programmeinstellungen', 1, 6),(44, 'Anwesenheit eintragen', 1, 1),(45, 'Abwesenehit eintragen', 1, 1),(46, 'Mitgliedergruppe erstellen / l\u00f6schen', 1, 6),(47, 'Ausbildungsinhalte eintragen', 1, 1),(48, 'Fahrzeugverwaltung', 1, 2),(49, 'Ger\u00e4tepr\u00fcfung', 1, 6),(50, 'Fahrzeugeinteilung eintragen', 1, 1),(51, 'Benutzerverwaltung', 1, 6),(52, 'Karte / Einsatzgebiet editieren', 1, 6),(53, 'Bef\u00f6r. / Lehrgangsmeldung', 1, 3),(54, 'Mitgliederanrede erstellen', 1, 6),(55, 'E-Mail senden/schreiben', 1, 4),(56, 'Bestandsverwaltung', 1, 4),(57, 'Bestandsverwaltung organisieren', 1, 4),(58, 'Bestandsverwaltung Artikel anlegen', 1, 4),(59, 'Mitgliederakte', 1, 2),(60, 'Fahrzeugakte', 1, 2),(61, 'Beteiligungszeit', 1, 5),(62, 'Datensicherung', 1, 6),(63, 'Ausbildungsplan erstellen', 1, 4),(64, 'Ausbildungsplan', 1, 3),(65, 'virtuelles Lager leeren', 1, 4),(66, 'M\u00e4ngelmeldung bearbeiten', 1, 4),(67, 'Lehrgang anlegen', 1, 6),(68, 'Atemschutzpass eintragen', 1, 1),(69, 'Atemschutzpass', 1, 3),(70, 'Abrechnung', 1, 2),(71, 'Abrechnung - Artikel', 1, 2),(72, 'Abrechnung - Konto', 1, 2),(73, 'Abrechnung - manuelle Verbuchung', 1, 2),(74, 'Veranstaltung editieren (24 Std.)', 1, 1),(75, 'Fahrtenbuch eintrag', 1, 1),(76, 'Schichtplaner', 1, 1),(77, 'Mitgliederlaufbahn pflegen', 1, 2),(78, 'Fahrtenbuchliste', 1, 3),(79, 'Schichtplanerliste', 1, 3),(80, 'Mitgliederlaufbahnliste', 1, 3),(81, 'Einsatz anlegen', 1, 1),(82, 'Dienstabend anlegen', 1, 1),(83, 'BSW anlegen', 1, 1),(84, 'Sonstige Veranstaltung', 1, 1),(85, 'Anwesenheit l\u00f6schen', 1, 1),(86, 'Lehrg\u00e4nge Mitgliedern hinzuf\u00fcgen', 1, 2),(87, 'Urlaubsplaner', 1, 1),(88, 'Protokoll / T\u00e4tigkeitsbericht', 1, 4),(89, 'Protokoll lesen', 1, 4),(90, 'Organisationen erstellen', 1, 6),(91, 'Stichwort-Kategorie', 1, 6),(92, 'Mitgliederlaufbahnen editieren', 1, 2),(93, 'Mitgliederverwaltung editieren', 1, 2),(94, 'Mitgliederuntersuchung', 1, 2),(95, 'Einsatzbericht neu erstellen', 1, 1),(0, 'Termine anzeigen', 2, 7),(1, 'Geburtstage anzeigen', 2, 7),(2, 'G26 anzeigen', 2, 7),(3, 'G25 anzeigen', 2, 7),(4, 'G30 anzeigen', 2, 7),(5, 'LKW F\u00fchrerschein Ablauf anzeigen', 2, 7),(6, 'AGT Training anzeigen', 2, 7),(7, 'Ablauf Dienstausweis anzeigen', 2, 7),(8, 'Ablauf Fahrberechtigung anzeigen', 2, 7),(9, 'Abgelaufene G26 anzeigen', 2, 7),(10, 'Abgelaufene G25 anzeigen', 2, 7),(11, 'Abgelaufene G30 anzeigen', 2, 7),(12, 'Abgelaufenes AGT Training anzeigen', 2, 7),(13, 'Abgelaufene LKW F\u00fchrerscheine anzeigen', 2, 7),(14, 'Abgelaufene Dienstausweise anzeigen', 2, 7),(15, 'Abgelaufene Fahrberechtigung anzeigen', 2, 7),(16, 'T\u00dcV anzeigen', 2, 7),(17, 'Sicherheitspr\u00fcfung anzeigen', 2, 7),(18, 'Fahrzeug Wartung anzeigen', 2, 7),(19, 'Gaswartung anzeigen', 2, 7),(20, 'Abgelaufener T\u00dcV anzeigen', 2, 7),(21, 'Abgelaufener SP anzeigen', 2, 7),(22, 'Abgelaufene Wartung anzeigen', 2, 7),(23, 'Abgelaufener Gaswartung anzeigen', 2, 7),(24, 'Ger\u00e4tepr\u00fcfung anzeigen', 2, 7),(25, 'M\u00e4ngelmeldungen anzeigen', 2, 7),(26, 'Anwesenheit Sonstige Veranstaltung', 2, 5),(27, 'Verf\u00fcgbarkeit Einsatz', 2, 5),(28, 'Einsatz - Stadtteilstatistik', 2, 5),(29, 'Schutzzielstatistik', 2, 5),(30, 'Tag / Nacht Eins\u00e4tze', 2, 5),(31, 'Fahrzeugbelegung (Einsatz)', 2, 5),(32, 'Atemschutzstatistik', 2, 5),(33, 'Veranstaltungz\u00e4hlung', 2, 5),(34, 'Durchscnittsalter', 2, 5),(35, 'Mitgliederzahlen', 2, 5),(36, 'Mitglieder Dienstgrad', 2, 5),(37, 'Mitglieder Funktionen (Anzahl)', 2, 5),(38, 'Veranstaltung Editieren - Einsatz bearbeiten', 2, 1),(39, 'D.-Explorer - Abrechnungen anzeigen', 2, 4),(40, 'D.-Explorer - Ausbildungunterlagen anzeigen', 2, 4),(41, 'D.-Explorer - Bestandslisten', 2, 4),(42, 'D.-Explorer - Eigene Dateien', 2, 4),(43, 'D.-Explorer - Verdienstausfallb.', 2, 4),(44, 'D.-Explorer - M\u00e4ngelmeldungen', 2, 4),(45, 'D.-Explorer - Lehrgangsmeldungen', 2, 4),(46, 'D.-Expolrer - Fahrzeugeinteilung', 2, 4),(47, 'D.-Explorer - Einsatzbereichte', 2, 4),(48, 'D.-Explorer - Briefe', 2, 4),(49, 'D.-Explorer - Beteiligungs\u00fcbersicht', 2, 4),(50, 'D.-Explorer - Berichte', 2, 4),(51, 'D.-Explorer - Atemschutz', 2, 4),(52, 'Mitgliederliste - Zausatzdaten', 2, 3),(53, 'Sonstige Mannstunden', 2, 5),(54, 'frei54', 2, 0),(55, 'frei55', 2, 0),(56, 'frei56', 2, 0),(57, 'frei57', 2, 0),(58, 'frei58', 2, 0),(59, 'frei59', 2, 0),(60, 'frei60', 2, 0),(61, 'frei61', 2, 0),(62, 'frei62', 2, 0),(63, 'frei63', 2, 0),(64, 'frei64', 2, 0),(65, 'frei65', 2, 0),(66, 'frei66', 2, 0),(67, 'frei67', 2, 0),(68, 'frei68', 2, 0),(69, 'frei69', 2, 0),(70, 'frei70', 2, 0),(71, 'frei71', 2, 0),(72, 'frei72', 2, 0),(73, 'frei73', 2, 0),(74, 'frei74', 2, 0),(75, 'frei75', 2, 0),(76, 'frei76', 2, 0),(77, 'frei77', 2, 0),(78, 'frei78', 2, 0),(79, 'frei79', 2, 0),(80, 'frei80', 2, 0),(81, 'frei81', 2, 0),(82, 'frei82', 2, 0),(83, 'frei83', 2, 0),(84, 'frei84', 2, 0),(85, 'frei85', 2, 0),(86, 'frei86', 2, 0),(87, 'frei87', 2, 0),(88, 'frei88', 2, 0),(89, 'frei89', 2, 0),(90, 'frei90', 2, 0),(91, 'frei91', 2, 0),(92, 'frei92', 2, 0),(93, 'frei93', 2, 0),(94, 'frei94', 2, 0),(95, 'frei95', 2, 0);";
            String sql121 = "CREATE TABLE `berechtigung_gruppe_name` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            String sql122 = "INSERT INTO `berechtigung_gruppe_name` (`id`, `name`) VALUES ('1', 'Veranstaltungen / Anwesenheit'), ('2', 'Mitglieder- / Fahrzeugverwaltung'), ('3', 'Listen'), ('4', 'Berichte / Dokumente'), ('5', 'Statistik'), ('6', 'Optionen / Verwaltung'), ('7', 'Informationsbereich');";
            updateDatenbank.executeSql(sql118);
            updateDatenbank.executeSql(sql121);
            updateDatenbank.executeSql(sql122);
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('LookAndFeel', 'JAVA-CLASSIC', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.17' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.17");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.17")) {
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES ('ErhalteneInfoMeldung', '', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.18' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.18");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.18")) {
            updateDatenbank.executeSql("ALTER TABLE `mitglieder` ADD `mitgliedBis` TEXT NOT NULL AFTER `mitgliedSeit`;");
            updateDatenbank.executeSql("ALTER TABLE `mitglieder_history` ADD `mitgliedBis` TEXT NOT NULL AFTER `mitgliedSeit`;");
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('anmeldungSpeichernErlauben', '0', '" + m + "'),('terminVersandtViaEMailFolgeMonat', '0', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.19' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.19");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.19")) {
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.20' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.20");
        }
        if ((dbVersion = tabEinstellungen.getVersion()).equals("Version: 3.20")) {
            try {
                updateDatenbank.executeSql("Update clients set typ = 'FMS';");
            }
            catch (SQLException m) {
                // empty catch block
            }
            updateDatenbank.executeSql("ALTER TABLE `ftpsync_del` ADD `statusDB` INT NOT NULL AFTER `status`;");
            updateDatenbank.executeSql("ALTER TABLE `clients` ADD `online` INT NOT NULL AFTER `typ`;");
            new TabelleClients().updateOnline(1);
            int m = 1;
            while (m < mantantenAnzahl + 1) {
                updateDatenbank.executeSql("INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES ('ZyklischerEMailAuftrag', '0', '" + m + "');");
                updateDatenbank.executeSql("INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('TerminDisplay_AnzeigeAnazahlVeranstaltungen', '6', '" + m + "'), ('TerminDisplay_AnzeigeAnazahlVeranstaltungListe', '10', '" + m + "'), ('TerminDisplay_AnzeigeDauerVeranstaltungen', '60', '" + m + "'), ('TerminDisplay_AnzeigeDauerUhr', '10', '" + m + "'), ('TerminDisplay_AnzeigenLetzenEinsatz', '1', '" + m + "'), ('TerminDisplay_HintergrundBild', '', '" + m + "'), ('TerminDisplay_HintergrundBildAktivieren', '0', '" + m + "'), ('TerminDisplay_LetzterEinsatzOrtAnzeigen', '1', '" + m + "'), ('Anwesenheitsliste_DirektDruck_HeaderText', 'Anwesenheitsliste -  Einsatz ________________', '" + m + "'), ('Anwesenheitsliste_DirektDruck_HeaderText_MitDatum', '1', '" + m + "');");
                ++m;
            }
            updateDatenbank.executeSql("Update einstellungen set wert = 'Version: 3.21' where `key` = 'version';");
            logging.logInfo((Object)"Aktualisierung fertig --> Version: 3.21");
        }
        Joomla.nutzungFMS("Update beendet! --> Aktuelle Version ist jetzt: Version: 3.21");
        JOptionPane.showMessageDialog(null, Konstante.UPDATE_ERFOLGREICH_INSTALLIERT);
        StartBildschirmAO.startDialogText.setText("Update Installation Abgeschlossen... Lade neue Einstellungen...");
        runApplication.EINSTELLUNGEN = tabEinstellungen.getAllEinstellungen();
        runApplication.EINSTELLUNGEN_GESPEICHERT = new TabelleEinstellungen_gespeichert().getAllEinstellungen();
        StartBildschirmAO.startDialogText.setText("Update Installation Abgeschlossen... Lade Hauptprogramm...");
    }
}

