import React from "react";

interface NavbarProps {
  title: string;
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ title, onLogout }) => {
  return (
    <header className="bg-blue-600 text-white p-4 flex justify-between items-center shadow">
      <h1 className="text-2xl font-bold">{title}</h1>
      <button
        onClick={onLogout}
        className="bg-red-500 px-4 py-2 rounded hover:bg-red-600"
      >
        Logout
      </button>
    </header>
  );
};

export default Navbar;
