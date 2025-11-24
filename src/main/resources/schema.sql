CREATE TABLE IF NOT EXISTS users (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

-- We can add more later (tips, projects, etc.)
