-- lb12.arbeitsgeraete definition

CREATE TABLE `arbeitsgeraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.buero_lbz_fuehrung definition

CREATE TABLE `buero_lbz_fuehrung` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.edv definition

CREATE TABLE `edv` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `Eigentuemer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.elektrische_geraete definition

CREATE TABLE `elektrische_geraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.erste_hilfe_und_hygiene definition

CREATE TABLE `erste_hilfe_und_hygiene` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.fahrzeuge_und_fahrzeugladegeraete definition

CREATE TABLE `fahrzeuge_und_fahrzeugladegeraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Loeschbezirk` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.fahrzeughalle_und_werkstatt definition

CREATE TABLE `fahrzeughalle_und_werkstatt` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.kueche_fahrzeughalle definition

CREATE TABLE `kueche_fahrzeughalle` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.loeschgeraete definition

CREATE TABLE `loeschgeraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.mitglieder definition

CREATE TABLE `mitglieder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vorname` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `nachname` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `geburtsdatum` datetime DEFAULT NULL,
  `rang` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `eintrittsdatum` datetime DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.rettungsgeraete definition

CREATE TABLE `rettungsgeraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.schulungsraum_und_ausbildung definition

CREATE TABLE `schulungsraum_und_ausbildung` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.schutzkleidung definition

CREATE TABLE `schutzkleidung` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Feuerwehrangehoeriger` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.signal_und_beleuchtungsgeraete definition

CREATE TABLE `signal_und_beleuchtungsgeraete` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Lagerort` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Fahrzeug_Geraetehaus` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Inventarnummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Artikelbezeichnung` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Typ` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Spezifikation` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Seriennummer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Hersteller` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anzahl` int DEFAULT NULL,
  `Einheit` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Anschaffungsjahr` int DEFAULT NULL,
  `Kosten` decimal(10,2) DEFAULT NULL,
  `Bemerkung` text COLLATE utf8mb4_bin,
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `feuerwehr_heusweiler` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.users definition

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `role` varchar(255) COLLATE utf8mb4_bin DEFAULT 'user',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;