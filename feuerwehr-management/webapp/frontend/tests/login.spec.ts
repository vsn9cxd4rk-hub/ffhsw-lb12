import { test, expect } from '@playwright/test';
import { E2E_ADMIN_USER, E2E_ADMIN_PASSWORD } from './credentials';

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
    if (!E2E_ADMIN_PASSWORD) throw new Error('E2E_ADMIN_PASSWORD ist nicht gesetzt');
    await page.goto('/login');
    await page.getByPlaceholder('Benutzername').fill(E2E_ADMIN_USER);
    await page.getByPlaceholder('Passwort').fill(E2E_ADMIN_PASSWORD);
    await page.getByRole('button', { name: 'Anmelden' }).click();
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('leitet unauthentifizierte Benutzer zum Login', async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page).toHaveURL(/\/login/);
  });
});
