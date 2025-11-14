const { DataTypes } = require('sequelize');
const sequelize = require('../db');

const Feuerwehr_Heusweiler = sequelize.define('Feuerwehr_Heusweiler', {
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  name: { type: DataTypes.STRING, allowNull: false }
}, { timestamps: false, tableName: 'Feuerwehr_Heusweiler' });

module.exports = Feuerwehr_Heusweiler;
