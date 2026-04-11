"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getMembers = getMembers;
exports.createMember = createMember;
exports.getMember = getMember;
exports.updateMember = updateMember;
exports.deleteMember = deleteMember;
exports.getMemberHistory = getMemberHistory;
exports.createMemberFamily = createMemberFamily;
exports.updateMemberFamily = updateMemberFamily;
exports.deleteMemberFamily = deleteMemberFamily;
exports.upsertMemberWork = upsertMemberWork;
exports.upsertMemberBank = upsertMemberBank;
exports.upsertMemberExamination = upsertMemberExamination;
exports.createMemberAvailability = createMemberAvailability;
exports.getMemberCourses = getMemberCourses;
exports.createMemberCourse = createMemberCourse;
exports.updateMemberCourse = updateMemberCourse;
exports.getMemberGroups = getMemberGroups;
exports.createMemberGroup = createMemberGroup;
exports.updateMemberGroup = updateMemberGroup;
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
const DATE_FIELDS = ['birthDate', 'memberSince', 'memberUntil', 'marriageDate'];
function convertDates(data) {
    const result = { ...data };
    for (const field of DATE_FIELDS) {
        if (result[field] && typeof result[field] === 'string' && !result[field].includes('T')) {
            result[field] = new Date(result[field]).toISOString();
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
    availability: { orderBy: { validFrom: 'desc' }, take: 1 },
};
async function getMembers(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const { search, groupId, rank, isInactive } = req.query;
        const where = {
            deletedAt: null,
        };
        if (search) {
            where.OR = [
                { firstName: { contains: search } },
                { lastName: { contains: search } },
                { email: { contains: search } },
                { phoneMobile: { contains: search } },
            ];
        }
        if (groupId)
            where.groupId = parseInt(groupId);
        if (rank)
            where.rank = rank;
        if (isInactive !== undefined)
            where.isInactive = isInactive === 'true';
        const [members, total] = await Promise.all([
            database_1.prisma.member.findMany({
                where,
                skip,
                take,
                include: { group: { select: { id: true, name: true } } },
                orderBy: [{ lastName: 'asc' }, { firstName: 'asc' }],
            }),
            database_1.prisma.member.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, members, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createMember(req, res) {
    try {
        const member = await database_1.prisma.member.create({
            data: convertDates(req.body),
            include: MEMBER_INCLUDE,
        });
        // Log creation in history
        await database_1.prisma.memberHistory.create({
            data: {
                memberId: member.id,
                field: 'Angelegt',
                oldValue: null,
                newValue: `${member.firstName} ${member.lastName}`,
                changedBy: req.user?.username || 'System',
            },
        });
        (0, response_1.sendSuccess)(res, member, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getMember(req, res) {
    try {
        const id = parseInt(req.params.id);
        const member = await database_1.prisma.member.findFirst({
            where: { id, deletedAt: null },
            include: {
                ...MEMBER_INCLUDE,
                courses: { include: { category: true }, orderBy: { createdAt: 'desc' } },
                availability: { orderBy: { validFrom: 'desc' } },
            },
        });
        if (!member) {
            (0, response_1.sendError)(res, 'Mitglied nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, member);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateMember(req, res) {
    try {
        const id = parseInt(req.params.id);
        const existing = await database_1.prisma.member.findFirst({ where: { id, deletedAt: null } });
        if (!existing) {
            (0, response_1.sendError)(res, 'Mitglied nicht gefunden', 404);
            return;
        }
        // Track changes for history
        const changes = [];
        const FIELD_LABELS = {
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
            if (req.body[field] === undefined)
                continue;
            const oldRaw = existing[field];
            const newRaw = req.body[field];
            const oldStr = oldRaw instanceof Date ? oldRaw.toISOString().substring(0, 10)
                : oldRaw === null || oldRaw === undefined ? '' : String(oldRaw);
            const newStr = newRaw === null || newRaw === undefined ? '' : String(newRaw);
            if (oldStr !== newStr) {
                const label = FIELD_LABELS[field];
                const formatVal = (v) => v === 'true' ? 'Ja' : v === 'false' ? 'Nein' : v || '-';
                changes.push({ field: label, oldValue: formatVal(oldStr), newValue: formatVal(newStr) });
            }
        }
        const member = await database_1.prisma.member.update({
            where: { id },
            data: convertDates(req.body),
            include: MEMBER_INCLUDE,
        });
        // Save history
        if (changes.length > 0) {
            await database_1.prisma.memberHistory.createMany({
                data: changes.map(c => ({
                    memberId: id,
                    field: c.field,
                    oldValue: c.oldValue,
                    newValue: c.newValue,
                    changedBy: req.user.username,
                })),
            });
        }
        (0, response_1.sendSuccess)(res, member);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteMember(req, res) {
    try {
        const id = parseInt(req.params.id);
        await database_1.prisma.member.update({
            where: { id },
            data: { deletedAt: new Date(), isInactive: true },
        });
        (0, response_1.sendSuccess)(res, { message: 'Mitglied gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getMemberHistory(req, res) {
    try {
        const id = parseInt(req.params.id);
        const history = await database_1.prisma.memberHistory.findMany({
            where: { memberId: id },
            orderBy: { changedAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, history);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Family
async function createMemberFamily(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const family = await database_1.prisma.memberFamily.create({ data: { ...req.body, memberId } });
        (0, response_1.sendSuccess)(res, family, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateMemberFamily(req, res) {
    try {
        const id = parseInt(req.params.familyId);
        const family = await database_1.prisma.memberFamily.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, family);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteMemberFamily(req, res) {
    try {
        await database_1.prisma.memberFamily.delete({ where: { id: parseInt(req.params.familyId) } });
        (0, response_1.sendSuccess)(res, { message: 'Angehöriger gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Work
async function upsertMemberWork(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const work = await database_1.prisma.memberWork.upsert({
            where: { memberId },
            update: req.body,
            create: { ...req.body, memberId },
        });
        (0, response_1.sendSuccess)(res, work);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Bank
async function upsertMemberBank(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const bank = await database_1.prisma.memberBank.upsert({
            where: { memberId },
            update: req.body,
            create: { ...req.body, memberId },
        });
        (0, response_1.sendSuccess)(res, bank);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Examination
async function upsertMemberExamination(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const exam = await database_1.prisma.memberExamination.upsert({
            where: { memberId },
            update: req.body,
            create: { ...req.body, memberId },
        });
        (0, response_1.sendSuccess)(res, exam);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Availability
async function createMemberAvailability(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const av = await database_1.prisma.memberAvailability.create({ data: { ...req.body, memberId } });
        (0, response_1.sendSuccess)(res, av, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Courses
async function getMemberCourses(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const courses = await database_1.prisma.course.findMany({
            where: { memberId },
            include: { category: true },
            orderBy: { createdAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, courses);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createMemberCourse(req, res) {
    try {
        const memberId = parseInt(req.params.id);
        const course = await database_1.prisma.course.create({
            data: { ...req.body, memberId },
            include: { category: true },
        });
        (0, response_1.sendSuccess)(res, course, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateMemberCourse(req, res) {
    try {
        const id = parseInt(req.params.courseId);
        const course = await database_1.prisma.course.update({
            where: { id },
            data: req.body,
            include: { category: true },
        });
        (0, response_1.sendSuccess)(res, course);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Groups
async function getMemberGroups(_req, res) {
    try {
        const groups = await database_1.prisma.memberGroup.findMany({ orderBy: { name: 'asc' } });
        (0, response_1.sendSuccess)(res, groups);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createMemberGroup(req, res) {
    try {
        const group = await database_1.prisma.memberGroup.create({ data: req.body });
        (0, response_1.sendSuccess)(res, group, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateMemberGroup(req, res) {
    try {
        const id = parseInt(req.params.groupId);
        const group = await database_1.prisma.memberGroup.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, group);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=member.controller.js.map