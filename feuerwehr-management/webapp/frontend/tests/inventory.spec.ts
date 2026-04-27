import { test, expect } from '@playwright/test';

test.describe('Bestandsliste', () => {
  test('zeigt Bestandsliste mit Filtern', async ({ page }) => {
    await page.goto('/inventory');
    await expect(page.getByText('Lager')).toBeVisible();
    await expect(page.getByText('Geräteklasse')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Neuer Artikel' })).toBeVisible();
  });

  test('öffnet Artikel-Formular', async ({ page }) => {
    await page.goto('/inventory');
    await page.getByRole('button', { name: 'Neuer Artikel' }).click();
    await expect(page.getByText('Neuer Artikel')).toBeVisible();
    await expect(page.getByLabel('Bezeichnung')).toBeVisible();
  });

  test('Artikel-Formular enthält alle Felder', async ({ page }) => {
    await page.goto('/inventory');
    await page.getByRole('button', { name: 'Neuer Artikel' }).click();
    await expect(page.getByLabel('Bezeichnung')).toBeVisible();
    await expect(page.getByLabel('Inventarnummer')).toBeVisible();
    await expect(page.getByLabel('Hersteller')).toBeVisible();
    await expect(page.getByLabel('Seriennummer')).toBeVisible();
    await expect(page.getByLabel('DIN')).toBeVisible();
    await expect(page.getByLabel('Bezeichnung LB')).toBeVisible();
    await expect(page.getByLabel(/Gemeinde/)).toBeVisible();
    await expect(page.getByLabel(/MP Feuer/)).toBeVisible();
    await expect(page.getByLabel('Indienststellung')).toBeVisible();
  });

  test('Suche filtert Artikel', async ({ page }) => {
    await page.goto('/inventory');
    const searchInput = page.getByPlaceholder(/suchen/i);
    await searchInput.fill('Testsuche123');
    await page.waitForTimeout(500);
    await expect(page.getByText('Keine Artikel gefunden')).toBeVisible();
  });
});
