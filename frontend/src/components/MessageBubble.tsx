import React from 'react';

interface MessageBubbleProps {
  message: {
    id: number;
    sender: string;
    content: string;
    timestamp: string;
  };
  currentUserEmail: string;
}

const MessageBubble: React.FC<MessageBubbleProps> = ({ message, currentUserEmail }) => {
  const isOwnMessage = message.sender === currentUserEmail;

  return (
    <div className={`my-2 flex ${isOwnMessage ? 'justify-end' : 'justify-start'}`}>
      <div
        className={`p-3 max-w-xs md:max-w-md rounded-lg shadow-md text-sm ${
          isOwnMessage ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-800'
        }`}
      >
        <p>{message.content}</p>
        <span className="block text-xs mt-1 opacity-70 text-right">
          {new Date(message.timestamp).toLocaleTimeString()}
        </span>
      </div>
    </div>
  );
};

export default MessageBubble;
