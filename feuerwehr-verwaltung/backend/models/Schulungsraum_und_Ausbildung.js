const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Schulungsraum_und_Ausbildung = sequelize.define('Schulungsraum_und_Ausbildung', {
  ID: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
  lagerort: DataTypes.STRING,
  fahrzeug_geraetehaus: DataTypes.STRING,
  Inventarnummer: DataTypes.STRING,
  Artikelbezeichnung: DataTypes.STRING,
  Typ: DataTypes.STRING,
  Spezifikation: DataTypes.STRING,
  Seriennummer: DataTypes.STRING,
  Hersteller: DataTypes.STRING,
  Anzahl: DataTypes.INTEGER,
  Einheit: DataTypes.STRING,
  Anschaffungsjahr: DataTypes.INTEGER,
  Kosten: DataTypes.DECIMAL(10,2),
  Bemerkung: DataTypes.TEXT,
  eigentuemer: DataTypes.STRING,
  feuerwehr_heusweiler: DataTypes.STRING,
  Pruefung: DataTypes.BOOLEAN,
  Norm: DataTypes.STRING
}, {
  timestamps: false,
  tableName: 'Schulungsraum_und_Ausbildung'
});

module.exports = Schulungsraum_und_Ausbildung;
