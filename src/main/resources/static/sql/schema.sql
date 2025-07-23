CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(15) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(30) NOT NULL UNIQUE,
    password_hash CHAR(60) NOT NULL
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
