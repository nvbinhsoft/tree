# Blog Backend API

Spring Boot backend for a personal SWE-focused blog. It exposes public read-only endpoints for posts, tags, and settings plus JWT-protected admin endpoints to manage content and uploads.

## Prerequisites
- Java 17+
- Gradle 8+ (or use `gradle` installed locally)
- PostgreSQL 14+

## Project layout
- `src/main/resources/db/migration`: Flyway migrations (schema + seed data)
- `src/main/resources/application.yml`: Default configuration (datasource, JWT, uploads)
- `openapi-blog.yaml`: Reference OpenAPI contract for the API surface

## Database setup
1. Start PostgreSQL locally and create a database/user:
   ```bash
   psql -U postgres -c "CREATE DATABASE blog;"
   psql -U postgres -c "CREATE USER blog_user WITH ENCRYPTED PASSWORD 'change-me';"
   psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE blog TO blog_user;"
   ```
2. Update `spring.datasource` credentials in `src/main/resources/application.yml` or override via environment variables:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blog
   export SPRING_DATASOURCE_USERNAME=blog_user
   export SPRING_DATASOURCE_PASSWORD=change-me
   ```
3. On application startup Flyway will create tables and seed:
   - an admin user (`username: admin`, password hash stored in migration; replace with your own bcrypt hash before production)
   - a default settings row

To change the admin password, generate a bcrypt hash with Spring Security (e.g., `new BCryptPasswordEncoder().encode("newpass")`) and update the `password_hash` value in `V1__init.sql` or in the `users` table.

## Running the application
```bash
gradle bootRun
```
The API is served under the `/api` context path (e.g., `http://localhost:8080/api/posts`). Static uploads are served from the configured `app.upload.base-dir` at the `/assets/**` URL prefix.

## Authentication
- Login: `POST /api/auth/login` with `{ "username": "admin", "password": "<your password>" }`
- Use the returned Bearer token for admin routes under `/api/admin/**`.

## File uploads
- Authenticated endpoint: `POST /api/admin/uploads` with multipart field `file`
- Files are stored under `app.upload.base-dir` (default `uploads/`) and exposed at `/assets/**`

## Frontend integration tips
- Public endpoints for posts/tags/settings are unauthenticated and return Markdown bodies for client-side rendering.
- Insert uploaded image URLs directly into Markdown as `![alt text](/assets/...)`.
- Use `openapi-blog.yaml` to generate client types for the frontend if desired.

## Tests
Run the test suite (uses JUnit 5):
```bash
gradle test
```
Note: In restricted environments without Maven Central access, dependency resolution may fail; ensure the build can reach external repositories.
