package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper {

    public static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    public static List<String> getAllCourses() throws SQLException {
        List<String> courses = new ArrayList<>();
        String query = "SELECT CourseName FROM COURSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courses.add(rs.getString("CourseName"));
            }
        }
        return courses;
    }

    public static int getCourseIdByName(String courseName) throws SQLException {
        String query = "SELECT CourseID FROM COURSE WHERE CourseName = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CourseID");
                }
            }
        }
        return -1;
    }

    public static boolean enrollStudentInCourse(int studentId, int courseId) throws SQLException {
        String query = "INSERT INTO STUDENT_COURSE_ENROLLMENT (StudentID, CourseID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<String[]> getCoursesForStudent(int studentId) throws SQLException {
        List<String[]> courses = new ArrayList<>();
        String query = "SELECT c.CourseName FROM COURSE c JOIN STUDENT_COURSE_ENROLLMENT sce ON c.CourseID = sce.CourseID WHERE sce.StudentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(new String[]{rs.getString("CourseName")});
                }
            }
        }
        return courses;
    }

    public static String getCourseDescription(int courseId) throws SQLException {
        String query = "SELECT CourseDescription FROM COURSE WHERE CourseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("CourseDescription");
                }
            }
        }
        return "";
    }

    public static String getTeacherName(int courseId) throws SQLException {
        String query = "SELECT f.FacultyName FROM FACULTY f JOIN COURSE c ON f.FacultyID = c.FacultyID WHERE c.CourseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("FacultyName");
                }
            }
        }
        return "Unknown Instructor";
    }

    public static int getEstimatedCompletionTime(int courseId) throws SQLException {
        String query = "SELECT CompletionTime FROM COURSE WHERE CourseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    try {
                        return Integer.parseInt(rs.getString("CompletionTime"));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public static List<String> getResourcesForCourse(int courseId) throws SQLException {
        String query = "SELECT Resources FROM COURSE WHERE CourseID = ?";
        List<String> resources = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String raw = rs.getString("Resources");
                    if (raw != null && !raw.trim().isEmpty()) {
                        resources = Arrays.asList(raw.split("\\|")); // Assuming '|' as the delimiter
                    }
                }
            }
        }
        return resources;
    }

    public static List<String[]> getTopicsForCourse(int courseId) throws SQLException {
        String query = "SELECT Topics FROM COURSE WHERE CourseID = ?";
        List<String[]> topics = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String raw = rs.getString("Topics");
                    if (raw != null && !raw.trim().isEmpty()) {
                        String[] topicNames = raw.split(",");
                        for (String topic : topicNames) {
                            topics.add(new String[]{topic.trim(), ""}); // Only topic name, no subtopic
                        }
                    }
                }
            }
        }
        return topics;
    }

}
