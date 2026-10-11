import { describe, it, expect } from 'vitest';
import { jwtSecretProblem } from '../src/config/jwt';

// Without JWT_ACCESS_SECRET the code used to fall back to a hard-coded string, so anybody
// could sign admin tokens. The server now refuses to start with a missing/weak secret.

describe('JWT-Secret-Prüfung', () => {
  it('lehnt fehlende, kurze und Platzhalter-Secrets ab', () => {
    expect(jwtSecretProblem('')).toMatch(/nicht gesetzt/);
    expect(jwtSecretProblem('short')).toMatch(/kürzer/);
    expect(jwtSecretProblem('fallback-access-secret-change-me')).not.toBeNull();
    expect(jwtSecretProblem('your-access-secret-change-in-production-min-32-chars')).toMatch(/Platzhalter/);
  });

  it('akzeptiert einen zufälligen Wert', () => {
    expect(jwtSecretProblem('Zm9vYmFyYmF6cXV4MTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1u')).toBeNull();
  });
});
