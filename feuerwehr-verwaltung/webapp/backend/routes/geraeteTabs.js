const express = require('express');
const auth = require('../middleware/auth');
const router = express.Router();

const models = {
  'Fahrzeuge_und_Fahrzeugladegeraete': require('../models/Fahrzeuge_und_Fahrzeugladegeraete'),
  'Schutzkleidung': require('../models/Schutzkleidung'),
  'Erste_Hilfe_und_Hygiene': require('../models/Erste_Hilfe_und_Hygiene'),
  'Signal_und_Beleuchtungsgeraete': require('../models/Signal_und_Beleuchtungsgeraete'),
  'Arbeitsgeraete': require('../models/Arbeitsgeraete'),
  'Loeschgeraete': require('../models/Loeschgeraete'),
  'Rettungsgeraete': require('../models/Rettungsgeraete'),
  'Elektrische_Geraete': require('../models/Elektrische_Geraete'),
  'Fahrzeughalle_und_Werkstatt': require('../models/Fahrzeughalle_und_Werkstatt'),
  'Schulungsraum_und_Ausbildung': require('../models/Schulungsraum_und_Ausbildung'),
  'Kueche_Fahrzeughalle': require('../models/Kueche_Fahrzeughalle'),
  'Buero_LBZ_Fuehrung': require('../models/Buero_LBZ_Fuehrung'),
  'EDV': require('../models/EDV')
};

Object.entries(models).forEach(([table, Model]) => {
  // GET mit Suche/Filter
  router.get(`/${table}`, auth, async (req, res) => {
    const { q } = req.query;
    const where = q ? {
      Artikelbezeichnung: { [require('sequelize').Op.like]: `%${q}%` }
    } : {};
    const result = await Model.findAll({ where });
    res.json(result);
  });

  // POST (neu)
  router.post(`/${table}`, auth, async (req, res) => {
    const entry = await Model.create(req.body);
    res.json(entry);
  });

  // PUT (bearbeiten)
  router.put(`/${table}/:id`, auth, async (req, res) => {
    await Model.update(req.body, { where: { ID: req.params.id } });
    res.json({ success: true });
  });

  // DELETE
  router.delete(`/${table}/:id`, auth, async (req, res) => {
    await Model.destroy({ where: { ID: req.params.id } });
    res.json({ success: true });
  });
});

module.exports = router;
