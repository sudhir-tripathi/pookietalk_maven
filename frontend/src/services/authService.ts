// src/services/authService.ts

const BASE_URL = "http://localhost:8080/api/auth";

const authService = {
  login: async (credentials: { username: string; password: string }) => {
    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Login failed: ${errorText}`);
    }

    const data = await response.json();

    if (!data.token) {
      throw new Error("Login response did not contain a token");
    }

    // Store token for future requests
    localStorage.setItem("token", data.token);

    return data;
  },

  register: async (data: { username: string; email: string; password: string }) => {
    const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Registration failed: ${errorText}`);
    }

    return await response.json();
  },

  getCurrentUser: async (providedToken?: string) => {
    const token = providedToken || localStorage.getItem("token");

    if (!token || token === "undefined" || token === "null") {
      throw new Error("Invalid or missing token");
    }

    const response = await fetch("http://localhost:8080/api/users", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to fetch user: ${errorText}`);
    }

    return await response.json();
  },

  logout: () => {
    localStorage.removeItem("token");
  },

  getToken: () => {
    return localStorage.getItem("token");
  },
};

export default authService;
