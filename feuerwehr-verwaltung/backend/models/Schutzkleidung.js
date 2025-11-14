const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Schutzkleidung = sequelize.define('Schutzkleidung', {
  ID: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
  lagerort_id: { type: DataTypes.INTEGER, references: { model: 'Lagerort', key: 'id' } },
  fahrzeug_geraetehaus_id: { type: DataTypes.INTEGER, references: { model: 'Fahrzeug_Geraetehaus', key: 'id' } },
  Inventarnummer: DataTypes.STRING,
  Artikelbezeichnung: DataTypes.STRING,
  Typ: DataTypes.STRING,
  Spezifikation: DataTypes.STRING,
  Feuerwehrangehoeriger: DataTypes.STRING,
  Seriennummer: DataTypes.STRING,
  Hersteller: DataTypes.STRING,
  Anzahl: DataTypes.INTEGER,
  Einheit: DataTypes.STRING,
  Anschaffungsjahr: DataTypes.INTEGER,
  Kosten: DataTypes.DECIMAL(10,2),
  Bemerkung: DataTypes.TEXT,
  eigentuemer_id: { type: DataTypes.INTEGER, references: { model: 'Eigentuemer', key: 'id' } },
    feuerwehr_heusweiler_id: { type: DataTypes.INTEGER, references: { model: 'Feuerwehr_Heusweiler', key: 'id' } },
  Pruefung: DataTypes.BOOLEAN,
  Norm: DataTypes.STRING
}, {
  timestamps: false,
  tableName: 'Schutzkleidung'
});

const Lagerort = require('./Lagerort');
const Fahrzeug_Geraetehaus = require('./Fahrzeug_Geraetehaus');
const Eigentuemer = require('./Eigentuemer');
const Feuerwehr_Heusweiler = require('./Feuerwehr_Heusweiler');
Schutzkleidung.belongsTo(Lagerort, { foreignKey: 'lagerort_id' });
Schutzkleidung.belongsTo(Fahrzeug_Geraetehaus, { foreignKey: 'fahrzeug_geraetehaus_id' });
Schutzkleidung.belongsTo(Eigentuemer, { foreignKey: 'eigentuemer_id' });
Schutzkleidung.belongsTo(Feuerwehr_Heusweiler, { foreignKey: 'feuerwehr_heusweiler_id' });
module.exports = Schutzkleidung;
