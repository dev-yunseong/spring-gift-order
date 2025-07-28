CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(15) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE email_members (
    id BIGINT PRIMARY KEY REFERENCES members(id),
    email VARCHAR(30) NOT NULL UNIQUE,
    password_hash CHAR(60) NOT NULL
);

CREATE TABLE social_members (
    id BIGINT PRIMARY KEY REFERENCES members(id),
    provider_id BIGINT NOT NULL UNIQUE,
    provider VARCHAR(30) NOT NULL,
    nickname VARCHAR(30) NOT NULL UNIQUE,
    profile_image VARCHAR(255) NOT NULL,
    UNIQUE (provider_id, provider)
);

CREATE TABLE wishes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT REFERENCES members(id),
    product_id BIGINT REFERENCES products(id),
    count INT,
    UNIQUE (member_id, product_id)
);

CREATE TABLE options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL REFERENCES products(id),
    name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    UNIQUE (product_id, name)
);

CREATE TABLE option_buyings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_id BIGINT NOT NULL REFERENCES options(id),
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    message VARCHAR(255)
);
