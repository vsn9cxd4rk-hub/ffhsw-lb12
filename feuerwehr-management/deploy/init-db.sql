-- =============================================================================
-- Feuerwehr Management System - Datenbankinitialisierung
-- Datenbank:  FFWVSLB12
-- Benutzer:   FFWVSLB12
-- Passwort:   Ffw#VSLB12!25
--
-- Ausführen als MySQL-root:
--   sudo mysql < /pfad/zum/deploy/init-db.sql
-- oder interaktiv:
--   sudo mysql -u root -p < deploy/init-db.sql
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------
-- Datenbank und Benutzer anlegen
-- -----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `FFWVSLB12`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'FFWVSLB12'@'localhost' IDENTIFIED BY 'Ffw#VSLB12!25';
GRANT ALL PRIVILEGES ON `FFWVSLB12`.* TO 'FFWVSLB12'@'localhost';
FLUSH PRIVILEGES;

USE `FFWVSLB12`;

-- =============================================================================
-- TABELLEN
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Berechtigungsgruppen (76 Berechtigungs-Bits BR0-BR75)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `permission_groups` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) NULL,
  `br0`  TINYINT(1) NOT NULL DEFAULT 0, `br1`  TINYINT(1) NOT NULL DEFAULT 0,
  `br2`  TINYINT(1) NOT NULL DEFAULT 0, `br3`  TINYINT(1) NOT NULL DEFAULT 0,
  `br4`  TINYINT(1) NOT NULL DEFAULT 0, `br5`  TINYINT(1) NOT NULL DEFAULT 0,
  `br6`  TINYINT(1) NOT NULL DEFAULT 0, `br7`  TINYINT(1) NOT NULL DEFAULT 0,
  `br8`  TINYINT(1) NOT NULL DEFAULT 0, `br9`  TINYINT(1) NOT NULL DEFAULT 0,
  `br10` TINYINT(1) NOT NULL DEFAULT 0, `br11` TINYINT(1) NOT NULL DEFAULT 0,
  `br12` TINYINT(1) NOT NULL DEFAULT 0, `br13` TINYINT(1) NOT NULL DEFAULT 0,
  `br14` TINYINT(1) NOT NULL DEFAULT 0, `br15` TINYINT(1) NOT NULL DEFAULT 0,
  `br16` TINYINT(1) NOT NULL DEFAULT 0, `br17` TINYINT(1) NOT NULL DEFAULT 0,
  `br18` TINYINT(1) NOT NULL DEFAULT 0, `br19` TINYINT(1) NOT NULL DEFAULT 0,
  `br20` TINYINT(1) NOT NULL DEFAULT 0, `br21` TINYINT(1) NOT NULL DEFAULT 0,
  `br22` TINYINT(1) NOT NULL DEFAULT 0, `br23` TINYINT(1) NOT NULL DEFAULT 0,
  `br24` TINYINT(1) NOT NULL DEFAULT 0, `br25` TINYINT(1) NOT NULL DEFAULT 0,
  `br26` TINYINT(1) NOT NULL DEFAULT 0, `br27` TINYINT(1) NOT NULL DEFAULT 0,
  `br28` TINYINT(1) NOT NULL DEFAULT 0, `br29` TINYINT(1) NOT NULL DEFAULT 0,
  `br30` TINYINT(1) NOT NULL DEFAULT 0, `br31` TINYINT(1) NOT NULL DEFAULT 0,
  `br32` TINYINT(1) NOT NULL DEFAULT 0, `br33` TINYINT(1) NOT NULL DEFAULT 0,
  `br34` TINYINT(1) NOT NULL DEFAULT 0, `br35` TINYINT(1) NOT NULL DEFAULT 0,
  `br36` TINYINT(1) NOT NULL DEFAULT 0, `br37` TINYINT(1) NOT NULL DEFAULT 0,
  `br38` TINYINT(1) NOT NULL DEFAULT 0, `br39` TINYINT(1) NOT NULL DEFAULT 0,
  `br40` TINYINT(1) NOT NULL DEFAULT 0, `br41` TINYINT(1) NOT NULL DEFAULT 0,
  `br42` TINYINT(1) NOT NULL DEFAULT 0, `br43` TINYINT(1) NOT NULL DEFAULT 0,
  `br44` TINYINT(1) NOT NULL DEFAULT 0, `br45` TINYINT(1) NOT NULL DEFAULT 0,
  `br46` TINYINT(1) NOT NULL DEFAULT 0, `br47` TINYINT(1) NOT NULL DEFAULT 0,
  `br48` TINYINT(1) NOT NULL DEFAULT 0, `br49` TINYINT(1) NOT NULL DEFAULT 0,
  `br50` TINYINT(1) NOT NULL DEFAULT 0, `br51` TINYINT(1) NOT NULL DEFAULT 0,
  `br52` TINYINT(1) NOT NULL DEFAULT 0, `br53` TINYINT(1) NOT NULL DEFAULT 0,
  `br54` TINYINT(1) NOT NULL DEFAULT 0, `br55` TINYINT(1) NOT NULL DEFAULT 0,
  `br56` TINYINT(1) NOT NULL DEFAULT 0, `br57` TINYINT(1) NOT NULL DEFAULT 0,
  `br58` TINYINT(1) NOT NULL DEFAULT 0, `br59` TINYINT(1) NOT NULL DEFAULT 0,
  `br60` TINYINT(1) NOT NULL DEFAULT 0, `br61` TINYINT(1) NOT NULL DEFAULT 0,
  `br62` TINYINT(1) NOT NULL DEFAULT 0, `br63` TINYINT(1) NOT NULL DEFAULT 0,
  `br64` TINYINT(1) NOT NULL DEFAULT 0, `br65` TINYINT(1) NOT NULL DEFAULT 0,
  `br66` TINYINT(1) NOT NULL DEFAULT 0, `br67` TINYINT(1) NOT NULL DEFAULT 0,
  `br68` TINYINT(1) NOT NULL DEFAULT 0, `br69` TINYINT(1) NOT NULL DEFAULT 0,
  `br70` TINYINT(1) NOT NULL DEFAULT 0, `br71` TINYINT(1) NOT NULL DEFAULT 0,
  `br72` TINYINT(1) NOT NULL DEFAULT 0, `br73` TINYINT(1) NOT NULL DEFAULT 0,
  `br74` TINYINT(1) NOT NULL DEFAULT 0, `br75` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Benutzer
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `username`  VARCHAR(100) NOT NULL,
  `email`     VARCHAR(255) NULL,
  `password`  VARCHAR(255) NOT NULL,
  `name`      VARCHAR(255) NULL,
  `isAdmin`   TINYINT(1)   NOT NULL DEFAULT 0,
  `isActive`  TINYINT(1)   NOT NULL DEFAULT 1,
  `groupId`   INT          NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_username_key` (`username`),
  UNIQUE KEY `users_email_key` (`email`),
  CONSTRAINT `users_groupId_fkey`
    FOREIGN KEY (`groupId`) REFERENCES `permission_groups` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Refresh-Tokens (JWT)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `refresh_tokens` (
  `id`        VARCHAR(191) NOT NULL,
  `token`     VARCHAR(512) NOT NULL,
  `userId`    INT          NOT NULL,
  `expiresAt` DATETIME     NOT NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `refresh_tokens_token_key` (`token`),
  CONSTRAINT `refresh_tokens_userId_fkey`
    FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitgliedergruppen
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_groups` (
  `id`                   INT         NOT NULL AUTO_INCREMENT,
  `name`                 VARCHAR(100) NOT NULL,
  `employeeNumberPrefix` VARCHAR(20)  NULL,
  `nextEmployeeNumber`   INT          NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `members` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `groupId`         INT          NULL,
  `salutation`      VARCHAR(20)  NULL,
  `lastName`        VARCHAR(100) NOT NULL,
  `firstName`       VARCHAR(100) NOT NULL,
  `street`          VARCHAR(255) NULL,
  `city`            VARCHAR(100) NULL,
  `phonePrivate`    VARCHAR(50)  NULL,
  `phoneMobile`     VARCHAR(50)  NULL,
  `phoneWork`       VARCHAR(50)  NULL,
  `telegramId`      VARCHAR(100) NULL,
  `email`           VARCHAR(255) NULL,
  `email2`          VARCHAR(255) NULL,
  `occupation`      VARCHAR(255) NULL,
  `nationality`     VARCHAR(100) NULL,
  `rank`            VARCHAR(100) NULL,
  `isInactive`      TINYINT(1)   NOT NULL DEFAULT 0,
  `memberSince`     DATETIME     NULL,
  `memberUntil`     DATETIME     NULL,
  `birthDate`       DATETIME     NULL,
  `marriageDate`    DATETIME     NULL,
  `comment`         TEXT         NULL,
  `driverLicenseNo` VARCHAR(100) NULL,
  `driverAuthNo`    VARCHAR(100) NULL,
  `serviceCardNo`   VARCHAR(100) NULL,
  `healthInsurance` VARCHAR(255) NULL,
  `medications`     TEXT         NULL,
  `conditions`      TEXT         NULL,
  `swimmingBadge`   VARCHAR(100) NULL,
  `sportsRating`    VARCHAR(100) NULL,
  `emailDisabled`       TINYINT(1)   NOT NULL DEFAULT 0,
  `qualLicenseC`        TINYINT(1)   NOT NULL DEFAULT 0,
  `qualLicenseB`        TINYINT(1)   NOT NULL DEFAULT 0,
  `qualFirstAid`        TINYINT(1)   NOT NULL DEFAULT 0,
  `qualRadioOperator`   TINYINT(1)   NOT NULL DEFAULT 0,
  `qualMachinist`       TINYINT(1)   NOT NULL DEFAULT 0,
  `qualTruppmann`       TINYINT(1)   NOT NULL DEFAULT 0,
  `qualTruppfuehrer`    TINYINT(1)   NOT NULL DEFAULT 0,
  `qualGruppenfuehrer`  TINYINT(1)   NOT NULL DEFAULT 0,
  `qualZugfuehrer`      TINYINT(1)   NOT NULL DEFAULT 0,
  `qualRettSan`         TINYINT(1)   NOT NULL DEFAULT 0,
  `qualFwSan`           TINYINT(1)   NOT NULL DEFAULT 0,
  `qualVerbandfuehrer`  TINYINT(1)   NOT NULL DEFAULT 0,
  `qualAGT`             TINYINT(1)   NOT NULL DEFAULT 0,
  `qualTH1`             TINYINT(1)   NOT NULL DEFAULT 0,
  `tenantId`            INT          NULL,
  `createdAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt`       DATETIME     NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `members_groupId_fkey`
    FOREIGN KEY (`groupId`) REFERENCES `member_groups` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Angehörige
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_family` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `memberId`     INT          NOT NULL,
  `name`         VARCHAR(255) NOT NULL,
  `phone`        VARCHAR(50)  NULL,
  `phone2`       VARCHAR(50)  NULL,
  `email`        VARCHAR(255) NULL,
  `street`       VARCHAR(255) NULL,
  `city`         VARCHAR(100) NULL,
  `relationship` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `member_family_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Arbeitgeber
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_work` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `memberId`      INT          NOT NULL,
  `employer`      VARCHAR(255) NULL,
  `street`        VARCHAR(255) NULL,
  `city`          VARCHAR(100) NULL,
  `phone`         VARCHAR(50)  NULL,
  `contactPerson` VARCHAR(255) NULL,
  `email`         VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_work_memberId_key` (`memberId`),
  CONSTRAINT `member_work_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Bankdaten
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_bank` (
  `id`       INT         NOT NULL AUTO_INCREMENT,
  `memberId` INT         NOT NULL,
  `iban`     VARCHAR(34) NULL,
  `bic`      VARCHAR(11) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_bank_memberId_key` (`memberId`),
  CONSTRAINT `member_bank_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Untersuchungen (G25/G26/G30)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_examinations` (
  `id`               INT        NOT NULL AUTO_INCREMENT,
  `memberId`         INT        NOT NULL,
  `g25Date`          DATETIME   NULL,
  `g26Date`          DATETIME   NULL,
  `g30Date`          DATETIME   NULL,
  `agtTrainingDate`  DATETIME   NULL,
  `lkwLicenseExpiry` DATETIME   NULL,
  `notifyG25`        TINYINT(1) NOT NULL DEFAULT 0,
  `notifyG26`        TINYINT(1) NOT NULL DEFAULT 0,
  `notifyG30`        TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_examinations_memberId_key` (`memberId`),
  CONSTRAINT `member_examinations_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Verfügbarkeit
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_availability` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `memberId`   INT          NOT NULL,
  `status`     VARCHAR(50)  NOT NULL,
  `validFrom`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `validUntil` DATETIME     NULL,
  `notes`      VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `member_availability_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Mitglieder - Änderungshistorie
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_history` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `memberId`  INT          NOT NULL,
  `field`     VARCHAR(100) NOT NULL,
  `oldValue`  TEXT         NULL,
  `newValue`  TEXT         NULL,
  `changedBy` VARCHAR(100) NOT NULL,
  `changedAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `member_history_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Fahrzeuge
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicles` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(100) NOT NULL,
  `description`  TEXT         NULL,
  `licensePlate` VARCHAR(20)  NULL,
  `callSign`     VARCHAR(50)  NULL,
  `seats`        INT          NULL,
  `minCrew`      INT          NULL,
  `maxCrew`      INT          NULL,
  `licenseClass` VARCHAR(20)  NULL,
  `isRetired`    TINYINT(1)   NOT NULL DEFAULT 0,
  `isTrailer`    TINYINT(1)   NOT NULL DEFAULT 0,
  `sortOrder`    INT          NOT NULL DEFAULT 0,
  `tenantId`     INT          NULL,
  `createdAt`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Fahrzeuge - Prüftermine
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle_inspections` (
  `id`            INT        NOT NULL AUTO_INCREMENT,
  `vehicleId`     INT        NOT NULL,
  `tuevDate`      DATETIME   NULL,
  `spDate`        DATETIME   NULL,
  `serviceDate`   DATETIME   NULL,
  `notifyTuev`    TINYINT(1) NOT NULL DEFAULT 0,
  `notifySp`      TINYINT(1) NOT NULL DEFAULT 0,
  `notifyService` TINYINT(1) NOT NULL DEFAULT 0,
  `notes`         TEXT       NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vehicle_inspections_vehicleId_key` (`vehicleId`),
  CONSTRAINT `vehicle_inspections_vehicleId_fkey`
    FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Fahrtenbuch
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `logbook_entries` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `vehicleId`    INT          NOT NULL,
  `date`         DATETIME     NOT NULL,
  `driver`       VARCHAR(255) NOT NULL,
  `startMileage` INT          NOT NULL,
  `endMileage`   INT          NOT NULL,
  `purpose`      VARCHAR(500) NOT NULL,
  `destination`  VARCHAR(500) NULL,
  `notes`        TEXT         NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `logbook_entries_vehicleId_fkey`
    FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Geräteprüfungen
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `equipment_inspections` (
  `id`             INT          NOT NULL AUTO_INCREMENT,
  `vehicleId`      INT          NOT NULL,
  `type`           VARCHAR(100) NOT NULL,
  `lastInspection` DATETIME     NULL,
  `nextInspection` DATETIME     NULL,
  `notes`          TEXT         NULL,
  `notifyDue`      TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `equipment_inspections_vehicleId_fkey`
    FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Geräteklassen
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `device_classes` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `sortOrder` INT          NOT NULL DEFAULT 0,
  `createdAt` DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_classes_name_key` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Geräte-Unterklassen
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `device_subclasses` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `deviceClassId` INT          NOT NULL,
  `name`          VARCHAR(255) NOT NULL,
  `sortOrder`     INT          NOT NULL DEFAULT 0,
  `createdAt`     DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_subclasses_deviceClassId_name_key` (`deviceClassId`, `name`),
  CONSTRAINT `device_subclasses_deviceClassId_fkey`
    FOREIGN KEY (`deviceClassId`) REFERENCES `device_classes` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Prüfkriterien (je Unterklasse)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `inspection_criteria` (
  `id`               INT          NOT NULL AUTO_INCREMENT,
  `deviceSubclassId` INT          NOT NULL,
  `name`             VARCHAR(255) NOT NULL,
  `sortOrder`        INT          NOT NULL DEFAULT 0,
  `createdAt`        DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  CONSTRAINT `inspection_criteria_deviceSubclassId_fkey`
    FOREIGN KEY (`deviceSubclassId`) REFERENCES `device_subclasses` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Lager
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouses` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) NULL,
  `vehicleId`   INT          NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `warehouses_vehicleId_fkey`
    FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Artikel / Bestandsliste
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `articles` (
  `id`                 INT            NOT NULL AUTO_INCREMENT,
  `name`               VARCHAR(255)   NOT NULL,
  `manufacturer`       VARCHAR(255)   NULL,
  `articleType`        VARCHAR(100)   NULL,
  `description`        TEXT           NULL,
  `inspectionInterval` INT            NULL,
  `imagePath`          VARCHAR(500)   NULL,
  `value`              DECIMAL(10,2)  NULL,
  `ean`                VARCHAR(50)    NULL,
  `isExtinguisher`     TINYINT(1)     NOT NULL DEFAULT 0,
  `inventoryNumber`    VARCHAR(100)   NULL,
  `warehouseId`        INT            NULL,
  `deviceSubclassId`   INT            NULL,
  `manufacturingDate`  DATETIME(3)    NULL,
  `specification`      VARCHAR(500)   NULL,
  `serialNumber`       VARCHAR(255)   NULL,
  `din`                VARCHAR(100)   NULL,
  `isDecommissioned`   TINYINT(1)     NOT NULL DEFAULT 0,
  `createdAt`          DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`          DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `articles_inventoryNumber_key` (`inventoryNumber`),
  CONSTRAINT `articles_warehouseId_fkey`
    FOREIGN KEY (`warehouseId`) REFERENCES `warehouses` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `articles_deviceSubclassId_fkey`
    FOREIGN KEY (`deviceSubclassId`) REFERENCES `device_subclasses` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Artikel - Zuweisung zu Lager/Fahrzeug
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_assignments` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `articleId`   INT          NOT NULL,
  `warehouseId` INT          NOT NULL,
  `vehicleId`   INT          NULL,
  `quantity`    INT          NOT NULL DEFAULT 1,
  `assignedTo`  VARCHAR(255) NULL,
  `assignedAt`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `notes`       VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `article_assignments_articleId_fkey`
    FOREIGN KEY (`articleId`) REFERENCES `articles` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `article_assignments_warehouseId_fkey`
    FOREIGN KEY (`warehouseId`) REFERENCES `warehouses` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `article_assignments_vehicleId_fkey`
    FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Artikel - Prüfbuch (Prüfungen je Artikel)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_inspections` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `articleId`     INT          NOT NULL,
  `inspectedAt`   DATETIME     NOT NULL,
  `inspectedBy`   VARCHAR(255) NOT NULL,
  `result`        VARCHAR(50)  NOT NULL,
  `notes`         TEXT         NULL,
  `nextDueDate`   DATETIME     NULL,
  `createdAt`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `article_inspections_articleId_fkey`
    FOREIGN KEY (`articleId`) REFERENCES `articles` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Prüfkriterien-Einzelergebnisse (io/nio je Kriterium pro Prüfung)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `inspection_criterion_results` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `inspectionId` INT         NOT NULL,
  `criterionId`  INT         NOT NULL,
  `result`       VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `inspection_criterion_results_inspId_critId_key` (`inspectionId`, `criterionId`),
  CONSTRAINT `inspection_criterion_results_inspectionId_fkey`
    FOREIGN KEY (`inspectionId`) REFERENCES `article_inspections` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inspection_criterion_results_criterionId_fkey`
    FOREIGN KEY (`criterionId`) REFERENCES `inspection_criteria` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Einsätze
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `operations` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `operationNumber` VARCHAR(50)  NULL,
  `officialNumber`  VARCHAR(50)  NULL,
  `date`            DATETIME     NOT NULL,
  `alarmTime`       VARCHAR(10)  NULL,
  `departureTime`   VARCHAR(10)  NULL,
  `arrivalTime`     VARCHAR(10)  NULL,
  `returnTime`      VARCHAR(10)  NULL,
  `location`        VARCHAR(500) NOT NULL,
  `district`        VARCHAR(100) NULL,
  `keyword`         VARCHAR(255) NULL,
  `vehicles`        TEXT         NULL,
  `description`     TEXT         NULL,
  `leaderCount`     INT          NOT NULL DEFAULT 0,
  `memberCount`     INT          NOT NULL DEFAULT 0,
  `commanderId`     INT          NULL,
  `commanderBfId`   INT          NULL,
  `categoryId`      INT          NULL,
  `tenantId`        INT          NULL,
  `createdAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Einsätze - Fahrzeugzeiten
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `operation_times` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `operationId`   INT          NOT NULL,
  `vehicleId`     INT          NULL,
  `vehicleName`   VARCHAR(100) NOT NULL,
  `alarmTime`     VARCHAR(10)  NULL,
  `departureTime` VARCHAR(10)  NULL,
  `arrivalTime`   VARCHAR(10)  NULL,
  `returnTime`    VARCHAR(10)  NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `operation_times_operationId_fkey`
    FOREIGN KEY (`operationId`) REFERENCES `operations` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Einsätze - Berichte
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `operation_reports` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `operationId` INT          NOT NULL,
  `content`     TEXT         NOT NULL,
  `createdBy`   VARCHAR(100) NOT NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `operation_reports_operationId_fkey`
    FOREIGN KEY (`operationId`) REFERENCES `operations` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Einsätze - Dokumente (PDF-Uploads)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `operation_documents` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `operationId` INT          NOT NULL,
  `fileName`    VARCHAR(255) NOT NULL,
  `filePath`    VARCHAR(500) NOT NULL,
  `fileSize`    INT          NOT NULL,
  `mimeType`    VARCHAR(100) NOT NULL,
  `uploadedBy`  VARCHAR(100) NOT NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `operation_documents_operationId_fkey`
    FOREIGN KEY (`operationId`) REFERENCES `operations` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Veranstaltungen / Dienstabende
-- (category: 1=Einsatz, 2=Dienstabend, 3=BSW, 4=Sonstige, 5=Übung)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `events` (
  `id`                   INT          NOT NULL AUTO_INCREMENT,
  `name`                 VARCHAR(255) NOT NULL,
  `name2`                VARCHAR(255) NULL,
  `category`             INT          NOT NULL DEFAULT 2,
  `date`                 DATETIME     NOT NULL,
  `startTime`            VARCHAR(10)  NULL,
  `endTime`              VARCHAR(10)  NULL,
  `hasVehicleAssignment` TINYINT(1)   NOT NULL DEFAULT 0,
  `infoSent`             TINYINT(1)   NOT NULL DEFAULT 0,
  `notes`                TEXT         NULL,
  `tenantId`             INT          NULL,
  `createdAt`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Anwesenheiten
-- (status: present / absent / excused)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `attendances` (
  `id`        INT         NOT NULL AUTO_INCREMENT,
  `year`      INT         NOT NULL,
  `eventId`   INT         NOT NULL,
  `memberId`  INT         NOT NULL,
  `status`    VARCHAR(20) NOT NULL DEFAULT 'present',
  `createdAt` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `attendances_eventId_memberId_key` (`eventId`, `memberId`),
  CONSTRAINT `attendances_eventId_fkey`
    FOREIGN KEY (`eventId`) REFERENCES `events` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `attendances_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Abwesenheiten
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `absences` (
  `id`       INT      NOT NULL AUTO_INCREMENT,
  `memberId` INT      NOT NULL,
  `eventId`  INT      NULL,
  `date`     DATETIME NOT NULL,
  `reason`   INT      NOT NULL DEFAULT 0,
  `notes`    TEXT     NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `absences_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Veranstaltungen - Dokumente
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_documents` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `eventId`     INT          NOT NULL,
  `fileName`    VARCHAR(255) NOT NULL,
  `filePath`    VARCHAR(500) NOT NULL,
  `fileSize`    INT          NOT NULL,
  `mimeType`    VARCHAR(100) NOT NULL,
  `uploadedBy`  VARCHAR(100) NOT NULL,
  `createdAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `event_documents_eventId_fkey`
    FOREIGN KEY (`eventId`) REFERENCES `events` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Templates (Dokument-Vorlagen)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `templates` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `filePath`  VARCHAR(500) NOT NULL,
  `fileSize`  INT          NOT NULL,
  `mimeType`  VARCHAR(100) NOT NULL,
  `createdBy` VARCHAR(100) NOT NULL,
  `updatedBy` VARCHAR(100) NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `templates_name_key` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Template-Änderungshistorie
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `template_history` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `templateId` INT          NOT NULL,
  `action`     VARCHAR(50)  NOT NULL,
  `changedBy`  VARCHAR(100) NOT NULL,
  `changedAt`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `template_history_templateId_fkey`
    FOREIGN KEY (`templateId`) REFERENCES `templates` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Brandsicherheitswachen
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `fire_watches` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `date`      DATETIME     NOT NULL,
  `startTime` VARCHAR(10)  NULL,
  `endTime`   VARCHAR(10)  NULL,
  `location`  VARCHAR(500) NULL,
  `notes`     TEXT         NULL,
  `tenantId`  INT          NULL,
  `createdAt` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Ausbildungs-Kategorien (Lehrgänge)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_categories` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Ausbildungen / Lehrgänge (je Mitglied)
-- (status: pending / active / completed / failed)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `courses` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `memberId`   INT          NOT NULL,
  `categoryId` INT          NOT NULL,
  `status`     VARCHAR(50)  NOT NULL DEFAULT 'pending',
  `startDate`  DATETIME     NULL,
  `endDate`    DATETIME     NULL,
  `location`   VARCHAR(255) NULL,
  `notes`           TEXT         NULL,
  `certificatePath` VARCHAR(500) NULL,
  `createdAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `courses_memberId_fkey`
    FOREIGN KEY (`memberId`) REFERENCES `members` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `courses_categoryId_fkey`
    FOREIGN KEY (`categoryId`) REFERENCES `course_categories` (`id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Dienstgrade
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ranks` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(100) NOT NULL,
  `abbreviation` VARCHAR(20)  NOT NULL,
  `sortOrder`    INT          NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Abwesenheitsgründe
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `absence_reasons` (
  `id`    INT         NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(100) NOT NULL,
  `color` VARCHAR(20)  NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Einstellungen (Key-Value)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `settings` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `key`         VARCHAR(100) NOT NULL,
  `value`       TEXT         NOT NULL,
  `description` VARCHAR(500) NULL,
  `updatedAt`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `settings_key_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Betriebsjahre
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `years` (
  `id`       INT        NOT NULL AUTO_INCREMENT,
  `year`     INT        NOT NULL,
  `isActive` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `years_year_key` (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- INITIALDATEN (Seed)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Berechtigungsgruppen
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `permission_groups`
  (`id`, `name`, `description`,
   `br0`,`br1`,`br2`,`br3`,`br4`,`br5`,`br6`,`br7`,`br8`,`br9`,
   `br10`,`br11`,`br12`,`br13`,`br14`,`br15`,`br16`,`br17`,`br18`,`br19`,
   `br20`,`br21`,`br22`,`br23`,`br24`,`br25`,`br26`,`br27`,`br28`,`br29`,
   `br30`,`br31`,`br32`,`br33`,`br34`,`br35`,`br36`,`br37`,`br38`,`br39`,
   `br40`,`br41`,`br42`,`br43`,`br44`,`br45`,`br46`,`br47`,`br48`,`br49`,
   `br50`,`br51`,`br52`,`br53`,`br54`,`br55`,`br56`,`br57`,`br58`,`br59`,
   `br60`,`br61`,`br62`,`br63`,`br64`,`br65`,`br66`,`br67`,`br68`,`br69`,
   `br70`,`br71`,`br72`,`br73`,`br74`,`br75`)
VALUES
  -- Administrator: Vollzugriff
  (1, 'Administrator', 'Vollzugriff auf alle Funktionen',
   1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,
   1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,
   1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,
   1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1),
  -- Gerätewarte: Fahrzeuge, Bestandsliste, Prüfbuch (kein Personal/Einsätze/Veranstaltungen/Ausbildung)
  (2, 'Gerätewarte', 'Zugriff auf Fahrzeuge, Bestandsliste und Prüfbuch',
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0),
  -- Benutzer: Personal (eigene), Veranstaltungen, Ausbildung (kein Fahrzeuge/Bestandsliste/Prüfbuch/Einsätze)
  (3, 'Benutzer', 'Zugriff auf eigene Daten, Veranstaltungen und Ausbildung',
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0),
  -- Maschinisten: wie Benutzer + Fahrzeuge/Fahrtenbuch
  (4, 'Maschinisten', 'Wie Benutzer, zusätzlich Fahrzeuge und Fahrtenbuch',
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0),
  -- Gruppenführer: wie Benutzer + Einsätze/Veranstaltungen (Berichte anlegen)
  (5, 'Gruppenführer', 'Wie Benutzer, zusätzlich Berichte bei Einsätzen und Veranstaltungen',
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,
   0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0);

-- -----------------------------------------------------------------------------
-- Admin-Benutzer
-- HINWEIS: Der Passwort-Hash wird von "npm run seed" (bcrypt) korrekt gesetzt.
-- Dieser Eintrag ist ein Platzhalter. Nach dem Seed-Schritt ist das
-- Passwort "Admin123!" (muss nach erster Anmeldung geändert werden).
-- -----------------------------------------------------------------------------
-- INSERT IGNORE INTO `users`
--  (`id`, `username`, `email`, `password`, `name`, `isAdmin`, `isActive`, `groupId`)
-- VALUES
--  (1, 'admin', 'admin@feuerwehr.local',
--   'PENDING_BCRYPT_HASH_SET_BY_SEED',
--   'Administrator', 1, 1, 1);

-- -----------------------------------------------------------------------------
-- Dienstgrade
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `ranks` (`id`, `name`, `abbreviation`, `sortOrder`) VALUES
  (1,  'Feuerwehrmann-Anwärter', 'FMA', 1),
  (2,  'Feuerwehrmann',          'FM',  2),
  (3,  'Oberfeuerwehrmann',      'OFM', 3),
  (4,  'Hauptfeuerwehrmann',     'HFM', 4),
  (5,  'Löschmeister',           'LM',  5),
  (6,  'Oberlöschmeister',       'OLM', 6),
  (7,  'Hauptlöschmeister',      'HLM', 7),
  (8,  'Brandmeister',           'BM',  8),
  (9,  'Oberbrandmeister',       'OBM', 9),
  (10, 'Hauptbrandmeister',      'HBM', 10),
  (11, 'Brandinspektor',         'BI',  11);

-- -----------------------------------------------------------------------------
-- Ausbildungs-Kategorien
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `course_categories` (`id`, `name`, `description`) VALUES
  (1,  'Führerschein',                       'Führerscheinausbildung'),
  (2,  'Erste Hilfe',                        'Erste-Hilfe-Kurs'),
  (3,  'Truppführer',                        'Truppführer-Lehrgang'),
  (4,  'Gruppenführer',                      'Gruppenführer-Lehrgang'),
  (5,  'Zugführer',                          'Zugführer-Lehrgang'),
  (6,  'Sprechfunker',                       'Sprechfunker-Ausbildung'),
  (7,  'Atemschutzgeräteträger',             'Atemschutz-Ausbildung'),
  (8,  'Absturzsicherung',                   'Absturzsicherungs-Ausbildung'),
  (9,  'Kettensäge',                         'Kettensägen-Ausbildung'),
  (10, 'TM1',                                'Truppmann Lehrgang Teil 1'),
  (11, 'CBRN-Schutz',                        'CBRN-Schutz-Ausbildung'),
  (12, 'Wasserrettung',                      'Wasserrettungs-Ausbildung'),
  (13, 'Maschinisten',                       'Maschinisten-Ausbildung'),
  (14, 'Drehleiter',                         'Drehleiter-Ausbildung'),
  (15, 'Sonstiges',                          'Sonstige Ausbildungen'),
  (16, 'G26.3 Untersuchung',                 'Arbeitsmedizinische Vorsorgeuntersuchung G26.3 für Atemschutzgeräteträger'),
  (17, 'LKW-Führerschein Folgeuntersuchung', 'Ärztliche Untersuchung zur Verlängerung der Fahrerlaubnis Klasse C/CE'),
  (18, 'TM2',                                'Truppmann Lehrgang Teil 2'),
  (19, 'TH 1/2',                             'Technische Hilfe Ausbildung'),
  (20, 'Patientengerechtes Retten',          'Patientengerechtes Retten Ausbildung');

-- -----------------------------------------------------------------------------
-- Feste Lagerorte
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `warehouses` (`id`, `name`) VALUES
  (1, 'Gerätehalle'),
  (2, 'Werkstatt'),
  (3, 'Schulungsraum'),
  (4, 'LBF Büro');

-- -----------------------------------------------------------------------------
-- Abwesenheitsgründe
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `absence_reasons` (`id`, `name`, `color`) VALUES
  (1, 'Undefiniert',    'gray'),
  (2, 'Unentschuldigt', 'red'),
  (3, 'Entschuldigt',   'yellow'),
  (4, 'Urlaub',         'blue'),
  (5, 'Krank',          'orange'),
  (6, 'BSW',            'green');

-- -----------------------------------------------------------------------------
-- Standard-Mitgliedergruppe
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `member_groups` (`id`, `name`, `nextEmployeeNumber`) VALUES
  (1, 'Einsatzabteilung', 1),
  (2, 'Jugendfeuerwehr', 1),
  (3, 'Altersabteilung', 1),
  (4, 'passive Mitglieder', 1);

-- -----------------------------------------------------------------------------
-- Aktuelles Betriebsjahr
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `years` (`year`, `isActive`)
  VALUES (YEAR(NOW()), 1);

-- -----------------------------------------------------------------------------
-- Standard-Einstellungen
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO `settings` (`key`, `value`, `description`) VALUES
  ('fireStationName',        'FFW Heusweiler - LB Wahlschied', 'Name der Feuerwehr'),
  ('fireStationCity',        'Heusweiler',            'Stadt'),
  ('fireStationZip',         '66265',                 'PLZ'),
  ('fireStationStreet',      'Im Dorf 13',            'Straße'),
  ('fireStationPhone',       '',                      'Telefon'),
  ('fireStationEmail',       '',                      'E-Mail'),
  ('inspectionReminderDays', '30',                    'Erinnerung vor Prüfung (Tage)'),
  ('medicalExamReminderDays','60',                    'Erinnerung vor Untersuchung (Tage)');

-- -----------------------------------------------------------------------------
-- Geräteklassen mit Unterklassen und Prüfkriterien
-- -----------------------------------------------------------------------------

-- PSA
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (1, 'PSA', 1);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (1, 1, 'Helme', 1),
  (2, 1, 'Schutzkleidung TH', 2),
  (3, 1, 'Schutzkleidung Brandbekämpfung', 3),
  (4, 1, 'Schutzkleidung sonstige', 4);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (1, 'Zustand Helmschale', 1), (1, 'Innenausstattung', 2), (1, 'Visier/Gesichtsschutz', 3), (1, 'Nackenschutz', 4), (1, 'Kennzeichnung', 5),
  (2, 'Zustand Obermaterial', 1), (2, 'Nähte', 2), (2, 'Verschlüsse', 3), (2, 'Reflexstreifen', 4), (2, 'Kennzeichnung', 5),
  (3, 'Zustand Obermaterial', 1), (3, 'Nähte', 2), (3, 'Verschlüsse', 3), (3, 'Reflexstreifen', 4), (3, 'Feuchtesperre', 5), (3, 'Kennzeichnung', 6),
  (4, 'Zustand Obermaterial', 1), (4, 'Nähte', 2), (4, 'Verschlüsse', 3), (4, 'Kennzeichnung', 4);

-- Erste Hilfe & Hygiene
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (2, 'Erste Hilfe & Hygiene', 2);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (5, 2, 'Sanitäts- & Wiederbelebungsgeräte', 1);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (5, 'Vollständigkeit', 1), (5, 'Zustand Geräte', 2), (5, 'Verfallsdaten', 3), (5, 'Funktionsprüfung', 4), (5, 'Kennzeichnung', 5);

-- Signal- & Beleuchtungsgeräte
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (3, 'Signal- & Beleuchtungsgeräte', 3);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (6, 3, 'Funkgeräte & Melder', 1),
  (7, 3, 'Geräte Verkehrssicherung', 2),
  (8, 3, 'Signal- & Beleuchtungsgeräte', 3);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (6, 'Zustand Gehäuse', 1), (6, 'Akku/Batterie', 2), (6, 'Funktionsprüfung', 3), (6, 'Antenne', 4), (6, 'Kennzeichnung', 5),
  (7, 'Zustand', 1), (7, 'Leuchtmittel', 2), (7, 'Funktionsprüfung', 3), (7, 'Kennzeichnung', 4),
  (8, 'Zustand Gehäuse', 1), (8, 'Leuchtmittel', 2), (8, 'Akku/Batterie', 3), (8, 'Funktionsprüfung', 4), (8, 'Kabel und Stecker', 5), (8, 'Kennzeichnung', 6);

-- Arbeitsgeräte
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (4, 'Arbeitsgeräte', 4);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (9,  4, 'Geräte & Werkzeuge', 1),
  (10, 4, 'Pumpen', 2);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (9, 'Zustand', 1), (9, 'Vollständigkeit', 2), (9, 'Funktionsprüfung', 3), (9, 'Kennzeichnung', 4),
  (10, 'Zustand Gehäuse', 1), (10, 'Dichtungen', 2), (10, 'Funktionsprüfung', 3), (10, 'Ölstand', 4), (10, 'Kraftstoff', 5), (10, 'Kennzeichnung', 6);

-- Löschgeräte
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (5, 'Löschgeräte', 5);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (11, 5, 'Schläuche', 1),
  (12, 5, 'Löschgeräte', 2),
  (13, 5, 'Tragbare Feuerlöscher', 3),
  (14, 5, 'Wasserführende Armaturen', 4);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (11, 'Zustand Schlauch', 1), (11, 'Kupplungen', 2), (11, 'Dichtungen', 3), (11, 'Druckprüfung', 4), (11, 'Kennzeichnung', 5),
  (12, 'Zustand', 1), (12, 'Vollständigkeit', 2), (12, 'Funktionsprüfung', 3), (12, 'Kennzeichnung', 4),
  (13, 'Zustand Behälter', 1), (13, 'Schlauch/Düse', 2), (13, 'Manometer/Druck', 3), (13, 'Plombierung', 4), (13, 'Prüfdatum', 5), (13, 'Kennzeichnung', 6),
  (14, 'Zustand', 1), (14, 'Kupplungen', 2), (14, 'Dichtungen', 3), (14, 'Funktionsprüfung', 4), (14, 'Kennzeichnung', 5);

-- Rettungsgeräte
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (6, 'Rettungsgeräte', 6);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (15, 6, 'Feuerwehrhaltegurte', 1),
  (16, 6, 'Feuerwehrleinen', 2),
  (17, 6, 'Rettungsgeräte', 3),
  (18, 6, 'Spanngurte & Seile', 4),
  (19, 6, 'Tragbare Leitern', 5);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (15, 'Zustand Gurt', 1), (15, 'Karabiner', 2), (15, 'Nähte', 3), (15, 'Kennzeichnung', 4),
  (16, 'Zustand Leine', 1), (16, 'Karabiner', 2), (16, 'Leinenbeutel', 3), (16, 'Kennzeichnung', 4),
  (17, 'Zustand', 1), (17, 'Vollständigkeit', 2), (17, 'Funktionsprüfung', 3), (17, 'Kennzeichnung', 4),
  (18, 'Zustand', 1), (18, 'Verschlüsse/Haken', 2), (18, 'Kennzeichnung', 3),
  (19, 'Zustand Holme', 1), (19, 'Sprossen', 2), (19, 'Gelenke/Verschlüsse', 3), (19, 'Standfüße', 4), (19, 'Kennzeichnung', 5);

-- Elektrische Geräte
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (7, 'Elektrische Geräte', 7);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (20, 7, 'Elektrische Geräte', 1);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (20, 'Zustand Gehäuse', 1), (20, 'Einspannfutter', 2), (20, 'Werkzeug', 3), (20, 'Ersatztrennscheiben', 4), (20, 'Kabel und Stecker', 5), (20, 'elektr. Prüfung', 6);

-- Geräte & Fahrzeuge im GH
INSERT IGNORE INTO `device_classes` (`id`, `name`, `sortOrder`) VALUES (8, 'Geräte & Fahrzeuge im GH', 8);
INSERT IGNORE INTO `device_subclasses` (`id`, `deviceClassId`, `name`, `sortOrder`) VALUES
  (21, 8, 'Geräte & Fahrzeuge', 1);
INSERT IGNORE INTO `inspection_criteria` (`deviceSubclassId`, `name`, `sortOrder`) VALUES
  (21, 'Zustand', 1), (21, 'Vollständigkeit', 2), (21, 'Funktionsprüfung', 3), (21, 'Kennzeichnung', 4);

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- Zusammenfassung der angelegten Tabellen:
--
--  Auth / Benutzer:
--    users               - Systembenutzer (Login)
--    refresh_tokens      - JWT Refresh-Tokens
--    permission_groups   - Berechtigungsgruppen mit 76 BR-Bits
--
--  Personal:
--    members             - Mitglieder (Stammdaten)
--    member_groups       - Mitgliedergruppen (z.B. Aktive, Jugend)
--    member_family       - Angehörige / Notfallkontakte
--    member_work         - Arbeitgeber
--    member_bank         - Bankdaten (IBAN/BIC)
--    member_examinations - G25/G26/G30-Untersuchungen
--    member_availability - Verfügbarkeitsstatus
--    member_history      - Änderungsprotokoll
--
--  Fahrzeuge:
--    vehicles            - Fahrzeuge
--    vehicle_inspections - TÜV/SP/Service-Termine
--    logbook_entries     - Fahrtenbuch
--    equipment_inspections - Geräteprüfungen
--
--  Geräteprüfung:
--    device_classes               - Geräteklassen (PSA, Löschgeräte, etc.)
--    device_subclasses            - Unterklassen (Helme, Schläuche, etc.)
--    inspection_criteria          - Prüfkriterien je Unterklasse
--    inspection_criterion_results - io/nio Einzelergebnisse je Prüfung
--
--  Bestandsliste:
--    warehouses          - Lager (auch fahrzeuggebunden)
--    articles            - Artikel / Ausrüstung (inkl. Geräteklasse, DIN, Seriennr.)
--    article_assignments - Zuweisung Artikel → Lager/Fahrzeug
--    article_inspections - Prüfbuch (Prüfungen je Artikel)
--
--  Einsätze:
--    operations          - Einsätze
--    operation_times     - Fahrzeugzeiten je Einsatz
--    operation_reports   - Einsatzberichte
--    operation_documents - Dokumente (PDF-Uploads)
--
--  Veranstaltungen:
--    events              - Dienstabende, BSW, Sonstiges
--    attendances         - Anwesenheitsliste
--    absences            - Abwesenheiten
--    event_documents     - Dokumente (PDF-Uploads)
--    fire_watches        - Brandsicherheitswachen
--
--  Templates:
--    templates           - Dokument-Vorlagen
--    template_history    - Änderungshistorie
--
--  Ausbildung:
--    course_categories   - Lehrgang-Typen
--    courses             - Lehrgänge je Mitglied
--
--  Stammdaten:
--    ranks               - Dienstgrade
--    absence_reasons     - Abwesenheitsgründe
--    settings            - Systemeinstellungen (Key-Value)
--    years               - Betriebsjahre
-- =============================================================================
