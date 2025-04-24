package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuizSeeder {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL. Seeding quiz data...");

            // Clear existing quiz data to avoid duplicates
            stmt.executeUpdate("DELETE FROM QUIZ_OPTIONS");
            stmt.executeUpdate("DELETE FROM QUIZ_QUESTION");

            // Seed quiz questions and options
            insertQuizData(conn);

            System.out.println("Quiz seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Quiz seeding failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertQuizData(Connection conn) throws SQLException {
        String sqlQuestion = "INSERT INTO QUIZ_QUESTION (CourseID, QuestionDescription, CorrectAnswer) VALUES (?, ?, ?)";
        String sqlOption = "INSERT INTO QUIZ_OPTIONS (QuestionID, OptionText) VALUES (?, ?)";

        try (PreparedStatement stmtQuestion = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtOption = conn.prepareStatement(sqlOption)) {

            // Mathematics Course (CourseID 1)

            // Question 1
            stmtQuestion.setInt(1, 1);
            stmtQuestion.setString(2, "What is the derivative of x^2?");
            stmtQuestion.setString(3, "2x");
            stmtQuestion.executeUpdate();
            int q1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, q1); stmtOption.setString(2, "2x"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q1); stmtOption.setString(2, "x^2"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q1); stmtOption.setString(2, "x"); stmtOption.executeUpdate();

            // Question 2
            stmtQuestion.setInt(1, 1);
            stmtQuestion.setString(2, "What is the value of π (pi) approximately?");
            stmtQuestion.setString(3, "3.14");
            stmtQuestion.executeUpdate();
            int q2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, q2); stmtOption.setString(2, "2.71"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q2); stmtOption.setString(2, "3.14"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q2); stmtOption.setString(2, "1.41"); stmtOption.executeUpdate();

            // Question 3
            stmtQuestion.setInt(1, 1);
            stmtQuestion.setString(2, "If sin(θ) = 0.5, what is θ in degrees?");
            stmtQuestion.setString(3, "30");
            stmtQuestion.executeUpdate();
            int q3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, q3); stmtOption.setString(2, "30"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q3); stmtOption.setString(2, "45"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q3); stmtOption.setString(2, "60"); stmtOption.executeUpdate();

            // Question 4
            stmtQuestion.setInt(1, 1);
            stmtQuestion.setString(2, "What is the integral of 1/x?");
            stmtQuestion.setString(3, "ln|x| + C");
            stmtQuestion.executeUpdate();
            int q4 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, q4); stmtOption.setString(2, "ln|x| + C"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q4); stmtOption.setString(2, "1/x + C"); stmtOption.executeUpdate();
            stmtOption.setInt(1, q4); stmtOption.setString(2, "x^2 + C"); stmtOption.executeUpdate();

            // Computer Science Course (CourseID 2) — untouched
         // Computer Science (CourseID 2) - Add 2 more questions
            stmtQuestion.setInt(1, 2);
            stmtQuestion.setString(2, "Which data structure uses LIFO?");
            stmtQuestion.setString(3, "Stack");
            stmtQuestion.executeUpdate();
            int csQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, csQ2); stmtOption.setString(2, "Stack"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csQ2); stmtOption.setString(2, "Queue"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csQ2); stmtOption.setString(2, "Graph"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 2);
            stmtQuestion.setString(2, "What is the time complexity of binary search?");
            stmtQuestion.setString(3, "O(log n)");
            stmtQuestion.executeUpdate();
            int csQ3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, csQ3); stmtOption.setString(2, "O(log n)"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csQ3); stmtOption.setString(2, "O(n)"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csQ3); stmtOption.setString(2, "O(n^2)"); stmtOption.executeUpdate();
            
            stmtQuestion.setInt(1, 2);
            stmtQuestion.setString(2, "Which data structure uses FIFO (First In First Out)?");
            stmtQuestion.setString(3, "Queue");
            stmtQuestion.executeUpdate();
            int csq2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, csq2); stmtOption.setString(2, "Stack"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csq2); stmtOption.setString(2, "Queue"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csq2); stmtOption.setString(2, "Tree"); stmtOption.executeUpdate();

            // Physics (CourseID 3)
            stmtQuestion.setInt(1, 3);
            stmtQuestion.setString(2, "What is Newton’s second law?");
            stmtQuestion.setString(3, "F = ma");
            stmtQuestion.executeUpdate();
            int phyQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, phyQ1); stmtOption.setString(2, "F = ma"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyQ1); stmtOption.setString(2, "E = mc^2"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyQ1); stmtOption.setString(2, "V = IR"); stmtOption.executeUpdate();
            
         // Physics - Question 1
            stmtQuestion.setInt(1, 3);
            stmtQuestion.setString(2, "What is the unit of electrical resistance?");
            stmtQuestion.setString(3, "Ohm");
            stmtQuestion.executeUpdate();
            int phyq1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, phyq1); stmtOption.setString(2, "Ohm"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq1); stmtOption.setString(2, "Volt"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq1); stmtOption.setString(2, "Ampere"); stmtOption.executeUpdate();

            // Physics - Question 2
            stmtQuestion.setInt(1, 3);
            stmtQuestion.setString(2, "What is the acceleration due to gravity on Earth?");
            stmtQuestion.setString(3, "9.8 m/s^2");
            stmtQuestion.executeUpdate();
            int phyq2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, phyq2); stmtOption.setString(2, "9.8 m/s^2"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq2); stmtOption.setString(2, "10 m/s^2"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq2); stmtOption.setString(2, "8.9 m/s^2"); stmtOption.executeUpdate();

            // Physics - Question 3
            stmtQuestion.setInt(1, 3);
            stmtQuestion.setString(2, "What type of lens converges light?");
            stmtQuestion.setString(3, "Convex");
            stmtQuestion.executeUpdate();
            int phyq3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, phyq3); stmtOption.setString(2, "Concave"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq3); stmtOption.setString(2, "Convex"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyq3); stmtOption.setString(2, "Plane"); stmtOption.executeUpdate();


            // Computer Science - Question 3
            stmtQuestion.setInt(1, 2);
            stmtQuestion.setString(2, "Which keyword is used to inherit a class in Java?");
            stmtQuestion.setString(3, "extends");
            stmtQuestion.executeUpdate();
            int csq3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, csq3); stmtOption.setString(2, "implements"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csq3); stmtOption.setString(2, "extends"); stmtOption.executeUpdate();
            stmtOption.setInt(1, csq3); stmtOption.setString(2, "inherits"); stmtOption.executeUpdate();
            

            stmtQuestion.setInt(1, 3);
            stmtQuestion.setString(2, "What is the unit of electric current?");
            stmtQuestion.setString(3, "Ampere");
            stmtQuestion.executeUpdate();
            int phyQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, phyQ2); stmtOption.setString(2, "Ampere"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyQ2); stmtOption.setString(2, "Volt"); stmtOption.executeUpdate();
            stmtOption.setInt(1, phyQ2); stmtOption.setString(2, "Joule"); stmtOption.executeUpdate();

            // Biology (CourseID 4)
            stmtQuestion.setInt(1, 4);
            stmtQuestion.setString(2, "What is the powerhouse of the cell?");
            stmtQuestion.setString(3, "Mitochondria");
            stmtQuestion.executeUpdate();
            int bioQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, bioQ1); stmtOption.setString(2, "Mitochondria"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioQ1); stmtOption.setString(2, "Nucleus"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioQ1); stmtOption.setString(2, "Ribosome"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 4);
            stmtQuestion.setString(2, "DNA stands for?");
            stmtQuestion.setString(3, "Deoxyribonucleic Acid");
            stmtQuestion.executeUpdate();
            int bioQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, bioQ2); stmtOption.setString(2, "Deoxyribonucleic Acid"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioQ2); stmtOption.setString(2, "Dual Nucleotide Acid"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioQ2); stmtOption.setString(2, "Deoxyribose Nucleic Amino"); stmtOption.executeUpdate();

            // Biology - Question 2
            stmtQuestion.setInt(1, 4);
            stmtQuestion.setString(2, "What is the basic unit of life?");
            stmtQuestion.setString(3, "Cell");
            stmtQuestion.executeUpdate();
            int bioq2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, bioq2); stmtOption.setString(2, "Tissue"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioq2); stmtOption.setString(2, "Organ"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioq2); stmtOption.setString(2, "Cell"); stmtOption.executeUpdate();

            // Biology - Question 3
            stmtQuestion.setInt(1, 4);
            stmtQuestion.setString(2, "Which organ is responsible for pumping blood?");
            stmtQuestion.setString(3, "Heart");
            stmtQuestion.executeUpdate();
            int bioq3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, bioq3); stmtOption.setString(2, "Liver"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioq3); stmtOption.setString(2, "Heart"); stmtOption.executeUpdate();
            stmtOption.setInt(1, bioq3); stmtOption.setString(2, "Lungs"); stmtOption.executeUpdate();

            
            // Machine Learning (CourseID 5)
            stmtQuestion.setInt(1, 5);
            stmtQuestion.setString(2, "What is overfitting?");
            stmtQuestion.setString(3, "Model performs well on training but poorly on test data");
            stmtQuestion.executeUpdate();
            int mlQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, mlQ1); stmtOption.setString(2, "Model performs well on training but poorly on test data"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ1); stmtOption.setString(2, "Model performs well on all data"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ1); stmtOption.setString(2, "Model only works for linear data"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 5);
            stmtQuestion.setString(2, "Which algorithm is used for classification?");
            stmtQuestion.setString(3, "Logistic Regression");
            stmtQuestion.executeUpdate();
            int mlQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, mlQ2); stmtOption.setString(2, "Logistic Regression"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ2); stmtOption.setString(2, "Linear Regression"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ2); stmtOption.setString(2, "K-means"); stmtOption.executeUpdate();
            
         // Machine Learning - Question 3
            stmtQuestion.setInt(1, 5);
            stmtQuestion.setString(2, "Which metric is commonly used to evaluate the performance of a regression model?");
            stmtQuestion.setString(3, "Mean Squared Error");
            stmtQuestion.executeUpdate();
            int mlQ3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, mlQ3); stmtOption.setString(2, "Accuracy"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ3); stmtOption.setString(2, "Precision"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ3); stmtOption.setString(2, "Mean Squared Error"); stmtOption.executeUpdate();
            
         // Machine Learning - Question 4
            stmtQuestion.setInt(1, 5);
            stmtQuestion.setString(2, "What is the main goal of unsupervised learning?");
            stmtQuestion.setString(3, "To find patterns in unlabeled data");
            stmtQuestion.executeUpdate();
            int mlQ4 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, mlQ4); stmtOption.setString(2, "To predict future outcomes based on labeled data"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ4); stmtOption.setString(2, "To classify data into predefined categories"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ4); stmtOption.setString(2, "To find patterns in unlabeled data"); stmtOption.executeUpdate();

            // Machine Learning - Question 5
            stmtQuestion.setInt(1, 5);
            stmtQuestion.setString(2, "Which of the following is a common dimensionality reduction technique?");
            stmtQuestion.setString(3, "Principal Component Analysis");
            stmtQuestion.executeUpdate();
            int mlQ5 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, mlQ5); stmtOption.setString(2, "Gradient Descent"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ5); stmtOption.setString(2, "Cross-Validation"); stmtOption.executeUpdate();
            stmtOption.setInt(1, mlQ5); stmtOption.setString(2, "Principal Component Analysis"); stmtOption.executeUpdate();
            
            // Software Engineering (CourseID 6)
            stmtQuestion.setInt(1, 6);
            stmtQuestion.setString(2, "Agile methodology promotes?");
            stmtQuestion.setString(3, "Iterative development");
            stmtQuestion.executeUpdate();
            int seQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, seQ1); stmtOption.setString(2, "Iterative development"); stmtOption.executeUpdate();
            stmtOption.setInt(1, seQ1); stmtOption.setString(2, "Waterfall approach"); stmtOption.executeUpdate();
            stmtOption.setInt(1, seQ1); stmtOption.setString(2, "No testing"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 6);
            stmtQuestion.setString(2, "What is version control used for?");
            stmtQuestion.setString(3, "Track code changes");
            stmtQuestion.executeUpdate();
            int seQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, seQ2); stmtOption.setString(2, "Track code changes"); stmtOption.executeUpdate();
            stmtOption.setInt(1, seQ2); stmtOption.setString(2, "Compile code"); stmtOption.executeUpdate();
            stmtOption.setInt(1, seQ2); stmtOption.setString(2, "Manage databases"); stmtOption.executeUpdate();

            // Data Science (CourseID 7)
            stmtQuestion.setInt(1, 7);
            stmtQuestion.setString(2, "What is the first step in the data science process?");
            stmtQuestion.setString(3, "Data Collection");
            stmtQuestion.executeUpdate();
            int dsQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, dsQ1); stmtOption.setString(2, "Data Collection"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ1); stmtOption.setString(2, "Modeling"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ1); stmtOption.setString(2, "Deployment"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 7);
            stmtQuestion.setString(2, "What does EDA stand for?");
            stmtQuestion.setString(3, "Exploratory Data Analysis");
            stmtQuestion.executeUpdate();
            int dsQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, dsQ2); stmtOption.setString(2, "Exploratory Data Analysis"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ2); stmtOption.setString(2, "Experimental Data Access"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ2); stmtOption.setString(2, "Effective Data Assignment"); stmtOption.executeUpdate();
            
         // Data Science - Question 5
            stmtQuestion.setInt(1, 7);
            stmtQuestion.setString(2, "Which of the following is a common library in Python used for data manipulation and analysis?");
            stmtQuestion.setString(3, "Pandas");
            stmtQuestion.executeUpdate();
            int dsQ5 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, dsQ5); stmtOption.setString(2, "TensorFlow"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ5); stmtOption.setString(2, "Pandas"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ5); stmtOption.setString(2, "PyTorch"); stmtOption.executeUpdate();

            // Data Science - Question 6
            stmtQuestion.setInt(1, 7);
            stmtQuestion.setString(2, "What type of plot is useful for visualizing the distribution of a single numerical variable?");
            stmtQuestion.setString(3, "Histogram");
            stmtQuestion.executeUpdate();
            int dsQ6 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, dsQ6); stmtOption.setString(2, "Scatter plot"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ6); stmtOption.setString(2, "Bar chart"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ6); stmtOption.setString(2, "Histogram"); stmtOption.executeUpdate();

            // Data Science - Question 7
            stmtQuestion.setInt(1, 7);
            stmtQuestion.setString(2, "What is the purpose of feature scaling in machine learning?");
            stmtQuestion.setString(3, "To normalize the range of independent variables");
            stmtQuestion.executeUpdate();
            int dsQ7 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, dsQ7); stmtOption.setString(2, "To reduce the number of features"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ7); stmtOption.setString(2, "To normalize the range of independent variables"); stmtOption.executeUpdate();
            stmtOption.setInt(1, dsQ7); stmtOption.setString(2, "To improve the accuracy of the model"); stmtOption.executeUpdate();
            
            // Cybersecurity (CourseID 8)
            stmtQuestion.setInt(1, 8);
            stmtQuestion.setString(2, "What does HTTPS stand for?");
            stmtQuestion.setString(3, "HyperText Transfer Protocol Secure");
            stmtQuestion.executeUpdate();
            int cyberQ1 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, cyberQ1); stmtOption.setString(2, "HyperText Transfer Protocol Secure"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ1); stmtOption.setString(2, "Host Text Transfer Protocol Secure"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ1); stmtOption.setString(2, "High Transfer Protocol Service"); stmtOption.executeUpdate();

            stmtQuestion.setInt(1, 8);
            stmtQuestion.setString(2, "What is the purpose of encryption?");
            stmtQuestion.setString(3, "To protect data from unauthorized access");
            stmtQuestion.executeUpdate();
            int cyberQ2 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, cyberQ2); stmtOption.setString(2, "To protect data from unauthorized access"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ2); stmtOption.setString(2, "To compress files"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ2); stmtOption.setString(2, "To increase bandwidth"); stmtOption.executeUpdate();
            
         // Cybersecurity - Question 3
            stmtQuestion.setInt(1, 8);
            stmtQuestion.setString(2, "What type of attack involves flooding a target server with a large amount of traffic to disrupt its services?");
            stmtQuestion.setString(3, "Denial of Service (DoS)");
            stmtQuestion.executeUpdate();
            int cyberQ3 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, cyberQ3); stmtOption.setString(2, "Phishing"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ3); stmtOption.setString(2, "Malware injection"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ3); stmtOption.setString(2, "Denial of Service (DoS)"); stmtOption.executeUpdate();

            // Cybersecurity - Question 4
            stmtQuestion.setInt(1, 8);
            stmtQuestion.setString(2, "What is a firewall primarily used for in network security?");
            stmtQuestion.setString(3, "To control incoming and outgoing network traffic");
            stmtQuestion.executeUpdate();
            int cyberQ4 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, cyberQ4); stmtOption.setString(2, "To encrypt network communications"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ4); stmtOption.setString(2, "To detect and remove malware"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ4); stmtOption.setString(2, "To control incoming and outgoing network traffic"); stmtOption.executeUpdate();

            // Cybersecurity - Question 5
            stmtQuestion.setInt(1, 8);
            stmtQuestion.setString(2, "What is the practice of deceiving individuals into revealing sensitive information called?");
            stmtQuestion.setString(3, "Social Engineering");
            stmtQuestion.executeUpdate();
            int cyberQ5 = getGeneratedKey(stmtQuestion);

            stmtOption.setInt(1, cyberQ5); stmtOption.setString(2, "Brute-force attack"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ5); stmtOption.setString(2, "SQL injection"); stmtOption.executeUpdate();
            stmtOption.setInt(1, cyberQ5); stmtOption.setString(2, "Social Engineering"); stmtOption.executeUpdate();
            
        }
    }


    private static int getGeneratedKey(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }
}