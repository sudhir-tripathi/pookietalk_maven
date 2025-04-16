import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import Login from '../pages/Login';
import Register from '../pages/Register';
import ChatRoom from '../pages/ChatRoom';
import Profile from '../pages/Profile';
import PrivateRoute from './PrivateRoute';

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      {/* Redirect root to login or dashboard */}
      <Route path="/" element={<Navigate to="/login" />} />

      {/* Public routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Private routes */}
      <Route
        path="/chat"
        element={
          <PrivateRoute>
            <ChatRoom />
          </PrivateRoute>
        }
      />
      <Route
        path="/profile"
        element={
          <PrivateRoute>
            <Profile />
          </PrivateRoute>
        }
      />

      {/* Catch-all fallback */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
};

export default AppRoutes;
