-- Ensure database creation
CREATE DATABASE IF NOT EXISTS app;
USE app;

-- Drop existing tables
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS foods;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Foods table
CREATE TABLE foods (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    calories DECIMAL(10,2) NOT NULL,
    carbs DECIMAL(10,2) NOT NULL,
    fat DECIMAL(10,2) NOT NULL,
    protein DECIMAL(10,2) NOT NULL,
    barcode VARCHAR(255),
    serving_size INT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    edited TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    edited_by INT,
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
    user_id INT,
    food_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
);
