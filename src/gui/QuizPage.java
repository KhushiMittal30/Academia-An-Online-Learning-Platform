package gui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizPage extends JFrame {
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private int score = 0;

    private JLabel questionLabel;
    private ButtonGroup optionGroup;
    private JPanel optionsPanel;
    private JButton nextButton;

    // --- THEME COLORS ---
    private static final Color THEME_PRIMARY_BLUE = new Color(0, 71, 171);
    private static final Color THEME_BACKGROUND = new Color(245, 248, 250);
    private static final Color THEME_WHITE = Color.WHITE;
    private static final Color THEME_TEXT_DARK = new Color(40, 40, 40);
    private static final Color THEME_TEXT_LIGHT = Color.WHITE;
    private static final Color THEME_BORDER_COLOR = new Color(210, 220, 230);

    // --- THEME FONTS ---
    private static final Font FONT_QUESTION = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_OPTION = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);

    // --- THEME BORDER ---
    private static final Border OPTION_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(THEME_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
    );

    // Inner class to hold question data
    private static class Question {
        String description;
        List<String> options;
        int correctOptionIndex;

        Question(String description, List<String> options, int correctOptionIndex) {
            this.description = description;
            this.options = options;
            this.correctOptionIndex = correctOptionIndex;
        }
    }

    public QuizPage(int courseId) {
        setTitle("Quiz for Course ID: " + courseId);
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(THEME_BACKGROUND);

        questions = new ArrayList<>();
        loadQuestions(courseId);

        // --- UI Components ---
        questionLabel = new JLabel("Loading quiz...");
        questionLabel.setFont(FONT_QUESTION);
        questionLabel.setForeground(THEME_TEXT_DARK);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(THEME_BACKGROUND);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        optionGroup = new ButtonGroup();

        nextButton = new JButton("Next");
        nextButton.setFont(FONT_BUTTON);
        nextButton.setBackground(THEME_PRIMARY_BLUE);
        nextButton.setForeground(THEME_TEXT_LIGHT);
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    loadQuestion(currentQuestionIndex);
                } else {
                    JOptionPane.showMessageDialog(QuizPage.this, "Quiz Completed!\nYour Score: " + score + "/" + questions.size(), "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        });

        // --- Layout ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(THEME_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        optionsScrollPane.setBackground(THEME_BACKGROUND);
        optionsScrollPane.getViewport().setBackground(THEME_BACKGROUND);
        mainPanel.add(optionsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(THEME_BACKGROUND);
        buttonPanel.add(nextButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Load Initial Question ---
        if (!questions.isEmpty()) {
            loadQuestion(0);
        } else {
            questionLabel.setText("No quiz questions available for this course.");
            optionsPanel.removeAll();
            nextButton.setEnabled(false);
            nextButton.setBackground(Color.LIGHT_GRAY);
            optionsPanel.revalidate();
            optionsPanel.repaint();
        }
    }

    private void loadQuestions(int courseId) {
        String sql = "SELECT q.QuestionID, q.QuestionDescription, q.CorrectAnswer " +
                "FROM QUIZ_QUESTION q WHERE q.CourseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int questionId = rs.getInt("QuestionID");
                    String description = rs.getString("QuestionDescription");
                    String correctAnswer = rs.getString("CorrectAnswer");

                    List<String> options = new ArrayList<>();
                    String optionSql = "SELECT OptionText FROM QUIZ_OPTIONS WHERE QuestionID = ? ORDER BY OptionID";
                    try (PreparedStatement optStmt = conn.prepareStatement(optionSql)) {
                        optStmt.setInt(1, questionId);
                        try (ResultSet optRs = optStmt.executeQuery()) {
                            int correctIndex = -1;
                            int index = 0;
                            while (optRs.next()) {
                                String optionText = optRs.getString("OptionText");
                                options.add(optionText);
                                if (optionText.equals(correctAnswer)) {
                                    correctIndex = index;
                                }
                                index++;
                            }
                            if (correctIndex != -1 && !options.isEmpty()) {
                                questions.add(new Question(description, options, correctIndex));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading quiz questions: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading quiz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadQuestion(int index) {
        if (index >= questions.size()) return;

        Question question = questions.get(index);
        questionLabel.setText("<html><div style='text-align: center; padding: 10px;'>"
                + (index + 1) + ". " + question.description + "</div></html>");

        optionsPanel.removeAll();
        optionGroup = new ButtonGroup();

        for (int i = 0; i < question.options.size(); i++) {
            String optionText = question.options.get(i);
            JRadioButton optionButton = new JRadioButton("<html><div style='padding: 5px;'>" + optionText + "</div></html>");
            optionButton.setActionCommand(String.valueOf(i));
            optionButton.setFont(FONT_OPTION);
            optionButton.setBackground(THEME_WHITE);
            optionButton.setForeground(THEME_TEXT_DARK);
            optionButton.setFocusPainted(false);
            optionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel optionWrapperPanel = new JPanel(new BorderLayout());
            optionWrapperPanel.setBackground(THEME_WHITE);
            optionWrapperPanel.setBorder(OPTION_BORDER);
            optionWrapperPanel.add(optionButton, BorderLayout.CENTER);

            optionGroup.add(optionButton);
            optionsPanel.add(optionWrapperPanel);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();

        nextButton.setText(currentQuestionIndex == questions.size() - 1 ? "Finish" : "Next");
    }

    private void checkAnswer() {
        ButtonModel selectedModel = optionGroup.getSelection();
        if (selectedModel != null) {
            int selectedIndex = Integer.parseInt(selectedModel.getActionCommand());
            Question question = questions.get(currentQuestionIndex);
            if (selectedIndex == question.correctOptionIndex) {
                score++;
            }
        }
    }
}