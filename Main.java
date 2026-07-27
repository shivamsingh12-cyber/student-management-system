import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.initializeSchema();
        SchoolSystem school = new SchoolSystem(db);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Student Management System (JDBC) ===");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addStudent(scanner, school); break;
                case "2": listStudents(school); break;
                case "3": updateEmail(scanner, school); break;
                case "4": deleteStudent(scanner, school); break;
                case "5": addCourse(scanner, school); break;
                case "6": listCourses(school); break;
                case "7": enroll(scanner, school); break;
                case "8": recordGrade(scanner, school); break;
                case "9": viewTranscript(scanner, school); break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Update Student Email");
        System.out.println("4. Delete Student");
        System.out.println("5. Add Course");
        System.out.println("6. List All Courses");
        System.out.println("7. Enroll Student in Course");
        System.out.println("8. Record Grade");
        System.out.println("9. View Student Transcript");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addStudent(Scanner scanner, SchoolSystem school) {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());
        school.addStudent(name, email, age);
    }

    private static void listStudents(SchoolSystem school) {
        List<Student> students = school.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        System.out.println(String.format("%-4s | %-20s | %-25s | %s", "ID", "Name", "Email", "Age"));
        students.forEach(System.out::println);
    }

    private static void updateEmail(Scanner scanner, SchoolSystem school) {
        System.out.print("Student ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("New email: ");
        String email = scanner.nextLine().trim();
        System.out.println(school.updateStudentEmail(id, email) ? "Updated." : "Student not found.");
    }

    private static void deleteStudent(Scanner scanner, SchoolSystem school) {
        System.out.print("Student ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.println(school.deleteStudent(id) ? "Deleted." : "Student not found.");
    }

    private static void addCourse(Scanner scanner, SchoolSystem school) {
        System.out.print("Course name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Credit hours: ");
        int hours = Integer.parseInt(scanner.nextLine().trim());
        school.addCourse(name, hours);
    }

    private static void listCourses(SchoolSystem school) {
        List<Course> courses = school.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }
        courses.forEach(System.out::println);
    }

    private static void enroll(Scanner scanner, SchoolSystem school) {
        System.out.print("Student ID: ");
        int sid = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Course ID: ");
        int cid = Integer.parseInt(scanner.nextLine().trim());
        System.out.println(school.enrollStudent(sid, cid));
    }

    private static void recordGrade(Scanner scanner, SchoolSystem school) {
        System.out.print("Student ID: ");
        int sid = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Course ID: ");
        int cid = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Grade (0-100): ");
        double grade = Double.parseDouble(scanner.nextLine().trim());
        System.out.println(school.recordGrade(sid, cid, grade));
    }

    private static void viewTranscript(Scanner scanner, SchoolSystem school) {
        System.out.print("Student ID: ");
        int sid = Integer.parseInt(scanner.nextLine().trim());
        List<String> transcript = school.getTranscript(sid);
        if (transcript.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }
        transcript.forEach(System.out::println);
    }
}