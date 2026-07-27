import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // For PostgreSQL instead, this would be:
    //   "jdbc:postgresql://localhost:5432/school_db"
    private static final String URL =
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; // set this to your real password

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void initializeSchema() {
        String students = """
            CREATE TABLE IF NOT EXISTS students (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                age INT NOT NULL
            )""";

        String courses = """
            CREATE TABLE IF NOT EXISTS courses (
                id SERIAL PRIMARY KEY,
                course_name VARCHAR(100) NOT NULL,
                credit_hours INT NOT NULL
            )""";

        String enrollments = """
            CREATE TABLE IF NOT EXISTS enrollments (
                id SERIAL PRIMARY KEY,
                student_id INT REFERENCES students(id) ON DELETE CASCADE,
                course_id INT REFERENCES courses(id) ON DELETE CASCADE,
                grade DECIMAL(4,2),
                UNIQUE(student_id, course_id)
            )""";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(students);
            stmt.execute(courses);
            stmt.execute(enrollments);
            System.out.println("Database schema ready.");
        } catch (SQLException e) {
            System.out.println("Schema initialization error: " + e.getMessage());
        }
    }

    // ---------- Student CRUD ----------

    public void addStudent(Student s) {
        String sql = "INSERT INTO students (name, email, age) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setInt(3, s.getAge());
            ps.executeUpdate();
            System.out.println("Added student: " + s.getName());
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getInt("age")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public boolean updateStudentEmail(int studentId, String newEmail) {
        String sql = "UPDATE students SET email = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, studentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    // ---------- Course CRUD ----------

    public void addCourse(Course c) {
        String sql = "INSERT INTO courses (course_name, credit_hours) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getCreditHours());
            ps.executeUpdate();
            System.out.println("Added course: " + c.getCourseName());
        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY id";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"), rs.getString("course_name"), rs.getInt("credit_hours")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
        }
        return courses;
    }

    // ---------- Enrollment (many-to-many) ----------

    public String enrollStudent(int studentId, int courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
            return "Enrollment successful.";
        } catch (SQLException e) {
            return "Error enrolling student: " + e.getMessage();
        }
    }

    public String recordGrade(int studentId, int courseId, double grade) {
        String sql = "UPDATE enrollments SET grade = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, grade);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            int rows = ps.executeUpdate();
            return rows > 0 ? "Grade recorded." : "No matching enrollment found.";
        } catch (SQLException e) {
            return "Error recording grade: " + e.getMessage();
        }
    }

    public List<String> getTranscript(int studentId) {
        List<String> lines = new ArrayList<>();
        String sql = """
            SELECT c.course_name, c.credit_hours, e.grade
            FROM enrollments e
            JOIN courses c ON e.course_id = c.id
            WHERE e.student_id = ?
            ORDER BY c.course_name""";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String grade = rs.getObject("grade") != null
                            ? String.valueOf(rs.getDouble("grade")) : "Not graded yet";
                    lines.add(String.format("%-25s | %d credit(s) | Grade: %s",
                            rs.getString("course_name"), rs.getInt("credit_hours"), grade));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transcript: " + e.getMessage());
        }
        return lines;
    }
}