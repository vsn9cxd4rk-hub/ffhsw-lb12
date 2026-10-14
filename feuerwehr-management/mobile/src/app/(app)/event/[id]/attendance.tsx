import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { Pressable, ScrollView, StyleSheet, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { eventsApi } from '@/api/client';
import type { AttendanceList, AttendanceStatus } from '@/api/types';
import { BIT_EVENTS, hasPermission } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { Section } from '@/components/ui/section';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';
import { ATTENDANCE_LABELS, eventSubtitle, memberName } from '@/lib/labels';

// Short button texts; the full word is the accessibility label
const BUTTONS: { status: AttendanceStatus; short: string; color: 'success' | 'warning' | 'danger' }[] = [
  { status: 'present', short: 'A', color: 'success' },
  { status: 'excused', short: 'E', color: 'warning' },
  { status: 'absent', short: 'X', color: 'danger' },
];

export default function AttendanceScreen() {
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const attendance = useQuery({ queryKey: ['events', id, 'attendance'], queryFn: () => eventsApi.getAttendance(id) });

  return (
    <ThemedView style={styles.container}>
      <QueryState isPending={attendance.isPending} error={attendance.error} />
      {/* Mounted once the list is there, so the editable state starts from the saved statuses */}
      {attendance.data && <AttendanceEditor eventId={id} list={attendance.data} />}
    </ThemedView>
  );
}

function AttendanceEditor({ eventId, list }: { eventId: number; list: AttendanceList }) {
  const queryClient = useQueryClient();
  const { user } = useSession();
  const canEdit = hasPermission(user, BIT_EVENTS);
  const [statuses, setStatuses] = useState<Record<number, AttendanceStatus>>(() =>
    Object.fromEntries(list.members.filter((m) => m.status).map((m) => [m.memberId, m.status as AttendanceStatus])),
  );
  const [dirty, setDirty] = useState(false);
  const [search, setSearch] = useState('');

  function setStatus(memberId: number, status: AttendanceStatus) {
    setStatuses((prev) => ({ ...prev, [memberId]: status }));
    setDirty(true);
  }

  // Typical Dienstabend: tick who is there, then mark everybody else absent in one go
  function markRestAbsent() {
    setStatuses((prev) => {
      const next = { ...prev };
      for (const m of list.members) if (!next[m.memberId]) next[m.memberId] = 'absent';
      return next;
    });
    setDirty(true);
  }

  const save = useMutation({
    mutationFn: () =>
      eventsApi.saveAttendance(
        eventId,
        Object.entries(statuses).map(([memberId, status]) => ({ memberId: Number(memberId), status })),
      ),
    onSuccess: () => {
      setDirty(false);
      queryClient.invalidateQueries({ queryKey: ['events', eventId] });
    },
  });

  const term = search.trim().toLowerCase();
  const members = list.members.filter((m) => !term || memberName(m.member).toLowerCase().includes(term));
  const count = (status: AttendanceStatus) => Object.values(statuses).filter((s) => s === status).length;
  const openCount = list.members.length - Object.keys(statuses).length;

  return (
    <>
      <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
        <ThemedText type="small" themeColor="textSecondary">
          {list.event.name} · {eventSubtitle(list.event)}
        </ThemedText>
        {!canEdit && (
          <ThemedText type="small" themeColor="warning">
            Nur Ansicht: Zum Erfassen der Anwesenheit fehlt das Recht „Veranstaltungen pflegen“.
          </ThemedText>
        )}
        <TextField label="Suche" value={search} onChangeText={setSearch} placeholder="Name" autoCorrect={false} />
        <Section title={`Mitglieder (${list.members.length})`} empty="Keine Treffer">
          {members.map((m) => (
            <View key={m.memberId} style={styles.row}>
              <View style={styles.name}>
                <ThemedText type="small">{memberName(m.member)}</ThemedText>
                {m.member.rank ? (
                  <ThemedText type="small" themeColor="textSecondary">
                    {m.member.rank}
                  </ThemedText>
                ) : null}
              </View>
              {BUTTONS.map((b) => (
                <StatusButton
                  key={b.status}
                  label={b.short}
                  accessibilityLabel={`${memberName(m.member)}: ${ATTENDANCE_LABELS[b.status]}`}
                  color={b.color}
                  selected={statuses[m.memberId] === b.status}
                  disabled={!canEdit}
                  onPress={() => setStatus(m.memberId, b.status)}
                />
              ))}
            </View>
          ))}
        </Section>
        <ThemedText type="small" themeColor="textSecondary">
          A = anwesend, E = entschuldigt, X = abwesend
        </ThemedText>
      </ScrollView>

      <ThemedView type="backgroundElement" style={styles.footer}>
        <SafeAreaView edges={['bottom']} style={styles.footerInner}>
          <ThemedText type="small">
            {count('present')} anwesend · {count('excused')} entschuldigt · {count('absent')} abwesend
            {openCount > 0 ? ` · ${openCount} offen` : ''}
          </ThemedText>
          {save.error && (
            <ThemedText type="small" themeColor="danger">
              {save.error instanceof TypeError ? 'Server nicht erreichbar' : save.error.message}
            </ThemedText>
          )}
          {save.isSuccess && !dirty && (
            <ThemedText type="small" themeColor="success">
              Gespeichert.
            </ThemedText>
          )}
          {canEdit && (
            <View style={styles.actions}>
              <View style={styles.action}>
                <Button title="Übrige abwesend" variant="secondary" onPress={markRestAbsent} disabled={openCount === 0} />
              </View>
              <View style={styles.action}>
                <Button title="Speichern" onPress={() => save.mutate()} disabled={!dirty} busy={save.isPending} />
              </View>
            </View>
          )}
        </SafeAreaView>
      </ThemedView>
    </>
  );
}

function StatusButton({
  label,
  accessibilityLabel,
  color,
  selected,
  disabled,
  onPress,
}: {
  label: string;
  accessibilityLabel: string;
  color: 'success' | 'warning' | 'danger';
  selected: boolean;
  disabled: boolean;
  onPress: () => void;
}) {
  const theme = useTheme();
  return (
    <Pressable
      accessibilityRole="radio"
      accessibilityLabel={accessibilityLabel}
      accessibilityState={{ selected, disabled }}
      disabled={disabled}
      onPress={onPress}
      hitSlop={Spacing.one}
      style={[
        styles.statusButton,
        {
          borderColor: selected ? theme[color] : theme.backgroundSelected,
          backgroundColor: selected ? theme[color] : theme.background,
        },
      ]}>
      <ThemedText type="smallBold" style={selected ? styles.selectedText : undefined}>
        {label}
      </ThemedText>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: {
    padding: Spacing.three,
    gap: Spacing.three,
    width: '100%',
    maxWidth: MaxContentWidth,
    alignSelf: 'center',
  },
  row: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  name: { flex: 1 },
  statusButton: {
    width: 44,
    height: 40,
    borderWidth: 1,
    borderRadius: Spacing.two,
    alignItems: 'center',
    justifyContent: 'center',
  },
  selectedText: { color: '#ffffff' },
  footer: { paddingHorizontal: Spacing.three, paddingTop: Spacing.two },
  footerInner: { width: '100%', maxWidth: MaxContentWidth, alignSelf: 'center', gap: Spacing.one, paddingBottom: Spacing.two },
  actions: { flexDirection: 'row', gap: Spacing.two },
  action: { flex: 1 },
});
