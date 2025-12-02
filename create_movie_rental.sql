DROP DATABASE IF EXISTS movie_rental;
CREATE DATABASE movie_rental;
USE movie_rental;

CREATE TABLE Movie (
    movieId BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(100),
    releaseYear YEAR,
    durationInMinutes INT,
    rating VARCHAR(10),
    availableCopies INT DEFAULT 0,
    timesRented INT DEFAULT 0,
    PRIMARY KEY(movieId)
);

CREATE TABLE User (
    userId BIGINT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(userId)
);

CREATE TABLE UserRole (
    username VARCHAR(50) NOT NULL,
    rolename VARCHAR(50) NOT NULL,
    PRIMARY KEY(username, rolename)
);

CREATE TABLE Rental (
    rentalId BIGINT NOT NULL AUTO_INCREMENT,
    movieId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    rentalDate DATE NOT NULL,
    dueDate DATE NOT NULL,
    dateReturned DATE,  -- NULL means not yet returned
    PRIMARY KEY(rentalId),
    FOREIGN KEY (movieId) REFERENCES Movie(movieId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE,
    INDEX idx_user_rental (userId),
    INDEX idx_movie_rental (movieId)
);

INSERT INTO Movie (title, genre, releaseYear, durationInMinutes, rating, availableCopies, timesRented)
VALUES 
    -- Drama
    ('The Shawshank Redemption', 'Drama', 1994, 142, 'R', 5, 250),
    ('Forrest Gump', 'Drama', 1994, 142, 'PG-13', 4, 180),
    ('The Green Mile', 'Drama', 1999, 189, 'R', 3, 120),
    
    -- Action
    ('The Dark Knight', 'Action', 2008, 152, 'PG-13', 3, 300),
    ('Mad Max: Fury Road', 'Action', 2015, 120, 'R', 4, 150),
    ('John Wick', 'Action', 2014, 101, 'R', 5, 220),
    
    -- Sci-Fi
    ('Inception', 'Sci-Fi', 2010, 148, 'PG-13', 4, 280),
    ('The Matrix', 'Sci-Fi', 1999, 136, 'R', 3, 310),
    ('Interstellar', 'Sci-Fi', 2014, 169, 'PG-13', 2, 200),
    ('Blade Runner 2049', 'Sci-Fi', 2017, 164, 'R', 3, 95),
    
    -- Comedy
    ('Superbad', 'Comedy', 2007, 113, 'R', 6, 175),
    ('The Grand Budapest Hotel', 'Comedy', 2014, 99, 'R', 4, 140),
    ('Groundhog Day', 'Comedy', 1993, 101, 'PG', 3, 160),
    
    -- Horror
    ('Get Out', 'Horror', 2017, 104, 'R', 4, 185),
    ('A Quiet Place', 'Horror', 2018, 90, 'PG-13', 5, 210),
    ('Hereditary', 'Horror', 2018, 127, 'R', 2, 130),
    
    -- Romance
    ('The Notebook', 'Romance', 2004, 123, 'PG-13', 5, 190),
    ('La La Land', 'Romance', 2016, 128, 'PG-13', 3, 165),
    
    -- Thriller
    ('Se7en', 'Thriller', 1995, 127, 'R', 3, 145),
    ('Gone Girl', 'Thriller', 2014, 149, 'R', 4, 170),
    
    -- Animation
    ('Spirited Away', 'Animation', 2001, 125, 'PG', 4, 155),
    ('Toy Story', 'Animation', 1995, 81, 'G', 6, 240),
    ('WALL-E', 'Animation', 2008, 98, 'G', 5, 200),
    
    -- Fantasy
    ('The Lord of the Rings: The Fellowship of the Ring', 'Fantasy', 2001, 178, 'PG-13', 3, 275),
    ('Harry Potter and the Prisoner of Azkaban', 'Fantasy', 2004, 142, 'PG', 4, 220),

	('Baby Driver', 'Action', 2017, 115, 'R', 3, 180);
    
-- Insert Users (Note: passwords should be bcrypt hashed in real application)
-- These are placeholder passwords - in production use bcrypt!
INSERT INTO User (firstName, lastName, email, username, password)
VALUES 
    ('John', 'Smith', 'john.smith@email.com', 'smith', '1234'),
    ('Sarah', 'Johnson', 'sarah.j@email.com', 'john', '1234'),
    ('Mike', 'Davis', 'mike.d@email.com', 'mike', '1234'),
    ('Emily', 'Wilson', 'emily.w@email.com', 'emily', '1234'),
    ('David', 'Brown', 'david.b@email.com', 'david', '1234'),
    ('Lisa', 'Taylor', 'lisa.t@email.com', 'lisa', '1234'),
    ('James', 'Anderson', 'james.a@email.com', 'james', '1234'),
    ('Maria', 'Martinez', 'maria.m@email.com', 'maria', '1234'),
    ('Robert', 'Garcia', 'robert.g@email.com', 'robert', '1234'),
    ('Jennifer', 'Rodriguez', 'jennifer.r@email.com', 'jennifer', '1234');
    
INSERT INTO UserRole (username, rolename) VALUES
('smith', 'manager'),
('john', 'user'),
('mike', 'user'),
('emily', 'user'),
('david', 'user'),
('lisa', 'user'),
('james', 'user'),
('maria', 'user'),
('robert', 'user'),
('jennifer', 'user');

-- Insert Active Rentals (movies currently checked out)
INSERT INTO Rental (movieId, userId, rentalDate, dueDate, dateReturned)
VALUES 
    -- Active rentals (dateReturned is NULL)
    (1, 1, '2025-10-10', '2025-10-31', NULL),
    (7, 2, '2025-10-15', '2025-11-05', NULL),
    (4, 3, '2025-10-18', '2025-11-08', NULL),
    (14, 4, '2025-10-20', '2025-11-10', NULL),
    (22, 5, '2025-10-22', '2025-11-12', NULL),
    
    -- Recently returned rentals
    (8, 1, '2025-09-15', '2025-10-05', '2025-10-03'),
    (2, 2, '2025-09-20', '2025-10-10', '2025-10-08'),
    (11, 3, '2025-09-25', '2025-10-15', '2025-10-14'),
    (17, 6, '2025-10-01', '2025-10-21', '2025-10-19'),
    (5, 7, '2025-10-05', '2025-10-25', '2025-10-23'),
    
    -- Older rental history
    (3, 8, '2025-08-10', '2025-08-30', '2025-08-28'),
    (9, 9, '2025-08-15', '2025-09-04', '2025-09-02'),
    (19, 10, '2025-08-20', '2025-09-09', '2025-09-07'),
    (24, 1, '2025-07-12', '2025-08-01', '2025-07-30'),
    (6, 4, '2025-07-18', '2025-08-07', '2025-08-05');