public class Course {
    private int id;
    private String courseName;
    private int creditHours;

    public Course(String courseName, int creditHours) {
        this.courseName = courseName;
        this.creditHours = creditHours;
    }

    public Course(int id, String courseName, int creditHours) {
        this.id = id;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }

    public int getId() { return id; }
    public String getCourseName() { return courseName; }
    public int getCreditHours() { return creditHours; }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %d credit(s)", id, courseName, creditHours);
    }
}