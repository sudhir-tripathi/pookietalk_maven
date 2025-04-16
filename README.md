/PookieTalk/
├── /backend/
│   ├── /src/
│   │   ├── /main/
│   │   │   ├── /java/
│   │   │   │   └── /com/pookietalk/
│   │   │   │       ├── /config/          # Application-wide configurations
│   │   │   │       │   ├── SecurityConfig.java
│   │   │   │       │   ├── JwtAuthFilter.java
│   │   │   │       │   ├── WebSocketConfig.java
│   │   │   │       │   ├── CorsConfig.java
│   │   │   │       ├── /controller/     # API Endpoints (Controllers)
│   │   │   │       │   ├── AuthController.java
│   │   │   │       │   ├── UserController.java
│   │   │   │       │   ├── ChatController.java
│   │   │   │       │   ├── MessageController.java
│   │   │   │       ├── /dto/            # Data Transfer Objects
│   │   │   │       │   ├── AuthRequestDTO.java
│   │   │   │       │   ├── AuthResponseDTO.java
│   │   │   │       │   ├── UserDTO.java
│   │   │   │       │   ├── ChatDTO.java
│   │   │   │       │   ├── MessageDTO.java
│   │   │   │       ├── /model/          # Database Entities (Models)
│   │   │   │       │   ├── User.java
│   │   │   │       │   ├── Chat.java
│   │   │   │       │   ├── Message.java
│   │   │   │       ├── /repository/     # Data Access Layer (JPA Repositories)
│   │   │   │       │   ├── UserRepository.java
│   │   │   │       │   ├── ChatRepository.java
│   │   │   │       │   ├── MessageRepository.java
│   │   │   │       ├── /service/        # Business Logic Layer (Services)
│   │   │   │       │   ├── AuthService.java
│   │   │   │       │   ├── UserService.java
│   │   │   │       │   ├── ChatService.java
│   │   │   │       │   ├── MessageService.java
│   │   │   │       │   ├── JwtService.java
│   │   │   │       ├── /util/           # Utility Classes (Helpers)
│   │   │   │       │   ├── PasswordUtil.java
│   │   │   │       ├── PookieTalkApplication.java # Main Spring Boot Entry Point
│   │   │   └── /resources/
│   │   │       └── application.yml   # Configurations (DB, Security, WebSocket)
│   │   └── /test/
│   │       └── /java/
│   │           └── /com/pookietalk/
│   │               ├── AuthServiceTest.java
│   │               ├── UserServiceTest.java
│   │   └── /docker/
│   │       ├── Dockerfile       # Docker containerization
│   │       └── docker-compose.yml # Backend + PostgreSQL setup
│   │   └── /scripts/
│   │       └── init-db.sql      # Database schema setup
│   ├── pom.xml             # Spring Boot dependencies
│   └── README.md           # Backend documentation
│
├── /frontend/
│   ├── /public/                # Static assets
│   │   ├── index.html         # Root HTML file
│   │   ├── logo.png
│   │   ├── robots.txt
│   │   └── manifest.json      # PWA support
│   ├── /src/
│   │   ├── /assets/            # Images, icons, global styles
│   │   │   ├── /icons/
│   │   │   └── /images/
│   │   ├── /components/        # Reusable UI components
│   │   │   ├── ChatBox.tsx
│   │   │   ├── Message.tsx
│   │   │   ├── Sidebar.tsx
│   │   │   ├── UserList.tsx
│   │   │   ├── Navbar.tsx
│   │   │   └── InputField.tsx
│   │   ├── /pages/             # App pages
│   │   │   ├── Login.tsx
│   │   │   ├── Register.tsx
│   │   │   ├── ChatRoom.tsx
│   │   │   └── Profile.tsx
│   │   ├── /context/           # Global state management (React Context API)
│   │   │   ├── AuthContext.tsx
│   │   │   └── ChatContext.tsx
│   │   ├── /service/          # API calls to backend
│   │   │   ├── authService.ts
│   │   │   ├── chatService.ts
│   │   │   └── userService.ts
│   │   ├── /hook/             # Custom React Hooks
│   │   │   ├── useAuth.ts
│   │   │   └── useChat.ts
│   │   ├── /route/            # React Router setup
│   │   │   └── AppRoutes.tsx
│   │   ├── /util/             # Utility functions (formatting, validation)
│   │   │   ├── formatDate.ts
│   │   │   └── validation.ts
│   │   ├── App.tsx            # Main App Component
│   │   └── index.tsx          # React entry point
│   ├── package.json           # Project dependencies
│   ├── tailwind.config.js     # Tailwind CSS config
│   ├── tsconfig.json          # TypeScript config
│   ├── vite.config.ts         # Vite config
│   └── README.md              # Documentation
│
├── docker-compose.yml         # Runs backend + frontend together
├── .gitignore                 # Ignore files for Git
└── README.md                  # Project Documentation


Step 1: Backend Development (Spring Boot + Java)
Setup Spring Boot Project

Configure dependencies in pom.xml

Setup application.yml (Database, Security, WebSocket)

Implement PookieTalkApplication.java (Main entry point)

Security & Authentication

Implement JWT-based authentication (JwtUtil.java, JwtAuthFilter.java, SecurityConfig.java)

User authentication (AuthService.java, AuthController.java)

Database & Models

Define JPA entities (User.java, Chat.java, Message.java)

Setup repositories (UserRepository.java, ChatRepository.java, MessageRepository.java)

Business Logic & API Endpoints

User services (UserService.java, UserController.java)

Chat services (ChatService.java, ChatController.java)

Message services (MessageService.java, MessageController.java)

WebSocket for Real-time Chat

Setup WebSocket (WebSocketConfig.java)

Implement chat handling logic

Testing & Deployment

Write unit tests (AuthServiceTest.java, UserServiceTest.java)

Setup Docker (Dockerfile, docker-compose.yml)

Once the backend is functional, we will move to Frontend Development (React + TypeScript).




hase 1: Authentication Setup
Goal: Implement login and registration with JWT handling.

Build AuthContext

File: /context/AuthContext.tsx

Store user, token, login(), logout()

Persist token in localStorage or sessionStorage

Create AuthService

File: /service/authService.ts

login(credentials): Promise<AuthResponseDTO>

register(payload): Promise<AuthResponseDTO>

Design Pages

/pages/Login.tsx

/pages/Register.tsx

Forms with Tailwind, submit to authService, store token in context

Token Handling

Attach token in API calls using Axios interceptor

File: /service/axiosConfig.ts

✅ Phase 2: Protected Routes + Routing
Goal: Ensure only logged-in users access app pages

Setup Routing

File: /route/AppRoutes.tsx

Define routes using react-router-dom

Create PrivateRoute wrapper component

Redirect Logic

If not logged in, redirect to /login

If logged in and visit /login, redirect to /chatroom

✅ Phase 3: User Listing and Profile
Goal: View all users and their profiles

UserService API

File: /service/userService.ts

getAllUsers(), getUserById()

Components

UserList.tsx: List of users from /api/users

Profile.tsx: View individual user details

UI Integration

Fetch user list after login

Token is sent automatically via Axios

✅ Phase 4: Chat Functionality
Goal: Enable real-time messaging via WebSockets

WebSocket Setup

Connect to backend WebSocket endpoint

File: /context/ChatContext.tsx

Components

ChatRoom.tsx: Main chat page

ChatBox.tsx: Messages display

Message.tsx: Individual message bubbles

InputField.tsx: Send new messages

Handling Messages

Real-time update using WebSocket onmessage

Store chat messages in ChatContext

✅ Phase 5: Polish & Utilities
Goal: Improve UX, error handling, and responsiveness

Error Handling

Show toast alerts for failed requests (e.g., using react-toastify)

Mobile Responsiveness

Tailwind responsive layout tweaks

Dark Mode Support (Optional)

Use Tailwind’s dark mode classes

✅ Phase 6: Testing & Deployment
Goal: Ensure stability and deploy full app

Test with Postman and frontend

Login, register, chat

Invalid tokens, expired tokens

Bundle with Docker

Use Dockerfile in frontend

Update docker-compose.yml to include frontend service

Push to GitHub + CI/CD (Optional)