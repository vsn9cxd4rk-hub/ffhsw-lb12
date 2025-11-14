const express = require('express');
const Mitglied = require('../models/Mitglied');
const auth = require('../middleware/auth');
const router = express.Router();

// Alle Mitglieder (mit Suche/Filter)
router.get('/', auth, async (req, res) => {
  try {
    const { q } = req.query;
    const where = q ? {
      [require('sequelize').Op.or]: [
        { vorname: { [require('sequelize').Op.like]: `%${q}%` } },
        { nachname: { [require('sequelize').Op.like]: `%${q}%` } },
        { rang: { [require('sequelize').Op.like]: `%${q}%` } }
      ]
    } : {};
    const mitglieder = await Mitglied.findAll({ where });
    res.json(mitglieder);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

// Neues Mitglied
router.post('/', auth, async (req, res) => {
  try {
    const mitglied = await Mitglied.create(req.body);
    res.json(mitglied);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

// Mitglied bearbeiten
router.put('/:id', auth, async (req, res) => {
  try {
    await Mitglied.update(req.body, { where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

// Mitglied löschen
router.delete('/:id', auth, async (req, res) => {
  try {
    await Mitglied.destroy({ where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
