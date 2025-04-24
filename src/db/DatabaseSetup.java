package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL. Setting up database...");

            // Drop tables in correct order (child tables first)
            stmt.executeUpdate("DROP TABLE IF EXISTS QUIZ_OPTIONS");
            stmt.executeUpdate("DROP TABLE IF EXISTS QUIZ_QUESTION");
            stmt.executeUpdate("DROP TABLE IF EXISTS STUDENT_COURSE_ENROLLMENT");
            stmt.executeUpdate("DROP TABLE IF EXISTS FACULTY_COURSE");
            stmt.executeUpdate("DROP TABLE IF EXISTS COURSE");
            stmt.executeUpdate("DROP TABLE IF EXISTS FACULTY");
            stmt.executeUpdate("DROP TABLE IF EXISTS STUDENT");

            // Creating STUDENT table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS STUDENT (" +
                    "StudentID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "StudentName VARCHAR(100) NOT NULL, " +
                    "PasswordHash VARCHAR(255) NOT NULL)");

            // Creating FACULTY table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FACULTY (" +
                    "FacultyID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "FacultyName VARCHAR(100) NOT NULL)");

            // Create the COURSE table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS COURSE (" +
                    "CourseID INT PRIMARY KEY AUTO_INCREMENT, " + // Changed to AUTO_INCREMENT
                    "CourseName VARCHAR(255) NOT NULL, " +
                    "CourseDescription TEXT, " +
                    "CompletionTime VARCHAR(50), " +
                    "Resources TEXT, " +
                    "FacultyID INT, " +
                    "Topics TEXT, " +
                    "FOREIGN KEY (FacultyID) REFERENCES FACULTY(FacultyID) ON DELETE SET NULL)");

            // Creating STUDENT_COURSE_ENROLLMENT table (Many-to-Many)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS STUDENT_COURSE_ENROLLMENT (" +
                    "StudentID INT, " +
                    "CourseID INT, " +
                    "PRIMARY KEY (StudentID, CourseID), " +
                    "FOREIGN KEY (StudentID) REFERENCES STUDENT(StudentID) ON DELETE CASCADE, " +
                    "FOREIGN KEY (CourseID) REFERENCES COURSE(CourseID) ON DELETE CASCADE)");

            // Creating FACULTY_COURSE table (Many-to-Many)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FACULTY_COURSE (" +
                    "FacultyID INT, " +
                    "CourseID INT, " +
                    "PRIMARY KEY (FacultyID, CourseID), " +
                    "FOREIGN KEY (FacultyID) REFERENCES FACULTY(FacultyID) ON DELETE CASCADE, " +
                    "FOREIGN KEY (CourseID) REFERENCES COURSE(CourseID) ON DELETE CASCADE)");

            // Creating QUIZ_QUESTION table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS QUIZ_QUESTION (" +
                    "QuestionID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "CourseID INT, " +
                    "QuestionDescription TEXT NOT NULL, " +
                    "CorrectAnswer VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (CourseID) REFERENCES COURSE(CourseID) ON DELETE CASCADE)");

            // Creating QUIZ_OPTIONS table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS QUIZ_OPTIONS (" +
                    "OptionID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "QuestionID INT, " +
                    "OptionText TEXT NOT NULL, " +
                    "FOREIGN KEY (QuestionID) REFERENCES QUIZ_QUESTION(QuestionID) ON DELETE CASCADE)");

            System.out.println("Database setup completed successfully.");
        } catch (SQLException e) {
            System.err.println("Database setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}