import { test, expect } from '@playwright/test';

test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Login', () => {
  test('zeigt Login-Seite', async ({ page }) => {
    await page.goto('/login');
    await expect(page.getByText('Feuerwehr Management')).toBeVisible();
    await expect(page.getByPlaceholder('Benutzername')).toBeVisible();
    await expect(page.getByPlaceholder('Passwort')).toBeVisible();
  });

  test('zeigt Fehler bei falschem Passwort', async ({ page }) => {
    await page.goto('/login');
    await page.getByPlaceholder('Benutzername').fill('admin');
    await page.getByPlaceholder('Passwort').fill('falsch');
    await page.getByRole('button', { name: 'Anmelden' }).click();
    await expect(page.getByText(/fehlgeschlagen|ungültig|falsch/i)).toBeVisible();
  });

  test('leitet zum Dashboard nach Login weiter', async ({ page }) => {
    await page.goto('/login');
    await page.getByPlaceholder('Benutzername').fill('admin');
    await page.getByPlaceholder('Passwort').fill('Admin123!');
    await page.getByRole('button', { name: 'Anmelden' }).click();
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('leitet unauthentifizierte Benutzer zum Login', async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page).toHaveURL(/\/login/);
  });
});
