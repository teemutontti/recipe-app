-- Ensure database creation
CREATE DATABASE IF NOT EXISTS app;
USE app;

-- Drop existing tables
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS foods;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Foods table
CREATE TABLE foods (
    id CHAR(36) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    calories DECIMAL(10,2) NOT NULL,
    carbs DECIMAL(10,2) NOT NULL,
    fat DECIMAL(10,2) NOT NULL,
    protein DECIMAL(10,2) NOT NULL,
    barcode VARCHAR(255),
    serving_size INT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36),
    edited TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    edited_by CHAR(36),
    FOREIGN KEY (edited_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Logs table
CREATE TABLE logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    meal VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    time TIME,
    user_id CHAR(36),
    food_id CHAR(36),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
);
