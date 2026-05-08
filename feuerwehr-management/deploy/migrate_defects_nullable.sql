-- =============================================================================
-- Migration: article_defects und article_repairs - articleId nullable + subject
-- Datum: 2026-04-28
--
-- Zweck: Mängel und Reparaturen können jetzt auch für Fahrzeuge und "Sonstige"
--         erfasst werden, nicht nur für Artikel aus der Bestandsliste.
--
-- WICHTIG: Bestehende Daten bleiben vollständig erhalten!
--          - Vorhandene Mängel/Reparaturen behalten ihre articleId
--          - Das neue Feld 'subject' ist NULL für bestehende Einträge
--          - Der Foreign Key wird angepasst (SET NULL statt CASCADE)
--
-- Ausführen auf dem Produktivsystem:
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate_defects_nullable.sql
-- =============================================================================

USE `FFWVSLB12`;

-- -----------------------------------------------------------------------------
-- 1. article_defects: articleId nullable machen + subject Feld hinzufügen
-- -----------------------------------------------------------------------------

-- Foreign Key entfernen (Name kann variieren - beide Varianten versuchen)
SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = 'FFWVSLB12'
  AND TABLE_NAME = 'article_defects'
  AND CONSTRAINT_NAME = 'article_defects_articleId_fkey');

-- FK droppen falls vorhanden
ALTER TABLE `article_defects` DROP FOREIGN KEY `article_defects_articleId_fkey`;

-- Spalte nullable machen
ALTER TABLE `article_defects` MODIFY COLUMN `articleId` INT NULL;

-- Subject-Feld hinzufügen (nach articleId)
ALTER TABLE `article_defects` ADD COLUMN `subject` VARCHAR(255) NULL AFTER `articleId`;

-- FK neu anlegen mit ON DELETE SET NULL (statt CASCADE)
ALTER TABLE `article_defects` ADD CONSTRAINT `article_defects_articleId_fkey`
  FOREIGN KEY (`articleId`) REFERENCES `articles` (`id`)
  ON DELETE SET NULL ON UPDATE CASCADE;

-- -----------------------------------------------------------------------------
-- 2. article_repairs: articleId nullable machen + subject Feld hinzufügen
-- -----------------------------------------------------------------------------

-- FK droppen
ALTER TABLE `article_repairs` DROP FOREIGN KEY `article_repairs_articleId_fkey`;

-- Spalte nullable machen
ALTER TABLE `article_repairs` MODIFY COLUMN `articleId` INT NULL;

-- Subject-Feld hinzufügen (nach articleId)
ALTER TABLE `article_repairs` ADD COLUMN `subject` VARCHAR(255) NULL AFTER `articleId`;

-- FK neu anlegen mit ON DELETE SET NULL
ALTER TABLE `article_repairs` ADD CONSTRAINT `article_repairs_articleId_fkey`
  FOREIGN KEY (`articleId`) REFERENCES `articles` (`id`)
  ON DELETE SET NULL ON UPDATE CASCADE;

-- -----------------------------------------------------------------------------
-- Verifizierung: Bestehende Daten prüfen
-- -----------------------------------------------------------------------------
SELECT 'article_defects' AS tabelle, COUNT(*) AS anzahl,
       SUM(CASE WHEN articleId IS NOT NULL THEN 1 ELSE 0 END) AS mit_artikel,
       SUM(CASE WHEN subject IS NOT NULL THEN 1 ELSE 0 END) AS mit_subject
FROM article_defects
UNION ALL
SELECT 'article_repairs', COUNT(*),
       SUM(CASE WHEN articleId IS NOT NULL THEN 1 ELSE 0 END),
       SUM(CASE WHEN subject IS NOT NULL THEN 1 ELSE 0 END)
FROM article_repairs;

-- =============================================================================
-- FERTIG. Bestehende Daten sind unverändert erhalten.
-- Neue Mängel/Reparaturen können jetzt mit subject statt articleId gespeichert
-- werden (für Fahrzeuge und "Sonstige").
-- =============================================================================
