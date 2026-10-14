import { test, expect } from '@playwright/test';

test.describe('Bestandsliste', () => {
  test('zeigt Bestandsliste mit Filtern', async ({ page }) => {
    await page.goto('/inventory');
    await expect(page.getByRole('heading', { name: 'Lager', exact: true })).toBeVisible();
    await expect(page.getByText('Geräteklasse').first()).toBeVisible();
    await expect(page.getByRole('button', { name: 'Neuer Artikel' })).toBeVisible();
  });

  test('öffnet Artikel-Formular', async ({ page }) => {
    await page.goto('/inventory');
    await page.getByRole('button', { name: 'Neuer Artikel' }).click();
    await expect(page.getByRole('heading', { name: 'Neuer Artikel' })).toBeVisible();
    await expect(page.getByLabel(/^Bezeichnung(\s*\*)?$/)).toBeVisible();
  });

  test('Artikel-Formular enthält alle Felder', async ({ page }) => {
    await page.goto('/inventory');
    await page.getByRole('button', { name: 'Neuer Artikel' }).click();
    await expect(page.getByLabel(/^Bezeichnung(\s*\*)?$/)).toBeVisible();
    await expect(page.getByLabel('Inventarnummer')).toBeVisible();
    await expect(page.getByLabel('Hersteller', { exact: true })).toBeVisible();
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

  test('Artikeldetail zeigt QR-Code und Barcode', async ({ page }) => {
    await page.goto('/inventory');
    // Warten bis die Artikel-Tabelle Zeilen geladen hat, dann ersten Artikel öffnen
    const firstRow = page.locator('table tbody tr').first();
    await expect(firstRow).toBeVisible();
    await firstRow.click();
    await expect(page).toHaveURL(/\/inventory\/\d+/);

    // Beide Kennzeichnungs-Boxen vorhanden
    await expect(page.getByText('QR-Code', { exact: true })).toBeVisible();
    await expect(page.getByText('Barcode', { exact: true })).toBeVisible();

    // Barcode wird als SVG gerendert (jsbarcode) und QR-Code als Bild
    await expect(page.getByRole('img', { name: /^Barcode / })).toBeVisible();
    await expect(page.getByRole('img', { name: 'QR-Code' })).toBeVisible();

    // Zwei "Etikett drucken"-Buttons: einer für QR, einer für Barcode
    await expect(page.getByRole('button', { name: 'Etikett drucken' })).toHaveCount(2);
  });
});
