// Prisma's DateTime fields require a full ISO-8601 datetime; <input type="date">
// only ever sends a plain "YYYY-MM-DD" string, which fails with "premature end
// of input" if passed through unconverted.
export function convertDates(data: Record<string, unknown>, fields: string[]): Record<string, unknown> {
  const result = { ...data };
  for (const field of fields) {
    if (result[field] && typeof result[field] === 'string' && !(result[field] as string).includes('T')) {
      result[field] = new Date(result[field] as string).toISOString();
    }
  }
  return result;
}
