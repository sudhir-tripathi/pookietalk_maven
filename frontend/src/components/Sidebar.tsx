import React from "react";

const Sidebar: React.FC = () => {
  return (
    <aside className="w-64 bg-white border-r h-full p-4 hidden md:block">
      <h2 className="text-xl font-semibold mb-4">Contacts</h2>
      <ul className="space-y-2">
        {/* Placeholder for future user list */}
        <li className="text-gray-700 p-2 rounded hover:bg-gray-100 cursor-pointer">
          @john_doe
        </li>
        <li className="text-gray-700 p-2 rounded hover:bg-gray-100 cursor-pointer">
          @jane_smith
        </li>
      </ul>
    </aside>
  );
};

export default Sidebar;
