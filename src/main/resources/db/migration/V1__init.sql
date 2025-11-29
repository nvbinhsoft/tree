CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    excerpt TEXT NULL,
    body TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    published_at TIMESTAMPTZ NULL,
    cover_image_url TEXT NULL,
    read_time_minutes INT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE settings (
    id BIGSERIAL PRIMARY KEY,
    blog_title VARCHAR(255),
    blog_subtitle VARCHAR(255),
    author_name VARCHAR(255),
    author_bio TEXT,
    profile_image_url TEXT,
    social_github TEXT,
    social_linkedin TEXT,
    social_x TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

INSERT INTO users (username, password_hash, role)
VALUES ('admin', '$2a$10$qv2EmYSoX9SkYlCeaV8aMeqi0WBG9hHo0xHpCTE8AwkN7yG6PNfLS', 'ADMIN');

INSERT INTO settings (blog_title, blog_subtitle, author_name, author_bio)
VALUES ('My Blog', 'SWE musings', 'Admin', 'Welcome to the blog');
