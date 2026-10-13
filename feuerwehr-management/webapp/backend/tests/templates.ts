import fs from 'fs';
import { prisma } from '../src/config/database';
import { resolveUploadPath } from '../src/utils/files';

/**
 * State of a report template in this installation: missing (no DB row), present (row and file
 * below UPLOAD_PATH) or broken (row without file – reported by backup-integrity.test.ts).
 * Generator tests only run where the template is actually usable.
 */
export async function templateState(name: string): Promise<'missing' | 'present' | 'broken'> {
  const template = await prisma.template.findFirst({ where: { name: { contains: name } } });
  if (!template) return 'missing';
  const file = resolveUploadPath(template.filePath);
  return file && fs.existsSync(file) ? 'present' : 'broken';
}
