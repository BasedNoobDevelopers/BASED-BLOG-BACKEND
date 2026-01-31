# Blog Platform – Backend Project Plan

This repository documents the **design, architecture, and implementation plan** for a production-minded blogging platform backend. The goal of this project is not just to build CRUD endpoints, but to demonstrate **clean architecture, security, scalability, and real-world backend decision making**.

---

## 🎯 Project Goals

- Build a **multi-user blogging platform**
- Apply **production-ready backend patterns**
- Practice **API design, authentication, caching, and data modeling**
- Serve as a **portfolio-quality backend project**

This project is intentionally backend-focused and designed to evolve incrementally.

---

## 🧱 Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA / Hibernate

### Database & Caching
- PostgreSQL (primary datastore)
- Redis (read-through cache)

### Infrastructure
- Docker & Docker Compose
- AWS (planned)
    - S3 (media storage)
    - RDS (Postgres)
    - ECS / EC2
    - CloudWatch

### CI/CD
- GitHub Actions

---

## 🗂️ Core Domain Models

### User
Represents a registered platform user.

**Fields**
- `id`
- `firstName`
- `lastName`
- `username` (unique)
- `email` (unique)
- `password` (hashed)
- `role` (ENUM)
- `createdAt`
- `lastLogin`
- `avatar` (S3 reference)

---

### Blog
Represents a blog post authored by a user.

**Fields**
- `id`
- `authorId`
- `title` (6–100 chars)
- `content` (long text / markdown)
- `createdAt`
- `edited` (boolean)
- `lastEditedAt`
- `coverImage` (S3 reference)
- `blogType` (ENUM)

---

### Tag
Represents reusable tags attached to blog posts.

**Fields**
- `id`
- `name` (unique)
- `createdAt`

**Relationships**
- Many-to-many with `Blog` via join table `blog_tag`

---

## 🔗 Database Relationships

- **User → Blog**: One-to-many
- **Blog → Tag**: Many-to-many
- **Blog ↔ Tag Join Table**: `blog_tag (blog_id, tag_id)`

Indexes are applied for:
- Tag name lookups
- Blog-tag joins
- Pagination queries

---

## 📦 DTO Design

Entities are **never exposed directly** via APIs.

### Auth DTOs
- `UserRegistrationDTO`
- `UserLoginDTO`
- `UserDTO`

### Blog DTOs
- `BlogCreateDTO`
- `BlogUpdateDTO`
- `BlogDTO`

**Notes**
- Create and update DTOs are intentionally separated
- Tags are exposed as `List<String>` in responses

---

## 🔐 Authentication & Security

- JWT-based authentication
- Role-based authorization
- Only authors can edit/delete their own blogs
- Admin override capability
- Secure password hashing

---

## 🌐 API Endpoints

### Auth
```
POST /auth/register
POST /auth/login
POST /auth/logout
```

### Blog
```
POST   /blogs
PUT    /blogs/{id}
DELETE /blogs/{id}
GET    /blogs
GET    /blogs/{id}
GET    /blogs/search
GET    /blogs/my
```

---

## ⚡ Caching Strategy (Redis)

- Read-through caching for blog retrieval
- Cache populated on first read
- Cache invalidated on:
    - Blog edit
    - Blog delete
- Database remains the source of truth

---

## 🧪 Error Handling

- Centralized exception handling (`@ControllerAdvice`)
- Consistent error response format
- Validation errors handled at DTO level

---

## 🐳 Local Development

- Docker Compose
    - App container
    - PostgreSQL
    - Redis

Environment variables managed via `.env`

---

## 🚀 Deployment (Planned)

- Dockerized Spring Boot application
- AWS ECS or EC2
- RDS PostgreSQL
- S3 for media storage
- CloudWatch logging

---

## 🔮 Future Enhancements

- Likes system
- Comment system
- Full-text search
- Background media support (optional)

---

## 📌 Development Phases

1. Core auth + blog CRUD
2. DTO validation and error handling
3. Pagination and search
4. Redis caching
5. Dockerization
6. Cloud deployment

---

## 🧠 Design Philosophy

- Start with a monolith
- Favor clarity over premature optimization
- Build features incrementally
- Treat this like a real production service

---

## 📄 License

This project is for educational and portfolio purposes.

