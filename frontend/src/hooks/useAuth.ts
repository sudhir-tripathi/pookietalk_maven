// src/hooks/useAuth.ts

import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import authService from "../services/authService";

export const useAuth = () => {
  const { user, token, login, logout } = useContext(AuthContext);

  const loginUser = async (username: string, password: string) => {
    const data = await authService.login({ username, password });
    login(data.token); // Set token in context + localStorage
    return data;
  };

  return { user, token, loginUser, logout };
};
