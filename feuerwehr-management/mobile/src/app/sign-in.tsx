import { Image } from 'expo-image';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';

export default function SignInScreen() {
  const { signIn, serverUrl: storedServerUrl } = useSession();

  // The root layout renders this screen only after the session check, so the stored server is known
  const [serverUrl, setServerUrl] = useState(storedServerUrl);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  async function handleSubmit() {
    setError(null);
    setBusy(true);
    try {
      await signIn(serverUrl, username.trim(), password);
    } catch (e) {
      // fetch throws a TypeError when the server cannot be reached at all
      setError(e instanceof TypeError ? 'Server nicht erreichbar' : (e as Error).message);
      setPassword('');
    } finally {
      setBusy(false);
    }
  }

  const canSubmit = !busy && serverUrl.trim() !== '' && username.trim() !== '' && password !== '';

  return (
    <ThemedView style={styles.container}>
      <SafeAreaView style={styles.flex}>
        <KeyboardAvoidingView
          style={styles.flex}
          behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
          <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
            <ThemedView style={styles.header}>
              <Image source={require('@/assets/images/fenix-icon.png')} style={styles.logo} />
              <ThemedText type="subtitle" style={styles.brand}>
                FENIX
              </ThemedText>
              <ThemedText type="small" themeColor="primary" style={styles.center}>
                Feuerwehr-Einsatz-Netzwerk-Informations-eXchange
              </ThemedText>
            </ThemedView>

            <TextField
              label="Server"
              value={serverUrl}
              onChangeText={setServerUrl}
              placeholder="z. B. 192.168.1.20:3001"
              autoCapitalize="none"
              autoCorrect={false}
              keyboardType="url"
            />
            <TextField
              label="Benutzername"
              value={username}
              onChangeText={setUsername}
              placeholder="Benutzername eingeben"
              autoCapitalize="none"
              autoCorrect={false}
              autoComplete="username"
              textContentType="username"
            />
            <TextField
              label="Passwort"
              value={password}
              onChangeText={setPassword}
              placeholder="Passwort eingeben"
              secureTextEntry
              autoComplete="current-password"
              textContentType="password"
              onSubmitEditing={() => canSubmit && handleSubmit()}
            />

            {error && (
              <ThemedText type="small" themeColor="danger">
                {error}
              </ThemedText>
            )}

            <Button title="Anmelden" onPress={handleSubmit} disabled={!canSubmit} busy={busy} />
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
    gap: Spacing.two,
    padding: Spacing.four,
    width: '100%',
    maxWidth: MaxContentWidth / 2,
    alignSelf: 'center',
  },
  header: { alignItems: 'center', gap: Spacing.one, marginBottom: Spacing.four },
  logo: { width: 96, height: 96, borderRadius: 48, marginBottom: Spacing.two },
  brand: { letterSpacing: 6, fontWeight: 900 },
  center: { textAlign: 'center' },
});
