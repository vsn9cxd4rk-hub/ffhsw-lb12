/**
 * Suggests the next inventory number. Besides the own numbering (e.g. 120001…121006) the
 * field also holds manufacturer and serial numbers ("23642003365", "56421-021", "1PR0…"),
 * so the highest number overall is useless. The own numbering is taken to be the most
 * frequent length among purely numeric values; the suggestion keeps that length.
 */
export function nextInventoryNumber(numbers: Array<string | null>): string {
  const byLength = new Map<number, number[]>();
  for (const raw of numbers) {
    const value = raw?.trim();
    if (!value || !/^\d+$/.test(value)) continue;
    const list = byLength.get(value.length) ?? [];
    list.push(parseInt(value, 10));
    byLength.set(value.length, list);
  }
  if (byLength.size === 0) return '1';

  let [length, values] = [...byLength][0];
  for (const [l, v] of byLength) {
    if (v.length > values.length || (v.length === values.length && l < length)) [length, values] = [l, v];
  }
  return String(Math.max(...values) + 1).padStart(length, '0');
}
