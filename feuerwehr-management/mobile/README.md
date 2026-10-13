# FENIX Mobile

Mobile-App (iOS/Android) für FENIX, gebaut mit [Expo](https://expo.dev) (SDK 57, React Native,
Expo Router). Sie spricht dieselbe REST-API wie das Web-Frontend (`webapp/backend`).

Stand: Grundgerüst mit Anmeldung und Dashboard (Kennzahlen, letzte Einsätze, anstehende Prüfungen).

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
- **Erzwungener Passwortwechsel** (`mustChangePassword`) ist noch nicht umgesetzt – solche Konten
  erhalten derzeit nur „Bitte zuerst das Passwort ändern“ als Fehler.
- Optional: Refresh-Token im Body statt Cookie zurückgeben und in SecureStore ablegen (robuster als
  der native Cookie-Speicher); erfordert eine kleine Backend-Erweiterung.

## Struktur

| Pfad | Inhalt |
|------|--------|
| `src/app/` | Screens (Expo Router: jede Datei ist ein Screen) |
| `src/app/sign-in.tsx` | Anmeldung (Server, Benutzer, Passwort) |
| `src/app/(app)/` | Bereich nach der Anmeldung, geschützt über `Stack.Protected` |
| `src/api/` | API-Client und Typen (Teilmenge von `webapp/frontend/src/types`) |
| `src/auth/session.tsx` | Session-Kontext: Anmelden, Abmelden, Wiederherstellen |
| `src/constants/theme.ts` | Farben (FENIX-Rot), Abstände |

## Prüfen

```powershell
npm run typecheck
npx expo-doctor
```

## Bauen für die Stores (später)

Mit EAS in der Cloud, auch iOS ohne Mac:

```powershell
npx eas-cli@latest login
npx eas-cli@latest build --platform android   # bzw. ios
```

Für iOS ist ein Apple-Developer-Account nötig (99 $/Jahr), für Google Play einmalig 25 $.
