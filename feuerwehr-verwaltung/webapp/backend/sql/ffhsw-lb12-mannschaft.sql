CREATE TABLE Mannschaft (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Löschbezirk VARCHAR(255),
    Name VARCHAR(255),
    Vorname VARCHAR(255),
    Geb_Datum DATE,
    Anschrift VARCHAR(255),
    Straße VARCHAR(255),
    Telefon VARCHAR(255),
    Mail VARCHAR(255),
    Eintritt_JW DATE,
    Eintr_aktiv DATE,
    Austritt DATE,
    DG VARCHAR(255),
    Altersabteilung INT,
    Dienstjahre INT,
    Aktiv_Ja_Nein INT
);

CREATE TABLE Lehrgänge (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Mannschaft_ID INT,
    TM1 DATE,
    TM2 DATE,
    TF DATE,
    Funk DATE,
    Maschinist DATE,
    Türöffnung_Fräsen DATE,
    Türöffnung DATE,
    AGT DATE,
    GF1 DATE,
    GF2 DATE,
    ZF1 DATE,
    ZF2 DATE,
    TH_I_2 DATE,
    PGR DATE,
    GW DATE,
    SAN_A DATE,
    SAN_B DATE,
    Erste_Hilfe DATE,
    VT_Brandbekämpfung DATE,
    BE_Didaktik_Aufbau DATE,
    FOREIGN KEY (Mannschaft_ID) REFERENCES Mannschaft(ID)
);

CREATE TABLE Beförderungen (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Mannschaft_ID INT,
    Letzte_Beförderung DATE,
    Geplante_Beförderung DATE,
    Jübiläum DATE,
    FOREIGN KEY (Mannschaft_ID) REFERENCES Mannschaft(ID)
);

CREATE TABLE Atemschutz (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Mannschaft_ID INT,
    AGT DATE,
    Letzte_G26 DATE,
    Nächste_G26 DATE,
    Belastungsübung DATE,
    Atemschutz_Ja_Nein INT,
    Bemerkungen VARCHAR(255),
    FOREIGN KEY (Mannschaft_ID) REFERENCES Mannschaft(ID)
);
