import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

export default function Header() {
  return (
    <Box sx={{ width: '100%', bgcolor: 'red', color: 'white', py: 2, px: 3, position: 'fixed', top: 0, left: 0, zIndex: 1201, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
      <Typography variant="h5" align="left" sx={{ color: 'white', fontWeight: 'bold' }}>
        Feuerwehr Verwaltung
      </Typography>
      <img src="/pictures/LB12.png" alt="Logo" style={{ height: 48, marginLeft: 'auto' }} />
    </Box>
  );
}
