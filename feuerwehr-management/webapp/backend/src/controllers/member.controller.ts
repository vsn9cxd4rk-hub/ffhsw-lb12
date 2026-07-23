import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

const DATE_FIELDS = ['birthDate', 'memberSince', 'memberUntil', 'marriageDate'];

function convertDates(data: Record<string, unknown>): Record<string, unknown> {
  const result = { ...data };
  for (const field of DATE_FIELDS) {
    if (result[field] && typeof result[field] === 'string' && !(result[field] as string).includes('T')) {
      result[field] = new Date(result[field] as string).toISOString();
    }
  }
  return result;
}

const MEMBER_INCLUDE = {
  group: true,
  family: true,
  work: true,
  bank: true,
  examination: true,
  availability: { orderBy: { validFrom: 'desc' as const }, take: 1 },
};

export async function getMembers(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const { search, groupId, rank, isInactive } = req.query;

    const where: Record<string, unknown> = {
      deletedAt: null,
    };

    if (search) {
      where.OR = [
        { firstName: { contains: search as string } },
        { lastName: { contains: search as string } },
        { email: { contains: search as string } },
        { phoneMobile: { contains: search as string } },
      ];
    }
    if (groupId) where.groupId = parseInt(groupId as string);
    if (rank) where.rank = rank as string;
    if (isInactive !== undefined) where.isInactive = isInactive === 'true';

    const [members, total] = await Promise.all([
      prisma.member.findMany({
        where,
        skip,
        take,
        include: { group: { select: { id: true, name: true } } },
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
      data: convertDates(req.body) as any,
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
      data: convertDates(req.body) as any,
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
    const family = await prisma.memberFamily.create({ data: { ...req.body, memberId } });
    sendSuccess(res, family, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberFamily(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.familyId);
    const family = await prisma.memberFamily.update({ where: { id }, data: req.body });
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
    const work = await prisma.memberWork.upsert({
      where: { memberId },
      update: req.body,
      create: { ...req.body, memberId },
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
    const bank = await prisma.memberBank.upsert({
      where: { memberId },
      update: req.body,
      create: { ...req.body, memberId },
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
    const exam = await prisma.memberExamination.upsert({
      where: { memberId },
      update: req.body,
      create: { ...req.body, memberId },
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
    const av = await prisma.memberAvailability.create({ data: { ...req.body, memberId } });
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
      data: { ...req.body, memberId },
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
      data: req.body,
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
    const group = await prisma.memberGroup.create({ data: req.body });
    sendSuccess(res, group, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateMemberGroup(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.groupId);
    const group = await prisma.memberGroup.update({ where: { id }, data: req.body });
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
