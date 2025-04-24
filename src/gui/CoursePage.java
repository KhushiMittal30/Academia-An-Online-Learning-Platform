package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;

public class CoursePage extends JFrame {
    public CoursePage(String courseName, int studentId, String courseDescription, String teacherName, int completionTime,
                      List<String> resources, List<String[]> topics) {

        setTitle(courseName);
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 255));

        // --- Header Panel (NORTH) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0, 71, 171));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel courseTitleLabel = new JLabel("<html><h2 style='color: white; font-size: 24px;'>" + courseName + "</h2></html>");
        JLabel descriptionLabel = new JLabel("<html><p style='color: #e0e0e0; font-size: 14px;'>" + courseDescription + "</p></html>");

        courseTitleLabel.setForeground(Color.WHITE);
        descriptionLabel.setForeground(new Color(224, 224, 224));

        headerPanel.add(courseTitleLabel);
        headerPanel.add(descriptionLabel);

        // --- Main Content Panel (CENTER) ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 245, 255));

        // Left Info Panel (WEST of CENTER)
        JPanel leftInfoPanel = new JPanel();
        leftInfoPanel.setLayout(new BoxLayout(leftInfoPanel, BoxLayout.Y_AXIS));
        leftInfoPanel.setBackground(new Color(240, 245, 255));
        leftInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 10)); // Add some padding

        JLabel teacherInfoLabel = new JLabel("Instructor: " + teacherName);
        JLabel completionInfoLabel = new JLabel("Estimated Completion: " + completionTime + " hours");

        teacherInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        completionInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        leftInfoPanel.add(teacherInfoLabel);
        leftInfoPanel.add(Box.createVerticalStrut(10));
        leftInfoPanel.add(completionInfoLabel);

        // Right Content Panel (CENTER of CENTER) - Contains Resources and Topics
        JPanel rightContentPanel = new JPanel();
        rightContentPanel.setLayout(new BoxLayout(rightContentPanel, BoxLayout.Y_AXIS));
        rightContentPanel.setBackground(new Color(240, 245, 255));
        rightContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20)); // Add some padding

        // Resources Panel
        JPanel resourcesPanel = new JPanel();
        resourcesPanel.setLayout(new BoxLayout(resourcesPanel, BoxLayout.Y_AXIS));
        resourcesPanel.setBorder(BorderFactory.createTitledBorder("Resources"));
        resourcesPanel.setBackground(Color.WHITE);

        for (String resourceEntry : resources) {
            // Split the resource entry by comma (you might need to adjust the delimiter)
            String[] individualResources = resourceEntry.split(",");
            for (String individualResource : individualResources) {
                String trimmedResource = individualResource.trim(); // Remove leading/trailing whitespace
                JLabel resourceLabel = new JLabel("• " + trimmedResource);
                resourceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                resourcesPanel.add(resourceLabel);
            }
        }
        int resourceHeight = Math.min(resources.size() * 30, 150);
        JScrollPane resourcesScrollPane = new JScrollPane(resourcesPanel);
        resourcesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourcesScrollPane.setPreferredSize(new Dimension(700, resourceHeight)); // Adjust width
        resourcesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        rightContentPanel.add(resourcesScrollPane);
        rightContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Topics Panel
        JPanel topicsPanel = new JPanel();
        topicsPanel.setLayout(new BoxLayout(topicsPanel, BoxLayout.Y_AXIS));
        topicsPanel.setBorder(BorderFactory.createTitledBorder("Course Topics"));
        topicsPanel.setBackground(new Color(250, 250, 250));
        for (String[] topicData : topics) {
            String topicName = topicData[0];
            String subtopics = topicData[1];
            JPanel topicCard = new JPanel(new BorderLayout());
            topicCard.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            topicCard.setBackground(new Color(200, 220, 255));
            topicCard.setPreferredSize(new Dimension(700, 90)); // Adjust width
            String topicHtml = "<html><h3 style='color: navy;'>" + topicName + "</h3>";
            if (!subtopics.isEmpty()) {
                topicHtml += "<p style='color: black;'>" + subtopics + "</p>";
            }
            topicHtml += "</html>";
            JLabel topicLabel = new JLabel(topicHtml);
            topicLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            topicCard.add(topicLabel, BorderLayout.CENTER);
            topicsPanel.add(topicCard);
            topicsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        JScrollPane topicsScrollPane = new JScrollPane(topicsPanel);
        topicsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        topicsScrollPane.setPreferredSize(new Dimension(700, Math.min(topics.size() * 100, 300))); // Adjust width
        topicsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        rightContentPanel.add(topicsScrollPane);

        contentPanel.add(leftInfoPanel, BorderLayout.WEST);
        contentPanel.add(rightContentPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // --- Bottom Button Panel (SOUTH) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 245, 255));
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());
        JButton quizButton = new JButton("Attempt Quiz");
        quizButton.setFont(new Font("Arial", Font.BOLD, 14));
        quizButton.setBackground(new Color(0, 153, 76));
        quizButton.setForeground(Color.WHITE);
        quizButton.setFocusPainted(false);
        quizButton.addActionListener(e -> {
            try {
                int courseId = db.DatabaseHelper.getCourseIdByName(courseName);
                if (courseId != -1) {
                    SwingUtilities.invokeLater(() -> new QuizPage(courseId).setVisible(true));
                } else {
                    JOptionPane.showMessageDialog(this, "Course ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching course ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(backButton);
        buttonPanel.add(quizButton);

        // --- Improve Scrollbar Appearance ---
        UIManager.put("ScrollBar.thumb", new Color(150, 150, 150));
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(120, 120, 120));
        UIManager.put("ScrollBar.thumbHighlight", new Color(180, 180, 180));
        UIManager.put("ScrollBar.thumbShadow", new Color(120, 120, 120));
        UIManager.put("ScrollBar.track", new Color(220, 220, 220));
        UIManager.put("ScrollBar.trackBackground", new Color(220, 220, 220));

        // Add components to mainPanel using BorderLayout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Final fix: force scroll to top
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
}