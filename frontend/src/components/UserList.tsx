import React from 'react';

interface User {
  id: number;
  username: string;
  email: string;
}

interface UserListProps {
  users: User[];
  onSelectUser: (user: User) => void;
}

const UserList: React.FC<UserListProps> = ({ users, onSelectUser }) => {
  return (
    <div className="w-1/4 bg-gray-100 p-4 overflow-y-auto border-r">
      <h2 className="text-lg font-semibold mb-4">Users</h2>
      {users.map(user => (
        <div
          key={user.id}
          className="p-2 cursor-pointer hover:bg-gray-200 rounded"
          onClick={() => onSelectUser(user)}
        >
          {user.username}
        </div>
      ))}
    </div>
  );
};

export default UserList;
