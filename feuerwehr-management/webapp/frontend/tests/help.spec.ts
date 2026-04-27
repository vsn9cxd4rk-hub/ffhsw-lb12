import { test, expect } from '@playwright/test';

test.describe('Online-Hilfe', () => {
  test('Hilfe-Seite ist erreichbar', async ({ page }) => {
    await page.goto('/help');
    await expect(page.getByText('Feuerwehr Management System - Hilfe')).toBeVisible();
  });

  test('zeigt Inhaltsverzeichnis', async ({ page }) => {
    await page.goto('/help');
    await expect(page.getByText('Inhaltsverzeichnis')).toBeVisible();
    await expect(page.getByRole('button', { name: /Übersicht/ })).toBeVisible();
    await expect(page.getByRole('button', { name: /Dashboard/ })).toBeVisible();
    await expect(page.getByRole('button', { name: /Bestandsliste/ })).toBeVisible();
    await expect(page.getByRole('button', { name: /Prüfbuch/ })).toBeVisible();
    await expect(page.getByRole('button', { name: /Wartung/ })).toBeVisible();
  });

  test('zeigt alle 12 Kapitel', async ({ page }) => {
    await page.goto('/help');
    await expect(page.getByText('1. Übersicht')).toBeVisible();
    await expect(page.getByText('2. Dashboard')).toBeVisible();
    await expect(page.getByText('3. Bestandsliste')).toBeVisible();
    await expect(page.getByText('4. Prüfbuch')).toBeVisible();
    await expect(page.getByText('5. Mängel & Reparaturen')).toBeVisible();
    await expect(page.getByText('6. Einstellungen')).toBeVisible();
    await expect(page.getByText('7. Personal')).toBeVisible();
    await expect(page.getByText('8. Fahrzeuge')).toBeVisible();
    await expect(page.getByText('9. Einsätze & Veranstaltungen')).toBeVisible();
    await expect(page.getByText('10. Installation Windows')).toBeVisible();
    await expect(page.getByText('11. Installation Linux')).toBeVisible();
    await expect(page.getByText('12. Wartung & Backup')).toBeVisible();
  });

  test('Hilfe-Link im Header funktioniert', async ({ page }) => {
    await page.goto('/dashboard');
    await page.locator('button[title="Hilfe"]').click();
    await expect(page).toHaveURL(/\/help/);
    await expect(page.getByText('Feuerwehr Management System - Hilfe')).toBeVisible();
  });

  test('Kapitel-Navigation scrollt', async ({ page }) => {
    await page.goto('/help');
    await page.getByRole('button', { name: /Installation Windows/ }).click();
    await page.waitForTimeout(500);
    await expect(page.locator('#install-windows')).toBeInViewport();
  });
});
