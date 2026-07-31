import {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

import { authApi } from "../api/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {
    const token = localStorage.getItem(
      "chefCourierToken"
    );

    if (!token) {
      setLoading(false);
      return;
    }

    authApi
      .me()
      .then((response) => {
        setUser(response.data);
      })
      .catch(() => {
        localStorage.removeItem(
          "chefCourierToken"
        );
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  async function login(credentials) {
    const response =
      await authApi.login(credentials);

    localStorage.setItem(
      "chefCourierToken",
      response.data.accessToken
    );

    setUser(response.data.user);

    return response.data.user;
  }

  async function register(data) {
    const response =
      await authApi.register(data);

    localStorage.setItem(
      "chefCourierToken",
      response.data.accessToken
    );

    setUser(response.data.user);

    return response.data.user;
  }

  function logout() {
    localStorage.removeItem(
      "chefCourierToken"
    );

    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
