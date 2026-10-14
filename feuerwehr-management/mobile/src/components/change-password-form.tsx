import { useState } from 'react';
import { View } from 'react-native';

import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { Button } from '@/components/ui/button';
import { TextField } from '@/components/ui/text-field';

/** Same rules as webapp/backend/src/utils/password.ts, checked here for an immediate message. */
function validate(oldPassword: string, newPassword: string, confirmPassword: string): string | null {
  if (newPassword !== confirmPassword) return 'Die neuen Passwörter stimmen nicht überein.';
  if (newPassword.length < 8) return 'Das neue Passwort muss mindestens 8 Zeichen lang sein.';
  if (!/[a-z]/.test(newPassword) || !/[A-Z]/.test(newPassword) || !/\d/.test(newPassword)) {
    return 'Das Passwort muss Groß- und Kleinbuchstaben sowie eine Zahl enthalten.';
  }
  if (newPassword === oldPassword) return 'Das neue Passwort muss sich vom bisherigen unterscheiden.';
  return null;
}

export function ChangePasswordForm({ onDone }: { onDone?: () => void }) {
  const { changePassword } = useSession();
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  async function handleSubmit() {
    const invalid = validate(oldPassword, newPassword, confirmPassword);
    setError(invalid);
    if (invalid) return;

    setBusy(true);
    try {
      await changePassword(oldPassword, newPassword);
      onDone?.();
    } catch (e) {
      setError(e instanceof TypeError ? 'Server nicht erreichbar' : (e as Error).message);
    } finally {
      setBusy(false);
    }
  }

  const canSubmit = oldPassword !== '' && newPassword !== '' && confirmPassword !== '';

  return (
    <View>
      <TextField
        label="Aktuelles Passwort"
        value={oldPassword}
        onChangeText={setOldPassword}
        secureTextEntry
        autoComplete="current-password"
        textContentType="password"
      />
      <TextField
        label="Neues Passwort"
        value={newPassword}
        onChangeText={setNewPassword}
        secureTextEntry
        autoComplete="new-password"
        textContentType="newPassword"
      />
      <TextField
        label="Neues Passwort bestätigen"
        value={confirmPassword}
        onChangeText={setConfirmPassword}
        secureTextEntry
        autoComplete="new-password"
        textContentType="newPassword"
        onSubmitEditing={() => canSubmit && handleSubmit()}
      />
      <ThemedText type="small" themeColor="textSecondary">
        Mindestens 8 Zeichen mit Groß- und Kleinbuchstaben sowie einer Zahl.
      </ThemedText>
      {error && (
        <ThemedText type="small" themeColor="danger">
          {error}
        </ThemedText>
      )}
      <Button title="Passwort ändern" onPress={handleSubmit} disabled={!canSubmit} busy={busy} />
    </View>
  );
}
