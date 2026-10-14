import { test, expect } from '@playwright/test';

test.describe('Ausbildung', () => {
  test('zeigt Lehrgangsliste mit Filtern', async ({ page }) => {
    await page.goto('/training');
    await expect(page.getByPlaceholder('Nach Mitglied suchen...')).toBeVisible();
    await expect(page.locator('select').filter({ hasText: 'Alle Lehrgänge' })).toBeVisible();
    const statusFilter = page.locator('select').filter({ hasText: 'Alle Status' });
    for (const label of ['Geplant', 'Laufend', 'Abgeschlossen', 'Nicht bestanden']) {
      await expect(statusFilter.getByRole('option', { name: label })).toBeAttached();
    }
    await expect(page.getByRole('button', { name: 'Neuer Lehrgang' })).toBeVisible();
  });

  test('Lehrgang erfassen, filtern und abschließen', async ({ page }) => {
    // Eigenes Mitglied, damit der Lehrgang eindeutig gefunden wird
    const lastName = `PwLehrgang${Date.now()}`;
    await page.goto('/members/new');
    await page.getByLabel('Vorname').fill('Lea');
    await page.getByLabel('Nachname').fill(lastName);
    await page.getByRole('button', { name: 'Mitglied anlegen' }).click();
    await expect(page).toHaveURL(/\/members\/\d+/);

    await page.goto('/training');
    await page.getByRole('button', { name: 'Neuer Lehrgang' }).click();
    const modal = page.getByRole('dialog');
    await expect(modal.getByText('Neuer Lehrgang')).toBeVisible();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeDisabled();

    const [memberSelect, categorySelect] = [modal.locator('select').nth(0), modal.locator('select').nth(1)];
    await memberSelect.selectOption({ label: `${lastName}, Lea` });
    await categorySelect.selectOption({ index: 1 });
    const categoryName = (await categorySelect.locator('option:checked').innerText()).trim();
    await modal.getByLabel('Beginn').fill('2026-09-01');
    await modal.getByLabel('Ende').fill('2026-09-12');
    await modal.getByLabel('Ort').fill('Playwright-Schule');
    await modal.getByRole('button', { name: 'Speichern' }).click();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeHidden();

    // Suche nach dem Mitglied
    await page.getByPlaceholder('Nach Mitglied suchen...').fill(lastName);
    const row = page.getByRole('row').filter({ hasText: lastName });
    await expect(row).toHaveCount(1);
    await expect(row).toContainText(categoryName);
    await expect(row).toContainText('Geplant');
    await expect(row).toContainText('01.09.2026 - 12.09.2026');
    await expect(row).toContainText('Playwright-Schule');

    // Bearbeiten: Mitglied ist gesperrt, Status auf abgeschlossen
    await row.click();
    await expect(modal.getByText('Lehrgang bearbeiten')).toBeVisible();
    await expect(modal.locator('select').nth(0)).toBeDisabled();
    await expect(modal.getByRole('button', { name: 'Urkunde hochladen' })).toBeVisible();
    await modal.getByLabel('Status').selectOption({ label: 'Abgeschlossen' });
    await modal.getByRole('button', { name: 'Speichern' }).click();
    await expect(modal.getByRole('button', { name: 'Speichern' })).toBeHidden();
    await expect(row).toContainText('Abgeschlossen');

    // Statusfilter
    const statusFilter = page.locator('select').filter({ hasText: 'Alle Status' });
    await statusFilter.selectOption({ label: 'Geplant' });
    await expect(page.getByRole('row').filter({ hasText: lastName })).toHaveCount(0);
    await statusFilter.selectOption({ label: 'Abgeschlossen' });
    await expect(page.getByRole('row').filter({ hasText: lastName })).toHaveCount(1);
  });
});
