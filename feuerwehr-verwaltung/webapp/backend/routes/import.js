const express = require('express');
const multer = require('multer');
const csv = require('csv-parser');
const fs = require('fs');
const path = require('path');
const router = express.Router();

// Set up multer for file uploads
const upload = multer({ dest: 'data/' });

// POST /api/import
router.post('/', upload.single('file'), async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({ error: 'No file uploaded.' });
    }
    const results = [];
    const filePath = path.join(__dirname, '..', req.file.path);
    const table = req.body.table;
    if (!table) {
      fs.unlinkSync(filePath);
      return res.status(400).json({ error: 'Keine Zieltabelle angegeben.' });
    }
    // Sequelize models mapping
    const models = {
      mitglieder: require('../models/Mitglied'),
      arbeitsgeraete: require('../models/Arbeitsgeraete'),
      loeschgeraete: require('../models/Loeschgeraete'),
      rettungsgeraete: require('../models/Rettungsgeraete'),
      schutzkleidung: require('../models/Schutzkleidung'),
      fahrzeuge_und_fahrzeugladegeraete: require('../models/Fahrzeuge_und_Fahrzeugladegeraete'),
      edv: require('../models/EDV'),
      signal_und_beleuchtungsgeraete: require('../models/Signal_und_Beleuchtungsgeraete'),
      elektrische_geraete: require('../models/Elektrische_Geraete'),
      erste_hilfe_und_hygiene: require('../models/Erste_Hilfe_und_Hygiene'),
      fahrzeughalle_und_werkstatt: require('../models/Fahrzeughalle_und_Werkstatt'),
      kueche_fahrzeughalle: require('../models/Kueche_Fahrzeughalle'),
      schulungsraum_und_ausbildung: require('../models/Schulungsraum_und_Ausbildung'),
      buero_lbz_fuehrung: require('../models/Buero_LBZ_Fuehrung'),
    };
    const Model = models[table];
    if (!Model) {
      fs.unlinkSync(filePath);
      return res.status(400).json({ error: 'Unbekannte Zieltabelle.' });
    }
    fs.createReadStream(filePath)
      .pipe(csv({ separator: ';' }))
      .on('data', (data) => results.push(data))
      .on('end', async () => {
        try {
          await Model.bulkCreate(results);
          res.json({ message: 'CSV import erfolgreich.', rows: results.length });
        } catch (err) {
          res.status(500).json({ error: 'Fehler beim Import: ' + err.message });
        }
        fs.unlinkSync(filePath);
      });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
