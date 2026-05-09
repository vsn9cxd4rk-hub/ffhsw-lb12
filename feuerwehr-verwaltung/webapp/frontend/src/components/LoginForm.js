import React, { useState } from 'react';
import { TextField, Button, Box, Typography } from '@mui/material';
import axios from 'axios';

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('/api/auth/login', { username, password });
      onLogin(res.data.token);
    } catch (err) {
      setError('Login fehlgeschlagen');
    }
  };

  return (
    <Box sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
      <Typography variant="h5" align="center">Login</Typography>
      <form onSubmit={handleSubmit}>
        <TextField label="Benutzername" fullWidth margin="normal" value={username} onChange={e => setUsername(e.target.value)} />
        <TextField label="Passwort" type="password" fullWidth margin="normal" value={password} onChange={e => setPassword(e.target.value)} />
        {error && <Typography color="error">{error}</Typography>}
        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, backgroundColor: '#c62828', color: '#fff', '&:hover': { backgroundColor: '#b71c1c' } }}>Login</Button>
      </form>
    </Box>
  );
}
