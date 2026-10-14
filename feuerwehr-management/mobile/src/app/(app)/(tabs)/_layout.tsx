import { Tabs } from 'expo-router/js-tabs';
import { SymbolView, type SymbolViewProps } from 'expo-symbols';
import type { ColorValue } from 'react-native';

import { BIT_EQUIPMENT, hasPermission } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { useTheme } from '@/hooks/use-theme';

// Tabs that need a permission go into <Tabs.Protected guard={hasPermission(user, BIT_…)}>,
// so the tab button disappears for users without that right. The backend checks again.
export default function TabLayout() {
  const theme = useTheme();
  const { user } = useSession();

  return (
    <Tabs
      screenOptions={{
        headerStyle: { backgroundColor: theme.primary },
        headerTintColor: '#ffffff',
        tabBarActiveTintColor: theme.primary,
      }}>
      <Tabs.Screen
        name="index"
        options={{
          title: 'Start',
          headerTitle: 'FENIX',
          tabBarIcon: ({ color, size }) => (
            <TabIcon name={{ ios: 'house', android: 'home', web: 'home' }} color={color} size={size} />
          ),
        }}
      />
      <Tabs.Screen
        name="events"
        options={{
          title: 'Termine',
          headerTitle: 'Veranstaltungen',
          tabBarIcon: ({ color, size }) => (
            <TabIcon name={{ ios: 'calendar', android: 'event', web: 'event' }} color={color} size={size} />
          ),
        }}
      />
      <Tabs.Protected guard={hasPermission(user, BIT_EQUIPMENT)}>
        <Tabs.Screen
          name="scan"
          options={{
            title: 'Scan',
            tabBarIcon: ({ color, size }) => (
              <TabIcon
                name={{ ios: 'qrcode.viewfinder', android: 'qr_code_scanner', web: 'qr_code_scanner' }}
                color={color}
                size={size}
              />
            ),
          }}
        />
        <Tabs.Screen
          name="equipment"
          options={{
            title: 'Geräte',
            headerTitle: 'Gerätewart',
            tabBarIcon: ({ color, size }) => (
              <TabIcon name={{ ios: 'checklist', android: 'checklist', web: 'checklist' }} color={color} size={size} />
            ),
          }}
        />
      </Tabs.Protected>
      <Tabs.Screen
        name="more"
        options={{
          title: 'Mehr',
          tabBarIcon: ({ color, size }) => (
            <TabIcon
              name={{ ios: 'ellipsis.circle', android: 'more_horiz', web: 'more_horiz' }}
              color={color}
              size={size}
            />
          ),
        }}
      />
    </Tabs>
  );
}

function TabIcon({ name, color, size }: { name: SymbolViewProps['name']; color: ColorValue; size: number }) {
  return <SymbolView name={name} tintColor={color} size={size} />;
}
