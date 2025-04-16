import React from "react";

const Profile: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-2xl">
        <div className="flex items-center gap-6 mb-6">
          <div className="w-24 h-24 rounded-full bg-gray-200 overflow-hidden">
            {/* Placeholder profile image */}
            <img
              src="https://via.placeholder.com/150"
              alt="Profile"
              className="w-full h-full object-cover"
            />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-gray-800">John Doe</h2>
            <p className="text-gray-500">@johndoe</p>
          </div>
        </div>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="email"
              value="john@example.com"
              readOnly
              className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 bg-gray-100 text-gray-700"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Status
            </label>
            <input
              type="text"
              value="Hey there! I'm using PookieTalk."
              readOnly
              className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 bg-gray-100 text-gray-700"
            />
          </div>
        </div>

        <div className="mt-6 text-right">
          <button className="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition-all">
            Edit Profile
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
