const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Geraet = sequelize.define('Geraet', {
  // Felder gemäß ffhsw-lb12-geraete.sql
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  bezeichnung: { type: DataTypes.STRING, allowNull: false },
  typ: { type: DataTypes.STRING },
  anschaffungsdatum: { type: DataTypes.DATE },
  pruefdatum: { type: DataTypes.DATE },
  status: { type: DataTypes.STRING },
}, {
  timestamps: false,
});

module.exports = Geraet;
