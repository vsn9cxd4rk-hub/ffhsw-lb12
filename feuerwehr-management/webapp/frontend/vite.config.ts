import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';
import { readFileSync } from 'fs';

// Vendor packages grouped into separately cached chunks
const CHUNK_GROUPS: Record<string, string[]> = {
  vendor: ['react', 'react-dom', 'scheduler', 'react-router', 'react-router-dom'],
  query: ['@tanstack/react-query', '@tanstack/query-core', 'axios', 'zustand'],
  ui: ['@headlessui/react', '@heroicons/react'],
  pdf: ['jspdf', 'jspdf-autotable'],
};
const CHUNKS: Record<string, string> = Object.fromEntries(
  Object.entries(CHUNK_GROUPS).flatMap(([chunk, pkgs]) => pkgs.map((p) => [p, chunk])),
);

const { version } = JSON.parse(readFileSync('./package.json', 'utf-8'));

export default defineConfig({
  define: {
    'import.meta.env.VITE_APP_VERSION': JSON.stringify(version),
  },
  plugins: [react(), tailwindcss()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:3001',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    // Vite 8 bundles with Rolldown: manualChunks only in function form
    rolldownOptions: {
      output: {
        manualChunks(id: string) {
          if (!id.includes('/node_modules/')) return undefined;
          // innermost package: node_modules/<name> or node_modules/@scope/<name>
          const parts = id.split('/node_modules/').pop()!.split('/');
          const pkg = parts[0].startsWith('@') ? `${parts[0]}/${parts[1]}` : parts[0];
          return CHUNKS[pkg];
        },
      },
    },
  },
});
