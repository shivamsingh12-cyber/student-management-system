🎓 Student Management System (JDBC)
A console-based Student Management System built with core Java and JDBC, persisting to a real relational database instead of flat files — the natural next step up from file-based persistence toward real backend development.
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)
Overview
This project manages students, courses, and enrollments through a proper relational schema — not just flat CSV records. It's the JDBC step in a learning progression: console app → JDBC + database → Spring Boot REST API → full-stack app.
Features
Student CRUD — add, list, update email, and delete students
Course management — add and list available courses
Enrollment system — many-to-many relationship between students and courses via a dedicated `enrollments` join table
Grade recording — assign grades per enrollment
Transcript generation — SQL `JOIN` across three tables to produce a readable per-student transcript
Auto schema creation — tables are created automatically on first run, no manual SQL setup required
SQL injection prevention — every query uses `PreparedStatement`, never string-concatenated SQL
Tech Stack
Component	Technology
Language	Java 17+
Persistence	JDBC (`PreparedStatement`, `ResultSet`)
Database	MySQL (PostgreSQL also supported — see below)
Interface	Console (menu-driven)
Project Structure
```
student-management/
├── src/
│   ├── Student.java          — Student model (plain POJO)
│   ├── Course.java           — Course model (plain POJO)
│   ├── DatabaseManager.java  — All JDBC/SQL logic lives here
│   ├── SchoolSystem.java     — Business logic & validation layer
│   └── Main.java             — Menu-driven CLI entry point
└── README.md
```
Database Schema
```
students                courses                 enrollments
─────────               ─────────                ───────────
id (PK)                 id (PK)                  id (PK)
name                     course_name              student_id (FK → students.id)
email (unique)          credit_hours             course_id (FK → courses.id)
age                                               grade
```
`enrollments` is the join table representing the many-to-many relationship between students and courses — one student can take many courses, one course can have many students.
Getting Started
Prerequisites
JDK 17 or newer
MySQL Community Server (download)
MySQL Connector/J driver jar (download)
Setup
```bash
git clone https://github.com/Abhishek102501/Student-Management-System.git
cd Student-Management-System
```
Create the database:
```sql
   mysql -u root -p
   CREATE DATABASE school_db;
   EXIT;
   ```
Place the downloaded MySQL Connector/J `.jar` in the project root (same level as `src/`)
Update credentials in `src/DatabaseManager.java` if different from defaults:
```java
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```
Compile and run:
```bash
   cd src
   javac -cp ".:../mysql-connector-j-9.7.0.jar" *.java
   java -cp ".:../mysql-connector-j-9.7.0.jar" Main
   ```
(Windows: replace `:` with `;` in the classpath)
Try it out
`1` → Add a student
`5` → Add a course
`7` → Enroll that student in that course
`8` → Record a grade
`9` → View their transcript — course name, credits, and grade joined together
`2` / `6` → List all students/courses to confirm persistence
Using PostgreSQL Instead
Only `DatabaseManager.java` needs to change — this is the whole point of JDBC as an abstraction layer:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/school_db";
```
Swap the driver jar to the PostgreSQL JDBC driver, and nothing else in the project changes.
Key Design Decisions
All SQL isolated in `DatabaseManager` — swapping databases means touching one file, not the whole codebase
`PreparedStatement` everywhere — parameterized queries instead of string concatenation, which is the standard defense against SQL injection
Validation layer (`SchoolSystem`) sits between the CLI and the database, keeping `DatabaseManager` a pure data-access layer
Auto-schema creation using `CREATE TABLE IF NOT EXISTS` so the project runs on a fresh database with zero manual setup
Possible Extensions
Wrap this in a Spring Boot REST API using Spring Data JPA
Add authentication with a `PreparedStatement`-based login table
Write JUnit tests for `SchoolSystem`'s validation logic
Connection pooling (HikariCP) instead of a new connection per query
Related Project
See the Library Management System — the CSV-based predecessor to this project in the same learning progression.
License
MIT — free to use and modify.
