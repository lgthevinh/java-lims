DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS BorrowDetail;
DROP TABLE IF EXISTS Librarian;
DROP TABLE IF EXISTS Student;

CREATE TABLE Book
(
    isbn                VARCHAR(13) PRIMARY KEY,
    title               TEXT,
    author              VARCHAR(50),
    year_of_publication DATE,
    publisher           VARCHAR(50),
    image_url           VARCHAR(100),
    available_amount    INTEGER DEFAULT 1
);

CREATE TABLE User
(
    id            INTEGER AUTO_INCREMENT PRIMARY KEY,
    social_id     VARCHAR(13) UNIQUE,
    name          VARCHAR(50),
    date_of_birth DATE,
    address_line  TEXT,
    phone_number  NUMERIC,
    email         VARCHAR(30),
    password      VARCHAR(50)
);

CREATE TABLE Student
(
    student_id  VARCHAR(10) UNIQUE PRIMARY KEY,
    user_id     INTEGER UNIQUE REFERENCES User (id),
    school_name VARCHAR(30),
    major       VARCHAR(30)
);

CREATE TABLE Librarian
(
    emp_id  INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES User (id)
);

CREATE TABLE BorrowDetail
(
    id                   INTEGER AUTO_INCREMENT PRIMARY KEY,
    book_isbn            VARCHAR(13) REFERENCES Book (isbn),
    borrower_id          NUMERIC REFERENCES User (id),
    librarian_id         NUMERIC REFERENCES Librarian (emp_id),
    borrow_date          DATE,
    expected_return_date DATE,
    actual_return_date   DATE
);

-- CREATE TABLE AppEntity
-- {
--     key VARCHAR(100) PRIMARY KEY
--     value VARCHAR(100)
-- };