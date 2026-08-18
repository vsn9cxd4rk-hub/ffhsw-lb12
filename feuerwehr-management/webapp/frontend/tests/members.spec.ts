import { test, expect, type Page } from '@playwright/test';

function daysAgo(days: number): string {
  const d = new Date();
  d.setDate(d.getDate() - days);
  return d.toISOString().substring(0, 10);
}

async function createMember(page: Page, firstName: string, lastName: string, groupLabel?: string) {
  await page.goto('/members/new');
  await page.getByLabel('Vorname').fill(firstName);
  await page.getByLabel('Nachname').fill(lastName);
  if (groupLabel) {
    await page.getByLabel('Gruppe').selectOption({ label: groupLabel });
  }
  await page.getByRole('button', { name: 'Mitglied anlegen' }).click();
  await expect(page).toHaveURL(/\/members\/\d+/);
}

test.describe('Mitgliederakte - Abwesenheiten', () => {
  test('Abwesenheit erfassen, bearbeiten und löschen', async ({ page }) => {
    await createMember(page, 'PwAbwesenheit', 'Testmitglied');
    await page.getByRole('button', { name: 'Abwesenheiten' }).click();
    await expect(page.getByText('Keine Abwesenheiten erfasst.')).toBeVisible();

    await page.getByRole('button', { name: 'Abwesenheit erfassen' }).click();
    const addModal = page.getByRole('dialog');
    await expect(addModal.getByText('Abwesenheit erfassen')).toBeVisible();
    await addModal.getByLabel('Datum').fill(daysAgo(10));
    await addModal.getByRole('combobox').selectOption({ label: 'Krank' });
    await addModal.getByLabel('Notiz').fill('Playwright-Test');
    await addModal.getByRole('button', { name: 'Speichern' }).click();
    await expect(addModal).not.toBeVisible();

    await expect(page.getByRole('cell', { name: 'Krank' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Playwright-Test' })).toBeVisible();

    // Bearbeiten: Grund ändern
    await page.getByRole('cell', { name: 'Playwright-Test' }).click();
    const editModal = page.getByRole('dialog');
    await expect(editModal.getByText('Abwesenheit bearbeiten')).toBeVisible();
    await editModal.getByRole('combobox').selectOption({ label: 'Urlaub' });
    await editModal.getByRole('button', { name: 'Speichern' }).click();
    await expect(editModal).not.toBeVisible();
    await expect(page.getByRole('cell', { name: 'Urlaub' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Krank' })).not.toBeVisible();

    // Löschen
    const row = page.getByRole('row').filter({ hasText: 'Playwright-Test' });
    await row.getByRole('button').click();
    await expect(page.getByText('Keine Abwesenheiten erfasst.')).toBeVisible();
  });
});

test.describe('Mitgliederakte - AGT-Tauglichkeit', () => {
  test('G26-Datum aus Untersuchungen wird automatisch für die Tauglichkeit berücksichtigt', async ({ page }) => {
    await createMember(page, 'PwAgt', 'Testmitglied');
    await page.getByRole('button', { name: 'Untersuchungen' }).click();

    await expect(page.getByText('AGT nicht tauglich')).toBeVisible();
    await expect(page.getByText('Keine gültige G26 Untersuchung vorhanden')).toBeVisible();

    await page.getByLabel('G26 (Atemschutz)').fill(daysAgo(30));
    await page.getByRole('button', { name: 'Speichern' }).click();

    // G26-Grund darf nicht mehr auftauchen, ohne dass ein separater Nachweis-Eintrag nötig war
    await expect(page.getByText('Keine gültige G26 Untersuchung vorhanden')).not.toBeVisible();
    // Die übrigen, unabhängigen AGT-Anforderungen bleiben offen (kein Nachweis erfasst)
    await expect(page.getByText('Belastungsübung älter als 1 Jahr')).toBeVisible();

    // Kein Nachweis-Eintrag wurde durch das Speichern des G26-Felds angelegt
    await expect(page.getByText('Keine AGT-Nachweise vorhanden')).toBeVisible();
  });
});
