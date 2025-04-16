const API_URL = "http://localhost:8080/api/chat"; // Update with your backend URL

const chatService = {
  getMessages: async () => {
    const response = await fetch(`${API_URL}/messages`);
    return response.ok ? await response.json() : [];
  },

  sendMessage: async (sender: string, text: string) => {
    await fetch(`${API_URL}/send`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sender, text }),
    });
  },
};

export default chatService;
