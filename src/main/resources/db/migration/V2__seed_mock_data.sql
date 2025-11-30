-- Seed realistic sample tags
INSERT INTO tags (name, slug) VALUES
    ('Spring Boot', 'spring-boot'),
    ('Java', 'java'),
    ('API Design', 'api-design'),
    ('DevOps', 'devops'),
    ('PostgreSQL', 'postgresql'),
    ('JWT', 'jwt'),
    ('Security', 'security')
ON CONFLICT (slug) DO NOTHING;

-- Seed sample posts with Markdown bodies
INSERT INTO posts (slug, title, excerpt, body, status, published_at, cover_image_url, read_time_minutes, created_at, updated_at) VALUES
(
    'shipping-a-spring-boot-blog-api',
    'Shipping a Spring Boot Blog API in a Weekend',
    'How I scaffolded a clean Spring Boot blog backend with JWT, Flyway, and uploads.',
$$### What I built
I wanted a lean backend for a personal SWE blog: public endpoints, JWT-protected admin, Markdown storage, and image uploads.

### Stack choices
- Spring Boot 3 + Spring Security
- Flyway for migrations
- PostgreSQL with UUID-based slugs
- Local uploads served under `/assets/**`

### Code sample
```java
@PostMapping("/admin/posts")
public PostDetailResponse create(@Valid @RequestBody PostRequest request) {
    return postService.create(request);
}
```

### Takeaways
- Ship the simplest thing that works.
- Keep Markdown raw; let the client render it.
- Use JWT for the tiny admin surface.

![cover](/assets/posts/2025/01/spring-blog.jpg)
$$,
    'PUBLISHED',
    now() - interval '30 days',
    '/assets/posts/2025/01/spring-blog.jpg',
    6,
    now() - interval '31 days',
    now() - interval '30 days'
),
(
    'markdown-and-uploads',
    'Handling Markdown, Images, and Uploads Cleanly',
    'Lessons learned storing raw Markdown, code fences, and image uploads without messing with content.',
$$### Markdown rules
- Store the raw string; avoid server-side transforms.
- Keep image URLs stable (`/assets/...`).

### Upload flow
1. Admin uploads an image via `/api/admin/uploads`.
2. Backend returns `{ url, fileName, size }`.
3. Frontend drops `url` into Markdown: `![alt](/assets/...)`.

### Example Markdown
```markdown
## Hello world

Here is a code block:

```bash
curl -H "Authorization: Bearer <token>" \\
     -F file=@cover.png \\
     http://localhost:8080/api/admin/uploads
```
```
$$,
    'PUBLISHED',
    now() - interval '20 days',
    '/assets/posts/2025/01/uploads.jpg',
    7,
    now() - interval '21 days',
    now() - interval '20 days'
),
(
    'postgres-tips-for-side-projects',
    'PostgreSQL Tips for Side Projects',
    'Pragmatic defaults for a hobby Spring Boot + Postgres app.',
$$### Checklist
- Use UUIDs or slugs for external identifiers.
- Set `statement_timeout` in dev to catch bad queries early.
- Keep Flyway migrations small and reversible when possible.

### Connection snippet
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/blog
    username: user
    password: password
```
$$,
    'PUBLISHED',
    now() - interval '10 days',
    '/assets/posts/2025/01/postgres.jpg',
    5,
    now() - interval '11 days',
    now() - interval '10 days'
),
(
    'jwt-auth-without-pain',
    'JWT Auth without Shooting Yourself in the Foot',
    'Concrete tips for sane JWT handling in tiny admin surfaces.',
$$### Recommendations
- Keep the admin surface small.
- Use short-lived tokens (1 hour).
- Rotate the signing key and keep it at least 32 bytes.

### Sample payload
```json
{
  "sub": "admin",
  "role": "ADMIN",
  "exp": 1719930000
}
```
$$,
    'PUBLISHED',
    now() - interval '5 days',
    '/assets/posts/2025/01/jwt.jpg',
    4,
    now() - interval '6 days',
    now() - interval '5 days'
),
(
    'local-dev-playbook',
    'Local Dev Playbook for the Blog Backend',
    'How I run the stack locally: Dockerized Postgres, Gradle, and realistic seed data.',
$$### Steps
1. `docker compose up -d postgres`
2. `SPRING_PROFILES_ACTIVE=dev gradle bootRun`
3. Hit `http://localhost:8080/api/posts`

### Notes
- Flyway seeds an admin user and mock content.
- Use `./gradlew test` before pushing.
$$,
    'DRAFT',
    NULL,
    '/assets/posts/2025/01/dev-playbook.jpg',
    3,
    now() - interval '2 days',
    now() - interval '2 days'
);

-- Map tags to posts
INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t WHERE p.slug = 'shipping-a-spring-boot-blog-api' AND t.slug IN ('spring-boot', 'api-design', 'java');

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t WHERE p.slug = 'markdown-and-uploads' AND t.slug IN ('spring-boot', 'api-design');

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t WHERE p.slug = 'postgres-tips-for-side-projects' AND t.slug IN ('postgresql', 'devops');

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t WHERE p.slug = 'jwt-auth-without-pain' AND t.slug IN ('jwt', 'security', 'spring-boot');

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t WHERE p.slug = 'local-dev-playbook' AND t.slug IN ('devops', 'api-design');
