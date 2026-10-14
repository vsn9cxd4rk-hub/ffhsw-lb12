import { test, expect } from '@playwright/test';

test.describe('Statistik', () => {
  test('zeigt Kennzahlen und alle Auswertungen', async ({ page }) => {
    await page.goto('/statistics');
    await expect(page.getByRole('heading', { name: 'Einsatzstatistik' })).toBeVisible();
    for (const label of ['Gesamt', 'Real-Einsätze', 'Fehlalarme', 'Aktiv tätig']) {
      await expect(page.getByText(label, { exact: true })).toBeVisible();
    }
    for (const title of [
      'Einsätze pro Monat', 'Einsätze nach Ortsteil', 'Einsätze nach Stichwort', 'Einsatzergebnis',
      'Einsätze nach Tageszeit', 'Einsätze nach Wochentag', 'Personal Top 10',
    ]) {
      await expect(page.getByText(title, { exact: true })).toBeVisible();
    }
  });

  test('Jahresauswahl bietet die letzten sechs Jahre und lädt neu', async ({ page }) => {
    const year = new Date().getFullYear();
    await page.goto('/statistics');
    const select = page.getByRole('combobox');
    await expect(select).toHaveValue(String(year));
    await expect(select.locator('option')).toHaveCount(6);

    const [res] = await Promise.all([
      page.waitForResponse(r => r.url().includes('/api/dashboard/statistics') && r.url().includes(`year=${year - 1}`)),
      select.selectOption(String(year - 1)),
    ]);
    expect(res.ok()).toBeTruthy();
    await expect(page.getByRole('heading', { name: 'Einsatzstatistik' })).toBeVisible();
    await expect(page.getByText('Einsätze pro Monat', { exact: true })).toBeVisible();
  });
});
