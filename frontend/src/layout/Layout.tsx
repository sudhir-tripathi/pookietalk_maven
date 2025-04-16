import React, { ReactNode } from 'react';
import { Link, Outlet } from 'react-router-dom';

interface LayoutProps {
  children?: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div className="app-container">
      <header className="header">
        <div className="header-content">
          <Link to="/" className="logo">PookieTalk</Link>
          <nav className="nav">
            <Link to="/chat">Chat</Link>
            <Link to="/profile">Profile</Link>
            <Link to="/login">Logout</Link>
          </nav>
        </div>
      </header>

      <main className="main-content">
        {children || <Outlet />}
      </main>

      <footer className="footer">
        &copy; {new Date().getFullYear()} PookieTalk. All rights reserved.
      </footer>
    </div>
  );
};

export default Layout;
