import { describe, it, expect, vi, beforeEach } from 'vitest';
import fs from 'fs';
import os from 'os';
import path from 'path';

// convertToPdf used to build a shell string for execSync, so a file name containing $(...)
// or backticks was executed. It must now call execFile with an argument array.

const calls: { cmd: string; args: string[] }[] = [];

vi.mock('child_process', async (importOriginal) => {
  const actual = await importOriginal<typeof import('child_process')>();
  return {
    ...actual,
    execFile: vi.fn((cmd: string, args: string[], _opts: unknown, cb: (err: Error | null, out?: unknown) => void) => {
      calls.push({ cmd, args });
      if (args.includes('--convert-to')) {
        const input = args[args.length - 1];
        fs.writeFileSync(input.replace(/\.[^.]+$/, '.pdf'), '%PDF-1.4');
      }
      cb(null, { stdout: '', stderr: '' });
    }),
    execSync: vi.fn(() => { throw new Error('execSync darf nicht verwendet werden'); }),
  };
});

describe('PDF-Konvertierung ohne Shell', () => {
  beforeEach(() => { calls.length = 0; });

  it('übergibt den Dateinamen als einzelnes Argument, ohne Shell', async () => {
    const { convertToPdf } = await import('../src/services/report-generator.service');
    const dir = fs.mkdtempSync(path.join(os.tmpdir(), 'pdfconv-'));
    const input = path.join(dir, '$(touch pwned)`id`.docx');
    fs.writeFileSync(input, 'x');

    const pdf = await convertToPdf(input);

    expect(pdf).toBe(input.replace(/\.docx$/, '.pdf'));
    const conversion = calls.find((c) => c.args.includes('--convert-to'));
    expect(conversion).toBeDefined();
    expect(conversion!.args).toEqual(['--headless', '--convert-to', 'pdf', '--outdir', dir, input]);
    expect(fs.existsSync(path.join(process.cwd(), 'pwned'))).toBe(false);
    fs.rmSync(dir, { recursive: true, force: true });
  });

  it('ermittelt den LibreOffice-Pfad nur einmal', async () => {
    const { convertToPdf } = await import('../src/services/report-generator.service');
    const dir = fs.mkdtempSync(path.join(os.tmpdir(), 'pdfconv-'));
    const input = path.join(dir, 'a.docx');
    fs.writeFileSync(input, 'x');

    await convertToPdf(input);

    expect(calls.filter((c) => c.args.includes('--version'))).toHaveLength(0);
    fs.rmSync(dir, { recursive: true, force: true });
  });
});
