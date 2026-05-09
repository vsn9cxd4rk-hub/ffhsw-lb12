import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Button, Box, TextField, Typography } from '@mui/material';
import axios from 'axios';

export default function UserTable({ token, userRole }) {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);
  const [editRow, setEditRow] = useState(null);
  const [error, setError] = useState('');

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/users', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRows(res.data);
    } catch (e) {
      setError('Fehler beim Laden der Benutzer');
    }
    setLoading(false);
  };

  useEffect(() => { fetchUsers(); }, []);

  const handleEdit = (row) => setEditRow(row);
  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/users/${id}`, { headers: { Authorization: `Bearer ${token}` } });
      fetchUsers();
    } catch (e) {
      setError('Löschen fehlgeschlagen');
    }
  };
  const handleSave = async () => {
    if (!editRow.username || !editRow.role) {
      setError('Benutzername und Rolle sind Pflichtfelder!');
      return;
    }
    try {
      if (editRow.id) {
        await axios.put(`/api/users/${editRow.id}`, editRow, { headers: { Authorization: `Bearer ${token}` } });
      } else {
        await axios.post('/api/users', editRow, { headers: { Authorization: `Bearer ${token}` } });
      }
      setEditRow(null);
      fetchUsers();
    } catch (e) {
      setError('Speichern fehlgeschlagen');
    }
  };

  const columns = [
    { field: 'username', headerName: 'Benutzername', width: 180 },
    { field: 'role', headerName: 'Rolle', width: 120 },
    {
      field: 'actions', headerName: 'Aktionen', width: 180, renderCell: (params) => (
        userRole === 'admin' ? <>
          <Button size="small" onClick={() => handleEdit(params.row)}>Bearbeiten</Button>
          <Button size="small" color="error" onClick={() => handleDelete(params.row.id)}>Löschen</Button>
        </> : null
      )
    }
  ];

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h6">Benutzerverwaltung</Typography>
      {error && <Typography color="error">{error}</Typography>}
      {userRole === 'admin' && <Button variant="contained" onClick={() => setEditRow({})}>Neu</Button>}
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} loading={loading} getRowId={r => r.id} />
      </div>
      {editRow && (
        <Box sx={{ mt: 2 }}>
          <Typography>{editRow.id ? 'Bearbeiten' : 'Neu anlegen'}</Typography>
          <TextField label="Benutzername" value={editRow.username || ''} onChange={e => setEditRow({ ...editRow, username: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Rolle" value={editRow.role || ''} onChange={e => setEditRow({ ...editRow, role: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Passwort" type="password" value={editRow.password || ''} onChange={e => setEditRow({ ...editRow, password: e.target.value })} sx={{ mr: 1 }} />
          <Button variant="contained" onClick={handleSave} sx={{ mr: 1 }}>Speichern</Button>
          <Button onClick={() => setEditRow(null)}>Abbrechen</Button>
        </Box>
      )}
    </Box>
  );
}
