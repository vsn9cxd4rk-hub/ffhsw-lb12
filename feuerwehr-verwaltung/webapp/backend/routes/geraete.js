const express = require('express');
const Geraet = require('../models/Geraet');
const auth = require('../middleware/auth');
const router = express.Router();

// Alle Geräte (mit Suche/Filter)
router.get('/', auth, async (req, res) => {
  const { q } = req.query;
  const where = q ? {
    [require('sequelize').Op.or]: [
      { bezeichnung: { [require('sequelize').Op.like]: `%${q}%` } },
      { typ: { [require('sequelize').Op.like]: `%${q}%` } },
      { status: { [require('sequelize').Op.like]: `%${q}%` } }
    ]
  } : {};
  const geraete = await Geraet.findAll({ where });
  res.json(geraete);
});

// Neues Gerät
router.post('/', auth, async (req, res) => {
  const geraet = await Geraet.create(req.body);
  res.json(geraet);
});

// Gerät bearbeiten
router.put('/:id', auth, async (req, res) => {
  await Geraet.update(req.body, { where: { id: req.params.id } });
  res.json({ success: true });
});

// Gerät löschen
router.delete('/:id', auth, async (req, res) => {
  await Geraet.destroy({ where: { id: req.params.id } });
  res.json({ success: true });
});

module.exports = router;
