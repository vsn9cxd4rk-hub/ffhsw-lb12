import { test, expect } from '@playwright/test';

test.describe('Fahrzeuge', () => {
  test('zeigt Fahrzeugliste mit Filter und Anlegen-Button', async ({ page }) => {
    await Promise.all([
      page.waitForResponse(r => r.url().includes('/api/vehicles?') && r.request().method() === 'GET'),
      page.goto('/vehicles'),
    ]);
    await expect(page.getByRole('columnheader', { name: 'Kennzeichen' })).toBeVisible();
    await expect(page.getByLabel('Außer Dienst zeigen')).not.toBeChecked();
    await expect(page.getByRole('button', { name: 'Neues Fahrzeug' })).toBeVisible();
  });

  test('Fahrzeug anlegen, Fahrtenbuch führen und außer Dienst stellen', async ({ page }) => {
    const name = `PW-Testfahrzeug ${Date.now()}`;

    // Anlegen
    await page.goto('/vehicles');
    await page.getByRole('button', { name: 'Neues Fahrzeug' }).click();
    const createModal = page.getByRole('dialog');
    await expect(createModal.getByRole('button', { name: 'Anlegen' })).toBeDisabled();
    await createModal.getByLabel('Bezeichnung').fill(name);
    await createModal.getByLabel('Kennzeichen').fill('PW-T 112');
    await createModal.getByLabel('Funkrufname').fill('Florian Test 99/1');
    await createModal.getByLabel('Min. Besatzung').fill('3');
    await createModal.getByLabel('Max. Besatzung').fill('9');
    await createModal.getByRole('button', { name: 'Anlegen' }).click();
    await expect(createModal.getByRole('button', { name: 'Anlegen' })).toBeHidden();

    const row = page.getByRole('row').filter({ hasText: name });
    await expect(row).toContainText('PW-T 112');
    await expect(row).toContainText('3 - 9');
    await expect(row).toContainText('Aktiv');

    // Detailseite mit drei Tabs
    await row.click();
    await expect(page).toHaveURL(/\/vehicles\/\d+/);
    await expect(page.getByRole('heading', { name })).toBeVisible();
    for (const tab of ['Fahrzeugdaten', 'Prüfungen', 'Fahrtenbuch']) {
      await expect(page.getByRole('button', { name: tab, exact: true })).toBeVisible();
    }

    // Prüfungstermine speichern
    await page.getByRole('button', { name: 'Prüfungen', exact: true }).click();
    await page.getByLabel('TÜV').fill('2027-05-31');
    const [inspectionSave] = await Promise.all([
      page.waitForResponse(r => /\/api\/vehicles\/\d+\/inspection/.test(r.url()) && r.request().method() === 'PUT'),
      page.getByRole('button', { name: 'Speichern' }).click(),
    ]);
    expect(inspectionSave.ok()).toBeTruthy();
    await page.reload();
    await page.getByRole('button', { name: 'Prüfungen', exact: true }).click();
    await expect(page.getByLabel('TÜV')).toHaveValue('2027-05-31');

    // Fahrtenbuch: Eintrag erfassen, gefahrene km werden berechnet
    await page.getByRole('button', { name: 'Fahrtenbuch', exact: true }).click();
    await expect(page.getByRole('button', { name: 'Drucken' })).toBeDisabled();
    await page.getByRole('button', { name: 'Neuer Eintrag' }).click();
    const logModal = page.getByRole('dialog');
    await expect(logModal.getByText('Neuer Fahrtenbucheintrag')).toBeVisible();
    await expect(logModal.getByRole('button', { name: 'Speichern' })).toBeDisabled();
    await logModal.getByLabel('Datum').fill('2026-10-01');
    await logModal.getByLabel('Fahrer').fill('Playwright');
    await logModal.getByLabel('Zweck').fill('Übungsfahrt');
    await logModal.getByLabel('km-Stand Start').fill('12000');
    // km-Stand Ende unter Start wird direkt im Formular abgelehnt
    await logModal.getByLabel('km-Stand Ende').fill('11999');
    await expect(logModal.getByText('km-Stand Ende darf nicht kleiner als km-Stand Start sein')).toBeVisible();
    await expect(logModal.getByRole('button', { name: 'Speichern' })).toBeDisabled();
    await logModal.getByLabel('km-Stand Ende').fill('12042');
    await expect(logModal.getByText('km-Stand Ende darf nicht kleiner')).toHaveCount(0);
    await logModal.getByRole('button', { name: 'Speichern' }).click();
    await expect(logModal.getByRole('button', { name: 'Speichern' })).toBeHidden();
    const logRow = page.getByRole('row').filter({ hasText: 'Übungsfahrt' });
    await expect(logRow).toContainText('Playwright');
    await expect(logRow).toContainText('42 km');
    await expect(page.getByRole('button', { name: 'Drucken' })).toBeEnabled();

    // Außer Dienst stellen: verschwindet aus der Standardliste
    await page.getByRole('button', { name: 'Fahrzeugdaten', exact: true }).click();
    await page.getByLabel('Außer Dienst', { exact: true }).check();
    const [vehicleSave] = await Promise.all([
      page.waitForResponse(r => /\/api\/vehicles\/\d+$/.test(r.url()) && r.request().method() === 'PUT'),
      page.getByRole('button', { name: 'Speichern' }).click(),
    ]);
    expect(vehicleSave.ok()).toBeTruthy();

    await Promise.all([
      page.waitForResponse(r => r.url().includes('/api/vehicles?') && r.request().method() === 'GET'),
      page.goto('/vehicles'),
    ]);
    await expect(page.getByRole('row').filter({ hasText: name })).toHaveCount(0);
    await page.getByLabel('Außer Dienst zeigen').check();
    await expect(page.getByRole('row').filter({ hasText: name })).toContainText('Außer Dienst');
  });
});
