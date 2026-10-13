import { Image } from 'expo-image';
import { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  TextInput,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

export default function SignInScreen() {
  const { signIn, serverUrl: storedServerUrl } = useSession();
  const theme = useTheme();

  const [serverUrl, setServerUrl] = useState(storedServerUrl);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  useEffect(() => setServerUrl(storedServerUrl), [storedServerUrl]);

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
  const inputStyle = [
    styles.input,
    { color: theme.text, backgroundColor: theme.backgroundElement, borderColor: theme.backgroundSelected },
  ];

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

            <ThemedText type="smallBold">Server</ThemedText>
            <TextInput
              style={inputStyle}
              value={serverUrl}
              onChangeText={setServerUrl}
              placeholder="z. B. 192.168.1.20:3001"
              placeholderTextColor={theme.textSecondary}
              autoCapitalize="none"
              autoCorrect={false}
              keyboardType="url"
            />

            <ThemedText type="smallBold">Benutzername</ThemedText>
            <TextInput
              style={inputStyle}
              value={username}
              onChangeText={setUsername}
              placeholder="Benutzername eingeben"
              placeholderTextColor={theme.textSecondary}
              autoCapitalize="none"
              autoCorrect={false}
              autoComplete="username"
              textContentType="username"
            />

            <ThemedText type="smallBold">Passwort</ThemedText>
            <TextInput
              style={inputStyle}
              value={password}
              onChangeText={setPassword}
              placeholder="Passwort eingeben"
              placeholderTextColor={theme.textSecondary}
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

            <Pressable
              onPress={handleSubmit}
              disabled={!canSubmit}
              style={({ pressed }) => [
                styles.button,
                { backgroundColor: pressed ? theme.primaryPressed : theme.primary },
                !canSubmit && styles.disabled,
              ]}>
              {busy ? (
                <ActivityIndicator color="#ffffff" />
              ) : (
                <ThemedText type="smallBold" style={styles.buttonText}>
                  Anmelden
                </ThemedText>
              )}
            </Pressable>
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
  input: {
    borderWidth: 1,
    borderRadius: Spacing.two,
    paddingHorizontal: Spacing.three,
    paddingVertical: Spacing.two + Spacing.one,
    fontSize: 16,
    marginBottom: Spacing.two,
  },
  button: {
    borderRadius: Spacing.two,
    paddingVertical: Spacing.three,
    alignItems: 'center',
    marginTop: Spacing.two,
  },
  buttonText: { color: '#ffffff' },
  disabled: { opacity: 0.5 },
});
