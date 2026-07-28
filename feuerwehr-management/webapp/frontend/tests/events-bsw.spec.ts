import { test, expect } from '@playwright/test';

async function createEvent(page: import('@playwright/test').Page, name: string, category: string) {
  await page.goto('/events');
  await page.getByRole('button', { name: 'Neue Veranstaltung' }).click();
  await expect(page.getByText('Neue Veranstaltung')).toBeVisible();
  await page.getByLabel('Bezeichnung').fill(name);
  await page.getByLabel('Kategorie').selectOption(category);
  await page.getByLabel('Datum').fill('2026-12-24');
  await page.getByRole('button', { name: 'Anlegen' }).click();
  await expect(page.getByText(name)).toBeVisible();
  await page.getByText(name).click();
}

test.describe('Veranstaltung - Brandsicherheitswache (BSW)', () => {
  test('BSW-Tab erscheint bei Kategorie BSW mit Checkliste und Bericht', async ({ page }) => {
    await createEvent(page, 'Test-BSW-Tab', '3');
    await expect(page.getByRole('button', { name: 'BSW' })).toBeVisible();
    await page.getByRole('button', { name: 'BSW' }).click();
    await expect(page.getByText('Checkliste Brandsicherheitswache')).toBeVisible();
    await expect(page.getByText('Bericht Brandsicherheitswache')).toBeVisible();
  });

  test('BSW-Tab erscheint nicht bei anderen Kategorien', async ({ page }) => {
    await createEvent(page, 'Test-Dienstabend-kein-BSW', '2');
    await expect(page.getByRole('button', { name: 'BSW' })).not.toBeVisible();
  });

  test('Checkliste zeigt alle 18 Punkte mit Ja/Nein-Auswahl', async ({ page }) => {
    await createEvent(page, 'Test-BSW-Checkliste', '3');
    await page.getByRole('button', { name: 'BSW' }).click();
    await expect(page.getByText('Rückmeldung an Feuerwehreinsatzzentrale')).toBeVisible();
    const jaRadios = page.locator('input[type="radio"][name^="bsw-item-"]');
    // 18 Punkte x 2 (ja/nein) = 36 Radiobuttons
    await expect(jaRadios).toHaveCount(36);
  });

  test('kann Formular ausfüllen und speichern', async ({ page }) => {
    await createEvent(page, 'Test-BSW-Formular', '3');
    await page.getByRole('button', { name: 'BSW' }).click();

    await page.locator('input[name="bsw-item-1"]').first().check();
    await page.getByLabel('Bemerkungen').fill('Testbemerkung');
    await page.getByLabel('Veranstaltungsort').fill('Bürgerhalle Heusweiler');
    await page.getByLabel('Wachhabender').fill('Erika Musterfrau');

    await page.getByRole('button', { name: 'Formular speichern' }).click();
    await expect(page.getByLabel('Veranstaltungsort')).toHaveValue('Bürgerhalle Heusweiler');

    // Nach Reload muss der gespeicherte Stand erhalten bleiben
    await page.reload();
    await page.getByRole('button', { name: 'BSW' }).click();
    await expect(page.getByLabel('Veranstaltungsort')).toHaveValue('Bürgerhalle Heusweiler');
    await expect(page.getByLabel('Wachhabender')).toHaveValue('Erika Musterfrau');
    await expect(page.locator('input[name="bsw-item-1"]').first()).toBeChecked();
  });

  test('zeigt zwei PDF-Erzeugen-Buttons mit Rückmeldung', async ({ page }) => {
    await createEvent(page, 'Test-BSW-PDF', '3');
    await page.getByRole('button', { name: 'BSW' }).click();

    const generateButtons = page.getByRole('button', { name: 'Als PDF erzeugen' });
    await expect(generateButtons).toHaveCount(2);

    await generateButtons.first().click();
    await expect(page.getByText(/erzeugt|fehlt|fehlgeschlagen/i)).toBeVisible({ timeout: 15000 });
  });
});
