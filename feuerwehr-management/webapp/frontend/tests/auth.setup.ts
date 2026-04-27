import { test as setup, expect } from '@playwright/test';

const authFile = 'tests/.auth/user.json';

setup('login as admin', async ({ page }) => {
  await page.goto('/login');
  await page.getByPlaceholder('Benutzername').fill('admin');
  await page.getByPlaceholder('Passwort').fill('Admin123!');
  await page.getByRole('button', { name: 'Anmelden' }).click();
  await expect(page).toHaveURL(/\/dashboard/);
  await page.context().storageState({ path: authFile });
});
