# React + Spring Boot Full-Stack App with JWT

A complete full-stack application with React frontend and Spring Boot backend, featuring JWT authentication.

## Architecture

```
30-react-fullstack/
├── backend/          # Spring Boot REST API (port 8080)
│   └── src/main/java/com/anupam/fullstack/
└── frontend/         # React 18 + Vite (port 5173)
    └── src/
```

## Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Backend | Spring Boot | 3.5.0 |
| Backend | Java | 21 |
| Backend | Spring Security + JWT | jjwt 0.12.6 |
| Backend | H2 Database | (managed) |
| Frontend | React | 18.3.1 |
| Frontend | Axios | 1.7.2 |
| Frontend | React Router | 6.23.1 |
| Frontend | Vite | 5.3.1 |

## Running

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Default Credentials

- Username: `admin`
- Password: `admin123`

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/auth/login | No | Login, returns JWT |
| GET | /api/products | Yes | List all products |
| GET | /api/products/{id} | Yes | Get product by ID |
| POST | /api/products | Yes | Create product |
| PUT | /api/products/{id} | Yes | Update product |
| DELETE | /api/products/{id} | Yes | Delete product |

## Blog Post

See the full walkthrough: [React + Spring Boot — Full-Stack App with JWT Authentication](https://anupamsinha.github.io/java/spring/2026/08/22/react-spring-boot-fullstack-jwt.html)
