-- =============================================================================
-- Sammel-Migration: Update von Version 1.1.0 direkt auf 1.2.0
-- Datenbank: FFWVSLB12
--
-- Kompatibel mit: MySQL 8.x und MariaDB 10.x+
--
-- Fasst die beiden Einzel-Migrationen zusammen, die zwischen 1.1.0 und 1.2.0
-- hinzugekommen sind (Version 1.1.1 enthielt keine Datenbankänderungen):
--   1. migrate-add-report-fields.sql  (Einsatzbericht, Kräftenachweis, FENIX)
--   2. migrate-add-bsw-report.sql     (Brandsicherheitswache-Formular)
--
-- Die beiden Migrationen betreffen unterschiedliche Tabellen/Spalten und
-- haben keine Abhängigkeit zueinander. Dieses Skript ist idempotent
-- (kann mehrfach ausgeführt werden).
--
-- Ausführen:
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-1.1.0-to-1.2.0.sql
--
-- Zusätzlich nötig (kein SQL, manueller Admin-Schritt nach dem Update):
--   Die beiden Vorlagen data/Templates/ChecklisteBrandsicherheitswacheLB12.docx
--   und data/Templates/BerichtBrandsicherheitswacheLB12.docx unter
--   Einstellungen → Templates hochladen.
-- =============================================================================

SET NAMES utf8mb4;

-- =============================================================================
-- Teil 1: migrate-add-report-fields.sql (Version 1.1.0 → Zwischenstand)
-- =============================================================================

-- Hilfsprozedur: Spalte hinzufügen falls noch nicht vorhanden
DROP PROCEDURE IF EXISTS `_add_column_if_not_exists`;
DELIMITER //
CREATE PROCEDURE `_add_column_if_not_exists`(
  IN p_table VARCHAR(64),
  IN p_column VARCHAR(64),
  IN p_definition VARCHAR(500)
)
BEGIN
  SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = p_table AND COLUMN_NAME = p_column
  );
  IF @col_exists = 0 THEN
    SET @ddl = CONCAT('ALTER TABLE `', p_table, '` ADD COLUMN `', p_column, '` ', p_definition);
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END //
DELIMITER ;

-- 1. Einsatzbericht-Felder
CALL _add_column_if_not_exists('operations', 'reportType',            'VARCHAR(30) NULL');
CALL _add_column_if_not_exists('operations', 'ilsOrderNumber',        'VARCHAR(50) NULL');
CALL _add_column_if_not_exists('operations', 'callerInfo',            'TEXT NULL');
CALL _add_column_if_not_exists('operations', 'policeInfo',            'TEXT NULL');
CALL _add_column_if_not_exists('operations', 'situationOnArrival',    'TEXT NULL');
CALL _add_column_if_not_exists('operations', 'actionsTaken',          'TEXT NULL');
CALL _add_column_if_not_exists('operations', 'resourcesUsed',         'TEXT NULL');
CALL _add_column_if_not_exists('operations', 'operationType',         'VARCHAR(100) NULL');
CALL _add_column_if_not_exists('operations', 'rescuedPersons',        'INT NOT NULL DEFAULT 0');
CALL _add_column_if_not_exists('operations', 'injuredFirefighters',   'INT NOT NULL DEFAULT 0');
CALL _add_column_if_not_exists('operations', 'deceasedPersons',       'INT NOT NULL DEFAULT 0');
CALL _add_column_if_not_exists('operations', 'deceasedFirefighters',  'INT NOT NULL DEFAULT 0');
CALL _add_column_if_not_exists('operations', 'createdByName',         'VARCHAR(100) NULL');
CALL _add_column_if_not_exists('operations', 'authorRole',            'VARCHAR(50) NULL');

-- 2. Kräftenachweis
CREATE TABLE IF NOT EXISTS `operation_personnel` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `operationId` INT NOT NULL,
  `memberId` INT NOT NULL,
  `vehicleName` VARCHAR(100) NOT NULL,
  `function` VARCHAR(50) NOT NULL,
  `section` VARCHAR(20) NOT NULL DEFAULT 'deployed',
  PRIMARY KEY (`id`),
  INDEX `idx_op_personnel_operation` (`operationId`),
  INDEX `idx_op_personnel_member` (`memberId`),
  CONSTRAINT `fk_op_personnel_operation`
    FOREIGN KEY (`operationId`) REFERENCES `operations` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_op_personnel_member`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. User-Member-Verknüpfung (1:1, optional)
CALL _add_column_if_not_exists('users', 'memberId', 'INT NULL');

SET @uk_exists = (SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND INDEX_NAME = 'users_memberId_key');
SET @sql = IF(@uk_exists = 0,
  'ALTER TABLE `users` ADD UNIQUE KEY `users_memberId_key` (`memberId`)',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND CONSTRAINT_NAME = 'users_memberId_fkey');
SET @sql = IF(@fk_exists = 0,
  'ALTER TABLE `users` ADD CONSTRAINT `users_memberId_fkey` FOREIGN KEY (`memberId`) REFERENCES `members` (`id`) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. Lehrgangskategorien: Qualifikationszuordnung
CALL _add_column_if_not_exists('course_categories', 'qualificationField', 'VARCHAR(50) NULL');

UPDATE `course_categories` SET `qualificationField` = 'qualAGT'             WHERE `id` = 7  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualTruppfuehrer'    WHERE `id` = 3  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualGruppenfuehrer'  WHERE `id` = 4  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualZugfuehrer'      WHERE `id` = 5  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualRadioOperator'   WHERE `id` = 6  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualFirstAid'        WHERE `id` = 2  AND `qualificationField` IS NULL;
UPDATE `course_categories` SET `qualificationField` = 'qualMachinist'       WHERE `id` = 13 AND `qualificationField` IS NULL;

-- 5. AGT-Nachweistabelle
CREATE TABLE IF NOT EXISTS `agt_records` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `memberId`  INT          NOT NULL,
  `type`      VARCHAR(50)  NOT NULL COMMENT 'g26, belastung, einsatzuebung, einsatz',
  `date`      DATETIME     NOT NULL,
  `result`    VARCHAR(50)  NULL COMMENT 'geeignet / nicht_geeignet (nur bei g26)',
  `notes`     VARCHAR(500) NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_agt_records_member` (`memberId`),
  INDEX `idx_agt_records_type_date` (`memberId`, `type`, `date` DESC),
  CONSTRAINT `agt_records_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Einsatzstatistik-Felder
CALL _add_column_if_not_exists('operations', 'operationResult',      'VARCHAR(30) NULL');
CALL _add_column_if_not_exists('operations', 'wasActivelyInvolved',  'TINYINT(1) DEFAULT 1');

-- 7. Berechtigungs-Bits statt starrer Gruppen-IDs
UPDATE `permission_groups` SET `br1` = 1 WHERE `id` = 2; -- Gerätewarte: Fahrzeuge
UPDATE `permission_groups` SET `br3` = 1 WHERE `id` = 2; -- Gerätewarte: Gerätewart-Bereich
UPDATE `permission_groups` SET `br1` = 1 WHERE `id` = 4; -- Maschinisten: Fahrzeuge
UPDATE `permission_groups` SET `br2` = 1 WHERE `id` = 5; -- Gruppenführer: Einsätze

INSERT INTO `permission_groups` (`name`, `description`, `br1`, `br2`, `br3`)
SELECT 'Gerätewart + Gruppenführer', 'Fahrzeuge, Gerätewart-Bereich und Einsätze kombiniert', 1, 1, 1
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `permission_groups` WHERE `name` = 'Gerätewart + Gruppenführer'
);

DROP PROCEDURE IF EXISTS `_add_column_if_not_exists`;

-- =============================================================================
-- Teil 2: migrate-add-bsw-report.sql (Zwischenstand → Version 1.2.0)
-- =============================================================================

SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'events' AND COLUMN_NAME = 'bswData'
);
SET @sql = IF(@col_exists = 0,
  'ALTER TABLE `events` ADD COLUMN `bswData` JSON NULL COMMENT ''Ausgefülltes Formular für Checkliste/Bericht Brandsicherheitswache (Kategorie 3)'' AFTER `notes`',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
