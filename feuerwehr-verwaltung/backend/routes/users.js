const express = require('express');
const bcrypt = require('bcryptjs');
const User = require('../models/User');
const auth = require('../middleware/auth');
const router = express.Router();

// Only admin middleware
function onlyAdmin(req, res, next) {
  if (req.user && req.user.role === 'admin') return next();
  return res.status(403).json({ error: 'Nur für Admins erlaubt.' });
}

// Alle User anzeigen (nur für Admins)
router.get('/', auth, onlyAdmin, async (req, res) => {
  const users = await User.findAll({ attributes: { exclude: ['password'] } });
  res.json(users);
});

// Neuen User anlegen (nur Admin)
router.post('/', auth, onlyAdmin, async (req, res) => {
  const { username, password, role } = req.body;
  if (!username || !password || !role) return res.status(400).json({ error: 'Alle Felder erforderlich.' });
  const hash = await bcrypt.hash(password, 10);
  try {
    const user = await User.create({ username, password: hash, role });
    res.json({ id: user.id, username: user.username, role: user.role });
  } catch (e) {
    res.status(400).json({ error: 'Benutzername bereits vergeben.' });
  }
});

// User bearbeiten (nur Admin)
router.put('/:id', auth, onlyAdmin, async (req, res) => {
  const { username, password, role } = req.body;
  const update = { username, role };
  if (password) update.password = await bcrypt.hash(password, 10);
  await User.update(update, { where: { id: req.params.id } });
  res.json({ success: true });
});

// User löschen (nur Admin)
router.delete('/:id', auth, onlyAdmin, async (req, res) => {
  await User.destroy({ where: { id: req.params.id } });
  res.json({ success: true });
});

module.exports = router;
