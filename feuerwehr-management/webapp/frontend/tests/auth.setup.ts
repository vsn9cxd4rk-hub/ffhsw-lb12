import { test as setup, expect } from '@playwright/test';
import { E2E_ADMIN_USER, E2E_ADMIN_PASSWORD } from './credentials';

const authFile = 'tests/.auth/user.json';

setup('login as admin', async ({ page }) => {
  if (!E2E_ADMIN_PASSWORD) throw new Error('E2E_ADMIN_PASSWORD ist nicht gesetzt');
  await page.goto('/login');
  await page.getByPlaceholder('Benutzername').fill(E2E_ADMIN_USER);
  await page.getByPlaceholder('Passwort').fill(E2E_ADMIN_PASSWORD);
  await page.getByRole('button', { name: 'Anmelden' }).click();
  await expect(page).toHaveURL(/\/dashboard/);
  await page.context().storageState({ path: authFile });
});
