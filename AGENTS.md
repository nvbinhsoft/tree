# AGENTS.md — Backend (Spring Boot)

## Goal

Build a **Spring Boot backend API** for a personal, SWE-focused blog.

Responsibilities:

- Provide **public APIs** to read posts, tags, and public settings.
- Provide **admin APIs** (JWT protected) to manage posts, tags, and settings.
- Store post bodies as **Markdown** (including code blocks and images).
- Handle **image uploads** and return URLs that the frontend will embed in Markdown.
- Do **not** render HTML; the frontend handles Markdown rendering and layout.

---

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Web, Spring Security, Spring Data JPA, Validation
- PostgreSQL
- Flyway for database migrations
- JWT for authentication
- Local filesystem storage for images (configurable upload directory)
- Optional: Springdoc OpenAPI for Swagger UI

Base API path: `/api`

---

## Data Model (DB Schema)

Use PostgreSQL with at least these tables:

### `users`

- `id` BIGSERIAL PK
- `username` VARCHAR UNIQUE NOT NULL
- `password_hash` VARCHAR NOT NULL
- `role` VARCHAR NOT NULL (e.g. `ADMIN`)
- `created_at` TIMESTAMPTZ
- `updated_at` TIMESTAMPTZ

### `posts`

- `id` BIGSERIAL PK
- `slug` VARCHAR(255) UNIQUE NOT NULL
- `title` VARCHAR(255) NOT NULL
- `excerpt` TEXT NULL
- `body` TEXT NOT NULL  
  - Raw **Markdown** string. May contain `![alt](url)` and fenced code blocks.
- `status` VARCHAR(20) NOT NULL (`DRAFT` or `PUBLISHED`)
- `published_at` TIMESTAMPTZ NULL
- `cover_image_url` TEXT NULL
- `read_time_minutes` INT NULL
- `created_at` TIMESTAMPTZ DEFAULT now()
- `updated_at` TIMESTAMPTZ DEFAULT now()

### `tags`

- `id` BIGSERIAL PK
- `name` VARCHAR(100) NOT NULL
- `slug` VARCHAR(100) UNIQUE NOT NULL
- `created_at` TIMESTAMPTZ
- `updated_at` TIMESTAMPTZ

### `post_tags`

- `post_id` BIGINT FK → posts.id
- `tag_id` BIGINT FK → tags.id
- Composite PK (`post_id`, `tag_id`)

### `settings`

Single-row table for blog configuration:

- `id` BIGSERIAL PK
- `blog_title` VARCHAR
- `blog_subtitle` VARCHAR
- `author_name` VARCHAR
- `author_bio` TEXT
- `profile_image_url` TEXT
- `social_github` TEXT
- `social_linkedin` TEXT
- `social_x` TEXT

(Optionally) `post_views` table can be added later.

Use Flyway migration scripts to create these tables and seed:
- One admin user (with a known initial password).
- One default settings row.

---

## Security & Auth

- Use Spring Security with JWT Bearer tokens.
- Public endpoints (read-only) do **not** require auth.
- Admin endpoints under `/api/admin/**` require `Authorization: Bearer <token>`.

### Login

- `POST /api/auth/login`
  - Request: `{ "username": "...", "password": "..." }`
  - Validate credentials against `users` table.
  - On success, return JSON:
    ```json
    {
      "accessToken": "<JWT>",
      "tokenType": "Bearer",
      "expiresIn": 3600
    }
    ```
  - JWT should encode:
    - Subject: username
    - Role: `ADMIN`
    - Expiration timestamp

Implement a JWT filter that:
- Extracts the token from `Authorization` header.
- Validates signature and expiry.
- Sets `SecurityContext` with an authenticated principal having role `ROLE_ADMIN`.

---

## Public API

Base path for public API: `/api`

### `GET /posts`

- Query params:
  - `page` (int, default 0)
  - `size` (int, default 10, max 100)
  - `tag` (string, optional, tag slug)
  - `q` (string, optional, text search on title/body)
  - `sort` (string, `publishedAtDesc` | `publishedAtAsc`, default `publishedAtDesc`)
- Behavior:
  - Return only posts with `status = PUBLISHED`.
  - Apply tag and search filters if provided.
- Response:
  - Paginated JSON with `content`, `page`, `size`, `totalElements`, `totalPages`, `last`.
  - Each item: `id`, `slug`, `title`, `excerpt`, tags, `coverImageUrl`, `readTimeMinutes`, `publishedAt`.

### `GET /posts/{slug}`

- Path param: `slug`
- Behavior:
  - Find a post by slug with `status = PUBLISHED`.
- Response JSON includes:
  - `id`, `slug`, `title`, `excerpt`
  - `body` (raw Markdown string)
  - `tags` (id, name, slug)
  - `coverImageUrl`
  - `readTimeMinutes`
  - `publishedAt`

### `GET /tags`

- Return all tags as JSON list: `id`, `name`, `slug`.

### `GET /settings/public`

- Return public fields from `settings` table:
  - Blog title, subtitle, author name, bio, profile image URL, social links.

---

## Admin API (JWT Protected)

All paths start with `/api/admin` and require `ROLE_ADMIN`.

### Posts

- `GET /admin/posts`
  - Query params: `page`, `size`, `status`, `q`.
  - Returns paginated list of posts (draft + published).

- `GET /admin/posts/{id}`
  - Return full admin view of a post including `body`, `status`, `created_at`, `updated_at`.

- `POST /admin/posts`
  - Request body (JSON):
    - `title` (required)
    - `slug` (optional; auto-generate from title if missing)
    - `excerpt` (optional)
    - `body` (Markdown, required)
    - `status` (`DRAFT` or `PUBLISHED`)
    - `tagIds` (array of tag IDs)
    - `coverImageUrl` (optional)
  - Behavior:
    - Generate slug if absent and ensure uniqueness.
    - Compute `read_time_minutes` from word count of `body`.
    - If status is `PUBLISHED` and `published_at` is null, set `published_at = now()`.

- `PUT /admin/posts/{id}`
  - Same fields as create.
  - Update post; recalc read time if body changed.

- `DELETE /admin/posts/{id}`
  - Hard delete is acceptable for this personal blog.

- `POST /admin/posts/{id}/publish`
  - Set `status = PUBLISHED`, set `published_at` if null.

- `POST /admin/posts/{id}/unpublish`
  - Set `status = DRAFT`.

### Tags

- `GET /admin/tags`
  - Returns all tags.

- `POST /admin/tags`
  - Body: `{ "name": "Spring Boot" }`
  - Behavior:
    - Generate unique slug (kebab-case) from name.

- `PUT /admin/tags/{id}`
  - Update tag name (and optionally slug).

- `DELETE /admin/tags/{id}`

### Settings

- `GET /admin/settings`
  - Return the single settings row.

- `PUT /admin/settings`
  - Update blog title, subtitle, author info, profile image URL, and social links.

---

## Image Upload

### `POST /admin/uploads`

- Content-Type: `multipart/form-data`
- Field: `file` (image)
- Behavior:
  - Validate file type and size (basic checks).
  - Save to a configured upload directory, e.g. `uploads/posts/{yyyy}/{MM}/{uuid}.{ext}`.
  - Map static resources so files under the upload directory are served at a public URL prefix, e.g. `/assets/**`.
- Response JSON:
  ```json
  {
    "id": 42,
    "url": "/assets/posts/2025/11/uuid.png",
    "fileName": "uuid.png",
    "size": 123456
  }
  ```

The frontend will insert this `url` into Markdown as `![alt text](/assets/...)`. Backend does not alter the order of content; it just stores Markdown as a string in `posts.body`.

---

## Implementation Hints

### Package Structure (suggested)

```text
com.example.blog
  config/
    SecurityConfig.java
    WebConfig.java
  auth/
    AuthController.java
    JwtTokenProvider.java
    JwtAuthenticationFilter.java
    CustomUserDetailsService.java
  post/
    PostEntity.java
    TagEntity.java
    PostRepository.java
    TagRepository.java
    PostService.java
    PostController.java        # public
    PostAdminController.java   # admin
  settings/
    SettingsEntity.java
    SettingsRepository.java
    SettingsService.java
    SettingsController.java
  upload/
    UploadController.java
    FileStorageService.java
  common/
    ErrorHandler.java          # @ControllerAdvice for ErrorResponse
    dto/                       # request/response DTOs
```

### Read Time Calculation

- Simple approach:
  - Count words in `body` (Markdown).  
  - Assume ~230 words per minute.  
  - `read_time_minutes = max(1, ceil(wordCount / 230))`.

### Slug Generation

- Lowercase, trim whitespace.
- Replace spaces with `-`.
- Remove/normalize special characters.
- If slug exists, append suffix (`-2`, `-3`, etc.).

The backend should focus on providing clean, predictable JSON and stable image URLs. The frontend will handle layout, Substack-like typography, Markdown rendering, and code presentation.
