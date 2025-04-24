package gui;

import db.DatabaseHelper;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.List;

// Custom rounded border class
class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(c.getForeground()); // Border color same as foreground
        g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.bottom = insets.top = this.radius + 1;
        return insets;
    }
}

public class CourseDashboard extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238); // Light gray
    private static final Color SIDEBAR_COLOR = new Color(245, 245, 245); // Slightly lighter gray
    private static final Color BUTTON_COLOR = new Color(220, 220, 220); // Light gray button
    private static final Color BUTTON_HOVER_COLOR = new Color(200, 200, 200);
    private static final Color CARD_COLOR = new Color(153, 153, 255); // Original purple
    private static final Font PRIMARY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public CourseDashboard(int studentId) {
        setTitle("Course Dashboard");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createSidebar(studentId), BorderLayout.WEST);
        mainPanel.add(createCoursePanel(studentId), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar(int studentId) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menuItems = {"Subject Matter", "Tasks", "Teachers", "Exam", "Challenges", "Settings"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            styleSidebarButton(button);
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JButton addCourseButton = new JButton("Add Course");
        styleSidebarButton(addCourseButton);
        addCourseButton.addActionListener(e -> openAddCourseWindow(studentId));
        sidebar.add(addCourseButton);
        sidebar.add(Box.createVerticalGlue()); // Push buttons to the top

        return sidebar;
    }

    private void styleSidebarButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setForeground(Color.BLACK);
        button.setBackground(BUTTON_COLOR);
        button.setFont(PRIMARY_FONT);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(10));

        button.addMouseListener(new MouseAdapter() {
            private Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                originalColor = button.getBackground();
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void openAddCourseWindow(int studentId) {
        JDialog addCourseDialog = new JDialog(this, "Add Course", true);
        addCourseDialog.setSize(300, 200);
        addCourseDialog.setLayout(new BorderLayout());
        addCourseDialog.setLocationRelativeTo(this);
        addCourseDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        try {
            List<String> courseList = DatabaseHelper.getAllCourses();
            String[] courses = courseList.toArray(new String[0]);

            JComboBox<String> courseDropdown = new JComboBox<>(courses);
            courseDropdown.setBackground(Color.WHITE);
            courseDropdown.setFont(PRIMARY_FONT);

            JLabel label = new JLabel("Select Course:");
            label.setForeground(Color.BLACK);
            label.setFont(PRIMARY_FONT);
            panel.add(label);
            panel.add(courseDropdown);

            JButton addButton = new JButton("Add");
            addButton.setFont(PRIMARY_FONT);
            addButton.setBackground(BUTTON_COLOR);
            addButton.setForeground(Color.BLACK);
            addButton.setFocusPainted(false);
            addButton.setBorder(new RoundedBorder(8));
            addButton.addMouseListener(new MouseAdapter() {
                private Color originalColor = addButton.getBackground();

                @Override
                public void mouseEntered(MouseEvent e) {
                    originalColor = addButton.getBackground();
                    addButton.setBackground(BUTTON_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    addButton.setBackground(originalColor);
                }
            });
            addButton.addActionListener(e -> {
                try {
                    String selectedCourse = (String) courseDropdown.getSelectedItem();
                    int courseId = DatabaseHelper.getCourseIdByName(selectedCourse);

                    if (courseId != -1) {
                        boolean success = DatabaseHelper.enrollStudentInCourse(studentId, courseId);
                        JOptionPane.showMessageDialog(addCourseDialog,
                                success ? "Course Added Successfully!" : "Failed to Add Course!",
                                "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(addCourseDialog,
                                "Course not found!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    addCourseDialog.dispose();
                    refreshDashboard(studentId);
                } catch (SQLException ex) {
                    ex.printStackTrace(); // optional: for debugging
                    JOptionPane.showMessageDialog(addCourseDialog,
                            "Database error occurred!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(addButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

            addCourseDialog.add(panel, BorderLayout.CENTER);
            addCourseDialog.add(buttonPanel, BorderLayout.SOUTH);
            addCourseDialog.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(addCourseDialog, "Error fetching courses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCoursePanel(int studentId) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(BACKGROUND_COLOR);

        JPanel coursePanel = new JPanel();
        coursePanel.setBackground(BACKGROUND_COLOR);
        coursePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            List<String[]> courses = DatabaseHelper.getCoursesForStudent(studentId);
            int courseCount = courses.size();
            int columns = 4;
            if (courseCount <= 2) {
                columns = 2;
            } else if (courseCount == 3) {
                columns = 3;
            }
            int rows = (int) Math.ceil(courseCount / (double) columns);

            coursePanel.setLayout(new GridLayout(rows, columns, 15, 15));
            // We don't need to set a fixed preferred size for coursePanel anymore,
            // as the JScrollPane will handle the scrolling based on its content.

            for (int i = 0; i < courseCount; i++) {
                String[] courseData = courses.get(i);
                String courseName = courseData[0];

                JButton cardButton = new JButton("<html><center><b style='font-size:14px;'>" + courseName + "</b></center></html>");
                cardButton.setPreferredSize(new Dimension(180, 100));
                cardButton.setBackground(CARD_COLOR);
                cardButton.setForeground(Color.WHITE);
                cardButton.setFont(BOLD_FONT.deriveFont(Font.BOLD, 14));
                cardButton.setFocusPainted(false);
                cardButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                cardButton.addMouseListener(new MouseAdapter() {
                    private Color originalColor = cardButton.getBackground();

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        originalColor = cardButton.getBackground();
                        cardButton.setBackground(cardButton.getBackground().darker());
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        cardButton.setBackground(originalColor);
                    }
                });
                cardButton.addActionListener(e -> openCoursePage(courseName, studentId));
                coursePanel.add(cardButton);
            }

            JScrollPane scrollPane = new JScrollPane(coursePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(900, 450)); // Set a preferred size for the scroll pane itself
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            outerPanel.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return outerPanel;
    }

    private void openCoursePage(String courseName, int studentId) {
        try {
            int courseId = DatabaseHelper.getCourseIdByName(courseName);

            if (courseId == -1) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String description = DatabaseHelper.getCourseDescription(courseId);
            String teacher = DatabaseHelper.getTeacherName(courseId);
            int completionTime = DatabaseHelper.getEstimatedCompletionTime(courseId);
            List<String> resources = DatabaseHelper.getResourcesForCourse(courseId);
            List<String[]> topics = DatabaseHelper.getTopicsForCourse(courseId);

            SwingUtilities.invokeLater(() ->
                    new CoursePage(courseName, studentId, description, teacher, completionTime, resources, topics).setVisible(true));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error opening course: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshDashboard(int studentId) {
        dispose();
        new CourseDashboard(studentId).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseDashboard(1).setVisible(true));
    }
}