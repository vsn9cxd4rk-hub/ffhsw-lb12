import { test, expect } from '@playwright/test';
import { E2E_ADMIN_USER } from './credentials';

test.describe('Benutzerverwaltung', () => {
  test('ist über die Navigation erreichbar und zeigt die Benutzerliste', async ({ page }) => {
    await page.goto('/dashboard');
    await page.getByRole('link', { name: 'Benutzerverwaltung' }).click();
    await expect(page).toHaveURL(/\/admin\/users/);
    await expect(page.getByPlaceholder('Benutzer suchen...')).toBeVisible();
    await expect(page.getByRole('columnheader', { name: 'Benutzername' })).toBeVisible();
    await expect(page.getByRole('columnheader', { name: 'Rollen' })).toBeVisible();
  });

  test('Benutzer anlegen, suchen, bearbeiten und löschen', async ({ page }) => {
    const username = `pw-user-${Date.now()}`;
    await page.goto('/admin/users');

    // Anlegen: ohne Berechtigungsgruppe und Passwort nicht speicherbar
    await page.getByRole('button', { name: 'Neuer Benutzer' }).click();
    const modal = page.getByRole('dialog');
    await expect(modal.getByText('Neuer Benutzer')).toBeVisible();
    await modal.getByLabel('Benutzername').fill(username);
    await modal.getByLabel('Name', { exact: true }).fill('Playwright Testbenutzer');
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeDisabled();
    await modal.getByLabel('Passwort').fill('PwTest1234');
    await modal.locator('select').first().selectOption({ index: 1 });
    await modal.getByRole('button', { name: 'Speichern' }).click();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeHidden();

    // Suche findet den Benutzer; ein vom Admin vergebenes Passwort muss gewechselt werden
    await page.getByPlaceholder('Benutzer suchen...').fill(username);
    const row = page.getByRole('row').filter({ hasText: username });
    await expect(row).toHaveCount(1);
    await expect(row).toContainText('Playwright Testbenutzer');
    await expect(row).toContainText('Aktiv');
    await expect(row).toContainText('Passwortwechsel offen');

    // Bearbeiten: Benutzername ist gesperrt, deaktivieren
    await row.getByRole('button', { name: 'Bearbeiten' }).click();
    await expect(modal.getByText('Benutzer bearbeiten')).toBeVisible();
    await expect(modal.getByLabel('Benutzername')).toBeDisabled();
    await modal.getByLabel('Aktiv').uncheck();
    await modal.getByRole('button', { name: 'Speichern' }).click();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeHidden();
    await expect(row).toContainText('Inaktiv');

    // Löschen erst nach Rückfrage; Abbrechen lässt den Benutzer stehen
    await row.getByRole('button', { name: 'Löschen' }).click();
    const confirm = page.getByRole('dialog').filter({ hasText: 'Benutzer löschen' });
    await expect(confirm.getByText(`„${username}“`)).toBeVisible();
    await confirm.getByRole('button', { name: 'Abbrechen' }).click();
    await expect(row).toHaveCount(1);

    await row.getByRole('button', { name: 'Löschen' }).click();
    await confirm.getByRole('button', { name: 'Löschen' }).click();
    await expect(page.getByRole('row').filter({ hasText: username })).toHaveCount(0);
  });

  test('das eigene Konto hat keinen Löschen-Button', async ({ page }) => {
    await page.goto('/admin/users');
    await page.getByPlaceholder('Benutzer suchen...').fill(E2E_ADMIN_USER);
    const own = page.getByRole('row').filter({ has: page.getByText(E2E_ADMIN_USER, { exact: true }) });
    await expect(own.getByRole('button', { name: 'Bearbeiten' })).toBeVisible();
    await expect(own.getByRole('button', { name: 'Löschen' })).toHaveCount(0);
  });

  test('zu schwaches Passwort wird mit Fehlermeldung abgelehnt', async ({ page }) => {
    await page.goto('/admin/users');
    await page.getByRole('button', { name: 'Neuer Benutzer' }).click();
    const modal = page.getByRole('dialog');
    await modal.getByLabel('Benutzername').fill(`pw-weak-${Date.now()}`);
    await modal.getByLabel('Passwort').fill('kurz');
    await modal.locator('select').first().selectOption({ index: 1 });
    await modal.getByRole('button', { name: 'Speichern' }).click();
    await expect(modal.locator('.bg-red-50')).toBeVisible();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeVisible();
  });
});
