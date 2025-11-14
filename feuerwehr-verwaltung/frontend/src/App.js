

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

const geraeteTabs = [
  'Fahrzeuge_und_Fahrzeugladegeraete',
  'Schutzkleidung',
  'Erste_Hilfe_und_Hygiene',
  'Signal_und_Beleuchtungsgeraete',
  'Arbeitsgeraete',
  'Loeschgeraete',
  'Rettungsgeraete',
  'Elektrische_Geraete',
  'Fahrzeughalle_und_Werkstatt',
  'Schulungsraum_und_Ausbildung',
  'Kueche_Fahrzeughalle',
  'Buero_LBZ_Fuehrung',
  'EDV'
];

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#1976d2' },
    secondary: { main: '#dc004e' },
  },
});

const drawerWidth = 240;

const NAV_ITEMS = [
  { key: 'mitglieder', label: 'Mitgliederverwaltung' },
  { key: 'geraete', label: 'Gerätewartung' },
  { key: 'benutzer', label: 'Benutzerverwaltung', adminOnly: true },
];

function App() {
  const [token, setToken] = useState(null);
  const [nav, setNav] = useState('mitglieder');
  const [geraeteTab, setGeraeteTab] = useState(0);

  if (!token) return <LoginForm onLogin={setToken} />;
  const userPayload = parseJwt(token);
  const userRole = userPayload?.role;

  // Filter nav items for non-admins
  const navItems = NAV_ITEMS.filter(item => !item.adminOnly || userRole === 'admin');

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ display: 'flex' }}>
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
          }}
        >
          <Typography variant="h5" align="center" sx={{ my: 2 }}>
            Feuerwehr Verwaltung
          </Typography>
          <List>
            {navItems.map(item => (
              <ListItem key={item.key} disablePadding>
                <ListItemButton selected={nav === item.key} onClick={() => setNav(item.key)}>
                  <ListItemText primary={item.label} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Drawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }}>
          {nav === 'geraete' && (
            <>
              <Tabs
                value={geraeteTab}
                onChange={(_, v) => setGeraeteTab(v)}
                sx={{ mb: 2 }}
                variant="scrollable"
                scrollButtons="auto"
              >
                {geraeteTabs.map((name) => (
                  <Tab key={name} label={name} />
                ))}
              </Tabs>
              <DynamicTable token={token} tableName={geraeteTabs[geraeteTab]} />
            </>
          )}
          {nav === 'mitglieder' && <MitgliederTable token={token} />}
          {nav === 'benutzer' && userRole === 'admin' && <UserTable token={token} userRole={userRole} />}
        </Box>
      </Box>
    </ThemeProvider>
  );
}

export default App;
