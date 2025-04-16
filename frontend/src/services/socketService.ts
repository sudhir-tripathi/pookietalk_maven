import { io, Socket } from "socket.io-client";

const SOCKET_URL = "http://localhost:8080"; // Update with your backend URL
let socket: Socket;

const socketService = {
  connect: () => {
    socket = io(SOCKET_URL);
  },

  disconnect: () => {
    socket.disconnect();
  },

  onMessage: (callback: (message: { sender: string; text: string }) => void) => {
    socket.on("message", callback);
  },
};

export default socketService;
