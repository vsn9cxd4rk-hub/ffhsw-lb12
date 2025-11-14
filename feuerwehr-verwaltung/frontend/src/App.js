

import React, { useState } from 'react';
import LoginForm from './components/LoginForm';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Box from '@mui/material/Box';

import MitgliederTable from './components/MitgliederTable';
import DynamicTable from './components/DynamicTable';
import UserTable from './components/UserTable';
import { parseJwt } from './components/jwtHelper';
import Header from './components/Header';

const geraeteTabellen = [
  { key: 'arbeitsgeraete', label: 'Arbeitsgeräte', table: 'Arbeitsgeraete' },
  { key: 'loeschgeraete', label: 'Löschgeräte', table: 'Loeschgeraete' },
  { key: 'elektrische_geraete', label: 'Elektrische Geräte', table: 'Elektrische_Geraete' },
  { key: 'rettungsgeraete', label: 'Rettungsgeräte', table: 'Rettungsgeraete' },
  { key: 'erste_hilfe_und_hygiene', label: 'Erste Hilfe und Hygiene', table: 'Erste_Hilfe_und_Hygiene' },
  { key: 'schutzkleidung', label: 'Schutzkleidung', table: 'Schutzkleidung' },
  { key: 'signal_und_beleuchtungsgeraete', label: 'Signal und Beleuchtungsgeräte', table: 'Signal_und_Beleuchtungsgeraete' },
  { key: 'fahrzeuge_und_fahrzeugladegeraete', label: 'Fahrzeuge und Fahrzeugladegeräte', table: 'Fahrzeuge_und_Fahrzeugladegeraete' },
  { key: 'edv', label: 'EDV', table: 'EDV' },
];

const gebaeudeTabellen = [
  { key: 'fahrzeughalle_und_werkstatt', label: 'Fahrzeughalle und Werkstatt', table: 'Fahrzeughalle_und_Werkstatt' },
  { key: 'kueche_fahrzeughalle', label: 'Küche Fahrzeughalle', table: 'Kueche_Fahrzeughalle' },
  { key: 'schulungsraum_und_ausbildung', label: 'Schulungsraum und Ausbildung', table: 'Schulungsraum_und_Ausbildung' },
  { key: 'buero_lbz_fuehrung', label: 'Büro LBZ Führung', table: 'Buero_LBZ_Fuehrung' },
];

const theme = createTheme({
  // Add your theme configuration here, e.g. palette, typography, etc.
});

function App() {
  // State and logic for navigation and authentication
  const drawerWidth = 260;
  const [nav, setNav] = useState('geraete');
  const [selectedTable, setSelectedTable] = useState(null);
  // Token and user role logic
  const token = localStorage.getItem('token');
  let userRole = null;
  if (token) {
    try {
      userRole = parseJwt(token).role;
    } catch (e) {
      userRole = null;
    }
  }
  const [loggedIn, setLoggedIn] = useState(!!token);
  const handleLogin = (newToken) => {
    localStorage.setItem('token', newToken);
    setLoggedIn(true);
    window.location.reload();
  };
  if (!loggedIn) {
    return <LoginForm onLogin={handleLogin} />;
  }
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      {/* Horizontal Header */}
      <Header />
      <Box sx={{ display: 'flex', pt: 0 }}>
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            position: 'relative',
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box', mt: 8, position: 'relative' },
          }}
        >
          <List>
            {/* Benutzerverwaltung ganz oben */}
            {userRole === 'admin' && (
              <ListItem key="benutzer" disablePadding>
                <ListItemButton selected={nav === 'benutzer'} onClick={() => { setNav('benutzer'); setSelectedTable(null); }}>
                  <ListItemText primary="Benutzerverwaltung" />
                </ListItemButton>
              </ListItem>
            )}
            {/* Mitgliederverwaltung */}
            <ListItem key="mitglieder" disablePadding>
              <ListItemButton selected={nav === 'mitglieder'} onClick={() => { setNav('mitglieder'); setSelectedTable(null); }}>
                <ListItemText primary="Mitgliederverwaltung" />
              </ListItemButton>
            </ListItem>
            {/* Gerätewartung mit Unterpunkten */}
            <ListItem key="geraete" disablePadding>
              <ListItemButton
                selected={nav === 'geraete'}
                onClick={() => {
                  if (nav === 'geraete') {
                    setNav(null);
                    setSelectedTable(null);
                  } else {
                    setNav('geraete');
                    setSelectedTable(null);
                  }
                }}
              >
                <ListItemText primary="Gerätewartung" />
              </ListItemButton>
            </ListItem>
            {nav === 'geraete' && (
              <List sx={{ pl: 3 }}>
                {geraeteTabellen.map(tab => (
                  <ListItem key={tab.key} disablePadding>
                    <ListItemButton selected={selectedTable === tab.table} onClick={() => setSelectedTable(tab.table)}>
                      <ListItemText primary={tab.label} />
                    </ListItemButton>
                  </ListItem>
                ))}
              </List>
            )}
            {/* Gebäudeverwaltung mit Unterpunkten */}
            <ListItem key="gebaeude" disablePadding>
              <ListItemButton
                selected={nav === 'gebaeude'}
                onClick={() => {
                  if (nav === 'gebaeude') {
                    setNav(null);
                    setSelectedTable(null);
                  } else {
                    setNav('gebaeude');
                    setSelectedTable(null);
                  }
                }}
              >
                <ListItemText primary="Gebäudeverwaltung" />
              </ListItemButton>
            </ListItem>
            {nav === 'gebaeude' && (
              <List sx={{ pl: 3 }}>
                {gebaeudeTabellen.map(tab => (
                  <ListItem key={tab.key} disablePadding>
                    <ListItemButton selected={selectedTable === tab.table} onClick={() => setSelectedTable(tab.table)}>
                      <ListItemText primary={tab.label} />
                    </ListItemButton>
                  </ListItem>
                ))}
              </List>
            )}
          </List>
          <Box sx={{ position: 'absolute', bottom: 16, left: 0, width: '100%', px: 2 }}>
            <ListItem disablePadding>
              <ListItemButton onClick={() => {
                localStorage.removeItem('token');
                window.location.reload();
              }} sx={{ backgroundColor: 'red', color: '#fff', textAlign: 'left', justifyContent: 'flex-start', pl: 2, fontSize: 16, minHeight: 36, '&:hover': { backgroundColor: '#b71c1c' } }}>
                <ListItemText primary="Logout" sx={{ color: '#fff' }} />
              </ListItemButton>
            </ListItem>
          </Box>
        </Drawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3, ml: 2, mt: 8 }}>
          {/* Anzeige der jeweiligen Tabelle */}
          {selectedTable && <DynamicTable token={token} tableName={selectedTable} />}
          {nav === 'mitglieder' && !selectedTable && <MitgliederTable token={token} />}
          {nav === 'benutzer' && userRole === 'admin' && !selectedTable && <UserTable token={token} userRole={userRole} />}
        </Box>
      </Box>

    </ThemeProvider>
  );
}

export default App;
