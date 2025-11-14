const express = require('express');
const Eigentuemer = require('../models/Eigentuemer');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, async (req, res) => {
  try {
    const data = await Eigentuemer.findAll();
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.post('/', auth, async (req, res) => {
  try {
    const entry = await Eigentuemer.create(req.body);
    res.json(entry);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.put('/:id', auth, async (req, res) => {
  try {
    await Eigentuemer.update(req.body, { where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.delete('/:id', auth, async (req, res) => {
  try {
    await Eigentuemer.destroy({ where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
