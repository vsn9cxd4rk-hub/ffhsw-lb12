import { test, expect } from '@playwright/test';

test.describe('Mängel & Reparaturen', () => {
  test('zeigt Mängel-Seite mit zwei Tabs', async ({ page }) => {
    await page.goto('/defects');
    await expect(page.getByRole('button', { name: 'Mängel' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Reparaturen' })).toBeVisible();
  });

  test('zeigt Filter in Mängel-Tab', async ({ page }) => {
    await page.goto('/defects');
    await expect(page.getByText('Status')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Neuer Mangel' })).toBeVisible();
  });

  test('öffnet Mangel-Formular', async ({ page }) => {
    await page.goto('/defects');
    await page.getByRole('button', { name: 'Neuer Mangel' }).click();
    await expect(page.getByText('Neuen Mangel')).toBeVisible();
    await expect(page.getByLabel(/Beschreibung/)).toBeVisible();
    await expect(page.getByLabel(/Gemeldet von/)).toBeVisible();
  });

  test('wechselt zu Reparaturen-Tab', async ({ page }) => {
    await page.goto('/defects');
    await page.getByRole('button', { name: 'Reparaturen' }).click();
    await expect(page.getByRole('button', { name: 'Neue Reparatur' })).toBeVisible();
  });

  test('öffnet Reparatur-Formular', async ({ page }) => {
    await page.goto('/defects');
    await page.getByRole('button', { name: 'Reparaturen' }).click();
    await page.getByRole('button', { name: 'Neue Reparatur' }).click();
    await expect(page.getByText('Neue Reparatur')).toBeVisible();
    await expect(page.getByLabel(/Durchgeführt von/)).toBeVisible();
  });
});
