# Feuerwehr Verwaltung

Moderne Webanwendung zur Verwaltung von Feuerwehr-Mitgliedern und Geräten.

## Features
- Sichere User-Verwaltung (JWT, Rollen)
- Mitgliederverwaltung (CRUD, Suche, Filter)
- Geräte-Inventarisierung & Prüfung (CRUD, Suche, Filter)
- Responsive UI mit konfigurierbaren Styles (Dark/Light Mode, Theme-Editor)
- MySQL-Datenbank

## Technologie-Stack
- Frontend: React, Material UI
- Backend: Node.js, Express, Sequelize
- Auth: JWT
- DB: MySQL

## Setup
1. Backend: `cd backend && npm install && npm start`
2. Frontend: `cd frontend && npm install && npm start`

## Datenbank
Tabellenstruktur gemäß ffhsw-lb12-mannschaft.sql und ffhsw-lb12-geraete.sql.

## Hinweise
- Styles und Themes sind konfigurierbar.
- Tabellen sind editierbar, such- und filterbar.
