import React, { useState } from 'react';
import { authApi } from '../../api/auth';
import { Card } from '../../components/ui/Card';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { useAuth } from '../../hooks/useAuth';

export function ChangePasswordPage() {
  const { logout } = useAuth();
  const [form, setForm] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  const handleSubmit = async () => {
    setError('');
    if (form.newPassword !== form.confirmPassword) {
      setError('Die neuen Passwörter stimmen nicht überein.');
      return;
    }
    if (form.newPassword.length < 8) {
      setError('Das neue Passwort muss mindestens 8 Zeichen lang sein.');
      return;
    }
    setLoading(true);
    try {
      await authApi.changePassword(form.oldPassword, form.newPassword);
      setSuccess(true);
      setTimeout(() => logout(), 2000);
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Passwort konnte nicht geändert werden';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <Card title="Passwort ändern">
        {success ? (
          <div className="bg-green-50 border border-green-200 text-green-700 text-sm px-4 py-3 rounded-md">
            Passwort erfolgreich geändert. Sie werden abgemeldet...
          </div>
        ) : (
          <div className="space-y-4">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
            )}
            <Input label="Aktuelles Passwort" value={form.oldPassword} onChange={(e) => u('oldPassword', e.target.value)} type="password" required />
            <Input label="Neues Passwort" value={form.newPassword} onChange={(e) => u('newPassword', e.target.value)} type="password" required />
            <Input label="Neues Passwort bestätigen" value={form.confirmPassword} onChange={(e) => u('confirmPassword', e.target.value)} type="password" required />
            <div className="flex justify-end">
              <Button variant="primary" onClick={handleSubmit} loading={loading} disabled={!form.oldPassword || !form.newPassword || !form.confirmPassword}>
                Passwort ändern
              </Button>
            </div>
          </div>
        )}
      </Card>
    </div>
  );
}
