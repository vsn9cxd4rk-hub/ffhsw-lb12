import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';
import { convertDates } from '../utils/dates';
import { pickWritable } from '../utils/body';

const DATE_FIELDS = ['birthDate', 'memberSince', 'memberUntil', 'marriageDate'];
const EXAMINATION_DATE_FIELDS = ['g25Date', 'g26Date', 'g30Date', 'agtTrainingDate', 'lkwLicenseExpiry'];
const COURSE_DATE_FIELDS = ['startDate', 'endDate'];
const AVAILABILITY_DATE_FIELDS = ['validFrom', 'validUntil'];
const ABSENCE_DATE_FIELDS = ['date'];

const MEMBER_INCLUDE = {
  group: true,
  family: true,
  work: true,
  bank: true,
  examination: true,
  availability: { orderBy: { validFrom: 'desc' as const }, take: 1 },
};

// Non-admins only need the member list for the personnel pickers (Einsatz, Lehrgang). They
// must not receive contact data, birth date, health insurance, medications/conditions etc.
const PICKER_SELECT = {
  id: true,
  firstName: true,
  lastName: true,
  rank: true,
  groupId: true,
  isInactive: true,
  qualLicenseC: true, qualLicenseB: true, qualFirstAid: true, qualRadioOperator: true,
  qualMachinist: true, qualTruppmann: true, qualTruppfuehrer: true, qualGruppenfuehrer: true,
  qualZugfuehrer: true, qualRettSan: true, qualFwSan: true, qualVerbandfuehrer: true,
  qualAGT: true, qualTH1: true,
  group: { select: { id: true, name: true } },
} as const;

export async function getMembers(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const { search, groupId, rank, isInactive } = req.query;

    const where: Record<string, unknown> = {
      deletedAt: null,
    };

    const isAdmin = !!req.user?.isAdmin;
    if (search) {
      where.OR = [
        { firstName: { contains: search as string } },
        { lastName: { contains: search as string } },
        // searching contact fields would let non-admins probe them
        ...(isAdmin ? [
          { email: { contains: search as string } },
          { phoneMobile: { contains: search as string } },
        ] : []),
      ];
    }
    if (groupId) where.groupId = parseInt(groupId as string);
    if (rank) where.rank = rank as string;
    if (isInactive !== undefined) where.isInactive = isInactive === 'true';

    const [members, total] = await Promise.all([
      isAdmin
        ? prisma.member.findMany({
          where,
          skip,
          take,
          include: { group: { select: { id: true, name: true } } },
          orderBy: [{ lastName: 'asc' }, { firstName: 'asc' }],
        })
        : prisma.member.findMany({
          where,
          skip,
          take,
          select: PICKER_SELECT,
          orderBy: [{ lastName: 'asc' }, { firstName: 'asc' }],
        }),
      prisma.member.count({ where }),
    ]);

    sendPaginated(res, members, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createMember(req: Request, res: Response): Promise<void> {
  try {
    const member = await prisma.member.create({
      data: convertDates(pickWritable('Member', req.body), DATE_FIELDS) as any,
      include: MEMBER_INCLUDE,
    });

    // Log creation in history
    await prisma.memberHistory.create({
      data: {
        memberId: member.id,
        field: 'Angelegt',
        oldValue: null,
        newValue: `${member.firstName} ${member.lastName}`,
        changedBy: req.user?.username || 'System',
      },
    });

    sendSuccess(res, member, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getMember(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const member = await prisma.member.findFirst({
      where: { id, deletedAt: null },
      include: {
        ...MEMBER_INCLUDE,
        courses: { include: { category: true }, orderBy: { createdAt: 'desc' } },
        availability: { orderBy: { validFrom: 'desc' } },
        absences: { orderBy: { date: 'desc' } },
      },
    });
    if (!member) { sendError(res, 'Mitglied nicht gefunden', 404); return; }
    sendSuccess(res, member);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMember(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const existing = await prisma.member.findFirst({ where: { id, deletedAt: null } });
    if (!existing) { sendError(res, 'Mitglied nicht gefunden', 404); return; }

    // Track changes for history
    const changes: Array<{ field: string; oldValue: string | null; newValue: string | null }> = [];
    const FIELD_LABELS: Record<string, string> = {
      firstName: 'Vorname', lastName: 'Nachname', salutation: 'Anrede', rank: 'Dienstgrad',
      groupId: 'Gruppe', street: 'Straße', city: 'Ort', phonePrivate: 'Telefon Privat',
      phoneMobile: 'Mobil', phoneWork: 'Telefon Arbeit', email: 'E-Mail', email2: 'E-Mail 2',
      birthDate: 'Geburtsdatum', memberSince: 'Mitglied seit', memberUntil: 'Mitglied bis',
      isInactive: 'Außer Dienst', comment: 'Kommentar', occupation: 'Beruf',
      nationality: 'Nationalität', driverLicenseNo: 'Führerschein-Nr.',
      serviceCardNo: 'Dienstausweis-Nr.', healthInsurance: 'Krankenkasse',
      qualLicenseC: 'FS Klasse C', qualLicenseB: 'FS Klasse B',
      qualFirstAid: 'Erste Hilfe', qualRadioOperator: 'Sprechfunker',
      qualMachinist: 'Maschinist', qualTruppmann: 'Truppmann',
      qualTruppfuehrer: 'Truppführer', qualGruppenfuehrer: 'Gruppenführer',
      qualZugfuehrer: 'Zugführer', qualRettSan: 'RettSan', qualFwSan: 'FwSan',
      qualVerbandfuehrer: 'Verbandsführer', qualAGT: 'AGT', qualTH1: 'TH1',
    };
    for (const field of Object.keys(FIELD_LABELS)) {
      if (req.body[field] === undefined) continue;
      const oldRaw = (existing as Record<string, unknown>)[field];
      const newRaw = req.body[field];
      const oldStr = oldRaw instanceof Date ? oldRaw.toISOString().substring(0, 10)
        : oldRaw === null || oldRaw === undefined ? '' : String(oldRaw);
      const newStr = newRaw === null || newRaw === undefined ? '' : String(newRaw);
      if (oldStr !== newStr) {
        const label = FIELD_LABELS[field];
        const formatVal = (v: string) => v === 'true' ? 'Ja' : v === 'false' ? 'Nein' : v || '-';
        changes.push({ field: label, oldValue: formatVal(oldStr), newValue: formatVal(newStr) });
      }
    }

    const member = await prisma.member.update({
      where: { id },
      data: convertDates(pickWritable('Member', req.body), DATE_FIELDS) as any,
      include: MEMBER_INCLUDE,
    });

    // Save history
    if (changes.length > 0) {
      await prisma.memberHistory.createMany({
        data: changes.map(c => ({
          memberId: id,
          field: c.field,
          oldValue: c.oldValue,
          newValue: c.newValue,
          changedBy: req.user!.username,
        })),
      });
    }

    sendSuccess(res, member);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteMember(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    await prisma.member.update({
      where: { id },
      data: { deletedAt: new Date(), isInactive: true },
    });
    sendSuccess(res, { message: 'Mitglied gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getMemberHistory(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const history = await prisma.memberHistory.findMany({
      where: { memberId: id },
      orderBy: { changedAt: 'desc' },
    });
    sendSuccess(res, history);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Family
export async function createMemberFamily(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const family = await prisma.memberFamily.create({
      data: { ...pickWritable('MemberFamily', req.body, ['memberId']), memberId } as any,
    });
    sendSuccess(res, family, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberFamily(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.familyId);
    const family = await prisma.memberFamily.update({
      where: { id },
      data: pickWritable('MemberFamily', req.body, ['memberId']),
    });
    sendSuccess(res, family);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteMemberFamily(req: Request, res: Response): Promise<void> {
  try {
    await prisma.memberFamily.delete({ where: { id: parseInt(req.params.familyId) } });
    sendSuccess(res, { message: 'Angehöriger gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Work
export async function upsertMemberWork(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const data = pickWritable('MemberWork', req.body, ['memberId']);
    const work = await prisma.memberWork.upsert({
      where: { memberId },
      update: data,
      create: { ...data, memberId },
    });
    sendSuccess(res, work);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Bank
export async function upsertMemberBank(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const data = pickWritable('MemberBank', req.body, ['memberId']);
    const bank = await prisma.memberBank.upsert({
      where: { memberId },
      update: data,
      create: { ...data, memberId },
    });
    sendSuccess(res, bank);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Examination
export async function upsertMemberExamination(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const data = convertDates(pickWritable('MemberExamination', req.body, ['memberId']), EXAMINATION_DATE_FIELDS);
    const exam = await prisma.memberExamination.upsert({
      where: { memberId },
      update: data,
      create: { ...data, memberId },
    });
    sendSuccess(res, exam);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Availability
export async function createMemberAvailability(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const av = await prisma.memberAvailability.create({
      data: { ...convertDates(pickWritable('MemberAvailability', req.body, ['memberId']), AVAILABILITY_DATE_FIELDS), memberId } as any,
    });
    sendSuccess(res, av, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Courses
export async function getMemberCourses(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const courses = await prisma.course.findMany({
      where: { memberId },
      include: { category: true },
      orderBy: { createdAt: 'desc' },
    });
    sendSuccess(res, courses);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

const VALID_QUALIFICATION_FIELDS = [
  'qualLicenseB', 'qualLicenseC', 'qualFirstAid', 'qualRadioOperator',
  'qualMachinist', 'qualTruppmann', 'qualTruppfuehrer', 'qualGruppenfuehrer',
  'qualZugfuehrer', 'qualRettSan', 'qualFwSan', 'qualVerbandfuehrer',
  'qualAGT', 'qualTH1',
];

async function applyQualificationFromCourse(memberId: number, categoryId: number, status: string) {
  if (status !== 'completed') return;
  const category = await prisma.courseCategory.findUnique({ where: { id: categoryId } });
  if (!category?.qualificationField) return;
  if (!VALID_QUALIFICATION_FIELDS.includes(category.qualificationField)) return;
  await prisma.member.update({
    where: { id: memberId },
    data: { [category.qualificationField]: true },
  });
}

export async function createMemberCourse(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const course = await prisma.course.create({
      data: { ...convertDates(pickWritable('Course', req.body, ['memberId']), COURSE_DATE_FIELDS), memberId } as any,
      include: { category: true },
    });
    await applyQualificationFromCourse(memberId, course.categoryId, course.status);
    sendSuccess(res, course, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberCourse(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.courseId);
    const course = await prisma.course.update({
      where: { id },
      data: convertDates(pickWritable('Course', req.body, ['memberId']), COURSE_DATE_FIELDS),
      include: { category: true },
    });
    await applyQualificationFromCourse(course.memberId, course.categoryId, course.status);
    sendSuccess(res, course);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Groups
export async function getMemberGroups(_req: Request, res: Response): Promise<void> {
  try {
    const groups = await prisma.memberGroup.findMany({ orderBy: { name: 'asc' } });
    sendSuccess(res, groups);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createMemberGroup(req: Request, res: Response): Promise<void> {
  try {
    const group = await prisma.memberGroup.create({ data: pickWritable('MemberGroup', req.body) as any });
    sendSuccess(res, group, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberGroup(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.groupId);
    const group = await prisma.memberGroup.update({ where: { id }, data: pickWritable('MemberGroup', req.body) });
    sendSuccess(res, group);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// AGT Records
export async function getAgtRecords(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const records = await prisma.agtRecord.findMany({
      where: { memberId },
      orderBy: { date: 'desc' },
    });
    sendSuccess(res, records);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createAgtRecord(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const { type, date, result, notes } = req.body;
    const record = await prisma.agtRecord.create({
      data: { memberId, type, date: new Date(date), result: result || null, notes: notes || null },
    });
    sendSuccess(res, record, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteAgtRecord(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.recordId);
    await prisma.agtRecord.delete({ where: { id } });
    sendSuccess(res, { message: 'Eintrag gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Absences
export async function createMemberAbsence(req: Request, res: Response): Promise<void> {
  try {
    const memberId = parseInt(req.params.id);
    const absence = await prisma.absence.create({
      data: { ...convertDates(pickWritable('Absence', req.body, ['memberId']), ABSENCE_DATE_FIELDS), memberId } as any,
    });
    sendSuccess(res, absence, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberAbsence(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.absenceId);
    const absence = await prisma.absence.update({
      where: { id },
      data: convertDates(pickWritable('Absence', req.body, ['memberId']), ABSENCE_DATE_FIELDS) as any,
    });
    sendSuccess(res, absence);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteMemberAbsence(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.absenceId);
    await prisma.absence.delete({ where: { id } });
    sendSuccess(res, { message: 'Abwesenheit gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
