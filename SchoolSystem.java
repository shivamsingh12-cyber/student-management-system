import java.util.List;

public class SchoolSystem {
    private DatabaseManager db;

    public SchoolSystem(DatabaseManager db) {
        this.db = db;
    }

    public void addStudent(String name, String email, int age) {
        if (age <= 0 || age > 120) {
            System.out.println("Error: invalid age.");
            return;
        }
        db.addStudent(new Student(name, email, age));
    }

    public List<Student> getAllStudents() { return db.getAllStudents(); }

    public boolean updateStudentEmail(int studentId, String newEmail) {
        return db.updateStudentEmail(studentId, newEmail);
    }

    public boolean deleteStudent(int studentId) {
        return db.deleteStudent(studentId);
    }

    public void addCourse(String name, int creditHours) {
        db.addCourse(new Course(name, creditHours));
    }

    public List<Course> getAllCourses() { return db.getAllCourses(); }

    public String enrollStudent(int studentId, int courseId) {
        return db.enrollStudent(studentId, courseId);
    }

    public String recordGrade(int studentId, int courseId, double grade) {
        if (grade < 0 || grade > 100) return "Error: grade must be between 0 and 100.";
        return db.recordGrade(studentId, courseId, grade);
    }

    public List<String> getTranscript(int studentId) {
        return db.getTranscript(studentId);
    }
}
