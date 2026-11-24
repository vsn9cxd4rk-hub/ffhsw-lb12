-- =============================================================================
-- Migration: Update von Version 1.4.1 auf 1.5.0
-- Datenbank: FFWVSLB12
--
-- Kompatibel mit: MySQL 8.x und MariaDB 10.x+
--
-- Schema-Änderungen:
--   Neues Modul "S2 Lage" mit sechs neuen Tabellen:
--     s2_lagen       - Lagen (Einsatz/Übung) der TEL
--     s2_berichte    - stündliche Lageberichte (lfd. Nr., taktische Zeit, Inhalt)
--     s2_abschnitte  - Einsatzabschnitte mit EA-Grenze
--     s2_kraefte     - Einheiten/Fahrzeuge mit Stärke und Status
--     s2_objekte     - taktische Zeichen, Flächen und Linien der Lagekarte
--     s2_meldungen   - Meldungseingang mit Zeitstempel
--   Bestehende Tabellen bleiben unverändert (1.4.1 hatte keine Schemaänderung).
--
-- Keine SQL-Schritte nötig (automatisch beim ersten Backend-Start von 1.5.0):
--   - Gruppen, deren Name "Gruppenführer" enthält, erhalten die neue
--     Berechtigung "S2-Lage führen" (br6). Weitere Gruppen in Einstellungen →
--     Berechtigungsgruppen freischalten.
--
-- Dieses Skript ist idempotent (kann mehrfach ausgeführt werden).
--
-- Ausführen (VOR dem Start des neuen Backends; update.sh macht das automatisch):
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-1.4.1-to-1.5.0.sql
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- S2 Lage (seit 1.5.0): Lagen, stündliche Lageberichte, Einsatzabschnitte,
-- Kräfte, Objekte der Lagekarte, Meldungseingang
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `s2_lagen` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(200) NOT NULL,
  `kommune`    VARCHAR(100) NOT NULL DEFAULT 'Gemeinde Heusweiler',
  `isExercise` TINYINT(1)   NOT NULL DEFAULT 0,
  `status`     VARCHAR(20)  NOT NULL DEFAULT 'aktiv',
  `recipient`  VARCHAR(200) NOT NULL DEFAULT 'fuehrungsstab@saarbruecken.de',
  `contact`    VARCHAR(200) NULL,
  `startedAt`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `endedAt`    DATETIME     NULL,
  `version`    INT          NOT NULL DEFAULT 1,
  `createdBy`  VARCHAR(100) NULL,
  `updatedBy`  VARCHAR(100) NULL,
  `createdAt`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `s2_berichte` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `lageId`       INT          NOT NULL,
  `lfdNr`        INT          NOT NULL,
  `zeit`         VARCHAR(11)  NOT NULL,
  `bearbeiter`   VARCHAR(100) NULL,
  `karteAlsFoto` TINYINT(1)   NOT NULL DEFAULT 0,
  `status`       VARCHAR(20)  NOT NULL DEFAULT 'entwurf',
  `data`         JSON         NOT NULL,
  `version`      INT          NOT NULL DEFAULT 1,
  `createdBy`    VARCHAR(100) NULL,
  `updatedBy`    VARCHAR(100) NULL,
  `createdAt`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `s2_berichte_lageId_lfdNr_key` (`lageId`, `lfdNr`),
  CONSTRAINT `s2_berichte_lageId_fkey`
    FOREIGN KEY (`lageId`) REFERENCES `s2_lagen` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `s2_abschnitte` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `lageId`      INT          NOT NULL,
  `nr`          INT          NOT NULL,
  `name`        VARCHAR(100) NOT NULL,
  `lage`        TEXT         NULL,
  `schwerpunkt` TINYINT(1)   NOT NULL DEFAULT 0,
  `flaeche`     JSON         NULL,
  `version`     INT          NOT NULL DEFAULT 1,
  `updatedBy`   VARCHAR(100) NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `s2_abschnitte_lageId_nr_key` (`lageId`, `nr`),
  CONSTRAINT `s2_abschnitte_lageId_fkey`
    FOREIGN KEY (`lageId`) REFERENCES `s2_lagen` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `s2_kraefte` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `lageId`      INT          NOT NULL,
  `abschnittId` INT          NULL,
  `org`         VARCHAR(20)  NOT NULL,
  `formation`   VARCHAR(100) NULL,
  `bezeichnung` VARCHAR(100) NOT NULL,
  `staerke`     VARCHAR(20)  NULL,
  `status`      VARCHAR(20)  NOT NULL DEFAULT 'alarmiert',
  `bemerkung`   VARCHAR(255) NULL,
  `version`     INT          NOT NULL DEFAULT 1,
  `updatedBy`   VARCHAR(100) NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `s2_kraefte_lageId_idx` (`lageId`),
  CONSTRAINT `s2_kraefte_lageId_fkey`
    FOREIGN KEY (`lageId`) REFERENCES `s2_lagen` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `s2_kraefte_abschnittId_fkey`
    FOREIGN KEY (`abschnittId`) REFERENCES `s2_abschnitte` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `s2_objekte` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `lageId`        INT          NOT NULL,
  `art`           VARCHAR(20)  NOT NULL,
  `typ`           VARCHAR(30)  NOT NULL,
  `punkte`        JSON         NOT NULL,
  `text`          VARCHAR(100) NULL,
  `randkaestchen` TINYINT(1)   NOT NULL DEFAULT 1,
  `version`       INT          NOT NULL DEFAULT 1,
  `updatedBy`     VARCHAR(100) NULL,
  `createdAt`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `s2_objekte_lageId_idx` (`lageId`),
  CONSTRAINT `s2_objekte_lageId_fkey`
    FOREIGN KEY (`lageId`) REFERENCES `s2_lagen` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `s2_meldungen` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `lageId`    INT          NOT NULL,
  `nr`        VARCHAR(20)  NULL,
  `eingangAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `absender`  VARCHAR(100) NULL,
  `ort`       VARCHAR(200) NULL,
  `inhalt`    TEXT         NOT NULL,
  `erledigt`  TINYINT(1)   NOT NULL DEFAULT 0,
  `version`   INT          NOT NULL DEFAULT 1,
  `updatedBy` VARCHAR(100) NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `s2_meldungen_lageId_eingangAt_idx` (`lageId`, `eingangAt`),
  CONSTRAINT `s2_meldungen_lageId_fkey`
    FOREIGN KEY (`lageId`) REFERENCES `s2_lagen` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
