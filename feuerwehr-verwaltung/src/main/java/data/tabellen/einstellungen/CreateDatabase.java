/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.hash
 */
package data.tabellen.einstellungen;

import ao.einstellungen.GrundkonfigurationAO;
import ao.einstellungen.GrundkonfigurationJWSAO;
import ao.utils.ProzessBarAO;
import data.DatenbankZugriff;
import data.DatenbankZugriffMySQL;
import data.DatenbankZugriffMySQL2;
import go.Mandant;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.hash;

public class CreateDatabase {
    public int createDatabase(String dbName) {
        try {
            Statement statement = DatenbankZugriffMySQL.getInstance().getDbConnection().createStatement();
            String sql = "create Database " + dbName + ";";
            logging.logSQL((Object)sql);
            statement.executeUpdate(sql);
            return 0;
        }
        catch (SQLException e) {
            logging.logError((Object)"Datenbank exitstiert bereits, Sie wird gel\u00f6scht");
            if (e.toString().contains("database exists")) {
                JOptionPane.showMessageDialog(null, "Datenbank existiert bereits.\n\nBitte l\u00f6schen Sie die Datenbank in ihrem MySQL Workbench oder\nw\u00e4hlen Sie einen anderen Datenbanknamen aus!", "Fehlermeldung", 0);
            }
            return -1;
        }
    }

    public void createTables(String bundesland, Mandant mandant, String clientID) throws SQLException, UnknownHostException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String adminPasswort = runApplication.JavaWebStart == 1 ? GrundkonfigurationJWSAO.adminPasswort.getText() : GrundkonfigurationAO.adminPasswort.getText();
        int gesamtStatements = 122;
        String sql1 = "CREATE TABLE IF NOT EXISTS `veranstaltung` (`id` int(11) NOT NULL,`name` text NOT NULL,`name2` text NOT NULL,`kategorie` int(11) NOT NULL,`datum` date NOT NULL,`zeit` varchar(10) NOT NULL, `zeitEnde` varchar(10) NOT NULL, `fahrzeugeinteilung` int(11) NOT NULL, `infoVersandt` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql2 = "CREATE TABLE IF NOT EXISTS `stichwort` (`id` int(11) NOT NULL,`kategorie` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql3 = "CREATE TABLE IF NOT EXISTS `mitglieder` (`id` int(11) NOT NULL,`mitgliederGruppe` int(11) NOT NULL,`anrede` int(11) NOT NULL,`name` text NOT NULL,`vorname` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefonPrivat` text NOT NULL,`telefonMobil` text NOT NULL,`telefonArbeit` text NOT NULL,`telegrammID` text NOT NULL,`email` text NOT NULL, `email2` text NOT NULL, `beruf` text NOT NULL, `dienstgrad` int(11) NOT NULL,`ausserDienst` int(11) NOT NULL,`mitgliedSeit` text NOT NULL,`mitgliedBis` text NOT NULL,`gebDatum` text NOT NULL,`hochzeit` text NOT NULL,`kommentar` text NOT NULL,`fuehrerscheinNummer` text NOT NULL,`fahrberechtigungNummer` text NOT NULL,`dienstausweisNummer` text NOT NULL, `loeschkenner` int(11) NOT NULL, `eMailDeaktiv` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql4 = "CREATE TABLE IF NOT EXISTS `lehrgang` (`mitgliedID` int(11) NOT NULL,`lehrgangID` int(11) NOT NULL,`status` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql5 = "CREATE TABLE IF NOT EXISTS `fahrzeugeinteilung_temp` (`mitgliederID` int(11) NOT NULL,`dienstgradID` int(11) NOT NULL,`klasseC` int(11) NOT NULL,`klasseB` int(11) NOT NULL,`Maschi` int(11) NOT NULL,`dlkmaschi` int(11) NOT NULL,`korbsteuerung` int(11) NOT NULL,`chef` int(11) NOT NULL, `tm1` int(11) NOT NULL,`AGT` int(11) NOT NULL,`TF` int(11) NOT NULL,`GF` int(11) NOT NULL,`ZF` int(11) NOT NULL,`rh` int(11) NOT NULL,`rs` int(11) NOT NULL,`ra` int(11) NOT NULL,`beteiligung` int(20) NOT NULL,`position` int(20) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql6 = "CREATE TABLE IF NOT EXISTS `fahrzeugeinteilung` (`id` int(100) NOT NULL,`veranstaltungID` int(11) NOT NULL,`kategorie` int(11) NOT NULL,`jahr` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`position` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql7 = "CREATE TABLE IF NOT EXISTS `fahrzeuge` (`id` int(11) NOT NULL,`name` text NOT NULL,`beschreibung` int(11) NOT NULL,`kennzeichen` text NOT NULL,`funkrufname` text NOT NULL,`sitzplaetze` int(11) NOT NULL,`minBesatzung` int(11) NOT NULL,`maxBesatzung` int(11) NOT NULL,`fuehrerschein` text NOT NULL, `ausserDienst` int(11) NOT NULL,`anhaenger` int(11) NOT NULL,`trupp` int(11) NOT NULL,`sortierung` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql8 = "CREATE TABLE IF NOT EXISTS `einsatz` ( `id` int(100) NOT NULL,`einsatzNummer` int(11) NOT NULL,`einsatznummerOffiziell` text NOT NULL,`veranstaltungID` int(11) NOT NULL,`Datum` date NOT NULL,`ZeitAlarm` varchar(10) NOT NULL,`ZeitAusgerueckt` varchar(10) NOT NULL,`zeitEingetroffen` varchar(10) NOT NULL,`zeitEingerueckt` varchar(10) NOT NULL,`Ort` text NOT NULL,`stadtteil` text NOT NULL,`Stichwort` int(11) NOT NULL,`Fahrzeug` text NOT NULL,`fahrzeugID` text NOT NULL,`beschreibung` text NOT NULL,`staerkeGF` int(3) NOT NULL,`staerkeFM` int(3) NOT NULL, `einsatzleiter` int(11) NOT NULL, `staerkeZF` int(3) NOT NULL,`einsatzleiterBF` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql9 = "CREATE TABLE IF NOT EXISTS `dienstgrad` (`id` int(2) NOT NULL,`beschreibung` text NOT NULL,`beschreibungLang` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql10 = "INSERT INTO `dienstgrad` (`id`, `beschreibung`,`beschreibungLang`, `mandantID`) VALUES (0, '---', '<Kein Dienstgrad>', " + mandant.getId() + ");";
        String sql11 = "CREATE TABLE IF NOT EXISTS `anwesenheit` (`id` int(100) NOT NULL,`jahr` int(4) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql12 = "CREATE TABLE IF NOT EXISTS `veranstaltung_kategorie` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql13 = "INSERT INTO `veranstaltung_kategorie` (`id`, `name`, `mandantID`) VALUES (1, 'Einsatz', " + mandant.getId() + "),(2, 'Dienstabend', " + mandant.getId() + "),(3, 'BSW', " + mandant.getId() + "),(4, 'Sonstige', " + mandant.getId() + ");";
        String sql14 = "CREATE TABLE IF NOT EXISTS `einsatz_kategorie` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql15 = "INSERT INTO `einsatz_kategorie` (`id`, `name`, `mandantID`) VALUES (1, 'Brandeinsatz', " + mandant.getId() + "),(2, 'Technische Hilfeleistung', " + mandant.getId() + "),(3, 'Wachbesetzung', " + mandant.getId() + "),(4, 'Sonstige', " + mandant.getId() + "),('5', 'Rettungsdienst', " + mandant.getId() + "),('6', 'First Responder', " + mandant.getId() + ");";
        String sql16 = "CREATE TABLE IF NOT EXISTS `statistikEinsatz` (`id` int(100) NOT NULL,`veranstaltungID` int(100) NOT NULL,`einsatzID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`stichwort` int(11) NOT NULL,`kategorie` int(11) NOT NULL,`ausrueckezeit` int(11) NOT NULL,`dauer` int(11) NOT NULL,`dauerAlarmfahrt` int(11) NOT NULL,`mannstunden` int(11) NOT NULL,`wochentag` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql17 = "CREATE TABLE IF NOT EXISTS `mitglieder_arbeit` (`id` int(11) NOT NULL,`name` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefon` text NOT NULL,`ansprechpartner` text NOT NULL,`email` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql18 = "CREATE TABLE IF NOT EXISTS `mitglieder_angehoerige` (`id` int(11) NOT NULL,`name` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefonPrivat` text NOT NULL,`telefonMobil` text NOT NULL,`email` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql19 = "CREATE TABLE IF NOT EXISTS `fahrzeug_beschreibung` (`id` int(11) NOT NULL,`beschreibung` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql20 = "INSERT INTO `fahrzeug_beschreibung` (`id`, `beschreibung`) VALUES (1, 'Hilfeleistungsl\u00f6schfahrzeug'),(2, 'L\u00f6schgruppenfahrzeug'),(3, 'Tankl\u00f6schfahrzeug'),(4, 'Drehleiter'),(5, 'Teleskopmast'),(6, 'Mannschaftstransportfahrzeug'),(7, 'GW Logistik'),(8, 'Schlauchwagen'),(9, 'LKW'),(10, 'R\u00fcstwagen'),(11, 'Feldk\u00fcche'),(12, 'Einsatzleitwagen'),(13, 'Rettungswagen'),(14, 'Krankentransportwagen'),(15, 'Notarzteinsatzfahrzeuge'),(16, 'Sonderfahrzeuge');";
        String sql21 = "CREATE TABLE IF NOT EXISTS `karte_hydranten` (`id` int(20) NOT NULL,`starssenid` int(11) NOT NULL,`hausnummer` text NOT NULL,`nennweite` int(4) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql22 = "CREATE TABLE IF NOT EXISTS `karte_strassen` (`id` int(11) NOT NULL,`name` text NOT NULL,`bild` text NOT NULL,`bild2` text NOT NULL,`anfahrt` text NOT NULL,`info` text NOT NULL,`koordinaten` text NOT NULL,`PLZ` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql23 = "CREATE TABLE IF NOT EXISTS `user` (`userid` text NOT NULL,`passwort` text NOT NULL,`usergruppe` text NOT NULL,`admin` int(1) NOT NULL,`deaktiv` int(1) NOT NULL,`loeschkenner` int(1) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql24 = "CREATE TABLE IF NOT EXISTS `statistikbsw` (`id` int(100) NOT NULL,`veranstaltungID` int(100) NOT NULL,`bswID` int(11) NOT NULL, `jahr` int(11) NOT NULL,`dauer` int(11) NOT NULL,`mannstunden` int(11) NOT NULL,`wochentag` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql25 = "CREATE TABLE IF NOT EXISTS `jahresberichte` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`title` text NOT NULL,`bericht` text NOT NULL,`erstelldatum` date NOT NULL,`autoBericht` int(11) NOT NULL,`dateiname` text NOT NULL,`statistiken` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql26 = "CREATE TABLE IF NOT EXISTS `abwesenheitsgrund` (`id` int(11) NOT NULL,`name` text NOT NULL,`kurzName` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql27 = "CREATE TABLE IF NOT EXISTS `abwesenheit` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`grund` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql28 = "INSERT INTO `abwesenheitsgrund` (`id`, `name`, `kurzName`, `mandantID`) VALUES (0, 'Undefiniert', 'UNDEF.', " + mandant.getId() + "),(1, 'Unentschuldigt', 'UE', " + mandant.getId() + "),(2, 'Entschuldigt', 'E', " + mandant.getId() + "),(3, 'Urlaub', 'U', " + mandant.getId() + "),(4, 'Krank', 'K', " + mandant.getId() + "),(5, 'Brandsicherheitswache', 'BSW', " + mandant.getId() + ");";
        String sql29 = "CREATE TABLE IF NOT EXISTS `einstellungen` (`key` text NOT NULL,`wert` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql30 = "INSERT INTO `einstellungen` (`key`, `wert`, `mandantID`) VALUES ('EinsatzBericht', 'data/templates/Einsatzbericht.xml', " + mandant.getId() + "),('Name', '" + mandant.getName() + "', " + mandant.getId() + "),('Stadt', '', " + mandant.getId() + "),('plz', '', " + mandant.getId() + "),('telefon', '', " + mandant.getId() + "),('strasse', '', " + mandant.getId() + "),('verdienstausfall', 'data/templates/Verdienstausfallbescheinigung.xml', " + mandant.getId() + "),('fehlalarm', '30', " + mandant.getId() + "),('briefkopf', 'images/briefkopf.jpg', " + mandant.getId() + "),('untersuchungVorwarnung', '2', " + mandant.getId() + "),('gebAnzeigen', '1', " + mandant.getId() + "),('autoBerichtAktiv', '1', " + mandant.getId() + "),('ZeitAutoBericht', '60', " + mandant.getId() + "),('letzterAutoBericht', '0', " + mandant.getId() + "),('untersuchungVorwarnungFahrzeug', '2', " + mandant.getId() + "),('m\u00e4ngelmeldung', 'data/templates/M\u00e4ngelmeldung.xml', " + mandant.getId() + "),('vorwarnungGer\u00e4te', '1', " + mandant.getId() + "),('termineAnzeigen', '1', " + mandant.getId() + "),('agtTrainingAnzeigen', '1', " + mandant.getId() + "),('WieVieleLehrgangsmeldungenProJahr', '2', " + mandant.getId() + "),('version', '" + "Version: 3.21" + "', -1),('smtpPort', '587', " + mandant.getId() + "),('smtpServer', '', " + mandant.getId() + "),('pop3Server', '', " + mandant.getId() + "),('pop3Port', '995', " + mandant.getId() + "),('emailAdresse', '', " + mandant.getId() + "),('emailPasswort', '', " + mandant.getId() + "),('useSSL', '1', " + mandant.getId() + "),('emailModul', '0', " + mandant.getId() + "),('emailSignatur', '', " + mandant.getId() + "),('vorbelegungDienstStart', '19:30', " + mandant.getId() + "),('vorbelegungDienstEnde', '22:00', " + mandant.getId() + "),('einsatzSchnittstelle', '0', " + mandant.getId() + "),('autoDBsave', '1', " + mandant.getId() + "),('autoDBsaveTage', '30', " + mandant.getId() + "),('letzterDBsave', '0', " + mandant.getId() + "),('untersuchungViaEMail', '0', " + mandant.getId() + "),('untersuchungViaEMailChefBCC', '0', " + mandant.getId() + "),('terminVersandtViaEMail', '0', " + mandant.getId() + "),('vCardSeperator', ';', " + mandant.getId() + "),('ablaufLKWF\u00fchrerscheinViaEMail', '0', " + mandant.getId() + "), ('ablaufLKWAnzeigen', '0', " + mandant.getId() + "), ('automatischesUpdate', '1', " + mandant.getId() + "), ('einsatzleiterBF', '0', " + mandant.getId() + "), ('bundesland', '" + bundesland + "', " + mandant.getId() + "),('abrechnungModul', '1', " + mandant.getId() + "),('geraetepruefungViaEMail', '1', " + mandant.getId() + "),('offeneMaengelAnzeigen', '1', " + mandant.getId() + "),('fahrzeugUntersuchungViaEMail', '0', " + mandant.getId() + "),('m\u00e4ngelmeldungViaEMailVersenden', '0', " + mandant.getId() + "),('gebAnzeigeModus', '1', " + mandant.getId() + "),('vorbelegungBSWTreffen', '12:15', " + mandant.getId() + "), ('vorbelegungBSWVeranstaltungStart', '15:30', " + mandant.getId() + "), ('vorbelegungBSWEnde', '18:15', " + mandant.getId() + "),('EinsatzBerichtArt', 'PDF (intern)', " + mandant.getId() + "),('Schichtplaner', '1', " + mandant.getId() + "),('Fahrtenbuch', '1', " + mandant.getId() + "),('SichtbarkeitVeranstaltungVergangenheit', '1', " + mandant.getId() + "), ('SichtbarkeitVeranstaltungZukunft', '12', " + mandant.getId() + "),('JoomlaVeranstaltungSenden', '0', " + mandant.getId() + "), ('JoomlaLink', '', " + mandant.getId() + "), ('JoomlaAusbildungsplanSenden', '0', " + mandant.getId() + "), ('AlwaysOnTop', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponente', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponenteVisible', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponenteEMail', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponenteEMailAn1', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponenteEMailAn2', '0', " + mandant.getId() + "), ('JoomlaEinsatzkomponenteEMailAn3', '0', " + mandant.getId() + "), ('terminVersandtViaEMailConfig', '1', " + mandant.getId() + "),('joomlaEinsatzkomponenteConfig', '1', " + mandant.getId() + "), ('joomlaEinsatzkomponenteSecretKey', '', " + mandant.getId() + "), ('joomlaEinsatzkomponenteStichwort', '1', " + mandant.getId() + "),('Joomla_mod_Veranstaltung', '/modules/mod_Veranstaltung/veranstaltung.php', " + mandant.getId() + "),('Joomla_mod_Veranstaltung_update', '/modules/mod_veranstaltung/veranstaltung_update.php', " + mandant.getId() + "),('Joomla_mod_VeranstaltungKategorie', '/modules/mod_veranstaltung/veranstaltungKategorie.php', " + mandant.getId() + "),('Joomla_mod_Veranstaltung_delete', '/modules/mod_veranstaltung/veranstaltung_delete.php', " + mandant.getId() + "),('Joomla_mod_Ausbildungsplan_delete', '/modules/mod_ausbildungsplan/ausbildungsplan_delete.php', " + mandant.getId() + "),('Joomla_mod_VeranstaltungKategorie_delete', '/modules/mod_veranstaltung/veranstaltungKategorie_delete.php', " + mandant.getId() + "),('Joomla_mod_AusbildungKategorie_delete', '/modules/mod_ausbildungsplan/ausbildungKategorie_delete.php', " + mandant.getId() + "),('Joomla_mod_Ausbildungsplan', '/modules/mod_ausbildungsplan/ausbildungsplan.php', " + mandant.getId() + "),('Joomla_mod_AusbildungKategorie', '/modules/mod_ausbildungsplan/ausbildungKategorie.php', " + mandant.getId() + "),('Joomla_com_Einsatz', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway.php', " + mandant.getId() + "),('Joomla_com_Einsatz_Freischalten', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_freischalten.php', " + mandant.getId() + "),('VerdienstausfallBerichtArt', 'PDF (intern)', " + mandant.getId() + "), ('M\u00e4ngelBerichtArt', 'PDF (intern)', " + mandant.getId() + "),('Urlaubsplaner', '1', " + mandant.getId() + "),('emailTemplateEinsatzbericht', '', " + mandant.getId() + "),('emailTemplateEinsatzberichtAN', '', " + mandant.getId() + "),('emailTemplateEinsatzberichtCC', '', " + mandant.getId() + "),('emailTemplateEinsatzberichtBCC', '', " + mandant.getId() + "),('globaleEMailEinheitsf\u00fchrungAktiviert', '0', " + mandant.getId() + "),('globaleEMailGer\u00e4tewarteAktiviert', '0', " + mandant.getId() + "),('globaleEMailEinheitsf\u00fchrung', '', " + mandant.getId() + "),('globaleEMailGer\u00e4tewarte', '', " + mandant.getId() + "),('Statistik2', '1', " + mandant.getId() + "),('EinsatznummerIstPflicht', '0', " + mandant.getId() + "),('EinsatzLeiterBFIstPflicht', '0', " + mandant.getId() + "),('schutzziel1', '8', " + mandant.getId() + "), ('schutzziel2', '13', " + mandant.getId() + "), ('emailTemplateM\u00e4ngelmeldungAN', '', " + mandant.getId() + "), ('emailTemplateM\u00e4ngelmeldungCC', '', " + mandant.getId() + "), ('emailTemplateM\u00e4ngelmeldungBCC', '', " + mandant.getId() + "), ('emailTemplateM\u00e4ngelmeldung', '', " + mandant.getId() + "),('verdienstausfallOptionen', '1', " + mandant.getId() + "), ('headerPrint', '1', " + mandant.getId() + "), ('footerPrint', '0', " + mandant.getId() + "), ('modulVeranstaltung', '1', " + mandant.getId() + "), ('modulAusbildungsplan', '1', " + mandant.getId() + "), ('modulFahrzeugeinteilung', '1', " + mandant.getId() + "), ('eMailName', '', " + mandant.getId() + "), ('zeilenh\u00f6heDruck', '30', " + mandant.getId() + "), ('zeilenh\u00f6heAnsicht', '30', " + mandant.getId() + "), ('bswHitliste', '1', " + mandant.getId() + "), ('getakteteInternetverbindung', '0', " + mandant.getId() + "), ('onlineStatus', '1', " + mandant.getId() + "), ('JoomlaEinsatzKomponenteNurAlamierung\u00dcbertragen', '0', '" + mandant.getId() + "'), ('JoomlaEinsatzKomponenteEinsatzBericht\u00dcbermitteln', '0', '" + mandant.getId() + "'),('Joomla_com_Einsatz_Bericht', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_bericht.php', '" + mandant.getId() + "'), ('WeitereOrganisationen', '1', '" + mandant.getId() + "'), ('Joomla_com_Einsatz_Orgaisation', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_organisation.php', '" + mandant.getId() + "'), ('Joomla_com_Einsatz_Fahrzeug', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_fahrzeug.php', '" + mandant.getId() + "'),('Joomla_com_Einsatz_Delete', '/components/com_einsatzkomponente/eiko_feuerwehrmanagement_gateway_delete.php', '" + mandant.getId() + "'),('FullBackupInZip', '0', '" + mandant.getId() + "'),('FullBackupPath', '', '" + mandant.getId() + "'),('mitgliedSeitFormat', 'yyyy', '" + mandant.getId() + "'),('hochzeitFeldFuerMitglieder', '0', '" + mandant.getId() + "'),('mitgliedSeitPflichtEintrag', '1', '" + mandant.getId() + "'), ('LehrgangEintragenAusMitgliederVerwaltungMode', '0', '" + mandant.getId() + "')," + "('unwetterwarnungPop3', '', '" + mandant.getId() + "'), ('unwetterwarnungEMail', '', '" + mandant.getId() + "'), ('unwetterwarnungPopPort', '', '" + mandant.getId() + "'), ('unwetterwarnungPasswort', '', '" + mandant.getId() + "'), ('unwetterwarnungSSL', '0', '" + mandant.getId() + "'), ('unwetterwarnungModulAktiv', '0', '" + mandant.getId() + "')," + "('vorwarnungAblaufDienstausweis', '2', '" + mandant.getId() + "'), ('ablaufDienstausweisAnzeigen', '1', '" + mandant.getId() + "'),('ablaufDienstausweisViaEMail', '0', '" + mandant.getId() + "'), ('pr\u00fcfungDerFahrerlaubnis', '0', '" + mandant.getId() + "'),('darstellungLehrg\u00e4ngeMitgliederverwaltung', 'CheckBox', '" + mandant.getId() + "'),('ablaufFahrberechtigungAnzeigen', '0', '" + mandant.getId() + "'), ('vorwarnungAblaufFahrberechtigung', '2', '" + mandant.getId() + "'),('ablaufFahrberechtigungViaEMail', '0', '" + mandant.getId() + "'),('druckAnwesenheitsListeMode', '1', '" + mandant.getId() + "'),('dienstgradAufAnwesenheitsliste', '1', '" + mandant.getId() + "'),('modulMitgliederVerf\u00fcgbarkeit', '1', '" + mandant.getId() + "'),('schfiftgr\u00f6\u00dfeAnwesenheitsliste', '26', '" + mandant.getId() + "'),('bestaetignungFreistellungEinsatzArt', 'PDF (intern)', '" + mandant.getId() + "'), ('bestaetignungFreistellungEinsatz', 'data/Templates/BescheinigungEinsatzTeilnahme.xml', '" + mandant.getId() + "'), ('bestaetignungFreistellungEinsatzAktiv', '0', '" + mandant.getId() + "')," + "('modulEinsatzgebiet', '1', '" + mandant.getId() + "'),('globaleEMailG25Aktiviert', '0', '" + mandant.getId() + "'), ('globaleEMailG25', '', '" + mandant.getId() + "'), ('globaleEMailG26Aktiviert', '0', '" + mandant.getId() + "'), ('globaleEMailG26', '', '" + mandant.getId() + "'), ('globaleEMailFahrberechtigungAktiviert', '0', '" + mandant.getId() + "'), ('globaleEMailFahrberechtigung', '', '" + mandant.getId() + "'), ('globaleEMailDienstausweisAktiviert', '0', '" + mandant.getId() + "'), ('globaleEMailDienstausweis', '', '" + mandant.getId() + "'),('EinsatzBerichtFahrzeugbelegungHinzuf\u00fcgen', '0', '" + mandant.getId() + "'), ('EinsatzBerichtAtemschutzpassHinzuf\u00fcgen', '0', '" + mandant.getId() + "'),('EinsatzBerichtEinsatzleiterMitDienstgrad', '1', '" + mandant.getId() + "'),('feldEintreffenAusblenden', '0', '" + mandant.getId() + "'), ('feldStadtteilAusblenden', '0', '" + mandant.getId() + "'), ('langesDatumsformatUntersuchungsliste', '0', '" + mandant.getId() + "'),('LookAndFeel', 'JAVA-CLASSIC', '" + mandant.getId() + "'),('anmeldungSpeichernErlauben', '1', '" + mandant.getId() + "'),('terminVersandtViaEMailFolgeMonat', '0', '" + mandant.getId() + "')," + "('TerminDisplay_AnzeigeAnazahlVeranstaltungen', '6', '" + mandant.getId() + "'), ('TerminDisplay_AnzeigeAnazahlVeranstaltungListe', '10', '" + mandant.getId() + "'), ('TerminDisplay_AnzeigeDauerVeranstaltungen', '60', '" + mandant.getId() + "'), ('TerminDisplay_AnzeigeDauerUhr', '10', '" + mandant.getId() + "'), ('TerminDisplay_AnzeigenLetzenEinsatz', '1', '" + mandant.getId() + "'), ('TerminDisplay_HintergrundBild', '', '" + mandant.getId() + "'), ('TerminDisplay_HintergrundBildAktivieren', '0', '" + mandant.getId() + "'), ('TerminDisplay_LetzterEinsatzOrtAnzeigen', '1', '" + mandant.getId() + "'), ('Anwesenheitsliste_DirektDruck_HeaderText', 'Anwesenheitsliste -  Einsatz _________________', '" + mandant.getId() + "'), ('Anwesenheitsliste_DirektDruck_HeaderText_MitDatum', '1', '" + mandant.getId() + "');";
        String sql31 = "CREATE TABLE IF NOT EXISTS `brandsicherheitswachen_temp` (`mitgliederID` int(11) NOT NULL,`beteiligung` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql32 = "CREATE TABLE IF NOT EXISTS `jahr` (`jahr` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql33 = "CREATE TABLE IF NOT EXISTS `mitglieder_gruppe` (`id` int(11) NOT NULL,`personalnummer` int(11) NOT NULL, `nextPersonalnummer` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql34 = "INSERT INTO `mitglieder_gruppe` (`id`, `personalnummer`, `nextPersonalnummer`, `name`, `mandantID`) VALUES (1, 11000, 11000, 'Einsatzabteilung', " + mandant.getId() + ")";
        String sql35 = "CREATE TABLE IF NOT EXISTS `briefe` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`title` text NOT NULL,`bericht` text NOT NULL,`erstelldatum` date NOT NULL,`dateiname` text NOT NULL, `empfaenger` text NOT NULL, `template` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1";
        String sql36 = "CREATE TABLE IF NOT EXISTS `einsatz_berichte` (`id` int(11) NOT NULL,`einsatzNummer` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`dateiname` text NOT NULL,`fahrzeugbelegung` int(11) NOT NULL,`atemschutz` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql37 = "INSERT INTO `user` (`userid`, `passwort`, `usergruppe`, `admin`, `deaktiv`, `loeschkenner`, `mandantID`) VALUES ('admin', '" + hash.createHashCode((String)adminPasswort) + "', 'admin', 1, 0, 0, " + mandant.getId() + ");";
        String sql38 = "CREATE TABLE IF NOT EXISTS `brandsicherheitswachen` (`id` int(11) NOT NULL,`bswNummer` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`ort` text NOT NULL,`art` text NOT NULL,`datum` date NOT NULL,`zeit_treffen` text NOT NULL,`zeit_start` text NOT NULL,`zeit_ende` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql39 = "CREATE TABLE IF NOT EXISTS `mitglieder_untersuchung` (`id` int(11) NOT NULL,`g25` text NOT NULL, `g26` text NOT NULL, `agttraining` text NOT NULL, `infoG25` int(11) NOT NULL, `infoG26` int(11) NOT NULL, `ablaufLKW` text NOT NULL, `infoAblaufLKW` int(11) NOT NULL,`ablaufDienstausweis` text NOT NULL,`infoAblaufDienstausweis` int(11) NOT NULL,`pruefungDerFahrberechtigung` text NOT NULL,`infoPruefungDerFahrberechtigung` int(11) NOT NULL,`g30` text NOT NULL, `infoG30` int(11) NOT NULL,`g41` text NOT NULL, `g42` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql40 = "CREATE TABLE IF NOT EXISTS `ausbildung_kategorie` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql41 = "INSERT INTO `ausbildung_kategorie` (`id`, `name`, `mandantID`) VALUES (1, 'FwDV3', " + mandant.getId() + "),(2, 'FwDV7', " + mandant.getId() + "),(3, 'FwDV10', " + mandant.getId() + ");";
        String sql42 = "CREATE TABLE IF NOT EXISTS `ausbildung` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`ausbildungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql43 = "CREATE TABLE IF NOT EXISTS `einsatz_zeiten` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`einsatzID` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`zeitAlarm` text NOT NULL,`zeitAusgerueckt` text NOT NULL,`zeitEingetroffen` text NOT NULL,`zeitEingerueckt` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql44 = "CREATE TABLE IF NOT EXISTS `fahrzeug_untersuchung` (`id` int(11) NOT NULL,`tuev` text NOT NULL,`sp` text NOT NULL,`service` text NOT NULL,`gaswartung` text NOT NULL, `infoTuev` INT NOT NULL, `infoSP` INT NOT NULL, `infoService` INT NOT NULL, `infoGas` INT NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql45 = "CREATE TABLE IF NOT EXISTS `mitglieder_bankverbindung` (`id` int(11) NOT NULL,`iban` text NOT NULL,`bic` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql46 = "CREATE TABLE IF NOT EXISTS `geraetepruefung` (`id` int(11) NOT NULL,`stromerzeuger` text NOT NULL,`steckleiter` text NOT NULL,`schiebleiter` text NOT NULL,`hydraulik` text NOT NULL,`pumpe` text NOT NULL,`kettensaege` text NOT NULL,`doppelkanister` text NOT NULL,`geraetepruefung_allgem` text NOT NULL,`abstusiset` text NOT NULL, `infoEMail` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql47 = "CREATE TABLE IF NOT EXISTS `maengelmeldung` (`id` int(11) NOT NULL, `jahr` int(11) NOT NULL,`mitgliedID` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`datum` text NOT NULL,`wann` text NOT NULL,`beschreibung` text NOT NULL,`dateiname` text NOT NULL,`status` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql48 = "CREATE TABLE IF NOT EXISTS `berechtigunggruppe` (`id` int(11) NOT NULL, `seite` int(11) NOT NULL, `name` text NOT NULL,`BR0` int(11) NOT NULL,`BR1` int(11) NOT NULL,`BR2` int(11) NOT NULL,`BR3` int(11) NOT NULL,`BR4` int(11) NOT NULL,`BR5` int(11) NOT NULL,`BR6` int(11) NOT NULL,`BR7` int(11) NOT NULL,`BR8` int(11) NOT NULL,`BR9` int(11) NOT NULL,`BR10` int(11) NOT NULL,`BR11` int(11) NOT NULL,`BR12` int(11) NOT NULL,`BR13` int(11) NOT NULL,`BR14` int(11) NOT NULL,`BR15` int(11) NOT NULL,`BR16` int(11) NOT NULL,`BR17` int(11) NOT NULL,`BR18` int(11) NOT NULL,`BR19` int(11) NOT NULL,`BR20` int(11) NOT NULL,`BR21` int(11) NOT NULL,`BR22` int(11) NOT NULL,`BR23` int(11) NOT NULL,`BR24` int(11) NOT NULL,`BR25` int(11) NOT NULL,`BR26` int(11) NOT NULL,`BR27` int(11) NOT NULL,`BR28` int(11) NOT NULL,`BR29` int(11) NOT NULL,`BR30` int(11) NOT NULL,`BR31` int(11) NOT NULL,`BR32` int(11) NOT NULL,`BR33` int(11) NOT NULL,`BR34` int(11) NOT NULL,`BR35` int(11) NOT NULL,`BR36` int(11) NOT NULL,`BR37` int(11) NOT NULL,`BR38` int(11) NOT NULL,`BR39` int(11) NOT NULL,`BR40` int(11) NOT NULL,`BR41` int(11) NOT NULL,`BR42` int(11) NOT NULL,`BR43` int(11) NOT NULL,`BR44` int(11) NOT NULL,`BR45` int(11) NOT NULL,`BR46` int(11) NOT NULL,`BR47` int(11) NOT NULL,`BR48` int(11) NOT NULL,`BR49` int(11) NOT NULL,`BR50` int(11) NOT NULL,`BR51` int(11) NOT NULL,`BR52` int(11) NOT NULL,`BR53` int(11) NOT NULL,`BR54` int(11) NOT NULL,`BR55` int(11) NOT NULL,`BR56` int(11) NOT NULL,`BR57` int(11) NOT NULL,`BR58` int(11) NOT NULL,`BR59` int(11) NOT NULL,`BR60` int(11) NOT NULL,`BR61` int(11) NOT NULL,`BR62` int(11) NOT NULL,`BR63` int(11) NOT NULL,`BR64` int(11) NOT NULL,`BR65` int(11) NOT NULL,`BR66` int(11) NOT NULL,`BR67` int(11) NOT NULL,`BR68` int(11) NOT NULL,`BR69` int(11) NOT NULL,`BR70` int(11) NOT NULL,`BR71` int(11) NOT NULL,`BR72` int(11) NOT NULL,`BR73` int(11) NOT NULL,`BR74` int(11) NOT NULL,`BR75` int(11) NOT NULL,`BR76` int(11) NOT NULL,`BR77` int(11) NOT NULL,`BR78` int(11) NOT NULL,`BR79` int(11) NOT NULL,`BR80` int(11) NOT NULL,`BR81` int(11) NOT NULL,`BR82` int(11) NOT NULL,`BR83` int(11) NOT NULL,`BR84` int(11) NOT NULL,`BR85` int(11) NOT NULL,`BR86` int(11) NOT NULL,`BR87` int(11) NOT NULL,`BR88` int(11) NOT NULL,`BR89` int(11) NOT NULL,`BR90` int(11) NOT NULL,`BR91` int(11) NOT NULL,`BR92` int(11) NOT NULL,`BR93` int(11) NOT NULL,`BR94` int(11) NOT NULL,`BR95` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql49 = "INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`,`BR88`,`BR89`,`BR90`,`BR91`,`BR92`,`BR93`,`BR94`,`BR95`, `mandantID`) VALUES (0, 1, 'Public', 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,0,0,0,0,0,1,1,0, " + mandant.getId() + "),(1, 1, 'Administrator', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,1,1,1,1,1,1,1,1, " + mandant.getId() + ");";
        String sql50 = "CREATE TABLE IF NOT EXISTS `lehrgang_kategorie` (`id` int(11) NOT NULL,`art` text NOT NULL,`name` text NOT NULL,`relevant` int(11) NOT NULL,`reihenfolge` int(11) NOT NULL,`ue` int(11) NOT NULL,`loeschbar` int(11) NOT NULL,`loeschkenner` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql51 = "INSERT INTO `lehrgang_kategorie` (`id`, `art`, `name`, `relevant`, `reihenfolge`, `ue`, `loeschbar`, `loeschkenner`, `mandantID`) VALUES(1, 'F\u00fc', 'F\u00fchrerschein Klasse C', 1, 11, 0, 1, 0, " + mandant.getId() + "),(2, 'F\u00fc', 'F\u00fchrerschein Klasse B', 0, 0, 0, 1, 0, " + mandant.getId() + "),(3, 'F\u00fc', 'F\u00fchrerschein Klasse CE', 0, 0, 0, 0, 0, " + mandant.getId() + "),(4, 'L', 'Erste Hilfe', 1, 1, 0, 0, 0, " + mandant.getId() + "),(5, 'L', 'Erste Hilfe Fortbildung', 0, 0, 0, 0, 0, " + mandant.getId() + "),(6, 'L', 'TM 1', 1, 2, 0, 1, 0, " + mandant.getId() + "),(7, 'L', 'Sprechfunker', 1, 3, 0, 0, 0, " + mandant.getId() + "),(8, 'L', 'Atemschutz', 1, 4, 0, 1, 0, " + mandant.getId() + "),(9, 'L', 'RBA Stufe 1', 0, 0, 0, 0, 0, " + mandant.getId() + "),(10, 'L', 'Absturzsicherung', 1, 5, 0, 0, 0, " + mandant.getId() + "),(11, 'L', 'TH 1', 1, 6, 0, 0, 0, " + mandant.getId() + "),(12, 'L', 'Maschinist', 1, 7, 0, 1, 0, " + mandant.getId() + "),(13, 'L', 'Maschinist Fortbildung', 0, 0, 0, 0, 0, " + mandant.getId() + "),(14, 'L', 'Kettens\u00e4ge', 0, 0, 0, 0, 0, " + mandant.getId() + "),(15, 'L', 'Truppf\u00fchrer', 1, 8, 0, 1, 0, " + mandant.getId() + "),(16, 'L', 'RBA Stufe 2', 0, 0, 0, 0, 0, " + mandant.getId() + "),(17, 'L', 'FwDV 500 (Gefahrgut)', 1, 9, 0, 0, 0, " + mandant.getId() + "),(18, 'L', 'FwDV 500 (Strahlenschutz)', 1, 10, 0, 0, 0, " + mandant.getId() + "),(19, 'L', 'Gruppenf\u00fchrer', 1, 13, 0, 1, 0, " + mandant.getId() + "),(20, 'L', 'Zugf\u00fchrer', 0, 0, 0, 1, 0, " + mandant.getId() + "),(21, 'L', 'DLK Korbsteuerung', 0, 0, 0, 1, 0, " + mandant.getId() + "),(22, 'L', 'DLK Maschinist', 0, 0, 0, 1, 0, " + mandant.getId() + "),(23, 'L', 'Rettungshelfer', 0, 0, 0, 1, 0, " + mandant.getId() + "),(24, 'L', 'Rettungssanit\u00e4ter', 0, 0, 0, 1, 0, " + mandant.getId() + "),(25, 'L', 'Rettungsassistent', 0, 0, 0, 1, 0, " + mandant.getId() + "),(26, 'F', 'Einheitsf\u00fchrer / Wehrleiter', 0, 0, 0, 1, 0, " + mandant.getId() + "),(27, 'F', 'Ger\u00e4tewart', 0, 0, 0, 1, 0, " + mandant.getId() + "),(28, 'F', 'Getr\u00e4nkewart', 0, 0, 0, 0, 0, " + mandant.getId() + "),(29, 'F', 'Materialwart', 0, 0, 0, 0, 0, " + mandant.getId() + ");";
        String sql52 = "CREATE TABLE IF NOT EXISTS `lehrgangsmeldung` (`mitgliedID` int(11) NOT NULL,`lehrgang` text NOT NULL,`art` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql53 = "CREATE TABLE IF NOT EXISTS `mitglieder_anrede` (`id` int(11) NOT NULL,`name` text NOT NULL, `anredeBrief` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql54 = "INSERT INTO `mitglieder_anrede` (`id`, `name`, `anredeBrief`, `mandantID`) VALUES (1, 'Herr', 'Sehr geehrter', " + mandant.getId() + "),(2, 'Frau', 'Sehr geehrte', " + mandant.getId() + ");";
        String sql55 = "CREATE TABLE IF NOT EXISTS `email_gesendet` (`id` int(11) NOT NULL,`an` text NOT NULL,`cc` text NOT NULL,`bcc` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`anhang` int(11) NOT NULL,`date` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql56 = "CREATE TABLE IF NOT EXISTS `email_entwurf` (`id` int(11) NOT NULL,`an` text NOT NULL,`cc` text NOT NULL,`bcc` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`anhang` text NOT NULL,`date` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql57 = "CREATE TABLE IF NOT EXISTS `lager` (`id` int(11) NOT NULL,`name` text NOT NULL,`verantwortlicher` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql58 = "INSERT INTO `lager` (`id`, `name`, `verantwortlicher`, `mandantID`) VALUES (9000, 'Virtuelles- / Defektteile- / Ausmusterlager', 0, " + mandant.getId() + ");";
        String sql59 = "CREATE TABLE IF NOT EXISTS `lager_artikel` (`id` int(11) NOT NULL,`name` text NOT NULL,`beschreibung` text NOT NULL,`bild` text NOT NULL,`wert` int(11) NOT NULL,`EAN` int(100) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql60 = "CREATE TABLE IF NOT EXISTS `lager_zugewiesen` (`id` int(100) NOT NULL,`artikelID` int(11) NOT NULL,`anzahl` int(11) NOT NULL,`gruppe` text NOT NULL,`mitgliedID` int(11) NOT NULL,`ort` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql61 = "CREATE TABLE IF NOT EXISTS `email_empfangende` (`id` int(11) NOT NULL,`sender` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`date` text NOT NULL,`size` int(11) NOT NULL,`anhang` int(11) NOT NULL,`gelesen` int(11) NOT NULL,`art` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql62 = "CREATE TABLE IF NOT EXISTS `statistiksonstigeveranstaltung` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`kategorie` int(11) NOT NULL,`jahr` int(11) NOT NULL,`dauer` int(11) NOT NULL,`mannstunden` int(11) NOT NULL,`wochentag` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql63 = "CREATE TABLE IF NOT EXISTS `ausbildung_plan` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`ausbildungKategorie` int(11) NOT NULL,`details` text NOT NULL,`ausbilder1` int(11) NOT NULL, `ausbilder2` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql64 = "CREATE TABLE IF NOT EXISTS `logbuch` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`user` text NOT NULL,`aktion` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql65 = "CREATE TABLE IF NOT EXISTS `email_ausgang` (`id` int(11) NOT NULL,`an` text NOT NULL,`cc` text NOT NULL,`bcc` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`anhang` text NOT NULL,`date` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql66 = "CREATE TABLE IF NOT EXISTS `maengelmeldung_kommentar` (`mangelID` int(11) NOT NULL,`kommentarID` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL, `kommentar` text NOT NULL, `user` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql67 = "CREATE TABLE IF NOT EXISTS `keystore` (`key` text NOT NULL,`wert` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql68 = "INSERT INTO `keystore` (`key`, `wert`, `mandantID`) VALUES ('Nummer1', '" + hash.createHashCode((String)"0") + "', " + mandant.getId() + "),('Nummer2', '00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ', " + mandant.getId() + ");";
        String sql69 = this.createDienstgradItems(bundesland, mandant);
        String sql70 = "CREATE TABLE IF NOT EXISTS `atemschutzpass` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`zeit` int(11) NOT NULL,`einsatzart` int(11) NOT NULL,`truppZuordnung` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql71 = "CREATE TABLE IF NOT EXISTS `atemschutzpass_einsatzart` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql72 = "INSERT INTO `atemschutzpass_einsatzart` (`id`, `name`) VALUES (1, 'PA'),(2, 'Filter'),(3, 'CSA');";
        String sql73 = "CREATE TABLE IF NOT EXISTS `abrechnung_konto` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql74 = "INSERT INTO `abrechnung_konto` (`id`, `name`, `mandantID`) VALUES (1, 'SYSTEM', " + mandant.getId() + ");";
        String sql75 = "CREATE TABLE IF NOT EXISTS `abrechnung_artikelklassen` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql76 = "INSERT INTO `abrechnung_artikelklassen` (`id`, `name`, `mandantID`) VALUES(1, 'Einsatz', " + mandant.getId() + "),(2, 'Dienstabend', " + mandant.getId() + "),(3, 'BSW', " + mandant.getId() + "),(4, 'Sonstige', " + mandant.getId() + "),(100, 'SYSTEM', " + mandant.getId() + "),(101, 'RABATT', " + mandant.getId() + ");";
        String sql77 = "CREATE TABLE IF NOT EXISTS `abrechnung_artikel` (`id` int(11) NOT NULL,`name` text NOT NULL,`klasse` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`wert` int(11) NOT NULL,`rabattwert` int(11) NOT NULL,`mwst` int(11) NOT NULL,`berechnungsart` int(11) NOT NULL,`berechnungsart2` int(11) NOT NULL,`rabattart` int(11) NOT NULL,`aktiv` int(11) NOT NULL,`von` text NOT NULL,`bis` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql78 = "INSERT INTO `abrechnung_artikel` (`id`, `name`, `klasse`, `buchungskonto`, `zahlungsart`, `wert`, `rabattwert`, `mwst`, `berechnungsart`, `berechnungsart2`, `rabattart`, `aktiv`, `von`, `bis`, `mandantID`) VALUES(4000, 'Verg\u00fctung Brandsicherheitswache', 3, 1, 2, 0, 0, 1, 1, 1, 1, 0, '2015-01-01', '2099-12-31', " + mandant.getId() + "),(4001, 'Verg\u00fctung Einsatz', 1, 1, 2, 0, 0, 1, 1, 1, 0, 0, '2015-01-01', '2099-12-31', " + mandant.getId() + "),(4002, 'Verg\u00fctung Dienstabend', 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31', " + mandant.getId() + "),(4003, 'Verg\u00fctung Sonstige', 4, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31', " + mandant.getId() + ");";
        String sql79 = "CREATE TABLE IF NOT EXISTS `abrechnung` (`id` int(11) NOT NULL,`abrechnungID` int(11) NOT NULL,`artikelID` int(11) NOT NULL,`buchungskonto` int(11) NOT NULL,`zahlungsart` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`veranstaltungKategorie` int(11) NOT NULL,`wert` int(11) NOT NULL,`menge` int(11) NOT NULL,`datum` text NOT NULL,`status` int(11) NOT NULL,`umbuchungID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql80 = "CREATE TABLE IF NOT EXISTS `mitgliederakte_kommentar` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`kommentar` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql81 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_elemente` (`id` int(11) NOT NULL,`gruppe` text NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql82 = "INSERT INTO `einsatz_bericht_elemente` (`id`, `gruppe`, `name`, `mandantID`) VALUES(1, 'EinsatzArt', 'Kleinbrand', " + mandant.getId() + "),(2, 'EinsatzArt', 'Mittelbrand', " + mandant.getId() + "),(3, 'EinsatzArt', 'Gro\u00dfbrand', " + mandant.getId() + "),(4, 'EinsatzArt', 'Kaminbrand', " + mandant.getId() + "),(5, 'EinsatzArt', 'Gasaustr\u00f6mung', " + mandant.getId() + "),(6, 'EinsatzArt', 'Blinder Alarm', " + mandant.getId() + "),(7, 'EinsatzArt', 'B\u00f6swilliger Alarm', " + mandant.getId() + "),(8, 'EinsatzArt', 'Verkehrsunfall', " + mandant.getId() + "),(9, 'EinsatzArt', 'Verkehrsst\u00f6rung', " + mandant.getId() + "),(10, 'EinsatzArt', 'Einsturz', " + mandant.getId() + "),(11, 'EinsatzArt', 'Mensch in Notlage', " + mandant.getId() + "),(12, 'EinsatzArt', 'Gefahrgut (GSG)', " + mandant.getId() + "),(13, 'EinsatzArt', '\u00d6lspur', " + mandant.getId() + "),(14, 'EinsatzArt', 'Tier in Notlage', " + mandant.getId() + "),(15, 'EinsatzArt', 'Tiertranssport', " + mandant.getId() + "),(16, 'EinsatzArt', 'Tierkadaver', " + mandant.getId() + "),(17, 'EinsatzArt', 'Betriebsunfall', " + mandant.getId() + "),(18, 'EinsatzArt', 'TH Baum', " + mandant.getId() + "),(19, 'EinsatzArt', 'TH Wasser', " + mandant.getId() + "),(20, 'EinsatzArt', 'keine Angaben', " + mandant.getId() + "),(101, 'Stelle', 'Keller', " + mandant.getId() + "),(102, 'Stelle', 'Erdgeschlo\u00df', " + mandant.getId() + "),(103, 'Stelle', 'Obergescho\u00df', " + mandant.getId() + "),(104, 'Stelle', 'Dachgescho\u00df', " + mandant.getId() + "),(105, 'Stelle', 'Bungalow', " + mandant.getId() + "),(106, 'Stelle', 'Baustelle', " + mandant.getId() + "),(107, 'Stelle', 'Freigel\u00e4nde', " + mandant.getId() + "),(108, 'Stelle', 'Auf dem Wasser', " + mandant.getId() + "),(109, 'Stelle', 'Stra\u00dfe', " + mandant.getId() + "),(110, 'Stelle', 'Autobahn', " + mandant.getId() + "),(111, 'Stelle', 'Landstra\u00dfe', " + mandant.getId() + "),(112, 'Stelle', 'Kraftfahrstra\u00dfe', " + mandant.getId() + "),(113, 'Stelle', 'keine Angaben', " + mandant.getId() + "),(201, 'Objekt', 'Wohngeb\u00e4ude', " + mandant.getId() + "),(202, 'Objekt', 'Verwaltungsgeb\u00e4ude', " + mandant.getId() + "),(203, 'Objekt', 'Landwirdschaftl. Geb\u00e4ude', " + mandant.getId() + "),(204, 'Objekt', 'Industriebetrieb', " + mandant.getId() + "),(205, 'Objekt', 'gewerbl. Betrieb', " + mandant.getId() + "),(206, 'Objekt', 'Fahrzeug', " + mandant.getId() + "),(207, 'Objekt', 'Wald', " + mandant.getId() + "),(208, 'Objekt', 'Feld', " + mandant.getId() + "),(209, 'Objekt', 'Grasnarbe', " + mandant.getId() + "),(210, 'Objekt', 'keine Angaben', " + mandant.getId() + "),(301, 'Alamierung', 'Digitaler Meldeempf\u00e4nger (DME)', " + mandant.getId() + "),(302, 'Alamierung', 'Leitstelle Feuerwehr', " + mandant.getId() + "),(303, 'Alamierung', 'Leitstelle Polizei', " + mandant.getId() + "),(304, 'Alamierung', 'Telefon', " + mandant.getId() + "),(305, 'Alamierung', 'Sirene', " + mandant.getId() + "),(401, 'Ausdehnung', 'Auf Entstehungrum begrenzt', " + mandant.getId() + "),(402, 'Ausdehnung', 'vor Eintreffen (auf andere R\u00e4ume \u00fcbgergeriffen)', " + mandant.getId() + "),(403, 'Ausdehnung', 'vor Eintreffen (auf andere Geb\u00e4ude \u00fcbgergeriffen)', " + mandant.getId() + "),(404, 'Ausdehnung', 'w\u00e4hrend der Brandbek\u00e4mpfung (auf andere R\u00e4ume \u00fcbgergeriffen)', " + mandant.getId() + "),(405, 'Ausdehnung', 'w\u00e4hrend der Brandbek\u00e4mpfung (auf andere Geb\u00e4ude \u00fcbgergeriffen)', " + mandant.getId() + "),(406, 'Ausdehnung', 'keine Angaben', " + mandant.getId() + ");";
        String sql83 = "CREATE TABLE IF NOT EXISTS `einsatz_bericht_daten` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`einsatzID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`einsatzArt` int(11) NOT NULL,`stelle` int(11) NOT NULL,`objekt` int(11) NOT NULL,`eigentuemerName` text NOT NULL,`eigentuemerAnschrift` text NOT NULL,`eigentuemerTelefon` text NOT NULL,`verursacherName` text NOT NULL,`verursacherAnschrift` text NOT NULL,`verursacherTelefon` text NOT NULL,`alamierung` int(11) NOT NULL,`meldenderName` text NOT NULL,`meldenderAnschrift` text NOT NULL,`meldenderTelefon` text NOT NULL,`lage` text NOT NULL,`verlauf` text NOT NULL,`eingesetzteGeraete` text NOT NULL,`verbrauchWasser` text NOT NULL,`verbrauchSchaum` text NOT NULL,`verbrauchPulver` text NOT NULL,`verbrauchBindemittel` text NOT NULL,`vorEintreffenGeloescht` int(11) NOT NULL,`schnellangriff` int(11) NOT NULL,`crohr` text NOT NULL,`brohr` text NOT NULL,`kleinloeschgeraet` text NOT NULL,`tragbareLeitern` int(11) NOT NULL,`atemschutzgeraet` text NOT NULL,`fluchthauben` text NOT NULL,`belueftungsgeraet` text NOT NULL,`rettungsgeraet` int(11) NOT NULL,`ausdehnung` int(11) NOT NULL,`entstehungsursache` text NOT NULL,`verletzte` text NOT NULL,`gerettete` text NOT NULL,`tote` text NOT NULL,`schadenhoehe` text NOT NULL,`brandwacheFahrzeug` int(11) NOT NULL,`staerke` text NOT NULL,`dauer` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql84 = "CREATE TABLE IF NOT EXISTS `ftpsync` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL,`ordner` text NOT NULL,`status` int(11) NOT NULL, `statusDB` int(11) NOT NULL,`groesse` BIGINT NOT NULL DEFAULT '0', `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql85 = "INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`, `statusDB`, `mandantID`) VALUES(1, 'SYSTEM', '', 'data', 0, 0, " + mandant.getId() + "),(2, 'SYSTEM', '', 'data/Templates', 0, 0, " + mandant.getId() + "),(3, 'SYSTEM', '', 'data/Papierkorb', 0, 0, " + mandant.getId() + "),(4, 'SYSTEM', '', 'data/Eigene Dateien', 0, 0, " + mandant.getId() + "),(5, 'SYSTEM', '', 'data/Mitgliederakte', 0, 0, " + mandant.getId() + "),(6, 'SYSTEM', '', 'data/Fahrzeugakte', 0, 0, " + mandant.getId() + "),(7, 'SYSTEM', '', 'data/EMail', 0, 0, " + mandant.getId() + "),(8, 'SYSTEM', '', 'data/EMail/Anhang', 0, 0, " + mandant.getId() + "),(9, 'SYSTEM', '', 'data/EMail/Anhang/Gesendet', 0, 0, " + mandant.getId() + "),(10, 'SYSTEM', '', 'data/EMail/Anhang/Entwurf', 0, 0, " + mandant.getId() + "),(11, 'SYSTEM', '', 'data/EMail/Anhang/Empfangende', 0, 0, " + mandant.getId() + "),(12, 'SYSTEM', '', 'data/EMail/Temp', 0, 0, " + mandant.getId() + "),(13, 'SYSTEM', '', 'data/EMail/Temp/original_nachricht', 0, 0, " + mandant.getId() + "),(14, 'SYSTEM', '', 'data/DBBACKUP', 0, 0, " + mandant.getId() + "),(15, 'SYSTEM', '', 'data/Bestandsliste', 0, 0, " + mandant.getId() + "),(16, 'SYSTEM', '', 'data/Abrechnung', 0, 0, " + mandant.getId() + "),(17, 'SYSTEM', '', 'data/Ausbildungsunterlagen', 0, 0, " + mandant.getId() + "),(18, 'SYSTEM', 'data/Templates/Einsatzbericht.docx', '', 0, 0, " + mandant.getId() + "),(19, 'SYSTEM', 'data/Templates/Einsatzbericht.xml', '', 0, 0, " + mandant.getId() + "),(20, 'SYSTEM', 'data/Templates/M\u00e4ngelmeldung.docx', '', 0, 0, " + mandant.getId() + "),(21, 'SYSTEM', 'data/Templates/M\u00e4ngelmeldung.xml', '', 0, 0, " + mandant.getId() + "),(22, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0, 0, " + mandant.getId() + "),(23, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0, 0, " + mandant.getId() + "),(24, 'SYSTEM', '', 'data/KarteBilder', 0, 0, " + mandant.getId() + "),(25, 'SYSTEM', '', 'data/KarteBilder/gro\u00df', 0, 0, " + mandant.getId() + "),(26, 'SYSTEM', '', 'data/KarteBilder/klein', 0, 0, " + mandant.getId() + "),(27, 'SYSTEM', '', 'data/Atemschutz', 0, 0, " + mandant.getId() + ");";
        String sql86 = "CREATE TABLE IF NOT EXISTS `ftpsync_del` (`id` int(11) NOT NULL,`clientID` text NOT NULL,`datei` text NOT NULL, `status` int(11) NOT NULL, `statusDB` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql87 = "CREATE TABLE IF NOT EXISTS `schicht_mitglieder` (`schichtID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql88 = "CREATE TABLE IF NOT EXISTS `schicht_gruppen_mitglieder` (`gruppenID` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql89 = "CREATE TABLE IF NOT EXISTS `schicht_gruppe` (`id` int(11) NOT NULL,`name` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql90 = "CREATE TABLE IF NOT EXISTS `fahrtenbuch` (`id` int(11) NOT NULL,`fahrzeugID` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`datumVon` text NOT NULL,`zeitVon` text NOT NULL,`datumBis` text NOT NULL,`zeitBis` text NOT NULL,`kmBeginn` int(11) NOT NULL,`kmEnde` int(11) NOT NULL,`distance` int(11) NOT NULL,`tanken` text NOT NULL,`pumpenbetrieb` text NOT NULL,`sonstiges` text NOT NULL,`fahrer` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql91 = "CREATE TABLE IF NOT EXISTS `mitglieder_laufbahn` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datumVon` text NOT NULL,`datum` text NOT NULL,`art` text NOT NULL,`alterDienstgrad` int(11) NOT NULL,`neuerDienstgrad` int(11) NOT NULL,`lehrgang` int(11) NOT NULL,`ue` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql92 = "CREATE TABLE IF NOT EXISTS `statistiklehrgang` (`id` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`lehrgangID` int(11) NOT NULL,`dauer` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql93 = "CREATE TABLE IF NOT EXISTS `schicht` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`name` text NOT NULL,`datumVon` text NOT NULL,`uhrVon` text NOT NULL,`datumBis` text NOT NULL,`uhrBis` text NOT NULL,`von` int(11) NOT NULL,`bis` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql94 = "CREATE TABLE IF NOT EXISTS `systemwarnung` (`id` int(11) NOT NULL,`datum` text NOT NULL,`zeit` text NOT NULL,`info` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql95 = "CREATE TABLE IF NOT EXISTS `ftpsync_error` (`clientID` text NOT NULL, `datei` text NOT NULL,`ordner` text NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql96 = "CREATE TABLE IF NOT EXISTS `urlaub` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`mitgliederID` int(11) NOT NULL,`datumVon` text NOT NULL,`datumBis` text NOT NULL, `loeschkenner` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql97 = "CREATE TABLE IF NOT EXISTS `bef\u00f6rderung_konfiguration` (`id` int(11) NOT NULL,`dienstgradID` int(11) NOT NULL,`dienstgradVoraussetzung` int(11) NOT NULL,`zeit` int(11) NOT NULL,`dienstZeit` int(11) NOT NULL,`nurZeitBefoerderung` int(11) NOT NULL,`letzteStufe` int(11) NOT NULL,`auslassen` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql98 = "CREATE TABLE IF NOT EXISTS `bef\u00f6rderung_erforderlich` (`id` int(11) NOT NULL, `lehrgangID` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql99 = "CREATE TABLE IF NOT EXISTS `mitglieder_history` (`changeDate` text NOT NULL,`changeTime` text NOT NULL,`benutzer` text NOT NULL, `id` int(11) NOT NULL,`mitgliederGruppe` int(11) NOT NULL,`anrede` int(11) NOT NULL,`name` text NOT NULL,`vorname` text NOT NULL,`strasse` text NOT NULL,`ort` text NOT NULL,`telefonPrivat` text NOT NULL,`telefonMobil` text NOT NULL,`telefonArbeit` text NOT NULL,`telegrammID`  text NOT NULL,`email` text NOT NULL,`email2` text NOT NULL, `beruf` text NOT NULL, `dienstgrad` int(11) NOT NULL,`ausserDienst` int(11) NOT NULL,`mitgliedSeit` text NOT NULL,`mitgliedBis` text NOT NULL, `gebDatum` text NOT NULL,`hochzeit` text NOT NULL,`kommentar` text NOT NULL,`loeschkenner` int(11) NOT NULL, `eMailDeaktiv` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql100 = "CREATE TABLE IF NOT EXISTS `dateisystem` (`id` int(11) NOT NULL, `dateiStream` longblob NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql101 = "CREATE TABLE IF NOT EXISTS `mandant` (`id` int(11) NOT NULL,`name` text NOT NULL,`bf` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql102 = "INSERT INTO mandant (`id` ,`name`, `bf`) VALUES (" + mandant.getId() + ", '" + mandant.getName() + "', " + mandant.getBf() + ");";
        String sql103 = "CREATE TABLE IF NOT EXISTS `clients` (`id` int(11) NOT NULL,`clientID` text NOT NULL, `alias` text NOT NULL, `typ` text NOT NULL,`online` int(11) NOT NULL, `zugelassen` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql104 = "INSERT INTO clients (`id` ,`clientID`, `alias`,`typ`,`online`, `zugelassen`, `mandantID`) VALUES (1, '" + clientID + "', '" + InetAddress.getLocalHost() + "', '" + "FMS" + "', 1,  1, " + mandant.getId() + ");";
        String sql105 = "CREATE TABLE IF NOT EXISTS `statistikmitglieder` (`id` int(11) NOT NULL,`jahr` int(11) NOT NULL,`alterGes` int(11) NOT NULL,`anzahl` int(11) NOT NULL,`erstellung` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql106 = "CREATE TABLE IF NOT EXISTS `php` (`id` int(11) NOT NULL,`typ` text NOT NULL,`adresse` text NOT NULL,`parameter` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql107 = "CREATE TABLE IF NOT EXISTS `protokoll` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`jahr` int(11) NOT NULL,`title` text NOT NULL,`protokolltext` text NOT NULL,`erstelldatum` text NOT NULL,`mandantID` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql108 = "CREATE TABLE IF NOT EXISTS `einsatz_organisationen` (`id` int(11) NOT NULL,`veranstaltungID` int(11) NOT NULL,`organisationID` int(11) NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql109 = "CREATE TABLE IF NOT EXISTS `organisationen` (`id` int(11) NOT NULL,`name` text NOT NULL,`sortierung` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql110 = "INSERT INTO `organisationen` (`id`, `name`, `sortierung`, `mandantID`) VALUES(1, '', 0, " + mandant.getId() + ");";
        String sql111 = "CREATE TABLE IF NOT EXISTS `ehrungen_konfiguration` (`id` int(11) NOT NULL,`ehrungID` int(11) NOT NULL, `zeit` int(11) NOT NULL, `mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql112 = "CREATE TABLE IF NOT EXISTS `email_unwetterwarnung` (`id` int(11) NOT NULL,`sender` text NOT NULL,`betreff` text NOT NULL,`nachricht` text NOT NULL,`date` text NOT NULL,`size` int(11) NOT NULL,`anhang` int(11) NOT NULL,`gelesen` int(11) NOT NULL,`art` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql113 = "CREATE TABLE IF NOT EXISTS `berechtigung` (`id` int(11) NOT NULL,`name` text NOT NULL,`seite` int(11) NOT NULL, `gruppe` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql115 = "CREATE TABLE IF NOT EXISTS `mitglieder_verfuegbarkeit` (`id` int(11) NOT NULL,`mitgliedID` int(11) NOT NULL,`telegrammID` text NOT NULL,`status` int(11) NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql116 = "CREATE TABLE IF NOT EXISTS `einstellungen_gespeichert` (`key` text NOT NULL,`wert` text NOT NULL,`mandantID` int(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql117 = "INSERT INTO `einstellungen_gespeichert` (`key`, `wert`, `mandantID`) VALUES ('G25', '', '" + mandant.getId() + "'), ('G26', '', '" + mandant.getId() + "'),('Dienstausweis', '', '" + mandant.getId() + "'), ('Fahrberechtigung', '', '" + mandant.getId() + "'),('G30', '', '" + mandant.getId() + "'),('unwetterwarnungDatumBis', 'null', '" + mandant.getId() + "'), ('unwetterwarnungUhrzeitBis', 'null', '" + mandant.getId() + "'),('ErhalteneInfoMeldung', '', '" + mandant.getId() + "'),('ZyklischerEMailAuftrag', '0', '" + mandant.getId() + "');";
        String sql118 = "INSERT INTO `berechtigung` (`id`, `name`, `seite`, `gruppe`) VALUES(0, 'Dienstgrad anlegen', 1, 6),(1, 'Mitgliederverwaltung', 1, 2),(2, 'Mitglied l\u00f6schen / au\u00dfer Dienst', 1, 2),(3, 'Mitgliederliste + Geburtstagsliste', 1, 3),(4, 'Einsatzliste', 1, 3),(5, 'Brandsicherheitswachliste', 1, 3),(6, 'Lehrgangsliste', 1, 3),(7, 'Anwesenheitsliste', 1, 3),(8, 'Arbeitgeberliste', 1, 3),(9, 'Angeh\u00f6rigenliste', 1, 3),(10, 'Untersuchungsliste', 1, 3),(11, 'Beteiligungs\u00fcbersicht', 1, 3),(12, 'Bankverbindungsliste', 1, 3),(13, 'Veranstaltungsliste', 1, 3),(14, 'Dokumentenexplorer', 1, 4),(15, 'Einsatzbericht erstellen', 1, 1),(16, 'Verdienstausfallbescheinigung', 1, 4),(17, 'Jahresbericht erstellen', 1, 4),(18, 'Brief erstellen', 1, 4),(19, 'M\u00e4ngelmeldung', 1, 4),(20, 'Anwesenheit Gesamt', 1, 5),(21, 'Anwesenheit Einsatz', 1, 5),(22, 'Anwesenheit Dienstabend', 1, 5),(23, 'Anwesenheit BSW', 1, 5),(24, 'Abwesenheitsstatistik', 1, 5),(25, 'Einsatzart / Stichwort', 1, 5),(26, 'Ausr\u00fcckezeiten', 1, 5),(27, 'Einsatzdauer', 1, 5),(28, 'Mannstunden Einsatz', 1, 5),(29, 'Einsatz pro Monat', 1, 5),(30, 'Einsatz pro Stunde', 1, 5),(31, 'Einsatz pro Woche', 1, 5),(32, 'BSW Mannstunden', 1, 5),(33, 'Fehlalarme', 1, 5),(34, 'Beteiligung bei...', 1, 5),(35, 'Ausbildungsstatistik', 1, 5),(36, 'Fahrzeug Statistik', 1, 5),(37, 'Alarmfahrtdauer', 1, 5),(38, 'Fahrzeuggruppe anlegen', 1, 6),(39, 'Stichwort anlegen', 1, 6),(40, 'Veranstaltungskategorie anlegen', 1, 6),(41, 'Fahrzeug au\u00dfer Dienst', 1, 2),(42, 'Abwesenheitgrund erstellen', 1, 6),(43, 'Programmeinstellungen', 1, 6),(44, 'Anwesenheit eintragen', 1, 1),(45, 'Abwesenehit eintragen', 1, 1),(46, 'Mitgliedergruppe erstellen / l\u00f6schen', 1, 6),(47, 'Ausbildungsinhalte eintragen', 1, 1),(48, 'Fahrzeugverwaltung', 1, 2),(49, 'Ger\u00e4tepr\u00fcfung', 1, 6),(50, 'Fahrzeugeinteilung eintragen', 1, 1),(51, 'Benutzerverwaltung', 1, 6),(52, 'Karte / Einsatzgebiet editieren', 1, 6),(53, 'Bef\u00f6r. / Lehrgangsmeldung', 1, 3),(54, 'Mitgliederanrede erstellen', 1, 6),(55, 'E-Mail senden/schreiben', 1, 4),(56, 'Bestandsverwaltung', 1, 4),(57, 'Bestandsverwaltung organisieren', 1, 4),(58, 'Bestandsverwaltung Artikel anlegen', 1, 4),(59, 'Mitgliederakte', 1, 2),(60, 'Fahrzeugakte', 1, 2),(61, 'Beteiligungszeit', 1, 5),(62, 'Datensicherung', 1, 6),(63, 'Ausbildungsplan erstellen', 1, 4),(64, 'Ausbildungsplan', 1, 3),(65, 'virtuelles Lager leeren', 1, 4),(66, 'M\u00e4ngelmeldung bearbeiten', 1, 4),(67, 'Lehrgang anlegen', 1, 6),(68, 'Atemschutzpass eintragen', 1, 1),(69, 'Atemschutzpass', 1, 3),(70, 'Abrechnung', 1, 2),(71, 'Abrechnung - Artikel', 1, 2),(72, 'Abrechnung - Konto', 1, 2),(73, 'Abrechnung - manuelle Verbuchung', 1, 2),(74, 'Veranstaltung editieren (24 Std.)', 1, 1),(75, 'Fahrtenbuch eintrag', 1, 1),(76, 'Schichtplaner', 1, 1),(77, 'Mitgliederlaufbahn pflegen', 1, 2),(78, 'Fahrtenbuchliste', 1, 3),(79, 'Schichtplanerliste', 1, 3),(80, 'Mitgliederlaufbahnliste', 1, 3),(81, 'Einsatz anlegen', 1, 1),(82, 'Dienstabend anlegen', 1, 1),(83, 'BSW anlegen', 1, 1),(84, 'Sonstige Veranstaltung', 1, 1),(85, 'Anwesenheit l\u00f6schen', 1, 1),(86, 'Lehrg\u00e4nge Mitgliedern hinzuf\u00fcgen', 1, 2),(87, 'Urlaubsplaner', 1, 1),(88, 'Protokoll / T\u00e4tigkeitsbericht', 1, 4),(89, 'Protokoll lesen', 1, 4),(90, 'Organisationen erstellen', 1, 6),(91, 'Stichwort-Kategorie', 1, 6),(92, 'Mitgliederlaufbahnen editieren', 1, 2),(93, 'Mitgliederverwaltung editieren', 1, 2),(94, 'Mitgliederuntersuchung', 1, 2),(95, 'Einsatzbericht neu erstellen', 1, 1),(0, 'Termine anzeigen', 2, 7),(1, 'Geburtstage anzeigen', 2, 7),(2, 'G26 anzeigen', 2, 7),(3, 'G25 anzeigen', 2, 7),(4, 'G30 anzeigen', 2, 7),(5, 'LKW F\u00fchrerschein Ablauf anzeigen', 2, 7),(6, 'AGT Training anzeigen', 2, 7),(7, 'Ablauf Dienstausweis anzeigen', 2, 7),(8, 'Ablauf Fahrberechtigung anzeigen', 2, 7),(9, 'Abgelaufene G26 anzeigen', 2, 7),(10, 'Abgelaufene G25 anzeigen', 2, 7),(11, 'Abgelaufene G30 anzeigen', 2, 7),(12, 'Abgelaufenes AGT Training anzeigen', 2, 7),(13, 'Abgelaufene LKW F\u00fchrerscheine anzeigen', 2, 7),(14, 'Abgelaufene Dienstausweise anzeigen', 2, 7),(15, 'Abgelaufene Fahrberechtigung anzeigen', 2, 7),(16, 'T\u00dcV anzeigen', 2, 7),(17, 'Sicherheitspr\u00fcfung anzeigen', 2, 7),(18, 'Fahrzeug Wartung anzeigen', 2, 7),(19, 'Gaswartung anzeigen', 2, 7),(20, 'Abgelaufener T\u00dcV anzeigen', 2, 7),(21, 'Abgelaufener SP anzeigen', 2, 7),(22, 'Abgelaufene Wartung anzeigen', 2, 7),(23, 'Abgelaufener Gaswartung anzeigen', 2, 7),(24, 'Ger\u00e4tepr\u00fcfung anzeigen', 2, 7),(25, 'M\u00e4ngelmeldungen anzeigen', 2, 7),(26, 'Anwesenheit Sonstige Veranstaltung', 2, 5),(27, 'Verf\u00fcgbarkeit Einsatz', 2, 5),(28, 'Einsatz - Stadtteilstatistik', 2, 5),(29, 'Schutzzielstatistik', 2, 5),(30, 'Tag / Nacht Eins\u00e4tze', 2, 5),(31, 'Fahrzeugbelegung (Einsatz)', 2, 5),(32, 'Atemschutzstatistik', 2, 5),(33, 'Veranstaltungz\u00e4hlung', 2, 5),(34, 'Durchscnittsalter', 2, 5),(35, 'Mitgliederzahlen', 2, 5),(36, 'Mitglieder Dienstgrad', 2, 5),(37, 'Mitglieder Funktionen (Anzahl)', 2, 5),(38, 'Veranstaltung Editieren - Einsatz bearbeiten', 2, 1),(39, 'D.-Explorer - Abrechnungen anzeigen', 2, 4),(40, 'D.-Explorer - Ausbildungunterlagen anzeigen', 2, 4),(41, 'D.-Explorer - Bestandslisten', 2, 4),(42, 'D.-Explorer - Eigene Dateien', 2, 4),(43, 'D.-Explorer - Verdienstausfallb.', 2, 4),(44, 'D.-Explorer - M\u00e4ngelmeldungen', 2, 4),(45, 'D.-Explorer - Lehrgangsmeldungen', 2, 4),(46, 'D.-Expolrer - Fahrzeugeinteilung', 2, 4),(47, 'D.-Explorer - Einsatzbereichte', 2, 4),(48, 'D.-Explorer - Briefe', 2, 4),(49, 'D.-Explorer - Beteiligungs\u00fcbersicht', 2, 4),(50, 'D.-Explorer - Berichte', 2, 4),(51, 'D.-Explorer - Atemschutz', 2, 4),(52, 'Mitgliederliste - Zausatzdaten', 2, 3),(53, 'Sonstige Mannstunden', 2, 5),(54, 'frei54', 2, 0),(55, 'frei55', 2, 0),(56, 'frei56', 2, 0),(57, 'frei57', 2, 0),(58, 'frei58', 2, 0),(59, 'frei59', 2, 0),(60, 'frei60', 2, 0),(61, 'frei61', 2, 0),(62, 'frei62', 2, 0),(63, 'frei63', 2, 0),(64, 'frei64', 2, 0),(65, 'frei65', 2, 0),(66, 'frei66', 2, 0),(67, 'frei67', 2, 0),(68, 'frei68', 2, 0),(69, 'frei69', 2, 0),(70, 'frei70', 2, 0),(71, 'frei71', 2, 0),(72, 'frei72', 2, 0),(73, 'frei73', 2, 0),(74, 'frei74', 2, 0),(75, 'frei75', 2, 0),(76, 'frei76', 2, 0),(77, 'frei77', 2, 0),(78, 'frei78', 2, 0),(79, 'frei79', 2, 0),(80, 'frei80', 2, 0),(81, 'frei81', 2, 0),(82, 'frei82', 2, 0),(83, 'frei83', 2, 0),(84, 'frei84', 2, 0),(85, 'frei85', 2, 0),(86, 'frei86', 2, 0),(87, 'frei87', 2, 0),(88, 'frei88', 2, 0),(89, 'frei89', 2, 0),(90, 'frei90', 2, 0),(91, 'frei91', 2, 0),(92, 'frei92', 2, 0),(93, 'frei93', 2, 0),(94, 'frei94', 2, 0),(95, 'frei95', 2, 0);";
        String sql119 = "INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`, `BR88`, `BR89`, `BR90`, `BR91`, `BR92`, `BR93`, `BR94`, `BR95`, `mandantID`) VALUES (0, 2, 'Public', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, " + mandant.getId() + ");";
        String sql120 = "INSERT INTO `berechtigunggruppe` (`id`, `seite`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`, `BR76`, `BR77`, `BR78`, `BR79`, `BR80`, `BR81`, `BR82`, `BR83`, `BR84`, `BR85`, `BR86`, `BR87`, `BR88`, `BR89`, `BR90`, `BR91`, `BR92`, `BR93`, `BR94`, `BR95`, `mandantID`) VALUES (1, 2, 'Administrator', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, " + mandant.getId() + ");";
        String sql121 = "CREATE TABLE IF NOT EXISTS `berechtigung_gruppe_name` (`id` int(11) NOT NULL,`name` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String sql122 = "INSERT INTO `berechtigung_gruppe_name` (`id`, `name`) VALUES ('1', 'Veranstaltungen / Anwesenheit'), ('2', 'Mitglieder- / Fahrzeugverwaltung'), ('3', 'Listen'), ('4', 'Berichte / Dokumente'), ('5', 'Statistik'), ('6', 'Optionen / Verwaltung'), ('7', 'Informationsbereich');";
        logging.logSQL((Object)sql1);
        statement.executeUpdate(sql1);
        ProzessBarAO.progressbar.setValue(100 / gesamtStatements);
        logging.logSQL((Object)sql2);
        statement.executeUpdate(sql2);
        ProzessBarAO.progressbar.setValue(200 / gesamtStatements);
        logging.logSQL((Object)sql3);
        statement.executeUpdate(sql3);
        ProzessBarAO.progressbar.setValue(300 / gesamtStatements);
        logging.logSQL((Object)sql4);
        statement.executeUpdate(sql4);
        ProzessBarAO.progressbar.setValue(400 / gesamtStatements);
        logging.logSQL((Object)sql5);
        statement.executeUpdate(sql5);
        ProzessBarAO.progressbar.setValue(500 / gesamtStatements);
        logging.logSQL((Object)sql6);
        statement.executeUpdate(sql6);
        ProzessBarAO.progressbar.setValue(600 / gesamtStatements);
        logging.logSQL((Object)sql7);
        statement.executeUpdate(sql7);
        ProzessBarAO.progressbar.setValue(700 / gesamtStatements);
        logging.logSQL((Object)sql8);
        statement.executeUpdate(sql8);
        ProzessBarAO.progressbar.setValue(800 / gesamtStatements);
        logging.logSQL((Object)sql9);
        statement.executeUpdate(sql9);
        ProzessBarAO.progressbar.setValue(900 / gesamtStatements);
        logging.logSQL((Object)sql10);
        statement.executeUpdate(sql10);
        ProzessBarAO.progressbar.setValue(1000 / gesamtStatements);
        logging.logSQL((Object)sql11);
        statement.executeUpdate(sql11);
        ProzessBarAO.progressbar.setValue(1100 / gesamtStatements);
        logging.logSQL((Object)sql12);
        statement.executeUpdate(sql12);
        ProzessBarAO.progressbar.setValue(1200 / gesamtStatements);
        logging.logSQL((Object)sql13);
        statement.executeUpdate(sql13);
        ProzessBarAO.progressbar.setValue(1300 / gesamtStatements);
        logging.logSQL((Object)sql14);
        statement.executeUpdate(sql14);
        ProzessBarAO.progressbar.setValue(1400 / gesamtStatements);
        logging.logSQL((Object)sql15);
        statement.executeUpdate(sql15);
        ProzessBarAO.progressbar.setValue(1500 / gesamtStatements);
        logging.logSQL((Object)sql16);
        statement.executeUpdate(sql16);
        ProzessBarAO.progressbar.setValue(1600 / gesamtStatements);
        logging.logSQL((Object)sql17);
        statement.executeUpdate(sql17);
        ProzessBarAO.progressbar.setValue(1700 / gesamtStatements);
        logging.logSQL((Object)sql18);
        statement.executeUpdate(sql18);
        ProzessBarAO.progressbar.setValue(1800 / gesamtStatements);
        logging.logSQL((Object)sql19);
        statement.executeUpdate(sql19);
        ProzessBarAO.progressbar.setValue(1900 / gesamtStatements);
        if (mandant.getId() == 1) {
            logging.logSQL((Object)sql20);
            statement.executeUpdate(sql20);
            ProzessBarAO.progressbar.setValue(2000 / gesamtStatements);
        }
        logging.logSQL((Object)sql21);
        statement.executeUpdate(sql21);
        ProzessBarAO.progressbar.setValue(2100 / gesamtStatements);
        logging.logSQL((Object)sql22);
        statement.executeUpdate(sql22);
        ProzessBarAO.progressbar.setValue(2200 / gesamtStatements);
        logging.logSQL((Object)sql23);
        statement.executeUpdate(sql23);
        ProzessBarAO.progressbar.setValue(2300 / gesamtStatements);
        logging.logSQL((Object)sql24);
        statement.executeUpdate(sql24);
        ProzessBarAO.progressbar.setValue(2400 / gesamtStatements);
        logging.logSQL((Object)sql25);
        statement.executeUpdate(sql25);
        ProzessBarAO.progressbar.setValue(2500 / gesamtStatements);
        logging.logSQL((Object)sql26);
        statement.executeUpdate(sql26);
        ProzessBarAO.progressbar.setValue(2600 / gesamtStatements);
        logging.logSQL((Object)sql27);
        statement.executeUpdate(sql27);
        ProzessBarAO.progressbar.setValue(2700 / gesamtStatements);
        logging.logSQL((Object)sql28);
        statement.executeUpdate(sql28);
        ProzessBarAO.progressbar.setValue(2800 / gesamtStatements);
        logging.logSQL((Object)sql29);
        statement.executeUpdate(sql29);
        ProzessBarAO.progressbar.setValue(2900 / gesamtStatements);
        logging.logSQL((Object)sql30);
        statement.executeUpdate(sql30);
        if (mandant.getBf() == 1) {
            statement.executeUpdate("UPDATE einstellungen set `wert` = 0 where `key` = 'modulVeranstaltung' and mandantID = " + mandant.getId() + ";");
            statement.executeUpdate("UPDATE einstellungen set `wert` = 0 where `key` = 'modulAusbildungsplan' and mandantID = " + mandant.getId() + ";");
            statement.executeUpdate("UPDATE einstellungen set `wert` = 0 where `key` = 'modulFahrzeugeinteilung' and mandantID = " + mandant.getId() + ";");
        }
        ProzessBarAO.progressbar.setValue(3000 / gesamtStatements);
        logging.logSQL((Object)sql31);
        statement.executeUpdate(sql31);
        ProzessBarAO.progressbar.setValue(3100 / gesamtStatements);
        logging.logSQL((Object)sql32);
        statement.executeUpdate(sql32);
        ProzessBarAO.progressbar.setValue(3200 / gesamtStatements);
        logging.logSQL((Object)sql33);
        statement.executeUpdate(sql33);
        ProzessBarAO.progressbar.setValue(3300 / gesamtStatements);
        logging.logSQL((Object)sql34);
        statement.executeUpdate(sql34);
        ProzessBarAO.progressbar.setValue(3400 / gesamtStatements);
        logging.logSQL((Object)sql35);
        statement.executeUpdate(sql35);
        ProzessBarAO.progressbar.setValue(3500 / gesamtStatements);
        logging.logSQL((Object)sql36);
        statement.executeUpdate(sql36);
        ProzessBarAO.progressbar.setValue(3600 / gesamtStatements);
        logging.logSQL((Object)sql37);
        statement.executeUpdate(sql37);
        ProzessBarAO.progressbar.setValue(3700 / gesamtStatements);
        logging.logSQL((Object)sql38);
        statement.executeUpdate(sql38);
        ProzessBarAO.progressbar.setValue(3800 / gesamtStatements);
        logging.logSQL((Object)sql39);
        statement.executeUpdate(sql39);
        ProzessBarAO.progressbar.setValue(3900 / gesamtStatements);
        logging.logSQL((Object)sql40);
        statement.executeUpdate(sql40);
        ProzessBarAO.progressbar.setValue(4000 / gesamtStatements);
        logging.logSQL((Object)sql41);
        statement.executeUpdate(sql41);
        ProzessBarAO.progressbar.setValue(4100 / gesamtStatements);
        logging.logSQL((Object)sql42);
        statement.executeUpdate(sql42);
        ProzessBarAO.progressbar.setValue(4200 / gesamtStatements);
        logging.logSQL((Object)sql43);
        statement.executeUpdate(sql43);
        ProzessBarAO.progressbar.setValue(4300 / gesamtStatements);
        logging.logSQL((Object)sql44);
        statement.executeUpdate(sql44);
        ProzessBarAO.progressbar.setValue(4400 / gesamtStatements);
        logging.logSQL((Object)sql45);
        statement.executeUpdate(sql45);
        ProzessBarAO.progressbar.setValue(4500 / gesamtStatements);
        logging.logSQL((Object)sql46);
        statement.executeUpdate(sql46);
        ProzessBarAO.progressbar.setValue(4600 / gesamtStatements);
        logging.logSQL((Object)sql47);
        statement.executeUpdate(sql47);
        ProzessBarAO.progressbar.setValue(4700 / gesamtStatements);
        logging.logSQL((Object)sql48);
        statement.executeUpdate(sql48);
        ProzessBarAO.progressbar.setValue(4800 / gesamtStatements);
        logging.logSQL((Object)sql49);
        statement.executeUpdate(sql49);
        ProzessBarAO.progressbar.setValue(4900 / gesamtStatements);
        logging.logSQL((Object)sql50);
        statement.executeUpdate(sql50);
        ProzessBarAO.progressbar.setValue(5000 / gesamtStatements);
        logging.logSQL((Object)sql51);
        statement.executeUpdate(sql51);
        ProzessBarAO.progressbar.setValue(5100 / gesamtStatements);
        logging.logSQL((Object)sql52);
        statement.executeUpdate(sql52);
        ProzessBarAO.progressbar.setValue(5200 / gesamtStatements);
        logging.logSQL((Object)sql53);
        statement.executeUpdate(sql53);
        ProzessBarAO.progressbar.setValue(5300 / gesamtStatements);
        logging.logSQL((Object)sql54);
        statement.executeUpdate(sql54);
        ProzessBarAO.progressbar.setValue(5400 / gesamtStatements);
        logging.logSQL((Object)sql55);
        statement.executeUpdate(sql55);
        ProzessBarAO.progressbar.setValue(5500 / gesamtStatements);
        logging.logSQL((Object)sql56);
        statement.executeUpdate(sql56);
        ProzessBarAO.progressbar.setValue(5600 / gesamtStatements);
        logging.logSQL((Object)sql57);
        statement.executeUpdate(sql57);
        ProzessBarAO.progressbar.setValue(5700 / gesamtStatements);
        logging.logSQL((Object)sql58);
        statement.executeUpdate(sql58);
        ProzessBarAO.progressbar.setValue(5800 / gesamtStatements);
        logging.logSQL((Object)sql59);
        statement.executeUpdate(sql59);
        ProzessBarAO.progressbar.setValue(5900 / gesamtStatements);
        logging.logSQL((Object)sql60);
        statement.executeUpdate(sql60);
        ProzessBarAO.progressbar.setValue(6000 / gesamtStatements);
        logging.logSQL((Object)sql61);
        statement.executeUpdate(sql61);
        ProzessBarAO.progressbar.setValue(6100 / gesamtStatements);
        logging.logSQL((Object)sql62);
        statement.executeUpdate(sql62);
        ProzessBarAO.progressbar.setValue(6200 / gesamtStatements);
        logging.logSQL((Object)sql63);
        statement.executeUpdate(sql63);
        ProzessBarAO.progressbar.setValue(6300 / gesamtStatements);
        logging.logSQL((Object)sql64);
        statement.executeUpdate(sql64);
        ProzessBarAO.progressbar.setValue(6400 / gesamtStatements);
        logging.logSQL((Object)sql65);
        statement.executeUpdate(sql65);
        ProzessBarAO.progressbar.setValue(6500 / gesamtStatements);
        logging.logSQL((Object)sql66);
        statement.executeUpdate(sql66);
        ProzessBarAO.progressbar.setValue(6600 / gesamtStatements);
        logging.logSQL((Object)sql67);
        statement.executeUpdate(sql67);
        ProzessBarAO.progressbar.setValue(6700 / gesamtStatements);
        logging.logSQL((Object)sql68);
        statement.executeUpdate(sql68);
        ProzessBarAO.progressbar.setValue(6800 / gesamtStatements);
        logging.logSQL((Object)sql69);
        statement.executeUpdate(sql69);
        ProzessBarAO.progressbar.setValue(6900 / gesamtStatements);
        logging.logSQL((Object)sql70);
        statement.executeUpdate(sql70);
        ProzessBarAO.progressbar.setValue(7000 / gesamtStatements);
        logging.logSQL((Object)sql71);
        statement.executeUpdate(sql71);
        ProzessBarAO.progressbar.setValue(7100 / gesamtStatements);
        if (mandant.getId() == 1) {
            logging.logSQL((Object)sql72);
            statement.executeUpdate(sql72);
            ProzessBarAO.progressbar.setValue(7200 / gesamtStatements);
        }
        logging.logSQL((Object)sql73);
        statement.executeUpdate(sql73);
        ProzessBarAO.progressbar.setValue(7300 / gesamtStatements);
        logging.logSQL((Object)sql74);
        statement.executeUpdate(sql74);
        ProzessBarAO.progressbar.setValue(7400 / gesamtStatements);
        logging.logSQL((Object)sql75);
        statement.executeUpdate(sql75);
        ProzessBarAO.progressbar.setValue(7500 / gesamtStatements);
        logging.logSQL((Object)sql76);
        statement.executeUpdate(sql76);
        ProzessBarAO.progressbar.setValue(7600 / gesamtStatements);
        logging.logSQL((Object)sql77);
        statement.executeUpdate(sql77);
        ProzessBarAO.progressbar.setValue(7700 / gesamtStatements);
        logging.logSQL((Object)sql78);
        statement.executeUpdate(sql78);
        ProzessBarAO.progressbar.setValue(7800 / gesamtStatements);
        logging.logSQL((Object)sql79);
        statement.executeUpdate(sql79);
        ProzessBarAO.progressbar.setValue(7900 / gesamtStatements);
        logging.logSQL((Object)sql80);
        statement.executeUpdate(sql80);
        ProzessBarAO.progressbar.setValue(8000 / gesamtStatements);
        logging.logSQL((Object)sql81);
        statement.executeUpdate(sql81);
        ProzessBarAO.progressbar.setValue(8100 / gesamtStatements);
        logging.logSQL((Object)sql82);
        statement.executeUpdate(sql82);
        ProzessBarAO.progressbar.setValue(8200 / gesamtStatements);
        logging.logSQL((Object)sql83);
        statement.executeUpdate(sql83);
        ProzessBarAO.progressbar.setValue(8300 / gesamtStatements);
        logging.logSQL((Object)sql84);
        statement.executeUpdate(sql84);
        ProzessBarAO.progressbar.setValue(8400 / gesamtStatements);
        logging.logSQL((Object)sql85);
        statement.executeUpdate(sql85);
        ProzessBarAO.progressbar.setValue(8500 / gesamtStatements);
        logging.logSQL((Object)sql86);
        statement.executeUpdate(sql86);
        ProzessBarAO.progressbar.setValue(8600 / gesamtStatements);
        logging.logSQL((Object)sql87);
        statement.executeUpdate(sql87);
        ProzessBarAO.progressbar.setValue(8700 / gesamtStatements);
        logging.logSQL((Object)sql88);
        statement.executeUpdate(sql88);
        ProzessBarAO.progressbar.setValue(8800 / gesamtStatements);
        logging.logSQL((Object)sql89);
        statement.executeUpdate(sql89);
        ProzessBarAO.progressbar.setValue(8900 / gesamtStatements);
        logging.logSQL((Object)sql90);
        statement.executeUpdate(sql90);
        ProzessBarAO.progressbar.setValue(9000 / gesamtStatements);
        logging.logSQL((Object)sql91);
        statement.executeUpdate(sql91);
        ProzessBarAO.progressbar.setValue(9100 / gesamtStatements);
        logging.logSQL((Object)sql92);
        statement.executeUpdate(sql92);
        ProzessBarAO.progressbar.setValue(9200 / gesamtStatements);
        logging.logSQL((Object)sql93);
        statement.executeUpdate(sql93);
        ProzessBarAO.progressbar.setValue(9300 / gesamtStatements);
        logging.logSQL((Object)sql94);
        statement.executeUpdate(sql94);
        ProzessBarAO.progressbar.setValue(9400 / gesamtStatements);
        logging.logSQL((Object)sql95);
        statement.executeUpdate(sql95);
        ProzessBarAO.progressbar.setValue(9500 / gesamtStatements);
        logging.logSQL((Object)sql96);
        statement.executeUpdate(sql96);
        ProzessBarAO.progressbar.setValue(9600 / gesamtStatements);
        logging.logSQL((Object)sql97);
        statement.executeUpdate(sql97);
        ProzessBarAO.progressbar.setValue(9700 / gesamtStatements);
        logging.logSQL((Object)sql98);
        statement.executeUpdate(sql98);
        ProzessBarAO.progressbar.setValue(9800 / gesamtStatements);
        logging.logSQL((Object)sql99);
        statement.executeUpdate(sql99);
        ProzessBarAO.progressbar.setValue(9900 / gesamtStatements);
        logging.logSQL((Object)sql100);
        statement.executeUpdate(sql100);
        ProzessBarAO.progressbar.setValue(10000 / gesamtStatements);
        logging.logSQL((Object)sql101);
        statement.executeUpdate(sql101);
        ProzessBarAO.progressbar.setValue(10100 / gesamtStatements);
        logging.logSQL((Object)sql102);
        statement.executeUpdate(sql102);
        ProzessBarAO.progressbar.setValue(10200 / gesamtStatements);
        logging.logSQL((Object)sql103);
        statement.executeUpdate(sql103);
        ProzessBarAO.progressbar.setValue(10300 / gesamtStatements);
        logging.logSQL((Object)sql104);
        statement.executeUpdate(sql104);
        ProzessBarAO.progressbar.setValue(10400 / gesamtStatements);
        logging.logSQL((Object)sql105);
        statement.executeUpdate(sql105);
        ProzessBarAO.progressbar.setValue(10500 / gesamtStatements);
        logging.logSQL((Object)sql106);
        statement.executeUpdate(sql106);
        ProzessBarAO.progressbar.setValue(10600 / gesamtStatements);
        logging.logSQL((Object)sql107);
        statement.executeUpdate(sql107);
        ProzessBarAO.progressbar.setValue(10700 / gesamtStatements);
        logging.logSQL((Object)sql108);
        statement.executeUpdate(sql108);
        ProzessBarAO.progressbar.setValue(10800 / gesamtStatements);
        logging.logSQL((Object)sql109);
        statement.executeUpdate(sql109);
        ProzessBarAO.progressbar.setValue(10900 / gesamtStatements);
        logging.logSQL((Object)sql110);
        statement.executeUpdate(sql110);
        ProzessBarAO.progressbar.setValue(11000 / gesamtStatements);
        logging.logSQL((Object)sql111);
        statement.executeUpdate(sql111);
        ProzessBarAO.progressbar.setValue(11100 / gesamtStatements);
        logging.logSQL((Object)sql112);
        statement.executeUpdate(sql112);
        ProzessBarAO.progressbar.setValue(11200 / gesamtStatements);
        logging.logSQL((Object)sql113);
        statement.executeUpdate(sql113);
        ProzessBarAO.progressbar.setValue(11300 / gesamtStatements);
        logging.logSQL((Object)sql115);
        statement.executeUpdate(sql115);
        ProzessBarAO.progressbar.setValue(11500 / gesamtStatements);
        logging.logSQL((Object)sql116);
        statement.executeUpdate(sql116);
        ProzessBarAO.progressbar.setValue(11600 / gesamtStatements);
        logging.logSQL((Object)sql117);
        statement.executeUpdate(sql117);
        ProzessBarAO.progressbar.setValue(11700 / gesamtStatements);
        if (mandant.getId() == 1) {
            logging.logSQL((Object)sql118);
            statement.executeUpdate(sql118);
            ProzessBarAO.progressbar.setValue(11800 / gesamtStatements);
        }
        logging.logSQL((Object)sql119);
        statement.executeUpdate(sql119);
        ProzessBarAO.progressbar.setValue(11900 / gesamtStatements);
        logging.logSQL((Object)sql120);
        statement.executeUpdate(sql120);
        ProzessBarAO.progressbar.setValue(12000 / gesamtStatements);
        logging.logSQL((Object)sql121);
        statement.executeUpdate(sql121);
        ProzessBarAO.progressbar.setValue(12100 / gesamtStatements);
        logging.logSQL((Object)sql122);
        statement.executeUpdate(sql122);
        ProzessBarAO.progressbar.setValue(12200 / gesamtStatements);
    }

    public String createDienstgradItems(String bundesland, Mandant mandant) {
        if (bundesland.equals("Nordrhein-Westfalen") | bundesland.equals("Berlin") | bundesland.equals("Bremen")) {
            return "INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`, `mandantID`) VALUES(1, 'FMA', 'Feuerwehrmannanw\u00e4rter', " + mandant.getId() + "),(2, 'FM', 'Feuerwehrmann', " + mandant.getId() + "),(3, 'OFM', 'Oberfeuerwehrmann', " + mandant.getId() + "),(4, 'HFM', 'Hauptfeuerwehrmann', " + mandant.getId() + "),(5, 'UBM', 'Unterbrandmeister', " + mandant.getId() + "),(6, 'BM', 'Brandmeister', " + mandant.getId() + "),(7, 'OBM', 'Oberbrandmeister', " + mandant.getId() + "),(8, 'HBM', 'Hauptbrandmeister', " + mandant.getId() + "),(9, 'HBM', 'Hauptbrandmeister m. Zulage', " + mandant.getId() + "),(10, 'BI', 'Brandinspektor', " + mandant.getId() + "),(11, 'BOI', 'Brandoberinspecktor', " + mandant.getId() + "),(12, 'StBI', 'Stadtbrandinspektor', " + mandant.getId() + "),(13, 'BAR', 'Brandamtsrat', " + mandant.getId() + "),(14, 'BOAR', 'Brandoberamtsrat', " + mandant.getId() + "),(15, 'BR', 'Brandrat', " + mandant.getId() + "),(16, 'OBR', 'Oberbrandrat', " + mandant.getId() + "),(17, 'BD', 'Branddirektor', " + mandant.getId() + "),(18, 'OBD', 'Oberbranddirektor', " + mandant.getId() + "),(19, 'DdBF', 'Direktor der Berufsfeuerwehr', " + mandant.getId() + ");";
        }
        if (bundesland.equals("Hamburg")) {
            return "INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`, `mandantID`) VALUES(1, 'FMA', 'Feuerwehrmannanw\u00e4rter', " + mandant.getId() + "),(2, 'FM', 'Feuerwehrmann', " + mandant.getId() + "),(3, 'OFM', 'Oberfeuerwehrmann', " + mandant.getId() + "),(4, 'HFM', 'Hauptfeuerwehrmann', " + mandant.getId() + "),(5, 'UBM', 'Unterbrandmeister', " + mandant.getId() + "),(6, 'BM', 'Brandmeister', " + mandant.getId() + "),(7, 'OBM', 'Oberbrandmeister', " + mandant.getId() + "),(8, 'HBM', 'Hauptbrandmeister', " + mandant.getId() + "),(9, 'HBM', 'Hauptbrandmeister m. Zulage', " + mandant.getId() + "),(10, 'BI', 'Brandinspektor', " + mandant.getId() + "),(11, 'WF', 'Wehrf\u00fchrer', " + mandant.getId() + "),(12, 'BWF', 'Bereichswehrf\u00fchrer', " + mandant.getId() + "),(13, 'BF', 'Bereichsf\u00fchrer', " + mandant.getId() + "),(14, 'BOAR', 'Brandoberamtsrat', " + mandant.getId() + "),(15, 'BR', 'Brandrat', " + mandant.getId() + "),(16, 'OBR', 'Oberbrandrat', " + mandant.getId() + "),(17, 'BD', 'Branddirektor', " + mandant.getId() + "),(18, 'OBD', 'Oberbranddirektor', " + mandant.getId() + "),(19, 'DdBF', 'Direktor der Berufsfeuerwehr', " + mandant.getId() + ");";
        }
        return "INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`, `mandantID`) VALUES(1, 'FMA', 'Feuerwehrmannanw\u00e4rter', " + mandant.getId() + "),(2, 'FM', 'Feuerwehrmann', " + mandant.getId() + "),(3, 'OFM', 'Oberfeuerwehrmann', " + mandant.getId() + "),(4, 'HFM', 'Hauptfeuerwehrmann', " + mandant.getId() + "),(5, 'EHFM', 'Erster Hauptfeuerwehrmann', " + mandant.getId() + "),(6, 'LM', 'L\u00f6schmeister', " + mandant.getId() + "),(7, 'OLM', 'Oberl\u00f6schmeister', " + mandant.getId() + "),(8, 'HLM', 'Hauptl\u00f6schmeister', " + mandant.getId() + "),(9, 'EHLM', 'Erster Hauptl\u00f6schmeister', " + mandant.getId() + "),(10, 'BM', 'Brandmeister', " + mandant.getId() + "),(11, 'OBM', 'Oberbrandmeister', " + mandant.getId() + "),(12, 'HBM', 'Hauptbrandmeister', " + mandant.getId() + "),(13, 'EHBM', 'Erster Hauptbrandmeister', " + mandant.getId() + "),(14, 'ABM', 'Abschnittsbrandmeister', " + mandant.getId() + "),(15, 'KBM', 'Kreisbrandmeister', " + mandant.getId() + "),(16, 'RBM', 'Regierungsbrandmeister', " + mandant.getId() + "),(17, 'EBM', 'Erster Bezigsbrandmeister', " + mandant.getId() + ");";
    }

    public void deleteDienstgrad() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and id between 1 and 50;";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void testConnection() throws SQLException {
        Statement statement = DatenbankZugriffMySQL.getInstance().getDbConnection().createStatement();
        String sql = "select * from user;";
        logging.logSQL((Object)sql);
        statement.execute(sql);
    }

    public void dropDatabase(String dbName) {
        try {
            Statement statement = DatenbankZugriffMySQL.getInstance().getDbConnection().createStatement();
            String sql = "drop Database " + dbName + ";";
            logging.logSQL((Object)sql);
            statement.executeUpdate(sql);
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    public void changeRootPasswort(String passwort) {
        block4: {
            try {
                Statement statement = DatenbankZugriffMySQL2.getInstance().getDbConnection().createStatement();
                String sql1 = "UPDATE mysql.user SET Password=PASSWORD('" + passwort + "') WHERE User='root';";
                String sql2 = "FLUSH PRIVILEGES;";
                System.out.println(passwort);
                logging.logSQL((Object)"Update RootPasswort --> UPDATE mysql.user SET Password=PASSWORD('*******') WHERE User='root';");
                statement.executeUpdate(sql1);
                logging.logSQL((Object)sql2);
                statement.executeUpdate(sql2);
            }
            catch (SQLException e) {
                if (!e.toString().contains("Access")) break block4;
                logging.logWarning((Object)"Das Datenbankpasswort ist falsch... ich versuche disen Fahler zu behnadeln...");
                try {
                    Statement statement = DatenbankZugriffMySQL.getInstance().getDbConnection().createStatement();
                    String sql1 = "UPDATE mysql.user SET Password=PASSWORD('" + passwort + "') WHERE User='root';";
                    String sql2 = "FLUSH PRIVILEGES;";
                    System.out.println(passwort);
                    logging.logSQL((Object)"Update RootPasswort --> UPDATE mysql.user SET Password=PASSWORD('*******') WHERE User='root';");
                    statement.executeUpdate(sql1);
                    logging.logSQL((Object)sql2);
                    statement.executeUpdate(sql2);
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
            }
        }
    }
}

