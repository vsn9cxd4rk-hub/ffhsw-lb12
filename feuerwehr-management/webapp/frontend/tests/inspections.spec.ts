import { test, expect } from '@playwright/test';

test.describe('Prüfbuch', () => {
  test('zeigt drei Tabs', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByRole('button', { name: 'Fällige Prüfungen' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Prüfhistorie' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Berichte' })).toBeVisible();
  });

  test('zeigt Suchfeld', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByPlaceholder(/Inv.-Nr/)).toBeVisible();
  });

  test('zeigt Geräteklassen-Filter im Due-Tab', async ({ page }) => {
    await page.goto('/inspections');
    await expect(page.getByText('Geräteklasse').first()).toBeVisible();
    await expect(page.getByText('Unterklasse').first()).toBeVisible();
  });

  test('öffnet Prüfungsdialog', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfung dokumentieren' }).click();
    await expect(page.getByText('Prüfung dokumentieren')).toBeVisible();
    await expect(page.getByLabel(/Prüfdatum/)).toBeVisible();
    await expect(page.locator('label:has-text("Prüfer") + select')).toBeVisible();
    await expect(page.getByText('Prüfart', { exact: true })).toBeVisible();
  });

  test('Prüfer ist eine Auswahlliste der Gerätewarte', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfung dokumentieren' }).click();
    const pruefer = page.locator('label:has-text("Prüfer") + select');
    await expect(pruefer).toBeVisible();
    // Platzhalter + mindestens ein Gerätewart als Option (nicht frei eintippbar)
    await expect(pruefer.locator('option')).not.toHaveCount(0);
    await expect(pruefer.getByRole('option', { name: '-- Prüfer wählen --' })).toBeAttached();
  });

  test('Prüfhistorie Tab wechselt', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfhistorie' }).click();
    await page.waitForTimeout(500);
    const table = page.locator('table');
    await expect(table).toBeVisible();
  });

  test('Berichte Tab zeigt Filter', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Berichte' }).click();
    await expect(page.getByText('PDF Export')).toBeVisible();
  });
});

test.describe('Mangel aus Prüfung erfassen', () => {
  test('zeigt "Mangel erfassen" im Prüfdialog mit vorausgewähltem Artikel', async ({ page }) => {
    await page.goto('/inventory');
    // Prüfung für die erste Artikelzeile starten -> Dialog mit vorausgewähltem Artikel
    await page.getByTitle('Prüfung durchführen').first().click();
    await expect(page.getByText('Prüfung dokumentieren')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Mangel erfassen' })).toBeVisible();
  });

  test('ist ohne ausgewählten Artikel nicht sichtbar', async ({ page }) => {
    await page.goto('/inspections');
    await page.getByRole('button', { name: 'Prüfung dokumentieren' }).click();
    await expect(page.getByText('Prüfung dokumentieren')).toBeVisible();
    // Kein Artikel gewählt -> Button darf nicht erscheinen
    await expect(page.getByRole('button', { name: 'Mangel erfassen' })).toHaveCount(0);
  });

  test('öffnet Mangel-Formular vorbelegt aus der Prüfung', async ({ page }) => {
    await page.goto('/inventory');
    await page.getByTitle('Prüfung durchführen').first().click();
    await expect(page.getByText('Prüfung dokumentieren')).toBeVisible();

    // Prüfer aus der Auswahlliste wählen -> soll ins Mangel-Formular übernommen werden
    const prueferSelect = page.locator('label:has-text("Prüfer") + select');
    const optionLabels = await prueferSelect.locator('option').allInnerTexts();
    // Ersten echten Prüfer (nicht den Platzhalter) auswählen
    const prueferName = optionLabels.find(l => l && !l.startsWith('--')) || '';
    await prueferSelect.selectOption({ label: prueferName });

    await page.getByRole('button', { name: 'Mangel erfassen' }).click();

    // Mangel-Dialog offen mit eigenen Feldern
    await expect(page.getByRole('button', { name: 'Mangel speichern' })).toBeVisible();
    await expect(page.getByLabel(/Beschreibung/)).toBeVisible();
    await expect(page.getByLabel(/Gemeldet von/)).toHaveValue(prueferName);
  });
});
