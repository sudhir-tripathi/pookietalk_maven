import React from 'react';
import Message from './Message';

interface MessageData {
  id: number;
  sender: string;
  content: string;
  timestamp: string;
}

interface ChatBoxProps {
  messages: MessageData[];
}

const ChatBox: React.FC<ChatBoxProps> = ({ messages }) => {
  return (
    <div className="flex-1 overflow-y-auto px-4 py-2">
      {messages.map((msg) => (
        <Message key={msg.id} message={msg} />
      ))}
    </div>
  );
};

export default ChatBox;
