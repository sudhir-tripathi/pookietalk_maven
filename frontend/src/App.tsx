import React from "react";
import AppRoutes from "./routes/AppRoutes";
import { AuthProvider } from "./context/AuthContext";
import Layout from "./layout/Layout"; // ⬅️ Import the layout wrapper

const App = () => (
  <AuthProvider>
    <Layout>
      <AppRoutes />
    </Layout>
  </AuthProvider>
);

export default App;
