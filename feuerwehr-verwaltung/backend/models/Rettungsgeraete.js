const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Rettungsgeraete = sequelize.define('Rettungsgeraete', {
  ID: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
  Lagerort: DataTypes.STRING,
  Fahrzeug_Geraetehaus: DataTypes.STRING,
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
  Pruefung: DataTypes.BOOLEAN,
  Norm: DataTypes.STRING
}, {
  timestamps: false,
  tableName: 'Rettungsgeraete'
});

module.exports = Rettungsgeraete;
