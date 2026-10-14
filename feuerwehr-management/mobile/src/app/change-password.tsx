import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { useSession } from '@/auth/session';
import { ChangePasswordForm } from '@/components/change-password-form';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { MaxContentWidth, Spacing } from '@/constants/theme';

/** Shown instead of the app while user.mustChangePassword is set (see the root layout). */
export default function ForcedChangePasswordScreen() {
  const { signOut } = useSession();

  return (
    <ThemedView style={styles.container}>
      <SafeAreaView style={styles.flex}>
        <KeyboardAvoidingView
          style={styles.flex}
          behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
          <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
            <ThemedText type="subtitle">Neues Passwort</ThemedText>
            <ThemedView type="backgroundElement" style={styles.notice}>
              <ThemedText type="small" themeColor="warning">
                Ihr Passwort wurde vom Administrator vergeben oder ist ein Startpasswort. Bitte legen
                Sie jetzt ein eigenes Passwort fest.
              </ThemedText>
            </ThemedView>
            <ChangePasswordForm />
            <Button title="Abmelden" variant="secondary" onPress={signOut} />
          </ScrollView>
        </KeyboardAvoidingView>
      </SafeAreaView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  flex: { flex: 1 },
  content: {
    flexGrow: 1,
    justifyContent: 'center',
    gap: Spacing.three,
    padding: Spacing.four,
    width: '100%',
    maxWidth: MaxContentWidth / 2,
    alignSelf: 'center',
  },
  notice: { borderRadius: Spacing.two, padding: Spacing.three },
});
