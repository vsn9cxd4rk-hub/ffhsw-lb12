import { test, expect, type Page } from '@playwright/test';

// S2 Lage: Lage anlegen, Abschnitt und Kräfte erfassen, EA-Grenze und Zeichen auf die Karte,
// Lagebericht fortschreiben und exportieren; zwei Bearbeiter gleichzeitig am selben Bericht.

async function createLage(page: Page): Promise<string> {
  await page.goto('/s2');
  await page.getByRole('button', { name: 'Neue Lage' }).click();
  await page.getByLabel('Bezeichnung').fill(`PW-Lage ${Date.now()}`);
  await page.getByRole('button', { name: 'Anlegen' }).click();
  await expect(page).toHaveURL(/\/s2\/\d+/);
  return page.url().split('?')[0];
}

test.describe('S2 Lage', () => {
  test('Abschnitt, Kräfte, Lagekarte und Lagebericht mit Export', async ({ page }) => {
    test.setTimeout(90_000);
    const url = await createLage(page);
    await expect(page.getByText('Übung', { exact: true }).first()).toBeVisible();

    // Abschnitte & Kräfte
    await page.getByRole('button', { name: 'Abschnitte & Kräfte' }).click();
    await page.getByRole('button', { name: 'Abschnitt anlegen' }).click();
    await page.getByLabel('Name').fill('Heusweiler');
    await page.getByLabel(/Raumbezogener Schwerpunkt/).check();
    await page.getByRole('button', { name: 'Speichern' }).click();
    await expect(page.getByText('EA 1: Heusweiler')).toBeVisible();

    await page.getByLabel('Neue Kraft Einheit').fill('LB 111');
    await page.getByLabel('Neue Kraft Stärke').fill('1/8/9');
    await page.getByLabel('Neue Kraft Status').selectOption('einsatz');
    await page.getByRole('button', { name: 'Hinzufügen' }).click();
    await page.getByLabel('Neue Kraft Einheit').fill('LB 118');
    await page.getByLabel('Neue Kraft Stärke').fill('1/6');
    await page.getByLabel('Neue Kraft Status').selectOption('grundschutz');
    await page.getByRole('button', { name: 'Hinzufügen' }).click();
    await expect(page.getByTestId('s2-staerke-gesamt')).toHaveText('16');

    // Lagekarte: EA-Grenze mit drei Klicks, Zeichen über Gitterwert
    await page.getByRole('button', { name: 'Lagekarte' }).click();
    const karte = page.getByTestId('s2-karte');
    await expect(karte).toBeVisible();
    await page.getByRole('button', { name: 'EA-Grenze' }).click();
    await page.getByLabel('Einsatzabschnitt').selectOption({ label: 'EA 1: Heusweiler' });
    const box = (await karte.boundingBox())!;
    for (const [fx, fy] of [[0.4, 0.45], [0.5, 0.45], [0.45, 0.55]]) {
      await karte.click({ position: { x: box.width * fx, y: box.height * fy } });
    }
    await page.getByRole('button', { name: 'Fertig' }).click();
    await expect(page.getByRole('button', { name: 'Grenze EA 1 entfernen' })).toBeVisible();

    await page.getByRole('button', { name: 'Zeichen', exact: true }).click();
    await page.getByLabel('Zeichen suchen').fill('Bereitstellungsraum');
    await page.getByRole('button', { name: /^Bereitstellungsraum$/ }).click();
    await page.getByLabel(/Ort: Gitter/).fill('349,6/5467,2');
    await page.getByRole('button', { name: 'Suchen' }).click();
    await page.getByRole('button', { name: /Zeichen hier setzen/ }).click();
    await expect(page.getByText(/^Bereitstellungsraum$/).last()).toBeVisible();

    // Lagebericht Nr. 1
    await page.getByRole('button', { name: 'Lagebericht', exact: true }).click();
    await page.getByRole('button', { name: '1. Lagebericht anlegen' }).click();
    await expect(page.getByRole('heading', { name: 'Lagebericht Nr. 1' })).toBeVisible();
    await page.getByLabel('Zeit', { exact: true }).fill('141100nov26');
    await page.getByRole('button', { name: 'Aus Kräfteübersicht übernehmen' }).click();
    await expect(page.getByLabel('Stärke gesamt')).toHaveValue('16');
    await expect(page.getByLabel('Im Einsatz')).toHaveValue('FW: LB 111');
    await page.getByLabel('Schadenslage Stufe 2').check();
    await page.getByLabel('Schadenslage Prognose').selectOption('Stagnation');
    await page.getByLabel('Kurzfassung').fill('Allgemein: Starkregen, Keller überflutet\nEA Heusweiler: Pumparbeiten');
    await page.getByRole('button', { name: 'Speichern', exact: true }).click();
    await expect(page.getByText(/gespeichert von/)).toBeVisible();

    await expect(page.getByTestId('s2-mail-betreff')).toHaveText(/^ÜBUNG – ÜBUNG – ÜBUNG \| Lagebericht Gemeinde Heusweiler Nr\. 1 – 141100nov26$/);
    const [xlsx] = await Promise.all([page.waitForEvent('download'), page.getByRole('button', { name: 'Lagebericht (Excel)' }).click()]);
    expect(xlsx.suggestedFilename()).toBe('Lagebericht_Heusweiler_141100nov26_V1.xlsx');
    const [pptx] = await Promise.all([page.waitForEvent('download'), page.getByRole('button', { name: 'Lagekarte (PowerPoint)' }).click()]);
    expect(pptx.suggestedFilename()).toBe('Lagekarte_Heusweiler_141100nov26_V1.pptx');

    // Fortschreibung: Nr. 2 übernimmt die Werte
    await page.getByRole('button', { name: 'Fortschreiben (Nr. 2)' }).click();
    await expect(page.getByRole('heading', { name: 'Lagebericht Nr. 2' })).toBeVisible();
    await expect(page.getByLabel('Kurzfassung')).toHaveValue(/Starkregen/);
    expect(url).toMatch(/\/s2\/\d+$/);
  });

  test('zwei Bearbeiter: veralteter Stand überschreibt nichts', async ({ page, context }) => {
    const url = await createLage(page);
    await page.getByRole('button', { name: '1. Lagebericht anlegen' }).click();
    await expect(page.getByRole('heading', { name: 'Lagebericht Nr. 1' })).toBeVisible();

    const other = await context.newPage();
    await other.goto(url);
    await expect(other.getByRole('heading', { name: 'Lagebericht Nr. 1' })).toBeVisible();

    // A speichert, B hat gleichzeitig ungespeicherte Änderungen auf dem alten Stand
    await other.getByLabel('Presse').fill('Anfrage SR');
    await page.getByLabel('Kurzfassung').fill('Allgemein: Version A');
    await page.getByRole('button', { name: 'Speichern', exact: true }).click();
    await expect(page.getByText(/gespeichert von/)).toBeVisible();

    await expect(other.getByRole('alert')).toContainText('inzwischen geändert', { timeout: 15_000 });
    await other.getByRole('button', { name: 'Speichern', exact: true }).click();
    await expect(other.getByText(/zwischenzeitlich .*geändert/)).toBeVisible();
    await other.getByRole('button', { name: 'Eigene Änderungen verwerfen und neu laden' }).click();
    await expect(other.getByLabel('Kurzfassung')).toHaveValue('Allgemein: Version A');
    await expect(other.getByLabel('Presse')).toHaveValue('');
  });
});
