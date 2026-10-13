import { Stack } from 'expo-router';
import {
  ActivityIndicator,
  Pressable,
  RefreshControl,
  ScrollView,
  StyleSheet,
  View,
} from 'react-native';

import { dashboardApi } from '@/api/client';
import type { DashboardStats } from '@/api/types';
import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useQuery } from '@/hooks/use-query';
import { useTheme } from '@/hooks/use-theme';
import { formatDate } from '@/lib/format';

const STATUS_COLOR = { red: 'danger', yellow: 'warning', green: 'success' } as const;

export default function DashboardScreen() {
  const { user, signOut } = useSession();
  const { data, error, isLoading, isRefreshing, reload } = useQuery(dashboardApi.getStats);
  const theme = useTheme();

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen
        options={{
          headerRight: () => (
            <Pressable onPress={signOut} hitSlop={Spacing.three}>
              <ThemedText type="smallBold" style={styles.headerAction}>
                Abmelden
              </ThemedText>
            </Pressable>
          ),
        }}
      />
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={
          <RefreshControl refreshing={isRefreshing} onRefresh={reload} tintColor={theme.primary} />
        }>
        <ThemedText type="small" themeColor="textSecondary">
          Angemeldet als {user?.name ?? user?.username}
        </ThemedText>

        {isLoading && <ActivityIndicator color={theme.primary} style={styles.loading} />}
        {error && (
          <ThemedText type="small" themeColor="danger">
            Daten konnten nicht geladen werden: {error.message}
          </ThemedText>
        )}
        {data && <Dashboard stats={data} />}
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
          <Row
            key={op.id}
            title={op.keyword ? `${op.keyword} – ${op.location}` : op.location}
            subtitle={`${formatDate(op.date)} · ${op.leaderCount + op.memberCount} Einsatzkräfte`}
          />
        ))}
      </Section>

      <Section title="Anstehende Prüfungen" empty="Keine Prüfungen fällig">
        {stats.upcomingInspections.map((inspection, index) => (
          <Row
            key={`${inspection.type}-${inspection.entityName}-${index}`}
            title={inspection.entityName}
            subtitle={`${inspection.type} · fällig ${formatDate(inspection.dueDate)}`}
            status={STATUS_COLOR[inspection.status]}
          />
        ))}
      </Section>

      {/* Only filled for admins (health data, Art. 9 DSGVO) */}
      {stats.upcomingMedicalExams.length > 0 && (
        <Section title="Anstehende Untersuchungen" empty="">
          {stats.upcomingMedicalExams.map((exam, index) => (
            <Row
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

function Section({ title, empty, children }: { title: string; empty: string; children: React.ReactNode[] }) {
  return (
    <ThemedView style={styles.section}>
      <ThemedText type="smallBold">{title}</ThemedText>
      <ThemedView type="backgroundElement" style={styles.card}>
        {children.length > 0 ? (
          children
        ) : (
          <ThemedText type="small" themeColor="textSecondary">
            {empty}
          </ThemedText>
        )}
      </ThemedView>
    </ThemedView>
  );
}

function Row({
  title,
  subtitle,
  status,
}: {
  title: string;
  subtitle: string;
  status?: 'danger' | 'warning' | 'success';
}) {
  const theme = useTheme();
  return (
    <View style={styles.row}>
      {status && <View style={[styles.dot, { backgroundColor: theme[status] }]} />}
      <View style={styles.rowText}>
        <ThemedText type="small">{title}</ThemedText>
        <ThemedText type="small" themeColor="textSecondary">
          {subtitle}
        </ThemedText>
      </View>
    </View>
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
  headerAction: { color: '#ffffff', paddingHorizontal: Spacing.two },
  loading: { marginTop: Spacing.five },
  tiles: { flexDirection: 'row', gap: Spacing.two },
  tile: {
    flex: 1,
    alignItems: 'center',
    paddingVertical: Spacing.three,
    paddingHorizontal: Spacing.one,
    borderRadius: Spacing.three,
  },
  center: { textAlign: 'center' },
  section: { gap: Spacing.two },
  card: { borderRadius: Spacing.three, padding: Spacing.three, gap: Spacing.three },
  row: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  rowText: { flex: 1 },
  dot: { width: 10, height: 10, borderRadius: 5 },
});
