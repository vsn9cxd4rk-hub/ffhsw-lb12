const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Eigentuemer = sequelize.define('Eigentuemer', {
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  name: { type: DataTypes.STRING, allowNull: false }
}, { timestamps: false, tableName: 'Eigentuemer' });

module.exports = Eigentuemer;
