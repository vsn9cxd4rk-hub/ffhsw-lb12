import { execSync } from 'child_process';
import { fileURLToPath } from 'url';

// Removes the records the specs created (members, events, operations, vehicles, users),
// see backend/scripts/cleanup-e2e-data.ts. Needs the backend's DB access, i.e. a local run.
// E2E_KEEP_DATA=1 keeps them, e.g. to look at a failed run.
export default function globalTeardown() {
  if (process.env.E2E_KEEP_DATA) return;
  try {
    execSync('npm run -s e2e:cleanup', {
      cwd: fileURLToPath(new URL('../../backend', import.meta.url)),
      stdio: ['ignore', 'inherit', 'inherit'],
    });
  } catch {
    console.warn('E2E-Testdaten konnten nicht entfernt werden (manuell: npm run e2e:cleanup im Backend).');
  }
}
