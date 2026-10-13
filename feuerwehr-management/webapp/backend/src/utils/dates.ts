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

/**
 * Adds calendar months and stays in the target month: 31.08. + 6 months is 28./29.02.,
 * whereas Date#setMonth rolls over to 02./03.03. (inspection due dates were 1-3 days late).
 */
export function addMonths(date: Date, months: number): Date {
  const result = new Date(date);
  const day = result.getDate();
  result.setDate(1);
  result.setMonth(result.getMonth() + months);
  const lastDayOfMonth = new Date(result.getFullYear(), result.getMonth() + 1, 0).getDate();
  result.setDate(Math.min(day, lastDayOfMonth));
  return result;
}
