-- Создание базы данных department
CREATE DATABASE department;

-- Переключение на базу данных department
\c department

--Создание таблицы
CREATE TABLE Departments
(
    ID          SERIAL PRIMARY KEY,
    DepCode     VARCHAR(20)  NOT NULL UNIQUE,
    DepJob      VARCHAR(100) NOT NULL UNIQUE,
    Description VARCHAR(255)
);

--Заполнение таблицы
INSERT INTO Departments (DepCode, DepJob, Description)
SELECT 'Dep' || i          AS DepCode,
       'Job' || i          AS DepJob,
       'Description ' || i AS Description
FROM generate_series(1, 10) AS t(i);

