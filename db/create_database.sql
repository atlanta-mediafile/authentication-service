CREATE DATABASE Auth;

USE Auth;

CREATE TABLE User (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    fullName VARCHAR(120) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    creationDate DATETIME default current_timestamp,
    ultimaActividad DATETIME default current_timestamp
);