import React, { useState, useEffect, useRef } from "react";
import { useAuth } from '../hooks/useAuth';
import chatService from "../services/chatService";
import socketService from "../services/socketService";
import MessageBubble from "../components/MessageBubble";

interface ChatMessage {
  id: number;
  sender: string;
  content: string;
  timestamp: string;
}

const ChatRoom: React.FC = () => {
  const { user, logout } = useAuth();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [message, setMessage] = useState("");
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!user) return;

    // Load previous messages
    chatService.getMessages().then(setMessages);

    // Listen for new messages
    socketService.onMessage((newMessage: ChatMessage) => {
      setMessages((prev) => [...prev, newMessage]);
    });

    return () => {
      socketService.disconnect();
    };
  }, [user]);

  const sendMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim()) return;

    try {
      await chatService.sendMessage(user?.email || "", message);
      setMessage("");
    } catch (error) {
      console.error("Failed to send message", error);
    }
  };

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      {/* Header */}
      <div className="bg-blue-600 text-white p-4 flex justify-between items-center shadow">
        <h2 className="text-xl font-bold">PookieTalk</h2>
        <button onClick={logout} className="bg-red-500 px-3 py-1 rounded hover:bg-red-600">
          Logout
        </button>
      </div>

      {/* Chat Messages */}
      <div className="flex-1 overflow-y-auto p-4">
        {messages.map((msg) => (
          <MessageBubble key={msg.id} message={msg} currentUserEmail={user?.email || ""} />
        ))}
        <div ref={messagesEndRef} />
      </div>

      {/* Message Input */}
      <form onSubmit={sendMessage} className="flex p-4 bg-white border-t">
        <input
          type="text"
          className="flex-1 p-2 border rounded focus:outline-none focus:ring focus:border-blue-300"
          placeholder="Type a message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button
          type="submit"
          className="ml-2 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded"
        >
          Send
        </button>
      </form>
    </div>
  );
};

export default ChatRoom;
