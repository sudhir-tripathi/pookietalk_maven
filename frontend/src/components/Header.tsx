import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '@hook/useAuth';

const Header: React.FC = () => {
  const { user, logout } = useAuth();

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="logo">PookieTalk</Link>
        <nav className="nav">
          {user ? (
            <>
              <Link to="/chat">Chat</Link>
              <Link to="/profile">Profile</Link>
              <button onClick={logout}>Logout</button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
