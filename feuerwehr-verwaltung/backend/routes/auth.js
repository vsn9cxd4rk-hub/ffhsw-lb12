const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const config = require('../config');
const router = express.Router();

router.post('/register', async (req, res) => {
  const { username, password } = req.body;
  if (!username || !password) return res.status(400).json({ error: 'Benutzername und Passwort erforderlich.' });
  const hash = await bcrypt.hash(password, 10);
  try {
    const user = await User.create({ username, password: hash });
    res.json({ id: user.id, username: user.username });
  } catch (e) {
    res.status(400).json({ error: 'Benutzername bereits vergeben.' });
  }
});

router.post('/login', async (req, res) => {
  const { username, password } = req.body;
  const user = await User.findOne({ where: { username } });
  if (!user) return res.status(401).json({ error: 'Ungültige Zugangsdaten.' });
  const valid = await bcrypt.compare(password, user.password);
  if (!valid) return res.status(401).json({ error: 'Ungültige Zugangsdaten.' });
  const token = jwt.sign({ id: user.id, role: user.role }, config.jwtSecret, { expiresIn: '8h' });
  res.json({ token });
});

module.exports = router;
