-- =============================================================================
-- Migration: Update von Version 1.3.x auf 1.4.0
-- Datenbank: FFWVSLB12
--
-- Kompatibel mit: MySQL 8.x und MariaDB 10.x+
--
-- Schema-Änderungen:
--   users.mustChangePassword  (Passwortwechsel beim nächsten Login erzwingen)
--   operations.commanderName  (tatsächlicher Einsatzleiter, wenn der Ersteller
--                              nur Einheitenführer war; Freitext)
--
-- Keine SQL-Schritte nötig (automatisch beim ersten Backend-Start von 1.4.0):
--   - Gruppen mit Einsatz-Berechtigung (br2) erhalten die neuen Berechtigungen
--     "Veranstaltungen pflegen" (br4) und "Ausbildung pflegen" (br5).
--   - Benutzer, die noch das alte Standardpasswort haben, müssen es beim
--     nächsten Login ändern.
--
-- Dieses Skript ist idempotent (kann mehrfach ausgeführt werden).
--
-- Ausführen (VOR dem Start des neuen Backends):
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-1.3.x-to-1.4.0.sql
-- =============================================================================

SET NAMES utf8mb4;

SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'mustChangePassword'
);
SET @sql = IF(@col_exists = 0,
  'ALTER TABLE `users` ADD COLUMN `mustChangePassword` TINYINT(1) NOT NULL DEFAULT 0 AFTER `isActive`',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'operations' AND COLUMN_NAME = 'commanderName'
);
SET @sql = IF(@col_exists = 0,
  'ALTER TABLE `operations` ADD COLUMN `commanderName` VARCHAR(100) NULL AFTER `authorRole`',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
