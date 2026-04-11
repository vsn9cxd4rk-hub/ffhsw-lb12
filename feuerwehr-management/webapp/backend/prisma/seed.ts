import { PrismaClient } from '@prisma/client';
import bcrypt from 'bcryptjs';

const prisma = new PrismaClient();

async function main() {
  console.log('Seeding database...');

  // Create permission groups
  const adminGroup = await prisma.permissionGroup.upsert({
    where: { id: 1 },
    update: {},
    create: {
      id: 1,
      name: 'Administrator',
      description: 'Vollzugriff auf alle Funktionen',
      br0: true, br1: true, br2: true, br3: true, br4: true, br5: true,
      br6: true, br7: true, br8: true, br9: true, br10: true, br11: true,
      br12: true, br13: true, br14: true, br15: true, br16: true, br17: true,
      br18: true, br19: true, br20: true, br21: true, br22: true, br23: true,
      br24: true, br25: true, br26: true, br27: true, br28: true, br29: true,
      br30: true, br31: true, br32: true, br33: true, br34: true, br35: true,
      br36: true, br37: true, br38: true, br39: true, br40: true, br41: true,
      br42: true, br43: true, br44: true, br45: true, br46: true, br47: true,
      br48: true, br49: true, br50: true, br51: true, br52: true, br53: true,
      br54: true, br55: true, br56: true, br57: true, br58: true, br59: true,
      br60: true, br61: true, br62: true, br63: true, br64: true, br65: true,
      br66: true, br67: true, br68: true, br69: true, br70: true, br71: true,
      br72: true, br73: true, br74: true, br75: true,
    },
  });

  const geraetewarteGroup = await prisma.permissionGroup.upsert({
    where: { id: 2 },
    update: { name: 'Gerätewarte', description: 'Zugriff auf Fahrzeuge, Bestandsliste und Prüfbuch' },
    create: {
      id: 2,
      name: 'Gerätewarte',
      description: 'Zugriff auf Fahrzeuge, Bestandsliste und Prüfbuch',
    },
  });

  const userGroup = await prisma.permissionGroup.upsert({
    where: { id: 3 },
    update: { name: 'Benutzer', description: 'Zugriff auf eigene Daten, Veranstaltungen und Ausbildung' },
    create: {
      id: 3,
      name: 'Benutzer',
      description: 'Zugriff auf eigene Daten, Veranstaltungen und Ausbildung',
    },
  });

  const machinistenGroup = await prisma.permissionGroup.upsert({
    where: { id: 4 },
    update: { name: 'Maschinisten', description: 'Wie Benutzer, zusätzlich Fahrzeuge und Fahrtenbuch' },
    create: {
      id: 4,
      name: 'Maschinisten',
      description: 'Wie Benutzer, zusätzlich Fahrzeuge und Fahrtenbuch',
    },
  });

  const gruppenfuehrerGroup = await prisma.permissionGroup.upsert({
    where: { id: 5 },
    update: { name: 'Gruppenführer', description: 'Wie Benutzer, zusätzlich Berichte bei Einsätzen und Veranstaltungen' },
    create: {
      id: 5,
      name: 'Gruppenführer',
      description: 'Wie Benutzer, zusätzlich Berichte bei Einsätzen und Veranstaltungen',
    },
  });

  console.log('Permission groups created:', adminGroup.name, geraetewarteGroup.name, userGroup.name, machinistenGroup.name, gruppenfuehrerGroup.name);

  // Create admin user
  const hashedPassword = await bcrypt.hash('Admin123!', 12);
  const admin = await prisma.user.upsert({
    where: { username: 'admin' },
    update: {},
    create: {
      username: 'admin',
      email: 'admin@feuerwehr.local',
      password: hashedPassword,
      name: 'Administrator',
      isAdmin: true,
      isActive: true,
      groupId: adminGroup.id,
    },
  });
  console.log('Admin user created:', admin.username);

  // Create ranks (Dienstgrade)
  const ranks = [
    { name: 'Feuerwehrmann-Anwärter', abbreviation: 'FwA', sortOrder: 1 },
    { name: 'Feuerwehrmann', abbreviation: 'Fw', sortOrder: 2 },
    { name: 'Oberfeuerwehrmann', abbreviation: 'OFw', sortOrder: 3 },
    { name: 'Hauptfeuerwehrmann', abbreviation: 'HFw', sortOrder: 4 },
    { name: 'Unterbrandmeister', abbreviation: 'UBM', sortOrder: 5 },
    { name: 'Brandmeister', abbreviation: 'BM', sortOrder: 6 },
    { name: 'Oberbrandmeister', abbreviation: 'OBM', sortOrder: 7 },
    { name: 'Hauptbrandmeister', abbreviation: 'HBM', sortOrder: 8 },
    { name: 'Brandinspektor', abbreviation: 'BI', sortOrder: 9 },
    { name: 'Brandoberinspektor', abbreviation: 'BOI', sortOrder: 10 },
    { name: 'Brandamtmann', abbreviation: 'BAM', sortOrder: 11 },
    { name: 'Brandamtsrat', abbreviation: 'BAR', sortOrder: 12 },
    { name: 'Branddirektor', abbreviation: 'BD', sortOrder: 13 },
  ];

  for (const rank of ranks) {
    await prisma.rank.upsert({
      where: { id: ranks.indexOf(rank) + 1 },
      update: {},
      create: rank,
    });
  }
  console.log('Ranks created:', ranks.length);

  // Create course categories (Lehrgang-Kategorien)
  const categories = [
    { name: 'Führerschein', description: 'Führerscheinausbildung' },
    { name: 'Erste Hilfe', description: 'Erste-Hilfe-Kurs' },
    { name: 'Truppführer', description: 'Truppführer-Lehrgang' },
    { name: 'Gruppenführer', description: 'Gruppenführer-Lehrgang' },
    { name: 'Zugführer', description: 'Zugführer-Lehrgang' },
    { name: 'Sprechfunker', description: 'Sprechfunker-Ausbildung' },
    { name: 'Atemschutzgeräteträger', description: 'Atemschutz-Ausbildung' },
    { name: 'Absturzsicherung', description: 'Absturzsicherungs-Ausbildung' },
    { name: 'Kettensäge', description: 'Kettensägen-Ausbildung' },
    { name: 'TM1', description: 'Technische Hilfeleistung' },
    { name: 'CBRN-Schutz', description: 'CBRN-Schutz-Ausbildung' },
    { name: 'Wasserrettung', description: 'Wasserrettungs-Ausbildung' },
    { name: 'Maschinisten', description: 'Maschinisten-Ausbildung' },
    { name: 'Drehleiter', description: 'Drehleiter-Ausbildung' },
    { name: 'Sonstiges', description: 'Sonstige Ausbildungen' },
  ];

  for (const cat of categories) {
    await prisma.courseCategory.upsert({
      where: { id: categories.indexOf(cat) + 1 },
      update: {},
      create: { ...cat, id: categories.indexOf(cat) + 1 },
    });
  }
  console.log('Course categories created:', categories.length);

  // Create absence reasons
  const absenceReasons = [
    { name: 'Undefiniert', color: 'gray' },
    { name: 'Unentschuldigt', color: 'red' },
    { name: 'Entschuldigt', color: 'yellow' },
    { name: 'Urlaub', color: 'blue' },
    { name: 'Krank', color: 'orange' },
    { name: 'BSW', color: 'green' },
  ];

  for (const reason of absenceReasons) {
    await prisma.absenceReason.upsert({
      where: { id: absenceReasons.indexOf(reason) + 1 },
      update: {},
      create: { ...reason, id: absenceReasons.indexOf(reason) + 1 },
    });
  }
  console.log('Absence reasons created:', absenceReasons.length);

  // Create member groups
  const memberGroups = [
    { id: 1, name: 'Einsatzabteilung', nextEmployeeNumber: 1 },
    { id: 2, name: 'Jugendfeuerwehr', nextEmployeeNumber: 1 },
    { id: 3, name: 'Altersabteilung', nextEmployeeNumber: 1 },
    { id: 4, name: 'passive Mitglieder', nextEmployeeNumber: 1 },
  ];
  for (const group of memberGroups) {
    await prisma.memberGroup.upsert({
      where: { id: group.id },
      update: { name: group.name },
      create: group,
    });
  }
  console.log('Member groups created:', memberGroups.length);

  // Create initial year
  const currentYear = new Date().getFullYear();
  await prisma.year.upsert({
    where: { year: currentYear },
    update: {},
    create: {
      year: currentYear,
      isActive: true,
    },
  });

  // Create default settings
  const defaultSettings = [
    { key: 'fireStationName', value: 'Freiwillige Feuerwehr', description: 'Name der Feuerwehr' },
    { key: 'fireStationCity', value: 'Musterstadt', description: 'Stadt' },
    { key: 'fireStationZip', value: '12345', description: 'PLZ' },
    { key: 'fireStationStreet', value: 'Feuerwehrstr. 1', description: 'Straße' },
    { key: 'fireStationPhone', value: '', description: 'Telefon' },
    { key: 'fireStationEmail', value: '', description: 'E-Mail' },
    { key: 'inspectionReminderDays', value: '30', description: 'Erinnerung vor Prüfung (Tage)' },
    { key: 'medicalExamReminderDays', value: '60', description: 'Erinnerung vor Untersuchung (Tage)' },
  ];

  for (const setting of defaultSettings) {
    await prisma.setting.upsert({
      where: { key: setting.key },
      update: {},
      create: setting,
    });
  }
  console.log('Default settings created');

  // Create device classes with subclasses and inspection criteria
  const deviceClassesData = [
    {
      name: 'PSA',
      sortOrder: 1,
      subclasses: [
        { name: 'Helme', sortOrder: 1, criteria: ['Zustand Helmschale', 'Innenausstattung', 'Visier/Gesichtsschutz', 'Nackenschutz', 'Kennzeichnung'] },
        { name: 'Schutzkleidung TH', sortOrder: 2, criteria: ['Zustand Obermaterial', 'Nähte', 'Verschlüsse', 'Reflexstreifen', 'Kennzeichnung'] },
        { name: 'Schutzkleidung Brandbekämpfung', sortOrder: 3, criteria: ['Zustand Obermaterial', 'Nähte', 'Verschlüsse', 'Reflexstreifen', 'Feuchtesperre', 'Kennzeichnung'] },
        { name: 'Schutzkleidung sonstige', sortOrder: 4, criteria: ['Zustand Obermaterial', 'Nähte', 'Verschlüsse', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Erste Hilfe & Hygiene',
      sortOrder: 2,
      subclasses: [
        { name: 'Sanitäts- & Wiederbelebungsgeräte', sortOrder: 1, criteria: ['Vollständigkeit', 'Zustand Geräte', 'Verfallsdaten', 'Funktionsprüfung', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Signal- & Beleuchtungsgeräte',
      sortOrder: 3,
      subclasses: [
        { name: 'Funkgeräte & Melder', sortOrder: 1, criteria: ['Zustand Gehäuse', 'Akku/Batterie', 'Funktionsprüfung', 'Antenne', 'Kennzeichnung'] },
        { name: 'Geräte Verkehrssicherung', sortOrder: 2, criteria: ['Zustand', 'Leuchtmittel', 'Funktionsprüfung', 'Kennzeichnung'] },
        { name: 'Signal- & Beleuchtungsgeräte', sortOrder: 3, criteria: ['Zustand Gehäuse', 'Leuchtmittel', 'Akku/Batterie', 'Funktionsprüfung', 'Kabel und Stecker', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Arbeitsgeräte',
      sortOrder: 4,
      subclasses: [
        { name: 'Geräte & Werkzeuge', sortOrder: 1, criteria: ['Zustand', 'Vollständigkeit', 'Funktionsprüfung', 'Kennzeichnung'] },
        { name: 'Pumpen', sortOrder: 2, criteria: ['Zustand Gehäuse', 'Dichtungen', 'Funktionsprüfung', 'Ölstand', 'Kraftstoff', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Löschgeräte',
      sortOrder: 5,
      subclasses: [
        { name: 'Schläuche', sortOrder: 1, criteria: ['Zustand Schlauch', 'Kupplungen', 'Dichtungen', 'Druckprüfung', 'Kennzeichnung'] },
        { name: 'Löschgeräte', sortOrder: 2, criteria: ['Zustand', 'Vollständigkeit', 'Funktionsprüfung', 'Kennzeichnung'] },
        { name: 'Tragbare Feuerlöscher', sortOrder: 3, criteria: ['Zustand Behälter', 'Schlauch/Düse', 'Manometer/Druck', 'Plombierung', 'Prüfdatum', 'Kennzeichnung'] },
        { name: 'Wasserführende Armaturen', sortOrder: 4, criteria: ['Zustand', 'Kupplungen', 'Dichtungen', 'Funktionsprüfung', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Rettungsgeräte',
      sortOrder: 6,
      subclasses: [
        { name: 'Feuerwehrhaltegurte', sortOrder: 1, criteria: ['Zustand Gurt', 'Karabiner', 'Nähte', 'Kennzeichnung'] },
        { name: 'Feuerwehrleinen', sortOrder: 2, criteria: ['Zustand Leine', 'Karabiner', 'Leinenbeutel', 'Kennzeichnung'] },
        { name: 'Rettungsgeräte', sortOrder: 3, criteria: ['Zustand', 'Vollständigkeit', 'Funktionsprüfung', 'Kennzeichnung'] },
        { name: 'Spanngurte & Seile', sortOrder: 4, criteria: ['Zustand', 'Verschlüsse/Haken', 'Kennzeichnung'] },
        { name: 'Tragbare Leitern', sortOrder: 5, criteria: ['Zustand Holme', 'Sprossen', 'Gelenke/Verschlüsse', 'Standfüße', 'Kennzeichnung'] },
      ],
    },
    {
      name: 'Elektrische Geräte',
      sortOrder: 7,
      subclasses: [
        { name: 'Elektrische Geräte', sortOrder: 1, criteria: ['Zustand Gehäuse', 'Einspannfutter', 'Werkzeug', 'Ersatztrennscheiben', 'Kabel und Stecker', 'elektr. Prüfung'] },
      ],
    },
    {
      name: 'Geräte & Fahrzeuge im GH',
      sortOrder: 8,
      subclasses: [
        { name: 'Geräte & Fahrzeuge', sortOrder: 1, criteria: ['Zustand', 'Vollständigkeit', 'Funktionsprüfung', 'Kennzeichnung'] },
      ],
    },
  ];

  for (const dc of deviceClassesData) {
    const deviceClass = await prisma.deviceClass.upsert({
      where: { name: dc.name },
      update: { sortOrder: dc.sortOrder },
      create: { name: dc.name, sortOrder: dc.sortOrder },
    });

    for (const sc of dc.subclasses) {
      const subclass = await prisma.deviceSubclass.upsert({
        where: { deviceClassId_name: { deviceClassId: deviceClass.id, name: sc.name } },
        update: { sortOrder: sc.sortOrder },
        create: { deviceClassId: deviceClass.id, name: sc.name, sortOrder: sc.sortOrder },
      });

      for (let i = 0; i < sc.criteria.length; i++) {
        const criterionName = sc.criteria[i];
        const existing = await prisma.inspectionCriterion.findFirst({
          where: { deviceSubclassId: subclass.id, name: criterionName },
        });
        if (!existing) {
          await prisma.inspectionCriterion.create({
            data: { deviceSubclassId: subclass.id, name: criterionName, sortOrder: i + 1 },
          });
        }
      }
    }
  }
  console.log('Device classes with subclasses and criteria created');

  console.log('\nSeed completed successfully!');
  console.log('Default admin credentials:');
  console.log('  Username: admin');
  console.log('  Password: Admin123!');
  console.log('\nIMPORTANT: Change the admin password immediately after first login!');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
