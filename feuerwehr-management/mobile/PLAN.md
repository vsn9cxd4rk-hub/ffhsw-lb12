# FENIX Mobile – Plan

Stand: 14.10.2026, App-Version 0.1.0 (Grundgerüst), Web-App 1.4.0

## 1. Ausgangslage

**Vorhanden in der Mobile-App**

- Expo SDK 57, Expo Router, React Compiler, Dark Mode, FENIX-Farben
- Anmeldung mit Server-Adresse. Das Sitzungsmodell entspricht dem Web-Frontend: Access-Token im
  Speicher, Refresh-Token als Cookie. Die Server-Adresse liegt in SecureStore.
- Dashboard (`/dashboard/stats`): Kennzahlen, letzte Einsätze, anstehende Prüfungen, für Admins
  zusätzlich Untersuchungen
- Ein eigener Mini-Hook `useQuery`, noch ohne Cache und Mutationen

**Bekannte Lücken** (siehe README)

- Ein erzwungener Passwortwechsel (`mustChangePassword`) fehlt. Betroffene Konten können die App nicht nutzen.
- Für Release-Builds braucht das Backend HTTPS (iOS ATS, Android 9+).
- Es gibt keine Navigationsstruktur (Tabs), keine Rechteprüfung in der Oberfläche und keine Detailseiten.

**Wichtige Randbedingungen aus dem Backend**

| Bereich | Recht | Bemerkung |
|---|---|---|
| Fahrzeuge, Fahrtenbuch | `br1` BIT_VEHICLES | ganze Route |
| Einsätze, Statistik | `br2` BIT_OPERATIONS | ganze Route |
| Bestand, Prüfbuch, Mängel | `br3` BIT_EQUIPMENT | ganze Route, **auch das Melden von Mängeln** |
| Veranstaltungen | `br4` BIT_EVENTS | Lesen für alle, Schreiben mit Recht |
| Ausbildung | `br5` BIT_TRAINING | Lesen für alle, Schreiben mit Recht |
| Mitglieder | Admin | Liste in reduzierter Form (Picker) mit br2/br5 |

Die Etiketten enthalten bereits einen QR-Code mit der URL `…/scan/<Inventarnummer>` oder einen
Barcode mit der Inventarnummer (`QrPrintView.tsx`). Beides kann die Handykamera direkt lesen.
Das ist der stärkste Hebel für die App.

## 2. Was passt aufs Handy und was nicht?

Faustregel: Mobil ist alles, was **vor Ort am Gerät, Fahrzeug oder Einsatz** passiert oder
**schnell nachgeschaut** wird. Am PC bleibt, was Stammdaten pflegt, viel tippt oder
Dokumente erzeugt.

| Web-Bereich | Mobil? | Was mobil sinnvoll ist |
|---|---|---|
| Dashboard / Benachrichtigungen | ✅ ja | Vorhanden. Ergänzen: `/dashboard/notifications`, Tippen springt in den Bereich |
| **Scan** (`/scan/:id`) | ✅✅ Kernfunktion | Kamera-Scan von QR und Barcode → Artikel → „Prüfen“ oder „Mangel melden“ |
| **Prüfbuch / Prüfungen** | ✅✅ Kernfunktion | Fällige Prüfungen, Prüfdialog mit Kriterien (io/nio, „Alle offenen auf io“), Foto als Prüfdokument |
| **Mängel** | ✅✅ Kernfunktion | Mangel melden mit Foto, offene Mängel ansehen, Status ändern, Reparatur erfassen |
| Bestandsliste / Artikel | ✅ lesend+ | Suche, Artikeldetail, Standort/Zuordnung, Dokumente ansehen. Keine Neuanlage, kein Import |
| Fahrzeuge | ✅ teilweise | Fahrzeugdetail, TÜV/SP-Termine, **Fahrtenbuch-Eintrag** (Fahrer, km, Zweck) |
| Einsätze | ✅ teilweise | Liste/Detail, **Schnellerfassung nach Rückkehr** (Ort, Stichwort, Zeiten je Fahrzeug, Personal). Berichte/PDF bleiben im Web |
| Veranstaltungen | ✅ ja | Termine ansehen, **Anwesenheit am Dienstabend abhaken** (`/events/:id/attendance`) |
| Ausbildung | ◐ lesend | Lehrgänge ansehen. Urkunden-Upload per Kamera ist denkbar |
| Statistik | ◐ optional | Einfache Kennzahlen, keine Diagramm-Ausgabe für den Druck |
| Mitglieder | ◐ nur Admin | Telefonbuch/Kontakt (anrufen), Verfügbarkeit. **Keine Gesundheitsdaten** im App-Cache (AGENTS.md) |
| Passwort ändern | ✅ Pflicht | Wird für `mustChangePassword` gebraucht |
| Hilfe, Lizenzen | ◐ | Lizenzen sind für die Stores nötig, Hilfe als Link ins Web |
| Einstellungen, Geräteklassen, Prüfarten, Vorlagen, Import | ❌ nein | Stammdaten-Pflege bleibt im Web |
| Benutzer- und Rechteverwaltung | ❌ nein | Bleibt im Web |
| Mitglieder-Stammdaten bearbeiten | ❌ nein | Zu viele Felder und sensible Daten |
| Berichte erzeugen (BSW, Einsatzbericht, Kräftenachweis) | ❌ erzeugen nein | Fertige PDFs nur ansehen oder teilen |

## 3. Was mobil neu dazukommen kann

Diese Funktionen hat die Web-App nicht, sie lohnen sich aber erst mobil:

1. **Kamera-Scan** statt Webseite per Handy-Browser (schneller, Taschenlampe, Serien-Scan beim Prüfen)
2. **Fotos** zu Mängeln und Prüfungen, direkt aus der Kamera
3. **Push-Benachrichtigungen**, z. B. „Prüfung überfällig“ oder „neuer Mangel“ (braucht Backend-Erweiterung)
4. **Offline-Erfassung** von Prüfungen in Gerätehaus oder Keller ohne Netz. Die Daten werden später synchronisiert.
5. **Eigene Daten** („Meine Lehrgänge“, „Meine Verfügbarkeit“) über `User.memberId` (braucht `/me`-Endpunkte)

## 4. Phasenplan

### Phase 0 – Fundament ✅ erledigt (14.10.2026)

Ohne dieses Fundament wird jede weitere Seite teurer.

- [x] **Passwortwechsel-Screen** für `mustChangePassword` (`PUT /auth/change-password`). Danach meldet sich die App automatisch mit dem neuen Passwort an. Freiwillig unter „Mehr“.
- [x] **@tanstack/react-query** statt des eigenen `useQuery` (wie im Web): Cache, Mutationen, Pull-to-Refresh, Invalidierung
- [x] **Navigation:** Tab-Leiste mit Start · Mehr. Scan, Prüfungen und Mängel kommen in Phase 1 als `Tabs.Protected` dazu.
- [x] **Rechte-Helfer** `hasPermission(user, 'br3')` analog zum Web-Frontend. Bit-Konstanten aus `permissionBits.ts` übernehmen.
- [x] **Gemeinsame UI-Bausteine:** Liste/Zeile, Statuspunkt, Leer- und Fehlerzustand, Formularfelder. Der Datumswähler folgt mit dem ersten Formular, das ihn braucht.
- [x] Typen aus `webapp/frontend/src/types` übernehmen (Article, Inspection, Defect …), jeweils mit dem Screen, der sie nutzt (Phase 1)
- [x] Lint (ESLint mit `eslint-config-expo`) und Typecheck eingerichtet. Die Startseite zeigt Hinweise aus `/dashboard/notifications`, gefiltert nach Rechten.

### Phase 1 – Gerätewart vor Ort ✅ weitgehend erledigt (14.10.2026)

Die Tabs Scan, Prüfungen und Mängel sind nur mit br3 sichtbar (`Tabs.Protected`).

- [x] **Scan-Screen** mit `expo-camera` (QR, Code 128/39, EAN-13), Taschenlampe und manueller Eingabe. Akzeptiert die URL `…/scan/<nr>` und die reine Nummer.
      Die Logik wie in `ScanPage.tsx`: Ist der Artikel fällig, öffnet sich die Prüfung, sonst das Artikeldetail. Anders als im Web wird auch die MP-Feuer- und die Gemeinde-Nummer exakt gesucht (Barcode-Etiketten tragen die MP-Feuer-Nummer). Bei mehreren oder nur ähnlichen Treffern erscheint eine Auswahl.
- [x] **Deep Link** `fenix://scan/<nr>` über die Route `(app)/scan/[code]`. Die Tabs bleiben darunter (`unstable_settings.anchor`).
- [ ] Optional: Universal/App Links, damit die QR-URL der Etiketten mit der Handy-Kamera direkt die App öffnet (braucht HTTPS-Domain und Dateien unter `/.well-known` im nginx). Ein Deep Link vor der Anmeldung geht nach dem Login noch verloren.
- [x] **Artikeldetail:** Prüfstatus, Stammdaten, Zuordnungen, frühere Prüfungen, offene Mängel. Dokumente (PDF) fehlen noch: Download und Anzeige brauchen `expo-file-system` und `expo-sharing`.
- [x] **Prüfdialog:** Kriterien, io/nio je Kriterium, „Alle offenen auf io“ (überschreibt kein nio), Prüfer (aus `/inspections/inspectors`, vorbelegt mit dem eigenen Mitglied), Prüfart, Datum, Bemerkung, Vorschau der nächsten Fälligkeit. Nach „nicht bestanden“ führt ein Knopf direkt zu „Mangel melden“. Bearbeiten bestehender Prüfungen bleibt im Web.
- [x] **Liste „Fällige Prüfungen“** (`/inspections/due`) mit Suche auf dem Gerät
- [x] **Mangel melden** aus dem Artikel oder aus einer nio-Prüfung (Beschreibung mit den beanstandeten Kriterien vorbelegt). Mängelliste nach Status, Detail mit Statuswechsel und Bemerkung. Mängel ohne Artikel (Fahrzeug, Sonstige) und Reparaturen bleiben im Web.
- [ ] **Foto-Upload** (`expo-image-picker`) als Prüfdokument (`POST /inspections/:id/documents`). **Blockiert:** Das Backend nimmt dort nur PDF an (`inspectionDocUpload`, `kinds: ['pdf']`), siehe Abschnitt 5.

### Phase 2 – Dienstbetrieb ✅ erledigt (14.10.2026)

Navigation: fünf Tabs (Start · Termine · Scan · Geräte · Mehr). „Prüfungen“ und „Mängel“ aus Phase 1 sind
im Tab **Geräte** zusammengefasst (Umschalter). Einsätze und Fahrzeuge liegen unter **Mehr → Bereiche**.

- [x] **Veranstaltungen (Tab Termine):** kommende Termine (heute grün) und die letzten 15, Detail mit
      Anwesenheitsübersicht, **Anwesenheitsliste** mit A/E/X je Mitglied, Suche und „Übrige abwesend“.
      Lesen für alle, Speichern mit br4. Ein gesetzter Status lässt sich nur ändern, nicht löschen (das Backend speichert per Upsert).
- [x] **Fahrzeuge** (br1): Liste mit dem dringendsten Prüftermin, Detail mit TÜV/SP/Service (rot/gelb wie die
      Hinweise), Fahrzeugdaten, letzte Fahrten. **Fahrtenbuch-Eintrag** mit Fahrer (vorbelegt), Start-km aus dem
      höchsten letzten End-Stand, Prüfung Ende ≥ Start.
- [x] **Einsätze** (br2): Liste, **Neuer Einsatz** (Datum, Alarmzeit mit „Jetzt“, Ort, Stichwort), Detail,
      Bearbeiten der Grunddaten. **Fahrzeuge und Zeiten** je Fahrzeug (Auswahl aus der Fahrzeugliste, wenn br1
      vorhanden, sonst Freitext; Stärke per +/−; vier Zeiten mit „Jetzt“; Alarmzeit vorbelegt), Entfernen mit
      Rückfrage. **Kräftenachweis:** Besatzung je Fahrzeug mit Soll/Ist, nachgerückte Kräfte, Hinzufügen
      mehrerer Mitglieder nacheinander (Suche, Funktion, Fahrzeug), Entfernen mit Rückfrage.
- [ ] Bleibt im Web: Berichtsfelder des Einsatzes, Einsatzbericht/Kräftenachweis als PDF, Dokumente, BSW-Daten,
      Veranstaltungen anlegen, Fahrtenbuch bearbeiten und drucken.

Hinweis zur Entwicklung: Der React Compiler macht aus `obj?.x` in einem Callback eine Memo-Abhängigkeit
`obj.x` ohne optionale Prüfung. Bei möglicherweise undefinierten Objekten den Wert vorher in eine Variable
ziehen (siehe `operation/[id]/time.tsx`).

### Phase 3 – Mehrwert und Betrieb

- [ ] Push-Benachrichtigungen (`expo-notifications`). Backend: Token speichern, Versand bei Fälligkeit oder neuem Mangel
- [ ] Offline-Warteschlange für Prüfungen und Mängel
- [ ] Ausbildung lesend, „Meine Daten“ (braucht Backend-Endpunkte)
- [ ] Admin: Telefonbuch (ohne Gesundheitsdaten)

### Phase 4 – Verteilung

- [ ] Backend über HTTPS erreichbar (nginx + Zertifikat), `deploy/` anpassen
- [ ] Refresh-Token optional im Body und in SecureStore (robuster als der native Cookie-Speicher)
- [ ] EAS-Profile (development / preview / production), App-Icons, Store-Einträge, Datenschutzerklärung
- [ ] Interne Verteilung zuerst (TestFlight / Play Internal Testing), dann ggf. Stores
- [ ] OTA-Updates mit `eas update`

## 5. Nötige Backend-Änderungen

| Anlass | Änderung |
|---|---|
| Jeder Kamerad soll Mängel melden können | Eigenes Recht (oder offen für alle angemeldeten Nutzer) nur für `POST /inventory/defects` und das Lesen des gescannten Artikels. Heute ist dafür br3 nötig. |
| Fotos zu Prüfungen | `inspectionDocUpload` um `'jpeg', 'png'` erweitern (die Signaturprüfung in `utils/upload.ts` kann das schon). Download läuft bereits als Attachment. |
| Fotos zu Mängeln | Dokument-Upload für Defects (gibt es bisher nur für Artikel, Prüfungen, Einsätze und Veranstaltungen) |
| Push | Tabelle für Geräte-Token und ein Versand-Job (z. B. täglich für Fälligkeiten) |
| „Meine Daten“ | `/me/courses`, `/me/availability` über `User.memberId` |
| Robustere Anmeldung | Refresh-Token optional im Response-Body |
| Datenschutz/Rechte | `/dashboard/stats` (anstehende Prüfungen) und `/dashboard/notifications` (Prüfungen, Mängel, Fahrzeuge) liefern Gerätewart- und Fahrzeugdaten auch an Benutzer ohne br3/br1. Die App filtert die Hinweise, besser wäre ein Filter im Backend. |
| Mobile-Clients | Versions-Header oder `/health` mit Mindest-App-Version, um alte Apps zu warnen |

## 6. Empfehlung: nächster Schritt

1. **Phase 0 komplett:** Passwortwechsel, react-query, Tabs, Rechte-Helfer. Das ist klein, schaltet aber alle weiteren Seiten frei.
2. Danach direkt **Scan → Artikel → Prüfdialog** (Phase 1). Die Etiketten hängen schon an den Geräten,
   die API ist vollständig vorhanden, und für den Gerätewart ist das der größte Zeitgewinn gegenüber dem PC.
3. Parallel klären: **Wer darf mobil Mängel melden?** Wenn das jeder Kamerad können soll, braucht es die
   Backend-Anpassung aus Abschnitt 5. Davon hängt ab, ob Phase 1 nur Gerätewarte anspricht oder alle.

Hinweis: `expo-camera` und `expo-image-picker` laufen in Expo Go. Für Push und Universal Links ist ein
Development-Build nötig (`eas build --profile development`).
