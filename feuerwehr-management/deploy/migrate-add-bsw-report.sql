-- =============================================================================
-- Migration: Brandsicherheitswache-Formular (Checkliste + Bericht) für
--            Veranstaltungen der Kategorie "BSW" (category = 3)
-- Datenbank: FFWVSLB12
--
-- Kompatibel mit: MySQL 8.x und MariaDB 10.x+
--
-- Ausführen:
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-add-bsw-report.sql
--
-- Die Migration ist idempotent (kann mehrfach ausgeführt werden).
--
-- Zusätzlich nötig (kein SQL, manueller Admin-Schritt):
--   Die beiden Vorlagen data/Templates/ChecklisteBrandsicherheitswacheLB12.docx
--   und data/Templates/BerichtBrandsicherheitswacheLB12.docx unter
--   Einstellungen → Templates hochladen, genau wie bei den anderen Vorlagen.
-- =============================================================================

SET NAMES utf8mb4;

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
