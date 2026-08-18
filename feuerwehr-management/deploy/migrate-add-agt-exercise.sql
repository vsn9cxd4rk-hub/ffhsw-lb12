-- =============================================================================
-- Migration: AGT-Übung-Kennzeichnung für Veranstaltungen der Kategorie "Übung"
--            (category = 5) + Nachweis Übungsteilnahme (PDF-Export)
-- Datenbank: FFWVSLB12
--
-- Kompatibel mit: MySQL 8.x und MariaDB 10.x+
--
-- Ausführen:
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-add-agt-exercise.sql
--
-- Die Migration ist idempotent (kann mehrfach ausgeführt werden).
--
-- Zusätzlich nötig (kein SQL, manueller Admin-Schritt):
--   Die Vorlage data/Templates/UebungsbesuchLB12.docx unter
--   Einstellungen → Templates hochladen, genau wie bei den anderen Vorlagen.
-- =============================================================================

SET NAMES utf8mb4;

SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'events' AND COLUMN_NAME = 'isAgtExercise'
);
SET @sql = IF(@col_exists = 0,
  'ALTER TABLE `events` ADD COLUMN `isAgtExercise` TINYINT(1) NOT NULL DEFAULT 0 AFTER `infoSent`',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
