const express = require('express');
const Fahrzeug_Geraetehaus = require('../models/Fahrzeug_Geraetehaus');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, async (req, res) => {
  try {
    const data = await Fahrzeug_Geraetehaus.findAll();
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.post('/', auth, async (req, res) => {
  try {
    const entry = await Fahrzeug_Geraetehaus.create(req.body);
    res.json(entry);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.put('/:id', auth, async (req, res) => {
  try {
    await Fahrzeug_Geraetehaus.update(req.body, { where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.delete('/:id', auth, async (req, res) => {
  try {
    await Fahrzeug_Geraetehaus.destroy({ where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
