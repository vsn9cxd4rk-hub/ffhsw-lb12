import { QueryClientProvider } from '@tanstack/react-query';
import { DarkTheme, DefaultTheme, Stack, ThemeProvider } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { useEffect } from 'react';
import { useColorScheme } from 'react-native';

import { SessionProvider, useSession } from '@/auth/session';
import { queryClient } from '@/lib/query-client';

SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  const colorScheme = useColorScheme();
  return (
    <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
      <QueryClientProvider client={queryClient}>
        <SessionProvider>
          <RootNavigator />
        </SessionProvider>
      </QueryClientProvider>
    </ThemeProvider>
  );
}

function RootNavigator() {
  const { isLoading, user } = useSession();

  // Keep the splash screen until the stored session has been checked, so the sign-in form
  // does not flash up for users who are still signed in.
  useEffect(() => {
    if (!isLoading) SplashScreen.hideAsync();
  }, [isLoading]);

  if (isLoading) return null;

  // The backend rejects everything except the password change until an initial or
  // admin-reset password has been replaced (code PASSWORD_CHANGE_REQUIRED).
  const mustChangePassword = !!user?.mustChangePassword;

  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Protected guard={!!user && !mustChangePassword}>
        <Stack.Screen name="(app)" />
      </Stack.Protected>
      <Stack.Protected guard={mustChangePassword}>
        <Stack.Screen name="change-password" />
      </Stack.Protected>
      <Stack.Protected guard={!user}>
        <Stack.Screen name="sign-in" />
      </Stack.Protected>
    </Stack>
  );
}
