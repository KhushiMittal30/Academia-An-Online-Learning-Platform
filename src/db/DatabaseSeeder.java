package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSeeder {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL. Seeding database...");

            // Clear tables to avoid duplicates
            stmt.executeUpdate("DELETE FROM STUDENT_COURSE_ENROLLMENT");
            stmt.executeUpdate("DELETE FROM COURSE");
            stmt.executeUpdate("DELETE FROM FACULTY");
            stmt.executeUpdate("DELETE FROM STUDENT");

            // Insert students with specific IDs and hashed passwords
            insertStudent(conn, "Alice", "alpha", 103222001);
            insertStudent(conn, "Bob", "beta", 103222002);
            insertStudent(conn, "Charlie", "gamma", 103222003);

            insertFaculty(conn);
            insertCourses(conn);
            insertFacultyCourse(conn);
            insertStudentCourseEnrollment(conn);

            System.out.println("Database seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Database seeding failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertStudent(Connection conn, String name, String password, int studentId) throws SQLException {
        String hashedPassword = DatabaseConnection.hashPassword(password);
        String sql = "INSERT INTO STUDENT (StudentID, StudentName, PasswordHash) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, name);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
        }
    }

    private static void insertFaculty(Connection conn) throws SQLException {
        String sql = "INSERT INTO FACULTY (FacultyName) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String[] facultyNames = {"Dr. John Doe", "Dr. Jane Smith", "Dr. Alan Turing", "Dr. Grace Hopper", "Dr. Richard Feynman"};
            for (String name : facultyNames) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        }
    }

    private static void insertCourses(Connection conn) throws SQLException {
        String sql = "INSERT INTO COURSE (CourseName, CourseDescription, CompletionTime, FacultyID, Topics, Resources) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object[][] courses = {
                    {"Mathematics", "Introduction to calculus and algebra", "6 weeks", 1,
                            "Algebra, Calculus, Geometry", "https://math1.com, https://math2.com"},
                    {"Computer Science", "Learn programming and algorithms", "8 weeks", 2,
                            "Programming, Data Structures, Algorithms", "https://cs1.com, https://cs2.com"},
                    {"Physics", "Fundamental laws of nature", "7 weeks", 3,
                            "Mechanics, Thermodynamics, Quantum Physics", "https://physics1.com, https://physics2.com"},
                    {"Biology", "Living organisms and ecosystems", "10 weeks", 4,
                            "Cell Biology, Genetics, Ecology", "https://bio1.com, https://bio2.com"},
                    {"Machine Learning", "AI and neural networks", "12 weeks", 5,
                            "Supervised Learning, Neural Networks, Deep Learning", "https://ml1.com, https://ml2.com"},
                    {"Software Engineering", "Software development practices", "9 weeks", 1,
                            "Agile, Design Patterns, Testing", "https://se1.com, https://se2.com"},
                    {"Data Science", "Data analytics and visualization", "10 weeks", 2,
                            "Statistics, Data Visualization, Big Data", "https://ds1.com, https://ds2.com"},
                    {"Cybersecurity", "Protecting systems and networks", "8 weeks", 3,
                            "Network Security, Cryptography, Ethical Hacking", "https://cyber1.com, https://cyber2.com"}
            };

            for (Object[] course : courses) {
                stmt.setString(1, (String) course[0]);
                stmt.setString(2, (String) course[1]);
                stmt.setString(3, (String) course[2]);
                stmt.setInt(4, (int) course[3]);
                stmt.setString(5, (String) course[4]);
                stmt.setString(6, (String) course[5]);
                stmt.executeUpdate();
            }
        }
    }

    private static void insertFacultyCourse(Connection conn) throws SQLException {
        String sql = "INSERT INTO FACULTY_COURSE (FacultyID, CourseID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int[][] facultyCourses = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {1, 6}, {2, 7}, {3, 8}};
            for (int[] fc : facultyCourses) {
                stmt.setInt(1, fc[0]);
                stmt.setInt(2, fc[1]);
                stmt.executeUpdate();
            }
        }
    }

    private static void insertStudentCourseEnrollment(Connection conn) throws SQLException {
        String sql = "INSERT INTO STUDENT_COURSE_ENROLLMENT (StudentID, CourseID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int[][] studentCourses = {{103222001, 1}, {103222001, 3}, {103222002, 2}, {103222002, 4}, {103222003, 5}, {103222003, 7}, {103222001, 6}, {103222002, 8}};
            // Updated StudentID to match the new seeded values
            for (int[] sc : studentCourses) {
                stmt.setInt(1, sc[0]);
                stmt.setInt(2, sc[1]);
                stmt.executeUpdate();
            }
        }
    }
}