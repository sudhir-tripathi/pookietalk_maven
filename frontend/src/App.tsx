import React from "react";
import AppRoutes from "./routes/AppRoutes";
import { AuthProvider } from "./context/AuthContext";
import { ToastProvider } from "./components/ToastProvider";
import Layout from "./layout/Layout";

const App = () => (
  <AuthProvider>
    <Layout>
      <ToastProvider />
      <AppRoutes />
    </Layout>
  </AuthProvider>
);

export default App;