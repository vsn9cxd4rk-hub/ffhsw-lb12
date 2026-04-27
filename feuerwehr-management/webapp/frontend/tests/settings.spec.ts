import { test, expect } from '@playwright/test';

test.describe('Einstellungen', () => {
  test('zeigt alle Tabs', async ({ page }) => {
    await page.goto('/settings');
    await expect(page.getByRole('button', { name: 'Allgemein' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Dienstgrade' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Jahre' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Templates' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Geräteprüfung' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Prüfarten' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Lagerorte' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Datenimport' })).toBeVisible();
  });

  test('Allgemein Tab zeigt Formular', async ({ page }) => {
    await page.goto('/settings');
    await expect(page.getByLabel('Feuerwehrname')).toBeVisible();
    await expect(page.getByLabel('Stadt')).toBeVisible();
  });

  test('Geräteprüfung Tab zeigt Klassen', async ({ page }) => {
    await page.goto('/settings');
    await page.getByRole('button', { name: 'Geräteprüfung' }).click();
    await expect(page.getByText('Geräteklassen & Prüfkriterien')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Neue Geräteklasse' })).toBeVisible();
  });

  test('Prüfarten Tab zeigt CRUD', async ({ page }) => {
    await page.goto('/settings');
    await page.getByRole('button', { name: 'Prüfarten' }).click();
    await expect(page.getByRole('button', { name: /Neue Prüfart/ })).toBeVisible();
  });

  test('Lagerorte Tab zeigt Lager', async ({ page }) => {
    await page.goto('/settings');
    await page.getByRole('button', { name: 'Lagerorte' }).click();
    await expect(page.getByText('Lagerorte')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Neuer Lagerort' })).toBeVisible();
  });

  test('Datenimport Tab zeigt Upload', async ({ page }) => {
    await page.goto('/settings');
    await page.getByRole('button', { name: 'Datenimport' }).click();
    await expect(page.getByText('Import-Typ')).toBeVisible();
    await expect(page.getByText('CSV-Datei')).toBeVisible();
  });
});
