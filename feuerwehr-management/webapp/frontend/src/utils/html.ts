/**
 * Escapes text for interpolation into HTML strings (print windows written via document.write).
 * Print windows share the app's origin, so unescaped user input there runs as script in the
 * session of whoever prints.
 */
export function escapeHtml(value: unknown): string {
  if (value === null || value === undefined) return '';
  return String(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

/**
 * Opens a print window with the given document and triggers printing once it has rendered.
 * All dynamic values in `html` must be passed through escapeHtml.
 */
export function openPrintWindow(html: string, features?: string): void {
  const win = window.open('', '_blank', features);
  if (!win) return;
  win.opener = null;
  win.document.write(html);
  win.document.close();
  setTimeout(() => { win.print(); }, 300);
}
