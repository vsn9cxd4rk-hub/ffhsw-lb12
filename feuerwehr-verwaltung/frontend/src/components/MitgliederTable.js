import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Button, Box, TextField, Typography } from '@mui/material';
import axios from 'axios';

export default function MitgliederTable({ token }) {
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [editRow, setEditRow] = useState(null);

  const fetchMitglieder = async () => {
    setLoading(true);
    const res = await axios.get('/api/mitglieder', {
      headers: { Authorization: `Bearer ${token}` },
      params: search ? { q: search } : {},
    });
    setRows(res.data);
    setLoading(false);
  };

  useEffect(() => { fetchMitglieder(); }, [search]);

  const handleEdit = (row) => setEditRow(row);
  const handleDelete = async (id) => {
    await axios.delete(`/api/mitglieder/${id}`, { headers: { Authorization: `Bearer ${token}` } });
    fetchMitglieder();
  };

  // Helper: check if string is valid German date (DD.MM.YYYY)
  function isValidGermanDate(str) {
    if (!str) return true;
    return /^\d{2}\.\d{2}\.\d{4}$/.test(str) && !isNaN(Date.parse(toIsoDate(str)));
  }

  // Convert DD.MM.YYYY to YYYY-MM-DD (for backend)
  function toIsoDate(str) {
    if (!str) return null;
    const m = str.match(/^(\d{2})\.(\d{2})\.(\d{4})$/);
    if (!m) return null;
    return `${m[3]}-${m[2]}-${m[1]}`;
  }

  // Convert YYYY-MM-DD to DD.MM.YYYY (for display)
  function toGermanDate(str) {
    if (!str) return '';
    // Accept ISO string with time (e.g. 1969-09-30T00:00:00.000Z)
    const m = str.match(/^(\d{4})-(\d{2})-(\d{2})/);
    if (!m) return str;
    return `${m[3]}.${m[2]}.${m[1]}`;
  }

  const handleSave = async () => {
    // Validate required fields
    if (!editRow.vorname || !editRow.nachname) {
      alert('Vorname und Nachname sind Pflichtfelder!');
      return;
    }
    // Validate date fields (German format)
    let geburtsdatum = editRow.geburtsdatum;
    let eintrittsdatum = editRow.eintrittsdatum;
    // Check format first
    if (geburtsdatum && !/^\d{2}\.\d{2}\.\d{4}$/.test(geburtsdatum)) {
      alert('Geburtsdatum muss im Format TT.MM.JJJJ eingegeben werden!');
      return;
    }
    if (eintrittsdatum && !/^\d{2}\.\d{2}\.\d{4}$/.test(eintrittsdatum)) {
      alert('Eintrittsdatum muss im Format TT.MM.JJJJ eingegeben werden!');
      return;
    }
    // Check if valid date
    if (geburtsdatum && isValidGermanDate(geburtsdatum) && isNaN(Date.parse(toIsoDate(geburtsdatum)))) {
      alert('Geburtsdatum ist kein gültiges Datum!');
      return;
    }
    if (eintrittsdatum && isValidGermanDate(eintrittsdatum) && isNaN(Date.parse(toIsoDate(eintrittsdatum)))) {
      alert('Eintrittsdatum ist kein gültiges Datum!');
      return;
    }
    // Convert to ISO for backend, or null
    geburtsdatum = geburtsdatum && isValidGermanDate(geburtsdatum) ? toIsoDate(geburtsdatum) : null;
    eintrittsdatum = eintrittsdatum && isValidGermanDate(eintrittsdatum) ? toIsoDate(eintrittsdatum) : null;
    const payload = { ...editRow, geburtsdatum, eintrittsdatum };
    if (editRow.id) {
      await axios.put(`/api/mitglieder/${editRow.id}`, payload, { headers: { Authorization: `Bearer ${token}` } });
    } else {
      await axios.post('/api/mitglieder', payload, { headers: { Authorization: `Bearer ${token}` } });
    }
    setEditRow(null);
    fetchMitglieder();
  };

  const columns = [
    { field: 'vorname', headerName: 'Vorname', width: 120, editable: true },
    { field: 'nachname', headerName: 'Nachname', width: 120, editable: true },
  { field: 'geburtsdatum', headerName: 'Geburtsdatum', width: 120, valueGetter: (params) => {
    const val = params.row && params.row.geburtsdatum;
    if (!val) return '';
    if (typeof val === 'string') return toGermanDate(val);
    if (val instanceof Date) return toGermanDate(val.toISOString());
    return '';
  } },
    { field: 'rang', headerName: 'Rang', width: 120 },
  { field: 'eintrittsdatum', headerName: 'Eintrittsdatum', width: 120, valueGetter: (params) => {
    const val = params.row && params.row.eintrittsdatum;
    if (!val) return '';
    if (typeof val === 'string') return toGermanDate(val);
    if (val instanceof Date) return toGermanDate(val.toISOString());
    return '';
  } },
    { field: 'aktiv', headerName: 'Aktiv', width: 80, type: 'boolean' },
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
      <Typography variant="h6">Mitglieder</Typography>
      <TextField label="Suche" value={search} onChange={e => setSearch(e.target.value)} sx={{ mb: 2 }} />
      <Button variant="contained" onClick={() => setEditRow({})}>Neu</Button>
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} loading={loading} getRowId={r => r.id} />
      </div>
      {editRow && (
        <Box sx={{ mt: 2 }}>
          <Typography>{editRow.id ? 'Bearbeiten' : 'Neu anlegen'}</Typography>
          <TextField label="Vorname" value={editRow.vorname || ''} onChange={e => setEditRow({ ...editRow, vorname: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Nachname" value={editRow.nachname || ''} onChange={e => setEditRow({ ...editRow, nachname: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Geburtsdatum" placeholder="TT.MM.JJJJ" value={editRow.geburtsdatum ? (/^\d{4}-\d{2}-\d{2}/.test(editRow.geburtsdatum) ? toGermanDate(editRow.geburtsdatum) : editRow.geburtsdatum) : ''} onChange={e => setEditRow({ ...editRow, geburtsdatum: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Rang" value={editRow.rang || ''} onChange={e => setEditRow({ ...editRow, rang: e.target.value })} sx={{ mr: 1 }} />
          <TextField label="Eintrittsdatum" placeholder="TT.MM.JJJJ" value={editRow.eintrittsdatum ? (/^\d{4}-\d{2}-\d{2}/.test(editRow.eintrittsdatum) ? toGermanDate(editRow.eintrittsdatum) : editRow.eintrittsdatum) : ''} onChange={e => setEditRow({ ...editRow, eintrittsdatum: e.target.value })} sx={{ mr: 1 }} />
          <Button variant="contained" onClick={handleSave} sx={{ mr: 1 }}>Speichern</Button>
          <Button onClick={() => setEditRow(null)}>Abbrechen</Button>
        </Box>
      )}
    </Box>
  );
}
