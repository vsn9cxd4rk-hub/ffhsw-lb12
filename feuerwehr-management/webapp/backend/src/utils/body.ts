import { Prisma } from '@prisma/client';

// Never client-writable on any model: identity/bookkeeping columns and server-managed
// file locations (a client-supplied filePath would let download/delete endpoints read or
// remove arbitrary files on the server).
const ALWAYS_OMIT = new Set([
  'id', 'createdAt', 'updatedAt', 'deletedAt', 'tenantId',
  'filePath', 'certificatePath', 'imagePath',
]);

type FieldInfo = { json: boolean };

const writableFields = new Map<string, Map<string, FieldInfo>>(
  Prisma.dmmf.datamodel.models.map((model) => [
    model.name,
    new Map(
      model.fields
        .filter((f) => (f.kind === 'scalar' || f.kind === 'enum') && !f.isUpdatedAt && !ALWAYS_OMIT.has(f.name))
        .map((f) => [f.name, { json: f.type === 'Json' }] as const),
    ),
  ]),
);

/**
 * Copies only the model's own scalar columns from a request body.
 *
 * Passing req.body straight into prisma.create/update lets a client send nested relation
 * writes (e.g. event.attendances.create.member.create.user.create { isAdmin: true }), which
 * bypasses every route-level permission check. Relation fields are dropped here, as are
 * object values on non-Json columns (Prisma would read them as atomic operations).
 * Foreign keys like `memberId` are scalars and stay writable unless listed in `omit`.
 */
export function pickWritable(
  model: Prisma.ModelName,
  body: unknown,
  omit: readonly string[] = [],
): Record<string, unknown> {
  const fields = writableFields.get(model);
  if (!fields) throw new Error(`Unknown model ${model}`);
  const result: Record<string, unknown> = {};
  if (!body || typeof body !== 'object' || Array.isArray(body)) return result;

  for (const [key, value] of Object.entries(body as Record<string, unknown>)) {
    const field = fields.get(key);
    if (!field || omit.includes(key) || value === undefined) continue;
    if (!field.json && value !== null && typeof value === 'object') continue;
    result[key] = value;
  }
  return result;
}
