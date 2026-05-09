const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Fahrzeug_Geraetehaus = sequelize.define('Fahrzeug_Geraetehaus', {
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  name: { type: DataTypes.STRING, allowNull: false }
}, { timestamps: false });

module.exports = Fahrzeug_Geraetehaus;
