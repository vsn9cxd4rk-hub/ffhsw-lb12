import { test, expect } from '@playwright/test';

test.describe('Prüfbuch', () => {
  test('zeigt drei Tabs', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByRole('button', { name: 'Fällige Prüfungen' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Prüfhistorie' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Berichte' })).toBeVisible();
  });

  test('zeigt Suchfeld', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByPlaceholder(/Inv.-Nr/)).toBeVisible();
  });

  test('zeigt Geräteklassen-Filter im Due-Tab', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByText('Geräteklasse')).toBeVisible();
    await expect(page.getByText('Unterklasse')).toBeVisible();
  });

  test('öffnet Prüfungsdialog', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfung dokumentieren' }).click();
    await expect(page.getByText('Prüfung dokumentieren')).toBeVisible();
    await expect(page.getByLabel(/Prüfdatum/)).toBeVisible();
    await expect(page.getByLabel(/Prüfer/)).toBeVisible();
    await expect(page.getByText('Prüfart')).toBeVisible();
  });

  test('Prüfhistorie Tab wechselt', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfhistorie' }).click();
    await page.waitForTimeout(500);
    const table = page.locator('table');
    await expect(table).toBeVisible();
  });

  test('Berichte Tab zeigt Filter', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Berichte' }).click();
    await expect(page.getByText('PDF Export')).toBeVisible();
  });
});
