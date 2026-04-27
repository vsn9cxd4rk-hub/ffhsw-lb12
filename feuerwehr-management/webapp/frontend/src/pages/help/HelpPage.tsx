import React, { useState } from 'react';

const chapters = [
  { id: 'uebersicht', title: '1. Übersicht' },
  { id: 'dashboard', title: '2. Dashboard' },
  { id: 'bestandsliste', title: '3. Bestandsliste' },
  { id: 'pruefbuch', title: '4. Prüfbuch' },
  { id: 'maengel', title: '5. Mängel & Reparaturen' },
  { id: 'einstellungen', title: '6. Einstellungen' },
  { id: 'personal', title: '7. Personal' },
  { id: 'fahrzeuge', title: '8. Fahrzeuge' },
  { id: 'einsaetze', title: '9. Einsätze & Veranstaltungen' },
  { id: 'install-windows', title: '10. Installation Windows' },
  { id: 'install-linux', title: '11. Installation Linux' },
  { id: 'wartung', title: '12. Wartung & Backup' },
];

function H2({ id, children }: { id: string; children: React.ReactNode }) {
  return <h2 id={id} className="text-xl font-bold text-gray-900 mt-10 mb-4 pb-2 border-b border-gray-200 scroll-mt-20">{children}</h2>;
}
function H3({ children }: { children: React.ReactNode }) {
  return <h3 className="text-lg font-semibold text-gray-800 mt-6 mb-2">{children}</h3>;
}
function P({ children }: { children: React.ReactNode }) {
  return <p className="text-sm text-gray-700 leading-relaxed mb-3">{children}</p>;
}
function Code({ children }: { children: React.ReactNode }) {
  return <pre className="bg-gray-900 text-green-400 text-xs p-4 rounded-lg overflow-x-auto mb-4 font-mono">{children}</pre>;
}
function Li({ children }: { children: React.ReactNode }) {
  return <li className="text-sm text-gray-700 ml-4 list-disc mb-1">{children}</li>;
}

export function HelpPage() {
  const [active, setActive] = useState('uebersicht');

  const scrollTo = (id: string) => {
    setActive(id);
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="flex gap-6 max-w-7xl mx-auto">
      {/* Sidebar TOC */}
      <nav className="w-56 flex-shrink-0 sticky top-20 self-start hidden lg:block">
        <h3 className="text-xs font-bold text-gray-400 uppercase mb-3">Inhaltsverzeichnis</h3>
        <ul className="space-y-1">
          {chapters.map(ch => (
            <li key={ch.id}>
              <button onClick={() => scrollTo(ch.id)}
                className={`w-full text-left px-3 py-1.5 rounded text-sm transition-colors ${active === ch.id ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-600 hover:bg-gray-50'}`}>
                {ch.title}
              </button>
            </li>
          ))}
        </ul>
      </nav>

      {/* Content */}
      <div className="flex-1 min-w-0 bg-white rounded-lg border border-gray-200 p-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">Feuerwehr Management System - Hilfe</h1>
        <p className="text-sm text-gray-500 mb-8">Dokumentation und Anleitung für alle Module</p>

        {/* 1. Übersicht */}
        <H2 id="uebersicht">1. Übersicht</H2>
        <P>Das Feuerwehr Management System ist eine webbasierte Anwendung zur Verwaltung aller Aspekte eines Feuerwehr-Löschbezirks. Es umfasst Personal-, Fahrzeug-, Geräte- und Einsatzverwaltung mit integriertem Prüfbuch gemäß DGUV 305-002.</P>
        <H3>Benutzergruppen</H3>
        <ul className="mb-4">
          <Li><strong>Administrator</strong> — Vollzugriff auf alle Funktionen inkl. Benutzerverwaltung und Einstellungen</Li>
          <Li><strong>Gerätewarte</strong> — Fahrzeuge, Bestandsliste, Prüfbuch, Mängelverwaltung</Li>
          <Li><strong>Benutzer</strong> — Personal (eigene Daten), Veranstaltungen, Ausbildung</Li>
          <Li><strong>Maschinisten</strong> — Wie Benutzer, zusätzlich Fahrzeuge und Fahrtenbuch</Li>
          <Li><strong>Gruppenführer</strong> — Wie Benutzer, zusätzlich Einsatzberichte</Li>
        </ul>

        {/* 2. Dashboard */}
        <H2 id="dashboard">2. Dashboard</H2>
        <P>Das Dashboard bietet eine Übersicht über den aktuellen Status des Löschbezirks.</P>
        <H3>Statistik-Karten</H3>
        <ul className="mb-4">
          <Li>Aktive Mitglieder, Fahrzeuge, Einsätze im laufenden Jahr</Li>
          <Li>Anstehende Prüfungen (Anzahl gelber + roter Einträge)</Li>
        </ul>
        <H3>Ampelsystem</H3>
        <P>Geräteprüfungen werden mit einem Ampelsystem dargestellt:</P>
        <ul className="mb-4">
          <Li><span className="inline-block h-2.5 w-2.5 rounded-full bg-green-500 mr-1" /> <strong>Grün</strong> — Prüfung nicht fällig (mehr als 2 Monate)</Li>
          <Li><span className="inline-block h-2.5 w-2.5 rounded-full bg-yellow-500 mr-1" /> <strong>Gelb</strong> — Prüfung innerhalb der nächsten 2 Monate fällig</Li>
          <Li><span className="inline-block h-2.5 w-2.5 rounded-full bg-red-500 mr-1" /> <strong>Rot</strong> — Prüfung überfällig oder nie durchgeführt</Li>
        </ul>
        <P>Klicken Sie auf einen Artikelnamen, um direkt zur Artikel-Detailseite zu springen.</P>

        {/* 3. Bestandsliste */}
        <H2 id="bestandsliste">3. Bestandsliste</H2>
        <P>Die Bestandsliste enthält alle Geräte und Ausrüstungsgegenstände des Löschbezirks.</P>
        <H3>Artikel anlegen/bearbeiten</H3>
        <P>Über den Button "Neuer Artikel" können Sie einen neuen Gegenstand erfassen. Folgende Felder stehen zur Verfügung:</P>
        <ul className="mb-4">
          <Li><strong>Bezeichnung</strong> — Name des Artikels (Pflichtfeld)</Li>
          <Li><strong>Inventarnummer</strong> — Eindeutige LB12-Inventarnummer</Li>
          <Li><strong>Inv.-Nr. Gemeinde (DOPPIK)</strong> — Gemeinde-Inventarnummer</Li>
          <Li><strong>Inv.-Nr. MP Feuer</strong> — MP-Feuer-Nummer</Li>
          <Li><strong>Bezeichnung LB</strong> — Löschbezirk-Kennung (Standard: LB12)</Li>
          <Li><strong>Hersteller / Seriennummer / DIN / Spezifikation</strong></Li>
          <Li><strong>Herstellerdatum / Indienststellung / Außerdienststellung</strong></Li>
          <Li><strong>Geräteklasse / Unterklasse</strong> — Gemäß DGUV 305-002</Li>
          <Li><strong>Prüfintervall</strong> — In Monaten</Li>
          <Li><strong>Aussonderungsfrist</strong> — In Monaten</Li>
          <Li><strong>Lagerort</strong> — Fahrzeug oder fester Lagerort</Li>
        </ul>
        <H3>Geräteklassen (DGUV 305-002)</H3>
        <P>Die Geräte werden gemäß DGUV 305-002 in folgende 11 Klassen eingeteilt:</P>
        <ol className="mb-4 ml-4 list-decimal text-sm text-gray-700 space-y-0.5">
          <li>Schutzkleidung und Schutzgerät</li><li>Löschgerät</li><li>Schläuche, Armaturen und Zubehör</li>
          <li>Rettungsgerät</li><li>Sanitäts- und Wiederbelebungsgerät</li><li>Beleuchtungs-, Signal- und Fernmeldegerät</li>
          <li>Arbeitsgerät</li><li>Handwerkzeug und Messgerät</li><li>Sondergerät</li><li>Pumpen</li><li>Atemschutz</li>
        </ol>
        <H3>Artikel-Detailseite</H3>
        <P>Klicken Sie in der Bestandsliste auf einen Artikel, um die Detailseite zu öffnen. Dort finden Sie folgende Tabs:</P>
        <ul className="mb-4">
          <Li><strong>Stammdaten</strong> — Alle Artikelinformationen auf einen Blick</Li>
          <Li><strong>Dokumente</strong> — PDF-Anhänge hochladen und verwalten</Li>
          <Li><strong>Prüfgrundsätze</strong> — Abweichende Prüfgrundsätze je Gerät (z.B. DGUV 305-002, Herstellerangaben)</Li>
          <Li><strong>Prüfintervalle</strong> — Individuelle Intervalle je Prüfart für diesen Artikel</Li>
          <Li><strong>Mängel</strong> — Gemeldete Mängel für diesen Artikel</Li>
          <Li><strong>Reparaturen</strong> — Durchgeführte Reparaturen</Li>
          <Li><strong>Prüfhistorie</strong> — Alle durchgeführten Prüfungen</Li>
        </ul>

        {/* 4. Prüfbuch */}
        <H2 id="pruefbuch">4. Prüfbuch</H2>
        <P>Das Prüfbuch dokumentiert alle Geräteprüfungen und ermöglicht die Planung anstehender Prüfungen.</P>
        <H3>Fällige Prüfungen</H3>
        <P>Der erste Tab zeigt alle Artikel, deren Prüfung fällig oder überfällig ist. Filtern Sie nach Geräteklasse und Unterklasse. Klicken Sie auf "Prüfen", um die Prüfung zu dokumentieren.</P>
        <H3>Prüfung dokumentieren</H3>
        <P>Im Prüfdialog erfassen Sie:</P>
        <ul className="mb-4">
          <Li><strong>Artikel</strong> — Auswahl des zu prüfenden Geräts</Li>
          <Li><strong>Prüfdatum</strong> — Datum der Prüfung</Li>
          <Li><strong>Prüfer</strong> — Name des Prüfers</Li>
          <Li><strong>Prüfart</strong> — Sicht- und Funktionsprüfung, Belastungsprüfung oder Elektroprüfung</Li>
          <Li><strong>Prüfkriterien</strong> — Jedes Kriterium wird mit "io" (in Ordnung) oder "nio" (nicht in Ordnung) bewertet</Li>
          <Li><strong>PDF-Anhang</strong> — Optional ein Prüfprotokoll als PDF anhängen</Li>
        </ul>
        <P>Das Gesamtergebnis wird automatisch berechnet: Nur wenn alle Kriterien "io" sind, gilt die Prüfung als "Bestanden".</P>
        <H3>Prüfung bearbeiten</H3>
        <P>In der Prüfhistorie können Sie über das Stift-Symbol eine bestehende Prüfung nachträglich bearbeiten.</P>
        <H3>Suche</H3>
        <P>Über das Suchfeld können Sie nach Inventarnummer, Gemeinde-Nr., MP-Feuer-Nr. oder Artikelname suchen. Die Suche gilt für alle Tabs.</P>
        <H3>Berichte / PDF-Export</H3>
        <P>Im Tab "Berichte" können Sie Prüfungen nach Geräteklasse und Jahr filtern und als PDF-Report exportieren. Der Report enthält pro Artikel eine Seite mit allen Prüfdetails.</P>

        {/* 5. Mängel & Reparaturen */}
        <H2 id="maengel">5. Mängel &amp; Reparaturen</H2>
        <H3>Mängelmeldesystem</H3>
        <P>Über den Menüpunkt "Mängel" können Gerätewarte und Administratoren Mängel an Geräten melden.</P>
        <ul className="mb-4">
          <Li><strong>Schweregrad</strong>: Gering, Mittel, Hoch, Kritisch</Li>
          <Li><strong>Status-Workflow</strong>: Offen → In Bearbeitung → Behoben → Geschlossen</Li>
        </ul>
        <H3>Reparaturdokumentation</H3>
        <P>Im Tab "Reparaturen" dokumentieren Sie durchgeführte Reparaturen mit Datum, Beschreibung, Kosten und dem durchführenden Techniker. Reparaturen können optional mit einem gemeldeten Mangel verknüpft werden.</P>

        {/* 6. Einstellungen */}
        <H2 id="einstellungen">6. Einstellungen</H2>
        <P>Nur Administratoren haben Zugriff auf die Einstellungen. Verfügbare Tabs:</P>
        <ul className="mb-4">
          <Li><strong>Allgemein</strong> — Feuerwehrname, Adresse, Erinnerungszeiträume</Li>
          <Li><strong>Dienstgrade</strong> — Dienstgrade mit Kürzel verwalten</Li>
          <Li><strong>Jahre</strong> — Betriebsjahre verwalten und aktives Jahr setzen</Li>
          <Li><strong>Templates</strong> — Dokumentvorlagen (PDF/Word) hochladen</Li>
          <Li><strong>Geräteprüfung</strong> — Geräteklassen, Unterklassen und Prüfkriterien verwalten (Baumstruktur)</Li>
          <Li><strong>Prüfarten</strong> — Prüfungsarten (Sicht-, Belastungs-, Elektroprüfung) verwalten</Li>
          <Li><strong>Lagerorte</strong> — Feste Lagerorte anlegen/bearbeiten. Fahrzeug-Lagerorte werden automatisch erstellt.</Li>
          <Li><strong>Datenimport</strong> — CSV-Import für Bestandsliste und Prüfungen</Li>
        </ul>
        <H3>CSV-Import</H3>
        <P>Unter "Datenimport" können Sie Artikel und Prüfungen per CSV-Datei importieren. Das CSV-Format erwartet Semikolon (;) oder Komma als Trennzeichen, UTF-8-Kodierung und eine Header-Zeile. Eine Vorschau zeigt die Daten vor dem Import. Fehler werden zeilengenau gemeldet.</P>

        {/* 7. Personal */}
        <H2 id="personal">7. Personal</H2>
        <P>Die Personalverwaltung umfasst alle Mitglieder des Löschbezirks mit Stammdaten, Qualifikationen, Kontaktdaten, Angehörigen und medizinischen Untersuchungen (G25/G26/G30).</P>
        <ul className="mb-4">
          <Li>Mitglieder können in Gruppen eingeteilt werden (Einsatzabteilung, Jugendfeuerwehr, etc.)</Li>
          <Li>Qualifikationen wie Atemschutz, Truppführer, Maschinist werden als Checkboxen geführt</Li>
          <Li>Ausbildungen/Lehrgänge werden pro Mitglied mit Status und Zertifikat dokumentiert</Li>
        </ul>

        {/* 8. Fahrzeuge */}
        <H2 id="fahrzeuge">8. Fahrzeuge</H2>
        <P>Die Fahrzeugverwaltung enthält alle Einsatzfahrzeuge mit technischen Daten, Prüfterminen (TÜV, SP, Service) und einem Fahrtenbuch.</P>
        <ul className="mb-4">
          <Li><strong>Prüftermine</strong> — TÜV, Sicherheitsprüfung und Service-Termine mit Erinnerungsfunktion</Li>
          <Li><strong>Fahrtenbuch</strong> — Fahrer, Kilometerstand, Zweck der Fahrt</Li>
          <Li><strong>Geräteprüfungen</strong> — Fahrzeuggebundene Geräteprüfungen</Li>
        </ul>

        {/* 9. Einsätze & Veranstaltungen */}
        <H2 id="einsaetze">9. Einsätze &amp; Veranstaltungen</H2>
        <H3>Einsätze</H3>
        <P>Einsätze werden mit Einsatznummer, Ort, Datum, Fahrzeugzeiten und Stärke dokumentiert. Einsatzberichte und Dokumente (PDF) können angehängt werden.</P>
        <H3>Veranstaltungen</H3>
        <P>Dienstabende, Übungen und sonstige Veranstaltungen mit Anwesenheitsliste. Kategorien: Einsatz, Dienstabend, BSW, Sonstige, Übung.</P>

        {/* 10. Installation Windows */}
        <H2 id="install-windows">10. Installation Windows</H2>
        <H3>Voraussetzungen</H3>
        <ul className="mb-4">
          <Li>Windows 10/11</Li>
          <Li>Node.js 20+ (Download von nodejs.org)</Li>
          <Li>MySQL 8.0+ oder MariaDB (Download von mysql.com oder mariadb.org)</Li>
          <Li>Git (optional, für Quellcode-Verwaltung)</Li>
        </ul>

        <H3>Schritt 1: MySQL einrichten</H3>
        <P>Installieren Sie MySQL und starten Sie den MySQL-Server. Öffnen Sie eine Eingabeaufforderung (cmd):</P>
        <Code>{`# Datenbank initialisieren (als Administrator in cmd ausführen)
cmd /c "mysql -u root -p < C:\\pfad\\zum\\projekt\\deploy\\init-db.sql"

# Alternativ in PowerShell:
Get-Content deploy\\init-db.sql | mysql -u root -p`}</Code>

        <H3>Schritt 2: Backend einrichten</H3>
        <Code>{`cd webapp\\backend
copy .env.example .env
# .env bearbeiten: DATABASE_URL, JWT_SECRET etc. anpassen

npm install
npx prisma generate
npx prisma db seed
npm run build`}</Code>

        <H3>Schritt 3: Frontend einrichten</H3>
        <Code>{`cd webapp\\frontend
npm install
npm run build`}</Code>

        <H3>Schritt 4: Starten</H3>
        <Code>{`# Backend starten (Port 3001)
cd webapp\\backend
npm start

# Frontend Entwicklungsmodus (Port 3000)
cd webapp\\frontend
npm run dev`}</Code>
        <P>Für den Produktionsbetrieb unter Windows empfiehlt sich die Nutzung von PM2 oder eines Windows-Diensts (z.B. NSSM).</P>

        {/* 11. Installation Linux (Manjaro) */}
        <H2 id="install-linux">11. Installation Linux (Manjaro)</H2>
        <H3>Systempakete installieren</H3>
        <Code>{`sudo pacman -Syu
sudo pacman -S nodejs npm mysql nginx git base-devel

# Node.js Version prüfen (mind. v18, empfohlen v20+)
node --version`}</Code>

        <H3>MySQL / MariaDB einrichten</H3>
        <Code>{`sudo pacman -S mariadb
sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
sudo systemctl start mariadb
sudo systemctl enable mariadb
sudo mysql_secure_installation

# Datenbank importieren
sudo mysql -u root -p < deploy/init-db.sql`}</Code>

        <H3>Backend einrichten</H3>
        <Code>{`# Verzeichnisstruktur erstellen
sudo mkdir -p /var/www/feuerwehrmanagement/{backend,frontend,uploads,backups}
sudo mkdir -p /var/log/feuerwehrmanagement

# Backend installieren
cd /var/www/feuerwehrmanagement/backend
cp .env.example .env
# .env bearbeiten (DATABASE_URL, JWT Secrets, UPLOAD_PATH etc.)
npm install --production=false
npm run build
npx prisma generate
npx prisma db seed`}</Code>

        <H3>.env Konfiguration</H3>
        <Code>{`NODE_ENV=production
PORT=3001
DATABASE_URL="mysql://FFWVSLB12:Ffw%23VSLB12!25@localhost:3306/FFWVSLB12"
JWT_ACCESS_SECRET=<openssl rand -hex 32>
JWT_REFRESH_SECRET=<openssl rand -hex 32>
CORS_ORIGIN=http://localhost:3000
UPLOAD_PATH=/var/www/feuerwehrmanagement/uploads`}</Code>

        <H3>Frontend bauen</H3>
        <Code>{`cd webapp/frontend
npm install
npm run build
cp -r dist/* /var/www/feuerwehrmanagement/frontend/`}</Code>

        <H3>PM2 Prozessmanager</H3>
        <Code>{`sudo npm install -g pm2
cd /var/www/feuerwehrmanagement
pm2 start ecosystem.config.js
pm2 save
pm2 startup systemd`}</Code>

        <H3>Nginx konfigurieren</H3>
        <Code>{`sudo cp deploy/nginx/feuerwehr.conf /etc/nginx/conf.d/feuerwehr.conf
# server_name anpassen
sudo nginx -t
sudo systemctl start nginx
sudo systemctl enable nginx`}</Code>

        {/* 12. Wartung & Backup */}
        <H2 id="wartung">12. Wartung &amp; Backup</H2>
        <H3>Datenbank-Backup</H3>
        <Code>{`# Manuelles Backup
mysqldump -u FFWVSLB12 -p FFWVSLB12 > backup-$(date +%Y%m%d).sql

# Automatisches Backup per Cron (täglich um 2:00 Uhr)
crontab -e
# Folgende Zeile hinzufügen:
0 2 * * * mysqldump -u FFWVSLB12 -pFfw#VSLB12!25 FFWVSLB12 > /var/www/feuerwehrmanagement/backups/backup-$(date +\\%Y\\%m\\%d).sql`}</Code>

        <H3>Backup wiederherstellen</H3>
        <Code>{`mysql -u root -p FFWVSLB12 < backup-20260427.sql`}</Code>

        <H3>Update einspielen</H3>
        <Code>{`# 1. Neuen Quellcode holen (git pull oder manuell kopieren)
# 2. Backend neu bauen
cd /var/www/feuerwehrmanagement/backend
npm install
npm run build
npx prisma generate
pm2 restart feuerwehr-backend

# 3. Frontend neu bauen
cd webapp/frontend
npm run build
cp -r dist/* /var/www/feuerwehrmanagement/frontend/`}</Code>

        <H3>Logs prüfen</H3>
        <Code>{`# PM2 Logs
pm2 logs feuerwehr-backend

# Oder direkt
tail -f /var/log/feuerwehrmanagement/pm2-out.log
tail -f /var/log/feuerwehrmanagement/pm2-error.log`}</Code>

        <H3>Erster Login</H3>
        <P>Nach der Installation melden Sie sich mit dem Standardbenutzer an:</P>
        <ul className="mb-4">
          <Li><strong>Benutzername:</strong> admin</Li>
          <Li><strong>Passwort:</strong> Admin123!</Li>
        </ul>
        <P><strong>Wichtig:</strong> Ändern Sie das Passwort sofort nach der ersten Anmeldung über das Benutzermenü oben rechts.</P>

        <H3>Ports</H3>
        <div className="overflow-x-auto mb-4">
          <table className="text-sm border border-gray-200 rounded">
            <thead className="bg-gray-50"><tr><th className="px-4 py-2 text-left">Dienst</th><th className="px-4 py-2 text-left">Port</th></tr></thead>
            <tbody>
              <tr className="border-t"><td className="px-4 py-2">Nginx (HTTP)</td><td className="px-4 py-2">80</td></tr>
              <tr className="border-t"><td className="px-4 py-2">Backend (intern)</td><td className="px-4 py-2">3001</td></tr>
              <tr className="border-t"><td className="px-4 py-2">MySQL/MariaDB</td><td className="px-4 py-2">3306</td></tr>
              <tr className="border-t"><td className="px-4 py-2">Frontend Dev</td><td className="px-4 py-2">3000</td></tr>
            </tbody>
          </table>
        </div>

        <div className="mt-12 pt-6 border-t border-gray-200 text-xs text-gray-400">
          Feuerwehr Management System — Online-Hilfe — Stand April 2026
        </div>
      </div>
    </div>
  );
}
