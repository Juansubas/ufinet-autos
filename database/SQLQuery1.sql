CREATE DATABASE car_management;
GO

USE car_management;
GO

-- Tabla de usuarios
CREATE TABLE users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);
GO

-- Tabla de autos
CREATE TABLE cars (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    brand VARCHAR(50),
    model VARCHAR(50),
    year INT,
    plate VARCHAR(20) UNIQUE,
    color VARCHAR(30),
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_Cars_User FOREIGN KEY (user_id) REFERENCES users(id)
);
GO

