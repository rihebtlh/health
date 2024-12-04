import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Notifications extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");
    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthSync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Constructor doesn't store userId anymore
    public Notifications() {
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 871, 609);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 10, 837, 76);
        contentPane.add(panel);
        panel.setLayout(null);

        // Panel with buttons
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(168, 6, 527, 60);
        panel.add(panel_1);
        panel_1.setLayout(null);

        // Add home button
        JButton homeButton = new JButton("HOME");
        homeButton.setBackground(Color.WHITE);
        homeButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        homeButton.setBounds(37, 9, 85, 41);
        homeButton.setBorderPainted(false);
        panel_1.add(homeButton);

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
                dispose();
            }
        });

        // Add notifications button
        JButton notificationsButton = new JButton("NOTIFICATIONS");
        notificationsButton.setBackground(Color.WHITE);
        notificationsButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        notificationsButton.setBounds(160, 11, 163, 40);
        notificationsButton.setBorderPainted(false);
        panel_1.add(notificationsButton);

        notificationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Notifications notificationPage = new Notifications(); // No need to pass userId
                notificationPage.setVisible(true);
                dispose();
            }
        });

        // Add history button
        JButton historyButton = new JButton("MEDICAL HISTORY");
        historyButton.setBackground(Color.WHITE);
        historyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        historyButton.setBounds(315, 12, 183, 40);
        historyButton.setBorderPainted(false);
        panel_1.add(historyButton);

        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MedicalHistory medicalHistoryPage = new MedicalHistory();
                medicalHistoryPage.setVisible(true);
                dispose();
            }
        });

        // Add logout button
        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to log out?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBounds(695, 20, 107, 40);
        panel.add(logoutButton);
        logoutButton.setForeground(new Color(23, 183, 251));
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel logoLabel = new JLabel("");
        logoLabel.setBounds(10, 10, 140, 56);
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(140, 56, java.awt.Image.SCALE_SMOOTH));
        logoLabel.setIcon(resizedImg);
        panel.add(logoLabel);

        // Panel to show notifications
        JPanel panel_11 = new JPanel();
        panel_11.setBackground(new Color(228, 228, 228));
        panel_11.setBounds(130, 212, 635, 315);
        contentPane.add(panel_11);

        JLabel lblNewLabel = new JLabel("Notifications");
        lblNewLabel.setForeground(new Color(53, 149, 196));
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblNewLabel.setBounds(130, 162, 196, 40);
        contentPane.add(lblNewLabel);

        // Fetch notifications for user
        int loggedInUserId = UserSession.getUserId(); // Get the user ID from UserSession
        ArrayList<String> notifications = fetchNotifications(loggedInUserId);
        panel_11.setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(311, 15, 2, 2);
        panel_11.add(scrollPane);

        // Create a JTextArea to display notifications
        JTextArea notificationArea = new JTextArea();
        notificationArea.setBounds(137, 10, 375, 295);
        panel_11.add(notificationArea);
        notificationArea.setEditable(false);
        notificationArea.setText(String.join("\n\n", notifications));
    }

    private ArrayList<String> fetchNotifications(int userId) {
        ArrayList<String> notifications = new ArrayList<>();
        String query = "SELECT message, date FROM notifications WHERE id = ? ORDER BY date DESC";
        System.out.println("Fetching notifications for user ID: " + userId);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String message = rs.getString("message");
                String date = rs.getString("date");
                notifications.add(message + " (Date: " + date + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching notifications.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return notifications;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Create Notifications frame without passing userId, as it's fetched from UserSession
                    Notifications frame = new Notifications();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
