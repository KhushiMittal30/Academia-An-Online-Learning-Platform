package gui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(c.getForeground());
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

public class LoginPage {
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    private static final Color PRIMARY_COLOR = new Color(66, 133, 244);
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // If  is not available, you can try "Metal" or the system's L&F
            if (UIManager.getLookAndFeel().getName().equals("Metal")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
            // If L&F cannot be set, proceed with the default
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    static class LoginGUI extends JFrame {
        private JTextField studentIdField;
        private JPasswordField passwordField;

        public LoginGUI() {
            setTitle("Academia Portal");
            setSize(500, 500); // Adjusted size for better layout
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            getContentPane().setBackground(BACKGROUND_COLOR);

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(BACKGROUND_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Added padding

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER; // Make components span across

            JLabel titleLabel = new JLabel("Academia Portal", SwingConstants.CENTER);
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(PRIMARY_COLOR);
            gbc.gridy = 0;
            panel.add(titleLabel, gbc);

            gbc.insets = new Insets(20, 10, 5, 10);
            JLabel idLabel = new JLabel("Student ID:");
            idLabel.setFont(LABEL_FONT);
            idLabel.setForeground(TEXT_COLOR);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST; // Align label to the left
            panel.add(idLabel, gbc);

            studentIdField = new JTextField(15);
            studentIdField.setFont(TEXT_FIELD_FONT);
            Border thickerBorder = BorderFactory.createLineBorder(TEXT_COLOR, 2);
            studentIdField.setBorder(BorderFactory.createCompoundBorder(
                    thickerBorder,
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            gbc.gridy = 2;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(studentIdField, gbc);

            gbc.insets = new Insets(10, 10, 5, 10);
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setFont(LABEL_FONT);
            passwordLabel.setForeground(TEXT_COLOR);
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(passwordLabel, gbc);

            passwordField = new JPasswordField(15);
            passwordField.setFont(TEXT_FIELD_FONT);
            Border thickerPasswordBorder = BorderFactory.createLineBorder(TEXT_COLOR, 2);
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                    thickerPasswordBorder,
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            gbc.gridy = 4;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(passwordField, gbc);

            gbc.insets = new Insets(25, 10, 10, 10);
            JButton loginButton = new JButton("Login");
            loginButton.setFont(BUTTON_FONT);
            loginButton.setBackground(PRIMARY_COLOR);
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);
            loginButton.setBorder(new RoundedBorder(10));
            loginButton.addActionListener(new LoginAction());
            gbc.gridy = 5;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(loginButton, gbc);

            add(panel, BorderLayout.CENTER);
        }

        class LoginAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String studentIdText = studentIdField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (!studentIdText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid ID! Use numbers (e.g., 1, 2, 3).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int studentId = Integer.parseInt(studentIdText);
                if (authenticateUser(studentId, password)) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login Successful");
                    dispose();
                    new CourseDashboard(studentId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean authenticateUser(int studentId, String password) {
            String query = "SELECT PasswordHash FROM STUDENT WHERE StudentID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = DatabaseConnection.hashPassword(password);
                        return hashedPassword != null && hashedPassword.equals(rs.getString("PasswordHash"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}