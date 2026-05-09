const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Lagerort = sequelize.define('Lagerort', {
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  name: { type: DataTypes.STRING, allowNull: false }
}, { 
  timestamps: false,
  tableName: 'lagerort'
});

module.exports = Lagerort;
