-- =============================================================================
-- Migration: Einsatzbericht- und Kräftenachweis-Felder
-- Datenbank: FFWVSLB12
--
-- Ausführen:
--   mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-add-report-fields.sql
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- Neue Felder in der operations-Tabelle
-- -----------------------------------------------------------------------------
ALTER TABLE `operations`
  ADD COLUMN `reportType` VARCHAR(30) NULL AFTER `tenantId`,
  ADD COLUMN `ilsOrderNumber` VARCHAR(50) NULL AFTER `reportType`,
  ADD COLUMN `callerInfo` TEXT NULL AFTER `ilsOrderNumber`,
  ADD COLUMN `policeInfo` TEXT NULL AFTER `callerInfo`,
  ADD COLUMN `situationOnArrival` TEXT NULL AFTER `policeInfo`,
  ADD COLUMN `actionsTaken` TEXT NULL AFTER `situationOnArrival`,
  ADD COLUMN `resourcesUsed` TEXT NULL AFTER `actionsTaken`,
  ADD COLUMN `operationType` VARCHAR(100) NULL AFTER `resourcesUsed`,
  ADD COLUMN `rescuedPersons` INT NOT NULL DEFAULT 0 AFTER `operationType`,
  ADD COLUMN `injuredFirefighters` INT NOT NULL DEFAULT 0 AFTER `rescuedPersons`,
  ADD COLUMN `deceasedPersons` INT NOT NULL DEFAULT 0 AFTER `injuredFirefighters`,
  ADD COLUMN `deceasedFirefighters` INT NOT NULL DEFAULT 0 AFTER `deceasedPersons`,
  ADD COLUMN `createdByName` VARCHAR(100) NULL AFTER `deceasedFirefighters`,
  ADD COLUMN `authorRole` VARCHAR(50) NULL AFTER `createdByName`;

-- -----------------------------------------------------------------------------
-- Neue Tabelle: operation_personnel (Kräftenachweis)
-- -----------------------------------------------------------------------------
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
