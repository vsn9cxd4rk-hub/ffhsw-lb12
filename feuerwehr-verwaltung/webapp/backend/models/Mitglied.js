const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Mitglied = sequelize.define('Mitglied', {
  // Felder gemäß ffhsw-lb12-mannschaft.sql
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  vorname: { type: DataTypes.STRING, allowNull: false },
  nachname: { type: DataTypes.STRING, allowNull: false },
  geburtsdatum: { type: DataTypes.DATE },
  rang: { type: DataTypes.STRING },
  eintrittsdatum: { type: DataTypes.DATE },
  aktiv: { type: DataTypes.BOOLEAN, defaultValue: true },
}, {
  timestamps: false,
  tableName: 'mitglieder',
});

module.exports = Mitglied;
