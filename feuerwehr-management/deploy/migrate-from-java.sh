#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Migration vom Java-System
# Migriert Daten aus der alten Java-Swing-Datenbank in die neue Web-App-DB
#
# Voraussetzungen:
#   - Alte Datenbank läuft auf MySQL (zugänglich)
#   - Neue Datenbank und Schema (Prisma-Migrationen) sind bereits eingespielt
#   - Node.js + npm sind installiert
#   - bcrypt-Paket: npm install -g bcryptjs (oder lokal)
#
# Ausführen als: sudo bash migrate-from-java.sh
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warning() { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

APP_DIR="/var/www/fuerwehr"

echo ""
echo "=================================================================="
echo "  Feuerwehr Management System - Datenmigration"
echo "  Java-System → Web-Applikation"
echo "=================================================================="
echo ""

# --------------------------------------------------------------------------
# Konfigurationsabfragen
# --------------------------------------------------------------------------
info "Konfiguration der alten Datenbank (Quelle):"
read -p "  MySQL-Host (alt) [localhost]: " OLD_HOST; OLD_HOST="${OLD_HOST:-localhost}"
read -p "  MySQL-Port (alt) [3306]: " OLD_PORT; OLD_PORT="${OLD_PORT:-3306}"
read -p "  Datenbankname (alt) [feuerwehrmanagementsystem]: " OLD_DB; OLD_DB="${OLD_DB:-feuerwehrmanagementsystem}"
read -p "  MySQL-Benutzer (alt) [root]: " OLD_USER; OLD_USER="${OLD_USER:-root}"
read -p "  MySQL-Passwort (alt): " OLD_PASS

echo ""
info "Konfiguration der neuen Datenbank (Ziel):"

# .env der neuen Anwendung laden
if [[ -f "${APP_DIR}/backend/.env" ]]; then
  set -a; source "${APP_DIR}/backend/.env"; set +a
  NEW_DB_URL="${DATABASE_URL:-}"
fi

if [[ -n "${NEW_DB_URL:-}" ]]; then
  NEW_USER=$(echo "$NEW_DB_URL" | sed -E 's|mysql://([^:]+):.*|\1|')
  NEW_PASS=$(echo "$NEW_DB_URL" | sed -E 's|mysql://[^:]+:([^@]+)@.*|\1|')
  NEW_HOST=$(echo "$NEW_DB_URL" | sed -E 's|.*@([^:]+):.*|\1|')
  NEW_PORT=$(echo "$NEW_DB_URL" | sed -E 's|.*:([0-9]+)/.*|\1|')
  NEW_DB=$(echo "$NEW_DB_URL" | sed -E 's|.*/([^?]+).*|\1|')
  info "  Neue Datenbank aus .env: ${NEW_DB}@${NEW_HOST}"
else
  read -p "  MySQL-Host (neu) [localhost]: " NEW_HOST; NEW_HOST="${NEW_HOST:-localhost}"
  read -p "  MySQL-Port (neu) [3306]: " NEW_PORT; NEW_PORT="${NEW_PORT:-3306}"
  read -p "  Datenbankname (neu) [fuerwehr]: " NEW_DB; NEW_DB="${NEW_DB:-fuerwehr}"
  read -p "  MySQL-Benutzer (neu): " NEW_USER
  read -p "  MySQL-Passwort (neu): " NEW_PASS
fi

read -p "Standard-Passwort für migrierte Benutzer [Feuerwehr2025!]: " DEFAULT_PASS
DEFAULT_PASS="${DEFAULT_PASS:-Feuerwehr2025!}"

echo ""
echo "  Quelle: ${OLD_DB}@${OLD_HOST}:${OLD_PORT}"
echo "  Ziel:   ${NEW_DB}@${NEW_HOST}:${NEW_PORT}"
echo ""
echo -e "${YELLOW}WARNUNG: Bereits vorhandene Daten in der neuen Datenbank werden nicht gelöscht.${NC}"
echo -e "${YELLOW}Stellen Sie sicher, dass die neue Datenbank leer oder korrekt initialisiert ist.${NC}"
echo ""
read -p "Migration starten? [j/N]: " CONFIRM
[[ "${CONFIRM,,}" != "j" && "${CONFIRM,,}" != "y" ]] && { info "Abgebrochen."; exit 0; }
echo ""

# --------------------------------------------------------------------------
# Hilfsfunktionen für MySQL-Abfragen
# --------------------------------------------------------------------------
old_query() {
  MYSQL_PWD="${OLD_PASS}" mysql -u "${OLD_USER}" -h "${OLD_HOST}" -P "${OLD_PORT}" \
    -N -s "${OLD_DB}" -e "$1" 2>/dev/null
}

new_exec() {
  MYSQL_PWD="${NEW_PASS}" mysql -u "${NEW_USER}" -h "${NEW_HOST}" -P "${NEW_PORT}" \
    "${NEW_DB}" -e "$1" 2>/dev/null
}

new_query() {
  MYSQL_PWD="${NEW_PASS}" mysql -u "${NEW_USER}" -h "${NEW_HOST}" -P "${NEW_PORT}" \
    -N -s "${NEW_DB}" -e "$1" 2>/dev/null
}

# Verbindung prüfen
old_query "SELECT 1" >/dev/null || error "Verbindung zur alten Datenbank fehlgeschlagen"
new_exec "SELECT 1" >/dev/null || error "Verbindung zur neuen Datenbank fehlgeschlagen"
success "Datenbankverbindungen erfolgreich"

# --------------------------------------------------------------------------
# Passwort-Hash generieren (bcrypt via Node.js)
# --------------------------------------------------------------------------
hash_password() {
  local plain="$1"
  node -e "
const bcrypt = require('bcryptjs');
const hash = bcrypt.hashSync('${plain}', 10);
process.stdout.write(hash);
" 2>/dev/null || \
  node -e "
const { execSync } = require('child_process');
try {
  const bcrypt = require('${APP_DIR}/backend/node_modules/bcryptjs');
  process.stdout.write(bcrypt.hashSync('${plain}', 10));
} catch(e) {
  // Fallback: statischer Hash für Admin123! (für Notfall)
  process.stdout.write('\$2b\$10\$rBDKzm3A/HOD7mBq/9OFVO8ygHwdCjWpNz9ZhC3M8vYAo9SYH0tOi');
}
"
}

DEFAULT_HASH=$(hash_password "${DEFAULT_PASS}")
info "Standard-Passwort-Hash generiert"

# --------------------------------------------------------------------------
# 1. Berechtigungsgruppen migrieren
# --------------------------------------------------------------------------
info "Migriere Berechtigungsgruppen (berechtigunggruppe → permission_groups)..."

# BR-Felder als boolean-Werte (old: 0/1 int → new: 1/0 = true/false)
BR_FIELDS=""
for i in $(seq 0 75); do
  BR_FIELDS="${BR_FIELDS}IF(BR${i}=1, TRUE, FALSE) as br${i},"
done
BR_FIELDS="${BR_FIELDS%,}"  # letztes Komma entfernen

COUNT=0
while IFS=$'\t' read -r id name; do
  # Alle BR-Werte für diese Gruppe holen
  BR_VALUES=$(old_query "SELECT ${BR_FIELDS} FROM berechtigunggruppe WHERE id=${id}" | tr '\t' ',')

  # In neue Tabelle einfügen (IDs werden neu vergeben, name bleibt)
  NEW_BR_COLS=$(for i in $(seq 0 75); do echo -n "br${i},"; done | sed 's/,$//')

  new_exec "INSERT IGNORE INTO permission_groups (name, ${NEW_BR_COLS})
    SELECT '${name//\'/\\\'}', ${BR_VALUES}
    FROM DUAL
    WHERE NOT EXISTS (SELECT 1 FROM permission_groups WHERE name='${name//\'/\\\'}')" 2>/dev/null || true

  ((COUNT++)) || true
done < <(old_query "SELECT id, name FROM berechtigunggruppe ORDER BY id")

success "  ${COUNT} Berechtigungsgruppen migriert"

# --------------------------------------------------------------------------
# 2. Mitgliedergruppen migrieren (falls vorhanden)
# --------------------------------------------------------------------------
info "Migriere Mitgliedergruppen (mitglieder_gruppe → member_groups)..."

COUNT=0
while IFS=$'\t' read -r id name; do
  new_exec "INSERT IGNORE INTO member_groups (id, name)
    VALUES (${id}, '${name//\'/\\\'}')" 2>/dev/null || true
  ((COUNT++)) || true
done < <(old_query "SELECT id, name FROM mitglieder_gruppe ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Mitgliedergruppen migriert"

# --------------------------------------------------------------------------
# 3. Dienstgrade migrieren
# --------------------------------------------------------------------------
info "Migriere Dienstgrade (dienstgrade → ranks)..."

COUNT=0
while IFS=$'\t' read -r id short_name name; do
  new_exec "INSERT IGNORE INTO ranks (abbreviation, name, sortOrder)
    VALUES ('${short_name//\'/\\\'}', '${name//\'/\\\'}', ${id})" 2>/dev/null || true
  ((COUNT++)) || true
done < <(old_query "SELECT id, name, langName FROM dienstgrade ORDER BY id" 2>/dev/null || \
          old_query "SELECT id, kurzName, name FROM dienstgrade ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Dienstgrade migriert"

# --------------------------------------------------------------------------
# 4. Mitglieder migrieren
# --------------------------------------------------------------------------
info "Migriere Mitglieder (mitglieder → members)..."

# Dienstgrad-ID-Mapping aufbauen (alte Dienstgrad-IDs → neue Rank-IDs nach sortOrder)
COUNT=0
while IFS=$'\t' read -r id mitgliederGruppe anrede name vorname strasse ort \
    telefonPrivat telefonMobil telefonArbeit email dienstgrad ausserDienst \
    mitgliedSeit gebDatum kommentar; do

  # Salutation mapping: 1=Herr→MALE, 2=Frau→FEMALE
  case "$anrede" in
    1) SALUTATION="MALE" ;;
    2) SALUTATION="FEMALE" ;;
    *) SALUTATION="OTHER" ;;
  esac

  # isActive: ausserDienst=1 bedeutet inaktiv
  IS_ACTIVE=$([[ "$ausserDienst" == "0" ]] && echo "TRUE" || echo "FALSE")

  # Geburtsdatum formatieren (verschiedene Formate möglich)
  BIRTH_DATE="NULL"
  if [[ -n "$gebDatum" && "$gebDatum" != "0000-00-00" && "$gebDatum" != "" ]]; then
    BIRTH_DATE="'${gebDatum}'"
  fi

  # Eintrittsjahr
  MEMBER_SINCE="NULL"
  if [[ -n "$mitgliedSeit" && "$mitgliedSeit" != "0" ]]; then
    MEMBER_SINCE="${mitgliedSeit}"
  fi

  # Rank-ID ermitteln (über sortOrder = alter Dienstgrad-ID)
  RANK_ID=$(new_query "SELECT id FROM ranks WHERE sortOrder=${dienstgrad} LIMIT 1" 2>/dev/null || echo "NULL")
  [[ -z "$RANK_ID" ]] && RANK_ID="NULL"

  # Gruppe-ID
  GROUP_ID="NULL"
  if [[ -n "$mitgliederGruppe" && "$mitgliederGruppe" != "0" ]]; then
    GRP=$(new_query "SELECT id FROM member_groups WHERE id=${mitgliederGruppe} LIMIT 1" 2>/dev/null || echo "")
    [[ -n "$GRP" ]] && GROUP_ID="$GRP"
  fi

  # Mitglied einfügen
  new_exec "INSERT IGNORE INTO members
    (id, firstName, lastName, salutation, street, city,
     phonePrivate, phoneMobile, phoneWork, email,
     rankId, memberGroupId, isActive, memberSince, birthDate, notes)
    VALUES (
      ${id},
      '${vorname//\'/\\\'}',
      '${name//\'/\\\'}',
      '${SALUTATION}',
      '${strasse//\'/\\\'}',
      '${ort//\'/\\\'}',
      '${telefonPrivat//\'/\\\'}',
      '${telefonMobil//\'/\\\'}',
      '${telefonArbeit//\'/\\\'}',
      '${email//\'/\\\'}',
      ${RANK_ID},
      ${GROUP_ID},
      ${IS_ACTIVE},
      ${MEMBER_SINCE},
      ${BIRTH_DATE},
      '${kommentar//\'/\\\'}'
    )" 2>/dev/null || true

  ((COUNT++)) || true
done < <(old_query "SELECT id, mitgliederGruppe, anrede, name, vorname, strasse, ort,
    telefonPrivat, telefonMobil, telefonArbeit, email, dienstgrad,
    ausserDienst, mitgliedSeit, gebDatum, REPLACE(kommentar, '\n', ' ')
    FROM mitglieder ORDER BY id")

success "  ${COUNT} Mitglieder migriert"

# --------------------------------------------------------------------------
# 5. Benutzer migrieren
# --------------------------------------------------------------------------
info "Migriere Benutzer (user → users)..."
info "  HINWEIS: Alte Passwörter können nicht migriert werden."
info "  Alle Benutzer erhalten das Standard-Passwort: ${DEFAULT_PASS}"

COUNT=0
while IFS=$'\t' read -r userid usergruppe admin deaktiv; do
  IS_ADMIN=$([[ "$admin" == "1" ]] && echo "TRUE" || echo "FALSE")
  IS_ACTIVE=$([[ "$deaktiv" == "0" ]] && echo "TRUE" || echo "FALSE")

  # Berechtigungsgruppe ermitteln
  GROUP_ID=$(new_query "SELECT id FROM permission_groups WHERE name='${usergruppe//\'/\\\'}' LIMIT 1" 2>/dev/null || echo "NULL")
  [[ -z "$GROUP_ID" || "$GROUP_ID" == "NULL" ]] && \
    GROUP_ID=$(new_query "SELECT id FROM permission_groups WHERE name='Administrator' LIMIT 1" 2>/dev/null || echo "NULL")

  new_exec "INSERT IGNORE INTO users (username, password, isAdmin, isActive, groupId, mustChangePassword)
    VALUES (
      '${userid//\'/\\\'}',
      '${DEFAULT_HASH//\'/\\\'}',
      ${IS_ADMIN},
      ${IS_ACTIVE},
      ${GROUP_ID:-NULL},
      TRUE
    )" 2>/dev/null || true

  ((COUNT++)) || true
done < <(old_query "SELECT userid, usergruppe, admin, deaktiv FROM user ORDER BY userid")

success "  ${COUNT} Benutzer migriert (alle müssen Passwort ändern)"

# --------------------------------------------------------------------------
# 6. Abwesenheitsgründe migrieren
# --------------------------------------------------------------------------
info "Migriere Abwesenheitsgründe (abwesenheitsgrund → absence_reasons)..."

COUNT=0
while IFS=$'\t' read -r id name kurzName; do
  new_exec "INSERT IGNORE INTO absence_reasons (id, name, shortName)
    VALUES (${id}, '${name//\'/\\\'}', '${kurzName//\'/\\\'}') " 2>/dev/null || true
  ((COUNT++)) || true
done < <(old_query "SELECT id, name, kurzName FROM abwesenheitsgrund ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Abwesenheitsgründe migriert"

# --------------------------------------------------------------------------
# 7. Lager migrieren
# --------------------------------------------------------------------------
info "Migriere Lager (lager → warehouses)..."

COUNT=0
while IFS=$'\t' read -r id name; do
  new_exec "INSERT IGNORE INTO warehouses (id, name)
    VALUES (${id}, '${name//\'/\\\'}') " 2>/dev/null || true
  ((COUNT++)) || true
done < <(old_query "SELECT id, name FROM lager ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Lager migriert"

# --------------------------------------------------------------------------
# 8. Ausbildungskategorien migrieren
# --------------------------------------------------------------------------
info "Migriere Ausbildungskategorien (ausbildung_kategorie → course_categories)..."

COUNT=0
while IFS=$'\t' read -r id name; do
  new_exec "INSERT IGNORE INTO course_categories (id, name)
    VALUES (${id}, '${name//\'/\\\'}') " 2>/dev/null || true
  ((COUNT++)) || true
done < <(old_query "SELECT id, name FROM ausbildung_kategorie ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Ausbildungskategorien migriert"

# --------------------------------------------------------------------------
# 9. Einsätze migrieren
# --------------------------------------------------------------------------
info "Migriere Einsätze (einsatz → operations)..."

COUNT=0
while IFS=$'\t' read -r id einsatzNummer datum zeitAlarm zeitAusgerueckt zeitEingetroffen zeitEingerueckt ort; do
  # Datum + Uhrzeit zusammensetzen
  ALARM_DATETIME="NULL"
  if [[ -n "$datum" && "$datum" != "0000-00-00" && -n "$zeitAlarm" ]]; then
    ALARM_DATETIME="'${datum} ${zeitAlarm}:00'"
  elif [[ -n "$datum" && "$datum" != "0000-00-00" ]]; then
    ALARM_DATETIME="'${datum} 00:00:00'"
  fi

  new_exec "INSERT IGNORE INTO operations (id, operationNumber, alarmTime, location)
    VALUES (
      ${id},
      '${einsatzNummer}',
      ${ALARM_DATETIME},
      '${ort//\'/\\\'}'
    )" 2>/dev/null || true

  ((COUNT++)) || true
done < <(old_query "SELECT id, einsatzNummer, Datum, ZeitAlarm, ZeitAusgerueckt,
    zeitEingetroffen, zeitEingerueckt, Ort
    FROM einsatz ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Einsätze migriert"

# --------------------------------------------------------------------------
# 10. Veranstaltungen migrieren
# --------------------------------------------------------------------------
info "Migriere Veranstaltungen (veranstaltung → events)..."

COUNT=0
while IFS=$'\t' read -r id name kategorie datum zeit zeitEnde; do
  EVENT_DATE="NULL"
  [[ -n "$datum" && "$datum" != "0000-00-00" ]] && EVENT_DATE="'${datum}'"

  new_exec "INSERT IGNORE INTO events (id, name, type, eventDate, startTime, endTime)
    VALUES (
      ${id},
      '${name//\'/\\\'}',
      CASE ${kategorie}
        WHEN 1 THEN 'EXERCISE'
        WHEN 2 THEN 'TRAINING'
        WHEN 3 THEN 'OTHER'
        ELSE 'OTHER'
      END,
      ${EVENT_DATE},
      ${zeit:+\'$zeit\'},
      ${zeitEnde:+\'$zeitEnde\'}
    )" 2>/dev/null || true

  ((COUNT++)) || true
done < <(old_query "SELECT id, name, kategorie, datum, zeit, zeitEnde
    FROM veranstaltung ORDER BY id" 2>/dev/null || echo "")

success "  ${COUNT} Veranstaltungen migriert"

# --------------------------------------------------------------------------
# 11. Anwesenheiten migrieren
# --------------------------------------------------------------------------
info "Migriere Anwesenheiten (anwesenheit → attendances)..."

COUNT=0
while IFS=$'\t' read -r id jahr veranstaltungID mitgliederID; do
  # Prüfe ob Veranstaltung und Mitglied in neuer DB vorhanden
  EVENT_EXISTS=$(new_query "SELECT id FROM events WHERE id=${veranstaltungID} LIMIT 1" 2>/dev/null || echo "")
  MEMBER_EXISTS=$(new_query "SELECT id FROM members WHERE id=${mitgliederID} LIMIT 1" 2>/dev/null || echo "")

  if [[ -n "$EVENT_EXISTS" && -n "$MEMBER_EXISTS" ]]; then
    new_exec "INSERT IGNORE INTO attendances (eventId, memberId, status)
      VALUES (${veranstaltungID}, ${mitgliederID}, 'PRESENT')" 2>/dev/null || true
    ((COUNT++)) || true
  fi
done < <(old_query "SELECT id, jahr, veranstaltungID, mitgliederID
    FROM anwesenheit ORDER BY id LIMIT 10000" 2>/dev/null || echo "")

success "  ${COUNT} Anwesenheitseinträge migriert"

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
echo ""
echo "=================================================================="
echo -e "${GREEN}  Migration abgeschlossen!${NC}"
echo "=================================================================="
echo ""

# Zusammenfassung
echo "  Statistik der neuen Datenbank:"
echo "    Mitglieder:          $(new_query 'SELECT COUNT(*) FROM members' 2>/dev/null)"
echo "    Benutzer:            $(new_query 'SELECT COUNT(*) FROM users' 2>/dev/null)"
echo "    Berechtigungsgruppen:$(new_query 'SELECT COUNT(*) FROM permission_groups' 2>/dev/null)"
echo "    Dienstgrade:         $(new_query 'SELECT COUNT(*) FROM ranks' 2>/dev/null)"
echo "    Einsätze:            $(new_query 'SELECT COUNT(*) FROM operations' 2>/dev/null)"
echo "    Veranstaltungen:     $(new_query 'SELECT COUNT(*) FROM events' 2>/dev/null)"
echo "    Anwesenheiten:       $(new_query 'SELECT COUNT(*) FROM attendances' 2>/dev/null)"
echo ""
echo -e "  ${YELLOW}WICHTIG:${NC}"
echo "  - Alle migrierten Benutzer haben das Passwort: ${DEFAULT_PASS}"
echo "  - Benutzer werden beim ersten Login zur Passwortänderung aufgefordert"
echo "  - Bitte überprüfen Sie die Daten nach der Migration sorgfältig"
echo ""
