import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Button, Box, TextField, Typography, MenuItem } from '@mui/material';
import axios from 'axios';

// Dropdown config for all *_id FKs and legacy string FKs
const dropdownConfig = {
  'lagerort_id': { endpoint: '/api/lagerort', labelField: 'name' },
  'fahrzeug_geraetehaus_id': { endpoint: '/api/fahrzeug_geraetehaus', labelField: 'name' },
  'eigentuemer_id': { endpoint: '/api/eigentuemer', labelField: 'name' },
  'feuerwehr_heusweiler_id': { endpoint: '/api/feuerwehr_heusweiler', labelField: 'name' },
  // legacy string fields for Alt-Tabellen
  'Lagerort': { endpoint: '/api/lagerort', labelField: 'name' },
  'Fahrzeug_Geraetehaus': { endpoint: '/api/fahrzeug_geraetehaus', labelField: 'name' },
  'eigentuemer': { endpoint: '/api/eigentuemer', labelField: 'name' },
  'Feuerwehr_Heusweiler': { endpoint: '/api/feuerwehr_heusweiler', labelField: 'name' },
};

// Feldlisten für alle Tabellen mit FKs
const tableFields = {
  'Fahrzeuge_und_Fahrzeugladegeraete': [
    'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'pruefung', 'norm', 'inventarnummer',
    'lagerort_id', 'fahrzeug_geraetehaus_id', 'eigentuemer_id', 'feuerwehr_heusweiler_id'
  ],
  'Arbeitsgeraete': [
    'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'pruefung', 'norm', 'inventarnummer',
    'lagerort_id', 'fahrzeug_geraetehaus_id', 'eigentuemer_id', 'feuerwehr_heusweiler_id'
  ],
  'Loeschgeraete': [
    'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'pruefung', 'norm', 'inventarnummer',
    'lagerort_id', 'fahrzeug_geraetehaus_id', 'eigentuemer_id', 'feuerwehr_heusweiler_id'
  ],
  'Rettungsgeraete': [
    'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'pruefung', 'norm', 'inventarnummer',
    'lagerort_id', 'fahrzeug_geraetehaus_id', 'eigentuemer_id', 'feuerwehr_heusweiler_id'
  ],
  'Schutzkleidung': [
    'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'pruefung', 'norm', 'inventarnummer',
    'lagerort_id', 'eigentuemer_id'
  ],
  // ...weitere Tabellen nach gleichem Muster ergänzen
};

const defaultFields = [
  'Artikelbezeichnung', 'Typ', 'Spezifikation', 'Seriennummer', 'Hersteller', 'Anzahl', 'Einheit', 'Anschaffungsjahr', 'Kosten', 'Bemerkung', 'Pruefung', 'Norm', 'Inventarnummer'
];

export default function DynamicTable({ token, tableName }) {
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [editRow, setEditRow] = useState(null);
  const [dropdownOptions, setDropdownOptions] = useState({});

  // Fetch dropdown options for all configured dropdown fields
  useEffect(() => {
    const fetchDropdowns = async () => {
      const opts = {};
      for (const field of Object.keys(dropdownConfig)) {
        try {
          const res = await axios.get(dropdownConfig[field].endpoint, { headers: { Authorization: `Bearer ${token}` } });
          opts[field] = res.data;
        } catch (e) {
          opts[field] = [];
        }
      }
      setDropdownOptions(opts);
    };
    fetchDropdowns();
  }, [token]);

  const fetchRows = async () => {
    setLoading(true);
    const res = await axios.get(`/api/geraeteTabs/${tableName}`, {
      headers: { Authorization: `Bearer ${token}` },
      params: search ? { q: search } : {},
    });
    setRows(res.data);
    setLoading(false);
  };

  useEffect(() => { fetchRows(); }, [search, tableName]);

  const handleEdit = (row) => setEditRow(row);
  const handleDelete = async (id) => {
    await axios.delete(`/api/geraeteTabs/${tableName}/${id}`, { headers: { Authorization: `Bearer ${token}` } });
    fetchRows();
  };
  const handleSave = async () => {
    if (editRow.ID) {
      await axios.put(`/api/geraeteTabs/${tableName}/${editRow.ID}`, editRow, { headers: { Authorization: `Bearer ${token}` } });
    } else {
      await axios.post(`/api/geraeteTabs/${tableName}`, editRow, { headers: { Authorization: `Bearer ${token}` } });
    }
    setEditRow(null);
    fetchRows();
  };

  // Use table-specific fields if available
  const fields = tableFields[tableName] || defaultFields;
  const columns = [
    { field: 'ID', headerName: 'ID', width: 80 },
    ...fields.map(f => ({ field: f, headerName: f, width: 140, editable: true })),
    {
      field: 'actions', headerName: 'Aktionen', width: 180, renderCell: (params) => (
        <>
          <Button size="small" onClick={() => handleEdit(params.row)}>Bearbeiten</Button>
          <Button size="small" color="error" onClick={() => handleDelete(params.row.ID)}>Löschen</Button>
        </>
      )
    }
  ];

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h6">{tableName}</Typography>
      <TextField label="Suche" value={search} onChange={e => setSearch(e.target.value)} sx={{ mb: 2 }} />
      <Button variant="contained" onClick={() => setEditRow({})}>Neu</Button>
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} loading={loading} getRowId={r => r.ID} />
      </div>
      {editRow && (
        <Box sx={{ mt: 2 }}>
          <Typography>{editRow.ID ? 'Bearbeiten' : 'Neu anlegen'}</Typography>
          {fields.map(f => {
            if (dropdownConfig[f]) {
              // Render dropdown for FK fields
              return (
                <TextField
                  key={f}
                  label={f}
                  select
                  value={editRow[f] || ''}
                  onChange={e => setEditRow({ ...editRow, [f]: e.target.value })}
                  sx={{ mr: 1, mb: 1 }}
                >
                  <MenuItem value=""><em>Bitte wählen</em></MenuItem>
                  {(dropdownOptions[f] || []).map(opt => (
                    <MenuItem key={opt.id || opt.ID} value={opt.id || opt.ID}>
                      {opt[dropdownConfig[f].labelField] || opt.name || opt.bezeichnung || opt.ID}
                    </MenuItem>
                  ))}
                </TextField>
              );
            }
            // Default text field
            return (
              <TextField
                key={f}
                label={f}
                value={editRow[f] || ''}
                onChange={e => setEditRow({ ...editRow, [f]: e.target.value })}
                sx={{ mr: 1, mb: 1 }}
              />
            );
          })}
          <Button variant="contained" onClick={handleSave} sx={{ mr: 1 }}>Speichern</Button>
          <Button onClick={() => setEditRow(null)}>Abbrechen</Button>
        </Box>
      )}
    </Box>
  );
}
