const express = require('express');
const Feuerwehr_Heusweiler = require('../models/Feuerwehr_Heusweiler');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, async (req, res) => {
  try {
    const data = await Feuerwehr_Heusweiler.findAll();
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.post('/', auth, async (req, res) => {
  try {
    const entry = await Feuerwehr_Heusweiler.create(req.body);
    res.json(entry);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.put('/:id', auth, async (req, res) => {
  try {
    await Feuerwehr_Heusweiler.update(req.body, { where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});
router.delete('/:id', auth, async (req, res) => {
  try {
    await Feuerwehr_Heusweiler.destroy({ where: { id: req.params.id } });
    res.json({ success: true });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
