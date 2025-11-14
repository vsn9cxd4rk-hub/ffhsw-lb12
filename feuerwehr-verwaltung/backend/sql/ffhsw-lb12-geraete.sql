-- lb12.eigentuemer definition

CREATE TABLE `eigentuemer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.fahrzeug_geraetehaus definition

CREATE TABLE `fahrzeug_geraetehaus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.feuerwehr_heusweiler definition

CREATE TABLE `feuerwehr_heusweiler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.lagerort definition

CREATE TABLE `lagerort` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- lb12.users definition

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `role` varchar(255) COLLATE utf8mb4_bin DEFAULT 'user',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_arbeitsgeräte_lagerort` (`lagerort_id`),
  KEY `fk_arbeitsgeräte_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_arbeitsgeräte_eigentuemer` (`eigentuemer_id`),
  KEY `fk_arbeitsgeräte_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_arbeitsgeräte_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_arbeitsgeräte_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_arbeitsgeräte_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_arbeitsgeräte_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_buero_lagerort` (`lagerort_id`),
  KEY `fk_buero_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_buero_eigentuemer` (`eigentuemer_id`),
  KEY `fk_buero_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_buero_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_buero_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_buero_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_buero_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `eigentuemer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `Pruefung` tinyint(1) DEFAULT NULL,
  `Norm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_edv_lagerort` (`lagerort_id`),
  KEY `fk_edv_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_edv_eigentuemer` (`eigentuemer_id`),
  KEY `fk_edv_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_edv_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_edv_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_edv_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_edv_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_egeraete_lagerort` (`lagerort_id`),
  KEY `fk_egeraete_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_egeraete_eigentuemer` (`eigentuemer_id`),
  KEY `fk_egeraete_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_egeraete_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_egeraete_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_egeraete_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_egeraete_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_erstehilfe_lagerort` (`lagerort_id`),
  KEY `fk_erstehilfe_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_erstehilfe_eigentuemer` (`eigentuemer_id`),
  KEY `fk_erstehilfe_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_erstehilfe_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_erstehilfe_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_erstehilfe_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_erstehilfe_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_fahrzeuge_lagerort` (`lagerort_id`),
  KEY `fk_fahrzeuge_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_fahrzeuge_eigentuemer` (`eigentuemer_id`),
  KEY `fk_fahrzeuge_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_fahrzeuge_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_fahrzeuge_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_fahrzeuge_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_fahrzeuge_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_werkstatt_lagerort` (`lagerort_id`),
  KEY `fk_werkstatt_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_werkstatt_eigentuemer` (`eigentuemer_id`),
  KEY `fk_werkstatt_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_werkstatt_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_werkstatt_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_werkstatt_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_werkstatt_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_kueche_lagerort` (`lagerort_id`),
  KEY `fk_kueche_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_kueche_eigentuemer` (`eigentuemer_id`),
  KEY `fk_kueche_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_kueche_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_kueche_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_kueche_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_kueche_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_loeschgeraete_fahrzeuge_lagerort` (`lagerort_id`),
  KEY `fk_loeschgeraete_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_loeschgeraete_eigentuemer` (`eigentuemer_id`),
  KEY `fk_loeschgeraete_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_loeschgeraete_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_loeschgeraete_fahrzeuge_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`),
  CONSTRAINT `fk_loeschgeraete_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_loeschgeraete_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_rettungsgeraete_lagerort` (`lagerort_id`),
  KEY `fk_rettungsgeraete_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_rettungsgeraete_eigentuemer` (`eigentuemer_id`),
  KEY `fk_rettungsgeraete_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_rettungsgeraete_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_rettungsgeraete_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_rettungsgeraete_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_rettungsgeraete_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_schulung_lagerort` (`lagerort_id`),
  KEY `fk_schulung_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_schulung_eigentuemer` (`eigentuemer_id`),
  KEY `fk_schulung_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_schulung_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_schulung_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_schulung_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_schulung_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_schutzkleidung_lagerort` (`lagerort_id`),
  KEY `fk_schutzkleidung_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_schutzkleidung_eigentuemer` (`eigentuemer_id`),
  KEY `fk_schutzkleidung_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_schutzkleidung_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_schutzkleidung_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_schutzkleidung_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_schutzkleidung_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
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
  `lagerort_id` int DEFAULT NULL,
  `fahrzeug_geraetehaus_id` int DEFAULT NULL,
  `eigentuemer_id` int DEFAULT NULL,
  `feuerwehr_heusweiler_id` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_signal_lagerort` (`lagerort_id`),
  KEY `fk_signal_geraetehaus` (`fahrzeug_geraetehaus_id`),
  KEY `fk_signal_eigentuemer` (`eigentuemer_id`),
  KEY `fk_signal_feuerwehr` (`feuerwehr_heusweiler_id`),
  CONSTRAINT `fk_signal_eigentuemer` FOREIGN KEY (`eigentuemer_id`) REFERENCES `eigentuemer` (`id`),
  CONSTRAINT `fk_signal_feuerwehr` FOREIGN KEY (`feuerwehr_heusweiler_id`) REFERENCES `feuerwehr_heusweiler` (`id`),
  CONSTRAINT `fk_signal_geraetehaus` FOREIGN KEY (`fahrzeug_geraetehaus_id`) REFERENCES `fahrzeug_geraetehaus` (`id`),
  CONSTRAINT `fk_signal_lagerort` FOREIGN KEY (`lagerort_id`) REFERENCES `lagerort` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

