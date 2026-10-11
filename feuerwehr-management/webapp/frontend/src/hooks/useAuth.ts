import { useAuthStore } from '../store/auth.store';
import { authApi } from '../api/auth';
import { announceLogin, logoutEverywhere } from '../api/client';
import { useNavigate } from 'react-router-dom';

export function useAuth() {
  const { user, accessToken, isAuthenticated, isLoading, setAuth, setLoading } = useAuthStore();
  const navigate = useNavigate();

  const login = async (username: string, password: string, redirectTo = '/dashboard') => {
    setLoading(true);
    try {
      const response = await authApi.login(username, password);
      const { accessToken: token, user: userData } = response.data.data;
      setAuth(userData, token);
      announceLogin();
      navigate(redirectTo);
    } catch (error) {
      setLoading(false);
      throw error;
    }
  };

  const logout = async () => {
    await logoutEverywhere();
    navigate('/login');
  };

  return {
    user,
    accessToken,
    isAuthenticated,
    isLoading,
    login,
    logout,
  };
}
