import { test, expect } from '@playwright/test';

test.describe('Dashboard', () => {
  test('zeigt Statistik-Karten', async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page.getByText('Aktive Mitglieder')).toBeVisible();
    await expect(page.getByText('Fahrzeuge')).toBeVisible();
    await expect(page.getByText(/Einsätze/)).toBeVisible();
    await expect(page.getByText(/Prüfungen/)).toBeVisible();
  });

  test('zeigt Prüfungs-Ampel', async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page.getByText('Anstehende Prüfungen')).toBeVisible();
  });

  test('zeigt letzte Einsätze', async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page.getByText('Letzte Einsätze')).toBeVisible();
  });
});
