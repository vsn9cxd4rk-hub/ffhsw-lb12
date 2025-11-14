const jwt = require('jsonwebtoken');
const config = require('../config');

function auth(req, res, next) {
  const token = req.headers['authorization']?.split(' ')[1];
  if (!token) return res.status(401).json({ error: 'Token fehlt.' });
  try {
    req.user = jwt.verify(token, config.jwtSecret);
    next();
  } catch {
    res.status(401).json({ error: 'Ungültiger Token.' });
  }
}

module.exports = auth;
