-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

-- Beer entries table
CREATE TABLE beer_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    photo_url VARCHAR(500) NOT NULL,
    beer_name VARCHAR(255),
    location VARCHAR(255),
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    entry_year INTEGER
);

-- Indexes for performance
CREATE INDEX idx_beer_entries_user_id ON beer_entries(user_id);
CREATE INDEX idx_beer_entries_year ON beer_entries(entry_year);
CREATE INDEX idx_users_username ON users(username);
