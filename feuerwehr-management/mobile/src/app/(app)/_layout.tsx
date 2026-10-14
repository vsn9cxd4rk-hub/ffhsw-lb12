import { Stack } from 'expo-router';

import { BIT_EQUIPMENT, BIT_OPERATIONS, BIT_VEHICLES, hasPermission } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { useTheme } from '@/hooks/use-theme';

// A deep link (fenix://scan/<nr>) opens a detail screen directly: keep the tabs below it,
// so "back" and the tab bar still work.
export const unstable_settings = { anchor: '(tabs)' };

// Tabs at the bottom; screens opened from a tab (details, forms) are pushed on this stack
// above the tab bar.
export default function AppLayout() {
  const theme = useTheme();
  const { user } = useSession();

  return (
    <Stack
      screenOptions={{
        headerStyle: { backgroundColor: theme.primary },
        headerTintColor: '#ffffff',
        headerBackButtonDisplayMode: 'minimal',
      }}>
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
      <Stack.Screen name="password" options={{ title: 'Passwort ändern' }} />

      {/* Gerätewart area; also reached through the deep link fenix://scan/<nr> */}
      <Stack.Protected guard={hasPermission(user, BIT_EQUIPMENT)}>
        <Stack.Screen name="scan/[code]" options={{ title: 'Etikett' }} />
        <Stack.Screen name="article/[id]" options={{ title: 'Artikel' }} />
        <Stack.Screen name="inspect/[articleId]" options={{ title: 'Prüfung' }} />
        <Stack.Screen name="defect/new" options={{ title: 'Mangel melden' }} />
        <Stack.Screen name="defect/[id]" options={{ title: 'Mangel' }} />
      </Stack.Protected>

      {/* Readable by everyone; changing the attendance needs BIT_EVENTS (checked on the screen) */}
      <Stack.Screen name="event/[id]/index" options={{ title: 'Veranstaltung' }} />
      <Stack.Screen name="event/[id]/attendance" options={{ title: 'Anwesenheit' }} />

      <Stack.Protected guard={hasPermission(user, BIT_VEHICLES)}>
        <Stack.Screen name="vehicles" options={{ title: 'Fahrzeuge' }} />
        <Stack.Screen name="vehicle/[id]/index" options={{ title: 'Fahrzeug' }} />
        <Stack.Screen name="vehicle/[id]/logbook" options={{ title: 'Fahrtenbuch-Eintrag' }} />
      </Stack.Protected>

      <Stack.Protected guard={hasPermission(user, BIT_OPERATIONS)}>
        <Stack.Screen name="operations" options={{ title: 'Einsätze' }} />
        <Stack.Screen name="operation/new" options={{ title: 'Neuer Einsatz' }} />
        <Stack.Screen name="operation/[id]/index" options={{ title: 'Einsatz' }} />
        <Stack.Screen name="operation/[id]/edit" options={{ title: 'Einsatz bearbeiten' }} />
        <Stack.Screen name="operation/[id]/time" options={{ title: 'Fahrzeug' }} />
        <Stack.Screen name="operation/[id]/personnel" options={{ title: 'Einsatzkraft hinzufügen' }} />
      </Stack.Protected>
    </Stack>
  );
}
