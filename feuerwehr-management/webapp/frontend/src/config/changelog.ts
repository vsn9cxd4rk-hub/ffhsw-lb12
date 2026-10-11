// Release notes shown in the About dialog (click on the version number).
// Newest version first. Keep in sync with CHANGELOG.md in the project root, which
// additionally contains the technical update steps for administrators.

export interface ChangelogSection {
  title: string;
  items: string[];
}

export interface ChangelogEntry {
  version: string;
  /** Version the changes are compared to */
  previousVersion: string;
  date: string; // YYYY-MM-DD
  summary: string;
  sections: ChangelogSection[];
  /** Only shown to administrators (update steps, operating notes) */
  adminNotes?: string[];
}

export const CHANGELOG: ChangelogEntry[] = [
  {
    version: '1.4.0',
    previousVersion: '1.3.2',
    date: '2026-10-09',
    summary: 'Sicherheits-Update: Berechtigungen für Veranstaltungen und Ausbildung, Schutz von Gesundheitsdaten, erzwungener Passwortwechsel.',
    sections: [
      {
        title: 'Neu',
        items: [
          'Neue Berechtigungen „Veranstaltungen pflegen“ und „Ausbildung pflegen“ (Einstellungen → Berechtigungsgruppen). Ansehen können weiterhin alle angemeldeten Benutzer.',
          'Ohne diese Berechtigung werden Veranstaltungen, Anwesenheit, BSW-Formulare und Lehrgänge schreibgeschützt angezeigt.',
          'Vom Administrator vergebene oder zurückgesetzte Passwörter müssen beim nächsten Login durch ein eigenes ersetzt werden. Die Benutzerverwaltung zeigt „Passwortwechsel offen“ an.',
          'Bei der Erstinstallation erhält der Benutzer „admin“ ein zufälliges Startpasswort statt eines festen Standardpassworts.',
          'Diese Übersicht der Änderungen: Klick auf die Versionsnummer im Dialog „Über diese Anwendung“.',
        ],
      },
      {
        title: 'Geändert',
        items: [
          'Gruppen mit Einsatz-Berechtigung (z.B. Gruppenführer) erhalten beim Update automatisch beide neuen Berechtigungen; andere Gruppen sehen Veranstaltungen und Ausbildung nur noch lesend.',
          'Lehrgangskategorien können nur noch Administratoren ändern.',
          'Untersuchungstermine (G25/G26/G30) erscheinen im Dashboard und in den Benachrichtigungen nur noch für Administratoren.',
          'Die Mitgliederauswahl in Einsätzen und Lehrgängen zeigt nur noch Name, Dienstgrad, Gruppe und Qualifikationen.',
          'Hochgeladene Dateien werden unter zufälligen Namen gespeichert; der ursprüngliche Dateiname bleibt für die Anzeige erhalten. Umlaute in Dateinamen werden jetzt korrekt angezeigt.',
          'Die PDF-Erzeugung (Einsatzbericht, Kräftenachweis, BSW, Übungsnachweis) blockiert den Server nicht mehr – andere Benutzer können währenddessen weiterarbeiten.',
          'Ein neues Passwort muss sich vom bisherigen unterscheiden.',
        ],
      },
      {
        title: 'Sicherheit',
        items: [
          'Hochgeladene Dateien sind nicht mehr ohne Anmeldung über /uploads/ abrufbar.',
          'Uploads werden auf ihren tatsächlichen Dateityp geprüft (z.B. keine als PDF getarnte HTML-Datei mehr).',
          'Druckansichten (Fahrtenbuch, QR- und Barcode-Etiketten) sind gegen eingeschleusten Code geschützt.',
          'Schutz gegen Einschleusen von Befehlen über Veranstaltungsnamen bei der PDF-Erzeugung.',
          'Zusätzliche Sicherheits-Header im Browser (Content-Security-Policy, Schutz vor Einbetten in fremde Seiten).',
          'Aktualisierte Programmbibliotheken mit bekannten Sicherheitslücken (u.a. E-Mail-Versand).',
        ],
      },
    ],
    adminNotes: [
      'Vor dem Start des neuen Backends die Datenbank-Migration ausführen: deploy/migrate-1.3.x-to-1.4.0.sql (update.sh erledigt das automatisch).',
      'Das Backend startet nur noch mit einem gültigen JWT_ACCESS_SECRET (mindestens 32 Zeichen, kein Platzhalter).',
      'Konten mit dem früheren Standardpasswort müssen es beim nächsten Login ändern.',
      'nginx-Konfiguration ersetzen bzw. den Block „location /uploads/“ entfernen; Security-Header-Snippet siehe deploy/README.md.',
      'E2E-Tests benötigen jetzt die Umgebungsvariable E2E_ADMIN_PASSWORD.',
      'Schritt-für-Schritt-Anleitung für Produktiv- und Backup-System: deploy/UPDATE-MANJARO.md.',
    ],
  },
];

/** Entry of the running version, falling back to the newest one. */
export function currentChangelogEntry(version: string): ChangelogEntry | undefined {
  return CHANGELOG.find((e) => e.version === version) ?? CHANGELOG[0];
}
