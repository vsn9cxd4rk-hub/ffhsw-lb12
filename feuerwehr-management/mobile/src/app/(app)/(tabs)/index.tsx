import { useQuery } from '@tanstack/react-query';
import { RefreshControl, ScrollView, StyleSheet, View } from 'react-native';

import { dashboardApi } from '@/api/client';
import type { DashboardStats, Notification, User } from '@/api/types';
import { BIT_EQUIPMENT, BIT_VEHICLES, hasPermission, type PermissionBit } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section, type StatusColor } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { formatDate } from '@/lib/format';

const STATUS_COLOR = { red: 'danger', yellow: 'warning', green: 'success' } as const;
const SEVERITY_COLOR: Record<Notification['severity'], StatusColor> = {
  red: 'danger',
  yellow: 'warning',
  info: 'textSecondary',
};

// The backend lists inspection, defect and vehicle notices for every user. Only show those for
// areas the user can open; medical notices are already restricted to admins by the backend.
const NOTIFICATION_PERMISSION: Partial<Record<string, PermissionBit>> = {
  inspection: BIT_EQUIPMENT,
  defect: BIT_EQUIPMENT,
  vehicle: BIT_VEHICLES,
};

function isVisible(notification: Notification, user: User | null) {
  const bit = NOTIFICATION_PERMISSION[notification.type];
  return !bit || hasPermission(user, bit);
}

export default function StartScreen() {
  const { user } = useSession();
  const theme = useTheme();
  const stats = useQuery({ queryKey: ['dashboard', 'stats'], queryFn: dashboardApi.getStats });
  const notifications = useQuery({
    queryKey: ['dashboard', 'notifications'],
    queryFn: dashboardApi.getNotifications,
    select: (list) => list.filter((n) => isVisible(n, user)),
  });
  const { refreshing, onRefresh } = usePullToRefresh(() =>
    Promise.all([stats.refetch(), notifications.refetch()]),
  );

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />
        }>
        <ThemedText type="small" themeColor="textSecondary">
          Angemeldet als {user?.name ?? user?.username}
        </ThemedText>

        <QueryState isPending={stats.isPending} error={stats.error} />

        {notifications.data && notifications.data.length > 0 && (
          <Section title="Hinweise">
            {notifications.data.map((n) => (
              <ListRow key={n.id} title={n.title} subtitle={n.message} status={SEVERITY_COLOR[n.severity]} />
            ))}
          </Section>
        )}

        {stats.data && <Dashboard stats={stats.data} />}
      </ScrollView>
    </ThemedView>
  );
}

function Dashboard({ stats }: { stats: DashboardStats }) {
  return (
    <>
      <View style={styles.tiles}>
        <Tile label="Aktive Mitglieder" value={stats.activeMembers} />
        <Tile label="Fahrzeuge" value={stats.vehicles} />
        <Tile label={`Einsätze ${new Date().getFullYear()}`} value={stats.operationsThisYear} />
      </View>

      <Section title="Letzte Einsätze" empty="Keine Einsätze vorhanden">
        {stats.recentOperations.map((op) => (
          <ListRow
            key={op.id}
            title={op.keyword ? `${op.keyword} – ${op.location}` : op.location}
            subtitle={`${formatDate(op.date)} · ${op.leaderCount + op.memberCount} Einsatzkräfte`}
          />
        ))}
      </Section>

      <Section title="Anstehende Prüfungen" empty="Keine Prüfungen fällig">
        {stats.upcomingInspections.map((inspection, index) => (
          <ListRow
            key={`${inspection.type}-${inspection.entityName}-${index}`}
            title={inspection.entityName}
            subtitle={`${inspection.type} · fällig ${formatDate(inspection.dueDate)}`}
            status={STATUS_COLOR[inspection.status]}
          />
        ))}
      </Section>

      {/* Only filled for admins (health data, Art. 9 DSGVO) */}
      {stats.upcomingMedicalExams.length > 0 && (
        <Section title="Anstehende Untersuchungen">
          {stats.upcomingMedicalExams.map((exam, index) => (
            <ListRow
              key={`${exam.memberName}-${exam.examType}-${index}`}
              title={exam.memberName}
              subtitle={`${exam.examType} · fällig ${formatDate(exam.dueDate)}`}
            />
          ))}
        </Section>
      )}
    </>
  );
}

function Tile({ label, value }: { label: string; value: number }) {
  return (
    <ThemedView type="backgroundElement" style={styles.tile}>
      <ThemedText type="subtitle" themeColor="primary">
        {value}
      </ThemedText>
      <ThemedText type="small" themeColor="textSecondary" style={styles.center}>
        {label}
      </ThemedText>
    </ThemedView>
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
  tiles: { flexDirection: 'row', gap: Spacing.two },
  tile: {
    flex: 1,
    alignItems: 'center',
    paddingVertical: Spacing.three,
    paddingHorizontal: Spacing.one,
    borderRadius: Spacing.three,
  },
  center: { textAlign: 'center' },
});
