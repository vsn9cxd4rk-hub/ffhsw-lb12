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
    date: '2026-10-14',
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
          'Geräteprüfung: Der Button „Alle offenen auf io setzen“ bewertet alle noch offenen Prüfkriterien mit einem Klick. Bereits als nio markierte Kriterien bleiben unverändert – also erst Mängel markieren, dann den Rest bestätigen.',
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
          'Die Anmeldung endet nicht mehr nach einer festen Zeit ab dem Login. Jede Nutzung verlängert sie; abgemeldet wird erst nach 7 Tagen ohne Nutzung.',
          'Ein lange offener Dialog (z.B. Geräteprüfung) verliert seine Eingaben nicht mehr, wenn der Server kurz nicht erreichbar ist. Es erscheint eine Fehlermeldung und man kann erneut speichern.',
          'Statistik-Diagramme und Oberflächen-Styling auf neue Hauptversionen umgestellt (Recharts 3, Tailwind CSS 4).',
          'Einsatz-Details: Die Zeiten (Alarm, Ausgerückt, Eingetroffen, Eingerückt) werden nur noch pro Fahrzeug im Tab „Fahrzeugzeiten“ gepflegt. Die Alarmzeit des Einsatzes ist die früheste Fahrzeug-Alarmzeit und wird beim Anlegen eines Fahrzeugs vorgeschlagen.',
          'Dashboard: Überfällige Termine (TÜV, SP, Service, Geräteprüfungen am Fahrzeug, Untersuchungen, LKW-Führerschein) verschwinden nicht mehr, sobald sie abgelaufen sind. Sie bleiben als „Überfällig“ stehen, bis der Termin erneuert ist. Ausgemusterte Fahrzeuge und inaktive Mitglieder werden nicht aufgeführt.',
          'Kräftenachweis: Bei mehr als 9 Kräften je Abschnitt werden Folgeblätter erzeugt. Bisher fehlten alle Kräfte ab der zehnten ohne Hinweis.',
          'Geräteprüfung: Die nächste Fälligkeit wird am Monatsende richtig berechnet (31.08. + 6 Monate ist der 28.02., nicht der 03.03.).',
          'Einsatzbericht: Ist der Ersteller Einsatzleiter, steht er jetzt auch im Feld „Einsatzleiter“ (blieb bisher leer). Ist er Einheitenführer, wird der tatsächliche Einsatzleiter in einem eigenen Feld erfasst (mit Vorschlägen aus der Mitgliederliste, auch für Einsatzleiter anderer Löschbezirke).',
          'Der Dienstgrad im Einsatzbericht wird nur noch bei exakt passendem Vor- und Nachnamen ergänzt (vorher konnte der Dienstgrad eines Namensvetters erscheinen).',
          'Der Tab „Bericht“ beim Einsatz entfällt. Der Einsatzbericht wird aus den Angaben unter „Details“ erzeugt (Tab „Dokumente“).',
          'Geräteprüfung: Beim Ändern der nächsten Fälligkeit war das Datum teilweise einen Tag zu früh vorbelegt.',
          'Neue Benutzer können wieder ohne E-Mail-Adresse angelegt werden.',
          'Benutzer werden erst nach einer Rückfrage gelöscht; das eigene Konto kann man nicht mehr löschen.',
          'Fahrtenbuch: Ein km-Stand am Ende, der unter dem km-Stand am Start liegt, wird abgelehnt.',
          'Die vorgeschlagene nächste Inventarnummer folgt der eigenen Nummernfolge und nicht mehr Hersteller- oder Seriennummern.',
          'CSV-Import von Artikeln: Steht eine Inventarnummer mehrfach in der Datei, wird sie nur einmal übernommen und die Wiederholung als übersprungen gezählt.',
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
          'Aktualisierte Programmbibliotheken: keine bekannten Sicherheitslücken und keine als veraltet markierten Bibliotheken mehr (u.a. E-Mail-Versand, Excel-Export).',
        ],
      },
    ],
    adminNotes: [
      'Die Oberfläche benötigt einen aktuellen Browser (Chrome/Edge ab 111, Firefox ab 128, Safari ab 16.4). Rechner mit Windows 7/8 werden nicht mehr unterstützt.',
      'Der Server benötigt Node.js 20 oder neuer.',
      'Vor dem Start des neuen Backends die Datenbank-Migration ausführen: deploy/migrate-1.3.x-to-1.4.0.sql (update.sh erledigt das automatisch).',
      'Das Backend startet nur noch mit einem gültigen JWT_ACCESS_SECRET (mindestens 32 Zeichen, kein Platzhalter). JWT_REFRESH_SECRET wird nicht verwendet und kann aus der .env entfernt werden.',
      'Dateipfade der Uploads werden beim ersten Start automatisch relativ zu UPLOAD_PATH gespeichert. Ein Datenbank-Backup funktioniert damit auf jedem System, auf das der Upload-Ordner mitkopiert wird (Backup-System, Docker, lokale Entwicklung).',
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
