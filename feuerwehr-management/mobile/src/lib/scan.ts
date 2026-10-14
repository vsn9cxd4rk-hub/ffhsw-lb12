import { inventoryApi } from '@/api/client';
import type { Article } from '@/api/types';

/**
 * Labels carry either a QR code with the web URL ".../scan/<inventoryNumber>"
 * (webapp/frontend/src/components/inventory/QrPrintView.tsx) or a barcode with the
 * MP-Feuer number, falling back to the inventory number. Returns the bare number.
 */
export function parseScannedCode(data: string): string {
  const text = data.trim();
  const match = /\/scan\/([^/?#]+)/.exec(text);
  if (!match) return text;
  try {
    return decodeURIComponent(match[1]);
  } catch {
    return match[1];
  }
}

/**
 * Articles whose inventory, MP-Feuer or community number equals the code. Without such a match
 * the search hits (name, EAN, partial numbers) are returned as suggestions with exact = false.
 */
export async function findArticlesByCode(code: string): Promise<{ exact: boolean; articles: Article[] }> {
  const candidates = await inventoryApi.searchArticles(code);
  const exact = candidates.filter((a) =>
    [a.inventoryNumber, a.mpFeuerInventoryNumber, a.communityInventoryNumber].includes(code),
  );
  return exact.length > 0 ? { exact: true, articles: exact } : { exact: false, articles: candidates };
}
