import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Button, Box, TextField, Typography } from '@mui/material';
import axios from 'axios';

export default function GeraeteTable({ token }) {
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [editRow, setEditRow] = useState(null);

  const fetchGeraete = async () => {
    setLoading(true);
    const res = await axios.get('/api/geraete', {
      headers: { Authorization: `Bearer ${token}` },
      params: search ? { q: search } : {},
    });
    setRows(res.data);
    setLoading(false);
  };

  useEffect(() => { fetchGeraete(); }, [search]);

  const handleEdit = (row) => setEditRow(row);
  const handleDelete = async (id) => {
    await axios.delete(`/api/geraete/${id}`, { headers: { Authorization: `Bearer ${token}` } });
    fetchGeraete();
  };
  const handleSave = async () => {
    if (editRow.id) {
      await axios.put(`/api/geraete/${editRow.id}`, editRow, { headers: { Authorization: `Bearer ${token}` } });
    } else {
      await axios.post('/api/geraete', editRow, { headers: { Authorization: `Bearer ${token}` } });
    }
    setEditRow(null);
    fetchGeraete();
  };

  const columns = [
    { field: 'bezeichnung', headerName: 'Bezeichnung', width: 140, editable: true },
    { field: 'typ', headerName: 'Typ', width: 120 },
    { field: 'anschaffungsdatum', headerName: 'Anschaffungsdatum', width: 140 },
    { field: 'pruefdatum', headerName: 'Prüfdatum', width: 120 },
    { field: 'status', headerName: 'Status', width: 120 },
    {
      field: 'actions', headerName: 'Aktionen', width: 180, renderCell: (params) => (
        <>
          <Button size="small" onClick={() => handleEdit(params.row)}>Bearbeiten</Button>
          <Button size="small" color="error" onClick={() => handleDelete(params.row.id)}>Löschen</Button>
        </>
      )
    }
  ];

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h6">Geräte</Typography>
      <TextField label="Suche" value={search} onChange={e => setSearch(e.target.value)} sx={{ mb: 2 }} />
      <Button variant="contained" onClick={() => setEditRow({})}>Neu</Button>
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} loading={loading} getRowId={r => r.id} />
      </div>
      {editRow && (
        <Box sx={{ mt: 2 }}>
          <Typography>{editRow.id ? 'Bearbeiten' : 'Neu anlegen'}</Typography>
          <TextField label="Bezeichnung" value={editRow.bezeichnung || ''} onChange={e => setEditRow({ ...editRow, bezeichnung: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Typ" value={editRow.typ || ''} onChange={e => setEditRow({ ...editRow, typ: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Anschaffungsdatum" value={editRow.anschaffungsdatum || ''} onChange={e => setEditRow({ ...editRow, anschaffungsdatum: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Prüfdatum" value={editRow.pruefdatum || ''} onChange={e => setEditRow({ ...editRow, pruefdatum: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Status" value={editRow.status || ''} onChange={e => setEditRow({ ...editRow, status: e.target.value })} sx={{ mr: 1 }} />
          <Button variant="contained" onClick={handleSave} sx={{ mr: 1 }}>Speichern</Button>
          <Button onClick={() => setEditRow(null)}>Abbrechen</Button>
        </Box>
      )}
    </Box>
  );
}
