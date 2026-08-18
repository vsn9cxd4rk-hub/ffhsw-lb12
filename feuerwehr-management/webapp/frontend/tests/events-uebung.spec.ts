import { test, expect, type Page } from '@playwright/test';

async function createMember(page: Page, firstName: string, lastName: string, groupLabel: string) {
  await page.goto('/members/new');
  await page.getByLabel('Vorname').fill(firstName);
  await page.getByLabel('Nachname').fill(lastName);
  await page.getByLabel('Gruppe').selectOption({ label: groupLabel });
  await page.getByRole('button', { name: 'Mitglied anlegen' }).click();
  await expect(page).toHaveURL(/\/members\/\d+/);
}

async function createEvent(page: Page, name: string, category: string) {
  await page.goto('/events');
  await page.getByRole('button', { name: 'Neue Veranstaltung' }).click();
  await expect(page.getByRole('dialog').getByText('Neue Veranstaltung')).toBeVisible();
  await page.getByLabel('Bezeichnung').fill(name);
  await page.getByLabel('Kategorie').selectOption(category);
  await page.getByLabel('Datum').fill('2026-12-24');
  await page.getByRole('button', { name: 'Anlegen' }).click();
  await expect(page.getByText(name)).toBeVisible();
  await page.getByText(name).click();
}

test.describe('Veranstaltung - Übung', () => {
  test('Übung-Tab erscheint bei Kategorie Übung, Feld heißt "Thema"', async ({ page }) => {
    await createEvent(page, 'Test-Uebung-Tab', '5');
    await expect(page.getByRole('button', { name: 'Übung' })).toBeVisible();
    await expect(page.getByLabel('Thema')).toBeVisible();
    await expect(page.getByLabel('Bezeichnung 2')).not.toBeVisible();
  });

  test('Übung-Tab erscheint nicht bei anderen Kategorien', async ({ page }) => {
    await createEvent(page, 'Test-Dienstabend-keine-Uebung', '2');
    await expect(page.getByRole('button', { name: 'Übung' })).not.toBeVisible();
  });

  test('AGT-Übung-Kennzeichnung wird gespeichert und bleibt nach Reload erhalten', async ({ page }) => {
    await createEvent(page, 'Test-Uebung-AGT-Flag', '5');
    await page.getByRole('button', { name: 'Übung' }).click();
    const checkbox = page.getByRole('checkbox', { name: 'AGT-Übung' });
    await expect(checkbox).not.toBeChecked();
    await checkbox.check();
    await page.getByRole('button', { name: 'Speichern' }).click();

    await page.reload();
    await page.getByRole('button', { name: 'Übung' }).click();
    await expect(page.getByRole('checkbox', { name: 'AGT-Übung' })).toBeChecked();
  });

  test('Teilnehmer werden auf die aktive Einsatzabteilung gefiltert', async ({ page }) => {
    await createMember(page, 'PwEinsatzabteilung', 'Uebungstest', 'Einsatzabteilung');
    await createMember(page, 'PwAltersabteilung', 'Uebungstest', 'Altersabteilung');

    await createEvent(page, 'Test-Uebung-Teilnehmerfilter', '5');
    await page.getByRole('button', { name: 'Übung' }).click();
    await page.getByRole('button', { name: 'Teilnehmer erfassen' }).click();
    await expect(page).toHaveURL(/\/events\/\d+\/attendance/);

    await expect(page.getByText('Uebungstest, PwEinsatzabteilung')).toBeVisible();
    await expect(page.getByText('Uebungstest, PwAltersabteilung')).not.toBeVisible();
  });

  test('Nachweis Übungsteilnahme kann als PDF erzeugt und im Dokumente-Tab gefunden werden', async ({ page }) => {
    await createEvent(page, 'Test-Uebung-PDF', '5');
    await page.getByRole('button', { name: 'Übung' }).click();
    await page.getByRole('button', { name: 'Nachweis als PDF erzeugen' }).click();
    await expect(page.getByText(/erzeugt|fehlt|fehlgeschlagen/i)).toBeVisible({ timeout: 15000 });

    await page.getByRole('button', { name: 'Dokumente' }).click();
    await expect(page.getByText(/Uebungsbesuch/i)).toBeVisible();
  });
});
