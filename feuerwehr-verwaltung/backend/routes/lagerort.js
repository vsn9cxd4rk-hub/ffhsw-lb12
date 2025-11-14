const express = require('express');
const Lagerort = require('../models/Lagerort');
const auth = require('../middleware/auth');
const router = express.Router();

// Alle Lagerorte
router.get('/', auth, async (req, res) => {
  try {
    const data = await Lagerort.findAll();
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
// Neuen Lagerort anlegen
router.post('/', auth, async (req, res) => {
  try {
    const entry = await Lagerort.create(req.body);
    res.json(entry);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
// Lagerort bearbeiten
router.put('/:id', auth, async (req, res) => {
  try {
    await Lagerort.update(req.body, { where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
// Lagerort löschen
router.delete('/:id', auth, async (req, res) => {
  try {
    await Lagerort.destroy({ where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
