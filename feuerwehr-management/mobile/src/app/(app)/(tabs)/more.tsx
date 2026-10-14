import Constants from 'expo-constants';
import { router } from 'expo-router';
import { ScrollView, StyleSheet } from 'react-native';

import {
  BIT_OPERATIONS,
  BIT_VEHICLES,
  hasPermission,
  PERMISSION_LABELS,
  type PermissionBit,
} from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { ThemedView } from '@/components/themed-view';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';

export default function MoreScreen() {
  const { user, serverUrl, signOut } = useSession();
  const permissions = (Object.keys(PERMISSION_LABELS) as PermissionBit[]).filter((bit) =>
    hasPermission(user, bit),
  );

  return (
    <ThemedView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        {(hasPermission(user, BIT_VEHICLES) || hasPermission(user, BIT_OPERATIONS)) && (
          <Section title="Bereiche">
            {hasPermission(user, BIT_OPERATIONS) && (
              <ListRow title="Einsätze" subtitle="Erfassen, Fahrzeuge, Kräftenachweis" onPress={() => router.push('/operations')} />
            )}
            {hasPermission(user, BIT_VEHICLES) && (
              <ListRow title="Fahrzeuge" subtitle="Prüftermine, Fahrtenbuch" onPress={() => router.push('/vehicles')} />
            )}
          </Section>
        )}

        <Section title="Konto">
          <ListRow
            title={user?.name ?? user?.username ?? ''}
            subtitle={user?.isAdmin ? 'Administrator' : `Benutzer ${user?.username}`}
          />
          <ListRow title="Passwort ändern" onPress={() => router.push('/password')} />
          <ListRow title="Abmelden" titleColor="danger" onPress={signOut} />
        </Section>

        <Section title="Berechtigungen" empty="Nur Lesezugriff auf Veranstaltungen und Ausbildung">
          {permissions.map((bit) => (
            <ListRow key={bit} title={PERMISSION_LABELS[bit]} detail="✓" />
          ))}
        </Section>

        <Section title="App">
          <ListRow title="Server" detail={serverUrl} />
          <ListRow title="Version" detail={Constants.expoConfig?.version ?? '–'} />
        </Section>
      </ScrollView>
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
});
