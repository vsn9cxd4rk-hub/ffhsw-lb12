import { useAuthStore } from '../store/auth.store';
import { authApi } from '../api/auth';
import { useNavigate } from 'react-router-dom';

export function useAuth() {
  const { user, accessToken, isAuthenticated, isLoading, setAuth, clearAuth, setLoading } = useAuthStore();
  const navigate = useNavigate();

  const login = async (username: string, password: string) => {
    setLoading(true);
    try {
      const response = await authApi.login(username, password);
      const { accessToken: token, user: userData } = response.data.data;
      setAuth(userData, token);
      navigate('/dashboard');
    } catch (error) {
      setLoading(false);
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authApi.logout();
    } catch {
      // ignore errors
    } finally {
      clearAuth();
      navigate('/login');
    }
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
