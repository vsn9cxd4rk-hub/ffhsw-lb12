-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 03. Feb 2015 um 19:28
-- Server Version: 5.5.16
-- PHP-Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


--
-- Datenbank: `feuerwehrmanagementsystem`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abrechnung`
--

CREATE TABLE IF NOT EXISTS `abrechnung` (
  `id` int(11) NOT NULL,
  `abrechnungID` int(11) NOT NULL,
  `artikelID` int(11) NOT NULL,
  `buchungskonto` int(11) NOT NULL,
  `zahlungsart` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `veranstaltungKategorie` int(11) NOT NULL,
  `wert` int(11) NOT NULL,
  `menge` int(11) NOT NULL,
  `datum` text NOT NULL,
  `status` int(11) NOT NULL,
  `umbuchungID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abrechnung_artikel`
--

CREATE TABLE IF NOT EXISTS `abrechnung_artikel` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `klasse` int(11) NOT NULL,
  `buchungskonto` int(11) NOT NULL,
  `zahlungsart` int(11) NOT NULL,
  `wert` int(11) NOT NULL,
  `rabattwert` int(11) NOT NULL,
  `mwst` int(11) NOT NULL,
  `berechnungsart` int(11) NOT NULL,
  `berechnungsart2` int(11) NOT NULL,
  `rabattart` int(11) NOT NULL,
  `aktiv` int(11) NOT NULL,
  `von` text NOT NULL,
  `bis` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `abrechnung_artikel`
--

INSERT INTO `abrechnung_artikel` (`id`, `name`, `klasse`, `buchungskonto`, `zahlungsart`, `wert`, `rabattwert`, `mwst`, `berechnungsart`, `berechnungsart2`, `rabattart`, `aktiv`, `von`, `bis`) VALUES
(4000, 'Vergütung Brandsicherheitswache', 3, 1, 2, 0, 0, 1, 1, 1, 1, 0, '2015-01-01', '2099-12-31'),
(4001, 'Vergütung Einsatz', 1, 1, 2, 0, 0, 1, 1, 1, 0, 0, '2015-01-01', '2099-12-31'),
(4002, 'Vergütung Dienstabend', 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31'),
(4003, 'Vergütung Sonstige', 4, 1, 2, 0, 0, 1, 2, 0, 1, 0, '2015-01-01', '2099-12-31');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abrechnung_artikelklassen`
--

CREATE TABLE IF NOT EXISTS `abrechnung_artikelklassen` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `abrechnung_artikelklassen`
--

INSERT INTO `abrechnung_artikelklassen` (`id`, `name`) VALUES
(1, 'Einsatz'),
(2, 'Dienstabend'),
(3, 'BSW'),
(4, 'Sonstige'),
(100, 'SYSTEM'),
(101, 'RABATT');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abrechnung_konto`
--

CREATE TABLE IF NOT EXISTS `abrechnung_konto` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `abrechnung_konto`
--

INSERT INTO `abrechnung_konto` (`id`, `name`) VALUES
(1, 'SYSTEM');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abwesenheit`
--

CREATE TABLE IF NOT EXISTS `abwesenheit` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `veranstaltungKategorie` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL,
  `grund` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `abwesenheitsgrund`
--

CREATE TABLE IF NOT EXISTS `abwesenheitsgrund` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `kurzName` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `abwesenheitsgrund`
--

INSERT INTO `abwesenheitsgrund` (`id`, `name`, `kurzName`) VALUES
(0, 'Undefiniert', 'UNDEF.'),
(1, 'Unentschuldigt', 'UE'),
(2, 'Entschuldigt', 'E'),
(3, 'Urlaub', 'U'),
(4, 'Krank', 'K'),
(5, 'Brandsicherheitswache', 'BSW');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `anwesenheit`
--

CREATE TABLE IF NOT EXISTS `anwesenheit` (
  `id` int(100) NOT NULL,
  `jahr` int(4) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `veranstaltungKategorie` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `atemschutzpass`
--

CREATE TABLE IF NOT EXISTS `atemschutzpass` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `veranstaltungKategorie` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL,
  `zeit` int(11) NOT NULL,
  `einsatzart` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `atemschutzpass_einsatzart`
--

CREATE TABLE IF NOT EXISTS `atemschutzpass_einsatzart` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `atemschutzpass_einsatzart`
--

INSERT INTO `atemschutzpass_einsatzart` (`id`, `name`) VALUES
(1, 'PA'),
(2, 'Filter'),
(3, 'CSA');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ausbildung`
--

CREATE TABLE IF NOT EXISTS `ausbildung` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `ausbildungKategorie` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ausbildung_kategorie`
--

CREATE TABLE IF NOT EXISTS `ausbildung_kategorie` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `ausbildung_kategorie`
--

INSERT INTO `ausbildung_kategorie` (`id`, `name`) VALUES
(1, 'FwDV3'),
(2, 'FwDV7'),
(3, 'FwDV10');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ausbildung_plan`
--

CREATE TABLE IF NOT EXISTS `ausbildung_plan` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `ausbildungKategorie` int(11) NOT NULL,
  `details` text NOT NULL,
  `ausbilder1` int(11) NOT NULL,
  `ausbilder2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `berechtigunggruppe`
--

CREATE TABLE IF NOT EXISTS `berechtigunggruppe` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `BR0` int(11) NOT NULL,
  `BR1` int(11) NOT NULL,
  `BR2` int(11) NOT NULL,
  `BR3` int(11) NOT NULL,
  `BR4` int(11) NOT NULL,
  `BR5` int(11) NOT NULL,
  `BR6` int(11) NOT NULL,
  `BR7` int(11) NOT NULL,
  `BR8` int(11) NOT NULL,
  `BR9` int(11) NOT NULL,
  `BR10` int(11) NOT NULL,
  `BR11` int(11) NOT NULL,
  `BR12` int(11) NOT NULL,
  `BR13` int(11) NOT NULL,
  `BR14` int(11) NOT NULL,
  `BR15` int(11) NOT NULL,
  `BR16` int(11) NOT NULL,
  `BR17` int(11) NOT NULL,
  `BR18` int(11) NOT NULL,
  `BR19` int(11) NOT NULL,
  `BR20` int(11) NOT NULL,
  `BR21` int(11) NOT NULL,
  `BR22` int(11) NOT NULL,
  `BR23` int(11) NOT NULL,
  `BR24` int(11) NOT NULL,
  `BR25` int(11) NOT NULL,
  `BR26` int(11) NOT NULL,
  `BR27` int(11) NOT NULL,
  `BR28` int(11) NOT NULL,
  `BR29` int(11) NOT NULL,
  `BR30` int(11) NOT NULL,
  `BR31` int(11) NOT NULL,
  `BR32` int(11) NOT NULL,
  `BR33` int(11) NOT NULL,
  `BR34` int(11) NOT NULL,
  `BR35` int(11) NOT NULL,
  `BR36` int(11) NOT NULL,
  `BR37` int(11) NOT NULL,
  `BR38` int(11) NOT NULL,
  `BR39` int(11) NOT NULL,
  `BR40` int(11) NOT NULL,
  `BR41` int(11) NOT NULL,
  `BR42` int(11) NOT NULL,
  `BR43` int(11) NOT NULL,
  `BR44` int(11) NOT NULL,
  `BR45` int(11) NOT NULL,
  `BR46` int(11) NOT NULL,
  `BR47` int(11) NOT NULL,
  `BR48` int(11) NOT NULL,
  `BR49` int(11) NOT NULL,
  `BR50` int(11) NOT NULL,
  `BR51` int(11) NOT NULL,
  `BR52` int(11) NOT NULL,
  `BR53` int(11) NOT NULL,
  `BR54` int(11) NOT NULL,
  `BR55` int(11) NOT NULL,
  `BR56` int(11) NOT NULL,
  `BR57` int(11) NOT NULL,
  `BR58` int(11) NOT NULL,
  `BR59` int(11) NOT NULL,
  `BR60` int(11) NOT NULL,
  `BR61` int(11) NOT NULL,
  `BR62` int(11) NOT NULL,
  `BR63` int(11) NOT NULL,
  `BR64` int(11) NOT NULL,
  `BR65` int(11) NOT NULL,
  `BR66` int(11) NOT NULL,
  `BR67` int(11) NOT NULL,
  `BR68` int(11) NOT NULL,
  `BR69` int(11) NOT NULL,
  `BR70` int(11) NOT NULL,
  `BR71` int(11) NOT NULL,
  `BR72` int(11) NOT NULL,
  `BR73` int(11) NOT NULL,
  `BR74` int(11) NOT NULL,
  `BR75` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `berechtigunggruppe`
--

INSERT INTO `berechtigunggruppe` (`id`, `name`, `BR0`, `BR1`, `BR2`, `BR3`, `BR4`, `BR5`, `BR6`, `BR7`, `BR8`, `BR9`, `BR10`, `BR11`, `BR12`, `BR13`, `BR14`, `BR15`, `BR16`, `BR17`, `BR18`, `BR19`, `BR20`, `BR21`, `BR22`, `BR23`, `BR24`, `BR25`, `BR26`, `BR27`, `BR28`, `BR29`, `BR30`, `BR31`, `BR32`, `BR33`, `BR34`, `BR35`, `BR36`, `BR37`, `BR38`, `BR39`, `BR40`, `BR41`, `BR42`, `BR43`, `BR44`, `BR45`, `BR46`, `BR47`, `BR48`, `BR49`, `BR50`, `BR51`, `BR52`, `BR53`, `BR54`, `BR55`, `BR56`, `BR57`, `BR58`, `BR59`, `BR60`, `BR61`, `BR62`, `BR63`, `BR64`, `BR65`, `BR66`, `BR67`, `BR68`, `BR69`, `BR70`, `BR71`, `BR72`, `BR73`, `BR74`, `BR75`) VALUES
(0, 'Public', 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1, 'Administrator', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `brandsicherheitswachen`
--

CREATE TABLE IF NOT EXISTS `brandsicherheitswachen` (
  `id` int(11) NOT NULL,
  `bswNummer` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `ort` text NOT NULL,
  `art` text NOT NULL,
  `datum` date NOT NULL,
  `zeit_treffen` text NOT NULL,
  `zeit_start` text NOT NULL,
  `zeit_ende` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `brandsicherheitswachen_temp`
--

CREATE TABLE IF NOT EXISTS `brandsicherheitswachen_temp` (
  `mitgliederID` int(11) NOT NULL,
  `beteiligung` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `briefe`
--

CREATE TABLE IF NOT EXISTS `briefe` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `title` text NOT NULL,
  `bericht` text NOT NULL,
  `erstelldatum` date NOT NULL,
  `dateiname` text NOT NULL,
  `empfaenger` text NOT NULL,
  `template` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dienstgrad`
--

CREATE TABLE IF NOT EXISTS `dienstgrad` (
  `id` int(2) NOT NULL,
  `beschreibung` text NOT NULL,
  `beschreibungLang` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `dienstgrad`
--

INSERT INTO `dienstgrad` (`id`, `beschreibung`, `beschreibungLang`) VALUES
(0, '---', '<Kein Dienstgrad>'),
(1, 'FMA', 'Feuerwehrmannanwärter'),
(2, 'FM', 'Feuerwehrmann'),
(3, 'OFM', 'Oberfeuerwehrmann'),
(4, 'HFM', 'Hauptfeuerwehrmann'),
(5, 'UBM', 'Unterbrandmeister'),
(6, 'BM', 'Brandmeister'),
(7, 'OBM', 'Oberbrandmeister'),
(8, 'HBM', 'Hauptprandmeister'),
(9, 'HBM', 'Hauptprandmeister m. Zulage'),
(10, 'BI', 'Brandinspektor'),
(11, 'BOI', 'Brandoberinspecktor'),
(12, 'StBI', 'Stadtbrandinspektor'),
(13, 'BAR', 'Brandamtsrat'),
(14, 'BOAR', 'Brandoberamtsrat'),
(15, 'BR', 'Brandrat'),
(16, 'OBR', 'Oberbrandrat'),
(17, 'BD', 'Branddirektor'),
(18, 'OBD', 'Oberbranddirektor'),
(19, 'DdBF', 'Direktor der Berufsfeuerwehr');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz`
--

CREATE TABLE IF NOT EXISTS `einsatz` (
  `id` int(100) NOT NULL,
  `einsatzNummer` int(11) NOT NULL,
  `einsatznummerOffiziell` text NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `Datum` date NOT NULL,
  `ZeitAlarm` varchar(10) NOT NULL,
  `ZeitAusgerueckt` varchar(10) NOT NULL,
  `zeitEingetroffen` varchar(10) NOT NULL,
  `zeitEingerueckt` varchar(10) NOT NULL,
  `Ort` text NOT NULL,
  `stadtteil` text NOT NULL,
  `Stichwort` int(11) NOT NULL,
  `Fahrzeug` text NOT NULL,
  `beschreibung` text NOT NULL,
  `staerkeGF` int(3) NOT NULL,
  `staerkeFM` int(3) NOT NULL,
  `einsatzleiter` int(11) NOT NULL,
  `staerkeZF` int(3) NOT NULL,
  `einsatzleiterBF` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz_berichte`
--

CREATE TABLE IF NOT EXISTS `einsatz_berichte` (
  `id` int(11) NOT NULL,
  `einsatzNummer` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `dateiname` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz_bericht_daten`
--

CREATE TABLE IF NOT EXISTS `einsatz_bericht_daten` (
  `id` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `einsatzID` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `einsatzArt` int(11) NOT NULL,
  `stelle` int(11) NOT NULL,
  `objekt` int(11) NOT NULL,
  `eigentuemerName` text NOT NULL,
  `eigentuemerAnschrift` text NOT NULL,
  `eigentuemerTelefon` text NOT NULL,
  `verursacherName` text NOT NULL,
  `verursacherAnschrift` text NOT NULL,
  `verursacherTelefon` text NOT NULL,
  `alamierung` int(11) NOT NULL,
  `meldenderName` text NOT NULL,
  `meldenderAnschrift` text NOT NULL,
  `meldenderTelefon` text NOT NULL,
  `lage` text NOT NULL,
  `verlauf` text NOT NULL,
  `eingesetzteGeraete` text NOT NULL,
  `verbrauchWasser` text NOT NULL,
  `verbrauchSchaum` text NOT NULL,
  `verbrauchPulver` text NOT NULL,
  `verbrauchBindemittel` text NOT NULL,
  `vorEintreffenGeloescht` int(11) NOT NULL,
  `schnellangriff` int(11) NOT NULL,
  `crohr` text NOT NULL,
  `brohr` text NOT NULL,
  `kleinloeschgeraet` text NOT NULL,
  `tragbareLeitern` int(11) NOT NULL,
  `atemschutzgeraet` text NOT NULL,
  `fluchthauben` text NOT NULL,
  `belueftungsgeraet` text NOT NULL,
  `rettungsgeraet` int(11) NOT NULL,
  `ausdehnung` int(11) NOT NULL,
  `entstehungsursache` text NOT NULL,
  `verletzte` text NOT NULL,
  `gerettete` text NOT NULL,
  `tote` text NOT NULL,
  `schadenhoehe` text NOT NULL,
  `brandwacheFahrzeug` int(11) NOT NULL,
  `staerke` text NOT NULL,
  `dauer` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz_bericht_elemente`
--

CREATE TABLE IF NOT EXISTS `einsatz_bericht_elemente` (
  `id` int(11) NOT NULL,
  `gruppe` text NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `einsatz_bericht_elemente`
--

INSERT INTO `einsatz_bericht_elemente` (`id`, `gruppe`, `name`) VALUES
(1, 'EinsatzArt', 'Kleinbrand'),
(2, 'EinsatzArt', 'Mittelbrand'),
(3, 'EinsatzArt', 'Großbrand'),
(4, 'EinsatzArt', 'Kaminbrand'),
(5, 'EinsatzArt', 'Gasauströmung'),
(6, 'EinsatzArt', 'Blinder Alarm'),
(7, 'EinsatzArt', 'Böswilliger Alarm'),
(8, 'EinsatzArt', 'Verkehrsunfall'),
(9, 'EinsatzArt', 'Verkehrsstörung'),
(10, 'EinsatzArt', 'Einsturz'),
(11, 'EinsatzArt', 'Mensch in Notlage'),
(12, 'EinsatzArt', 'Gefahrgut (GSG)'),
(13, 'EinsatzArt', 'Ölspur'),
(14, 'EinsatzArt', 'Tier in Notlage'),
(15, 'EinsatzArt', 'Tiertranssport'),
(16, 'EinsatzArt', 'Tierkadaver'),
(17, 'EinsatzArt', 'Betriebsunfall'),
(18, 'EinsatzArt', 'TH Baum'),
(19, 'EinsatzArt', 'TH Wasser'),
(20, 'EinsatzArt', 'keine Angaben'),
(101, 'Stelle', 'Keller'),
(102, 'Stelle', 'Erdgeschloß'),
(103, 'Stelle', 'Obergeschoß'),
(104, 'Stelle', 'Dachgeschoß'),
(105, 'Stelle', 'eingeschloss. Gebäude'),
(106, 'Stelle', 'Baustelle'),
(107, 'Stelle', 'Freigelände'),
(108, 'Stelle', 'Auf dem Wasser'),
(109, 'Stelle', 'Straße'),
(110, 'Stelle', 'Autobahn'),
(111, 'Stelle', 'Landstraße'),
(112, 'Stelle', 'Kraftfahrstraße'),
(113, 'Stelle', 'keine Angaben'),
(201, 'Objekt', 'Wohngebäude'),
(202, 'Objekt', 'Verwaltungsgebäude'),
(203, 'Objekt', 'Landwirdschaftl. Gebäude'),
(204, 'Objekt', 'Industriebetrieb'),
(205, 'Objekt', 'gewerbl. Betrieb'),
(206, 'Objekt', 'Fahrzeug'),
(207, 'Objekt', 'Wald'),
(208, 'Objekt', 'Feld'),
(209, 'Objekt', 'Grasnarbe'),
(210, 'Objekt', 'keine Angaben'),
(301, 'Alamierung', 'Digitaler Meldeempfänger (DME)'),
(302, 'Alamierung', 'Leitstelle Feuerwehr'),
(303, 'Alamierung', 'Leitstelle Polizei'),
(304, 'Alamierung', 'Telefon'),
(305, 'Alamierung', 'Sirene'),
(301, 'Ausdehnung', 'Auf Entstehungrum begrenzt'),
(302, 'Ausdehnung', 'vor Eintreffen (auf andere Räume übgergeriffen)'),
(303, 'Ausdehnung', 'vor Eintreffen (auf andere Gebäude übgergeriffen)'),
(304, 'Ausdehnung', 'während der Brandbekämpfung (auf andere Räume übgergeriffen)'),
(305, 'Ausdehnung', 'während der Brandbekämpfung (auf andere Gebäude übgergeriffen)'),
(306, 'Ausdehnung', 'keine Angaben');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz_kategorie`
--

CREATE TABLE IF NOT EXISTS `einsatz_kategorie` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `einsatz_kategorie`
--

INSERT INTO `einsatz_kategorie` (`id`, `name`) VALUES
(1, 'Brandeinsatz'),
(2, 'Technische Hilfeleistung'),
(3, 'Wachbesetzungen'),
(4, 'Sonstige'),
(5, 'Rettungsdienst'),
(6, 'First Responder');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einsatz_zeiten`
--

CREATE TABLE IF NOT EXISTS `einsatz_zeiten` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `einsatzID` int(11) NOT NULL,
  `fahrzeugID` int(11) NOT NULL,
  `zeitAlarm` text NOT NULL,
  `zeitAusgerueckt` text NOT NULL,
  `zeitEingetroffen` text NOT NULL,
  `zeitEingerueckt` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `einstellungen`
--

CREATE TABLE IF NOT EXISTS `einstellungen` (
  `key` text NOT NULL,
  `wert` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `einstellungen`
--

INSERT INTO `einstellungen` (`key`, `wert`) VALUES
('EinsatzBericht', 'data/templates/Einsatzbericht.xml'),
('Name', ''),
('Stadt', ''),
('plz', ''),
('telefon', ''),
('strasse', ''),
('verdienstausfall', 'data/templates/Verdienstausfallbescheinigung.xml'),
('fehlalarm', '30'),
('briefkopf', 'images/briefkopf.jpg'),
('untersuchungVorwarnung', '2'),
('gebAnzeigen', '1'),
('autoBerichtAktiv', '1'),
('ZeitAutoBericht', '60'),
('letzterAutoBericht', '1422984129148'),
('untersuchungVorwarnungFahrzeug', '2'),
('mängelmeldung', 'data/templates/Mängelmeldung.xml'),
('vorwarnungGeräte', '1'),
('termineAnzeigen', '1'),
('agtTrainingAnzeigen', '1'),
('WieVieleLehrgangsmeldungenProJahr', '2'),
('version', 'Version: 1.24'),
('smtpPort', '587'),
('smtpServer', ''),
('pop3Server', ''),
('pop3Port', '995'),
('emailAdresse', ''),
('emailPasswort', '00000MXcgtglXaVXGRsrVipSRNaqIO3hPGT80vD'),
('useSSL', '1'),
('emailModul', '0'),
('emailSignatur', ''),
('vorbelegungDienstStart', '19:30'),
('vorbelegungDienstEnde', '22:00'),
('einsatzSchnittstelle', '0'),
('autoDBsave', '1'),
('autoDBsaveTage', '30'),
('letzterDBsave', '1422984129144'),
('untersuchungViaEMail', '0'),
('untersuchungViaEMailChefBCC', '0'),
('terminVersandtViaEMail', '0'),
('vCardSeperator', ';'),
('ablaufLKWFührerscheinViaEMail', '0'),
('ablaufLKWAnzeigen', '0'),
('automatischesUpdate', '1'),
('einsatzleiterBF', '0'),
('bundesland', 'Nordrhein-Westfalen'),
('abrechnungModul', '1'),
('geraetepruefungViaEMail', '1'),
('offeneMaengelAnzeigen', '1'),
('fahrzeugUntersuchungViaEMail', '0'),
('mängelmeldungViaEMailVersenden', '0'),
('gebAnzeigeModus', '1'),
('vorbelegungBSWTreffen', '12:15'),
('vorbelegungBSWVeranstaltungStart', '15:30'),
('vorbelegungBSWEnde', '18:15'),
('EinsatzBerichtArt', 'PDF (intern)');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_ausgang`
--

CREATE TABLE IF NOT EXISTS `email_ausgang` (
  `id` int(11) NOT NULL,
  `an` text NOT NULL,
  `cc` text NOT NULL,
  `bcc` text NOT NULL,
  `betreff` text NOT NULL,
  `nachricht` text NOT NULL,
  `anhang` text NOT NULL,
  `date` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_empfangende`
--

CREATE TABLE IF NOT EXISTS `email_empfangende` (
  `id` int(11) NOT NULL,
  `sender` text NOT NULL,
  `betreff` text NOT NULL,
  `nachricht` text NOT NULL,
  `date` text NOT NULL,
  `size` int(11) NOT NULL,
  `anhang` int(11) NOT NULL,
  `gelesen` int(11) NOT NULL,
  `art` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_entwurf`
--

CREATE TABLE IF NOT EXISTS `email_entwurf` (
  `id` int(11) NOT NULL,
  `an` text NOT NULL,
  `cc` text NOT NULL,
  `bcc` text NOT NULL,
  `betreff` text NOT NULL,
  `nachricht` text NOT NULL,
  `anhang` int(11) NOT NULL,
  `date` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_gesendet`
--

CREATE TABLE IF NOT EXISTS `email_gesendet` (
  `id` int(11) NOT NULL,
  `an` text NOT NULL,
  `cc` text NOT NULL,
  `bcc` text NOT NULL,
  `betreff` text NOT NULL,
  `nachricht` text NOT NULL,
  `anhang` int(11) NOT NULL,
  `date` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fahrzeuge`
--

CREATE TABLE IF NOT EXISTS `fahrzeuge` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `beschreibung` int(11) NOT NULL,
  `kennzeichen` text NOT NULL,
  `funkrufname` text NOT NULL,
  `sitzplaetze` int(11) NOT NULL,
  `minBesatzung` int(11) NOT NULL,
  `maxBesatzung` int(11) NOT NULL,
  `fuehrerschein` text NOT NULL,
  `ausserDienst` int(11) NOT NULL,
  `anhaenger` int(11) NOT NULL,
  `sortierung` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fahrzeugeinteilung`
--

CREATE TABLE IF NOT EXISTS `fahrzeugeinteilung` (
  `id` int(100) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `mitgliederID` int(11) NOT NULL,
  `fahrzeugID` int(11) NOT NULL,
  `position` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fahrzeugeinteilung_temp`
--

CREATE TABLE IF NOT EXISTS `fahrzeugeinteilung_temp` (
  `mitgliederID` int(11) NOT NULL,
  `dienstgradID` int(11) NOT NULL,
  `klasseC` int(11) NOT NULL,
  `klasseB` int(11) NOT NULL,
  `Maschi` int(11) NOT NULL,
  `dlkmaschi` int(11) NOT NULL,
  `korbsteuerung` int(11) NOT NULL,
  `chef` int(11) NOT NULL,
  `tm1` int(11) NOT NULL,
  `AGT` int(11) NOT NULL,
  `TF` int(11) NOT NULL,
  `GF` int(11) NOT NULL,
  `ZF` int(11) NOT NULL,
  `rh` int(11) NOT NULL,
  `rs` int(11) NOT NULL,
  `ra` int(11) NOT NULL,
  `beteiligung` int(20) NOT NULL,
  `position` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fahrzeug_beschreibung`
--

CREATE TABLE IF NOT EXISTS `fahrzeug_beschreibung` (
  `id` int(11) NOT NULL,
  `beschreibung` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `fahrzeug_beschreibung`
--

INSERT INTO `fahrzeug_beschreibung` (`id`, `beschreibung`) VALUES
(1, 'Hilfeleistungslöschfahrzeug'),
(2, 'Löschgruppenfahrzeug'),
(3, 'Tanklöschfahrzeug'),
(4, 'Drehleiter'),
(5, 'Telekopmast'),
(6, 'Mannschaftstransportfahrzeug'),
(7, 'GW Logistik'),
(8, 'Schlauchwagen'),
(9, 'LKW'),
(10, 'Rüstwagen'),
(11, 'Feldküche'),
(12, 'Einsatzleitwagen'),
(13, 'Rettungswagen'),
(14, 'Krankentransportwagen');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fahrzeug_untersuchung`
--

CREATE TABLE IF NOT EXISTS `fahrzeug_untersuchung` (
  `id` int(11) NOT NULL,
  `tuev` text NOT NULL,
  `sp` text NOT NULL,
  `service` text NOT NULL,
  `infoTuev` int(11) NOT NULL,
  `infoSP` int(11) NOT NULL,
  `infoService` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ftpsync`
--

CREATE TABLE IF NOT EXISTS `ftpsync` (
  `id` int(11) NOT NULL,
  `clientID` text NOT NULL,
  `datei` text NOT NULL,
  `ordner` text NOT NULL,
  `status` int(11) NOT NULL,
  `groesse` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `ftpsync`
--

INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`, `groesse`) VALUES
(1, 'SYSTEM', '', 'data', 0, 0),
(2, 'SYSTEM', '', 'data/Templates', 0, 0),
(3, 'SYSTEM', '', 'data/Papierkorb', 0, 0),
(4, 'SYSTEM', '', 'data/Eigene Dateien', 0, 0),
(5, 'SYSTEM', '', 'data/Mitgliederakte', 0, 0),
(6, 'SYSTEM', '', 'data/Fahrzeugakte', 0, 0),
(7, 'SYSTEM', '', 'data/EMail', 0, 0),
(8, 'SYSTEM', '', 'data/EMail/Anhang', 0, 0),
(9, 'SYSTEM', '', 'data/EMail/Anhang/Gesendet', 0, 0),
(10, 'SYSTEM', '', 'data/EMail/Anhang/Entwurf', 0, 0),
(11, 'SYSTEM', '', 'data/EMail/Anhang/Empfangende', 0, 0),
(12, 'SYSTEM', '', 'data/EMail/Temp', 0, 0),
(13, 'SYSTEM', '', 'data/EMail/Temp/original_nachricht', 0, 0),
(14, 'SYSTEM', '', 'data/DBBACKUP', 0, 0),
(15, 'SYSTEM', '', 'data/Bestandsliste', 0, 0),
(16, 'SYSTEM', '', 'data/Abrechnung', 0, 0),
(17, 'SYSTEM', '', 'data/Ausbildungsunterlagen', 0, 0),
(18, 'SYSTEM', 'data/Templates/Einsatzbericht.docx', '', 0, 0),
(19, 'SYSTEM', 'data/Templates/Einsatzbericht.xml', '', 0, 0),
(20, 'SYSTEM', 'data/Templates/Mängelmeldung.docx', '', 0, 0),
(21, 'SYSTEM', 'data/Templates/Mängelmeldung.xml', '', 0, 0),
(22, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0, 0),
(23, 'SYSTEM', 'data/Templates/Verdienstausfallbescheinigung.docx', '', 0, 0),
(24, 'SYSTEM', '', 'data/KarteBilder', 0, 0),
(25, 'SYSTEM', '', 'data/KarteBilder/groß', 0, 0),
(26, 'SYSTEM', '', 'data/KarteBilder/klein', 0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ftpsync_del`
--

CREATE TABLE IF NOT EXISTS `ftpsync_del` (
  `id` int(11) NOT NULL,
  `clientID` text NOT NULL,
  `datei` text NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `geraetepruefung`
--

CREATE TABLE IF NOT EXISTS `geraetepruefung` (
  `id` int(11) NOT NULL,
  `stromerzeuger` text NOT NULL,
  `steckleiter` text NOT NULL,
  `schiebleiter` text NOT NULL,
  `hydraulik` text NOT NULL,
  `pumpe` text NOT NULL,
  `kettensaege` text NOT NULL,
  `doppelkanister` text NOT NULL,
  `geraetepruefung_allgem` text NOT NULL,
  `abstusiset` text NOT NULL,
  `infoEMail` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `jahr`
--

CREATE TABLE IF NOT EXISTS `jahr` (
  `jahr` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `jahr`
--

INSERT INTO `jahr` (`jahr`) VALUES
(2015);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `jahresberichte`
--

CREATE TABLE IF NOT EXISTS `jahresberichte` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `title` text NOT NULL,
  `bericht` text NOT NULL,
  `erstelldatum` date NOT NULL,
  `dateiname` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `karte_hydranten`
--

CREATE TABLE IF NOT EXISTS `karte_hydranten` (
  `id` int(20) NOT NULL,
  `starssenid` int(11) NOT NULL,
  `hausnummer` text NOT NULL,
  `nennweite` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `karte_strassen`
--

CREATE TABLE IF NOT EXISTS `karte_strassen` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `bild` text NOT NULL,
  `bild2` text NOT NULL,
  `anfahrt` text NOT NULL,
  `info` text NOT NULL,
  `koordinaten` text NOT NULL,
  `PLZ` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `keystore`
--

CREATE TABLE IF NOT EXISTS `keystore` (
  `key` text NOT NULL,
  `wert` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `keystore`
--

INSERT INTO `keystore` (`key`, `wert`) VALUES
('Nummer1', '00001fpswvq9O9o32rM7RhIRK4dko2FjpTuIzLjE'),
('Nummer2', '00026fMUmf2aA7f4bpx675217-TTA0E-032990-771094HULzIkqtJxK6NgXr8lAJ');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lager`
--

CREATE TABLE IF NOT EXISTS `lager` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `verantwortlicher` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `lager`
--

INSERT INTO `lager` (`id`, `name`, `verantwortlicher`) VALUES
(9000, 'Virtuelles- / Defektteile- / Ausmusterlager', 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lager_artikel`
--

CREATE TABLE IF NOT EXISTS `lager_artikel` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `beschreibung` text NOT NULL,
  `bild` text NOT NULL,
  `wert` int(11) NOT NULL,
  `EAN` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lager_zugewiesen`
--

CREATE TABLE IF NOT EXISTS `lager_zugewiesen` (
  `id` int(100) NOT NULL,
  `artikelID` int(11) NOT NULL,
  `anzahl` int(11) NOT NULL,
  `gruppe` text NOT NULL,
  `mitgliedID` int(11) NOT NULL,
  `ort` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lehrgang`
--

CREATE TABLE IF NOT EXISTS `lehrgang` (
  `mitgliedID` int(11) NOT NULL,
  `lehrgangID` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lehrgangsmeldung`
--

CREATE TABLE IF NOT EXISTS `lehrgangsmeldung` (
  `mitgliedID` int(11) NOT NULL,
  `lehrgang` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lehrgang_kategorie`
--

CREATE TABLE IF NOT EXISTS `lehrgang_kategorie` (
  `id` int(11) NOT NULL,
  `art` text NOT NULL,
  `name` text NOT NULL,
  `relevant` int(11) NOT NULL,
  `reihenfolge` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `lehrgang_kategorie`
--

INSERT INTO `lehrgang_kategorie` (`id`, `art`, `name`, `relevant`, `reihenfolge`) VALUES
(1, 'L', 'Führerschein Klasse C', 1, 11),
(2, 'L', 'Führerschein Klasse B', 0, 0),
(3, 'L', 'Führerschein Klasse CE', 0, 0),
(4, 'L', 'Erste Hilfe', 1, 1),
(5, 'L', 'Erste Hilfe Fortbildung', 0, 0),
(6, 'L', 'TM 1', 1, 2),
(7, 'L', 'Sprechfunker', 1, 3),
(8, 'L', 'Atemschutz', 1, 4),
(9, 'L', 'WGA1', 0, 0),
(10, 'L', 'Absturzsicherung', 1, 5),
(11, 'L', 'TH 1', 1, 6),
(12, 'L', 'Maschinist', 1, 7),
(13, 'L', 'Maschinist Fortbildung', 0, 0),
(14, 'L', 'Kettensäge', 0, 0),
(15, 'L', 'Truppführer', 1, 8),
(16, 'L', 'WGA 2', 0, 0),
(17, 'L', 'FwDV 500 (GSG)', 1, 9),
(18, 'L', 'FwDV 500 (Strahlenschutz)', 1, 10),
(19, 'L', 'F3', 1, 12),
(20, 'L', 'F4', 0, 0),
(21, 'L', 'DLK Korbsteuerung', 0, 0),
(22, 'L', 'DLK Maschinist', 0, 0),
(23, 'L', 'Rettungshelfer', 0, 0),
(24, 'L', 'Rettungssanitäter', 0, 0),
(25, 'L', 'Rettungsassistent', 0, 0),
(26, 'F', 'Einheitsführer', 0, 0),
(27, 'F', 'Gerätewart', 0, 0),
(28, 'F', 'Getränkewart', 0, 0),
(29, 'F', 'Materialwart', 0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `logbuch`
--

CREATE TABLE IF NOT EXISTS `logbuch` (
  `id` int(11) NOT NULL,
  `datum` text NOT NULL,
  `zeit` text NOT NULL,
  `user` text NOT NULL,
  `aktion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `logbuch`
--

INSERT INTO `logbuch` (`id`, `datum`, `zeit`, `user`, `aktion`) VALUES
(1, '2015-02-03', '18:22:16', 'öffentlich', 'Programm wird beendet...');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `maengelmeldung`
--

CREATE TABLE IF NOT EXISTS `maengelmeldung` (
  `id` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `mitgliedID` int(11) NOT NULL,
  `fahrzeugID` int(11) NOT NULL,
  `datum` text NOT NULL,
  `wann` text NOT NULL,
  `beschreibung` text NOT NULL,
  `dateiname` text NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `maengelmeldung_kommentar`
--

CREATE TABLE IF NOT EXISTS `maengelmeldung_kommentar` (
  `mangelID` int(11) NOT NULL,
  `kommentarID` int(11) NOT NULL,
  `datum` text NOT NULL,
  `zeit` text NOT NULL,
  `kommentar` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder`
--

CREATE TABLE IF NOT EXISTS `mitglieder` (
  `id` int(11) NOT NULL,
  `mitgliederGruppe` int(11) NOT NULL,
  `anrede` int(11) NOT NULL,
  `name` text NOT NULL,
  `vorname` text NOT NULL,
  `strasse` text NOT NULL,
  `ort` text NOT NULL,
  `telefonPrivat` text NOT NULL,
  `telefonMobil` text NOT NULL,
  `telefonArbeit` text NOT NULL,
  `email` text NOT NULL,
  `email2` text NOT NULL,
  `dienstgrad` int(11) NOT NULL,
  `ausserDienst` int(11) NOT NULL,
  `mitgliedSeit` int(4) NOT NULL,
  `gebDatum` text NOT NULL,
  `kommentar` text NOT NULL,
  `loeschkenner` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitgliederakte_kommentar`
--

CREATE TABLE IF NOT EXISTS `mitgliederakte_kommentar` (
  `id` int(11) NOT NULL,
  `datum` text NOT NULL,
  `zeit` text NOT NULL,
  `kommentar` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_angehoerige`
--

CREATE TABLE IF NOT EXISTS `mitglieder_angehoerige` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `strasse` text NOT NULL,
  `ort` text NOT NULL,
  `telefonPrivat` text NOT NULL,
  `telefonMobil` text NOT NULL,
  `email` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_anrede`
--

CREATE TABLE IF NOT EXISTS `mitglieder_anrede` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `anredeBrief` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `mitglieder_anrede`
--

INSERT INTO `mitglieder_anrede` (`id`, `name`, `anredeBrief`) VALUES
(1, 'Herr', 'Sehr geehrter'),
(2, 'Frau', 'Sehr geehrte');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_arbeit`
--

CREATE TABLE IF NOT EXISTS `mitglieder_arbeit` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `strasse` text NOT NULL,
  `ort` text NOT NULL,
  `telefon` text NOT NULL,
  `ansprechpartner` text NOT NULL,
  `email` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_bankverbindung`
--

CREATE TABLE IF NOT EXISTS `mitglieder_bankverbindung` (
  `id` int(11) NOT NULL,
  `iban` text NOT NULL,
  `bic` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_gruppe`
--

CREATE TABLE IF NOT EXISTS `mitglieder_gruppe` (
  `id` int(11) NOT NULL,
  `personalnummer` int(11) NOT NULL,
  `nextPersonalnummer` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `mitglieder_gruppe`
--

INSERT INTO `mitglieder_gruppe` (`id`, `personalnummer`, `nextPersonalnummer`, `name`) VALUES
(1, 11000, 11000, 'Einsatzabteilung');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `mitglieder_untersuchung`
--

CREATE TABLE IF NOT EXISTS `mitglieder_untersuchung` (
  `id` int(11) NOT NULL,
  `g25` text NOT NULL,
  `g26` text NOT NULL,
  `agttraining` text NOT NULL,
  `infoG25` int(11) NOT NULL,
  `infoG26` int(11) NOT NULL,
  `ablaufLKW` text NOT NULL,
  `infoAblaufLKW` int(11) NOT NULL,
  `g30` text NOT NULL,
  `infoG30` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `statistikbsw`
--

CREATE TABLE IF NOT EXISTS `statistikbsw` (
  `id` int(100) NOT NULL,
  `veranstaltungID` int(100) NOT NULL,
  `bswID` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `dauer` int(11) NOT NULL,
  `mannstunden` int(11) NOT NULL,
  `wochentag` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `statistikeinsatz`
--

CREATE TABLE IF NOT EXISTS `statistikeinsatz` (
  `id` int(100) NOT NULL,
  `veranstaltungID` int(100) NOT NULL,
  `einsatzID` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `stichwort` int(11) NOT NULL,
  `kategorie` int(11) NOT NULL,
  `ausrueckezeit` int(11) NOT NULL,
  `dauer` int(11) NOT NULL,
  `dauerAlarmfahrt` int(11) NOT NULL,
  `mannstunden` int(11) NOT NULL,
  `wochentag` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `statistiksonstigeveranstaltung`
--

CREATE TABLE IF NOT EXISTS `statistiksonstigeveranstaltung` (
  `id` int(11) NOT NULL,
  `veranstaltungID` int(11) NOT NULL,
  `kategorie` int(11) NOT NULL,
  `jahr` int(11) NOT NULL,
  `dauer` int(11) NOT NULL,
  `mannstunden` int(11) NOT NULL,
  `wochentag` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `stichwort`
--

CREATE TABLE IF NOT EXISTS `stichwort` (
  `id` int(11) NOT NULL,
  `kategorie` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `userid` text NOT NULL,
  `passwort` text NOT NULL,
  `usergruppe` text NOT NULL,
  `admin` int(1) NOT NULL,
  `deaktiv` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `user`
--

INSERT INTO `user` (`userid`, `passwort`, `usergruppe`, `admin`, `deaktiv`) VALUES
('admin', '0000573371108185808xrequ36625050066015026743	', 'admin', 1, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `veranstaltung`
--

CREATE TABLE IF NOT EXISTS `veranstaltung` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `name2` text NOT NULL,
  `kategorie` int(11) NOT NULL,
  `datum` date NOT NULL,
  `zeit` varchar(10) NOT NULL,
  `zeitEnde` varchar(10) NOT NULL,
  `fahrzeugeinteilung` int(11) NOT NULL,
  `infoVersandt` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `veranstaltung_kategorie`
--

CREATE TABLE IF NOT EXISTS `veranstaltung_kategorie` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `veranstaltung_kategorie`
--

INSERT INTO `veranstaltung_kategorie` (`id`, `name`) VALUES
(1, 'Einsatz'),
(2, 'Dienstabend'),
(3, 'BSW'),
(4, 'Sonstige');
