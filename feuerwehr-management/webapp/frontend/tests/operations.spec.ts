import { test, expect } from '@playwright/test';

test.describe('Einsätze', () => {
  test('zeigt Einsatz-Übersicht', async ({ page }) => {
    await page.goto('/operations');
    await expect(page.getByRole('heading', { name: /Eins/i })).toBeVisible();
    await expect(page.getByRole('button', { name: /Neuer Einsatz/i })).toBeVisible();
  });

  test('kann neuen Einsatz anlegen', async ({ page }) => {
    await page.goto('/operations');
    await page.getByRole('button', { name: /Neuer Einsatz/i }).click();
    await expect(page.getByRole('heading', { name: 'Neuer Einsatz' })).toBeVisible();
    // Eindeutiger Einsatzort, damit wiederholte Testläufe keine Duplikate
    // erzeugen (sonst strict-mode-Violation beim Wiederfinden in der Liste).
    const ort = `Testort - Teststrasse ${Date.now()}`;
    await page.getByLabel('Datum').fill('2026-07-21');
    await page.getByLabel('Einsatzort').fill(ort);
    await page.getByRole('button', { name: /Anlegen/i }).click();
    await expect(page.getByText(ort).first()).toBeVisible();
  });

  test('zeigt alle Tabs in Einsatz-Detail', async ({ page }) => {
    await page.goto('/operations');
    // Click on first operation if exists
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await expect(page.getByRole('button', { name: 'Details' })).toBeVisible();
      await expect(page.getByRole('button', { name: 'Fahrzeugzeiten' })).toBeVisible();
      await expect(page.getByRole('button', { name: 'Kräfte' })).toBeVisible();
      // The free-text tab "Bericht" was removed in 1.4.0 (the report is generated from the details)
      await expect(page.getByRole('button', { name: 'Bericht', exact: true })).toHaveCount(0);
      await expect(page.getByRole('button', { name: 'Dokumente' })).toBeVisible();
    }
  });

  test('Details-Tab zeigt Einsatzbericht-Felder', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await expect(page.getByText('Einsatzbericht-Details')).toBeVisible();
      await expect(page.locator('label:has-text("Berichtsart") + select')).toBeVisible();
      await expect(page.locator('label:has-text("Einsatzstichwort") + select')).toBeVisible();
      await expect(page.getByLabel('ILS Auftragsnummer')).toBeVisible();
      await expect(page.getByLabel('Ersteller')).toBeVisible();
      await expect(page.getByLabel('Rolle des Erstellers')).toBeVisible();
      await expect(page.getByText('Einsatzart')).toBeVisible();
      await expect(page.getByText('Statistische Angaben')).toBeVisible();
    }
  });

  test('Details: keine Einsatz-Zeiten, Einsatzleiter-Feld nur für Einheitenführer', async ({ page }) => {
    await page.goto('/operations');
    await page.getByRole('button', { name: /Neuer Einsatz/i }).click();
    await page.getByLabel('Datum').fill('2026-07-21');
    await page.getByLabel('Einsatzort').fill(`Testort - Einsatzleiter ${Date.now()}`);
    // Navigate straight to the new operation instead of searching the (paginated) list
    const [resp] = await Promise.all([
      page.waitForResponse((r) => r.url().includes('/api/operations') && r.request().method() === 'POST'),
      page.getByRole('button', { name: /Anlegen/i }).click(),
    ]);
    await page.goto(`/operations/${(await resp.json()).data.id}`);

    // Times are maintained per vehicle only
    await expect(page.getByText('Einsatzbericht-Details')).toBeVisible();
    for (const label of ['Alarmzeit', 'Ausgerückt', 'Eingetroffen', 'Eingerückt']) {
      await expect(page.getByLabel(label, { exact: true })).toHaveCount(0);
    }

    const role = page.getByLabel('Rolle des Erstellers');
    const save = page.getByRole('button', { name: 'Speichern' });
    await expect(page.getByLabel('Einsatzleiter', { exact: true })).toHaveCount(0);

    await role.selectOption('Einheitenführer LB Wahlschied');
    const commander = page.getByLabel(/^Einsatzleiter/);
    await expect(commander).toBeVisible();
    await expect(save).toBeDisabled();

    await commander.fill('Erika Extern');
    await expect(save).toBeEnabled();
    const [saved] = await Promise.all([
      page.waitForResponse((r) => r.url().includes('/api/operations/') && r.request().method() === 'PUT'),
      save.click(),
    ]);
    expect((await saved.json()).data.commanderName).toBe('Erika Extern');

    await role.selectOption('Einsatzleiter');
    await expect(page.getByLabel(/^Einsatzleiter/)).toHaveCount(0);
  });

  test('Einsatzstichwort bietet Dropdown-Auswahl', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      const stichwort = page.locator('label:has-text("Einsatzstichwort") + select');
      await expect(stichwort).toBeVisible();
      // Check it's a select with options
      const options = stichwort.locator('option');
      const count = await options.count();
      expect(count).toBeGreaterThan(10);
    }
  });

  test('Einsatzart-Checkboxen sind vorhanden', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await expect(page.getByLabel('Kleinbrand a')).toBeVisible();
      await expect(page.getByLabel('Brandmelde Anlage')).toBeVisible();
      await expect(page.getByLabel('Techn. Hilfeleist.')).toBeVisible();
    }
  });
});

test.describe('Einsatz - Kräfte-Tab', () => {
  test('zeigt Kräfte-Sektionen', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await page.getByRole('button', { name: 'Kräfte' }).click();
      await expect(page.getByText(/Fahrzeug |Noch keine Fahrzeuge erfasst/).first()).toBeVisible();
      await expect(page.getByText('Nachgerückte Kräfte')).toBeVisible();
    }
  });

  test('kann Einsatzkraft hinzufügen', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await page.getByRole('button', { name: 'Kräfte' }).click();
      const addButtons = page.getByRole('button', { name: 'Hinzufügen' });
      await addButtons.first().click();
      await expect(page.getByText('Einsatzkraft hinzufügen')).toBeVisible();
      await expect(page.locator('label:has-text("Mitglied") + select')).toBeVisible();
      await expect(page.locator('label:has-text("Funktion") + select')).toBeVisible();
      await expect(page.getByRole('dialog').getByText('Fahrzeug', { exact: true })).toBeVisible();
    }
  });
});

test.describe('Einsatz - Dokumente & Generierung', () => {
  test('zeigt Generierungs-Buttons', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await page.getByRole('button', { name: 'Dokumente' }).click();
      await expect(page.getByRole('button', { name: /Einsatzbericht generieren/i })).toBeVisible();
      await expect(page.getByRole('button', { name: /Kräftenachweis generieren/i })).toBeVisible();
    }
  });

  test('Einsatzbericht-Generierung startet', async ({ page }) => {
    await page.goto('/operations');
    const firstRow = page.locator('table tbody tr').first();
    if (await firstRow.isVisible()) {
      await firstRow.click();
      await page.getByRole('button', { name: 'Dokumente' }).click();
      await page.getByRole('button', { name: /Einsatzbericht generieren/i }).click();
      // Should show success or error message (not just stay silent)
      await expect(page.getByText(/generiert|fehlt|fehlgeschlagen/i).first()).toBeVisible({ timeout: 15000 });
    }
  });
});
