const express = require('express');
const cors = require('cors');
const app = express();
const PORT = process.env.PORT || 9300;

app.use(cors());
app.use(express.json());

// ...existing code...
// Placeholder for routes (auth, mitglieder, geraete)

const sequelize = require('./db');
app.use('/api/auth', require('./routes/auth'));
app.use('/api/mitglieder', require('./routes/mitglieder'));
app.use('/api/geraeteTabs', require('./routes/geraeteTabs'));
app.use('/api/users', require('./routes/users'));
app.use('/api/lagerort', require('./routes/lagerort'));
app.use('/api/fahrzeug_geraetehaus', require('./routes/fahrzeug_geraetehaus'));
app.use('/api/eigentuemer', require('./routes/eigentuemer'));
app.use('/api/feuerwehr_heusweiler', require('./routes/feuerwehr_heusweiler'));

app.get('/', (req, res) => res.send('Feuerwehr Verwaltung Backend läuft.'));

sequelize.sync().then(() => {
	//app.listen(PORT, () => console.log(`Backend läuft auf Port ${PORT}`));
	app.listen(PORT, 'localhost', () => console.log(`Backend läuft auf Port ${PORT}`));
});

app.listen(PORT, () => console.log(`Backend läuft auf Port ${PORT}`));
