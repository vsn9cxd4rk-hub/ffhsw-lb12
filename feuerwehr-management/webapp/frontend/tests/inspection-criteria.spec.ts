import { test, expect, type Page } from '@playwright/test';

// "Alle offenen auf io setzen" im Prüfdialog. Eigene Geräteklasse mit fünf Kriterien und eigener
// Artikel (per API angelegt, vom globalTeardown wieder entfernt).

const CRITERIA = ['Sichtprüfung', 'Funktionsprüfung', 'Beschriftung', 'Dichtheit', 'Vollständigkeit'];

async function createTestArticle(page: Page): Promise<number> {
  // The refresh cookie from the login state yields an access token for direct API calls
  const refresh = await page.request.post('/api/auth/refresh');
  expect(refresh.ok()).toBeTruthy();
  const headers = { Authorization: `Bearer ${(await refresh.json()).data.accessToken}` };
  const post = async (url: string, data: object) => {
    const res = await page.request.post(url, { headers, data });
    expect(res.ok(), `${url}: ${await res.text()}`).toBeTruthy();
    return (await res.json()).data;
  };

  const stamp = Date.now();
  const deviceClass = await post('/api/settings/device-classes', { name: `PW-Geräteklasse ${stamp}` });
  const subclass = await post(`/api/settings/device-classes/${deviceClass.id}/subclasses`, { name: 'PW-Unterklasse' });
  for (const [sortOrder, name] of CRITERIA.entries()) {
    await post(`/api/settings/device-subclasses/${subclass.id}/criteria`, { name, sortOrder });
  }
  const article = await post('/api/inventory/articles', {
    name: `PW-Prüfartikel ${stamp}`, deviceSubclassId: subclass.id, inspectionInterval: 12,
  });
  return article.id;
}

async function openInspection(page: Page) {
  const articleId = await createTestArticle(page);
  await page.goto(`/inventory/${articleId}`);
  await page.getByRole('button', { name: 'Prüfung durchführen' }).click();
  const dialog = page.getByRole('dialog');
  await expect(dialog.getByText(CRITERIA[CRITERIA.length - 1])).toBeVisible();
  return dialog;
}

const criterionRow = (dialog: ReturnType<Page['getByRole']>, name: string) =>
  dialog.locator('div.flex.items-center.justify-between').filter({ has: dialog.page().getByText(name, { exact: true }) });

const isSelected = /bg-(green|red)-100/;

test.describe('Prüfung: alle offenen Kriterien auf io', () => {
  test('setzt alle Kriterien mit einem Klick auf io', async ({ page }) => {
    const dialog = await openInspection(page);
    const setAll = dialog.getByRole('button', { name: /^Alle offenen auf io setzen/ });
    await expect(setAll).toBeEnabled();
    await expect(dialog.getByText('Gesamtergebnis:')).toHaveCount(0);

    await setAll.click();
    for (const name of CRITERIA) {
      await expect(criterionRow(dialog, name).getByRole('button', { name: 'io', exact: true })).toHaveClass(/bg-green-100/);
    }
    await expect(dialog.getByText('Bestanden', { exact: true })).toBeVisible();
    await expect(setAll).toBeDisabled();
  });

  test('lässt ein vorher gesetztes nio stehen', async ({ page }) => {
    const dialog = await openInspection(page);
    await criterionRow(dialog, 'Dichtheit').getByRole('button', { name: 'nio', exact: true }).click();
    // Die Anzahl der noch offenen Kriterien steht im Button
    await dialog.getByRole('button', { name: 'Alle offenen auf io setzen (4)' }).click();

    await expect(criterionRow(dialog, 'Dichtheit').getByRole('button', { name: 'nio', exact: true })).toHaveClass(/bg-red-100/);
    await expect(criterionRow(dialog, 'Dichtheit').getByRole('button', { name: 'io', exact: true })).not.toHaveClass(isSelected);
    for (const name of CRITERIA.filter(n => n !== 'Dichtheit')) {
      await expect(criterionRow(dialog, name).getByRole('button', { name: 'io', exact: true })).toHaveClass(/bg-green-100/);
    }
    await expect(dialog.getByText('Nicht bestanden', { exact: true })).toBeVisible();
  });

  test('einzelne Kriterien lassen sich danach noch ändern; gespeichert wird das Ergebnis', async ({ page }) => {
    const dialog = await openInspection(page);
    await dialog.getByRole('button', { name: /^Alle offenen auf io setzen/ }).click();
    await criterionRow(dialog, 'Beschriftung').getByRole('button', { name: 'nio', exact: true }).click();
    await expect(dialog.getByText('Nicht bestanden', { exact: true })).toBeVisible();

    const pruefer = dialog.locator('label:has-text("Prüfer") + select');
    await pruefer.selectOption({ index: 1 });
    const [saved] = await Promise.all([
      page.waitForResponse(r => r.url().endsWith('/api/inspections') && r.request().method() === 'POST'),
      dialog.getByRole('button', { name: 'Prüfung speichern' }).click(),
    ]);
    expect(saved.status()).toBe(201);
    const inspection = (await saved.json()).data;
    expect(inspection.result).toBe('failed');
    expect(inspection.criterionResults).toHaveLength(CRITERIA.length);
    expect(inspection.criterionResults.filter((r: { result: string }) => r.result === 'nio')).toHaveLength(1);
  });
});
