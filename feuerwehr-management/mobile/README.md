# FENIX Mobile

Mobile-App (iOS/Android) für FENIX, gebaut mit [Expo](https://expo.dev) (SDK 57, React Native,
Expo Router). Sie spricht dieselbe REST-API wie das Web-Frontend (`webapp/backend`).

Stand: Phase 0 bis 2 aus [PLAN.md](PLAN.md):

- Anmeldung, erzwungener Passwortwechsel, Tabs „Start“ (Hinweise, Kennzahlen) und „Mehr“
- Gerätewart (nur mit Recht br3): Etikett scannen (QR/Barcode, Kamera oder Eingabe), Artikeldetail,
  fällige Prüfungen, Prüfdialog mit Kriterien, Mangel melden, Mängelliste mit Statuswechsel
- Deep Link `fenix://scan/<Inventarnummer>`
- Termine: Veranstaltungen und Anwesenheitsliste (Erfassen mit br4)
- Einsätze (br2): anlegen, Fahrzeuge mit Stärke und Zeiten, Kräftenachweis
- Fahrzeuge (br1): Prüftermine, Fahrtenbuch-Eintrag

Die Kamera braucht ein echtes Gerät (Expo Go). Im Browser (`w`) funktioniert nur die manuelle Eingabe zuverlässig.

## Starten

```powershell
cd mobile
npm install
npx expo start
```

- **Handy:** App „Expo Go“ installieren (App Store / Google Play), den QR-Code im Terminal scannen.
  Handy und PC müssen im selben WLAN sein.
- **Browser:** im Terminal `w` drücken (schnellster Test am PC).
- **Android-Emulator:** `a` drücken (Android Studio mit Emulator nötig).

Hinweis für diesen Rechner: Die globale npm-Konfiguration (`c:\project\apps\nodejs\etc\npmrc`)
setzt den Cache auf ein fremdes Benutzerprofil. Vor `npm`/`npx` daher
`$env:npm_config_cache="$env:LOCALAPPDATA\npm-cache"` setzen.

## Backend für die Entwicklung

Backend wie gewohnt starten (`webapp/backend`, `npm run dev`, Port 3001). In der App als Server
die **LAN-IP des PCs** eintragen, z. B. `192.168.1.20:3001` – `localhost` ist auf dem Handy das
Handy selbst. Ggf. Port 3001 in der Windows-Firewall freigeben.

Für den Browser-Test (`w`) muss die Expo-Adresse in `CORS_ORIGIN` des Backends stehen, z. B.
`CORS_ORIGIN=http://localhost:3000,http://localhost:8081`. Die nativen Apps senden keinen
Origin-Header und brauchen das nicht.

## Anmeldung

Gleiches Modell wie im Web-Frontend: Access-Token nur im Speicher, Refresh-Token als
httpOnly-Cookie von `/api/auth/login`; die native HTTP-Schicht von iOS/Android behält das Cookie,
so bleibt man nach einem App-Neustart angemeldet. Die Server-Adresse liegt in `expo-secure-store`.

Offene Punkte, bevor die App an Kameraden geht:

- **HTTPS:** Release-Builds von iOS (ATS) und Android 9+ blockieren unverschlüsseltes HTTP. Für
  den Betrieb braucht das Backend HTTPS (nginx mit Zertifikat). Expo Go erlaubt HTTP für Tests.
- Optional: Refresh-Token im Body statt Cookie zurückgeben und in SecureStore ablegen (robuster als
  der native Cookie-Speicher); erfordert eine kleine Backend-Erweiterung.

## Struktur

| Pfad | Inhalt |
|------|--------|
| `src/app/` | Screens (Expo Router: jede Datei ist ein Screen) |
| `src/app/sign-in.tsx` | Anmeldung (Server, Benutzer, Passwort) |
| `src/app/change-password.tsx` | Erzwungener Passwortwechsel (`mustChangePassword`) |
| `src/app/(app)/` | Bereich nach der Anmeldung, geschützt über `Stack.Protected`; Detailseiten als Stack |
| `src/app/(app)/(tabs)/` | Tab-Leiste; Tabs mit Rechtebedarf kommen in `Tabs.Protected` |
| `src/api/` | API-Client und Typen (Teilmenge von `webapp/frontend/src/types`) |
| `src/auth/session.tsx` | Session-Kontext: Anmelden, Abmelden, Wiederherstellen, Passwort ändern |
| `src/auth/permissions.ts` | Rechte-Bits (wie Backend) und `hasPermission` |
| `src/lib/query-client.ts` | react-query: Retry-Regeln, Refetch beim Zurückkehren in die App |
| `src/components/ui/` | Gemeinsame Bausteine: Button, TextField, DateField, TimeField, Stepper, ChoiceChips, Section/ListRow, QueryState |
| `src/app/(app)/(tabs)/` | `index`, `events`, `scan` und `equipment` (Gerätewart), `more` |
| `src/app/(app)/` | Gerätewart: `scan/[code]`, `article/[id]`, `inspect/[articleId]`, `defect/…`; `event/[id]/…`, `vehicles`, `vehicle/[id]/…`, `operations`, `operation/…` |
| `src/components/equipment/` | Listen „Fällige Prüfungen“ und „Mängel“ für den Tab Geräte |
| `src/lib/scan.ts` | Etikett-Inhalt (URL oder Nummer) auslesen, Artikel zur Nummer finden |
| `src/constants/theme.ts` | Farben (FENIX-Rot), Abstände |

## Prüfen

```powershell
npm run typecheck
npm run lint
npx expo-doctor
```

## Bauen für die Stores (später)

Mit EAS in der Cloud, auch iOS ohne Mac:

```powershell
npx eas-cli@latest login
npx eas-cli@latest build --platform android   # bzw. ios
```

Für iOS ist ein Apple-Developer-Account nötig (99 $/Jahr), für Google Play einmalig 25 $.
